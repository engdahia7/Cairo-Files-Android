package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CaseEntity
import com.example.data.model.CaseStoryRegistry
import com.example.data.model.InvestigationStoryStep
import com.example.data.model.StoryChoiceOption
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
fun DeepCaseInvestigationScreen(
    caseEntity: CaseEntity,
    onAdvanceCaseProgress: () -> Unit,
    onReadyForDeduction: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val steps = remember(caseEntity.id) { CaseStoryRegistry.getStoryStepsForCase(caseEntity.id) }
    var currentStepIndex by remember { mutableStateOf(0) }
    val discoveredClues = remember { mutableStateListOf<String>() }
    var selectedChoice by remember { mutableStateOf<StoryChoiceOption?>(null) }
    var choiceFeedback by remember { mutableStateOf<String?>(null) }
    var hasAdvancedCurrentStep by remember { mutableStateOf(false) }

    val currentStep = steps.getOrNull(currentStepIndex) ?: steps.last()
    val isLastStep = currentStepIndex >= steps.size - 1

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("deep_investigation_panel"),
        colors = CardDefaults.cardColors(containerColor = NoirDarkCard),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.5.dp, AmberGold)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = AmberGold.copy(alpha = 0.2f),
                        shape = CircleShape,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = "أحداث القضية",
                                tint = AmberGold,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "تحقيق ميداني: قضية #${caseEntity.id}",
                            color = AmberGold,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = caseEntity.title,
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Surface(
                    color = NoirSurface,
                    shape = CircleShape,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable { onClose() }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "إغلاق التحقيق",
                            tint = TextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Step Indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "محطة التحقيق ${currentStepIndex + 1} من ${steps.size}",
                    color = CyanTerminal,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${((currentStepIndex + 1).toFloat() / steps.size * 100).toInt()}% مكتمل",
                    color = AmberGold,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { (currentStepIndex + 1).toFloat() / steps.size },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = AmberGold,
                trackColor = NoirSurface
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Station Title & Location
            Surface(
                color = NoirSurfaceHighlight,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, Color(0xFF323648)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = currentStep.title,
                        color = AmberGold,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = CrimsonThread,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = currentStep.locationDescription,
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Narrative Story Box
            Surface(
                color = NoirObsidian,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, Color(0xFF262938)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "📜 مجريات الواقعة والتحريات:",
                        color = CyanTerminal,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = currentStep.narrativeEvent,
                        color = TextPrimary,
                        fontSize = 13.sp,
                        lineHeight = 19.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "❓ التحدي: ${currentStep.interactiveChallenge}",
                        color = AmberGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Interactive Choices
            Text(
                text = "اختر قرارك الجنائي لمواصلة التحقيق:",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(6.dp))

            currentStep.choiceOptions.forEach { option ->
                val isSelected = selectedChoice?.choiceId == option.choiceId
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            selectedChoice = option
                            choiceFeedback = option.detailedReaction
                            if (!discoveredClues.contains(option.clueUnlocked)) {
                                discoveredClues.add(option.clueUnlocked)
                            }
                            if (option.isProgressAdvancing && !hasAdvancedCurrentStep) {
                                hasAdvancedCurrentStep = true
                                onAdvanceCaseProgress()
                            }
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) NoirSurfaceHighlight else NoirSurface
                    ),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(
                        width = if (isSelected) 1.5.dp else 1.dp,
                        color = if (isSelected) AmberGold else Color(0xFF2A2D3C)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = if (isSelected) AmberGold else Color(0xFF252836),
                            shape = CircleShape,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "${option.choiceId}",
                                    color = if (isSelected) Color.Black else TextSecondary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = option.label,
                                color = if (isSelected) AmberGold else TextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = option.impactTag,
                                color = if (option.isProgressAdvancing) EmpathyColor else CrimsonGlow,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Detailed Story Reaction Feedback
            if (choiceFeedback != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    color = EmpathyColor.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, EmpathyColor.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = EmpathyColor,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "ما كشفته المعاينة الميدانية:",
                                color = EmpathyColor,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = choiceFeedback!!,
                            color = TextPrimary,
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                    }
                }
            }

            // Discovered Clues Box
            if (discoveredClues.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    color = NoirSurface,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "الأدلة والقرائن المحرزة من هذه القضية (${discoveredClues.size}):",
                            color = AmberGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        discoveredClues.forEach { clue ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Fingerprint,
                                    contentDescription = null,
                                    tint = AmberGold,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = clue,
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Navigation between steps or to Deduction Room
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (currentStepIndex > 0) {
                    Button(
                        onClick = {
                            currentStepIndex--
                            selectedChoice = null
                            choiceFeedback = null
                            hasAdvancedCurrentStep = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NoirSurface),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(0.8f)
                    ) {
                        Text("السابق", color = TextSecondary, fontSize = 12.sp)
                    }
                }

                if (!isLastStep) {
                    Button(
                        onClick = {
                            currentStepIndex++
                            selectedChoice = null
                            choiceFeedback = null
                            hasAdvancedCurrentStep = false
                        },
                        enabled = selectedChoice != null,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AmberGold,
                            disabledContainerColor = NoirSurface
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1.2f)
                    ) {
                        Text(
                            text = if (selectedChoice != null) "المحطة التالية ➡️" else "اختر قراراً للمتابعة",
                            color = if (selectedChoice != null) Color.Black else TextMuted,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Button(
                        onClick = {
                            onClose()
                            onReadyForDeduction()
                        },
                        enabled = selectedChoice != null,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EmpathyColor,
                            disabledContainerColor = NoirSurface
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1.2f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "إلى غرفة الاستنتاج النهائي 💡",
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
