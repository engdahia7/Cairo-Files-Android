package com.example.ui.components

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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Lightbulb
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
import com.example.data.model.CaseEntity
import com.example.data.model.CaseStatus
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

data class DeductionHypothesis(
    val id: Int,
    val title: String,
    val explanation: String,
    val isCorrect: Boolean,
    val feedback: String
)

@Composable
fun CaseDeductionDialog(
    caseEntity: CaseEntity,
    onSolveSuccess: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedHypothesisId by remember { mutableStateOf<Int?>(null) }
    var resultMessage by remember { mutableStateOf<String?>(null) }
    var isSolvedCorrectly by remember { mutableStateOf(false) }

    // Dynamic hypotheses tailored to case
    val hypotheses = when (caseEntity.id) {
        1 -> listOf(
            DeductionHypothesis(
                id = 1,
                title = "فرضية أ: شقيقها هرب من الباب السري بعد أن ترك الساعة عمداً كشفرة توقيت",
                explanation = "عقارب الساعة المتوقفة عند 11:47 تماثل توقيت إرسال الرسالة المشفرة، والباب السري مفتوح من الداخل.",
                isCorrect = true,
                feedback = "استنتاج جنائي عبقري! الساعة كانت وسيلة تضليل وموعداً سرياً للهروب نحو وسط البلد."
            ),
            DeductionHypothesis(
                id = 2,
                title = "فرضية ب: نورا لفقّت غيابه للاستيلاء على أوراق العمارة",
                explanation = "نورا تبدو متوترة وتكرر نفس الكلمات أثناء الاستجواب.",
                isCorrect = false,
                feedback = "فرضية خاطئة. الأدلة الجنائية وتقرير المعاينة يؤكدان أن نورا تلقت تهديداً حقيقياً."
            ),
            DeductionHypothesis(
                id = 3,
                title = "فرضية ج: اقتحام خارجي بالقوة وسرقة الخزنة",
                explanation = "الأبواب والنوافذ سليمة تماماً ولا توجد علامات عنف.",
                isCorrect = false,
                feedback = "غير منطقي! أقفال الأبواب سليمة، لا يمكن أن يكون اقتحاماً خارجياً عنيفاً."
            )
        )
        2 -> listOf(
            DeductionHypothesis(
                id = 1,
                title = "فرضية أ: سيارة نايل داتا تعمدت صدم الصحفي لإتلاف شريحة التسجيلات",
                explanation = "الصدمة استهدفت الجانب الأيمن حيث حقيبة الأجهزة الخاصة بيوسف.",
                isCorrect = true,
                feedback = "صحيح تماماً! الحادث كان مدبراً لطمس التحقيق الصحفي حول شبكة المرآة."
            ),
            DeductionHypothesis(
                id = 2,
                title = "فرضية ب: حادث سير عادي بسبب الأمطار",
                explanation = "الأسفلت كان مبللاً أثناء الليل.",
                isCorrect = false,
                feedback = "خاطئ. تقرير المرور يثبت عدم وجود آثار فرملة على الإطلاق مما يدل على التعمد."
            )
        )
        3 -> listOf(
            DeductionHypothesis(
                id = 1,
                title = "فرضية أ: نائب رئيس الشركة سلم الوثيقة تحت وطأة ابتزاز تسجيلات المرآة",
                explanation = "استخدم حساب الموظف المتوفى لفتح الخزنة 03:14 فجراً ونقل البيانات لأرشيف رشدي.",
                isCorrect = true,
                feedback = "استنتاج دقيق لا تشوبه شائبة! الوثيقة سُرقت بابتزاز منظم وليس بدافع مالي مباشر."
            ),
            DeductionHypothesis(
                id = 2,
                title = "فرضية ب: هجوم سيبراني خارجي من قراصنة هواة",
                explanation = "لا يوجد أثر فيزيائي لفتح الخزنة السويسرية.",
                isCorrect = false,
                feedback = "مردود. الخزنة فُتحت بمفاتيح وبصمات رقمية صحيحة من الداخل."
            )
        )
        4 -> listOf(
            DeductionHypothesis(
                id = 1,
                title = "فرضية أ: الخاطفون استهدفوا إجبار رجل الأعمال على التنازل عن أسهم الاتصالات لصالح المرآة",
                explanation = "شهادة عم سيد وتحديد شاحنة نايل لوجستيكس أكدت احتجازه في مستودع المنيب.",
                isCorrect = true,
                feedback = "حسمت القضية باقتدار! حماية الشاهد أنقذت حياة الضحية وكشفت ذراع الاستحواذ القسري للمنظمة."
            ),
            DeductionHypothesis(
                id = 2,
                title = "فرضية ب: طلب فدية عائلية سريعة بسبب ديون قمار",
                explanation = "لا توجد أدلة على دوافع سياسية أو تجارية كبرى.",
                isCorrect = false,
                feedback = "غير صحيح. الخاطفون لم يتصلوا بالعائلة بل طالبوا بأوراق ملكية الأسهم فقط."
            )
        )
        5 -> listOf(
            DeductionHypothesis(
                id = 1,
                title = "فرضية أ: المشتبه به تواجد فقط في باب اللوق وتم التلاعب ببث الميادين الأخرى بحقن فيديو مسبق",
                explanation = "فحص الطابع الزمني والألياف الضوئية أثبت استخدام فلاش مروان صبري لتزييف الكاميرات.",
                isCorrect = true,
                feedback = "عبقرية استثنائية! كشفت التزييف الرقمي الزمني وتتبعت الجاني الحقيقي في سوق الفلكي."
            ),
            DeductionHypothesis(
                id = 2,
                title = "فرضية ب: أربعة أفراد توائم نفذوا عملية تمويه متزامنة",
                explanation = "التسجيلات تظهر نفس الملامح بنفس الملابس في نفس اللحظة.",
                isCorrect = false,
                feedback = "مستحيل فيزيائياً وفحص كود البث أثبت التزييف الزمني المتأخر."
            )
        )
        6 -> listOf(
            DeductionHypothesis(
                id = 1,
                title = "فرضية أ: الزبون هو المحقق عادل كمال الذي اختطفته المنظمة بعد رصده لنقطة مقهى الحناوي",
                explanation = "أجندة المقهى كشفت خطوط الربط بين سيارات الرصد ولقاء يوسف كامل.",
                isCorrect = true,
                feedback = "أحسنت! فتحت هذا الباب المهم وأسست أولى خطوط شبكة اللوحة الجنائية بمكتبك."
            ),
            DeductionHypothesis(
                id = 2,
                title = "فرضية ب: زبون عادي هرب من ديون المقهى",
                explanation = "الأجندة مجرد دفتر حسابات تجارية شخصية.",
                isCorrect = false,
                feedback = "سطحي جداً.. الأجندة تحتوي على شفرات استخباراتية وأرقام سيارات ومواعيد سرية."
            )
        )
        7 -> listOf(
            DeductionHypothesis(
                id = 1,
                title = "فرضية أ: شقة الدور السابع وكر تجسس على مكتب محمد عبده لإدارة سلوكه والتنبؤ بقراراته",
                explanation = "الكاميرات موجهة لمكتبك والملف النفسي يثبت رصدهم لتحركاتك قبل استقالتك من الشرطة.",
                isCorrect = true,
                feedback = "كشف مرعب للحقيقة! أدركت أنك لست مجرد محقق خارجي، بل كنت هدفاً مرصوداً ومحسوباً للمرآة!"
            ),
            DeductionHypothesis(
                id = 2,
                title = "فرضية ب: مكتب لشركة دعاية وإعلان تدرس جمهور وسط البلد",
                explanation = "الشاشات لمجرد مسح الشوارع التجارية العامة.",
                isCorrect = false,
                feedback = "مرفوض! الملف يحمل اسمك الشخصي وصورك العائلية وأدق أسرار قضاياك السابقة."
            )
        )
        8 -> listOf(
            DeductionHypothesis(
                id = 1,
                title = "فرضية أ: يوسف كامل ترك مفتاح خزانة أمانات رمسيس 104 ليوصل إليك أصل ملف شبكة المرآة",
                explanation = "الطرد المؤجل بـ 90 يوماً كان خطة أمان مسبقة وضعها يوسف في حال تعرضه للموت.",
                isCorrect = true,
                feedback = "استنتاج لا يقبل الشك! خطة يوسف الاحتياطية نجحت في تسليمك المفتاح الحاسم."
            ),
            DeductionHypothesis(
                id = 2,
                title = "فرضية ب: طرد مزور أرسلته المنظمة للإيقاع بك في كمين بمحطة رمسيس",
                explanation = "الخط قد يكون مقلداً والهدف استدراجك.",
                isCorrect = false,
                feedback = "فحص الخط ونبرة الصوت على الشريط أكدا بنسبة 100% أنه يوسف كامل صديقك المقرب."
            )
        )
        9 -> listOf(
            DeductionHypothesis(
                id = 1,
                title = "فرضية أ: مبرمج شركة نايل داتا اغتيل بحقنة شلل عضلي لإسكاته بعد تسريب قاعدة البيانات",
                explanation = "بطاقة الدخول الممغنطة وتقرير سموم د. سلمى أثبتا اغتيالاً تقنياً احترافياً لمنع كشف المرآة.",
                isCorrect = true,
                feedback = "تحليل جنائي متقدم ورائع! ربطت الضحية بشركة نايل داتا وكشفت وحشية المنظمة في تصفية منسوبيها."
            ),
            DeductionHypothesis(
                id = 2,
                title = "فرضية ب: غرق عرضي بعد سهرة ليلية على كورنيش المعادي",
                explanation = "الجثة انتشلت من مياه النيل دون إصابات كدمية ظاهرة.",
                isCorrect = false,
                feedback = "خاطئ تماماً! الرئتان لم تحتويا على ماء النيل، وتقرير السموم أثبت الوفاة بالحقنة قبل إلقائه."
            )
        )
        10 -> listOf(
            DeductionHypothesis(
                id = 1,
                title = "فرضية أ: جميع القضايا التسع خلايا منفصلة تنبثق من منظمة 'المرآة' ويوسف يقود الحرب من الظل",
                explanation = "تطابق خيوط اللوحة والملف رقم 00 أثبت البنية الهرمية للمنظمة واتصال يوسف الهاتفي المباشر.",
                isCorrect = true,
                feedback = "إنجاز أسطوري للمحقق محمد عبده! حسمت لغز الفصل الأول بالكامل ودخلت المواجهة الكبرى!"
            ),
            DeductionHypothesis(
                id = 2,
                title = "فرضية ب: مجرد صدف متفرقة وجرائم عادية تم تضخيمها بهواجس المحقق الشخصية",
                explanation = "لا يوجد رابط عضوي مثبت قانونياً بين جميع الأطراف.",
                isCorrect = false,
                feedback = "غير مقبول على الإطلاق! جميع الوثائق والشفرات والكود M-01 وأرقام السيارات تتلاقى في كيان واحد."
            )
        )
        else -> listOf(
            DeductionHypothesis(
                id = 1,
                title = "فرضية أ: خيط مباشر يربط الضحية بأرشيف منظمة المرآة السري",
                explanation = "تطابق الأدلة المادية والأرقام المشفرة مع قاعدة بيانات نايل داتا.",
                isCorrect = true,
                feedback = "أحسنت! الفرضية تطابق ملفات الاستخبارات الجنائية وأغلقت القضية بنجاح."
            ),
            DeductionHypothesis(
                id = 2,
                title = "فرضية ب: جريمة فردية بدافع السرقة الشخصية",
                explanation = "محاولة تضليل من الجاني لتشتيت أنظار المباحث.",
                isCorrect = false,
                feedback = "الأدلة لا تدعم السرقة العشوائية لأن المقتنيات الثمينة تركت في مكانها."
            )
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("deduction_dialog"),
        colors = CardDefaults.cardColors(containerColor = NoirSurfaceHighlight),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.5.dp, if (isSolvedCorrectly) EmpathyColor else AmberGold)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = "الاستنتاج الجنائي",
                        tint = AmberGold,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "غرفة الاستنتاج الجنائي: قضية #${caseEntity.id}",
                        color = AmberGold,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Surface(
                    color = NoirDarkCard,
                    shape = CircleShape,
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { onClose() }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "إغلاق",
                            tint = TextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "ما هو استنتاجك النهائي بشأن لغز: '${caseEntity.title}'؟",
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "بناءً على المعاينة والدليل '${caseEntity.keyClueTitle}'، اختر الفرضية المطابقة:",
                color = TextSecondary,
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Hypotheses
            hypotheses.forEach { hyp ->
                val isSelected = selectedHypothesisId == hyp.id
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            selectedHypothesisId = hyp.id
                            resultMessage = hyp.feedback
                            if (hyp.isCorrect) {
                                isSolvedCorrectly = true
                            } else {
                                isSolvedCorrectly = false
                            }
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) NoirDarkCard else NoirSurface
                    ),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(
                        1.dp,
                        if (isSelected) AmberGold else Color(0xFF2B2E3D)
                    )
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = hyp.title,
                            color = if (isSelected) AmberGold else TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = hyp.explanation,
                            color = TextSecondary,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    }
                }
            }

            if (resultMessage != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    color = if (isSolvedCorrectly) EmpathyColor.copy(alpha = 0.15f) else CrimsonThread.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, if (isSolvedCorrectly) EmpathyColor else CrimsonThread),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isSolvedCorrectly) Icons.Default.CheckCircle else Icons.Default.Close,
                            contentDescription = null,
                            tint = if (isSolvedCorrectly) EmpathyColor else CrimsonGlow,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = resultMessage!!,
                            color = if (isSolvedCorrectly) EmpathyColor else CrimsonGlow,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        if (isSolvedCorrectly) {
                            onSolveSuccess()
                        }
                    },
                    enabled = isSolvedCorrectly,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AmberGold,
                        disabledContainerColor = NoirSurface
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = if (isSolvedCorrectly) "إغلاق القضية رسمياً ✓" else "اختر الفرضية الصحيحة أولاً",
                        color = if (isSolvedCorrectly) Color.Black else TextMuted,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
