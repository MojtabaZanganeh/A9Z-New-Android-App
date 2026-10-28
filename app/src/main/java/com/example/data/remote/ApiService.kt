package com.example.data.remote

import com.example.data.model.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface ApiService {

    @POST("auth-login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("check-token")
    suspend fun checkToken(@Body request: CheckTokenRequest): CheckTokenResponse

    @GET("get-config")
    suspend fun getConfig(): ConfigResponse

    @GET("get-pxs")
    suspend fun getPxs(): ProxiesResponse

    @GET("get-history")
    suspend fun getConfigHistory(): HistoryResponse

    @GET("get-px-history")
    suspend fun getProxyHistory(): HistoryResponse

    @GET("last-update")
    suspend fun getLastUpdate(): LastUpdateResponse

    @GET("last-px-update")
    suspend fun getLastPxUpdate(): LastUpdateResponse

    @GET("get-sub")
    suspend fun getSub(): SubResponse

    @GET("get-links")
    suspend fun getLinks(): GetLinksResponse

    @POST("save-config")
    suspend fun saveConfig(@Body payload: SaveConfigPayload): Response<Unit>

    @POST("save-px")
    suspend fun savePx(@Body payload: SaveProxyPayload): Response<Unit>

    @POST("save-sub")
    suspend fun saveSub(@Body payload: SaveSubPayload): Response<Unit>

    @POST("save-links")
    suspend fun saveLinks(@Body payload: SaveLinksPayload): Response<Unit>

    // Windscribe & Settings Endpoints
    @GET("get-settings")
    suspend fun getSettings(): GetSettingsResponse

    @POST("save-settings")
    suspend fun saveSettings(@Body payload: SaveSettingsPayload): SaveSettingsResponse

    @POST("get-windscribe-quota")
    suspend fun getWindscribeQuota(@Body request: WindscribeQuotaRequest): WindscribeQuotaResponse

    @POST("create-windscribe-email")
    suspend fun createWindscribeEmail(@Body request: CreateWindscribeEmailRequest): CreateWindscribeEmailResponse

    @POST("delete-windscribe-email")
    suspend fun deleteWindscribeEmail(@Body request: DeleteWindscribeEmailRequest): DeleteWindscribeEmailResponse

    @POST("check-windscribe-code")
    suspend fun checkWindscribeCode(@Body request: CheckWindscribeCodeRequest): CheckWindscribeCodeResponse

    @GET("get-windscribe-emails")
    suspend fun getWindscribeEmails(): GetWindscribeEmailsResponse

    // System Statistics
    @GET("get-stats")
    suspend fun getStats(): GetStatsResponse

    companion object {
        private const val BASE_URL = "https://a9z.ir/Login/login.php/"

        fun create(): ApiService {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor { chain ->
                    val original = chain.request()
                    val request = original.newBuilder()
                        .header("api-key", com.example.BuildConfig.A9Z_API_KEY)
                        .build()
                    chain.proceed(request)
                }
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build()

            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(ApiService::class.java)
        }
    }
}
