package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EndingEntity
import com.example.data.model.PlayerStatsEntity
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CrimsonGlow
import com.example.ui.theme.CrimsonThread
import com.example.ui.theme.CyanTerminal
import com.example.ui.theme.EmpathyColor
import com.example.ui.theme.JusticeColor
import com.example.ui.theme.NoirDarkCard
import com.example.ui.theme.NoirObsidian
import com.example.ui.theme.NoirSurface
import com.example.ui.theme.NoirSurfaceHighlight
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun EndingsScreen(
    endings: List<EndingEntity>,
    stats: PlayerStatsEntity?,
    onUnlockEnding: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var previewEnding by remember { mutableStateOf<EndingEntity?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NoirObsidian)
            .padding(16.dp)
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.MilitaryTech,
                        contentDescription = "النهايات",
                        tint = AmberGold,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "النهايات السبعة + النهاية السرية",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "قراراتك طوال الـ 50 قضية تحدد مصير العاصمة",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }

            Surface(
                color = CrimsonThread.copy(alpha = 0.2f),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, CrimsonThread)
            ) {
                Text(
                    text = "قضية 50 الختامية",
                    color = CrimsonGlow,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Secret Ending Preview Alert
        if (previewEnding != null) {
            val end = previewEnding!!
            Card(
                colors = CardDefaults.cardColors(containerColor = NoirSurfaceHighlight),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, if (end.isSecretEnding) CrimsonGlow else AmberGold),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("ending_preview_card")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = end.title,
                            color = if (end.isSecretEnding) CrimsonGlow else AmberGold,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Surface(
                            color = NoirObsidian,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = if (end.isSecretEnding) "سري للغاية" else "نهاية رئيسية",
                                color = if (end.isSecretEnding) CrimsonGlow else CyanTerminal,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = end.description,
                        color = TextPrimary,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        color = NoirObsidian,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "شروط الفتح: ${end.requirementText}",
                            color = TextSecondary,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = { previewEnding = null },
                        colors = ButtonDefaults.buttonColors(containerColor = NoirSurface),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "إغلاق المعاينة", color = TextPrimary, fontSize = 12.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Endings List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(endings, key = { it.id }) { ending ->
                val isSecret = ending.isSecretEnding

                Card(
                    colors = CardDefaults.cardColors(containerColor = NoirDarkCard),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(
                        1.dp,
                        if (isSecret) CrimsonThread else if (ending.isUnlocked) AmberGold else Color(0xFF2E3344)
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("ending_card_${ending.id}")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(
                                            if (isSecret) CrimsonThread.copy(alpha = 0.3f)
                                            else if (ending.isUnlocked) AmberGold.copy(alpha = 0.2f)
                                            else NoirSurface,
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (isSecret) Icons.Default.Visibility
                                        else if (ending.isUnlocked) Icons.Default.CheckCircle
                                        else Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = if (isSecret) CrimsonGlow else if (ending.isUnlocked) AmberGold else TextMuted,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = ending.title,
                                        color = if (isSecret) CrimsonGlow else TextPrimary,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = ending.subtitle,
                                        color = AmberGold,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            Surface(
                                color = if (ending.isUnlocked) EmpathyColor.copy(alpha = 0.15f)
                                else NoirSurface,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = if (ending.isUnlocked) "مفتوحة ✓" else "مغلقة 🔒",
                                    color = if (ending.isUnlocked) EmpathyColor else TextMuted,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = ending.description,
                            color = TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "المتطلب: ${ending.requirementText}",
                            color = CyanTerminal,
                            fontSize = 11.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { previewEnding = ending },
                                colors = ButtonDefaults.buttonColors(containerColor = NoirSurfaceHighlight),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f).testTag("preview_ending_${ending.id}")
                            ) {
                                Text(
                                    text = "قراءة التفاصيل",
                                    color = TextPrimary,
                                    fontSize = 11.sp
                                )
                            }

                            if (!ending.isUnlocked) {
                                val canUnlock = (stats?.totalCasesSolved ?: 0) >= 45 ||
                                        ((stats?.totalCasesSolved ?: 0) >= 10 && stats?.mirrorNetworkKnowledge ?: 0 >= 50)

                                if (canUnlock) {
                                    Button(
                                        onClick = { onUnlockEnding(ending.id) },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (isSecret) CrimsonThread else AmberGold
                                        ),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.weight(1f).testTag("unlock_ending_${ending.id}")
                                    ) {
                                        Text(
                                            text = if (isSecret) "كشف السر" else "فتح النهاية",
                                            color = if (isSecret) Color.White else Color.Black,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                } else {
                                    Surface(
                                        color = NoirSurface,
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Box(
                                            modifier = Modifier.padding(vertical = 10.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "🔒 تتطلب حل القضايا أولاً",
                                                color = TextMuted,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
