package com.example.util

import org.json.JSONObject
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

data class ParsedConfig(
    val protocol: String,
    val title: String,
    val serverInfo: String,
    val raw: String
)

data class ParsedProxy(
    val protocol: String,
    val serverInfo: String,
    val rawUrl: String
)

object ConfigParser {

    /**
     * Parses a V2Ray/Xray config URL:
     * - Protocol auto-detection (VLESS, VMESS, TROJAN, SHADOWSOCKS, HYSTERIA 2, etc.)
     * - Config title: everything after '#' is the title (URL-decoded). Fallback to VMess 'ps' or index.
     * - Server and port details for user context.
     */
    fun parseConfig(raw: String, index: Int = 1): ParsedConfig {
        val trimmed = raw.trim()
        val protocol = detectConfigProtocol(trimmed)

        var title = ""
        var server = ""

        // 1. Title from '#' if present: "توی کانفیگ از # به بعد = عنوان کانفیگ"
        if (trimmed.contains("#")) {
            val afterHash = trimmed.substringAfter("#")
            if (afterHash.isNotBlank()) {
                title = decodeUrlString(afterHash)
            }
        }

        // 2. VMess special parsing (JSON in Base64)
        if (trimmed.startsWith("vmess://", ignoreCase = true)) {
            val vmessBody = trimmed.substring(8).substringBefore("#")
            try {
                val decodedBytes = decodeBase64Safe(vmessBody)
                if (decodedBytes != null) {
                    val jsonStr = String(decodedBytes, StandardCharsets.UTF_8)
                    val json = JSONObject(jsonStr)

                    if (title.isBlank() && json.has("ps")) {
                        val ps = json.optString("ps", "")
                        if (ps.isNotBlank()) {
                            title = ps
                        }
                    }

                    val add = json.optString("add", "")
                    val port = json.optString("port", "")
                    if (add.isNotBlank() && port.isNotBlank()) {
                        server = "$add:$port"
                    } else if (add.isNotBlank()) {
                        server = add
                    }
                }
            } catch (e: Exception) {
                // If base64 decoding fails, keep defaults
            }
        }

        // 3. Fallback for title if empty
        if (title.isBlank()) {
            title = "کانفیگ $protocol #$index"
        }

        // 4. Server extraction for non-vmess if not yet set
        if (server.isBlank()) {
            server = extractServerFromUrl(trimmed)
        }

        return ParsedConfig(
            protocol = protocol,
            title = title,
            serverInfo = server,
            raw = trimmed
        )
    }

    /**
     * Auto-detects proxy protocol and extracts host/port.
     * As requested: "و توی پروکسی هم عنوان نداشته باشه فقط پروتکلش رو خودکار تشخیص بده و عنوان پروتکل رو نمایش بده"
     */
    fun parseProxy(rawUrl: String): ParsedProxy {
        val trimmed = rawUrl.trim()
        val protocol = detectProxyProtocol(trimmed)
        val server = extractProxyServerAndPort(trimmed)

        return ParsedProxy(
            protocol = protocol,
            serverInfo = server,
            rawUrl = trimmed
        )
    }

    private fun detectConfigProtocol(config: String): String {
        return when {
            config.startsWith("vless://", ignoreCase = true) -> "VLESS"
            config.startsWith("vmess://", ignoreCase = true) -> "VMESS"
            config.startsWith("trojan://", ignoreCase = true) -> "TROJAN"
            config.startsWith("ss://", ignoreCase = true) -> "SHADOWSOCKS"
            config.startsWith("ssr://", ignoreCase = true) -> "SHADOWSOCKSR"
            config.startsWith("hysteria2://", ignoreCase = true) || config.startsWith("hy2://", ignoreCase = true) -> "HYSTERIA 2"
            config.startsWith("hysteria://", ignoreCase = true) -> "HYSTERIA"
            config.startsWith("tuic://", ignoreCase = true) -> "TUIC"
            config.startsWith("wireguard://", ignoreCase = true) -> "WIREGUARD"
            config.startsWith("socks5://", ignoreCase = true) -> "SOCKS5"
            config.contains("://") -> config.substringBefore("://").uppercase()
            else -> "V2RAY"
        }
    }

    private fun detectProxyProtocol(proxyUrl: String): String {
        val clean = proxyUrl.trim()
        return when {
            clean.contains("socks", ignoreCase = true) ||
            clean.startsWith("socks5://", ignoreCase = true) ||
            clean.startsWith("socks://", ignoreCase = true) -> "SOCKS5"

            clean.startsWith("ss://", ignoreCase = true) -> "Shadowsocks"
            clean.startsWith("vmess://", ignoreCase = true) -> "VMess"
            clean.startsWith("vless://", ignoreCase = true) -> "VLESS"
            clean.startsWith("trojan://", ignoreCase = true) -> "Trojan"

            clean.contains("proxy?server=", ignoreCase = true) ||
            clean.contains("tg://proxy", ignoreCase = true) ||
            clean.contains("t.me/proxy", ignoreCase = true) ||
            clean.contains("secret=", ignoreCase = true) -> {
                val secret = getQueryParam(clean, "secret") ?: ""
                if (secret.startsWith("ee", ignoreCase = true) ||
                    secret.startsWith("dd", ignoreCase = true) ||
                    secret.length > 32
                ) {
                    "MTProto (TLS)"
                } else {
                    "MTProto"
                }
            }

            clean.startsWith("http://", ignoreCase = true) || clean.startsWith("https://", ignoreCase = true) -> {
                if (clean.contains("t.me/socks") || clean.contains("telegram.me/socks")) "SOCKS5"
                else if (clean.contains("t.me/proxy") || clean.contains("telegram.me/proxy")) "MTProto"
                else "HTTP"
            }

            else -> "MTProto"
        }
    }

    private fun extractServerFromUrl(url: String): String {
        return try {
            val withoutScheme = if (url.contains("://")) url.substringAfter("://") else url
            val withoutFragment = withoutScheme.substringBefore("#").substringBefore("?")
            val hostPart = if (withoutFragment.contains("@")) withoutFragment.substringAfter("@") else withoutFragment
            val host = hostPart.substringBefore("/").trim()
            host
        } catch (e: Exception) {
            ""
        }
    }

    private fun extractProxyServerAndPort(proxyUrl: String): String {
        val server = getQueryParam(proxyUrl, "server")
        val port = getQueryParam(proxyUrl, "port")
        if (!server.isNullOrBlank() && !port.isNullOrBlank()) {
            return "$server:$port"
        } else if (!server.isNullOrBlank()) {
            return server
        }
        return extractServerFromUrl(proxyUrl)
    }

    private fun getQueryParam(url: String, key: String): String? {
        val query = url.substringAfter("?", "").substringBefore("#")
        if (query.isBlank()) return null
        for (param in query.split("&")) {
            val parts = param.split("=", limit = 2)
            if (parts.size == 2 && parts[0].equals(key, ignoreCase = true)) {
                return decodeUrlString(parts[1])
            }
        }
        return null
    }

    private fun decodeUrlString(input: String): String {
        return try {
            URLDecoder.decode(input, StandardCharsets.UTF_8.name())
        } catch (e: Exception) {
            input
        }
    }

    private fun decodeBase64Safe(encoded: String): ByteArray? {
        val sanitized = encoded.trim().replace("\n", "").replace("\r", "")
        return try {
            android.util.Base64.decode(sanitized, android.util.Base64.DEFAULT)
        } catch (e: Exception) {
            try {
                android.util.Base64.decode(sanitized, android.util.Base64.URL_SAFE)
            } catch (e2: Exception) {
                try {
                    android.util.Base64.decode(sanitized, android.util.Base64.NO_WRAP)
                } catch (e3: Exception) {
                    null
                }
            }
        }
    }
}
