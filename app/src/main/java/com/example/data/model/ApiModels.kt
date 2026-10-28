package com.example.data.model

import com.squareup.moshi.Json

data class LoginRequest(
    @Json(name = "username") val username: String,
    @Json(name = "password") val password: String,
    @Json(name = "role") val role: String = "admin"
)

data class LoginResponse(
    @Json(name = "result") val result: Boolean,
    @Json(name = "token") val token: String? = null
)

data class CheckTokenRequest(
    @Json(name = "token") val token: String
)

data class CheckTokenResponse(
    @Json(name = "result") val result: Boolean
)

data class ConfigResponse(
    @Json(name = "config") val config: List<String>
)

data class ProxiesResponse(
    @Json(name = "pxs") val pxs: List<String>
)

data class HistoryItem(
    @Json(name = "content") val content: String,
    @Json(name = "count") val count: Int,
    @Json(name = "timestamp") val timestamp: String,
    @Json(name = "version") val version: String
)

data class HistoryResponse(
    @Json(name = "history") val history: List<HistoryItem>
)

data class LastUpdateResponse(
    @Json(name = "count") val count: Int,
    @Json(name = "timestamp") val timestamp: String
)

data class SubResponse(
    @Json(name = "sub") val sub: String
)

data class DownloadLinksMap(
    @Json(name = "A9Z") val a9z: String? = "",
    @Json(name = "Android") val android: String? = "",
    @Json(name = "ios") val ios: String? = "",
    @Json(name = "Win32") val win32: String? = "",
    @Json(name = "Win64") val win64: String? = "",
    @Json(name = "ws_windows") val wsWindows: String? = "",
    @Json(name = "ws_android") val wsAndroid: String? = "",
    @Json(name = "ws_ios") val wsIos: String? = "",
    @Json(name = "ws_mac") val wsMac: String? = ""
)

data class GetLinksResponse(
    @Json(name = "links") val links: DownloadLinksMap
)

// Saving payloads
data class SaveConfigPayload(
    @Json(name = "content") val content: String
)

data class SaveProxyPayload(
    @Json(name = "content") val content: String
)

data class SaveSubPayload(
    @Json(name = "sub") val sub: String
)

data class SaveLinksPayload(
    @Json(name = "a9z") val a9z: String = "",
    @Json(name = "android") val android: String = "",
    @Json(name = "ios") val ios: String = "",
    @Json(name = "win32") val win32: String = "",
    @Json(name = "win64") val win64: String = "",
    @Json(name = "ws_windows") val wsWindows: String = "",
    @Json(name = "ws_android") val wsAndroid: String = "",
    @Json(name = "ws_ios") val wsIos: String = "",
    @Json(name = "ws_mac") val wsMac: String = ""
)

// Windscribe & Settings Models
data class AppSettingsData(
    @Json(name = "windscribe_max_emails") val windscribeMaxEmails: Int = 3,
    @Json(name = "windscribe_period_days") val windscribePeriodDays: Int = 30,
    @Json(name = "windscribe_lifespan_minutes") val windscribeLifespanMinutes: Int = 5
)

data class GetSettingsResponse(
    @Json(name = "success") val success: Boolean = false,
    @Json(name = "settings") val settings: AppSettingsData? = null
)

data class SaveSettingsPayload(
    @Json(name = "windscribe_max_emails") val windscribeMaxEmails: Int,
    @Json(name = "windscribe_period_days") val windscribePeriodDays: Int,
    @Json(name = "windscribe_lifespan_minutes") val windscribeLifespanMinutes: Int
)

data class SaveSettingsResponse(
    @Json(name = "success") val success: Boolean = false,
    @Json(name = "settings") val settings: AppSettingsData? = null,
    @Json(name = "message") val message: String? = null
)

data class WindscribeQuotaRequest(
    @Json(name = "fingerprint") val fingerprint: String,
    @Json(name = "local_id") val localId: String
)

data class WindscribeQuotaResponse(
    @Json(name = "success") val success: Boolean = false,
    @Json(name = "used") val used: Int = 0,
    @Json(name = "max") val max: Int = 3,
    @Json(name = "remaining") val remaining: Int = 3,
    @Json(name = "period_days") val periodDays: Int = 30,
    @Json(name = "lifespan_minutes") val lifespanMinutes: Int = 5
)

data class CreateWindscribeEmailRequest(
    @Json(name = "fingerprint") val fingerprint: String = "",
    @Json(name = "local_id") val localId: String = "",
    @Json(name = "is_user_request") val isUserRequest: Boolean = true,
    @Json(name = "username") val username: String? = null,
    @Json(name = "password") val password: String? = null
)

data class CpanelInfo(
    @Json(name = "configured") val configured: Boolean? = false,
    @Json(name = "status") val status: Int? = 0,
    @Json(name = "message") val message: String? = null
)

data class WindscribeMessageItem(
    @Json(name = "id") val id: Any? = null,
    @Json(name = "from") val from: String? = "",
    @Json(name = "subject") val subject: String? = "",
    @Json(name = "date") val date: String? = "",
    @Json(name = "body") val body: String? = "",
    @Json(name = "code") val code: String? = null,
    @Json(name = "link") val link: String? = null
)

data class WindscribeEmailRecord(
    @Json(name = "id") val id: String? = "",
    @Json(name = "email") val email: String = "",
    @Json(name = "password") val password: String? = "",
    @Json(name = "created_at") val createdAt: String? = "",
    @Json(name = "created_timestamp") val createdTimestamp: Long? = 0L,
    @Json(name = "expires_at") val expiresAt: Long? = 0L,
    @Json(name = "lifespan_minutes") val lifespanMinutes: Int? = 5,
    @Json(name = "is_user_request") val isUserRequest: Boolean? = true,
    @Json(name = "code") val code: String? = null,
    @Json(name = "link") val link: String? = null,
    @Json(name = "status") val status: String? = "pending",
    @Json(name = "is_expired") val isExpired: Boolean? = false,
    @Json(name = "remaining_seconds") val remainingSeconds: Long? = 0L,
    @Json(name = "cpanel_message") val cpanelMessage: String? = null,
    @Json(name = "messages") val messages: List<WindscribeMessageItem>? = emptyList()
)

data class CreateWindscribeEmailResponse(
    @Json(name = "success") val success: Boolean = false,
    @Json(name = "rate_limited") val rateLimited: Boolean? = false,
    @Json(name = "error") val error: String? = null,
    @Json(name = "message") val message: String? = null,
    @Json(name = "data") val data: WindscribeEmailRecord? = null,
    @Json(name = "cpanel_info") val cpanelInfo: CpanelInfo? = null
)

data class DeleteWindscribeEmailRequest(
    @Json(name = "email") val email: String,
    @Json(name = "is_admin_deletion") val isAdminDeletion: Boolean = false
)

data class DeleteWindscribeEmailResponse(
    @Json(name = "success") val success: Boolean = false,
    @Json(name = "email") val email: String? = null,
    @Json(name = "deleted_by_admin") val deletedByAdmin: Boolean? = false
)

data class CheckWindscribeCodeRequest(
    @Json(name = "email") val email: String
)

data class CheckWindscribeCodeResponse(
    @Json(name = "success") val success: Boolean = false,
    @Json(name = "deleted_by_admin") val deletedByAdmin: Boolean? = false,
    @Json(name = "error") val error: String? = null,
    @Json(name = "email") val email: String? = null,
    @Json(name = "code") val code: String? = null,
    @Json(name = "link") val link: String? = null,
    @Json(name = "subject") val subject: String? = null,
    @Json(name = "status") val status: String? = "pending",
    @Json(name = "message") val message: String? = null,
    @Json(name = "messages") val messages: List<WindscribeMessageItem>? = emptyList()
)

data class GetWindscribeEmailsResponse(
    @Json(name = "success") val success: Boolean = false,
    @Json(name = "emails") val emails: List<WindscribeEmailRecord>? = emptyList()
)

// System Statistics Models
data class LoginRecordItem(
    @Json(name = "id") val id: Int? = 0,
    @Json(name = "role") val role: String? = "user",
    @Json(name = "time") val time: Any? = null,
    @Json(name = "success") val success: Boolean? = false,
    @Json(name = "ip") val ip: String? = "",
    @Json(name = "agent") val agent: String? = ""
)

data class LoginsStatsData(
    @Json(name = "total_user_logins") val totalUserLogins: Int = 0,
    @Json(name = "total_admin_logins") val totalAdminLogins: Int = 0,
    @Json(name = "total_attempts") val totalAttempts: Int = 0,
    @Json(name = "failed_logins") val failedLogins: Int = 0,
    @Json(name = "unique_ips") val uniqueIps: Int = 0,
    @Json(name = "daily_user_logins") val dailyUserLogins: Int = 0,
    @Json(name = "weekly_user_logins") val weeklyUserLogins: Int = 0,
    @Json(name = "monthly_user_logins") val monthlyUserLogins: Int = 0,
    @Json(name = "yearly_user_logins") val yearlyUserLogins: Int = 0,
    @Json(name = "recent_logins") val recentLogins: List<LoginRecordItem>? = emptyList()
)

data class EmailsStatsData(
    @Json(name = "total_created") val totalCreated: Int = 0,
    @Json(name = "user_created") val userCreated: Int = 0,
    @Json(name = "admin_created") val adminCreated: Int = 0,
    @Json(name = "total_verified") val totalVerified: Int = 0,
    @Json(name = "total_pending") val totalPending: Int = 0,
    @Json(name = "active_not_expired") val activeNotExpired: Int = 0
)

data class UsersStatsData(
    @Json(name = "unique_users_received_emails") val uniqueUsersReceivedEmails: Int = 0,
    @Json(name = "total_quota_records") val totalQuotaRecords: Int = 0
)

data class SystemStatsPayload(
    @Json(name = "logins") val logins: LoginsStatsData = LoginsStatsData(),
    @Json(name = "emails") val emails: EmailsStatsData = EmailsStatsData(),
    @Json(name = "users") val users: UsersStatsData = UsersStatsData(),
    @Json(name = "settings") val settings: AppSettingsData? = null
)

data class GetStatsResponse(
    @Json(name = "success") val success: Boolean = false,
    @Json(name = "stats") val stats: SystemStatsPayload? = null
)
