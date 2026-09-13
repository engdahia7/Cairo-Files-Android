package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.audio.DetectiveSoundEngine
import com.example.data.model.CaseLogSummary
import com.example.data.model.CaseObjective
import com.example.data.model.ObjectiveCategory
import com.example.data.model.ObjectivePriority
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

enum class CaseLogFilterTab(val label: String, val iconEmoji: String) {
    ALL("الكل", "📑"),
    IN_PROGRESS("قيد التحري", "🔍"),
    COMPLETED("المكتملة", "✅"),
    SECRET_UNLOCKED("أهداف سرية", "🔒")
}

@Composable
fun CaseLogCard(
    caseLog: CaseLogSummary,
    onOpenFullLog: () -> Unit,
    onNavigateTab: (GameTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val nextIncompleteTask = caseLog.objectives.firstOrNull { !it.isCompleted }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                DetectiveSoundEngine.playPaperRustle()
                onOpenFullLog()
            }
            .testTag("case_log_overview_card"),
        colors = CardDefaults.cardColors(containerColor = NoirDarkCard),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.2.dp, AmberGold.copy(alpha = 0.8f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header
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
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "📋", fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "سجل التحقيق والأهداف",
                                color = AmberGold,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "القضية #${caseLog.caseId}",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Progress Badge
                Surface(
                    color = if (caseLog.isReadyForVerdict) Color(0xFF1B5E20) else Color(0xFF2C2518),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(
                        1.dp,
                        if (caseLog.isReadyForVerdict) Color(0xFF4CAF50) else AmberGold.copy(alpha = 0.5f)
                    )
                ) {
                    Text(
                        text = "${caseLog.completedObjectives}/${caseLog.totalObjectives} منجز (${caseLog.completionPercentage}%)",
                        color = if (caseLog.isReadyForVerdict) Color(0xFF81C784) else AmberGold,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Progress bar
            LinearProgressIndicator(
                progress = { caseLog.completionPercentage / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = if (caseLog.isReadyForVerdict) Color(0xFF4CAF50) else AmberGold,
                trackColor = NoirSurface
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Highlight next active objective
            if (nextIncompleteTask != null) {
                Surface(
                    color = NoirSurface.copy(alpha = 0.85f),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Color(nextIncompleteTask.category.hexColor).copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = nextIncompleteTask.category.iconEmoji,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    color = Color(nextIncompleteTask.category.hexColor).copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = nextIncompleteTask.category.label,
                                        color = Color(nextIncompleteTask.category.hexColor),
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "الهدف التالي",
                                    color = TextSecondary,
                                    fontSize = 10.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = nextIncompleteTask.title,
                                color = TextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                        }

                        if (nextIncompleteTask.targetTab != null && nextIncompleteTask.targetActionLabel != null) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = AmberGold,
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier
                                    .clickable {
                                        DetectiveSoundEngine.playPaperRustle()
                                        onNavigateTab(nextIncompleteTask.targetTab)
                                    }
                                    .testTag("quick_action_task_btn")
                            ) {
                                Text(
                                    text = nextIncompleteTask.targetActionLabel,
                                    color = Color.Black,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            } else {
                Surface(
                    color = Color(0xFF1B5E20).copy(alpha = 0.3f),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Color(0xFF4CAF50).copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "✨", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "تم استيفاء جميع أهداف التحري! القضية جاهزة للإغلاق.",
                            color = Color(0xFF81C784),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Footer info and expand button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "الأدلة: ${caseLog.discoveredCluesCount}/${caseLog.totalCluesCount} • ${caseLog.readinessStatus}",
                    color = TextSecondary,
                    fontSize = 10.sp,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onOpenFullLog() }
                ) {
                    Text(
                        text = "فتح السجل المفصل",
                        color = AmberGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(text = "◀", color = AmberGold, fontSize = 10.sp)
                }
            }
        }
    }
}

@Composable
fun CaseLogDialog(
    caseLog: CaseLogSummary,
    onDismiss: () -> Unit,
    onNavigateTab: (GameTab) -> Unit,
    onOpenNotebook: () -> Unit = {},
    onOpenForensicLab: () -> Unit = {},
    onOpenDeduction: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf(CaseLogFilterTab.ALL) }
    var expandedObjectiveId by remember { mutableStateOf<String?>(null) }

    val filteredObjectives = remember(caseLog.objectives, selectedFilter) {
        when (selectedFilter) {
            CaseLogFilterTab.ALL -> caseLog.objectives
            CaseLogFilterTab.IN_PROGRESS -> caseLog.objectives.filter { !it.isCompleted }
            CaseLogFilterTab.COMPLETED -> caseLog.objectives.filter { it.isCompleted }
            CaseLogFilterTab.SECRET_UNLOCKED -> caseLog.objectives.filter { it.isSecretClueUnlocked }
        }
    }

    Dialog(
        onDismissRequest = {
            DetectiveSoundEngine.playPaperRustle()
            onDismiss()
        },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(12.dp)
                .testTag("case_log_full_dialog"),
            color = NoirObsidian,
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(1.5.dp, AmberGold)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Top Action Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = AmberGold,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = "📋", fontSize = 18.sp)
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "سجل التحقيق والأهداف الجارية",
                                    color = TextPrimary,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    color = CrimsonThread.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "CASE LOG",
                                        color = CrimsonGlow,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }
                            Text(
                                text = "القضية #${caseLog.caseId}: ${caseLog.caseTitle}",
                                color = AmberGold,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    IconButton(
                        onClick = {
                            DetectiveSoundEngine.playPaperRustle()
                            onDismiss()
                        },
                        modifier = Modifier.testTag("close_case_log_dialog_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "إغلاق",
                            tint = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Progress & Readiness Overview Banner
                Surface(
                    color = NoirDarkCard,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFF33384A)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "مستوى الجاهزية والتقدم الميداني",
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = caseLog.readinessStatus,
                                    color = if (caseLog.isReadyForVerdict) Color(0xFF81C784) else AmberGold,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Surface(
                                color = if (caseLog.isReadyForVerdict) Color(0xFF1B5E20) else Color(0xFF231C14),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(
                                    1.dp,
                                    if (caseLog.isReadyForVerdict) Color(0xFF4CAF50) else AmberGold.copy(alpha = 0.6f)
                                )
                            ) {
                                Text(
                                    text = "${caseLog.completedObjectives}/${caseLog.totalObjectives} هدف (${caseLog.completionPercentage}%)",
                                    color = if (caseLog.isReadyForVerdict) Color(0xFF81C784) else AmberGold,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        LinearProgressIndicator(
                            progress = { caseLog.completionPercentage / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(7.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = if (caseLog.isReadyForVerdict) Color(0xFF4CAF50) else AmberGold,
                            trackColor = NoirSurface
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "🔍 القرائن المكتشفة: ${caseLog.discoveredCluesCount}/${caseLog.totalCluesCount}",
                                color = CyanTerminal,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "🛡️ نشاط شبكة المرآة: ${caseLog.mirrorThreatLevel}",
                                color = TextSecondary,
                                fontSize = 10.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Filter Tabs Strip
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    CaseLogFilterTab.values().forEach { tab ->
                        val count = when (tab) {
                            CaseLogFilterTab.ALL -> caseLog.objectives.size
                            CaseLogFilterTab.IN_PROGRESS -> caseLog.objectives.count { !it.isCompleted }
                            CaseLogFilterTab.COMPLETED -> caseLog.objectives.count { it.isCompleted }
                            CaseLogFilterTab.SECRET_UNLOCKED -> caseLog.objectives.count { it.isSecretClueUnlocked }
                        }

                        FilterChip(
                            selected = selectedFilter == tab,
                            onClick = {
                                DetectiveSoundEngine.playPaperRustle()
                                selectedFilter = tab
                            },
                            label = {
                                Text(
                                    text = "${tab.iconEmoji} ${tab.label} ($count)",
                                    fontSize = 11.sp,
                                    fontWeight = if (selectedFilter == tab) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AmberGold,
                                selectedLabelColor = Color.Black,
                                containerColor = NoirDarkCard,
                                labelColor = TextSecondary
                            ),
                            border = BorderStroke(
                                1.dp,
                                if (selectedFilter == tab) AmberGold else Color(0xFF33384A)
                            ),
                            modifier = Modifier.testTag("case_log_tab_${tab.name.lowercase()}")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Objectives Lazy List
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (filteredObjectives.isEmpty()) {
                        item {
                            Surface(
                                color = NoirDarkCard,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 20.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = "📭", fontSize = 28.sp)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "لا توجد مهام في هذا التصنيف حالياً",
                                        color = TextSecondary,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    } else {
                        items(filteredObjectives, key = { it.id }) { objective ->
                            ObjectiveItemCard(
                                objective = objective,
                                isExpanded = expandedObjectiveId == objective.id,
                                onToggleExpand = {
                                    DetectiveSoundEngine.playPaperRustle()
                                    expandedObjectiveId = if (expandedObjectiveId == objective.id) null else objective.id
                                },
                                onNavigateTab = { tab ->
                                    onDismiss()
                                    onNavigateTab(tab)
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Quick Action Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Open Notebook
                    Surface(
                        color = Color(0xFF261E1A),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, AmberGold.copy(alpha = 0.7f)),
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                onDismiss()
                                onOpenNotebook()
                            }
                            .testTag("case_log_open_notebook_btn")
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "📝", fontSize = 13.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "الدفتر",
                                color = AmberGold,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Open Forensic Lab
                    Surface(
                        color = Color(0xFF142029),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, CyanTerminal.copy(alpha = 0.7f)),
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                onDismiss()
                                onOpenForensicLab()
                            }
                            .testTag("case_log_open_lab_btn")
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "🔬", fontSize = 13.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "المعمل",
                                color = CyanTerminal,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Verdict / Deduction
                    Surface(
                        color = if (caseLog.isReadyForVerdict) AmberGold else Color(0xFF2A201A),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(
                            1.dp,
                            if (caseLog.isReadyForVerdict) AmberGold else Color(0xFF554433)
                        ),
                        modifier = Modifier
                            .weight(1.3f)
                            .clickable {
                                onDismiss()
                                onOpenDeduction()
                            }
                            .testTag("case_log_open_deduction_btn")
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "⚖️", fontSize = 13.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "إغلاق القضية",
                                color = if (caseLog.isReadyForVerdict) Color.Black else TextSecondary,
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
private fun ObjectiveItemCard(
    objective: CaseObjective,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onNavigateTab: (GameTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryColor = Color(objective.category.hexColor)
    val cardBg = if (objective.isCompleted) NoirDarkCard.copy(alpha = 0.6f) else NoirDarkCard
    val borderColor = if (objective.isCompleted) Color(0xFF2E7D32).copy(alpha = 0.5f) else categoryColor.copy(alpha = 0.4f)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onToggleExpand() }
            .testTag("objective_card_${objective.id}"),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Top info line: Checkbox + Title + Category Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Custom Checkbox
                Surface(
                    color = if (objective.isCompleted) Color(0xFF2E7D32) else Color.Transparent,
                    shape = CircleShape,
                    border = BorderStroke(
                        1.5.dp,
                        if (objective.isCompleted) Color(0xFF4CAF50) else TextSecondary.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier.size(22.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        if (objective.isCompleted) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "مكتمل",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Surface(
                            color = categoryColor.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = objective.category.iconEmoji, fontSize = 9.sp)
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = objective.category.label,
                                    color = categoryColor,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        if (objective.priority == ObjectivePriority.SECRET_DISCOVERY || objective.isSecretClueUnlocked) {
                            Surface(
                                color = Color(0xFF8E24AA).copy(alpha = 0.2f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "🔒 كشف سري",
                                    color = Color(0xFFCE93D8),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = objective.title,
                        color = if (objective.isCompleted) TextSecondary else TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        textDecoration = if (objective.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    )
                }

                // Quick Navigation Action Button
                if (!objective.isCompleted && objective.targetTab != null && objective.targetActionLabel != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        color = AmberGold,
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier
                            .clickable {
                                DetectiveSoundEngine.playPaperRustle()
                                onNavigateTab(objective.targetTab)
                            }
                            .testTag("action_btn_${objective.id}")
                    ) {
                        Text(
                            text = objective.targetActionLabel,
                            color = Color.Black,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                        )
                    }
                }
            }

            // Description
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = objective.description,
                color = TextSecondary,
                fontSize = 11.sp,
                lineHeight = 16.sp
            )

            // Dynamic clue unlock callout
            if (objective.isSecretClueUnlocked && objective.unlockedByClueTitle != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    color = Color(0xFF4A148C).copy(alpha = 0.25f),
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, Color(0xFFAB47BC).copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "✨", fontSize = 11.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "تم تفعيل هذا الهدف بفضل العثور على: ${objective.unlockedByClueTitle}",
                            color = Color(0xFFE1BEE7),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Expanded Detective Monologue & Tips
            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    if (objective.detectiveTip.isNotEmpty()) {
                        Surface(
                            color = NoirSurfaceHighlight,
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, AmberGold.copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(modifier = Modifier.padding(8.dp)) {
                                Text(text = "💡", fontSize = 13.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(
                                        text = "ملاحظة المحقق الميدانية:",
                                        color = AmberGold,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = objective.detectiveTip,
                                        color = TextPrimary,
                                        fontSize = 11.sp,
                                        lineHeight = 15.sp
                                    )
                                }
                            }
                        }
                    }

                    if (objective.rewardInsight.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🎁", fontSize = 11.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "المردود: ${objective.rewardInsight}",
                                color = CyanTerminal,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}
