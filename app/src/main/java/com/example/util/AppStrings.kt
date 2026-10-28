package com.example.util

/**
 * Supported app languages
 */
enum class AppLanguage(val code: String, val displayName: String, val shortBadge: String) {
    PERSIAN("fa", "فارسی", "FA"),
    ENGLISH("en", "English", "EN")
}

/**
 * Centralized multilingual dictionary for Persian and English UI.
 */
object AppStrings {

    fun appTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "سامانه پیکربندی A9Z"
        AppLanguage.ENGLISH -> "A9Z Config Center"
    }

    // Tabs
    fun tabConfigs(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "کانفیگ‌ها"
        AppLanguage.ENGLISH -> "Configs"
    }

    fun tabProxies(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "پروکسی‌ها"
        AppLanguage.ENGLISH -> "Proxies"
    }

    fun tabDownloads(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "دانلود و اشتراک"
        AppLanguage.ENGLISH -> "Downloads & Sub"
    }

    fun tabAdmin(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "پنل مدیر"
        AppLanguage.ENGLISH -> "Admin Panel"
    }

    // Header actions
    fun adminButtonTooltip(lang: AppLanguage, isLoggedIn: Boolean) = when (lang) {
        AppLanguage.PERSIAN -> if (isLoggedIn) "پنل مدیریت" else "ورود به پنل مدیر"
        AppLanguage.ENGLISH -> if (isLoggedIn) "Admin Panel" else "Admin Login"
    }

    fun languageSwitchTooltip(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "تغییر زبان به انگلیسی"
        AppLanguage.ENGLISH -> "Switch Language to Persian"
    }

    // Stats Cards
    fun totalConfigsTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "کل کانفیگ‌های فعال V2Ray"
        AppLanguage.ENGLISH -> "Active V2Ray Configs"
    }

    fun totalProxiesTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "پروکسی‌های فعال تلگرام"
        AppLanguage.ENGLISH -> "Active Telegram Proxies"
    }

    fun activeCountSuffix(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "عدد فعال"
        AppLanguage.ENGLISH -> "active"
    }

    fun lastUpdatePrefix(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "بروزرسانی:"
        AppLanguage.ENGLISH -> "Updated:"
    }

    fun notUpdated(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "بروزرسانی نشده"
        AppLanguage.ENGLISH -> "Not updated yet"
    }

    fun refresh(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "بروزرسانی"
        AppLanguage.ENGLISH -> "Refresh"
    }

    // Buttons
    fun copyAllConfigs(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "کپی کردن همه کانفیگ‌ها به کلیپ‌بورد"
        AppLanguage.ENGLISH -> "Copy All Configs to Clipboard"
    }

    fun copyAllProxies(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "کپی کردن همه پروکسی‌ها به کلیپ‌بورد"
        AppLanguage.ENGLISH -> "Copy All Proxies to Clipboard"
    }

    fun copyConfig(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "کپی کانفیگ"
        AppLanguage.ENGLISH -> "Copy Config"
    }

    fun copy(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "کپی"
        AppLanguage.ENGLISH -> "Copy"
    }

    fun connect(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "اتصال مستقیم"
        AppLanguage.ENGLISH -> "Connect"
    }

    fun download(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "دانلود"
        AppLanguage.ENGLISH -> "Download"
    }

    fun notConfigured(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "تنظیم نشده"
        AppLanguage.ENGLISH -> "Not configured"
    }

    // Empty states
    fun emptyConfigs(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "کانفیگی یافت نشد.\nبا کشیدن صفحه به پایین یا کلیک بر روی دکمه همگام‌سازی، مجدد تلاش کنید."
        AppLanguage.ENGLISH -> "No configs found.\nPull down or tap refresh to sync with server."
    }

    fun emptyProxies(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "پروکسی تلگرام یافت نشد.\nاطلاعات را همگام‌سازی کنید."
        AppLanguage.ENGLISH -> "No proxies found.\nPlease refresh to sync."
    }

    // Downloads & Sub section
    fun subHeaderTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "لینک اشتراک اختصاصی وی‌توری (Sub)"
        AppLanguage.ENGLISH -> "Dedicated V2Ray Subscription Link (Sub)"
    }

    fun subHeaderDesc(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "جهت وارد کردن در نرم‌افزارهای V2RayNG ،v2rayN ،Streisand و سایر کلاینت‌ها"
        AppLanguage.ENGLISH -> "For importing into V2RayNG, v2rayN, Streisand, and other client apps"
    }

    fun copySubLink(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "کپی لینک اشتراک"
        AppLanguage.ENGLISH -> "Copy Sub Link"
    }

    fun openInBrowser(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "باز کردن در مرورگر"
        AppLanguage.ENGLISH -> "Open in Browser"
    }

    fun qrCodeTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "اسکن بارکد QR اشتراک"
        AppLanguage.ENGLISH -> "Subscription QR Code"
    }

    fun saveQrToGallery(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "ذخیره تصویر"
        AppLanguage.ENGLISH -> "Save Image"
    }

    fun saveImage(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "ذخیره تصویر"
        AppLanguage.ENGLISH -> "Save Image"
    }

    fun shareQrImage(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "اشتراک‌گذاری تصویر"
        AppLanguage.ENGLISH -> "Share QR Image"
    }

    fun qrSavedSuccess(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "تصویر کارت QR با موفقیت در گالری ذخیره شد"
        AppLanguage.ENGLISH -> "QR code card saved to Gallery"
    }

    fun qrSaveFailed(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "خطا در ذخیره تصویر QR کد"
        AppLanguage.ENGLISH -> "Failed to save QR code image"
    }

    fun qrSubtitle(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "قابل اسکن با V2Ray، v2rayNG، Sing-box و Streisand"
        AppLanguage.ENGLISH -> "Scan with V2Ray, v2rayNG, Sing-box & Streisand"
    }

    fun qrScanHint(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "دوربین برنامه V2Ray را روی بارکد بگیرید یا عکس آن را در گالری ذخیره کنید"
        AppLanguage.ENGLISH -> "Scan with your V2Ray app or save the photo to your gallery"
    }

    fun showQrCode(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "نمایش بارکد QR"
        AppLanguage.ENGLISH -> "Show QR Code"
    }

    fun configQrModalTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "بارکد QR اتصال به کانفیگ"
        AppLanguage.ENGLISH -> "Config QR Code"
    }

    fun close(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "بستن"
        AppLanguage.ENGLISH -> "Close"
    }

    fun downloadToolsHeader(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "دانلود نرم‌افزارها و ابزارها"
        AppLanguage.ENGLISH -> "Software & Client Downloads"
    }

    fun downloadToolsDesc(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "دانلود کلاینت‌های V2Ray برای سیستم‌عامل‌های مختلف"
        AppLanguage.ENGLISH -> "Download V2Ray clients for multiple operating systems"
    }

    // Download software titles
    fun dlA9zTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "آپدیت"
        AppLanguage.ENGLISH -> "Update"
    }

    fun dlAndroidTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "کلاینت اندروید V2Ray (نسخه رسمی v2rayNG)"
        AppLanguage.ENGLISH -> "V2Ray Android Client (Official v2rayNG)"
    }

    fun dlWin64Title(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "کلاینت ویندوز V2Ray (نسخه ۶۴ بیتی)"
        AppLanguage.ENGLISH -> "V2Ray Windows Client (64-bit Edition)"
    }

    fun dlWin32Title(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "کلاینت ویندوز V2Ray (نسخه ۳۲ بیتی)"
        AppLanguage.ENGLISH -> "V2Ray Windows Client (32-bit Edition)"
    }

    fun openLink(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "باز کردن لینک"
        AppLanguage.ENGLISH -> "Open Link"
    }

    fun copyLink(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "کپی لینک"
        AppLanguage.ENGLISH -> "Copy Link"
    }

    fun subNotSet(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "لینک اشتراک در حال حاضر تنظیم نشده است."
        AppLanguage.ENGLISH -> "Subscription link is currently not configured."
    }

    // Login Dialog
    fun loginTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "ورود مدیر سامانه"
        AppLanguage.ENGLISH -> "System Admin Login"
    }

    fun loginDesc(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "جهت ورود به بخش ویرایش اطلاعات، نام کاربری و پسوورد را وارد نمایید."
        AppLanguage.ENGLISH -> "Enter your username and password to access the administration panel."
    }

    fun usernameLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "نام کاربری"
        AppLanguage.ENGLISH -> "Username"
    }

    fun passwordLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "رمزعبور"
        AppLanguage.ENGLISH -> "Password"
    }

    fun biometricLoginPrompt(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "تأیید هویت با اثر انگشت"
        AppLanguage.ENGLISH -> "Biometric Authentication"
    }

    fun biometricLoginSubtitle(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "جهت ورود به پنل ادمین، حسگر اثر انگشت را لمس کنید"
        AppLanguage.ENGLISH -> "Touch the fingerprint sensor to enter admin panel"
    }

    fun biometricButton(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "ورود سریع با اثر انگشت"
        AppLanguage.ENGLISH -> "Quick Fingerprint Login"
    }

    fun biometricNotConfigured(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "ابتدا باید یک‌بار با نام کاربری و رمز عبور وارد شوید تا اثر انگشت فعال شود."
        AppLanguage.ENGLISH -> "You must log in with username and password once before using fingerprint."
    }

    fun biometricFailed(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "احراز هویت بیومتریک انجام نشد."
        AppLanguage.ENGLISH -> "Biometric authentication failed."
    }

    fun publish(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "انتشار"
        AppLanguage.ENGLISH -> "Share"
    }

    fun proxyQrModalTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "انتشار و بارکد پروکسی"
        AppLanguage.ENGLISH -> "Proxy QR Code & Share"
    }

    fun proxyQrScanHint(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "جهت اشتراک یا اتصال در تلگرام، بارکد را اسکن کنید"
        AppLanguage.ENGLISH -> "Scan QR code to connect or share proxy"
    }

    fun filterAll(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "همه"
        AppLanguage.ENGLISH -> "All"
    }

    fun initialLoading(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "در حال بارگذاری اطلاعات..."
        AppLanguage.ENGLISH -> "Loading configurations & proxies..."
    }

    fun cancel(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "انصراف"
        AppLanguage.ENGLISH -> "Cancel"
    }

    fun loginSubmit(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "ورود به پنل"
        AppLanguage.ENGLISH -> "Sign In"
    }

    fun showPassword(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "نمایش"
        AppLanguage.ENGLISH -> "Show"
    }

    fun hidePassword(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "پنهان‌سازی"
        AppLanguage.ENGLISH -> "Hide"
    }

    // Admin Dashboard
    fun adminDashboardTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "پنل مدیریتی انتشار اطلاعات"
        AppLanguage.ENGLISH -> "Data Publishing Admin Panel"
    }

    fun logout(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "خروج از پنل"
        AppLanguage.ENGLISH -> "Logout"
    }

    fun offlineWarning(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "عدم اتصال به سرور. اطلاعات نمایش داده شده آفلاین هستند."
        AppLanguage.ENGLISH -> "Unable to connect to server. Displaying offline cached data."
    }

    fun savingChanges(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "در حال ذخیره تغییرات روی سرور..."
        AppLanguage.ENGLISH -> "Saving changes to server..."
    }

    fun save(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "ذخیره"
        AppLanguage.ENGLISH -> "Save"
    }

    fun pasteFromClipboard(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "کلیپ‌بورد"
        AppLanguage.ENGLISH -> "Clipboard"
    }

    fun adminConfigsTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "پیکربندی‌های فعال V2Ray"
        AppLanguage.ENGLISH -> "Active V2Ray Configs"
    }

    fun adminConfigsSubtitle(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "کانفیگ‌ها را به صورت خط به خط با کلید اینتر از هم جدا کنید."
        AppLanguage.ENGLISH -> "Separate configs line-by-line using the Enter key."
    }

    fun adminProxiesTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "پروکسی‌های فعال تلگرام"
        AppLanguage.ENGLISH -> "Active Telegram Proxies"
    }

    fun adminProxiesSubtitle(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "لینک‌های اتصال پروکسی تلگرام را خط به خط وارد کنید."
        AppLanguage.ENGLISH -> "Enter Telegram proxy connection links line-by-line."
    }

    fun adminSubTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "لینک اشتراک V2Ray (Subscription Link)"
        AppLanguage.ENGLISH -> "V2Ray Subscription Link"
    }

    fun adminSubSubtitle(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "آدرس اشتراک که کلاینت‌ها برای بروزرسانی از آن استفاده می‌کنند."
        AppLanguage.ENGLISH -> "Subscription URL that client applications use to fetch updates."
    }

    fun adminDownloadsTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "تنظیم لینک‌های دانلود ابزارها"
        AppLanguage.ENGLISH -> "Configure Download Links"
    }

    fun adminDownloadsSubtitle(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "تک تک لینک‌های دانلود سرویس‌های کلاینت و این برنامه را تنظیم و ذخیره کنید."
        AppLanguage.ENGLISH -> "Configure and save download URLs for client software and this app."
    }

    fun adminDlA9zLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "لینک دانلود برنامه (اپ اختصاصی A9Z)"
        AppLanguage.ENGLISH -> "App Download Link (A9Z Android App)"
    }

    fun adminDlAndroidLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "لینک دانلود وی‌توری اندروید (Android client)"
        AppLanguage.ENGLISH -> "V2Ray Android Client Download Link"
    }

    fun adminDlWin32Label(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "لینک دانلود وی‌توری ویندوز ۳۲ بیتی (Win32)"
        AppLanguage.ENGLISH -> "V2Ray Windows 32-bit Download Link"
    }

    fun adminDlWin64Label(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "لینک دانلود وی‌توری ویندوز ۶۴ بیتی (Win64)"
        AppLanguage.ENGLISH -> "V2Ray Windows 64-bit Download Link"
    }

    fun saveAllDownloadLinks(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "ذخیره همه‌ لینک‌های دانلود"
        AppLanguage.ENGLISH -> "Save All Download Links"
    }

    // Success and error messages
    fun saveConfigsSuccess(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "کانفیگ‌ها با موفقیت ذخیره شدند."
        AppLanguage.ENGLISH -> "Configs saved successfully."
    }

    fun saveConfigsError(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "خطا در ذخیره‌سازی کانفیگ‌ها."
        AppLanguage.ENGLISH -> "Failed to save configs."
    }

    fun saveProxiesSuccess(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "پروکسی‌ها با موفقیت ذخیره شدند."
        AppLanguage.ENGLISH -> "Proxies saved successfully."
    }

    fun saveProxiesError(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "خطا در ذخیره‌سازی پروکسی‌ها."
        AppLanguage.ENGLISH -> "Failed to save proxies."
    }

    fun saveSubSuccess(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "لینک اشتراک با موفقیت ذخیره شد."
        AppLanguage.ENGLISH -> "Subscription link saved successfully."
    }

    fun saveSubError(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "خطا در ذخیره‌سازی لینک اشتراک."
        AppLanguage.ENGLISH -> "Failed to save subscription link."
    }

    fun saveDownloadsSuccess(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "لینک‌های دانلود با موفقیت ذخیره شدند."
        AppLanguage.ENGLISH -> "Download links saved successfully."
    }

    fun saveDownloadsError(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "خطا در ذخیره‌سازی لینک‌های دانلود."
        AppLanguage.ENGLISH -> "Failed to save download links."
    }

    // Login errors
    fun loginErrorEmpty(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "لطفاً نام کاربری و رمز عبور را وارد کنید."
        AppLanguage.ENGLISH -> "Please enter username and password."
    }

    fun loginErrorInvalid(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "نام کاربری یا کلمه عبور اشتباه است."
        AppLanguage.ENGLISH -> "Invalid username or password."
    }

    fun loginErrorNetwork(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "خطا در ارتباط با سرور. لطفا اتصال اینترنت را بررسی کنید."
        AppLanguage.ENGLISH -> "Server connection error. Please check your internet connection."
    }

    // Toasts
    fun copiedToast(label: String, lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "$label کپی شد"
        AppLanguage.ENGLISH -> "$label copied to clipboard"
    }

    fun clipboardAccessError(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "خطا در دسترسی به کلیپ‌بورد"
        AppLanguage.ENGLISH -> "Failed to access clipboard"
    }

    fun clipboardPasted(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "متن با موفقیت از کلیپ‌بورد قرار داده شد"
        AppLanguage.ENGLISH -> "Pasted from clipboard successfully"
    }

    fun clipboardEmpty(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "کلیپ‌بورد خالی است!"
        AppLanguage.ENGLISH -> "Clipboard is empty!"
    }

    fun clipboardReadError(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "خطا در خواندن کلیپ‌بورد"
        AppLanguage.ENGLISH -> "Failed to read from clipboard"
    }

    // Labels used in clipboard
    fun allConfigsLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "تمام کانفیگ‌ها"
        AppLanguage.ENGLISH -> "All Configs"
    }

    fun allProxiesLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "تمام پروکسی‌ها"
        AppLanguage.ENGLISH -> "All Proxies"
    }

    fun singleConfigLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "کانفیگ"
        AppLanguage.ENGLISH -> "Config"
    }

    fun singleProxyLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "لینک پروکسی"
        AppLanguage.ENGLISH -> "Proxy Link"
    }

    fun subLinkLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "لینک اشتراک"
        AppLanguage.ENGLISH -> "Subscription Link"
    }

    // Windscribe Tab & User Module
    fun tabWindscribe(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "Windscibe"
        AppLanguage.ENGLISH -> "Windscibe"
    }

    fun windscribeHeaderTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "دریافت ایمیل موقت اختصاصی Windscribe"
        AppLanguage.ENGLISH -> "Dedicated Windscribe Temp Email"
    }

    fun windscribeHeaderDesc(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "تولید آنی ایمیل یک‌بارمصرف جهت ثبت‌نام و دریافت اتوماتیک لینک تایید وینداسکرایب"
        AppLanguage.ENGLISH -> "Instant disposable email generator for Windscribe signup with automatic verification link extraction"
    }

    fun generateTempEmailBtn(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "دریافت ایمیل"
        AppLanguage.ENGLISH -> "Get Email"
    }

    fun quotaCompletedBtn(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "سقف مجاز تکمیل شده است"
        AppLanguage.ENGLISH -> "Quota Limit Reached"
    }

    fun generatingEmail(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "در حال ساخت ایمیل اختصاصی..."
        AppLanguage.ENGLISH -> "Generating dedicated email..."
    }

    fun quotaRemainingLabel(remaining: Int, max: Int, days: Int, lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "سهمیه ساخت ایمیل: $remaining از $max ایمیل (در بازه $days روزه)"
        AppLanguage.ENGLISH -> "Email Quota: $remaining / $max remaining (per $days days)"
    }

    fun activeEmailLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "آدرس ایمیل موقت شما:"
        AppLanguage.ENGLISH -> "Your Temporary Email:"
    }

    fun waitingForWindscribeEmail(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "در انتظار دریافت ایمیل تایید از سمت Windscribe (بررسی خودکار)..."
        AppLanguage.ENGLISH -> "Waiting for Windscribe verification email (Auto-polling)..."
    }

    fun verificationLinkReceived(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "لینک تایید حساب Windscribe دریافت شد!"
        AppLanguage.ENGLISH -> "Windscribe Verification Link Received!"
    }

    fun copyVerificationLink(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "کپی لینک تایید"
        AppLanguage.ENGLISH -> "Copy Link"
    }

    fun openVerificationLink(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "باز کردن در مرورگر"
        AppLanguage.ENGLISH -> "Open in Browser"
    }

    fun checkInboxNow(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "بررسی مجدد صندوق"
        AppLanguage.ENGLISH -> "Check Inbox"
    }

    fun deleteActiveEmail(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "حذف و ایمیل جدید"
        AppLanguage.ENGLISH -> "Delete & New Email"
    }

    fun windscribeGuideTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "راهنمای نصب و راه اندازی"
        AppLanguage.ENGLISH -> "Installation & Setup Guide"
    }

    fun windscribeGuideStep1(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "۱. ابتدا با دکمه بالا یک ایمیل موقت اختصاصی دریافت و آدرس آن را کپی کنید."
        AppLanguage.ENGLISH -> "1. Get a temporary email using the button above and copy the email address."
    }

    fun windscribeGuideStep2(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "۲. وارد برنامه یا سایت Windscribe شوید، یک نام کاربری و رمز عبور انتخاب کنید و ایمیل کپی‌شده را در بخش Email وارد نمایید."
        AppLanguage.ENGLISH -> "2. Open Windscribe app or website, pick a username & password, and paste the temporary email."
    }

    fun windscribeGuideStep3(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "۳. در همین صفحه منتظر بمانید تا لینک تایید (Confirm Email) به صورت خودکار دریافت شود؛ سپس روی «تایید مستقیم» کلیک کنید تا ۱۰ گیگابایت فعال شود."
        AppLanguage.ENGLISH -> "3. Wait here for the Confirm Email link to arrive automatically, then click Confirm to unlock 10GB."
    }

    // App Update Center Strings
    fun appUpdateSectionTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "بخش آپدیت و بروزرسانی برنامه A9Z"
        AppLanguage.ENGLISH -> "A9Z App Update Center"
    }

    fun appUpdateSectionDesc(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "بررسی خودکار انتشار نسخه جدید بر اساس لینک آپدیت سرور یا ریلیزهای گیت‌هاب پروژه"
        AppLanguage.ENGLISH -> "Automatically check for new versions via server update link or GitHub releases"
    }

    fun currentAppVersionLabel(version: String, lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "نسخه فعلی نصب‌شده: v$version"
        AppLanguage.ENGLISH -> "Current Installed Version: v$version"
    }

    fun checkForUpdateBtn(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "بررسی وجود آپدیت جدید"
        AppLanguage.ENGLISH -> "Check for Updates"
    }

    fun checkingForUpdates(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "در حال بررسی لینک آپدیت و ریلیزهای گیت‌هاب..."
        AppLanguage.ENGLISH -> "Checking server link & GitHub releases..."
    }

    fun updateAvailableTitle(versionOrSource: String, lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "نسخه جدید آماده دریافت است ($versionOrSource)"
        AppLanguage.ENGLISH -> "New Update Available ($versionOrSource)"
    }

    fun appIsUpToDate(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "برنامه شما به آخرین نسخه بروز است."
        AppLanguage.ENGLISH -> "Your application is up to date."
    }

    fun updateNowBtn(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "آپدیت"
        AppLanguage.ENGLISH -> "Update"
    }

    fun downloadingUpdateProgress(percent: Int, lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "در حال دانلود و نصب آپدیت... ($percent%)"
        AppLanguage.ENGLISH -> "Downloading & Installing Update... ($percent%)"
    }

    // Multi-Platform Downloads Titles
    fun v2rayDownloadsTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "لینک‌های دانلود V2Ray"
        AppLanguage.ENGLISH -> "V2Ray Client Downloads"
    }

    fun windscribeDownloadsTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "لینک‌های دانلود Windscribe VPN"
        AppLanguage.ENGLISH -> "Windscribe VPN Downloads"
    }

    fun dlIosV2rayTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "نسخه آی‌اواس V2Ray (FoXray / V2Box / Streisand)"
        AppLanguage.ENGLISH -> "V2Ray iOS Client (FoXray / V2Box / Streisand)"
    }

    fun dlMacV2rayTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "نسخه مک‌اواس V2Ray (V2RayXS / FoxRay)"
        AppLanguage.ENGLISH -> "V2Ray macOS Client (V2RayXS)"
    }

    fun dlWsWindowsTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "نسخه ویندوز Windscribe (Windows)"
        AppLanguage.ENGLISH -> "Windscribe for Windows"
    }

    fun dlWsMacTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "نسخه مک‌اواس Windscribe (macOS)"
        AppLanguage.ENGLISH -> "Windscribe for macOS"
    }

    fun dlWsAndroidTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "نسخه اندروید Windscribe (Android)"
        AppLanguage.ENGLISH -> "Windscribe for Android"
    }

    fun dlWsIosTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "نسخه آی‌اواس Windscribe (iOS)"
        AppLanguage.ENGLISH -> "Windscribe for iOS"
    }

    fun quickDownloadAndroidBanner(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "دانلود کلاینت اندروید V2Ray (نسخه رسمی v2rayNG)"
        AppLanguage.ENGLISH -> "Download V2Ray Android Client (v2rayNG)"
    }

    fun quickDownloadIosBanner(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "دانلود کلاینت آی‌اواس V2Ray (FoXray / V2Box)"
        AppLanguage.ENGLISH -> "Download V2Ray iOS Client (FoXray / V2Box)"
    }

    // Admin Sub-tabs & Sections
    fun adminTabConfigs(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "کانفیگ و پروکسی"
        AppLanguage.ENGLISH -> "Configs & Proxies"
    }

    fun adminTabWindscribe(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "Windscribe"
        AppLanguage.ENGLISH -> "Windscribe"
    }

    fun adminTabDownloads(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "لینک‌های دانلود"
        AppLanguage.ENGLISH -> "Download Links"
    }

    fun adminTabStats(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "آمار و تحلیل"
        AppLanguage.ENGLISH -> "System Stats"
    }

    fun adminDlIosLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "لینک دانلود نسخه iOS (FoXray / V2Box)"
        AppLanguage.ENGLISH -> "V2Ray iOS Client Download Link (FoXray / V2Box)"
    }

    fun adminDlWsWindowsLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "لینک دانلود وینداسکرایب ویندوز (Windows)"
        AppLanguage.ENGLISH -> "Windscribe Windows Download Link"
    }

    fun adminDlWsAndroidLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "لینک دانلود وینداسکرایب اندروید (Android)"
        AppLanguage.ENGLISH -> "Windscribe Android Download Link"
    }

    fun adminDlWsIosLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "لینک دانلود وینداسکرایب آی‌اواس (iOS)"
        AppLanguage.ENGLISH -> "Windscribe iOS Download Link"
    }

    fun adminDlWsMacLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "لینک دانلود وینداسکرایب مک‌اواس (macOS)"
        AppLanguage.ENGLISH -> "Windscribe macOS Download Link"
    }

    // Admin Windscribe Management
    fun wsAdminQuotaSettingsTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "تنظیمات سهمیه و زمان انقضای ایمیل موقت"
        AppLanguage.ENGLISH -> "Temp Email Quota & Lifespan Settings"
    }

    fun wsMaxEmailsLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "سقف تعداد ایمیل مجاز هر کاربر:"
        AppLanguage.ENGLISH -> "Max Emails per User:"
    }

    fun wsPeriodDaysLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "دوره محدودیت (روز):"
        AppLanguage.ENGLISH -> "Quota Period (Days):"
    }

    fun wsLifespanMinutesLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "مدت اعتبار هر ایمیل (دقیقه):"
        AppLanguage.ENGLISH -> "Email Lifespan (Minutes):"
    }

    fun wsAdminManualCreateTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "ساخت ایمیل دستی یا تصادفی"
        AppLanguage.ENGLISH -> "Create Custom or Random Email"
    }

    fun wsCustomUsernameHint(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "نام دلخواه ایمیل (اختیاری - خالی = تصادفی)"
        AppLanguage.ENGLISH -> "Custom username (optional - blank = random)"
    }

    fun wsCreateEmailBtn(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "ساخت ایمیل در سرور"
        AppLanguage.ENGLISH -> "Create Email on Server"
    }

    fun wsActiveEmailsListTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "لیست ایمیل‌های فعال و صندوق ورودی"
        AppLanguage.ENGLISH -> "Active Emails & Inbox Viewer"
    }

    fun wsDeleteAllEmailsBtn(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "پاکسازی کل ایمیل‌ها"
        AppLanguage.ENGLISH -> "Purge All Emails"
    }

    fun wsViewInboxBtn(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "مشاهده صندوق"
        AppLanguage.ENGLISH -> "Inbox"
    }

    fun wsStatusVerified(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "تایید شده"
        AppLanguage.ENGLISH -> "VERIFIED"
    }

    fun wsStatusPending(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "در انتظار تایید"
        AppLanguage.ENGLISH -> "PENDING"
    }

    // Statistics & Analytics
    fun statsFilterToday(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "امروز (۲۴ساعت)"
        AppLanguage.ENGLISH -> "Today (24h)"
    }

    fun statsFilterWeekly(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "هفتگی (۷روز)"
        AppLanguage.ENGLISH -> "Weekly (7d)"
    }

    fun statsFilterMonthly(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "ماهانه (۳۰روز)"
        AppLanguage.ENGLISH -> "Monthly (30d)"
    }

    fun statsFilterYearly(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "سالانه (۱سال)"
        AppLanguage.ENGLISH -> "Yearly (1y)"
    }

    fun statsFilterAllTime(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "کل دوران"
        AppLanguage.ENGLISH -> "All-Time"
    }

    fun statsUserLoginsTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "ورود موفق کاربران عادی"
        AppLanguage.ENGLISH -> "Successful User Logins"
    }

    fun statsAdminLoginsTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "ورود مدیران سامانه"
        AppLanguage.ENGLISH -> "Admin Logins"
    }

    fun statsTotalAttemptsTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "کل تلاش‌های ورود (لاگ‌ها)"
        AppLanguage.ENGLISH -> "Total Login Attempts (Logs)"
    }

    fun statsActiveTempEmailsTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "ایمیل‌های موقت ساخته‌شده"
        AppLanguage.ENGLISH -> "Created Temp Emails"
    }

    fun statsClarificationNote(lang: AppLanguage) = when (lang) {
        AppLanguage.PERSIAN -> "مبنای آمار: تعداد ورودهای موفق کاربران تفکیک‌شده بر اساس بازه زمانی انتخابی در کنار کل لاگ‌های ثبت‌شده در سامانه نمایش داده می‌شود."
        AppLanguage.ENGLISH -> "Analytics Basis: Successful user logins filtered by the selected period are shown alongside total system log attempts."
    }

    fun themeSwitchTooltip(lang: AppLanguage, isDark: Boolean) = when (lang) {
        AppLanguage.PERSIAN -> if (isDark) "تغییر به تم روشن" else "تغییر به تم تاریک"
        AppLanguage.ENGLISH -> if (isDark) "Switch to Light Theme" else "Switch to Dark Theme"
    }
}



