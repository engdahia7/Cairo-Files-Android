package com.example.ui.components

import com.example.R

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CaseEntity
import com.example.data.model.CaseStatus
import com.example.data.model.EvidenceCategory
import com.example.data.model.EvidenceEntity
import com.example.data.model.PlayerStatsEntity
import com.example.data.model.SuspectEntity
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CautionColor
import com.example.ui.theme.CrimsonGlow
import com.example.ui.theme.CrimsonThread
import com.example.ui.theme.CyanTerminal
import com.example.ui.theme.EmpathyColor
import com.example.ui.theme.JusticeColor
import com.example.ui.theme.NoirDarkCard
import com.example.ui.theme.NoirObsidian
import com.example.ui.theme.NoirSurface
import com.example.ui.theme.NoirSurfaceHighlight
import com.example.ui.theme.ObsessionColor
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun PersonalityRadarCard(
    stats: PlayerStatsEntity,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("personality_radar_card"),
        colors = CardDefaults.cardColors(containerColor = NoirDarkCard),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFF2C3142))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = "شخصية المحقق",
                        tint = AmberGold,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "منظومة شخصية محمد عبده",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Surface(
                    color = AmberGold.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = when {
                            stats.obsessionScore > 20 -> "مهووس بالحقيقة"
                            stats.empathyScore > stats.justiceScore -> "محقق إنساني"
                            stats.justiceScore > stats.empathyScore -> "قانوني صارم"
                            else -> "محقق متوازن"
                        },
                        color = AmberGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Text(
                text = "كل قرار واستجواب يؤثر على هذه القيم الأربع ويغير مسارات القضايا والنهايات:",
                color = TextSecondary,
                fontSize = 12.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Progress meters
            MoralityMeterRow(
                title = "العدالة (القانون فوق كل شيء)",
                value = stats.justiceScore,
                color = JusticeColor,
                icon = Icons.Default.Balance
            )
            Spacer(modifier = Modifier.height(8.dp))
            MoralityMeterRow(
                title = "التعاطف (الاهتمام بالناس قبل النتيجة)",
                value = stats.empathyScore,
                color = EmpathyColor,
                icon = Icons.Default.Favorite
            )
            Spacer(modifier = Modifier.height(8.dp))
            MoralityMeterRow(
                title = "الحذر (التصرف بحساب وحذر)",
                value = stats.cautionScore,
                color = CautionColor,
                icon = Icons.Default.Shield
            )
            Spacer(modifier = Modifier.height(8.dp))
            MoralityMeterRow(
                title = "الهوس (الاستعداد للذهاب لأبعد حد)",
                value = stats.obsessionScore,
                color = ObsessionColor,
                icon = Icons.Default.Fingerprint
            )
        }
    }
}

@Composable
fun MoralityMeterRow(
    title: String,
    value: Int,
    color: Color,
    icon: ImageVector
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = title,
                    color = TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Text(
                text = "$value نقطة",
                color = color,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { (value / 40f).coerceIn(0.05f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = NoirSurface
        )
    }
}

@Composable
fun CaseItemCard(
    caseEntity: CaseEntity,
    isSelected: Boolean,
    onClick: () -> Unit,
    onAdvanceProgress: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isLocked = caseEntity.status == CaseStatus.LOCKED
    val isResolved = caseEntity.status == CaseStatus.RESOLVED

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = !isLocked) { onClick() }
            .testTag("case_card_${caseEntity.id}"),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) NoirSurfaceHighlight else NoirDarkCard
        ),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = when {
                isSelected -> AmberGold
                isResolved -> EmpathyColor.copy(alpha = 0.6f)
                isLocked -> Color(0xFF242630)
                else -> Color(0xFF33384A)
            }
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
                            .size(34.dp)
                            .background(
                                color = if (isResolved) EmpathyColor.copy(alpha = 0.2f)
                                else if (isLocked) Color(0xFF22242D)
                                else AmberGold.copy(alpha = 0.2f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (caseEntity.id < 10) "0${caseEntity.id}" else "${caseEntity.id}",
                            color = if (isResolved) EmpathyColor else if (isLocked) TextMuted else AmberGold,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = caseEntity.title,
                            color = if (isLocked) TextMuted else TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${caseEntity.location} • ${caseEntity.timeLabel}",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }
                }

                // Status Tag
                Surface(
                    color = when (caseEntity.status) {
                        CaseStatus.RESOLVED -> EmpathyColor.copy(alpha = 0.15f)
                        CaseStatus.IN_PROGRESS -> AmberGold.copy(alpha = 0.15f)
                        CaseStatus.UNLOCKED -> CyanTerminal.copy(alpha = 0.15f)
                        CaseStatus.LOCKED -> Color(0xFF1E2028)
                        CaseStatus.UNRESOLVED -> CrimsonThread.copy(alpha = 0.15f)
                    },
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = when (caseEntity.status) {
                            CaseStatus.RESOLVED -> "مغلقة ✓"
                            CaseStatus.IN_PROGRESS -> "قيد التحقيق"
                            CaseStatus.UNLOCKED -> "مفتوحة"
                            CaseStatus.LOCKED -> "مغلقة بسريان"
                            CaseStatus.UNRESOLVED -> "معلقة"
                        },
                        color = when (caseEntity.status) {
                            CaseStatus.RESOLVED -> EmpathyColor
                            CaseStatus.IN_PROGRESS -> AmberGold
                            CaseStatus.UNLOCKED -> CyanTerminal
                            CaseStatus.LOCKED -> TextMuted
                            CaseStatus.UNRESOLVED -> CrimsonGlow
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = caseEntity.subtitle,
                color = TextSecondary,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )

            if (!isLocked) {
                Spacer(modifier = Modifier.height(10.dp))

                // Clue teaser
                Surface(
                    color = NoirSurface,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Fingerprint,
                            contentDescription = "الدليل المركزي",
                            tint = AmberGold,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "الدليل: ${caseEntity.keyClueTitle}",
                            color = TextPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Mirror clue
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = "أثر المرآة",
                        tint = CrimsonThread,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "خيط المرآة: ${caseEntity.mirrorClueSnippet}",
                        color = CrimsonGlow,
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Progress bar and action
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "نسبة جمع الأدلة",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                            Text(
                                text = "${caseEntity.progressPercent}%",
                                color = AmberGold,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = { caseEntity.progressPercent / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(5.dp)
                                .clip(RoundedCornerShape(2.5.dp)),
                            color = if (isResolved) EmpathyColor else AmberGold,
                            trackColor = NoirSurface
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    if (!isResolved) {
                        Button(
                            onClick = onClick,
                            colors = ButtonDefaults.buttonColors(containerColor = NoirSurfaceHighlight),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, AmberGold),
                            modifier = Modifier.testTag("advance_case_${caseEntity.id}")
                        ) {
                            Text(
                                text = "تفاصيل الملف",
                                color = AmberGold,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EvidenceItemCard(
    evidence: EvidenceEntity,
    onAnalyze: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("evidence_${evidence.id}"),
        colors = CardDefaults.cardColors(containerColor = NoirDarkCard),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            1.dp,
            if (evidence.isConfirmedOnBoard) CrimsonThread else Color(0xFF33384A)
        )
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
                                color = if (evidence.isSecretMirrorClue) CrimsonThread.copy(alpha = 0.2f)
                                else AmberGold.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (evidence.category) {
                                EvidenceCategory.PHYSICAL_OBJECT -> Icons.Default.Fingerprint
                                EvidenceCategory.DOCUMENT -> Icons.Default.Description
                                EvidenceCategory.PHOTO -> Icons.Default.Visibility
                                EvidenceCategory.DIGITAL_RECORD -> Icons.Default.Psychology
                                EvidenceCategory.FORENSIC_REPORT -> Icons.Default.MenuBook
                                EvidenceCategory.AUDIO_TAPE -> Icons.Default.RecordVoiceOver
                            },
                            contentDescription = evidence.title,
                            tint = if (evidence.isSecretMirrorClue) CrimsonGlow else AmberGold,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = evidence.title,
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "المرحلة: ${evidence.phase.name}",
                            color = TextMuted,
                            fontSize = 10.sp
                        )
                    }
                }

                if (evidence.isConfirmedOnBoard) {
                    Surface(
                        color = CrimsonThread.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "مربوط باللوحة 📌",
                            color = CrimsonGlow,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = evidence.description,
                color = TextSecondary,
                fontSize = 12.sp,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Analysis Insight Box
            Surface(
                color = NoirSurface,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "استنتاج الفحص",
                            tint = CyanTerminal,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "استنتاج الفحص الجنائي:",
                            color = CyanTerminal,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = evidence.analysisInsight,
                        color = TextPrimary,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun SuspectItemCard(
    suspect: SuspectEntity,
    onInterrogate: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("suspect_card_${suspect.id}"),
        colors = CardDefaults.cardColors(containerColor = NoirDarkCard),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, Color(0xFF2D3244))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val avatarRes = when (suspect.id) {
                        "youssef_kamel" -> R.drawable.img_journalist_avatar
                        "noura_elgohary" -> R.drawable.img_noura_avatar
                        "marwan_salem" -> R.drawable.img_marwan_avatar
                        "amm_saber" -> R.drawable.img_saber_avatar
                        "mohamed" -> R.drawable.img_detective_avatar
                        else -> null
                    }

                    if (avatarRes != null) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .border(1.5.dp, AmberGold, CircleShape)
                        ) {
                            Image(
                                painter = painterResource(id = avatarRes),
                                contentDescription = suspect.name,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    } else {
                        Text(
                            text = suspect.avatarEmoji,
                            fontSize = 28.sp,
                            modifier = Modifier
                                .background(NoirSurface, CircleShape)
                                .padding(6.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = suspect.name,
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = suspect.roleTitle,
                            color = AmberGold,
                            fontSize = 12.sp
                        )
                    }
                }

                Surface(
                    color = NoirSurface,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = suspect.statusTag,
                        color = TextSecondary,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = suspect.description,
                color = TextSecondary,
                fontSize = 12.sp,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // NPC Dynamic Variables (Trust, Fear, Loyalty, Secrets)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                NpcMetricBadge(title = "الثقة", value = "${suspect.trustScore}%", color = EmpathyColor)
                NpcMetricBadge(title = "الخوف", value = "${suspect.fearScore}%", color = CrimsonGlow)
                NpcMetricBadge(title = "الولاء", value = "${suspect.loyaltyScore}%", color = JusticeColor)
                NpcMetricBadge(title = "الأسرار", value = suspect.secretsLevel, color = AmberGold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onInterrogate,
                colors = ButtonDefaults.buttonColors(containerColor = NoirSurfaceHighlight),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, AmberGold.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("interrogate_btn_${suspect.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.RecordVoiceOver,
                    contentDescription = "استجواب ومواجهة",
                    tint = AmberGold,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "استجواب ومواجهة بالأدلة",
                    color = AmberGold,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun NpcMetricBadge(title: String, value: String, color: Color) {
    Surface(
        color = NoirSurface,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Text(text = title, color = TextMuted, fontSize = 10.sp)
            Text(text = value, color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}
