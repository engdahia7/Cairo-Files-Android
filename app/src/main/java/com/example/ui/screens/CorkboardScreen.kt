package com.example.ui.screens

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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BoardLinkEntity
import com.example.data.model.EvidenceEntity
import com.example.data.model.SuspectEntity
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

data class BoardNode(
    val id: String,
    val label: String,
    val type: String, // "مشتبه به" or "دليل مادي"
    val isPerson: Boolean,
    val iconEmoji: String
)

@Composable
fun CorkboardScreen(
    suspects: List<SuspectEntity>,
    evidenceList: List<EvidenceEntity>,
    boardLinks: List<BoardLinkEntity>,
    selectedSourceId: String?,
    selectedTargetId: String?,
    validationMessage: String?,
    onSelectNode: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Build all available nodes on the corkboard
    val nodes = listOf(
        BoardNode("mohamed", "محمد عبده (المحقق)", "محقق", true, "🕵️"),
        BoardNode("ev_01_clock", "الساعة 11:47", "دليل", false, "⏰"),
        BoardNode("noura", "نورا حمدي", "شاكية", true, "👩"),
        BoardNode("ev_01_code_m01", "رمز M-01 السري", "وثيقة", false, "📄"),
        BoardNode("youssef", "يوسف كامل (المختفي)", "صحفي", true, "🎙️"),
        BoardNode("ev_02_photo", "صورة الحادث الأخيرة", "صورة", false, "📷"),
        BoardNode("marwan", "مروان صبري", "خبير تقني", true, "💻"),
        BoardNode("ev_03_nile_data", "أرشيف NILE DATA", "سجل", false, "🏢"),
        BoardNode("omar", "الرائد عمر الديب", "شرطة", true, "👮"),
        BoardNode("ev_secret_subject01", "وثيقة SUBJECT 01", "سري جداً", false, "👁️")
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NoirObsidian)
            .padding(16.dp)
    ) {
        // Board Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "لوحة التحقيق والخيوط الحمراء",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "📌", fontSize = 16.sp)
                }
                Text(
                    text = "اربط الأدلة بالأشخاص واختبر الفرضيات لكشف الخلايا",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }

            Surface(
                color = CrimsonThread.copy(alpha = 0.2f),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, CrimsonThread)
            ) {
                Text(
                    text = "${boardLinks.size} خيوط نشطة",
                    color = CrimsonGlow,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Deduction Helper Instructions Box
        Surface(
            color = NoirDarkCard,
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, Color(0xFF2C3040)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Help,
                    contentDescription = "كيفية الربط",
                    tint = AmberGold,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = if (selectedSourceId == null)
                            "اضغط على بطاقة لاختيار العنصر الأول، ثم اضغط على بطاقة أخرى لمد خيط أحمر واختبار الفرضية."
                        else
                            "العنصر المحدد: ${nodes.find { it.id == selectedSourceId }?.label} — اضغط على العنصر الثاني للربط!",
                        color = if (selectedSourceId == null) TextSecondary else AmberGold,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        if (validationMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                color = if (validationMessage.contains("تأكيد")) EmpathyColor.copy(alpha = 0.15f)
                else CrimsonThread.copy(alpha = 0.15f),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(
                    1.dp,
                    if (validationMessage.contains("تأكيد")) EmpathyColor else CrimsonThread
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (validationMessage.contains("تأكيد")) Icons.Default.CheckCircle else Icons.Default.Help,
                        contentDescription = null,
                        tint = if (validationMessage.contains("تأكيد")) EmpathyColor else CrimsonGlow,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = validationMessage,
                        color = if (validationMessage.contains("تأكيد")) EmpathyColor else CrimsonGlow,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Horizontal Corkboard Pins (Interactive Nodes)
        Text(
            text = "بطاقات لوحة التحقيق (الأشخاص والأدلة):",
            color = TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(nodes, key = { it.id }) { node ->
                val isSelected = selectedSourceId == node.id || selectedTargetId == node.id
                CorkboardPinCard(
                    node = node,
                    isSelected = isSelected,
                    onClick = { onSelectNode(node.id) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Confirmed Red Threads List
        Text(
            text = "الخيوط المحققة والروابط المؤكدة (مسار كشف المرآة):",
            color = TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(boardLinks, key = { it.id }) { link ->
                val sourceLabel = nodes.find { it.id == link.sourceId }?.label ?: link.sourceId
                val targetLabel = nodes.find { it.id == link.targetId }?.label ?: link.targetId

                Card(
                    colors = CardDefaults.cardColors(containerColor = NoirDarkCard),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(
                        1.dp,
                        if (link.isValidConnection) CrimsonThread else Color(0xFF2E3242)
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("board_link_${link.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(
                                    if (link.isValidConnection) CrimsonGlow else TextMuted,
                                    CircleShape
                                )
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = sourceLabel,
                                    color = AmberGold,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "  ⟵ خيط أحمر ⟶  ",
                                    color = CrimsonThread,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = targetLabel,
                                    color = CyanTerminal,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = link.linkDescription,
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CorkboardPinCard(
    node: BoardNode,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(130.dp)
            .height(140.dp)
            .clickable { onClick() }
            .testTag("pin_${node.id}"),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF33271D) else NoirDarkCard
        ),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            if (isSelected) 2.dp else 1.dp,
            if (isSelected) AmberGold else Color(0xFF33384A)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Pin marker
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .background(if (node.isPerson) AmberGold else CrimsonThread, CircleShape)
            )

            Text(
                text = node.iconEmoji,
                fontSize = 30.sp
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = node.label,
                    color = if (isSelected) AmberGold else TextPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2
                )
                Text(
                    text = node.type,
                    color = TextMuted,
                    fontSize = 9.sp
                )
            }
        }
    }
}
