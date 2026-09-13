package com.example.ui.screens

import com.example.R

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.draw.clip
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SuspectEntity
import com.example.ui.components.NpcMetricBadge
import com.example.ui.components.SuspectItemCard
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
import com.example.ui.viewmodel.ActiveInterrogation
import com.example.ui.viewmodel.InterrogationDialogueOption

@Composable
fun InterrogationScreen(
    suspects: List<SuspectEntity>,
    activeInterrogation: ActiveInterrogation?,
    onStartInterrogation: (SuspectEntity) -> Unit,
    onChooseDialogueOption: (InterrogationDialogueOption) -> Unit,
    onDismissInterrogation: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NoirObsidian)
            .padding(16.dp)
    ) {
        if (activeInterrogation != null) {
            // Active Interrogation Scene
            ActiveInterrogationView(
                interrogation = activeInterrogation,
                suspects = suspects,
                onChooseOption = onChooseDialogueOption,
                onClose = onDismissInterrogation
            )
        } else {
            // Suspects Directory
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ملف المشتبه بهم والشهود",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "نظام الحوار التفاعلي — الثقة، الخوف، والولاء",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }

                Surface(
                    color = CyanTerminal.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "${suspects.size} شخصيات",
                        color = CyanTerminal,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(suspects, key = { it.id }) { suspect ->
                    SuspectItemCard(
                        suspect = suspect,
                        onInterrogate = { onStartInterrogation(suspect) }
                    )
                }
            }
        }
    }
}

@Composable
fun ActiveInterrogationView(
    interrogation: ActiveInterrogation,
    suspects: List<SuspectEntity>,
    onChooseOption: (InterrogationDialogueOption) -> Unit,
    onClose: () -> Unit
) {
    val suspect = suspects.find { it.id == interrogation.suspectId }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("active_interrogation_view"),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "رجوع",
                            tint = AmberGold
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "غرفة الاستجواب والمواجهة",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Surface(
                    color = CrimsonThread.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "تسجيل مباشر ⏺",
                        color = CrimsonGlow,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }
        }

        // Suspect Header & Current Metrics
        if (suspect != null) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = NoirDarkCard),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, Color(0xFF33384A))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
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
                                            .size(54.dp)
                                            .clip(CircleShape)
                                            .border(2.dp, AmberGold, CircleShape)
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
                                        fontSize = 32.sp
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
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            NpcMetricBadge("الثقة", "${suspect.trustScore}%", EmpathyColor)
                            NpcMetricBadge("الخوف", "${suspect.fearScore}%", CrimsonGlow)
                            NpcMetricBadge("الولاء", "${suspect.loyaltyScore}%", JusticeColor)
                            NpcMetricBadge("الأسرار", suspect.secretsLevel, AmberGold)
                        }
                    }
                }
            }
        }

        // Suspect Response Box (if option chosen)
        if (interrogation.lastResponse != null) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = NoirSurfaceHighlight),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, AmberGold.copy(alpha = 0.6f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.RecordVoiceOver,
                                contentDescription = "رد المشتبه به",
                                tint = AmberGold,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "رد ${interrogation.suspectName}:",
                                color = AmberGold,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "“${interrogation.lastResponse}”",
                            color = TextPrimary,
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            fontWeight = FontWeight.Medium
                        )

                        if (interrogation.lastImpactNotice != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                color = NoirObsidian,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "الأثر الناتج: ${interrogation.lastImpactNotice}",
                                    color = CyanTerminal,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Questions / Moral Choices
        item {
            Text(
                text = "اختر خط الاستجواب والمواجهة (يحدد مسار القضية):",
                color = TextSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        items(interrogation.options) { option ->
            Card(
                colors = CardDefaults.cardColors(containerColor = NoirDarkCard),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFF33384A)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = option.title,
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Moral indicators tags
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        if (option.empathyDelta != 0) {
                            MoralBadge("التعاطف ${if (option.empathyDelta > 0) "+${option.empathyDelta}" else "${option.empathyDelta}"}", EmpathyColor)
                        }
                        if (option.justiceDelta != 0) {
                            MoralBadge("العدالة ${if (option.justiceDelta > 0) "+${option.justiceDelta}" else "${option.justiceDelta}"}", JusticeColor)
                        }
                        if (option.cautionDelta != 0) {
                            MoralBadge("الحذر ${if (option.cautionDelta > 0) "+${option.cautionDelta}" else "${option.cautionDelta}"}", CautionColor)
                        }
                        if (option.obsessionDelta != 0) {
                            MoralBadge("الهوس ${if (option.obsessionDelta > 0) "+${option.obsessionDelta}" else "${option.obsessionDelta}"}", ObsessionColor)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = { onChooseOption(option) },
                        colors = ButtonDefaults.buttonColors(containerColor = AmberGold),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().testTag("choose_dialogue_option")
                    ) {
                        Text(
                            text = "طرح هذا السؤال ومواجهته",
                            color = Color.Black,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MoralBadge(text: String, color: Color) {
    Surface(
        color = color.copy(alpha = 0.15f),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = text,
            color = color,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}
