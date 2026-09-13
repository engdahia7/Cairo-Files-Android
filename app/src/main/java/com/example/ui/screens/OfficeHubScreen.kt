package com.example.ui.screens

import com.example.R

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CaseEntity
import com.example.data.model.PlayerStatsEntity
import com.example.ui.components.PersonalityRadarCard
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CrimsonGlow
import com.example.ui.theme.CrimsonThread
import com.example.ui.theme.CyanTerminal
import com.example.ui.theme.EmpathyColor
import com.example.ui.theme.NoirDarkCard
import com.example.ui.theme.NoirObsidian
import com.example.ui.theme.NoirSurface
import com.example.ui.theme.NoirSurfaceHighlight
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.GameTab

@Composable
fun OfficeHubScreen(
    stats: PlayerStatsEntity?,
    activeCase: CaseEntity?,
    isNightCallPlaying: Boolean,
    onTriggerCall: () -> Unit,
    onDismissCall: () -> Unit,
    onNavigateTab: (GameTab) -> Unit,
    onAdvanceActiveCase: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NoirObsidian)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            // Hero Atmospheric Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, Color(0xFF33384A), RoundedCornerShape(16.dp))
            ) {
                Image(
                    painter = painterResource(id = com.example.R.drawable.img_cairo_night_banner),
                    contentDescription = "ليالي القاهرة",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Dark gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    NoirObsidian.copy(alpha = 0.85f),
                                    NoirObsidian
                                )
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = com.example.R.drawable.img_detective_avatar),
                            contentDescription = "المحقق محمد عبده",
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .border(1.5.dp, AmberGold, CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Surface(
                            color = CrimsonThread,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "القاهرة — 02:13 ص",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "مكتب محمد عبده (31 سنة)",
                            color = AmberGold,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "ملفات القاهرة: آخر خيط",
                        color = TextPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "فوق محل قديم في وسط البلد.. حيث تنكشف خيوط منظمة المرآة",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // New: Detective Interactive Guide
        item {
            com.example.ui.components.DetectiveGuideCard(
                onNavigateTab = onNavigateTab,
                onStartCall = onTriggerCall
            )
        }

        // Night Call Replay Card / Audio Transcript
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("night_call_card"),
                colors = CardDefaults.cardColors(
                    containerColor = if (isNightCallPlaying) Color(0xFF2B1313) else NoirDarkCard
                ),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(
                    1.dp,
                    if (isNightCallPlaying) CrimsonThread else Color(0xFF33384A)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .border(
                                        1.5.dp,
                                        if (isNightCallPlaying) CrimsonThread else AmberGold,
                                        CircleShape
                                    )
                            ) {
                                Image(
                                    painter = painterResource(id = com.example.R.drawable.img_journalist_avatar),
                                    contentDescription = "يوسف كامل",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "تسجيل مكالمة الفجر (02:13 ص)",
                                    color = TextPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "المتصل: يوسف كامل (الصحفي المفترض وفاته)",
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Button(
                            onClick = {
                                if (isNightCallPlaying) onDismissCall() else onTriggerCall()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isNightCallPlaying) CrimsonThread else NoirSurfaceHighlight
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("play_night_call_btn")
                        ) {
                            Text(
                                text = if (isNightCallPlaying) "إيقاف" else "استماع",
                                color = if (isNightCallPlaying) Color.White else AmberGold,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    AnimatedVisibility(
                        visible = isNightCallPlaying,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp)
                                .background(NoirObsidian, RoundedCornerShape(8.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "محمد: 'ألو؟ مين؟'",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "الصوت: 'أنا الشخص اللي المفروض مات من سنتين.. يوسف كامل.'",
                                color = CrimsonGlow,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "الرسالة اللاحقة: 'لا تثق بأول شخص سيخبرك أن يوسف مات.. ابحث عن أول مرآة!'",
                                color = AmberGold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

        // Stats Overview Grid
        if (stats != null) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OfficeStatTile(
                        title = "القضايا المحلولة",
                        value = "${stats.totalCasesSolved} / 50",
                        subtitle = "الفصل ${stats.currentChapter}",
                        color = AmberGold,
                        icon = Icons.Default.Description,
                        modifier = Modifier.weight(1f)
                    )
                    OfficeStatTile(
                        title = "معرفة شبكة المرآة",
                        value = "${stats.mirrorNetworkKnowledge}%",
                        subtitle = "ملف رقم 00",
                        color = CrimsonGlow,
                        icon = Icons.Default.Visibility,
                        modifier = Modifier.weight(1f)
                    )
                    OfficeStatTile(
                        title = "مستوى المكتب",
                        value = when (stats.officeLevel) {
                            1 -> "بدائي"
                            2 -> "متطور"
                            else -> "مركز عمليات"
                        },
                        subtitle = "وسط البلد",
                        color = CyanTerminal,
                        icon = Icons.Default.Shield,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Personality Morality Radar
            item {
                PersonalityRadarCard(stats = stats)
            }
        }

        // Active Case Quick-Resume Tile
        if (activeCase != null) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("active_case_resume_card"),
                    colors = CardDefaults.cardColors(containerColor = NoirDarkCard),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, AmberGold.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    color = AmberGold.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "القضية النشطة #${activeCase.id}",
                                        color = AmberGold,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = activeCase.title,
                                    color = TextPrimary,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = activeCase.coldOpenNarrative,
                            color = TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Location Image Banner and Key Clue
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .border(1.dp, Color(0xFF33384A), RoundedCornerShape(10.dp))
                        ) {
                            Image(
                                painter = painterResource(id = com.example.R.drawable.img_cairo_crime_scene),
                                contentDescription = "مسرح الحدث",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            Surface(
                                color = NoirObsidian.copy(alpha = 0.8f),
                                shape = RoundedCornerShape(topStart = 8.dp),
                                modifier = Modifier.align(Alignment.BottomEnd)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = "الموقع",
                                        tint = CyanTerminal,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = activeCase.location,
                                        color = TextPrimary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = { onNavigateTab(GameTab.CASES) },
                                colors = ButtonDefaults.buttonColors(containerColor = AmberGold),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("open_case_files_btn")
                            ) {
                                Text(
                                    text = "فتح ملف القضية والأدلة",
                                    color = Color.Black,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Button(
                                onClick = { onNavigateTab(GameTab.CORKBOARD) },
                                colors = ButtonDefaults.buttonColors(containerColor = NoirSurfaceHighlight),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, CrimsonThread),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("open_corkboard_btn")
                            ) {
                                Text(
                                    text = "لوحة الخيوط الحمراء 📌",
                                    color = CrimsonGlow,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun OfficeStatTile(
    title: String,
    value: String,
    subtitle: String,
    color: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = NoirDarkCard),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0xFF282C3D))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = title, color = TextMuted, fontSize = 10.sp)
            Text(text = value, color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text(text = subtitle, color = color, fontSize = 10.sp)
        }
    }
}
