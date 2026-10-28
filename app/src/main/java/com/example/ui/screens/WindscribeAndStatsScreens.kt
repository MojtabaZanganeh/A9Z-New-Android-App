package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.CheckWindscribeCodeResponse
import com.example.data.model.WindscribeEmailRecord
import com.example.ui.theme.ActiveNeonCyan
import com.example.ui.theme.MidnightBg
import com.example.ui.theme.MidnightSurface
import com.example.ui.theme.MidnightSurfaceVariant
import com.example.ui.theme.ThemeCardBorder
import com.example.ui.theme.ThemeCodeBoxBg
import com.example.ui.theme.ThemeErrorRed
import com.example.ui.theme.ThemeOnAccent
import com.example.ui.theme.ThemePurpleAccent
import com.example.ui.theme.ThemeSuccessGreen
import com.example.ui.theme.ThemeTextPrimary
import com.example.ui.theme.ThemeTextSecondary
import com.example.ui.theme.ThemeWarningAmber
import com.example.ui.theme.isAppDarkTheme
import com.example.ui.viewmodel.AppViewModel
import com.example.util.AppLanguage
import com.example.util.AppStrings
import com.example.util.PersianDateConverter

@Composable
fun WindscribeUserView(
    viewModel: AppViewModel,
    currentLang: AppLanguage
) {
    val context = LocalContext.current
    val cachedData by viewModel.cachedData.collectAsStateWithLifecycle()
    val quota by viewModel.windscribeQuota.collectAsStateWithLifecycle()
    val activeEmail by viewModel.activeUserEmail.collectAsStateWithLifecycle()
    val isGenerating by viewModel.isGeneratingEmail.collectAsStateWithLifecycle()
    val isCheckingInbox by viewModel.isCheckingInbox.collectAsStateWithLifecycle()
    val statusMsg by viewModel.emailStatusMessage.collectAsStateWithLifecycle()
    val remainingSec by viewModel.remainingSeconds.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Hero Header & Temp Email Generator Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MidnightSurface),
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(1.dp, ActiveNeonCyan.copy(alpha = 0.35f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(ActiveNeonCyan.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            tint = ActiveNeonCyan,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = AppStrings.windscribeHeaderTitle(currentLang),
                            color = ThemeTextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        val remainingQuota = (quota.max - quota.used).coerceAtLeast(0)
                        Text(
                            text = AppStrings.quotaRemainingLabel(
                                remaining = remainingQuota,
                                max = quota.max,
                                days = quota.periodDays,
                                lang = currentLang
                            ),
                            color = ActiveNeonCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = AppStrings.windscribeHeaderDesc(currentLang),
                    color = ThemeTextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 18.sp
                )

                if (!statusMsg.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        color = ThemeErrorRed.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, ThemeErrorRed.copy(alpha = 0.45f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = statusMsg ?: "",
                            color = ThemeErrorRed,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (activeEmail == null) {
                    val isQuotaExhausted = quota.max > 0 && (quota.used >= quota.max || quota.remaining <= 0)
                    Button(
                        onClick = { viewModel.generateUserWindscribeEmail() },
                        enabled = !isGenerating && !isQuotaExhausted,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("generate_windscribe_email_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isQuotaExhausted) ThemeErrorRed.copy(alpha = 0.15f) else ActiveNeonCyan,
                            contentColor = if (isQuotaExhausted) ThemeErrorRed else ThemeOnAccent,
                            disabledContainerColor = if (isQuotaExhausted) ThemeErrorRed.copy(alpha = 0.15f) else ActiveNeonCyan.copy(alpha = 0.4f),
                            disabledContentColor = if (isQuotaExhausted) ThemeErrorRed else ThemeOnAccent
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (isGenerating) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = ThemeOnAccent,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = AppStrings.generatingEmail(currentLang),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        } else if (isQuotaExhausted) {
                            Icon(Icons.Default.Block, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = AppStrings.quotaCompletedBtn(currentLang),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        } else {
                            Icon(Icons.Default.AddCircleOutline, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = AppStrings.generateTempEmailBtn(currentLang),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                } else {
                    val rec = activeEmail!!
                    val mins = remainingSec / 60
                    val secs = remainingSec % 60
                    val timeFormatted = String.format("%02d:%02d", mins, secs)

                    // Active Email Display Box
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(MidnightSurfaceVariant)
                            .border(1.dp, ActiveNeonCyan.copy(alpha = 0.35f), RoundedCornerShape(14.dp))
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = AppStrings.activeEmailLabel(currentLang),
                                color = ThemeTextSecondary,
                                fontSize = 11.sp
                            )
                            // Countdown Timer Pill
                            Surface(
                                color = if (remainingSec > 60) ActiveNeonCyan.copy(alpha = 0.15f) else Color(0xFFFF5252).copy(alpha = 0.2f),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(
                                    0.8.dp,
                                    if (remainingSec > 60) ActiveNeonCyan else Color(0xFFFF5252)
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Timer,
                                        contentDescription = null,
                                        tint = if (remainingSec > 60) ActiveNeonCyan else Color(0xFFFF5252),
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = timeFormatted,
                                        color = ThemeTextPrimary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                        }

                        // Email Address Copy Row
                        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(ThemeCodeBoxBg)
                                    .clickable {
                                        viewModel.copyToClipboard(rec.email, "Email")
                                    }
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = rec.email,
                                    color = ActiveNeonCyan,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = AppStrings.copy(currentLang),
                                    tint = ActiveNeonCyan,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        // Verification Link Status (Email only contains a verification link, no code)
                        val hasLink = !rec.link.isNullOrBlank()

                        if (hasLink) {
                            val linkUrl = rec.link ?: ""
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = ThemeSuccessGreen.copy(alpha = 0.12f)
                                ),
                                border = BorderStroke(1.dp, ThemeSuccessGreen.copy(alpha = 0.6f)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = ThemeSuccessGreen,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = AppStrings.verificationLinkReceived(currentLang),
                                            color = ThemeSuccessGreen,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Button(
                                            onClick = {
                                                runCatching {
                                                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(linkUrl)))
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = ThemeSuccessGreen,
                                                contentColor = ThemeOnAccent
                                            ),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(42.dp),
                                            shape = RoundedCornerShape(10.dp)
                                        ) {
                                            Icon(Icons.Default.OpenInBrowser, contentDescription = null, modifier = Modifier.size(15.dp))
                                            Spacer(modifier = Modifier.width(5.dp))
                                            Text(
                                                text = AppStrings.openVerificationLink(currentLang),
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }

                                        OutlinedButton(
                                            onClick = {
                                                viewModel.copyToClipboard(linkUrl, "Confirm Link")
                                            },
                                            border = BorderStroke(1.dp, ThemeSuccessGreen),
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = ThemeSuccessGreen),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(42.dp),
                                            shape = RoundedCornerShape(10.dp)
                                        ) {
                                            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(15.dp))
                                            Spacer(modifier = Modifier.width(5.dp))
                                            Text(
                                                text = AppStrings.copyVerificationLink(currentLang),
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                }
                            }
                        } else {
                            // Auto-polling indicator
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = ActiveNeonCyan,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = AppStrings.waitingForWindscribeEmail(currentLang),
                                    color = ThemeTextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        // Action Buttons: Manual Check & Delete/Recreate
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { viewModel.checkActiveEmailInboxNow(silent = false) },
                                enabled = !isCheckingInbox,
                                border = BorderStroke(1.dp, ActiveNeonCyan.copy(alpha = 0.5f)),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = ActiveNeonCyan),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(42.dp),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(15.dp)
                                        .rotateAnimation(isCheckingInbox)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = AppStrings.checkInboxNow(currentLang),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            OutlinedButton(
                                onClick = { viewModel.deleteUserActiveEmail() },
                                border = BorderStroke(1.dp, ThemeErrorRed.copy(alpha = 0.5f)),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = ThemeErrorRed),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(42.dp),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.DeleteOutline, contentDescription = null, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = AppStrings.deleteActiveEmail(currentLang),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }

        // 2. Installation & Setup Guide Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MidnightSurface),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, ThemeCardBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Verified, contentDescription = null, tint = ActiveNeonCyan, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = AppStrings.windscribeGuideTitle(currentLang),
                        color = ThemeTextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                Text(AppStrings.windscribeGuideStep1(currentLang), color = ThemeTextSecondary, fontSize = 12.sp, lineHeight = 19.sp)
                Text(AppStrings.windscribeGuideStep2(currentLang), color = ThemeTextSecondary, fontSize = 12.sp, lineHeight = 19.sp)
                Text(AppStrings.windscribeGuideStep3(currentLang), color = ThemeTextSecondary, fontSize = 12.sp, lineHeight = 19.sp)
            }
        }
    }
}

