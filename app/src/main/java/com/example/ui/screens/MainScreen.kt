package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.example.ui.theme.isAppDarkTheme
import com.example.ui.viewmodel.AppUpdateState
import com.example.ui.viewmodel.AppViewModel
import com.example.util.AppLanguage
import com.example.util.AppStrings
import com.example.util.ConfigParser
import com.example.util.PersianDateConverter
import com.example.util.QrCodeHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: AppViewModel) {
    val cachedData by viewModel.cachedData.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    val currentLang by viewModel.currentLanguage.collectAsStateWithLifecycle()
    val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()

    var activeTab by remember { mutableStateOf(0) }

    if (cachedData == null) {
        InitialLoadingScreen(currentLang = currentLang)
    } else {
        Scaffold(
            topBar = {
                // Explicit LTR layout to strictly guarantee:
                // LEFT (start) = Language switch button (icon-only) + Theme switch button
                // CENTER = App logo icon fixed in the exact center (title text removed)
                // RIGHT (end) = Refresh button for all sections
                Surface(
                    color = if (isDarkTheme) MidnightBg else MidnightSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                ) {
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(64.dp)
                                .padding(horizontal = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            // 1. LEFT SIDE: Language Switcher Button (Icon Only) + Theme Switch Button
                            Row(
                                modifier = Modifier.align(Alignment.CenterStart),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                IconButton(
                                    onClick = { viewModel.toggleLanguage() },
                                    modifier = Modifier
                                        .testTag("language_switch_button")
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(ActiveNeonCyan.copy(alpha = 0.12f))
                                        .border(1.dp, ActiveNeonCyan.copy(alpha = 0.4f), CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Language,
                                        contentDescription = AppStrings.languageSwitchTooltip(currentLang),
                                        tint = ActiveNeonCyan,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                IconButton(
                                    onClick = { viewModel.toggleTheme() },
                                    modifier = Modifier
                                        .testTag("theme_switch_button")
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(ActiveNeonCyan.copy(alpha = 0.12f))
                                        .border(1.dp, ActiveNeonCyan.copy(alpha = 0.4f), CircleShape)
                                ) {
                                    Icon(
                                        imageVector = if (isDarkTheme) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                                        contentDescription = AppStrings.themeSwitchTooltip(currentLang, isDarkTheme),
                                        tint = ActiveNeonCyan,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            // 2. CENTER: App Logo Icon Fixed in Center (Text name removed as requested)
                            Box(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(ActiveNeonCyan.copy(alpha = 0.18f))
                                    .border(1.2.dp, ActiveNeonCyan, RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "A9Z",
                                    color = ActiveNeonCyan,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }

                            // 3. RIGHT SIDE: Global Refresh Button (Fetches all sections from API)
                            IconButton(
                                onClick = { viewModel.refreshData() },
                                enabled = !isRefreshing,
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .testTag("refresh_all_button")
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(ActiveNeonCyan.copy(alpha = 0.12f))
                                    .border(1.dp, ActiveNeonCyan.copy(alpha = 0.4f), CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Refresh,
                                    contentDescription = AppStrings.refresh(currentLang),
                                    tint = ActiveNeonCyan,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .rotateAnimation(isRefreshing)
                                )
                            }
                        }
                    }
                }
            },
            bottomBar = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    if (!isDarkTheme) {
                        HorizontalDivider(color = ThemeCardBorder, thickness = 1.dp)
                    }
                    NavigationBar(
                        containerColor = if (isDarkTheme) MidnightBg else MidnightSurface,
                        tonalElevation = if (isDarkTheme) 8.dp else 0.dp,
                        modifier = Modifier.navigationBarsPadding()
                    ) {
                        val tabs = listOf(
                            Triple("V2Ray", Icons.Default.List, AppStrings.tabConfigs(currentLang)),
                            Triple("Proxies", Icons.Default.Send, AppStrings.tabProxies(currentLang)),
                            Triple("Windscribe", Icons.Default.Email, AppStrings.tabWindscribe(currentLang)),
                            Triple("Downloads", Icons.Default.Star, AppStrings.tabDownloads(currentLang))
                        )

                        tabs.forEachIndexed { index, item ->
                            NavigationBarItem(
                                selected = activeTab == index,
                                onClick = {
                                    activeTab = index
                                },
                                icon = {
                                    Icon(
                                        imageVector = item.second,
                                        contentDescription = item.third,
                                        tint = if (activeTab == index) ActiveNeonCyan else ThemeTextSecondary
                                    )
                                },
                                label = {
                                    Text(
                                        text = item.third,
                                        fontSize = 10.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        fontWeight = if (activeTab == index) FontWeight.Bold else FontWeight.Normal,
                                        color = if (activeTab == index) ActiveNeonCyan else ThemeTextSecondary
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = ActiveNeonCyan.copy(alpha = 0.15f)
                                )
                            )
                        }
                    }
                }
            },
            containerColor = MidnightBg
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {

                    // Info Banner for Offline fallbacks
                    AnimatedVisibility(visible = errorMessage != null) {
                        errorMessage?.let { error ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.error.copy(alpha = 0.2f))
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
                                    )
                                    .padding(vertical = 12.dp, horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = ThemeTextPrimary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = error,
                                    color = ThemeTextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.Start
                                )
                            }
                        }
                    }

                    // Main Layout
                    Box(modifier = Modifier.weight(1f)) {
                        when (activeTab) {
                            0 -> ConfigsView(viewModel = viewModel, currentLang = currentLang)
                            1 -> ProxiesView(viewModel = viewModel, currentLang = currentLang)
                            2 -> WindscribeUserView(viewModel = viewModel, currentLang = currentLang)
                            3 -> DownloadsAndSubView(viewModel = viewModel, currentLang = currentLang)
                            else -> ConfigsView(viewModel = viewModel, currentLang = currentLang)
                        }
                    }
                }

                if (isRefreshing) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.TopCenter),
                        color = ActiveNeonCyan,
                        trackColor = Color.Transparent
                    )
                }
            }
        }
    }
}

@Composable
fun InitialLoadingScreen(currentLang: AppLanguage) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MidnightBg),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(ActiveNeonCyan.copy(alpha = 0.15f * alpha))
                    .border(2.dp, ActiveNeonCyan.copy(alpha = alpha), RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "A9Z",
                    color = ActiveNeonCyan,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            CircularProgressIndicator(
                color = ActiveNeonCyan,
                strokeWidth = 3.dp,
                modifier = Modifier.size(36.dp)
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = AppStrings.initialLoading(currentLang),
                color = ThemeTextPrimary.copy(alpha = 0.9f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ProtocolFilterChips(
    protocols: List<String>,
    counts: Map<String, Int>,
    selectedProtocol: String?,
    onSelectProtocol: (String?) -> Unit,
    currentLang: AppLanguage,
    totalCount: Int
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
    ) {
        item {
            FilterChip(
                selected = selectedProtocol == null,
                onClick = { onSelectProtocol(null) },
                label = {
                    Text(
                        "${AppStrings.filterAll(currentLang)} ($totalCount)",
                        fontSize = 12.sp,
                        fontWeight = if (selectedProtocol == null) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = if (isAppDarkTheme) ActiveNeonCyan.copy(alpha = 0.2f) else ActiveNeonCyan,
                    selectedLabelColor = if (isAppDarkTheme) ActiveNeonCyan else ThemeOnAccent,
                    containerColor = if (isAppDarkTheme) MidnightSurfaceVariant else MidnightSurface,
                    labelColor = if (isAppDarkTheme) ThemeTextSecondary else ThemeTextPrimary
                ),
                border = BorderStroke(
                    1.dp,
                    if (selectedProtocol == null) ActiveNeonCyan else ThemeCardBorder
                ),
                shape = RoundedCornerShape(8.dp)
            )
        }

        items(protocols) { proto ->
            val count = counts[proto] ?: 0
            val isSelected = selectedProtocol.equals(proto, ignoreCase = true)
            FilterChip(
                selected = isSelected,
                onClick = {
                    if (isSelected) onSelectProtocol(null) else onSelectProtocol(proto)
                },
                label = {
                    Text(
                        "$proto ($count)",
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = if (isAppDarkTheme) ActiveNeonCyan.copy(alpha = 0.2f) else ActiveNeonCyan,
                    selectedLabelColor = if (isAppDarkTheme) ActiveNeonCyan else ThemeOnAccent,
                    containerColor = if (isAppDarkTheme) MidnightSurfaceVariant else MidnightSurface,
                    labelColor = if (isAppDarkTheme) ThemeTextSecondary else ThemeTextPrimary
                ),
                border = BorderStroke(
                    1.dp,
                    if (isSelected) ActiveNeonCyan else ThemeCardBorder
                ),
                shape = RoundedCornerShape(8.dp)
            )
        }
    }
}

@Composable
fun ConfigsView(viewModel: AppViewModel, currentLang: AppLanguage) {
    val cachedData by viewModel.cachedData.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    var qrDialogConfig by remember { mutableStateOf<String?>(null) }

    val configList = remember(cachedData?.configs) {
        cachedData?.configs?.split("\n")?.map { it.trim() }?.filter { it.isNotBlank() } ?: emptyList()
    }

    val parsedConfigList = remember(configList) {
        configList.mapIndexed { index, raw ->
            ConfigParser.parseConfig(raw, index + 1)
        }
    }

    val availableProtocols = remember(parsedConfigList) {
        parsedConfigList.map { it.protocol }.distinct().sorted()
    }

    val protocolCounts = remember(parsedConfigList) {
        parsedConfigList.groupingBy { it.protocol }.eachCount()
    }

    var selectedProtocol by remember { mutableStateOf<String?>(null) }

    val displayedConfigs = remember(parsedConfigList, selectedProtocol) {
        if (selectedProtocol == null) parsedConfigList
        else parsedConfigList.filter { it.protocol.equals(selectedProtocol, ignoreCase = true) }
    }

    // Modal QR Dialog for Config
    qrDialogConfig?.let { cfg ->
        ConfigQrModalDialog(
            config = cfg,
            currentLang = currentLang,
            onDismiss = { qrDialogConfig = null },
            onCopy = {
                viewModel.copyToClipboard(cfg, AppStrings.singleConfigLabel(currentLang))
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StatsCard(
            title = AppStrings.totalConfigsTitle(currentLang),
            count = cachedData?.configCount ?: configList.size,
            lastUpdate = cachedData?.configTimestamp ?: "",
            icon = Icons.Default.List,
            accentColor = ActiveNeonCyan,
            onRefresh = { viewModel.refreshData() },
            isRefreshing = isRefreshing,
            currentLang = currentLang
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (configList.isEmpty()) {
            EmptyPlaceholder(AppStrings.emptyConfigs(currentLang))
        } else {
            if (availableProtocols.size > 1) {
                ProtocolFilterChips(
                    protocols = availableProtocols,
                    counts = protocolCounts,
                    selectedProtocol = selectedProtocol,
                    onSelectProtocol = { selectedProtocol = it },
                    currentLang = currentLang,
                    totalCount = parsedConfigList.size
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            Button(
                onClick = {
                    val fullConfigs = displayedConfigs.joinToString("\n") { it.raw }
                    viewModel.copyToClipboard(fullConfigs, AppStrings.allConfigsLabel(currentLang))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("copy_all_configs_button")
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isAppDarkTheme) ActiveNeonCyan.copy(alpha = 0.15f) else ActiveNeonCyan,
                    contentColor = if (isAppDarkTheme) ActiveNeonCyan else ThemeOnAccent
                ),
                shape = RoundedCornerShape(12.dp),
                border = if (isAppDarkTheme) BorderStroke(1.dp, ActiveNeonCyan.copy(alpha = 0.5f)) else null
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        AppStrings.copyAllConfigs(currentLang),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(displayedConfigs, key = { _, item -> item.raw }) { index, parsedItem ->
                    ConfigItemCard(
                        config = parsedItem.raw,
                        index = index + 1,
                        currentLang = currentLang,
                        onCopy = {
                            viewModel.copyToClipboard(parsedItem.raw, AppStrings.singleConfigLabel(currentLang))
                        },
                        onShowQr = {
                            qrDialogConfig = parsedItem.raw
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ProxiesView(viewModel: AppViewModel, currentLang: AppLanguage) {
    val cachedData by viewModel.cachedData.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    var qrDialogProxy by remember { mutableStateOf<String?>(null) }

    val proxyList = remember(cachedData?.proxies) {
        cachedData?.proxies?.split("\n")?.map { it.trim() }?.filter { it.isNotBlank() } ?: emptyList()
    }

    val parsedProxyList = remember(proxyList) {
        proxyList.map { ConfigParser.parseProxy(it) }
    }

    val availableProtocols = remember(parsedProxyList) {
        parsedProxyList.map { it.protocol }.distinct().sorted()
    }

    val protocolCounts = remember(parsedProxyList) {
        parsedProxyList.groupingBy { it.protocol }.eachCount()
    }

    var selectedProtocol by remember { mutableStateOf<String?>(null) }

    val displayedProxies = remember(parsedProxyList, selectedProtocol) {
        if (selectedProtocol == null) parsedProxyList
        else parsedProxyList.filter { it.protocol.equals(selectedProtocol, ignoreCase = true) }
    }

    // Modal QR Dialog for Proxy (with Copy inside modal)
    qrDialogProxy?.let { proxyUrl ->
        ProxyQrModalDialog(
            proxyUrl = proxyUrl,
            currentLang = currentLang,
            onDismiss = { qrDialogProxy = null },
            onCopy = {
                viewModel.copyToClipboard(proxyUrl, AppStrings.singleProxyLabel(currentLang))
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StatsCard(
            title = AppStrings.totalProxiesTitle(currentLang),
            count = cachedData?.proxyCount ?: proxyList.size,
            lastUpdate = cachedData?.proxyTimestamp ?: "",
            icon = Icons.Default.Send,
            accentColor = ActiveNeonCyan,
            onRefresh = { viewModel.refreshData() },
            isRefreshing = isRefreshing,
            currentLang = currentLang
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (proxyList.isEmpty()) {
            EmptyPlaceholder(AppStrings.emptyProxies(currentLang))
        } else {
            if (availableProtocols.size > 1) {
                ProtocolFilterChips(
                    protocols = availableProtocols,
                    counts = protocolCounts,
                    selectedProtocol = selectedProtocol,
                    onSelectProtocol = { selectedProtocol = it },
                    currentLang = currentLang,
                    totalCount = parsedProxyList.size
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            Button(
                onClick = {
                    val fullProxies = displayedProxies.joinToString("\n") { it.rawUrl }
                    viewModel.copyToClipboard(fullProxies, AppStrings.allProxiesLabel(currentLang))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("copy_all_proxies_button")
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isAppDarkTheme) ActiveNeonCyan.copy(alpha = 0.15f) else ActiveNeonCyan,
                    contentColor = if (isAppDarkTheme) ActiveNeonCyan else ThemeOnAccent
                ),
                shape = RoundedCornerShape(12.dp),
                border = if (isAppDarkTheme) BorderStroke(1.dp, ActiveNeonCyan.copy(alpha = 0.4f)) else null
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        AppStrings.copyAllProxies(currentLang),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(displayedProxies, key = { it.rawUrl }) { item ->
                    ProxyItemCard(
                        proxyUrl = item.rawUrl,
                        currentLang = currentLang,
                        onPublish = {
                            qrDialogProxy = item.rawUrl
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DownloadsAndSubView(viewModel: AppViewModel, currentLang: AppLanguage) {
    val cachedData by viewModel.cachedData.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val subUrl = cachedData?.subLink ?: ""

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MidnightSurface),
            border = BorderStroke(1.dp, ThemeCardBorder),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null,
                        tint = ActiveNeonCyan
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        AppStrings.subHeaderTitle(currentLang),
                        color = ThemeTextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Start
                    )
                }

                Text(
                    AppStrings.subHeaderDesc(currentLang),
                    color = ThemeTextSecondary,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 12.dp)
                )

                if (subUrl.isBlank()) {
                    Text(
                        text = AppStrings.subNotSet(currentLang),
                        color = ThemeTextSecondary,
                        textAlign = TextAlign.Center,
                        fontSize = 13.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                    )
                } else {
                    val qrBitmap = remember(subUrl) {
                        if (subUrl.isNotBlank()) {
                            QrCodeHelper.generateQrBitmap(subUrl, size = 600)
                        } else null
                    }

                    // Futuristic QR Container
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // QR Box with Cyber Neon Border & Viewfinder Accents
                        Box(
                            modifier = Modifier
                                .size(230.dp)
                                .background(
                                    brush = Brush.linearGradient(
                                        listOf(
                                            ActiveNeonCyan.copy(alpha = 0.25f),
                                            Color(0xFF8B5CF6).copy(alpha = 0.25f)
                                        )
                                    ),
                                    shape = RoundedCornerShape(22.dp)
                                )
                                .border(
                                    border = BorderStroke(
                                        1.5.dp,
                                        Brush.sweepGradient(
                                            listOf(
                                                ActiveNeonCyan,
                                                Color(0xFF8B5CF6),
                                                ActiveNeonCyan.copy(alpha = 0.4f),
                                                ActiveNeonCyan
                                            )
                                        )
                                    ),
                                    shape = RoundedCornerShape(22.dp)
                                )
                                .padding(14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            // Crisp white card for guaranteed 100% scanning accuracy
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color.White)
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                if (qrBitmap != null) {
                                    Image(
                                        bitmap = qrBitmap.asImageBitmap(),
                                        contentDescription = AppStrings.qrCodeTitle(currentLang),
                                        modifier = Modifier.fillMaxSize()
                                    )
                                } else {
                                    CircularProgressIndicator(
                                        color = ActiveNeonCyan,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                            }

                            // High-tech corner viewfinder markers
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val strokeW = 3.dp.toPx()
                                val bracketLen = 18.dp.toPx()
                                val cornerColor = ActiveNeonCyan

                                // Top-Left
                                drawLine(cornerColor, Offset(0f, 0f), Offset(bracketLen, 0f), strokeW)
                                drawLine(cornerColor, Offset(0f, 0f), Offset(0f, bracketLen), strokeW)

                                // Top-Right
                                drawLine(cornerColor, Offset(size.width, 0f), Offset(size.width - bracketLen, 0f), strokeW)
                                drawLine(cornerColor, Offset(size.width, 0f), Offset(size.width, bracketLen), strokeW)

                                // Bottom-Left
                                drawLine(cornerColor, Offset(0f, size.height), Offset(bracketLen, size.height), strokeW)
                                drawLine(cornerColor, Offset(0f, size.height), Offset(0f, size.height - bracketLen), strokeW)

                                // Bottom-Right
                                drawLine(cornerColor, Offset(size.width, size.height), Offset(size.width - bracketLen, size.height), strokeW)
                                drawLine(cornerColor, Offset(size.width, size.height), Offset(size.width, size.height - bracketLen), strokeW)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // URL Snippet container with quick copy button
                        Surface(
                            color = ThemeCodeBoxBg,
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, ThemeCardBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = subUrl,
                                        color = ActiveNeonCyan,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        fontSize = 12.sp,
                                        fontFamily = FontFamily.Monospace,
                                        textAlign = TextAlign.Left,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    IconButton(
                                        onClick = {
                                            viewModel.copyToClipboard(subUrl, AppStrings.subLinkLabel(currentLang))
                                        },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ContentCopy,
                                            contentDescription = AppStrings.copySubLink(currentLang),
                                            tint = ThemeTextPrimary.copy(alpha = 0.7f),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Action Buttons: Save to Gallery & Copy Link
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            FilledTonalButton(
                                onClick = {
                                    val poster = QrCodeHelper.createSubscriptionPosterBitmap(
                                        context = context,
                                        subUrl = subUrl,
                                        titleText = AppStrings.qrCodeTitle(currentLang),
                                        subtitleText = "",
                                        tagline = AppStrings.qrScanHint(currentLang)
                                    )
                                    if (poster != null) {
                                        val success = QrCodeHelper.saveBitmapToGallery(context, poster)
                                        if (success) {
                                            Toast.makeText(
                                                context,
                                                AppStrings.qrSavedSuccess(currentLang),
                                                Toast.LENGTH_LONG
                                            ).show()
                                        } else {
                                            Toast.makeText(
                                                context,
                                                AppStrings.qrSaveFailed(currentLang),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                },
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = if (isAppDarkTheme) ActiveNeonCyan.copy(alpha = 0.15f) else ActiveNeonCyan,
                                    contentColor = if (isAppDarkTheme) ActiveNeonCyan else ThemeOnAccent
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(42.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Download,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = AppStrings.saveImage(currentLang),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }

                            FilledTonalButton(
                                onClick = {
                                    viewModel.copyToClipboard(subUrl, AppStrings.subLinkLabel(currentLang))
                                },
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = ThemeTextPrimary.copy(alpha = 0.08f),
                                    contentColor = ThemeTextPrimary
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(42.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = ActiveNeonCyan
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = AppStrings.copySubLink(currentLang),
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // V2Ray Downloads Card (بدون لینک مستقیم خود برنامه A9Z)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MidnightSurface),
            border = BorderStroke(1.dp, ThemeCardBorder),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = ActiveNeonCyan
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        AppStrings.v2rayDownloadsTitle(currentLang),
                        color = ThemeTextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Start
                    )
                }
                Text(
                    AppStrings.downloadToolsDesc(currentLang),
                    color = ThemeTextSecondary,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 16.dp)
                )

                DownloadLinkItemRow(
                    title = AppStrings.dlAndroidTitle(currentLang),
                    url = cachedData?.dlAndroid ?: "",
                    icon = Icons.Default.PhoneAndroid,
                    color = ThemeSuccessGreen,
                    context = context,
                    currentLang = currentLang
                )

                HorizontalDivider(color = ThemeCardBorder, modifier = Modifier.padding(vertical = 12.dp))

                DownloadLinkItemRow(
                    title = AppStrings.dlIosV2rayTitle(currentLang),
                    url = cachedData?.dlIos ?: "",
                    icon = Icons.Default.PhoneIphone,
                    color = ActiveNeonCyan,
                    context = context,
                    currentLang = currentLang
                )

                HorizontalDivider(color = ThemeCardBorder, modifier = Modifier.padding(vertical = 12.dp))

                DownloadLinkItemRow(
                    title = AppStrings.dlWin64Title(currentLang),
                    url = cachedData?.dlWin64 ?: "",
                    icon = Icons.Default.Computer,
                    color = Color(0xFF0078D4),
                    context = context,
                    currentLang = currentLang
                )

                HorizontalDivider(color = ThemeCardBorder, modifier = Modifier.padding(vertical = 12.dp))

                DownloadLinkItemRow(
                    title = AppStrings.dlWin32Title(currentLang),
                    url = cachedData?.dlWin32 ?: "",
                    icon = Icons.Default.Computer,
                    color = ActiveNeonCyan,
                    context = context,
                    currentLang = currentLang
                )
            }
        }

        // Windscribe VPN Downloads Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MidnightSurface),
            border = BorderStroke(1.dp, ThemeCardBorder),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CloudDownload,
                        contentDescription = null,
                        tint = ActiveNeonCyan
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        AppStrings.windscribeDownloadsTitle(currentLang),
                        color = ThemeTextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Start
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                DownloadLinkItemRow(
                    title = AppStrings.dlWsAndroidTitle(currentLang),
                    url = cachedData?.dlWsAndroid ?: "",
                    icon = Icons.Default.PhoneAndroid,
                    color = ThemeSuccessGreen,
                    context = context,
                    currentLang = currentLang
                )

                HorizontalDivider(color = ThemeCardBorder, modifier = Modifier.padding(vertical = 12.dp))

                DownloadLinkItemRow(
                    title = AppStrings.dlWsIosTitle(currentLang),
                    url = cachedData?.dlWsIos ?: "",
                    icon = Icons.Default.PhoneIphone,
                    color = ActiveNeonCyan,
                    context = context,
                    currentLang = currentLang
                )

                HorizontalDivider(color = ThemeCardBorder, modifier = Modifier.padding(vertical = 12.dp))

                DownloadLinkItemRow(
                    title = AppStrings.dlWsWindowsTitle(currentLang),
                    url = cachedData?.dlWsWindows ?: "",
                    icon = Icons.Default.Computer,
                    color = Color(0xFF0078D4),
                    context = context,
                    currentLang = currentLang
                )

                HorizontalDivider(color = ThemeCardBorder, modifier = Modifier.padding(vertical = 12.dp))

                DownloadLinkItemRow(
                    title = AppStrings.dlWsMacTitle(currentLang),
                    url = cachedData?.dlWsMac ?: "",
                    icon = Icons.Default.LaptopMac,
                    color = ThemePurpleAccent,
                    context = context,
                    currentLang = currentLang
                )
            }
        }
    }
}

@Composable
fun DownloadLinkItemRow(
    title: String,
    url: String,
    icon: ImageVector,
    color: Color,
    context: android.content.Context,
    currentLang: AppLanguage
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(ThemeTextPrimary.copy(alpha = 0.04f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color.copy(alpha = 0.8f),
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.weight(1f).padding(end = 8.dp)
        ) {
            Text(
                title,
                color = ThemeTextPrimary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                textAlign = TextAlign.Start
            )
            if (url.isNotBlank()) {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    Text(
                        text = url,
                        color = ThemeTextSecondary,
                        fontSize = 10.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Left,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        if (url.isNotBlank()) {
            Button(
                onClick = {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        // ignore
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isAppDarkTheme) color.copy(alpha = 0.15f) else ActiveNeonCyan,
                    contentColor = if (isAppDarkTheme) color else ThemeOnAccent
                ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                modifier = Modifier.height(36.dp)
            ) {
                Text(AppStrings.download(currentLang), fontWeight = FontWeight.Bold, fontSize = 11.sp)
            }
        } else {
            Text(
                AppStrings.notConfigured(currentLang),
                color = ThemeTextSecondary,
                fontSize = 11.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun StatsCard(
    title: String,
    count: Int,
    lastUpdate: String,
    icon: ImageVector,
    accentColor: Color,
    onRefresh: () -> Unit,
    isRefreshing: Boolean,
    currentLang: AppLanguage
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MidnightSurface),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, color = accentColor.copy(alpha = 0.25f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(accentColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    color = ThemeTextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Start
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$count",
                        color = ThemeTextPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = AppStrings.activeCountSuffix(currentLang),
                        color = accentColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = ThemeTextSecondary,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = AppStrings.lastUpdatePrefix(currentLang),
                        color = ThemeTextSecondary,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = remember(lastUpdate, currentLang) {
                            PersianDateConverter.formatDateTime(lastUpdate, currentLang)
                        },
                        color = ThemeTextPrimary.copy(alpha = 0.8f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Start
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            IconButton(
                onClick = onRefresh,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(ThemeTextPrimary.copy(alpha = 0.04f))
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = AppStrings.refresh(currentLang),
                    tint = accentColor,
                    modifier = Modifier.rotateAnimation(isRefreshing)
                )
            }
        }
    }
}

@Composable
fun ConfigItemCard(
    config: String,
    index: Int,
    currentLang: AppLanguage,
    onCopy: () -> Unit,
    onShowQr: () -> Unit
) {
    val parsed = remember(config) {
        ConfigParser.parseConfig(config, index)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isAppDarkTheme) MidnightSurfaceVariant else MidnightSurface
        ),
        border = BorderStroke(1.dp, ThemeCardBorder),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header Row: Protocol Badge on LEFT, Actions on RIGHT
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // LEFT: Protocol Badge (نوع کانفیگ در سمت چپ)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(ActiveNeonCyan.copy(alpha = 0.15f))
                            .border(0.5.dp, ActiveNeonCyan.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = parsed.protocol,
                            color = ActiveNeonCyan,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }

                    // RIGHT: Action Buttons - QR Modal & Copy (دکمه‌های QR و کپی در سمت راست)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // QR Code Modal Trigger
                        FilledTonalButton(
                            onClick = onShowQr,
                            modifier = Modifier.height(34.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = if (isAppDarkTheme) ThemeTextPrimary.copy(alpha = 0.08f) else ActiveNeonCyan.copy(alpha = 0.15f),
                                contentColor = ActiveNeonCyan
                            ),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.QrCode,
                                contentDescription = AppStrings.showQrCode(currentLang),
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "QR",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Copy Action Button
                        FilledTonalButton(
                            onClick = onCopy,
                            modifier = Modifier.height(34.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = if (isAppDarkTheme) ActiveNeonCyan.copy(alpha = 0.15f) else ActiveNeonCyan,
                                contentColor = if (isAppDarkTheme) ActiveNeonCyan else ThemeOnAccent
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = AppStrings.copyConfig(currentLang),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = AppStrings.copyConfig(currentLang),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Config Title
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                Text(
                    text = parsed.title,
                    color = ThemeTextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Left,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Server / Port / IP details
            if (parsed.serverInfo.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(ThemeCodeBoxBg)
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = parsed.serverInfo,
                            color = ThemeTextPrimary.copy(alpha = 0.9f),
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Left,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = null,
                            tint = ActiveNeonCyan.copy(alpha = 0.7f),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ConfigQrModalDialog(
    config: String,
    currentLang: AppLanguage,
    onDismiss: () -> Unit,
    onCopy: () -> Unit
) {
    val context = LocalContext.current
    val parsed = remember(config) {
        ConfigParser.parseConfig(config, 0)
    }
    val qrBitmap = remember(config) {
        QrCodeHelper.generateQrBitmap(config, size = 650)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MidnightSurface),
            border = BorderStroke(1.dp, ActiveNeonCyan.copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header: Title and Close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(ActiveNeonCyan.copy(alpha = 0.15f))
                                .border(0.5.dp, ActiveNeonCyan.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = parsed.protocol,
                                color = ActiveNeonCyan,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = AppStrings.configQrModalTitle(currentLang),
                            color = ThemeTextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(ThemeTextPrimary.copy(alpha = 0.08f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = AppStrings.close(currentLang),
                            tint = ThemeTextPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Config Title Remark
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    Text(
                        text = parsed.title,
                        color = ThemeTextPrimary.copy(alpha = 0.9f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // QR Display Box with Cyber Border (Enlarged for easy scanning)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 280.dp)
                        .aspectRatio(1f)
                        .background(
                            brush = Brush.linearGradient(
                                listOf(ActiveNeonCyan.copy(alpha = 0.2f), Color(0xFF8B5CF6).copy(alpha = 0.2f))
                            ),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .border(
                            border = BorderStroke(
                                2.dp,
                                Brush.sweepGradient(
                                    listOf(ActiveNeonCyan, Color(0xFF8B5CF6), ActiveNeonCyan)
                                )
                            ),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.White)
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (qrBitmap != null) {
                            Image(
                                bitmap = qrBitmap.asImageBitmap(),
                                contentDescription = parsed.title,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            CircularProgressIndicator(
                                color = ActiveNeonCyan,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Buttons: Save as image & Copy config
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    FilledTonalButton(
                        onClick = {
                            val poster = QrCodeHelper.createSubscriptionPosterBitmap(
                                context = context,
                                subUrl = config,
                                titleText = parsed.protocol + " - " + parsed.title,
                                subtitleText = parsed.serverInfo,
                                tagline = AppStrings.qrScanHint(currentLang)
                            )
                            if (poster != null) {
                                val success = QrCodeHelper.saveBitmapToGallery(context, poster)
                                if (success) {
                                    Toast.makeText(
                                        context,
                                        AppStrings.qrSavedSuccess(currentLang),
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        context,
                                        AppStrings.qrSaveFailed(currentLang),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        },
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = ActiveNeonCyan.copy(alpha = 0.15f),
                            contentColor = ActiveNeonCyan
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = AppStrings.saveImage(currentLang),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    FilledTonalButton(
                        onClick = {
                            onCopy()
                            onDismiss()
                        },
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = ThemeTextPrimary.copy(alpha = 0.08f),
                            contentColor = ThemeTextPrimary
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = null,
                            modifier = Modifier.size(15.dp),
                            tint = ActiveNeonCyan
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = AppStrings.copyConfig(currentLang),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProxyQrModalDialog(
    proxyUrl: String,
    currentLang: AppLanguage,
    onDismiss: () -> Unit,
    onCopy: () -> Unit
) {
    val context = LocalContext.current
    val parsed = remember(proxyUrl) {
        ConfigParser.parseProxy(proxyUrl)
    }
    val qrBitmap = remember(proxyUrl) {
        QrCodeHelper.generateQrBitmap(proxyUrl, size = 650)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MidnightSurface),
            border = BorderStroke(1.dp, ActiveNeonCyan.copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header: Title and Close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(ActiveNeonCyan.copy(alpha = 0.15f))
                                .border(0.5.dp, ActiveNeonCyan.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = parsed.protocol,
                                color = ActiveNeonCyan,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = AppStrings.proxyQrModalTitle(currentLang),
                            color = ThemeTextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(ThemeTextPrimary.copy(alpha = 0.08f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = AppStrings.close(currentLang),
                            tint = ThemeTextPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Server info if present
                if (parsed.serverInfo.isNotBlank()) {
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(MidnightSurfaceVariant)
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = parsed.serverInfo,
                                color = ThemeTextSecondary,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Left,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.Place,
                                contentDescription = null,
                                tint = ActiveNeonCyan.copy(alpha = 0.7f),
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // QR Display Box with Cyber Border (matching Config and Subscription QR)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 280.dp)
                        .aspectRatio(1f)
                        .background(
                            brush = Brush.linearGradient(
                                listOf(ActiveNeonCyan.copy(alpha = 0.2f), Color(0xFF8B5CF6).copy(alpha = 0.2f))
                            ),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .border(
                            border = BorderStroke(
                                2.dp,
                                Brush.sweepGradient(
                                    listOf(ActiveNeonCyan, Color(0xFF8B5CF6), ActiveNeonCyan)
                                )
                            ),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.White)
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (qrBitmap != null) {
                            Image(
                                bitmap = qrBitmap.asImageBitmap(),
                                contentDescription = AppStrings.proxyQrModalTitle(currentLang),
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            CircularProgressIndicator(
                                color = ActiveNeonCyan,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = AppStrings.proxyQrScanHint(currentLang),
                    color = ThemeTextSecondary,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Action Buttons: Save image & Copy Link (as requested: "و دکمه کپی هم به اون مودال بره")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    FilledTonalButton(
                        onClick = {
                            val poster = QrCodeHelper.createSubscriptionPosterBitmap(
                                context = context,
                                subUrl = proxyUrl,
                                titleText = parsed.protocol + " Proxy",
                                subtitleText = parsed.serverInfo,
                                tagline = AppStrings.proxyQrScanHint(currentLang)
                            )
                            if (poster != null) {
                                val success = QrCodeHelper.saveBitmapToGallery(context, poster)
                                Toast.makeText(
                                    context,
                                    if (success) AppStrings.qrSavedSuccess(currentLang) else AppStrings.qrSaveFailed(currentLang),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = ActiveNeonCyan.copy(alpha = 0.15f),
                            contentColor = ActiveNeonCyan
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = AppStrings.saveImage(currentLang),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    FilledTonalButton(
                        onClick = {
                            onCopy()
                            onDismiss()
                        },
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = ThemeTextPrimary.copy(alpha = 0.08f),
                            contentColor = ThemeTextPrimary
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = null,
                            modifier = Modifier.size(15.dp),
                            tint = ActiveNeonCyan
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = AppStrings.copyLink(currentLang),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Direct Connect Button
                Button(
                    onClick = {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(proxyUrl))
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            onCopy()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ActiveNeonCyan,
                        contentColor = ThemeOnAccent
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = AppStrings.connect(currentLang),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun ProxyItemCard(proxyUrl: String, currentLang: AppLanguage, onPublish: () -> Unit) {
    val context = LocalContext.current
    val parsed = remember(proxyUrl) {
        ConfigParser.parseProxy(proxyUrl)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isAppDarkTheme) MidnightSurfaceVariant else MidnightSurface
        ),
        border = BorderStroke(1.dp, ThemeCardBorder),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header Row: Protocol Title on LEFT & Action buttons on RIGHT
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Auto-detected protocol badge (LEFT)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(ActiveNeonCyan.copy(alpha = 0.15f))
                            .border(0.5.dp, ActiveNeonCyan.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = parsed.protocol,
                            color = ActiveNeonCyan,
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp
                        )
                    }

                    // Action buttons: "انتشار" (QR Modal) & "اتصال مستقیم" (Connect) (RIGHT)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilledTonalButton(
                            onClick = onPublish,
                            modifier = Modifier.height(34.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = ActiveNeonCyan.copy(alpha = 0.15f),
                                contentColor = ActiveNeonCyan
                            ),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.QrCode,
                                contentDescription = AppStrings.publish(currentLang),
                                modifier = Modifier.size(14.dp),
                                tint = ActiveNeonCyan
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = AppStrings.publish(currentLang),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = {
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(proxyUrl))
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    onPublish()
                                }
                            },
                            modifier = Modifier.height(34.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ActiveNeonCyan,
                                contentColor = ThemeOnAccent
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = AppStrings.connect(currentLang),
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(AppStrings.connect(currentLang), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Server / Port details
            if (parsed.serverInfo.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(ThemeCodeBoxBg)
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = null,
                        tint = ActiveNeonCyan.copy(alpha = 0.7f),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = parsed.serverInfo,
                        color = ThemeTextPrimary.copy(alpha = 0.9f),
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Left,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyPlaceholder(tip: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = ThemeTextSecondary.copy(alpha = 0.4f),
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = tip,
            color = ThemeTextSecondary,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )
    }
}

@Composable
fun Modifier.rotateAnimation(enabled: Boolean): Modifier {
    if (!enabled) return this
    val transition = rememberInfiniteTransition()
    val angle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    return this.graphicsLayer {
        rotationZ = angle
    }
}
