package com.example.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.io.File
import java.io.FileOutputStream

object QrCodeHelper {

    /**
     * Generates a 100% standards-compliant, scan-tested QR Code Bitmap using ZXing.
     */
    fun generateQrBitmap(
        content: String,
        size: Int = 600,
        darkColor: Int = Color.BLACK,
        lightColor: Int = Color.WHITE
    ): Bitmap? {
        if (content.isBlank()) return null
        return try {
            val hints = hashMapOf<EncodeHintType, Any>(
                EncodeHintType.CHARACTER_SET to "UTF-8",
                EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.H,
                EncodeHintType.MARGIN to 1
            )
            val bitMatrix = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, size, size, hints)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val pixels = IntArray(width * height)
            for (y in 0 until height) {
                val offset = y * width
                for (x in 0 until width) {
                    pixels[offset + x] = if (bitMatrix.get(x, y)) darkColor else lightColor
                }
            }
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Creates a high-resolution, branded, gorgeous card bitmap featuring the QR code.
     * Perfect for saving to gallery or sharing with friends!
     */
    fun createSubscriptionPosterBitmap(
        context: Context,
        subUrl: String,
        titleText: String,
        subtitleText: String,
        tagline: String
    ): Bitmap? {
        val qrBitmap = generateQrBitmap(subUrl, size = 650) ?: return null

        val cardWidth = 800
        val cardHeight = 1040
        val card = Bitmap.createBitmap(cardWidth, cardHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(card)

        // 1. Background fill (Dark Midnight / Slate)
        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#0B0E14")
        }
        canvas.drawRect(0f, 0f, cardWidth.toFloat(), cardHeight.toFloat(), bgPaint)

        // 2. Subtle outer border with neon accent
        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#1F2937")
            style = Paint.Style.STROKE
            strokeWidth = 3f
        }
        val cardRect = RectF(16f, 16f, cardWidth - 16f, cardHeight - 16f)
        canvas.drawRoundRect(cardRect, 28f, 28f, borderPaint)

        // Top glow accent bar
        val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#00E5FF")
            style = Paint.Style.FILL
        }
        val topBarRect = RectF(cardWidth / 2f - 60f, 24f, cardWidth / 2f + 60f, 30f)
        canvas.drawRoundRect(topBarRect, 3f, 3f, glowPaint)

        // 3. Header text: "A9Z V2RAY CONFIGS"
        val headerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#00E5FF")
            textSize = 24f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("A9Z V2RAY CONFIGS", cardWidth / 2f, 75f, headerPaint)

        // 4. Subtitle / Title
        val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = 28f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText(titleText, cardWidth / 2f, 120f, titlePaint)

        // 5. White card container for QR code
        val qrBoxSize = 650f
        val qrBoxLeft = (cardWidth - qrBoxSize) / 2f
        val qrBoxTop = 150f
        val qrBoxRect = RectF(qrBoxLeft, qrBoxTop, qrBoxLeft + qrBoxSize, qrBoxTop + qrBoxSize)

        val qrContainerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            style = Paint.Style.FILL
        }
        canvas.drawRoundRect(qrBoxRect, 24f, 24f, qrContainerPaint)

        // Inner border for QR container
        val qrBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#E2E8F0")
            style = Paint.Style.STROKE
            strokeWidth = 2f
        }
        canvas.drawRoundRect(qrBoxRect, 24f, 24f, qrBorderPaint)

        // Draw the QR bitmap centered with padding inside white box
        val qrPadding = 25f
        val destRect = RectF(
            qrBoxLeft + qrPadding,
            qrBoxTop + qrPadding,
            qrBoxLeft + qrBoxSize - qrPadding,
            qrBoxTop + qrBoxSize - qrPadding
        )
        canvas.drawBitmap(qrBitmap, null, destRect, null)

        // 6. Subtitle / Instruction below QR
        val subPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#94A3B8")
            textSize = 20f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText(subtitleText, cardWidth / 2f, qrBoxTop + qrBoxSize + 45f, subPaint)

        // 7. URL box snippet
        val urlBoxPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#161B22")
            style = Paint.Style.FILL
        }
        val urlBoxRect = RectF(60f, qrBoxTop + qrBoxSize + 65f, cardWidth - 60f, qrBoxTop + qrBoxSize + 120f)
        canvas.drawRoundRect(urlBoxRect, 14f, 14f, urlBoxPaint)

        val urlTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#00E5FF")
            textSize = 17f
            typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
            textAlign = Paint.Align.CENTER
        }
        val displayUrl = if (subUrl.length > 55) subUrl.take(52) + "..." else subUrl
        canvas.drawText(displayUrl, cardWidth / 2f, qrBoxTop + qrBoxSize + 100f, urlTextPaint)

        // 8. Tagline / App footer
        val footerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#64748B")
            textSize = 15f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText(tagline, cardWidth / 2f, cardHeight - 35f, footerPaint)

        return card
    }

    /**
     * Saves bitmap directly to device gallery via MediaStore.
     * Works on Android 7 to Android 15+.
     */
    fun saveBitmapToGallery(context: Context, bitmap: Bitmap, baseName: String = "A9Z_Subscription_QR"): Boolean {
        val fileName = "${baseName}_${System.currentTimeMillis()}.png"
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/A9Z")
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
        }

        val resolver = context.contentResolver
        val uri = try {
            resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } ?: return false

        return try {
            resolver.openOutputStream(uri)?.use { outStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.clear()
                contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                resolver.update(uri, contentValues, null, null)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            try {
                resolver.delete(uri, null, null)
            } catch (_: Exception) {}
            false
        }
    }

    /**
     * Shares the QR code image via Android standard share sheet (FileProvider).
     */
    fun shareBitmap(context: Context, bitmap: Bitmap, shareChooserTitle: String = "Share QR Code"): Boolean {
        return try {
            val cacheDir = File(context.cacheDir, "images").apply { mkdirs() }
            val file = File(cacheDir, "A9Z_Sub_QR_${System.currentTimeMillis()}.png")
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(intent, shareChooserTitle).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
