package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * كيان قاعدة بيانات Room لتخزين ملفات القضايا المحلولة (الأرشيف الجنائي الدائم)
 * متضمناً تفاصيل المتهمين المدانين والأدلة الحاسمة التي أدت للإدانة لتكون مرجعاً تاريخياً للمحقق.
 */
@Entity(tableName = "solved_case_archive")
data class SolvedCaseArchiveEntity(
    @PrimaryKey val caseId: Int,
    val caseTitle: String,
    val caseSubtitle: String,
    val chapter: Int,
    val crimeDate: String,                // تاريخ الجريمة (مثال: ١٤ أكتوبر ١٩٤٨)
    val location: String,                 // مسرح الجريمة
    val culpritName: String,              // اسم المتهم المدان
    val culpritRole: String,              // صفة أو دور المتهم
    val culpritMotive: String,            // الدافع الجنائي وراء الجريمة
    val culpritConfession: String,        // نص اعتراف المتهم المدون بمحضر النيابة
    val sentenceVerdict: String,          // الحكم القضائي الصادر
    val leadingEvidenceSummary: String,   // الأدلة الحاسمة التي حسمت الإدانة
    val forensicProofDetails: String,     // التقرير الفني والتحليل الجنائي القاطع
    val deductionSummary: String,         // خلاصة استنتاج المحقق
    val officialStampCode: String,        // كود الختم الجنائي الرسمي (مثال: ج-١٩٤٨/01-مُغلق)
    val solvedTimestamp: Long = System.currentTimeMillis(),
    val ratingStars: Int = 5,
    val justiceScoreEarned: Int = 25,
    val investigatorNotes: String = ""    // هوامش مأمور الضبط القضائي
)
