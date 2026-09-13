package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import com.example.data.model.ColdCaseSideJob
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
fun ColdCasesSectionCard(
    coldCases: List<ColdCaseSideJob>,
    selectedCase: ColdCaseSideJob?,
    onSelectCase: (ColdCaseSideJob?) -> Unit,
    onSolveCase: (String, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("cold_cases_section_card"),
        colors = CardDefaults.cardColors(containerColor = NoirDarkCard),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, CyanTerminal.copy(alpha = 0.4f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(CyanTerminal.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FolderSpecial,
                            contentDescription = "بلاغات القاهرة الجانبية",
                            tint = CyanTerminal,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "بلاغات القاهرة الفرعية (Cold Cases)",
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "ألغاز سريعة من مواطني ومحامي العاصمة",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                val solvedCount = coldCases.count { it.isCompleted }
                Surface(
                    color = AmberGold.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "$solvedCount / ${coldCases.size} مكتمل",
                        color = AmberGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Cold Cases List
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                coldCases.forEach { job ->
                    val isSelected = selectedCase?.id == job.id
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) NoirSurfaceHighlight else NoirSurface
                        ),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) CyanTerminal else if (job.isCompleted) EmpathyColor.copy(alpha = 0.4f) else Color(0xFF2E3448)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectCase(if (isSelected) null else job) }
                            .testTag("cold_case_item_${job.id}")
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (job.isCompleted) Icons.Default.CheckCircle else Icons.Default.Psychology,
                                        contentDescription = null,
                                        tint = if (job.isCompleted) EmpathyColor else AmberGold,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = job.title,
                                            color = TextPrimary,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "${job.district} • المصدر: ${job.informant}",
                                            color = TextMuted,
                                            fontSize = 11.sp
                                        )
                                    }
                                }

                                Surface(
                                    color = if (job.isCompleted) EmpathyColor.copy(alpha = 0.2f) else AmberGold.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = if (job.isCompleted) "محلول ✓" else "مفتوح",
                                        color = if (job.isCompleted) EmpathyColor else AmberGold,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = job.narrative,
                                color = TextSecondary,
                                fontSize = 11.sp,
                                lineHeight = 16.sp
                            )

                            // Interactive Riddle Solving Form
                            if (isSelected) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Surface(
                                    color = NoirObsidian,
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(1.dp, Color(0xFF33384A)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.HelpOutline,
                                                contentDescription = null,
                                                tint = CyanTerminal,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = job.riddleQuestion,
                                                color = CyanTerminal,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        job.options.forEachIndexed { index, optionText ->
                                            val isSolvedOption = job.isCompleted && index == job.correctOptionIndex
                                            Surface(
                                                color = if (isSolvedOption) EmpathyColor.copy(alpha = 0.25f) else NoirSurfaceHighlight,
                                                shape = RoundedCornerShape(6.dp),
                                                border = BorderStroke(
                                                    1.dp,
                                                    if (isSolvedOption) EmpathyColor else Color(0xFF323B52)
                                                ),
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 3.dp)
                                                    .clickable(enabled = !job.isCompleted) {
                                                        onSolveCase(job.id, index)
                                                    }
                                                    .testTag("cold_case_opt_${job.id}_$index")
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(8.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text(
                                                        text = "${index + 1}.",
                                                        color = AmberGold,
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Text(
                                                        text = optionText,
                                                        color = TextPrimary,
                                                        fontSize = 11.sp
                                                    )
                                                }
                                            }
                                        }

                                        if (job.isCompleted) {
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Surface(
                                                color = Color(0xFF14241B),
                                                shape = RoundedCornerShape(6.dp),
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Column(modifier = Modifier.padding(8.dp)) {
                                                    Text(
                                                        text = "شرح الحل: ${job.explanationOnSolve}",
                                                        color = EmpathyColor,
                                                        fontSize = 11.sp
                                                    )
                                                    Spacer(modifier = Modifier.height(4.dp))
                                                    Text(
                                                        text = "مكافأة المرآة: ${job.mirrorClueReward}",
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
                    }
                }
            }
        }
    }
}
