package com.example.ui.screens

import com.example.R

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Schedule
import com.example.data.model.EncounterEntity
import com.example.data.model.TimeOfDay
import com.example.data.model.WeatherCondition
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

data class CairoDistrict(
    val id: String,
    val name: String,
    val description: String,
    val landmark: String,
    val atmosphereColor: Color
)

@Composable
fun CairoMapScreen(
    encounters: List<EncounterEntity>,
    onVisitEncounter: (EncounterEntity) -> Unit,
    currentTime: TimeOfDay = TimeOfDay.MIDNIGHT,
    currentWeather: WeatherCondition = WeatherCondition.FOGGY_RAIN,
    onCycleTime: () -> Unit = {},
    onCycleWeather: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedDistrictId by remember { mutableStateOf("downtown") }

    val districts = listOf(
        CairoDistrict("downtown", "وسط البلد", "عمارة اللواء والشوارع الخديوية", "ميدان طلعت حرب", AmberGold),
        CairoDistrict("zamalek", "الزمالك", "أزقة هادئة وسفارات وأرشيفات قديمة", "محل كيميا التحميض", CyanTerminal),
        CairoDistrict("sayeda", "السيدة زينب", "حارة الحناوي ومقاهي آخر الليل", "مقهى عم صابر", EmpathyColor),
        CairoDistrict("abbassiya", "العباسية", "عمارات أوقاف قديمة ومقرات مهجورة", "شقة الدور السابع", CrimsonGlow),
        CairoDistrict("nile", "كورنيش النيل", "مياه باردة ومراكب ومسارح جرائم غامضة", "مرسى زينهم والمعادي", Color(0xFF60A5FA))
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NoirObsidian)
            .padding(16.dp)
    ) {
        // Map Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Explore,
                        contentDescription = "خريطة القاهرة",
                        tint = AmberGold,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "خريطة أحياء القاهرة",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "تحرك عبر أحياء العاصمة لمقابلة الشهود وكشف الخيوط",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }

            // Interactive Dynamic Time & Weather Toggles
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Surface(
                    color = NoirSurfaceHighlight,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, AmberGold.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .clickable { onCycleTime() }
                        .testTag("cycle_time_btn")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = currentTime.iconEmoji, fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = currentTime.label.substringBefore(" ("),
                            color = AmberGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Surface(
                    color = NoirSurfaceHighlight,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, CyanTerminal.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .clickable { onCycleWeather() }
                        .testTag("cycle_weather_btn")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = currentWeather.iconEmoji, fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = currentWeather.label.substringBefore(" "),
                            color = CyanTerminal,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Dynamic Environmental Status Card
        Surface(
            color = NoirDarkCard,
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color(0xFF282E40)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${currentTime.iconEmoji} ${currentTime.label} | ${currentWeather.iconEmoji} ${currentWeather.label}",
                    color = TextPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = currentWeather.effectNote,
                    color = TextMuted,
                    fontSize = 10.sp,
                    maxLines = 1
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Atmospheric Cairo Location Banner
        val locationDrawable = when (selectedDistrictId) {
            "zamalek" -> R.drawable.img_loc_zamalek
            "downtown" -> R.drawable.img_cairo_crime_scene
            else -> R.drawable.img_office_hub
        }
        val currentDistrict = districts.find { it.id == selectedDistrictId } ?: districts.first()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, currentDistrict.atmosphereColor.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
        ) {
            Image(
                painter = painterResource(id = locationDrawable),
                contentDescription = currentDistrict.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        androidx.compose.ui.graphics.Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                NoirObsidian.copy(alpha = 0.85f)
                            )
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = currentDistrict.atmosphereColor,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = currentDistrict.name,
                            color = Color.Black,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = currentDistrict.landmark,
                        color = AmberGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Text(
                    text = currentDistrict.description,
                    color = TextPrimary,
                    fontSize = 11.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Districts Carousel
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            districts.take(3).forEach { dist ->
                val isSelected = selectedDistrictId == dist.id
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedDistrictId = dist.id }
                        .testTag("district_btn_${dist.id}"),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) NoirSurfaceHighlight else NoirDarkCard
                    ),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(
                        1.dp,
                        if (isSelected) dist.atmosphereColor else Color(0xFF2C3142)
                    )
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = dist.name,
                            color = if (isSelected) dist.atmosphereColor else TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = dist.landmark,
                            color = TextMuted,
                            fontSize = 10.sp,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            districts.drop(3).forEach { dist ->
                val isSelected = selectedDistrictId == dist.id
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedDistrictId = dist.id }
                        .testTag("district_btn_${dist.id}"),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) NoirSurfaceHighlight else NoirDarkCard
                    ),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(
                        1.dp,
                        if (isSelected) dist.atmosphereColor else Color(0xFF2C3142)
                    )
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = dist.name,
                            color = if (isSelected) dist.atmosphereColor else TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = dist.landmark,
                            color = TextMuted,
                            fontSize = 10.sp,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "اللقاءات الحية في شوارع العاصمة:",
            color = TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Encounters List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(encounters, key = { it.id }) { enc ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = NoirDarkCard),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(
                        1.dp,
                        if (enc.isVisited) EmpathyColor.copy(alpha = 0.5f) else Color(0xFF33384A)
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("encounter_${enc.id}")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                val encAvatarRes = when {
                                    enc.characterName.contains("صابر") -> R.drawable.img_saber_avatar
                                    enc.characterName.contains("نورا") -> R.drawable.img_noura_avatar
                                    enc.characterName.contains("مروان") -> R.drawable.img_marwan_avatar
                                    enc.characterName.contains("يوسف") -> R.drawable.img_journalist_avatar
                                    else -> null
                                }

                                if (encAvatarRes != null) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .border(
                                                1.5.dp,
                                                if (enc.isVisited) EmpathyColor else AmberGold,
                                                CircleShape
                                            )
                                    ) {
                                        Image(
                                            painter = painterResource(id = encAvatarRes),
                                            contentDescription = enc.characterName,
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .background(
                                                if (enc.isVisited) EmpathyColor.copy(alpha = 0.2f)
                                                else AmberGold.copy(alpha = 0.2f),
                                                CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.LocationOn,
                                            contentDescription = null,
                                            tint = if (enc.isVisited) EmpathyColor else AmberGold,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = enc.characterName,
                                        color = TextPrimary,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${enc.district} • ${enc.characterRole}",
                                        color = TextMuted,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            if (enc.isVisited) {
                                Surface(
                                    color = EmpathyColor.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "تم اللقاء ✓",
                                        color = EmpathyColor,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Dialogue Bubble
                        Surface(
                            color = NoirSurface,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "“${enc.dialogueText}”",
                                    color = TextPrimary,
                                    fontSize = 12.sp,
                                    lineHeight = 17.sp
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Lightbulb,
                                        contentDescription = "الخيط السري",
                                        tint = AmberGold,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "الخيط المكتشف: ${enc.hiddenClueUnlocked}",
                                        color = AmberGold,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }

                        if (!enc.isVisited) {
                            Spacer(modifier = Modifier.height(10.dp))

                            Button(
                                onClick = { onVisitEncounter(enc) },
                                colors = ButtonDefaults.buttonColors(containerColor = AmberGold),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("visit_encounter_btn_${enc.id}")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Navigation,
                                    contentDescription = "الذهاب للموقع",
                                    tint = Color.Black,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "التوجه إلى الموقع والتحدث معه",
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
    }
}
