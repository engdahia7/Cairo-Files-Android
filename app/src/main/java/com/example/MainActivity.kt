package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.CairoMapScreen
import com.example.ui.screens.CasesDossierScreen
import com.example.ui.screens.CorkboardScreen
import com.example.ui.screens.EndingsScreen
import com.example.ui.screens.InterrogationScreen
import com.example.ui.screens.OfficeHubScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CrimsonGlow
import com.example.ui.theme.CrimsonThread
import com.example.ui.theme.CyanTerminal
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NoirDarkCard
import com.example.ui.theme.NoirObsidian
import com.example.ui.theme.NoirSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.GameTab
import com.example.ui.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    CairoFilesApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CairoFilesApp(viewModel: GameViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = NoirObsidian,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "ملفات القاهرة",
                            color = AmberGold,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = ": آخر خيط",
                            color = TextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                },
                actions = {
                    Surface(
                        color = if (uiState.selectedTab == GameTab.SETTINGS) AmberGold else NoirDarkCard,
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(1.dp, AmberGold.copy(alpha = 0.5f)),
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clickable { viewModel.selectTab(GameTab.SETTINGS) }
                            .testTag("top_bar_settings_btn")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "الإعدادات والحفظ",
                                tint = if (uiState.selectedTab == GameTab.SETTINGS) Color.Black else AmberGold,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "الإعدادات",
                                color = if (uiState.selectedTab == GameTab.SETTINGS) Color.Black else AmberGold,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Surface(
                        color = CrimsonThread.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Text(
                            text = "المرآة: ${uiState.playerStats?.mirrorNetworkKnowledge ?: 0}%",
                            color = CrimsonGlow,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = NoirDarkCard,
                    titleContentColor = TextPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = NoirDarkCard,
                contentColor = TextPrimary,
                tonalElevation = 8.dp,
                modifier = Modifier.testTag("bottom_nav_bar")
            ) {
                NavigationBarItem(
                    selected = uiState.selectedTab == GameTab.OFFICE,
                    onClick = { viewModel.selectTab(GameTab.OFFICE) },
                    icon = { Icon(Icons.Default.Business, contentDescription = "المكتب") },
                    label = { Text("المكتب", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = AmberGold,
                        indicatorColor = AmberGold,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("nav_office")
                )

                NavigationBarItem(
                    selected = uiState.selectedTab == GameTab.CASES,
                    onClick = { viewModel.selectTab(GameTab.CASES) },
                    icon = { Icon(Icons.Default.Description, contentDescription = "القضايا") },
                    label = { Text("القضايا", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = AmberGold,
                        indicatorColor = AmberGold,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("nav_cases")
                )

                NavigationBarItem(
                    selected = uiState.selectedTab == GameTab.CORKBOARD,
                    onClick = { viewModel.selectTab(GameTab.CORKBOARD) },
                    icon = {
                        BadgedBox(badge = {
                            if (uiState.boardLinks.isNotEmpty()) {
                                Badge(containerColor = CrimsonThread) {
                                    Text("${uiState.boardLinks.size}", color = Color.White)
                                }
                            }
                        }) {
                            Icon(Icons.Default.PinDrop, contentDescription = "اللوحة")
                        }
                    },
                    label = { Text("اللوحة 📌", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = AmberGold,
                        indicatorColor = AmberGold,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("nav_corkboard")
                )

                NavigationBarItem(
                    selected = uiState.selectedTab == GameTab.INTERROGATION,
                    onClick = { viewModel.selectTab(GameTab.INTERROGATION) },
                    icon = { Icon(Icons.Default.RecordVoiceOver, contentDescription = "الاستجواب") },
                    label = { Text("الاستجواب", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = AmberGold,
                        indicatorColor = AmberGold,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("nav_interrogation")
                )

                NavigationBarItem(
                    selected = uiState.selectedTab == GameTab.CAIRO_MAP,
                    onClick = { viewModel.selectTab(GameTab.CAIRO_MAP) },
                    icon = { Icon(Icons.Default.Explore, contentDescription = "الخريطة") },
                    label = { Text("الخريطة", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = AmberGold,
                        indicatorColor = AmberGold,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("nav_cairo_map")
                )

                NavigationBarItem(
                    selected = uiState.selectedTab == GameTab.ENDINGS,
                    onClick = { viewModel.selectTab(GameTab.ENDINGS) },
                    icon = { Icon(Icons.Default.MilitaryTech, contentDescription = "النهايات") },
                    label = { Text("النهايات", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = AmberGold,
                        indicatorColor = AmberGold,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("nav_endings")
                )

                NavigationBarItem(
                    selected = uiState.selectedTab == GameTab.SETTINGS,
                    onClick = { viewModel.selectTab(GameTab.SETTINGS) },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "الإعدادات") },
                    label = { Text("الإعدادات", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = AmberGold,
                        indicatorColor = AmberGold,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("nav_settings")
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Notification / Banner Alert
            AnimatedVisibility(
                visible = uiState.bannerNotice != null,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                if (uiState.bannerNotice != null) {
                    Surface(
                        color = NoirDarkCard,
                        border = androidx.compose.foundation.BorderStroke(1.dp, AmberGold),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = AmberGold,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = uiState.bannerNotice ?: "",
                                    color = TextPrimary,
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp
                                )
                            }
                            IconButton(
                                onClick = { viewModel.dismissBanner() },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "إغلاق",
                                    tint = TextMuted
                                )
                            }
                        }
                    }
                }
            }

            // Screen Content Routing
            when (uiState.selectedTab) {
                GameTab.OFFICE -> {
                    OfficeHubScreen(
                        stats = uiState.playerStats,
                        activeCase = uiState.activeCase,
                        isNightCallPlaying = uiState.isNightCallPlaying,
                        onTriggerCall = { viewModel.triggerNightCall() },
                        onDismissCall = { viewModel.dismissNightCall() },
                        onNavigateTab = { viewModel.selectTab(it) },
                        onAdvanceActiveCase = { viewModel.advanceCaseProgress(it) }
                    )
                }
                GameTab.CASES -> {
                    CasesDossierScreen(
                        allCases = uiState.allCases,
                        selectedChapter = uiState.selectedChapter,
                        activeCase = uiState.activeCase,
                        caseEvidence = uiState.activeCaseEvidence,
                        onSelectChapter = { viewModel.selectChapter(it) },
                        onSelectCase = { viewModel.selectCase(it) },
                        onAdvanceCase = { viewModel.advanceCaseProgress(it) },
                        onDiscoverEvidence = { viewModel.discoverClue(it) }
                    )
                }
                GameTab.CORKBOARD -> {
                    CorkboardScreen(
                        suspects = uiState.allSuspects,
                        evidenceList = uiState.activeCaseEvidence,
                        boardLinks = uiState.boardLinks,
                        selectedSourceId = uiState.selectedBoardSourceId,
                        selectedTargetId = uiState.selectedBoardTargetId,
                        validationMessage = uiState.boardValidationMessage,
                        onSelectNode = { viewModel.selectBoardNode(it) }
                    )
                }
                GameTab.INTERROGATION -> {
                    InterrogationScreen(
                        suspects = uiState.allSuspects,
                        activeInterrogation = uiState.activeInterrogation,
                        onStartInterrogation = { viewModel.startInterrogation(it) },
                        onChooseDialogueOption = { viewModel.chooseDialogueOption(it) },
                        onDismissInterrogation = { viewModel.selectTab(GameTab.OFFICE) }
                    )
                }
                GameTab.CAIRO_MAP -> {
                    CairoMapScreen(
                        encounters = uiState.encounters,
                        onVisitEncounter = { viewModel.visitCairoDistrict(it) }
                    )
                }
                GameTab.ENDINGS -> {
                    EndingsScreen(
                        endings = uiState.endings,
                        stats = uiState.playerStats,
                        onUnlockEnding = { endingId ->
                            viewModel.unlockEnding(endingId)
                        }
                    )
                }
                GameTab.SETTINGS -> {
                    SettingsScreen(
                        uiState = uiState,
                        onManualSave = { viewModel.manualSaveGame() },
                        onToggleAutoSave = { viewModel.toggleAutoSave(it) },
                        onToggleSound = { viewModel.toggleSoundEffects(it) },
                        onToggleMusic = { viewModel.toggleAtmosphericMusic(it) },
                        onToggleVibration = { viewModel.toggleVibration(it) },
                        onSetTextSpeed = { viewModel.setTextSpeed(it) },
                        onResetGame = { viewModel.resetEntireGame() }
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "ملفات القاهرة: $name", modifier = modifier)
}

