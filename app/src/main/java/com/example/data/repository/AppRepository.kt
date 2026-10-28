package com.example.data.repository

import com.example.data.local.CachedData
import com.example.data.local.CachedDataDao
import com.example.data.local.CachedHistoryItem
import com.example.data.model.*
import com.example.data.remote.ApiService
import kotlinx.coroutines.flow.Flow
import java.io.IOException

class AppRepository(
    private val api: ApiService,
    private val dao: CachedDataDao
) {
    val cachedDataFlow: Flow<CachedData?> = dao.getCachedDataFlow()
    val configHistoryFlow: Flow<List<CachedHistoryItem>> = dao.getHistoryFlow("CONFIG")
    val proxyHistoryFlow: Flow<List<CachedHistoryItem>> = dao.getHistoryFlow("PROXY")

    suspend fun fetchAndCacheAll(): Result<Unit> {
        return runCatching {
            // Get last configs update
            val configUpdate = runCatching { api.getLastUpdate() }.getOrNull()
            // Get last proxies update
            val proxyUpdate = runCatching { api.getLastPxUpdate() }.getOrNull()

            // GetConfigs
            val configRes = runCatching { api.getConfig() }.getOrNull()
            val configsStr = configRes?.config?.joinToString("\n") ?: ""

            // GetProxies
            val proxyRes = runCatching { api.getPxs() }.getOrNull()
            val proxiesStr = proxyRes?.pxs?.joinToString("\n") ?: ""

            // GetSub
            val subRes = runCatching { api.getSub() }.getOrNull()
            val subStr = subRes?.sub ?: ""

            // GetLinks
            val linksRes = runCatching { api.getLinks() }.getOrNull()
            val map = linksRes?.links

            // History elements
            val configHistoryRes = runCatching { api.getConfigHistory() }.getOrNull()
            if (configHistoryRes != null) {
                dao.deleteHistoryByType("CONFIG")
                val items = configHistoryRes.history.map {
                    CachedHistoryItem(
                        type = "CONFIG",
                        content = it.content,
                        count = it.count,
                        timestamp = it.timestamp,
                        version = it.version
                    )
                }
                dao.insertHistoryItems(items)
            }

            val proxyHistoryRes = runCatching { api.getProxyHistory() }.getOrNull()
            if (proxyHistoryRes != null) {
                dao.deleteHistoryByType("PROXY")
                val items = proxyHistoryRes.history.map {
                    CachedHistoryItem(
                        type = "PROXY",
                        content = it.content,
                        count = it.count,
                        timestamp = it.timestamp,
                        version = it.version
                    )
                }
                dao.insertHistoryItems(items)
            }

            // Read existing cached record so we don't wipe items we failed to fetch
            val current = dao.getCachedDataOnce() ?: CachedData()

            val updatedCachedData = CachedData(
                id = 1,
                configs = configsStr.ifEmpty { current.configs },
                configCount = configUpdate?.count ?: (configsStr.split('\n').filter { it.isNotBlank() }.size.takeIf { it > 0 } ?: current.configCount),
                configTimestamp = configUpdate?.timestamp ?: current.configTimestamp,
                proxies = proxiesStr.ifEmpty { current.proxies },
                proxyCount = proxyUpdate?.count ?: (proxiesStr.split('\n').filter { it.isNotBlank() }.size.takeIf { it > 0 } ?: current.proxyCount),
                proxyTimestamp = proxyUpdate?.timestamp ?: current.proxyTimestamp,
                subLink = subStr.ifEmpty { current.subLink },
                dlA9z = map?.a9z ?: current.dlA9z,
                dlAndroid = map?.android ?: current.dlAndroid,
                dlIos = map?.ios ?: current.dlIos,
                dlWin32 = map?.win32 ?: current.dlWin32,
                dlWin64 = map?.win64 ?: current.dlWin64,
                dlWsWindows = map?.wsWindows ?: current.dlWsWindows,
                dlWsAndroid = map?.wsAndroid ?: current.dlWsAndroid,
                dlWsIos = map?.wsIos ?: current.dlWsIos,
                dlWsMac = map?.wsMac ?: current.dlWsMac
            )

            dao.insertCachedData(updatedCachedData)
        }
    }

    // Auth endpoints
    suspend fun login(req: LoginRequest): Result<LoginResponse> {
        return runCatching { api.login(req) }
    }

    suspend fun checkToken(token: String): Result<Boolean> {
        return runCatching {
            val response = api.checkToken(CheckTokenRequest(token))
            response.result
        }
    }

    // Admin Panel updates
    suspend fun saveConfig(content: String): Result<Unit> {
        return runCatching {
            val response = api.saveConfig(SaveConfigPayload(content))
            if (!response.isSuccessful) {
                throw IOException("Server error: ${response.code()}")
            }
            val current = dao.getCachedDataOnce() ?: CachedData()
            dao.insertCachedData(current.copy(
                configs = content,
                configCount = content.split('\n').filter { it.isNotBlank() }.size,
                configTimestamp = "به روز شده"
            ))
        }
    }

    suspend fun saveProxy(content: String): Result<Unit> {
        return runCatching {
            val response = api.savePx(SaveProxyPayload(content))
            if (!response.isSuccessful) {
                throw IOException("Server error: ${response.code()}")
            }
            val current = dao.getCachedDataOnce() ?: CachedData()
            dao.insertCachedData(current.copy(
                proxies = content,
                proxyCount = content.split('\n').filter { it.isNotBlank() }.size,
                proxyTimestamp = "به روز شده"
            ))
        }
    }

    suspend fun saveSub(subUrl: String): Result<Unit> {
        return runCatching {
            val response = api.saveSub(SaveSubPayload(subUrl))
            if (!response.isSuccessful) {
                throw IOException("Server error: ${response.code()}")
            }
            val current = dao.getCachedDataOnce() ?: CachedData()
            dao.insertCachedData(current.copy(subLink = subUrl))
        }
    }

    suspend fun saveLinks(payload: SaveLinksPayload): Result<Unit> {
        return runCatching {
            val response = api.saveLinks(payload)
            if (!response.isSuccessful) {
                throw IOException("Server error: ${response.code()}")
            }
            val current = dao.getCachedDataOnce() ?: CachedData()
            dao.insertCachedData(current.copy(
                dlA9z = payload.a9z,
                dlAndroid = payload.android,
                dlIos = payload.ios,
                dlWin32 = payload.win32,
                dlWin64 = payload.win64,
                dlWsWindows = payload.wsWindows,
                dlWsAndroid = payload.wsAndroid,
                dlWsIos = payload.wsIos,
                dlWsMac = payload.wsMac
            ))
        }
    }

    // Windscribe & Settings Repository Methods
    suspend fun getSettings(): Result<AppSettingsData> {
        return runCatching {
            val res = api.getSettings()
            res.settings ?: AppSettingsData()
        }
    }

    suspend fun saveSettings(maxEmails: Int, periodDays: Int, lifespanMinutes: Int): Result<SaveSettingsResponse> {
        return runCatching {
            api.saveSettings(SaveSettingsPayload(maxEmails, periodDays, lifespanMinutes))
        }
    }

    suspend fun getWindscribeQuota(fingerprint: String, localId: String): Result<WindscribeQuotaResponse> {
        return runCatching {
            api.getWindscribeQuota(WindscribeQuotaRequest(fingerprint, localId))
        }
    }

    suspend fun createWindscribeEmail(
        fingerprint: String,
        localId: String,
        isUserRequest: Boolean,
        customUsername: String? = null,
        customPassword: String? = null
    ): Result<CreateWindscribeEmailResponse> {
        return runCatching {
            api.createWindscribeEmail(
                CreateWindscribeEmailRequest(
                    fingerprint = fingerprint,
                    localId = localId,
                    isUserRequest = isUserRequest,
                    username = customUsername,
                    password = customPassword
                )
            )
        }
    }

    suspend fun deleteWindscribeEmail(email: String, isAdminDeletion: Boolean): Result<DeleteWindscribeEmailResponse> {
        return runCatching {
            api.deleteWindscribeEmail(DeleteWindscribeEmailRequest(email, isAdminDeletion))
        }
    }

    suspend fun checkWindscribeCode(email: String): Result<CheckWindscribeCodeResponse> {
        return runCatching {
            api.checkWindscribeCode(CheckWindscribeCodeRequest(email))
        }
    }

    suspend fun getWindscribeEmails(): Result<List<WindscribeEmailRecord>> {
        return runCatching {
            api.getWindscribeEmails().emails ?: emptyList()
        }
    }

    suspend fun getStats(): Result<SystemStatsPayload> {
        return runCatching {
            api.getStats().stats ?: SystemStatsPayload()
        }
    }
}
