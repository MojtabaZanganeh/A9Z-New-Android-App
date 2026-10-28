package com.example.ui.viewmodel

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.CachedData
import com.example.data.model.*
import com.example.data.remote.ApiService
import com.example.data.repository.AppRepository
import com.example.util.AppLanguage
import com.example.util.AppStrings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import java.util.concurrent.TimeUnit

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPrefs = application.getSharedPreferences("a9z_prefs", Context.MODE_PRIVATE)

    private val database = AppDatabase.getDatabase(application)
    private val repository = AppRepository(ApiService.create(), database.dao())

    // Device Fingerprint & Local ID for Windscribe Quota
    private val localDeviceId: String by lazy {
        var saved = sharedPrefs.getString("wsc_local_id", null)
        if (saved.isNullOrBlank()) {
            saved = "a9z_" + UUID.randomUUID().toString().replace("-", "").take(16)
            sharedPrefs.edit().putString("wsc_local_id", saved).apply()
        }
        saved
    }

    private val deviceFingerprint: String by lazy {
        val androidId = runCatching {
            Settings.Secure.getString(application.contentResolver, Settings.Secure.ANDROID_ID)
        }.getOrNull() ?: "unknown"
        "fp_${Build.MANUFACTURER}_${Build.MODEL}_$androidId".replace(" ", "_")
    }

    // Expose cached data
    val cachedData: StateFlow<CachedData?> = repository.cachedDataFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val configHistory = repository.configHistoryFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val proxyHistory = repository.proxyHistoryFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Synchronization States
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Theme State (Persisted in SharedPreferences, defaults to Dark theme)
    private val _isDarkTheme = MutableStateFlow(
        sharedPrefs.getBoolean("app_dark_theme", true).also {
            com.example.ui.theme.isAppDarkTheme = it
        }
    )
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    fun toggleTheme() {
        val next = !_isDarkTheme.value
        _isDarkTheme.value = next
        com.example.ui.theme.isAppDarkTheme = next
        sharedPrefs.edit().putBoolean("app_dark_theme", next).apply()
    }

    // Language State (Persisted in SharedPreferences)
    private val _currentLanguage = MutableStateFlow(
        if (sharedPrefs.getString("app_lang", "fa") == "en") AppLanguage.ENGLISH
        else AppLanguage.PERSIAN
    )
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    fun toggleLanguage() {
        val next = if (_currentLanguage.value == AppLanguage.PERSIAN) {
            AppLanguage.ENGLISH
        } else {
            AppLanguage.PERSIAN
        }
        _currentLanguage.value = next
        sharedPrefs.edit().putString("app_lang", next.code).apply()

        if (_errorMessage.value != null) {
            _errorMessage.value = AppStrings.offlineWarning(next)
        }
    }

    // Windscribe User State
    private val _windscribeQuota = MutableStateFlow(WindscribeQuotaResponse())
    val windscribeQuota: StateFlow<WindscribeQuotaResponse> = _windscribeQuota.asStateFlow()

    private val _activeUserEmail = MutableStateFlow<WindscribeEmailRecord?>(null)
    val activeUserEmail: StateFlow<WindscribeEmailRecord?> = _activeUserEmail.asStateFlow()

    private val _isGeneratingEmail = MutableStateFlow(false)
    val isGeneratingEmail: StateFlow<Boolean> = _isGeneratingEmail.asStateFlow()

    private val _isCheckingInbox = MutableStateFlow(false)
    val isCheckingInbox: StateFlow<Boolean> = _isCheckingInbox.asStateFlow()

    private val _emailStatusMessage = MutableStateFlow<String?>(null)
    val emailStatusMessage: StateFlow<String?> = _emailStatusMessage.asStateFlow()

    private val _remainingSeconds = MutableStateFlow(0L)
    val remainingSeconds: StateFlow<Long> = _remainingSeconds.asStateFlow()

    private var pollingJob: Job? = null
    private var countdownJob: Job? = null

    // App Self-Update State
    private val _appUpdateState = MutableStateFlow<AppUpdateState>(AppUpdateState.Idle)
    val appUpdateState: StateFlow<AppUpdateState> = _appUpdateState.asStateFlow()

    private val updateHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .followRedirects(true)
            .followSslRedirects(true)
            .build()
    }

    fun getCurrentAppVersion(): String {
        val context = getApplication<Application>()
        return runCatching {
            context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "1.0"
        }.getOrDefault("1.0")
    }

    init {
        refreshData()
        restoreSavedUserEmail()
    }

    private fun restoreSavedUserEmail() {
        val savedEmail = sharedPrefs.getString("active_ws_email", null)
        val savedExpires = sharedPrefs.getLong("active_ws_expires", 0L)
        val nowSec = System.currentTimeMillis() / 1000
        if (!savedEmail.isNullOrBlank() && savedExpires > nowSec) {
            val rec = WindscribeEmailRecord(
                email = savedEmail,
                expiresAt = savedExpires,
                remainingSeconds = savedExpires - nowSec,
                status = "pending"
            )
            _activeUserEmail.value = rec
            startEmailCountdownAndPolling(savedEmail, savedExpires)
        }
        fetchWindscribeQuota()
    }

    fun refreshData() {
        if (_isRefreshing.value) return
        viewModelScope.launch {
            _isRefreshing.value = true
            _errorMessage.value = null
            val fetchResult = repository.fetchAndCacheAll()
            repository.getWindscribeQuota(deviceFingerprint, localDeviceId)
                .onSuccess { quota ->
                    _windscribeQuota.value = quota
                }
            if (_activeUserEmail.value != null) {
                checkActiveEmailInboxNow(silent = true)
            }
            fetchResult
                .onSuccess {
                    _isRefreshing.value = false
                }
                .onFailure {
                    _isRefreshing.value = false
                    _errorMessage.value = AppStrings.offlineWarning(_currentLanguage.value)
                }
        }
    }

    fun fetchWindscribeQuota() {
        viewModelScope.launch {
            repository.getWindscribeQuota(deviceFingerprint, localDeviceId)
                .onSuccess { quota ->
                    _windscribeQuota.value = quota
                }
        }
    }

    fun generateUserWindscribeEmail() {
        if (_isGeneratingEmail.value) return
        viewModelScope.launch {
            _isGeneratingEmail.value = true
            _emailStatusMessage.value = null

            repository.createWindscribeEmail(
                fingerprint = deviceFingerprint,
                localId = localDeviceId,
                isUserRequest = true
            ).onSuccess { res ->
                if (res.success && res.data != null) {
                    val rec = res.data
                    val lifespanSec = (rec.lifespanMinutes ?: 5) * 60L
                    val expires = if ((rec.expiresAt ?: 0L) > 0L) rec.expiresAt!! else (System.currentTimeMillis() / 1000 + lifespanSec)
                    _activeUserEmail.value = rec
                    sharedPrefs.edit()
                        .putString("active_ws_email", rec.email)
                        .putLong("active_ws_expires", expires)
                        .apply()
                    startEmailCountdownAndPolling(rec.email, expires)
                    val curQuota = _windscribeQuota.value
                    val nextUsed = curQuota.used + 1
                    _windscribeQuota.value = curQuota.copy(
                        used = nextUsed,
                        remaining = (curQuota.max - nextUsed).coerceAtLeast(0)
                    )
                    fetchWindscribeQuota()
                } else {
                    _emailStatusMessage.value = res.message ?: "خطا در ساخت ایمیل موقت"
                }
            }.onFailure {
                _emailStatusMessage.value = AppStrings.loginErrorNetwork(_currentLanguage.value)
            }
            _isGeneratingEmail.value = false
        }
    }

    private fun startEmailCountdownAndPolling(email: String, expiresAtEpochSec: Long) {
        countdownJob?.cancel()
        pollingJob?.cancel()

        countdownJob = viewModelScope.launch {
            while (isActive) {
                val now = System.currentTimeMillis() / 1000
                val rem = (expiresAtEpochSec - now).coerceAtLeast(0L)
                _remainingSeconds.value = rem
                if (rem <= 0L) {
                    break
                }
                delay(1000L)
            }
        }

        pollingJob = viewModelScope.launch {
            while (isActive) {
                checkActiveEmailInboxNow(silent = true)
                val currentRec = _activeUserEmail.value
                if (!currentRec?.link.isNullOrBlank()) {
                    // Found verification link, slow down polling
                    delay(12000L)
                } else {
                    delay(5000L)
                }
            }
        }
    }

    fun checkActiveEmailInboxNow(silent: Boolean = false) {
        val currentEmail = _activeUserEmail.value?.email ?: return
        viewModelScope.launch {
            if (!silent) _isCheckingInbox.value = true
            repository.checkWindscribeCode(currentEmail)
                .onSuccess { res ->
                    if (res.deletedByAdmin == true) {
                        clearActiveUserEmailLocally()
                        _emailStatusMessage.value = res.message ?: "این ایمیل توسط مدیر حذف گردیده است."
                    } else if (res.success) {
                        val existing = _activeUserEmail.value
                        if (existing != null) {
                            _activeUserEmail.value = existing.copy(
                                code = null,
                                link = res.link ?: existing.link,
                                status = res.status ?: existing.status,
                                messages = res.messages ?: existing.messages
                            )
                        }
                    }
                }
            if (!silent) _isCheckingInbox.value = false
        }
    }

    fun deleteUserActiveEmail() {
        val currentEmail = _activeUserEmail.value?.email ?: return
        viewModelScope.launch {
            _isGeneratingEmail.value = true
            repository.deleteWindscribeEmail(currentEmail, isAdminDeletion = false)
            clearActiveUserEmailLocally()
            _isGeneratingEmail.value = false
            fetchWindscribeQuota()
        }
    }

    private fun clearActiveUserEmailLocally() {
        pollingJob?.cancel()
        countdownJob?.cancel()
        _activeUserEmail.value = null
        _remainingSeconds.value = 0L
        sharedPrefs.edit()
            .remove("active_ws_email")
            .remove("active_ws_expires")
            .apply()
    }

    // Clipboard Integration
    fun copyToClipboard(text: String, label: String) {
        val context = getApplication<Application>()
        val lang = _currentLanguage.value
        try {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(label, text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, AppStrings.copiedToast(label, lang), Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, AppStrings.clipboardAccessError(lang), Toast.LENGTH_SHORT).show()
        }
    }

    // =========================================================================
    // APP SELF-UPDATE CHECKER & INSTALLER (Server dlA9z Link + GitHub Releases)
    // =========================================================================

    fun checkForAppUpdate() {
        if (_appUpdateState.value is AppUpdateState.Checking || _appUpdateState.value is AppUpdateState.Downloading) return
        viewModelScope.launch {
            _appUpdateState.value = AppUpdateState.Checking
            val currentVersion = getCurrentAppVersion()

            // Refresh links from server first so dlA9z is completely up to date
            repository.fetchAndCacheAll()
            val latestCached = repository.cachedDataFlow.firstOrNull() ?: cachedData.value
            val serverUpdateUrl = latestCached?.dlA9z?.trim() ?: ""

            val resultState = withContext(Dispatchers.IO) {
                // 1. Check GitHub Releases (from project repo or repo extracted from serverUpdateUrl)
                val candidateRepos = mutableListOf("MojtabaZanganeh/A9Z-Configs")
                val githubRegex = Regex("""github\.com/([^/]+/[^/]+)""", RegexOption.IGNORE_CASE)
                githubRegex.find(serverUpdateUrl)?.groupValues?.getOrNull(1)?.let { extractedRepo ->
                    val cleanRepo = extractedRepo.removeSuffix(".git")
                    if (!candidateRepos.contains(cleanRepo)) {
                        candidateRepos.add(0, cleanRepo)
                    }
                }

                var githubUpdateFound: AppUpdateState.UpdateAvailable? = null
                val lastInstalledGithubTag = sharedPrefs.getString("last_installed_github_tag", "") ?: ""

                for (repoSlug in candidateRepos) {
                    runCatching {
                        val req = Request.Builder()
                            .url("https://api.github.com/repos/$repoSlug/releases/latest")
                            .header("Accept", "application/vnd.github+json")
                            .header("User-Agent", "A9Z-Android-Updater")
                            .build()
                        updateHttpClient.newCall(req).execute().use { resp ->
                            if (resp.isSuccessful) {
                                val bodyStr = resp.body?.string() ?: ""
                                if (bodyStr.isNotBlank()) {
                                    val json = JSONObject(bodyStr)
                                    val tagName = json.optString("tag_name", "").trim()
                                    val releaseName = json.optString("name", tagName).trim()
                                    val htmlUrl = json.optString("html_url", "").trim()
                                    val assets = json.optJSONArray("assets")
                                    var apkAssetUrl = ""
                                    if (assets != null) {
                                        for (i in 0 until assets.length()) {
                                            val asset = assets.optJSONObject(i) ?: continue
                                            val dlUrl = asset.optString("browser_download_url", "")
                                            if (dlUrl.endsWith(".apk", ignoreCase = true)) {
                                                apkAssetUrl = dlUrl
                                                break
                                            } else if (apkAssetUrl.isBlank() && dlUrl.isNotBlank()) {
                                                apkAssetUrl = dlUrl
                                            }
                                        }
                                    }

                                    if (tagName.isNotBlank() && isVersionNewer(tagName, currentVersion) && tagName != lastInstalledGithubTag) {
                                        val finalUrl = apkAssetUrl.ifBlank { serverUpdateUrl.ifBlank { htmlUrl } }
                                        if (finalUrl.isNotBlank()) {
                                            githubUpdateFound = AppUpdateState.UpdateAvailable(
                                                versionLabel = "GitHub $tagName",
                                                downloadUrl = finalUrl,
                                                remoteSignature = "gh:$tagName|$finalUrl"
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (githubUpdateFound != null) break
                }

                if (githubUpdateFound != null) {
                    return@withContext githubUpdateFound!!
                }

                // 2. Check Server's Direct Update Link (dlA9z)
                if (serverUpdateUrl.isNotBlank()) {
                    val lastInstalledUrl = sharedPrefs.getString("last_installed_a9z_url", "") ?: ""
                    val lastInstalledSig = sharedPrefs.getString("last_installed_a9z_sig", "") ?: ""

                    // Probe HTTP HEAD to detect file replacement even on the same URL
                    var etag = ""
                    var lastModified = ""
                    var contentLength = ""
                    runCatching {
                        val headReq = Request.Builder()
                            .url(serverUpdateUrl)
                            .head()
                            .header("User-Agent", "A9Z-Android-Updater")
                            .build()
                        updateHttpClient.newCall(headReq).execute().use { resp ->
                            etag = resp.header("ETag") ?: ""
                            lastModified = resp.header("Last-Modified") ?: ""
                            contentLength = resp.header("Content-Length") ?: ""
                        }
                    }

                    val currentRemoteSig = "$serverUpdateUrl|$etag|$lastModified|$contentLength"

                    // Check if URL contains a version tag newer than currentVersion
                    val urlVersionRegex = Regex("""[vV]?(\d+\.\d+(?:\.\d+)?)""")
                    val urlVersionMatch = urlVersionRegex.find(serverUpdateUrl.substringAfterLast("/"))?.groupValues?.getOrNull(1)

                    val isUrlChanged = serverUpdateUrl != lastInstalledUrl
                    val isRemoteFileChanged = lastInstalledSig.isNotBlank() && currentRemoteSig != lastInstalledSig
                    val isUrlVersionNewer = urlVersionMatch != null && isVersionNewer(urlVersionMatch, currentVersion)

                    if (isUrlChanged || isRemoteFileChanged || isUrlVersionNewer) {
                        val label = when {
                            !urlVersionMatch.isNullOrBlank() -> "v$urlVersionMatch"
                            lastModified.isNotBlank() -> lastModified.take(16)
                            else -> if (_currentLanguage.value == AppLanguage.PERSIAN) "لینک جدید سرور" else "New Server Build"
                        }
                        return@withContext AppUpdateState.UpdateAvailable(
                            versionLabel = label,
                            downloadUrl = serverUpdateUrl,
                            remoteSignature = currentRemoteSig
                        )
                    } else {
                        return@withContext AppUpdateState.UpToDate(
                            currentVersion = currentVersion,
                            directUrl = serverUpdateUrl
                        )
                    }
                }

                AppUpdateState.UpToDate(
                    currentVersion = currentVersion,
                    directUrl = ""
                )
            }

            _appUpdateState.value = resultState
        }
    }

    private fun isVersionNewer(remoteTag: String, currentVer: String): Boolean {
        val cleanRemote = remoteTag.trim().removePrefix("v").removePrefix("V")
        val cleanCurrent = currentVer.trim().removePrefix("v").removePrefix("V")
        if (cleanRemote == cleanCurrent) return false

        val remoteParts = cleanRemote.split(Regex("[^0-9]+")).filter { it.isNotBlank() }.mapNotNull { it.toIntOrNull() }
        val currentParts = cleanCurrent.split(Regex("[^0-9]+")).filter { it.isNotBlank() }.mapNotNull { it.toIntOrNull() }

        if (remoteParts.isEmpty()) return cleanRemote != cleanCurrent
        val maxLen = maxOf(remoteParts.size, currentParts.size)
        for (i in 0 until maxLen) {
            val r = remoteParts.getOrElse(i) { 0 }
            val c = currentParts.getOrElse(i) { 0 }
            if (r > c) return true
            if (r < c) return false
        }
        return false
    }

    fun performAppUpdate(downloadUrl: String, remoteSignature: String = "") {
        if (downloadUrl.isBlank()) return
        val context = getApplication<Application>()
        viewModelScope.launch {
            _appUpdateState.value = AppUpdateState.Downloading(0)

            withContext(Dispatchers.IO) {
                runCatching {
                    val req = Request.Builder()
                        .url(downloadUrl)
                        .header("User-Agent", "A9Z-Android-Updater")
                        .build()

                    updateHttpClient.newCall(req).execute().use { resp ->
                        if (!resp.isSuccessful) {
                            throw Exception("HTTP ${resp.code}")
                        }

                        val contentType = resp.header("Content-Type")?.lowercase() ?: ""
                        val isHtmlPage = contentType.contains("text/html") && !downloadUrl.endsWith(".apk", ignoreCase = true)

                        val etag = resp.header("ETag") ?: ""
                        val lastMod = resp.header("Last-Modified") ?: ""
                        val contentLenStr = resp.header("Content-Length") ?: ""
                        val computedSig = remoteSignature.ifBlank { "$downloadUrl|$etag|$lastMod|$contentLenStr" }

                        if (isHtmlPage) {
                            // Record update check & open release/download page in browser
                            saveUpdateSignature(downloadUrl, computedSig)
                            withContext(Dispatchers.Main) {
                                _appUpdateState.value = AppUpdateState.UpToDate(getCurrentAppVersion(), downloadUrl)
                                openUrlInBrowser(context, downloadUrl)
                            }
                            return@use
                        }

                        val body = resp.body ?: throw Exception("Empty response body")
                        val totalBytes = body.contentLength()
                        val updatesDir = File(context.cacheDir, "updates").apply { mkdirs() }
                        val apkFile = File(updatesDir, "A9Z-update.apk")

                        body.byteStream().use { input ->
                            FileOutputStream(apkFile).use { output ->
                                val buffer = ByteArray(8 * 1024)
                                var bytesRead: Int
                                var downloadedBytes = 0L
                                var lastReportedPercent = 0

                                while (input.read(buffer).also { bytesRead = it } != -1) {
                                    output.write(buffer, 0, bytesRead)
                                    downloadedBytes += bytesRead
                                    if (totalBytes > 0) {
                                        val percent = ((downloadedBytes * 100L) / totalBytes).toInt().coerceIn(0, 100)
                                        if (percent != lastReportedPercent) {
                                            lastReportedPercent = percent
                                            _appUpdateState.value = AppUpdateState.Downloading(percent)
                                        }
                                    } else {
                                        val pseudoPercent = ((downloadedBytes / (100 * 1024)).toInt()).coerceIn(5, 95)
                                        if (pseudoPercent != lastReportedPercent) {
                                            lastReportedPercent = pseudoPercent
                                            _appUpdateState.value = AppUpdateState.Downloading(pseudoPercent)
                                        }
                                    }
                                }
                                output.flush()
                            }
                        }

                        // Verify the downloaded file starts with ZIP/APK magic header ('P', 'K')
                        val isValidApk = apkFile.exists() && apkFile.length() > 1024 && runCatching {
                            apkFile.inputStream().use { ins ->
                                val header = ByteArray(2)
                                ins.read(header) == 2 && header[0] == 0x50.toByte() && header[1] == 0x4B.toByte()
                            }
                        }.getOrDefault(false)

                        saveUpdateSignature(downloadUrl, computedSig)

                        withContext(Dispatchers.Main) {
                            _appUpdateState.value = AppUpdateState.UpToDate(getCurrentAppVersion(), downloadUrl)
                            if (isValidApk) {
                                launchApkInstaller(context, apkFile, downloadUrl)
                            } else {
                                openUrlInBrowser(context, downloadUrl)
                            }
                        }
                    }
                }.onFailure {
                    withContext(Dispatchers.Main) {
                        _appUpdateState.value = AppUpdateState.UpToDate(getCurrentAppVersion(), downloadUrl)
                        openUrlInBrowser(context, downloadUrl)
                    }
                }
            }
        }
    }

    private fun saveUpdateSignature(downloadUrl: String, remoteSignature: String) {
        val editor = sharedPrefs.edit()
            .putString("last_installed_a9z_url", downloadUrl)
            .putString("last_installed_a9z_sig", remoteSignature)
        if (remoteSignature.startsWith("gh:")) {
            val ghTag = remoteSignature.removePrefix("gh:").substringBefore("|")
            editor.putString("last_installed_github_tag", ghTag)
        }
        editor.apply()
    }

    private fun launchApkInstaller(context: Context, apkFile: File, fallbackUrl: String) {
        runCatching {
            val apkUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                apkFile
            )
            val installIntent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(apkUri, "application/vnd.android.package-archive")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(installIntent)
        }.onFailure {
            openUrlInBrowser(context, fallbackUrl)
        }
    }

    private fun openUrlInBrowser(context: Context, url: String) {
        runCatching {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(browserIntent)
        }
    }
}

sealed class AppUpdateState {
    object Idle : AppUpdateState()
    object Checking : AppUpdateState()
    data class UpdateAvailable(
        val versionLabel: String,
        val downloadUrl: String,
        val remoteSignature: String = ""
    ) : AppUpdateState()
    data class UpToDate(
        val currentVersion: String,
        val directUrl: String = ""
    ) : AppUpdateState()
    data class Downloading(val progressPercent: Int) : AppUpdateState()
}

sealed class SaveStatus {
    object Idle : SaveStatus()
    object Saving : SaveStatus()
    data class Success(val message: String) : SaveStatus()
    data class Error(val message: String) : SaveStatus()
}
