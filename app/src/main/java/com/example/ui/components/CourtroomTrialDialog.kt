package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.CaseEntity
import com.example.data.model.EvidenceEntity
import com.example.data.model.iconEmoji
import com.example.ui.theme.*

@Composable
fun CourtroomTrialDialog(
    caseEntity: CaseEntity,
    evidenceList: List<EvidenceEntity>,
    onWinTrial: () -> Unit,
    onLoseTrial: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedEvidence by remember { mutableStateOf<EvidenceEntity?>(null) }
    var trialStep by remember { mutableStateOf(1) }
    var trialOutcomeMessage by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .testTag("courtroom_trial_dialog"),
            colors = CardDefaults.cardColors(containerColor = NoirObsidian),
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(2.dp, AmberGold)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Balance, contentDescription = null, tint = AmberGold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "محكمة الجنايات - محاكمة القضية #${caseEntity.id}",
                            color = AmberGold,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "إغلاق", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = Color(0xFF2B3245))
                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = NoirDarkCard),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, CyanTerminal.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(text = "القاضي الجنائي رئيس الدائرة:", color = CyanTerminal, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = when (trialStep) {
                                1 -> "الدفاع يدعي أن موكله كان في مكان آخر وقت ارتكاب الجريمة في ${caseEntity.location}. قدم الدليل القاطع لدحض هذه الكذبة!"
                                2 -> "محامي المتهم يطعن في قانونية القرائن المرفوعة. اختر القرينة المطابقة لتثبت الاتهام."
                                else -> "المرافعة النهائية: هل تتمسك بقرار إدانة المتهم بناءً على ملف التحقيق الكامل؟"
                            },
                            color = Color.White,
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "اختر قرينة من أدلة التحقيق لتقديمها للقاضي:", color = TextSecondary, fontSize = 11.sp)
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(evidenceList.filter { it.isDiscovered }) { ev ->
                        val isSelected = selectedEvidence?.id == ev.id
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) Color(0xFF2E2415) else NoirDarkCard
                            ),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.5.dp, if (isSelected) AmberGold else Color(0xFF2B3245)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedEvidence = ev }
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(text = "${ev.iconEmoji} ${ev.title}", color = AmberGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(text = ev.description, color = TextSecondary, fontSize = 11.sp, maxLines = 2)
                            }
                        }
                    }
                }

                if (trialOutcomeMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = trialOutcomeMessage!!, color = CrimsonGlow, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {
                        if (selectedEvidence != null) {
                            if (trialStep < 2) {
                                trialStep++
                                trialOutcomeMessage = "⚖️ قبل القاضي الدليل ومحامي المتهم في مأزق! انتقل للتحدي التالي."
                                selectedEvidence = null
                            } else {
                                onWinTrial()
                            }
                        } else {
                            trialOutcomeMessage = "⚠️ يجب اختيار قرينة قانونية من القائمة قبل تقديمها للمحكمة!"
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AmberGold),
                    modifier = Modifier.fillMaxWidth().height(44.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    val btnText = if (trialStep < 2) "تقديم الدليل للمحكمة 🏛️" else "النطق بالحكم النهائي والإدانة 🔨"
                    Text(text = btnText, color = NoirObsidian, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
