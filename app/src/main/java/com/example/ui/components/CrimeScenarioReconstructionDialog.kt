package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.CrimeScenario
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

@Composable
fun CrimeScenarioReconstructionDialog(
    scenario: CrimeScenario,
    isHighlightedOnBoard: Boolean,
    onHighlightOnBoard: () -> Unit,
    onAdoptIntoNotebook: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = NoirSurface),
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(1.5.dp, AmberGold),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .widthIn(max = 640.dp)
                .testTag("crime_scenario_reconstruction_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // Top Bar: Seal & Title
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(CrimsonThread.copy(alpha = 0.2f))
                                .border(1.5.dp, CrimsonThread, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = scenario.category.iconEmoji, fontSize = 22.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Surface(
                                color = CrimsonThread.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "إعادة بناء الجريمة • ${scenario.category.label}",
                                    color = AmberGold,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = scenario.title,
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "إغلاق",
                            tint = TextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Scenario Status Pill
                Surface(
                    color = if (scenario.isFormed) EmpathyColor.copy(alpha = 0.15f) else AmberGold.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, if (scenario.isFormed) EmpathyColor else AmberGold),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (scenario.isFormed) Icons.Default.CheckCircle else Icons.Default.Timeline,
                            contentDescription = null,
                            tint = if (scenario.isFormed) EmpathyColor else AmberGold,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (scenario.isFormed)
                                "✓ سيناريو مؤكد بالقرائن: جميع الخيوط المطلوبة مشدودة على اللوحة"
                            else
                                "⏳ سيناريو قيد التشكيل: اربط بقية الأدلة على اللوحة لاكتمال الاستنتاج",
                            color = if (scenario.isFormed) EmpathyColor else AmberGold,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // The Crime Thesis (فرضية الجريمة)
                Surface(
                    color = NoirDarkCard,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFF383C50)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "📜", fontSize = 15.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "فرضية الجريمة (Thesis):",
                                color = AmberGold,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = scenario.thesis,
                            color = TextPrimary,
                            fontSize = 12.5.sp,
                            lineHeight = 19.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Chronological Reconstruction Timeline
                Text(
                    text = "⏱️ التسلسل الزمني لإعادة بناء وقائع الجريمة:",
                    color = AmberGold,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                scenario.timelineSteps.forEach { step ->
                    Surface(
                        color = NoirDarkCard,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, Color(0xFF2E3244)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(NoirSurfaceHighlight)
                                    .border(1.dp, AmberGold, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = step.iconEmoji, fontSize = 16.sp)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = step.headline,
                                        color = TextPrimary,
                                        fontSize = 12.5.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Surface(
                                        color = CrimsonThread.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = step.timeLabel,
                                            color = CrimsonGlow,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = step.narrativeDetails,
                                    color = TextSecondary,
                                    fontSize = 11.5.sp,
                                    lineHeight = 17.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "🔍 الدليل المثبت: ", color = AmberGold, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    Text(text = step.supportingClue, color = CyanTerminal, fontSize = 10.sp)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // The 4 Logical Deduction Pillars
                Text(
                    text = "⚖️ أركان الاستنتاج الجنائي والتحليلي:",
                    color = AmberGold,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    DeductionPillCard(
                        icon = "🎯",
                        title = "الدافع الجنائي (Motive)",
                        text = scenario.perpetratorMotive,
                        accentColor = CrimsonGlow
                    )
                    DeductionPillCard(
                        icon = "⚙️",
                        title = "أسلوب التنفيذ (Modus Operandi)",
                        text = scenario.modusOperandi,
                        accentColor = AmberGold
                    )
                    DeductionPillCard(
                        icon = "⚠️",
                        title = "الثغرة القاتلة (Fatal Flaw)",
                        text = scenario.fatalFlaw,
                        accentColor = Color(0xFFFF7043)
                    )
                    DeductionPillCard(
                        icon = "💡",
                        title = "الاستنتاج النهائي (Conclusion)",
                        text = scenario.deductionInsight,
                        accentColor = CyanTerminal
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onHighlightOnBoard,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isHighlightedOnBoard) AmberGold else NoirSurfaceHighlight
                        ),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, AmberGold),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("highlight_scenario_on_board_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = null,
                            tint = if (isHighlightedOnBoard) Color.Black else AmberGold,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isHighlightedOnBoard) "إلغاء إبراز الخيوط" else "إبراز الخيوط على اللوحة 🧵",
                            color = if (isHighlightedOnBoard) Color.Black else AmberGold,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = onAdoptIntoNotebook,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (scenario.isAdoptedInNotebook) EmpathyColor.copy(alpha = 0.3f) else CrimsonThread
                        ),
                        shape = RoundedCornerShape(10.dp),
                        enabled = !scenario.isAdoptedInNotebook,
                        modifier = Modifier
                            .weight(1.1f)
                            .testTag("adopt_scenario_notebook_btn")
                    ) {
                        Icon(
                            imageVector = if (scenario.isAdoptedInNotebook) Icons.Default.CheckCircle else Icons.Default.EditNote,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (scenario.isAdoptedInNotebook) "معتمد في الدفتر ✓" else "اعتماد في دفتر التحريات ✍️",
                            color = Color.White,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DeductionPillCard(
    icon: String,
    title: String,
    text: String,
    accentColor: Color
) {
    Surface(
        color = NoirDarkCard,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.4f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = icon, fontSize = 13.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = title,
                    color = accentColor,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = text,
                color = TextSecondary,
                fontSize = 11.sp,
                lineHeight = 16.sp
            )
        }
    }
}
