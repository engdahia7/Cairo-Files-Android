package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.NotebookCategory
import com.example.data.model.NotebookEntry
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
fun DetectiveNotebookDialog(
    entries: List<NotebookEntry>,
    selectedCategory: NotebookCategory,
    onSelectCategory: (NotebookCategory) -> Unit,
    onAddCustomNote: (String, String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isWritingNewNote by remember { mutableStateOf(false) }
    var noteTitleInput by remember { mutableStateOf("") }
    var noteContentInput by remember { mutableStateOf("") }

    val filteredEntries = remember(entries, selectedCategory) {
        if (selectedCategory == NotebookCategory.ALL) entries
        else entries.filter { it.category == selectedCategory }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(12.dp)
                .testTag("detective_notebook_dialog"),
            shape = RoundedCornerShape(18.dp),
            color = Color(0xFF1A1412), // جلد بني غامق عتيق 1948
            border = BorderStroke(2.dp, AmberGold.copy(alpha = 0.8f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header (Notebook Title & Close)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = AmberGold.copy(alpha = 0.2f),
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.MenuBook,
                                    contentDescription = null,
                                    tint = AmberGold,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "دفتر ملاحظات المحقق عبده",
                                color = AmberGold,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "مذكرات الميدان - مصلحة الأمن العام ١٩٤٨",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_notebook_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "إغلاق الدفتر",
                            tint = TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Action Bar: Category Pills + Write Note Button
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    contentPadding = PaddingValues(bottom = 8.dp)
                ) {
                    items(NotebookCategory.values()) { category ->
                        val isSelected = category == selectedCategory
                        Surface(
                            color = if (isSelected) AmberGold else NoirDarkCard,
                            shape = RoundedCornerShape(20.dp),
                            border = BorderStroke(1.dp, if (isSelected) AmberGold else Color(0xFF423B36)),
                            modifier = Modifier
                                .clickable { onSelectCategory(category) }
                                .testTag("notebook_tab_${category.name}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = category.iconEmoji, fontSize = 13.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = category.label,
                                    color = if (isSelected) Color.Black else TextSecondary,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                // Button to toggle custom note writing
                Button(
                    onClick = { isWritingNewNote = !isWritingNewNote },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isWritingNewNote) CrimsonThread else NoirSurfaceHighlight
                    ),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, if (isWritingNewNote) CrimsonGlow else AmberGold),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("toggle_new_note_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Create,
                        contentDescription = null,
                        tint = if (isWritingNewNote) Color.White else AmberGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isWritingNewNote) "إلغاء تدوين الخاطرة" else "تدوين ملاحظة ميدانية جديدة بالقلم الحبر 🖋️",
                        color = if (isWritingNewNote) Color.White else AmberGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // New note input panel
                AnimatedVisibility(visible = isWritingNewNote) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .background(Color(0xFF241D1A), RoundedCornerShape(10.dp))
                            .border(1.dp, AmberGold.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        OutlinedTextField(
                            value = noteTitleInput,
                            onValueChange = { noteTitleInput = it },
                            label = { Text("عنوان الملاحظة", fontSize = 11.sp, color = TextMuted) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("notebook_title_input"),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AmberGold,
                                unfocusedBorderColor = Color(0xFF4D4039),
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = noteContentInput,
                            onValueChange = { noteContentInput = it },
                            label = { Text("نص الملاحظة أو الاستنتاج الشخصي...", fontSize = 11.sp, color = TextMuted) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(85.dp)
                                .testTag("notebook_content_input"),
                            maxLines = 4,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AmberGold,
                                unfocusedBorderColor = Color(0xFF4D4039),
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                if (noteTitleInput.isNotBlank() && noteContentInput.isNotBlank()) {
                                    onAddCustomNote(noteTitleInput.trim(), noteContentInput.trim())
                                    noteTitleInput = ""
                                    noteContentInput = ""
                                    isWritingNewNote = false
                                }
                            },
                            enabled = noteTitleInput.isNotBlank() && noteContentInput.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(containerColor = AmberGold),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier
                                .align(Alignment.End)
                                .testTag("save_notebook_entry_btn")
                        ) {
                            Text(
                                text = "حفظ بالقلم الحبر 🖋️",
                                color = Color.Black,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Notes List (Paper aesthetic)
                if (filteredEntries.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "لا توجد مذكرات مقيدة في هذا القسم بعد.",
                            color = TextMuted,
                            fontSize = 13.sp
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(filteredEntries, key = { it.id }) { entry ->
                            NotebookEntryCard(entry = entry)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NotebookEntryCard(entry: NotebookEntry) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("notebook_entry_${entry.id}"),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF26201D)),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(
            1.dp,
            when (entry.category) {
                NotebookCategory.CLUES -> AmberGold.copy(alpha = 0.6f)
                NotebookCategory.CONTRADICTIONS -> CrimsonGlow.copy(alpha = 0.6f)
                NotebookCategory.FORENSIC -> CyanTerminal.copy(alpha = 0.6f)
                NotebookCategory.PERSONAL -> EmpathyColor.copy(alpha = 0.6f)
                else -> Color(0xFF4A3E37)
            }
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = when (entry.category) {
                            NotebookCategory.CLUES -> AmberGold.copy(alpha = 0.2f)
                            NotebookCategory.CONTRADICTIONS -> CrimsonThread.copy(alpha = 0.2f)
                            NotebookCategory.FORENSIC -> CyanTerminal.copy(alpha = 0.2f)
                            NotebookCategory.PERSONAL -> EmpathyColor.copy(alpha = 0.2f)
                            else -> NoirSurfaceHighlight
                        },
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = entry.category.iconEmoji, fontSize = 10.sp)
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = entry.category.label,
                                color = when (entry.category) {
                                    NotebookCategory.CLUES -> AmberGold
                                    NotebookCategory.CONTRADICTIONS -> CrimsonGlow
                                    NotebookCategory.FORENSIC -> CyanTerminal
                                    NotebookCategory.PERSONAL -> EmpathyColor
                                    else -> TextSecondary
                                },
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    if (entry.relatedCaseId != null) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "قضية #${entry.relatedCaseId}",
                            color = TextMuted,
                            fontSize = 10.sp
                        )
                    }
                }

                Text(
                    text = entry.timestamp,
                    color = TextMuted,
                    fontSize = 10.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = entry.title,
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))

            // Badges for linked suspect and linked corkboard evidence
            if (entry.linkedSuspectName != null || entry.linkedEvidenceTitles.isNotEmpty() || entry.noteTypeTag != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (entry.noteTypeTag != null) {
                        Surface(
                            color = AmberGold.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(4.dp),
                            border = BorderStroke(1.dp, AmberGold.copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = entry.noteTypeTag,
                                color = AmberGold,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    if (entry.linkedSuspectName != null) {
                        Surface(
                            color = CrimsonThread.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(4.dp),
                            border = BorderStroke(1.dp, CrimsonGlow.copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = "🎙️ ${entry.linkedSuspectName}",
                                color = CrimsonGlow,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    if (entry.linkedEvidenceTitles.isNotEmpty()) {
                        Surface(
                            color = CyanTerminal.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(4.dp),
                            border = BorderStroke(1.dp, CyanTerminal.copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = "📌 ${entry.linkedEvidenceTitles.joinToString("، ")}",
                                color = CyanTerminal,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
            }

            Text(
                text = entry.content,
                color = TextSecondary,
                fontSize = 12.sp,
                lineHeight = 17.sp
            )
        }
    }
}
