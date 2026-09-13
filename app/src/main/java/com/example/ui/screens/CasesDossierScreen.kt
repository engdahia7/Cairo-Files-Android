package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.data.model.CaseEntity
import com.example.data.model.CaseStatus
import com.example.data.model.EvidenceEntity
import com.example.ui.components.CaseItemCard
import com.example.ui.components.EvidenceItemCard
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
fun CasesDossierScreen(
    allCases: List<CaseEntity>,
    selectedChapter: Int,
    activeCase: CaseEntity?,
    caseEvidence: List<EvidenceEntity>,
    onSelectChapter: (Int) -> Unit,
    onSelectCase: (CaseEntity) -> Unit,
    onAdvanceCase: (Int) -> Unit,
    onDiscoverEvidence: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var inspectingCase by remember { mutableStateOf<CaseEntity?>(activeCase) }
    var deductionCase by remember { mutableStateOf<CaseEntity?>(null) }
    var deepStoryCase by remember { mutableStateOf<CaseEntity?>(null) }

    val chapters = listOf(
        1 to "فصل 1: بناء المحقق (1-10)",
        2 to "فصل 2: ظهور المرآة (11-20)",
        3 to "فصل 3: محمد في القضية (21-30)",
        4 to "فصل 4: الخيانة والصراع (31-40)",
        5 to "فصل 5: النهاية الكبرى (41-50)"
    )

    val currentChapterCases = allCases.filter { it.chapter == selectedChapter }

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
                Text(
                    text = "سجل القضايا الجنائية (50 قضية)",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "كل قضية خيط يقود للمنظمة الرئيسية 'المرآة'",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }

            Surface(
                color = AmberGold.copy(alpha = 0.15f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "مجموع 5 فصول",
                    color = AmberGold,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Horizontal Chapter Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            chapters.forEach { (chapterNum, title) ->
                val isSelected = selectedChapter == chapterNum
                FilterChip(
                    selected = isSelected,
                    onClick = { onSelectChapter(chapterNum) },
                    label = {
                        Text(
                            text = title,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
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
                        if (isSelected) AmberGold else Color(0xFF2E3344)
                    ),
                    modifier = Modifier.testTag("chapter_tab_$chapterNum")
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Inspecting Case Detail Dialog / Banner
        if (inspectingCase != null) {
            val c = inspectingCase!!
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("case_detail_panel"),
                colors = CardDefaults.cardColors(containerColor = NoirSurfaceHighlight),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, AmberGold)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Folder,
                                contentDescription = "ملف القضية",
                                tint = AmberGold,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "ملف قضية #${c.id}: ${c.title}",
                                color = TextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        IconButton(
                            onClick = { inspectingCase = null },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "إغلاق",
                                tint = TextMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "المشهد الافتتاحي: ${c.coldOpenNarrative}",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "الشاكي",
                                tint = CyanTerminal,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "الشاكي: ${c.client}",
                                color = TextPrimary,
                                fontSize = 11.sp
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = "التوقيت",
                                tint = AmberGold,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = c.timeLabel,
                                color = AmberGold,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Action buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                deepStoryCase = c
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AmberGold),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("investigate_case_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = "أحداث التحقيق المتسلسلة",
                                tint = Color.Black,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "بدء أحداث القضية 🔍",
                                color = Color.Black,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = {
                                deductionCase = c
                            },
                            enabled = c.progressPercent >= 50,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CyanTerminal,
                                disabledContainerColor = NoirSurface
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("solve_deduction_btn")
                        ) {
                            Text(
                                text = if (c.progressPercent >= 50) "غرفة الاستنتاج 💡" else "🔒 اجمع الأدلة أولاً",
                                color = if (c.progressPercent >= 50) Color.Black else TextMuted,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Deep Investigation Story Screen Dialog
        if (deepStoryCase != null) {
            com.example.ui.components.DeepCaseInvestigationScreen(
                caseEntity = deepStoryCase!!,
                onAdvanceCaseProgress = {
                    onAdvanceCase(deepStoryCase!!.id)
                    inspectingCase = inspectingCase?.copy(
                        progressPercent = (inspectingCase?.progressPercent ?: 0) + 25
                    )
                },
                onReadyForDeduction = {
                    deductionCase = deepStoryCase
                    deepStoryCase = null
                },
                onClose = { deepStoryCase = null }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Deduction dialog when triggered
        if (deductionCase != null) {
            com.example.ui.components.CaseDeductionDialog(
                caseEntity = deductionCase!!,
                onSolveSuccess = {
                    val targetId = deductionCase!!.id
                    onAdvanceCase(targetId)
                    onAdvanceCase(targetId)
                    onAdvanceCase(targetId)
                    onAdvanceCase(targetId)
                    deductionCase = null
                    inspectingCase = null
                },
                onClose = { deductionCase = null }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Cases List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(currentChapterCases, key = { it.id }) { caseItem ->
                CaseItemCard(
                    caseEntity = caseItem,
                    isSelected = inspectingCase?.id == caseItem.id,
                    onClick = {
                        inspectingCase = caseItem
                        onSelectCase(caseItem)
                    },
                    onAdvanceProgress = {
                        onAdvanceCase(caseItem.id)
                    }
                )
            }
        }
    }
}
