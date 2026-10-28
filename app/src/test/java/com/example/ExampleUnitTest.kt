package com.example

import com.example.util.ConfigParser
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testConfigTitleExtraction() {
        val rawConfig = "vless://abcd-1234@server.com:443?security=reality#Server%20Germany%20MCI"
        val parsed = ConfigParser.parseConfig(rawConfig, 1)

        assertEquals("Server Germany MCI", parsed.title)
        assertEquals("VLESS", parsed.protocol)
        assertEquals("server.com:443", parsed.serverInfo)
    }

    @Test
    fun testConfigWithPersianTitle() {
        val rawConfig = "trojan://pass@1.2.3.4:443#سرور_اختصاصی_آلمان"
        val parsed = ConfigParser.parseConfig(rawConfig, 2)

        assertEquals("سرور_اختصاصی_آلمان", parsed.title)
        assertEquals("TROJAN", parsed.protocol)
        assertEquals("1.2.3.4:443", parsed.serverInfo)
    }

    @Test
    fun testProxyProtocolDetection() {
        val mtprotoUrl = "tg://proxy?server=142.250.180.3&port=443&secret=ee1234567890abcdef"
        val parsedMtproto = ConfigParser.parseProxy(mtprotoUrl)

        assertEquals("MTProto (TLS)", parsedMtproto.protocol)
        assertEquals("142.250.180.3:443", parsedMtproto.serverInfo)

        val socks5Url = "https://t.me/socks?server=proxy.example.com&port=1080"
        val parsedSocks5 = ConfigParser.parseProxy(socks5Url)

        assertEquals("SOCKS5", parsedSocks5.protocol)
        assertEquals("proxy.example.com:1080", parsedSocks5.serverInfo)
    }

    @Test
    fun testPersianDateConversion() {
        // "2026/09/06 18:04" -> September 6, 2026 is 15 Shahrivar 1405
        val result = com.example.util.PersianDateConverter.formatToPersianDateTime("2026/09/06 18:04")
        assertEquals("15 شهریور 1405 - 18:04", result)

        val result2 = com.example.util.PersianDateConverter.formatToPersianDateTime("2026/08/30 01:57")
        assertEquals("8 شهریور 1405 - 01:57", result2)

        val emptyResult = com.example.util.PersianDateConverter.formatToPersianDateTime(null)
        assertEquals("بروزرسانی نشده", emptyResult)
    }

    @Test
    fun testAppStringsLocalization() {
        val fa = com.example.util.AppLanguage.PERSIAN
        val en = com.example.util.AppLanguage.ENGLISH

        assertEquals("فارسی", fa.displayName)
        assertEquals("English", en.displayName)

        assertEquals("کپی کردن همه کانفیگ‌ها به کلیپ‌بورد", com.example.util.AppStrings.copyAllConfigs(fa))
        assertEquals("Copy All Configs to Clipboard", com.example.util.AppStrings.copyAllConfigs(en))

        assertEquals("دانلود", com.example.util.AppStrings.download(fa))
        assertEquals("Download", com.example.util.AppStrings.download(en))

        assertEquals("ورود مدیر سامانه", com.example.util.AppStrings.loginTitle(fa))
        assertEquals("System Admin Login", com.example.util.AppStrings.loginTitle(en))

        // Date formatting based on language
        val faDate = com.example.util.PersianDateConverter.formatDateTime("2026/09/06 18:04", fa)
        val enDate = com.example.util.PersianDateConverter.formatDateTime("2026/09/06 18:04", en)
        assertEquals("15 شهریور 1405 - 18:04", faDate)
        assertEquals("Sep 6, 2026 - 18:04", enDate)

        // QR Code Localization
        assertEquals("ذخیره تصویر", com.example.util.AppStrings.saveImage(fa))
        assertEquals("Save Image", com.example.util.AppStrings.saveImage(en))
        assertEquals("ذخیره تصویر", com.example.util.AppStrings.saveQrToGallery(fa))
        assertEquals("Save Image", com.example.util.AppStrings.saveQrToGallery(en))
        assertEquals("اشتراک‌گذاری تصویر", com.example.util.AppStrings.shareQrImage(fa))
        assertEquals("Share QR Image", com.example.util.AppStrings.shareQrImage(en))
    }
}

