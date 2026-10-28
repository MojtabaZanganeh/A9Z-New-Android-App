package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("A9Z", appName)
  }

  @Test
  fun `test qr code generation in android environment`() {
    val testUrl = "https://raw.githubusercontent.com/username/sub/main/all.txt"
    val qrBitmap = com.example.util.QrCodeHelper.generateQrBitmap(testUrl, size = 300)
    org.junit.Assert.assertNotNull(qrBitmap)
    org.junit.Assert.assertEquals(300, qrBitmap?.width)
    org.junit.Assert.assertEquals(300, qrBitmap?.height)

    val emptyBitmap = com.example.util.QrCodeHelper.generateQrBitmap("", size = 300)
    org.junit.Assert.assertNull(emptyBitmap)
  }

  @Test
  fun `test single config qr code generation`() {
    val vmessConfig = "vmess://eyJhZGQiOiIxMjcuMC4wLjEiLCJhaWQiOiIwIiwiaG9zdCI6IiIsImlkIjoiMTIzNDU2NzgtMTIzNC0xMjM0LTEyMzQtMTIzNDU2Nzg5MGFiIiwibmV0Ijoid3MiLCJwYXRoIjoiL3ZtZXNzIiwicG9ydCI6IjQ0MyIsInBzIjoiVGVzdFNlcnZlciIsInNjeSI6ImF1dG8iLCJzbmkiOiIiLCJ0bHMiOiJ0bHMiLCJ0eXBlIjoibm9uZSIsInYiOiIyIn0="
    val qrBitmap = com.example.util.QrCodeHelper.generateQrBitmap(vmessConfig, size = 500)
    org.junit.Assert.assertNotNull(qrBitmap)
    org.junit.Assert.assertEquals(500, qrBitmap?.width)
    org.junit.Assert.assertEquals(500, qrBitmap?.height)
  }
}
