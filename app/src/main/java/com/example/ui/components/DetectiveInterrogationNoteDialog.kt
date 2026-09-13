package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.EvidenceEntity
import com.example.data.model.NotebookCategory
import com.example.data.model.SuspectEntity
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DetectiveInterrogationNoteDialog(
    suspect: SuspectEntity?,
    allSuspects: List<SuspectEntity> = emptyList(),
    evidenceList: List<EvidenceEntity>,
    preSelectedEvidenceId: String? = null,
    onSaveNote: (
        title: String,
        content: String,
        category: NotebookCategory,
        linkedEvidenceIds: List<String>,
        linkedEvidenceTitles: List<String>,
        suspectId: String?,
        suspectName: String?,
        noteTypeTag: String
    ) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Chosen Suspect
    var selectedSuspect by remember(suspect) { mutableStateOf(suspect) }

    // Multi-selected evidence IDs
    val selectedEvidenceIds = remember(preSelectedEvidenceId) {
        mutableStateListOf<String>().apply {
            if (preSelectedEvidenceId != null) add(preSelectedEvidenceId)
        }
    }

    // Deduction focus / note type
    val noteTypes = listOf(
        "تناقض في الإفادة" to "⚖️",
        "شبهة جنائية مباشرة" to "🔍",
        "تطابق زمني مع الأحراز" to "⏱️",
        "سلوك ولغة جسد متوترة" to "🧠",
        "فرضية واستنتاج حر" to "💡"
    )
    var selectedNoteType by remember { mutableStateOf(noteTypes[0].first) }

    // Title & Content Inputs
    var noteTitle by remember {
        mutableStateOf(
            if (suspect != null) "ملاحظة حول إفادة ${suspect.name}"
            else "ملاحظة ميدانية جديدة"
        )
    }
    var noteContent by remember { mutableStateOf("") }

    // Quick thought suggestions based on chosen type and suspect
    val quickThoughts = remember(selectedSuspect, selectedNoteType) {
        generateQuickThoughts(selectedSuspect?.name, selectedNoteType)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(10.dp)
                .testTag("detective_interrogation_note_dialog"),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF1B1513), // جلد بني عتيق لدفاتر تحريات 1948
            border = BorderStroke(2.dp, AmberGold.copy(alpha = 0.85f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp)
            ) {
                // Top Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = AmberGold.copy(alpha = 0.2f),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.EditNote,
                                    contentDescription = null,
                                    tint = AmberGold,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "مذكرة استجواب المحقق عبده 🖋️",
                                color = AmberGold,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "تدوين ملاحظات ميدانية وربطها بأحراز لوحة التحقيق",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_interrogation_note_dialog")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "إغلاق",
                            tint = TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Suspect Card / Selector
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = NoirDarkCard),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, Color(0xFF3B332E))
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "المشتبه به أو الشاهد محل التحري:",
                                    color = TextSecondary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(6.dp))

                                if (selectedSuspect != null) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(text = selectedSuspect?.avatarEmoji ?: "👤", fontSize = 22.sp)
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Column {
                                                Text(
                                                    text = selectedSuspect?.name ?: "",
                                                    color = TextPrimary,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Text(
                                                    text = selectedSuspect?.roleTitle ?: "",
                                                    color = AmberGold,
                                                    fontSize = 11.sp
                                                )
                                            }
                                        }

                                        Surface(
                                            color = CrimsonThread.copy(alpha = 0.2f),
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = "جلسة استجواب نشطة 🎙️",
                                                color = CrimsonGlow,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                            )
                                        }
                                    }
                                } else {
                                    // Selector when opened without an active suspect
                                    LazyRow(
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        contentPadding = PaddingValues(vertical = 4.dp)
                                    ) {
                                        items(allSuspects) { s ->
                                            val isChosen = selectedSuspect?.id == s.id
                                            Surface(
                                                color = if (isChosen) AmberGold else NoirSurfaceHighlight,
                                                shape = RoundedCornerShape(8.dp),
                                                border = BorderStroke(1.dp, if (isChosen) AmberGold else Color(0xFF4A403A)),
                                                modifier = Modifier.clickable {
                                                    selectedSuspect = if (isChosen) null else s
                                                    if (!isChosen) {
                                                        noteTitle = "ملاحظة حول إفادة ${s.name}"
                                                    }
                                                }
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text(text = s.avatarEmoji, fontSize = 12.sp)
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text(
                                                        text = s.name,
                                                        color = if (isChosen) Color.Black else TextPrimary,
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

                    // Linked Evidence Selector (Core requirement: link notes to corkboard evidence)
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = NoirDarkCard),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, AmberGold.copy(alpha = 0.4f))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.PinDrop,
                                            contentDescription = null,
                                            tint = AmberGold,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "ربط الملاحظة بأحراز لوحة التحقيق 📌",
                                            color = AmberGold,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Surface(
                                        color = if (selectedEvidenceIds.isNotEmpty()) CyanTerminal.copy(alpha = 0.2f) else NoirSurface,
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = "${selectedEvidenceIds.size} أحراز محددة",
                                            color = if (selectedEvidenceIds.isNotEmpty()) CyanTerminal else TextMuted,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Text(
                                    text = "اختر الأدلة المادية التي تتعلق بها هذه الملاحظة لعرضها مثبتة على لوحة الفلين:",
                                    color = TextSecondary,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                FlowRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    evidenceList.forEach { ev ->
                                        val isSelected = selectedEvidenceIds.contains(ev.id)
                                        Surface(
                                            color = if (isSelected) AmberGold else Color(0xFF26201D),
                                            shape = RoundedCornerShape(8.dp),
                                            border = BorderStroke(
                                                1.dp,
                                                if (isSelected) AmberGold else Color(0xFF4A3E37)
                                            ),
                                            modifier = Modifier
                                                .clickable {
                                                    if (isSelected) selectedEvidenceIds.remove(ev.id)
                                                    else selectedEvidenceIds.add(ev.id)
                                                }
                                                .testTag("link_evidence_chip_${ev.id}")
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                if (isSelected) {
                                                    Icon(
                                                        imageVector = Icons.Default.Check,
                                                        contentDescription = null,
                                                        tint = Color.Black,
                                                        modifier = Modifier.size(13.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                }
                                                Text(
                                                    text = when (ev.id) {
                                                        "ev_01_clock" -> "⏱️"
                                                        "ev_01_code_m01" -> "📜"
                                                        "ev_01_phone" -> "📞"
                                                        "ev_02_photo" -> "📷"
                                                        "ev_03_nile_data" -> "💾"
                                                        "ev_secret_subject01" -> "📁"
                                                        else -> "🔍"
                                                    },
                                                    fontSize = 12.sp
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = ev.title,
                                                    color = if (isSelected) Color.Black else TextPrimary,
                                                    fontSize = 11.sp,
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Note Type Selector (Contradiction, Suspicion, Timeline, Psychology, Hypothesis)
                    item {
                        Column {
                            Text(
                                text = "محور الملاحظة ونوع الاستنتاج:",
                                color = TextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                items(noteTypes) { (type, emoji) ->
                                    val isChosen = selectedNoteType == type
                                    Surface(
                                        color = if (isChosen) CrimsonThread else NoirDarkCard,
                                        shape = RoundedCornerShape(8.dp),
                                        border = BorderStroke(
                                            1.dp,
                                            if (isChosen) CrimsonGlow else Color(0xFF453B34)
                                        ),
                                        modifier = Modifier
                                            .clickable { selectedNoteType = type }
                                            .testTag("note_type_${type}")
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(text = emoji, fontSize = 12.sp)
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = type,
                                                color = if (isChosen) Color.White else TextSecondary,
                                                fontSize = 11.sp,
                                                fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Quick Prompt / Thought Chips
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF221B18)),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, Color(0xFF382F2A))
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Lightbulb,
                                        contentDescription = null,
                                        tint = AmberGold,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "خواطر جاهزة للمحقق (انقر للإدراج السريع):",
                                        color = AmberGold,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    quickThoughts.forEach { thought ->
                                        Surface(
                                            color = NoirSurfaceHighlight,
                                            shape = RoundedCornerShape(6.dp),
                                            border = BorderStroke(1.dp, Color(0xFF4A4038)),
                                            modifier = Modifier.clickable {
                                                if (noteContent.isBlank()) {
                                                    noteContent = thought
                                                } else {
                                                    noteContent = "$noteContent\n\n$thought"
                                                }
                                            }
                                        ) {
                                            Text(
                                                text = "➕ $thought",
                                                color = TextPrimary,
                                                fontSize = 10.sp,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                lineHeight = 14.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Title & Content Text Fields
                    item {
                        Column {
                            OutlinedTextField(
                                value = noteTitle,
                                onValueChange = { noteTitle = it },
                                label = { Text("عنوان الملاحظة الجنائية", fontSize = 11.sp, color = TextMuted) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("interrogation_note_title_input"),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = AmberGold,
                                    unfocusedBorderColor = Color(0xFF4B3F38),
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                )
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = noteContent,
                                onValueChange = { noteContent = it },
                                label = {
                                    Text(
                                        "اكتب استنتاجك وتفاصيل ملاحظتك بالقلم الحبر 🖋️...",
                                        fontSize = 11.sp,
                                        color = TextMuted
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(115.dp)
                                    .testTag("interrogation_note_content_input"),
                                maxLines = 6,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = AmberGold,
                                    unfocusedBorderColor = Color(0xFF4B3F38),
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Action Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, TextMuted)
                    ) {
                        Text(text = "إلغاء", color = TextSecondary, fontSize = 12.sp)
                    }

                    Button(
                        onClick = {
                            if (noteTitle.isNotBlank() && noteContent.isNotBlank()) {
                                val selectedTitles = evidenceList
                                    .filter { selectedEvidenceIds.contains(it.id) }
                                    .map { it.title }

                                val cat = when (selectedNoteType) {
                                    "تناقض في الإفادة" -> NotebookCategory.CONTRADICTIONS
                                    "شبهة جنائية مباشرة" -> NotebookCategory.CLUES
                                    "تطابق زمني مع الأحراز" -> NotebookCategory.CLUES
                                    else -> NotebookCategory.INTERROGATION
                                }

                                onSaveNote(
                                    noteTitle.trim(),
                                    noteContent.trim(),
                                    cat,
                                    selectedEvidenceIds.toList(),
                                    selectedTitles,
                                    selectedSuspect?.id,
                                    selectedSuspect?.name,
                                    selectedNoteType
                                )
                                onDismiss()
                            }
                        },
                        enabled = noteTitle.isNotBlank() && noteContent.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = AmberGold),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(2f)
                            .testTag("save_interrogation_note_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Create,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "تثبيت الملاحظة وربطها باللوحة 📌🖋️",
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

private fun generateQuickThoughts(suspectName: String?, noteType: String): List<String> {
    val name = suspectName ?: "المشتبه به"
    return when (noteType) {
        "تناقض في الإفادة" -> listOf(
            "أقوال $name تتعارض مباشرة مع توقيت الحادث المسجل في الأحراز.",
            "أنكر $name معرفته بالوثيقة لكن إفادته السابقة تؤكد وجوده بموقع الاستلام.",
            "تغيرت ملامح $name وارتبك صوته عند مواجهته بالساعة السويسرية المتوقفة."
        )
        "شبهة جنائية مباشرة" -> listOf(
            "وجود صلة وثيقة بين $name وشبكة تهريب الذهب المنظمة.",
            "مكالمة الفجر 02:13 صادرة من كابينة هاتف قريبة من سكن $name.",
            "البصمات الكيميائية الخفية تطابق المادة المكتشفة في معمل التحاليل."
        )
        "تطابق زمني مع الأحراز" -> listOf(
            "التوقيت 11:47 يتطابق مع لحظة انقطاع التيار الكهربائي في عمارة الإيموبيليا.",
            "تزامن وصول الرسالة المشفرة M-01 مع رصد تردد اللاسلكي 104.2.",
            "ساعة الجيب المتوقفة تثبت أن الحادث وقع قبل وصول دورية الشرطة بربع ساعة."
        )
        "سلوك ولغة جسد متوترة" -> listOf(
            "تجنب النظر في عيني المحقق وسارع لتغيير مجرى الحديث عند ذكر ملف خوادم NILE.",
            "تردد طويل قبل الإجابة عن مصدر الأموال المودعة في بنك باركليز.",
            "علامات ولاء مزدوج وخوف شديد من انتقام قادة شبكة المرآة."
        )
        else -> listOf(
            "هناك حلقة مفقودة تربط هذه الشهادة بمسرح الجريمة الرئيسي.",
            "يجب إعادة فحص هذه النقطة في معمل الأدلة الجنائية للتأكد من دقتها.",
            "استنتاج: هذا الخيط يقود مباشرة إلى الرأس المدبر في قصر الدوبارة."
        )
    }
}
