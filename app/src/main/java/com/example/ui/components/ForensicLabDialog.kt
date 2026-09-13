package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Science
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.ForensicExamTarget
import com.example.data.model.ForensicToolType
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
fun ForensicLabDialog(
    targets: List<ForensicExamTarget>,
    selectedTarget: ForensicExamTarget?,
    selectedTool: ForensicToolType,
    activeFindingText: String?,
    onSelectTarget: (ForensicExamTarget) -> Unit,
    onSelectTool: (ForensicToolType) -> Unit,
    onApplyTool: (ForensicToolType) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(12.dp)
                .testTag("forensic_lab_dialog"),
            shape = RoundedCornerShape(18.dp),
            color = NoirObsidian,
            border = BorderStroke(1.5.dp, CyanTerminal.copy(alpha = 0.8f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header (Lab Station Title & Close)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = CyanTerminal.copy(alpha = 0.2f),
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Science,
                                    contentDescription = null,
                                    tint = CyanTerminal,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "معمل الفحص الجنائي والسموم ١٩٤٨",
                                color = CyanTerminal,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "مصلحة الطب الشرعي وقسم الكواشف الكيميائية",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_forensic_lab_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "إغلاق المعمل",
                            tint = TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Section 1: Specimen Carousel (الأحراز والعينات قيد الفحص)
                Text(
                    text = "اختر الحرز أو العينة الجنائية للفحص:",
                    color = TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(targets, key = { it.id }) { target ->
                        val isSelected = target.id == selectedTarget?.id
                        Surface(
                            color = if (isSelected) NoirSurfaceHighlight else NoirDarkCard,
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(
                                if (isSelected) 1.5.dp else 1.dp,
                                if (isSelected) AmberGold else Color(0xFF33384A)
                            ),
                            modifier = Modifier
                                .width(220.dp)
                                .clickable { onSelectTarget(target) }
                                .testTag("forensic_specimen_${target.id}")
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (target.isExamined) "تم الفحص ✓" else "بانتظار التحليل",
                                        color = if (target.isExamined) EmpathyColor else TextMuted,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = target.caseTitle,
                                        color = AmberGold,
                                        fontSize = 10.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = target.name,
                                    color = TextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = target.initialDescription,
                                    color = TextSecondary,
                                    fontSize = 10.sp,
                                    lineHeight = 14.sp,
                                    maxLines = 2
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Section 2: Tool Dock (أدوات المعمل الجنائي الأربعة)
                Text(
                    text = "اختر الأداة الجنائية أو الكاشف الكيميائي:",
                    color = TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    ForensicToolType.values().forEach { tool ->
                        val isSelected = tool == selectedTool
                        Surface(
                            color = if (isSelected) CyanTerminal.copy(alpha = 0.2f) else NoirDarkCard,
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(
                                if (isSelected) 1.5.dp else 1.dp,
                                if (isSelected) CyanTerminal else Color(0xFF33384A)
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onSelectTool(tool) }
                                .testTag("forensic_tool_${tool.name}")
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = tool.iconEmoji, fontSize = 20.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = when (tool) {
                                        ForensicToolType.MAGNIFIER -> "مكبر 20x"
                                        ForensicToolType.SECRET_INK_REAGENT -> "كاشف اليود"
                                        ForensicToolType.UV_WOODS_LAMP -> "أشعة UV"
                                        ForensicToolType.TOXICOLOGY_TUBE -> "فحص السموم"
                                    },
                                    color = if (isSelected) CyanTerminal else TextSecondary,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Section 3: Interactive Viewport (شاشة الفحص المعملي التفاعلية)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    colors = CardDefaults.cardColors(
                        containerColor = when (selectedTool) {
                            ForensicToolType.UV_WOODS_LAMP -> Color(0xFF140B24) // إضاءة فوق بنفسجية معتمة
                            ForensicToolType.SECRET_INK_REAGENT -> Color(0xFF1C1A14) // بيئة كاشف كيميائي بني
                            ForensicToolType.TOXICOLOGY_TUBE -> Color(0xFF221115) // بيئة سموم قرمزي
                            ForensicToolType.MAGNIFIER -> Color(0xFF141920) // بيئة مجهرية زرقاء
                        }
                    ),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(
                        1.5.dp,
                        when (selectedTool) {
                            ForensicToolType.UV_WOODS_LAMP -> Color(0xFFB388FF)
                            ForensicToolType.SECRET_INK_REAGENT -> AmberGold
                            ForensicToolType.TOXICOLOGY_TUBE -> CrimsonGlow
                            ForensicToolType.MAGNIFIER -> CyanTerminal
                        }
                    )
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        item {
                            // Viewport Header
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "منصة الفحص: ${selectedTarget?.name ?: "لم يتم اختيار عينة"}",
                                    color = TextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Surface(
                                    color = when (selectedTool) {
                                        ForensicToolType.UV_WOODS_LAMP -> Color(0xFF7C4DFF).copy(alpha = 0.3f)
                                        ForensicToolType.SECRET_INK_REAGENT -> AmberGold.copy(alpha = 0.2f)
                                        ForensicToolType.TOXICOLOGY_TUBE -> CrimsonThread.copy(alpha = 0.3f)
                                        ForensicToolType.MAGNIFIER -> CyanTerminal.copy(alpha = 0.2f)
                                    },
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = selectedTool.label,
                                        color = when (selectedTool) {
                                            ForensicToolType.UV_WOODS_LAMP -> Color(0xFFD1C4E9)
                                            ForensicToolType.SECRET_INK_REAGENT -> AmberGold
                                            ForensicToolType.TOXICOLOGY_TUBE -> CrimsonGlow
                                            ForensicToolType.MAGNIFIER -> CyanTerminal
                                        },
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = selectedTool.description,
                                color = TextMuted,
                                fontSize = 10.sp,
                                lineHeight = 14.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Finding Display (If tested)
                            if (activeFindingText != null) {
                                Surface(
                                    color = NoirDarkCard,
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(1.dp, AmberGold.copy(alpha = 0.5f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(text = "🧪 نتيجة الفحص المعملي المؤكدة:", color = AmberGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = activeFindingText,
                                            color = TextPrimary,
                                            fontSize = 12.sp,
                                            lineHeight = 17.sp
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = "✓ تم تسجيل النتيجة تلقائياً في دفتر ملاحظات المحقق وفي سجل ملفات المرآة.",
                                            color = EmpathyColor,
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(100.dp)
                                        .background(NoirDarkCard.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                                        .border(1.dp, Color(0xFF33384A), RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = "${selectedTool.iconEmoji} جاهز لتطبيق الفحص الكيميائي/المجهري",
                                            color = TextSecondary,
                                            fontSize = 12.sp
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "اضغط على زر (بدء الفحص المعملي) أدناه لاستخراج النتيجة الجنائية",
                                            color = TextMuted,
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(10.dp))
                            // Action Button: Apply Tool to Target
                            Button(
                                onClick = { onApplyTool(selectedTool) },
                                enabled = selectedTarget != null,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = when (selectedTool) {
                                        ForensicToolType.UV_WOODS_LAMP -> Color(0xFF651FFF)
                                        ForensicToolType.SECRET_INK_REAGENT -> AmberGold
                                        ForensicToolType.TOXICOLOGY_TUBE -> CrimsonThread
                                        ForensicToolType.MAGNIFIER -> CyanTerminal
                                    }
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("apply_forensic_tool_btn")
                            ) {
                                Text(
                                    text = "بدء الفحص المعملي الآن (${selectedTool.label}) 🔬",
                                    color = if (selectedTool == ForensicToolType.SECRET_INK_REAGENT || selectedTool == ForensicToolType.MAGNIFIER) Color.Black else Color.White,
                                    fontSize = 12.sp,
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
