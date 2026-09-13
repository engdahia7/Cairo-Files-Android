package com.example.data.model

data class NotebookEntry(
    val id: String,
    val title: String,
    val content: String,
    val timestamp: String,
    val category: NotebookEntryCategory = NotebookEntryCategory.CLUE,
    val relatedCaseId: Int? = null,
    val linkedSuspectName: String? = null,
    val linkedSuspectId: String? = null,
    val linkedEvidenceTitles: List<String> = emptyList(),
    val noteTypeTag: String? = null
)

enum class NotebookEntryCategory(val label: String, val iconEmoji: String) {
    CLUE("دليل", "🔎"),
    SUSPECT("مشتبه", "👤"),
    EVIDENCE("أحراز", "📁"),
    NOTE("ملاحظة", "📝")
}

data class ForensicExamTarget(
    val id: String,
    val caseTitle: String,
    val name: String,
    val initialDescription: String,
    val isExamined: Boolean = false
)

enum class ScenarioCategory(val label: String, val iconEmoji: String) {
    BLACKOUT_ABDUCTION("اختطاف وإظلام متعمد", "⚡"),
    MONEY_LAUNDERING("غسيل أموال", "💰"),
    STATE_COMPLICITY("تواطؤ مؤسسي", "🏛️"),
    STAGED_DISAPPEARANCE("اختفاء مدبر", "🎭")
}

data class ScenarioReconstructionStep(
    val stepOrder: Int,
    val timeLabel: String,
    val headline: String,
    val narrativeDetails: String,
    val supportingClue: String,
    val iconEmoji: String
)

data class CrimeScenario(
    val id: String,
    val title: String,
    val category: ScenarioCategory,
    val thesis: String,
    val requiredConnections: List<Pair<String,String>>,
    val timelineSteps: List<ScenarioReconstructionStep>,
    val perpetratorMotive: String,
    val modusOperandi: String,
    val keySuspectId: String,
    val fatalFlaw: String,
    val deductionInsight: String,
    val rewardInsightPoints: Int,
    val isFormed: Boolean = false,
    val isAdoptedInNotebook: Boolean = false
)

data class CairoGazetteEdition(
    val id: Int,
    val editionNumber: String,
    val dateString: String,
    val mainHeadline: String = "",
    val subHeadline: String = "",
    val leadStory: String = "",
    val detectiveQuote: String = "",
    val publicReaction: String = "",
    val vintageAdSnippet: String = ""
)


enum class NotebookCategory(
    val label: String,
    val iconEmoji: String
) {
    ALL("الكل", "📒"),
    CLUES("أدلة", "🔎"),
    CONTRADICTIONS("تناقضات", "⚠️"),
    FORENSIC("جنائي", "🧪"),
    PERSONAL("شخصي", "📝")
}

enum class ForensicToolType(
    val label: String,
    val description: String,
    val iconEmoji: String
) {
    MAGNIFIER("عدسة مكبرة", "فحص الأدلة الدقيقة", "🔍"),
    SECRET_INK_REAGENT("كاشف الحبر السري", "كشف الكتابات المخفية", "🧪"),
    UV_WOODS_LAMP("مصباح الأشعة فوق البنفسجية", "كشف الآثار المخفية", "🔦"),
    TOXICOLOGY_TUBE("أنبوب السموم", "تحليل العينات الكيميائية", "🧫")
}


val EvidenceEntity.iconEmoji: String
    get() = when (category.name) {
        "FORENSIC" -> "🧪"
        "DOCUMENT" -> "📄"
        "WEAPON" -> "🔪"
        "DIGITAL" -> "💾"
        else -> "📁"
    }
