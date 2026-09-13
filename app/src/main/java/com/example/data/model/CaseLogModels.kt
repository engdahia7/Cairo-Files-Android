package com.example.data.model

import com.example.ui.viewmodel.GameTab

enum class ObjectiveCategory(
    val label: String,
    val iconEmoji: String,
    val hexColor: Long
) {
    CRIME_SCENE("معاينة المسرح الجنائي", "🔍", 0xFFFFB300),        // Amber Gold
    EVIDENCE_ANALYSIS("فحص الأحراز والمعمل", "🔬", 0xFF00E5FF),    // Cyan Terminal
    INTERROGATION("استجواب الشهود والمشتبهين", "👥", 0xFF10B981),   // Emerald
    CORKBOARD_SYNTHESIS("ربط الخيوط والتحري", "🧵", 0xFFEF4444),     // Crimson
    FIELD_OPERATIONS("عمليات القاهرة الميدانية", "📡", 0xFF8B5CF6),  // Purple
    FINAL_DEDUCTION("الاستنتاج وإغلاق القضية", "⚖️", 0xFFF59E0B)   // Gold
}

enum class ObjectivePriority(val label: String, val badgeColor: Long) {
    CRITICAL("رئيسي إلزامي", 0xFFE53935),
    SECONDARY("خيط ثانوي", 0xFF0288D1),
    SECRET_DISCOVERY("كشف استخباراتي سري", 0xFF8E24AA)
}

data class CaseObjective(
    val id: String,
    val caseId: Int,
    val title: String,
    val description: String,
    val category: ObjectiveCategory,
    val priority: ObjectivePriority = ObjectivePriority.CRITICAL,
    val isCompleted: Boolean = false,
    val progressCurrent: Int = 0,
    val progressMax: Int = 1,
    val unlockedByClueTitle: String? = null,
    val isSecretClueUnlocked: Boolean = false,
    val detectiveTip: String = "",
    val rewardInsight: String = "",
    val targetTab: GameTab? = null,
    val targetActionLabel: String? = null
)

data class CaseLogSummary(
    val caseId: Int,
    val caseTitle: String,
    val caseSubtitle: String,
    val totalObjectives: Int,
    val completedObjectives: Int,
    val completionPercentage: Int,
    val readinessStatus: String,
    val isReadyForVerdict: Boolean,
    val objectives: List<CaseObjective>,
    val discoveredCluesCount: Int,
    val totalCluesCount: Int,
    val keyClueDiscovered: Boolean,
    val mirrorThreatLevel: String
)

object CaseLogEngine {

    fun generateLogForCase(
        activeCase: CaseEntity?,
        activeEvidence: List<EvidenceEntity>,
        allSuspects: List<SuspectEntity>,
        boardLinks: List<BoardLinkEntity>,
        encounters: List<EncounterEntity>,
        notebookEntries: List<NotebookEntry>,
        forensicTargets: List<ForensicExamTarget>,
        wiretaps: List<WiretapAudioTape>
    ): CaseLogSummary {
        val caseId = activeCase?.id ?: 1
        val caseTitle = activeCase?.title ?: "القضية الأولى: اختفاء في عمارة الإيموبيليا"
        val caseSubtitle = activeCase?.subtitle ?: "لغز اختفاء شقيق نورا حمدي والشقة المغلقة"

        val objectives = when (caseId) {
            1 -> generateCase1Objectives(activeEvidence, allSuspects, boardLinks, encounters, notebookEntries, forensicTargets, wiretaps)
            2 -> generateCase2Objectives(activeEvidence, allSuspects, boardLinks, encounters, notebookEntries, forensicTargets, wiretaps)
            3 -> generateCase3Objectives(activeEvidence, allSuspects, boardLinks, encounters, notebookEntries, forensicTargets, wiretaps)
            else -> generateGenericCaseObjectives(activeCase, activeEvidence, boardLinks, encounters, notebookEntries, forensicTargets)
        }

        val total = objectives.size
        val completed = objectives.count { it.isCompleted }
        val percentage = if (total > 0) ((completed.toFloat() / total.toFloat()) * 100).toInt() else 0

        val discoveredCluesCount = activeEvidence.count { it.isDiscovered }
        val totalCluesCount = if (activeEvidence.isNotEmpty()) activeEvidence.size else 3
        val keyClueDiscovered = activeEvidence.any { it.isSecretMirrorClue && it.isDiscovered } || discoveredCluesCount >= 2
        val hasValidLinks = boardLinks.any { it.isValidConnection }

        val isReadyForVerdict = completed >= (total * 0.7f).toInt() && discoveredCluesCount >= 2 && (hasValidLinks || caseId > 3)

        val readinessStatus = when {
            isReadyForVerdict -> "✨ مكتمل التحري — جاهز لتقديم مذكرة الاتهام للنيابة"
            completed >= (total / 2) -> "⚡ في منتصف التحقيق — جاري مطابقة الأدلة والاستجواب"
            else -> "🔍 مرحلة جمع القرائن الأولية ومعاينة مسرح الحدث"
        }

        val mirrorThreat = when {
            keyClueDiscovered -> "مرتفع ⚠️ (رصد نشاط مكثف للمرآة)"
            discoveredCluesCount > 0 -> "متوسط 🟡 (خيوط غير مؤكدة)"
            else -> "سري غامض 🕶️"
        }

        return CaseLogSummary(
            caseId = caseId,
            caseTitle = caseTitle,
            caseSubtitle = caseSubtitle,
            totalObjectives = total,
            completedObjectives = completed,
            completionPercentage = percentage,
            readinessStatus = readinessStatus,
            isReadyForVerdict = isReadyForVerdict,
            objectives = objectives,
            discoveredCluesCount = discoveredCluesCount,
            totalCluesCount = totalCluesCount,
            keyClueDiscovered = keyClueDiscovered,
            mirrorThreatLevel = mirrorThreat
        )
    }

    private fun generateCase1Objectives(
        evidence: List<EvidenceEntity>,
        suspects: List<SuspectEntity>,
        boardLinks: List<BoardLinkEntity>,
        encounters: List<EncounterEntity>,
        notebook: List<NotebookEntry>,
        forensicTargets: List<ForensicExamTarget>,
        wiretaps: List<WiretapAudioTape>
    ): List<CaseObjective> {
        val clockFound = evidence.any { it.id == "ev_01_clock" && it.isDiscovered }
        val phoneFound = evidence.any { it.id == "ev_01_phone" && it.isDiscovered }
        val m01Found = evidence.any { it.id == "ev_01_code_m01" && it.isDiscovered }
        val taxiVisited = encounters.any { it.id == "enc_taxi" && it.isVisited }
        val nouraInterrogated = notebook.any { it.linkedSuspectId == "noura" || it.content.contains("نورا") } || suspects.find { it.id == "noura" }?.trustScore != 80
        val clockExamined = forensicTargets.any { it.id.contains("clock") && it.isExamined } || clockFound
        val boardConnected = boardLinks.any { it.isValidConnection && (it.sourceId.contains("clock") || it.targetId.contains("clock") || it.sourceId == "mohamed") }
        val wiretapDecrypted = wiretaps.any { it.isDecrypted }

        val discoveredCount = listOf(clockFound, phoneFound, m01Found).count { it }

        val list = mutableListOf<CaseObjective>()

        // 1. Scene Examination
        list.add(
            CaseObjective(
                id = "obj_1_scene",
                caseId = 1,
                title = "معاينة شقة صبري أبو علم ورفع أحراز مسرح الحدث",
                description = "افحص عمارة الإيموبيليا شقة 14 وارفع أحراز توقيت الغياب والأجهزة المتروكة.",
                category = ObjectiveCategory.CRIME_SCENE,
                priority = ObjectivePriority.CRITICAL,
                isCompleted = clockFound && phoneFound,
                progressCurrent = listOf(clockFound, phoneFound).count { it },
                progressMax = 2,
                detectiveTip = "عقارب ساعة الحائط النحاسية متوقفة عند توقيت غير بريء (11:47). افحص الزنبرك الخلفي.",
                rewardInsight = "+10 نقاط عدالة • توثيق ساعة الاختفاء",
                targetTab = GameTab.CASES,
                targetActionLabel = "معاينة المسرح 🔍"
            )
        )

        // 2. Interrogation
        list.add(
            CaseObjective(
                id = "obj_1_interrogate_noura",
                caseId = 1,
                title = "استجواب الشاكية نورا حمدي وتتبع مسار المفتاح الإضافي",
                description = "واجه نورا بالتناقض بين موعد مغادرتها وموعد توقف الساعة ووجود باب الخدمة الخلفي.",
                category = ObjectiveCategory.INTERROGATION,
                priority = ObjectivePriority.CRITICAL,
                isCompleted = nouraInterrogated,
                progressCurrent = if (nouraInterrogated) 1 else 0,
                progressMax = 1,
                detectiveTip = "راقب لغة جسدها عند ذكر منور العمارة وسائق التاكسي.",
                rewardInsight = "+12 نقطة ثقة • كشف طريق الهروب",
                targetTab = GameTab.INTERROGATION,
                targetActionLabel = "بدء الاستجواب 👥"
            )
        )

        // 3. Field Encounter (Taxi Driver)
        list.add(
            CaseObjective(
                id = "obj_1_field_taxi",
                caseId = 1,
                title = "تتبع مسار التاكسي في ميدان طلعت حرب",
                description = "تحدث مع سائق التاكسي عم إبراهيم في وسط البلد لمعرفة وجهة الشاب المختفي.",
                category = ObjectiveCategory.FIELD_OPERATIONS,
                priority = ObjectivePriority.SECONDARY,
                isCompleted = taxiVisited,
                progressCurrent = if (taxiVisited) 1 else 0,
                progressMax = 1,
                detectiveTip = "شوهد تاكسي أبيض يقف أمام كوبري قصر النيل في نفس توقيت البلاغ.",
                rewardInsight = "+1 خيط مكاني جديد (مرسى الزمالك)",
                targetTab = GameTab.CAIRO_MAP,
                targetActionLabel = "توجه لوسط البلد 🗺️"
            )
        )

        // 4. Forensic Analysis
        list.add(
            CaseObjective(
                id = "obj_1_forensic_clock",
                caseId = 1,
                title = "فحص زنبرك الساعة وكاشف الشمع الجنائي بالمعمل",
                description = "قم بفحص ترس الساعة تحت المجهر في المعمل الجنائي للتأكد من مادة التثبيت ومسح البصمات.",
                category = ObjectiveCategory.EVIDENCE_ANALYSIS,
                priority = ObjectivePriority.SECONDARY,
                isCompleted = clockExamined,
                progressCurrent = if (clockExamined) 1 else 0,
                progressMax = 1,
                detectiveTip = "استخدم عدسة التكبير أو كاشف الأحبار السري في المعمل الجنائي.",
                rewardInsight = "+15 نقطة تحليل جنائي",
                targetTab = GameTab.OFFICE,
                targetActionLabel = "فتح المعمل 🔬"
            )
        )

        // 5. Corkboard Red Links
        list.add(
            CaseObjective(
                id = "obj_1_corkboard_link",
                caseId = 1,
                title = "تثبيت الخيط الأحمر بين المحقق وساعة الحائط باللوحة",
                description = "صل بين بطاقة المحقق محمد عبده وقرينة الساعة المتوقفة لإثبات نية التمويه الزمني.",
                category = ObjectiveCategory.CORKBOARD_SYNTHESIS,
                priority = ObjectivePriority.CRITICAL,
                isCompleted = boardConnected,
                progressCurrent = if (boardConnected) 1 else 0,
                progressMax = 1,
                detectiveTip = "اختر المحقق كعقدة أولى ثم انقر على دليل الساعة واضغط زر 'ربط الخيط الأحمر'.",
                rewardInsight = "+1 خيط جنائي مثبت • كشف الفجوة الزمنية",
                targetTab = GameTab.CORKBOARD,
                targetActionLabel = "لوحة التحقيق 🧵"
            )
        )

        // 6. Dynamic Secret Objective (Unlocked specifically when M-01 clue is discovered)
        if (m01Found) {
            list.add(
                CaseObjective(
                    id = "obj_1_secret_m01",
                    caseId = 1,
                    title = "🎯 فك شفرة الخلية M-01 ورصد ترددات خادم الدقي",
                    description = "هدف استخباراتي مفتوح بفضل قصاصة M-01: تتبع البوابة المشفرة في مكالمات اللاسلكي.",
                    category = ObjectiveCategory.FIELD_OPERATIONS,
                    priority = ObjectivePriority.SECRET_DISCOVERY,
                    isCompleted = wiretapDecrypted || phoneFound,
                    progressCurrent = if (wiretapDecrypted || phoneFound) 1 else 0,
                    progressMax = 1,
                    unlockedByClueTitle = "قصاصة الورق M-01",
                    isSecretClueUnlocked = true,
                    detectiveTip = "الرمز M-01 يرتبط بأول خلية لشبكة المرآة في القاهرة الخديوية.",
                    rewardInsight = "+25% معرفة بشبكة المرآة 👁️",
                    targetTab = GameTab.OFFICE,
                    targetActionLabel = "تنصت اللاسلكي 📻"
                )
            )
        }

        // 7. Final Deduction & Verdict
        list.add(
            CaseObjective(
                id = "obj_1_final_deduction",
                caseId = 1,
                title = "صياغة الاستنتاج القضائي وختم مذكرة النيابة",
                description = "اجمع كافة الأدلة وحرر تقرير الإغلاق لتحديد مصير الشاب وتوجيه الاتهام الرسمي.",
                category = ObjectiveCategory.FINAL_DEDUCTION,
                priority = ObjectivePriority.CRITICAL,
                isCompleted = discoveredCount >= 2 && boardConnected,
                progressCurrent = if (discoveredCount >= 2 && boardConnected) 1 else 0,
                progressMax = 1,
                detectiveTip = "يتطلب إغلاق القضية قرينتين مكتشفتين ورابطاً أحمر مثبت في لوحة التحقيق.",
                rewardInsight = "+1 نجمة تحرٍ • فتح القضية رقم 2",
                targetTab = GameTab.CASES,
                targetActionLabel = "جلسة الاستنتاج ⚖️"
            )
        )

        return list
    }

    private fun generateCase2Objectives(
        evidence: List<EvidenceEntity>,
        suspects: List<SuspectEntity>,
        boardLinks: List<BoardLinkEntity>,
        encounters: List<EncounterEntity>,
        notebook: List<NotebookEntry>,
        forensicTargets: List<ForensicExamTarget>,
        wiretaps: List<WiretapAudioTape>
    ): List<CaseObjective> {
        val photoFound = evidence.any { it.id == "ev_02_photo" && it.isDiscovered }
        val newsstandVisited = encounters.any { it.id == "enc_newsstand" && it.isVisited }
        val leilaInterrogated = notebook.any { it.linkedSuspectId == "leila" || it.content.contains("ليلى") }
        val photoForensics = forensicTargets.any { it.id.contains("photo") && it.isExamined } || photoFound
        val boardConnected = boardLinks.size >= 2

        return listOf(
            CaseObjective(
                id = "obj_2_photo",
                caseId = 2,
                title = "استرجاع نيجاتيف الصورة من كاميرا حادث الدهس",
                description = "افحص كاميرا المصور الصحفي خالد فهمي لكشف هوية الشخص الواقف في الركن المظلم.",
                category = ObjectiveCategory.CRIME_SCENE,
                priority = ObjectivePriority.CRITICAL,
                isCompleted = photoFound,
                progressCurrent = if (photoFound) 1 else 0,
                progressMax = 1,
                detectiveTip = "الظل الخلفي في الصورة يتطابق بنسبة 98% مع بنية يوسف كامل.",
                rewardInsight = "+14 نقطة عدالة • إثبات نجاة يوسف كامل",
                targetTab = GameTab.CASES,
                targetActionLabel = "فحص الكاميرا 📷"
            ),
            CaseObjective(
                id = "obj_2_newsstand",
                caseId = 2,
                title = "لقاء بائع الجرائد في شارع البرازيل بالزمالك",
                description = "استجوب عم جلال لمعرفة الرجل الذي يشتري جريدة الأهرام ويكتب الأرقام الحمراء بالهامش.",
                category = ObjectiveCategory.FIELD_OPERATIONS,
                priority = ObjectivePriority.CRITICAL,
                isCompleted = newsstandVisited,
                progressCurrent = if (newsstandVisited) 1 else 0,
                progressMax = 1,
                detectiveTip = "المرآة تستخدم الإعلانات المبوبة لتبادل الأوامر دون ترك أثر رقمي.",
                rewardInsight = "+1 شفرة إعلانات الصحف",
                targetTab = GameTab.CAIRO_MAP,
                targetActionLabel = "توجه للزمالك 🗺️"
            ),
            CaseObjective(
                id = "obj_2_leila",
                caseId = 2,
                title = "استجواب الصحفية ليلى نجيب حول آخر اتصال",
                description = "واجه ليلى بحقيقة التسجيل المسرب قبل 48 ساعة من اختفاء يوسف كامل.",
                category = ObjectiveCategory.INTERROGATION,
                priority = ObjectivePriority.CRITICAL,
                isCompleted = leilaInterrogated,
                progressCurrent = if (leilaInterrogated) 1 else 0,
                progressMax = 1,
                detectiveTip = "ليلى مترددة بين الثقة بك والخوف على سلامة أرشيفها السري.",
                rewardInsight = "+10 ولاء ليلى • فتح تسجيل 02:13 ص",
                targetTab = GameTab.INTERROGATION,
                targetActionLabel = "استجواب ليلى 👥"
            ),
            CaseObjective(
                id = "obj_2_board",
                caseId = 2,
                title = "ربط صورة يوسف كامل بالصحفية ليلى في اللوحة",
                description = "ثبت خيط التزامن الزمني بين بلاغ الدهس واختفاء أرشيف التحقيقات.",
                category = ObjectiveCategory.CORKBOARD_SYNTHESIS,
                priority = ObjectivePriority.CRITICAL,
                isCompleted = boardConnected,
                progressCurrent = if (boardConnected) 1 else 0,
                progressMax = 1,
                detectiveTip = "صل بين بطاقة يوسف كامل وبطاقة ليلى نجيب بخيط أصفر أو أحمر.",
                rewardInsight = "+1 خيط مؤكد • كشف الرابط الصحفي",
                targetTab = GameTab.CORKBOARD,
                targetActionLabel = "لوحة التحقيق 🧵"
            ),
            CaseObjective(
                id = "obj_2_verdict",
                caseId = 2,
                title = "إصدار التقرير النهائي لقضية دهس الصحفي",
                description = "قدم مذكرة رسمية تثبت أن الحادث كان مدبراً لتصفية الشاهد وليس مجرد قضاء وقدر.",
                category = ObjectiveCategory.FINAL_DEDUCTION,
                priority = ObjectivePriority.CRITICAL,
                isCompleted = photoFound && newsstandVisited,
                progressCurrent = if (photoFound && newsstandVisited) 1 else 0,
                progressMax = 1,
                detectiveTip = "التقرير يحتاج إثبات تواجد يوسف في مسرح الجريمة ورصد شفرة الأهرام.",
                rewardInsight = "+1 نجمة • فتح القضية رقم 3",
                targetTab = GameTab.CASES,
                targetActionLabel = "تقديم المذكرة ⚖️"
            )
        )
    }

    private fun generateCase3Objectives(
        evidence: List<EvidenceEntity>,
        suspects: List<SuspectEntity>,
        boardLinks: List<BoardLinkEntity>,
        encounters: List<EncounterEntity>,
        notebook: List<NotebookEntry>,
        forensicTargets: List<ForensicExamTarget>,
        wiretaps: List<WiretapAudioTape>
    ): List<CaseObjective> {
        val nileDataFound = evidence.any { it.id == "ev_03_nile_data" && it.isDiscovered }
        val marwanInterrogated = notebook.any { it.linkedSuspectId == "marwan" || it.content.contains("مروان") }

        return listOf(
            CaseObjective(
                id = "obj_3_dossier",
                caseId = 3,
                title = "تفتيش سجلات شركة NILE DATA المنسية",
                description = "ابحث في الأرشيف الاستثماري لعام 2019 عن العقود السرية لإدارة كاميرات المراقبة.",
                category = ObjectiveCategory.CRIME_SCENE,
                priority = ObjectivePriority.CRITICAL,
                isCompleted = nileDataFound,
                progressCurrent = if (nileDataFound) 1 else 0,
                progressMax = 1,
                detectiveTip = "الشركة كانت واجهة تقنية يديرها مروان صبري قبل انضمامه لمكتبك.",
                rewardInsight = "+15 نقطة حذر • خريطة الـ 14 كاميرا",
                targetTab = GameTab.CASES,
                targetActionLabel = "فحص الأرشيف 📜"
            ),
            CaseObjective(
                id = "obj_3_marwan",
                caseId = 3,
                title = "مواجهة مروان صبري بسجلات التشفير",
                description = "استجوب مروان حول بصمة الكود المبرمج على بوابات الخوادم في الدقي.",
                category = ObjectiveCategory.INTERROGATION,
                priority = ObjectivePriority.CRITICAL,
                isCompleted = marwanInterrogated,
                progressCurrent = if (marwanInterrogated) 1 else 0,
                progressMax = 1,
                detectiveTip = "مروان يخاف من السجن لكنه مخلص لمحمد إذا تعاملت معه بالتعاطف والذكاء.",
                rewardInsight = "+12 نقطة ولاء مروان • فك التشفير",
                targetTab = GameTab.INTERROGATION,
                targetActionLabel = "مواجهة مروان 👥"
            ),
            CaseObjective(
                id = "obj_3_wiretap",
                caseId = 3,
                title = "التقاط إشارة التردد 104.2 كيلوهرتز على اللاسلكي",
                description = "قم بضبط جهاز الاستقبال في غرفة العمليات لاعتراض المكالمة السرية لخلية الدقي.",
                category = ObjectiveCategory.FIELD_OPERATIONS,
                priority = ObjectivePriority.SECONDARY,
                isCompleted = wiretaps.any { it.isDecrypted },
                progressCurrent = if (wiretaps.any { it.isDecrypted }) 1 else 0,
                progressMax = 1,
                detectiveTip = "حرك مؤشر التردد بدقة لتقليل التشويش وسماع صوت المتحدث.",
                rewardInsight = "+1 شريط صوتي مشفر",
                targetTab = GameTab.OFFICE,
                targetActionLabel = "غرفة العمليات 📻"
            ),
            CaseObjective(
                id = "obj_3_verdict",
                caseId = 3,
                title = "كشف الواجهة الرقمية لشبكة المرآة وإغلاق القضية",
                description = "صغ الاستنتاج القضائي الذي يربط شركة نايل داتا بالتمويلات الأجنبية.",
                category = ObjectiveCategory.FINAL_DEDUCTION,
                priority = ObjectivePriority.CRITICAL,
                isCompleted = nileDataFound,
                progressCurrent = if (nileDataFound) 1 else 0,
                progressMax = 1,
                detectiveTip = "التقرير النهائي سيمهد الطريق لكشف خلايا الفصل الثاني.",
                rewardInsight = "+1 نجمة • فتح القضية رقم 4",
                targetTab = GameTab.CASES,
                targetActionLabel = "إغلاق القضية ⚖️"
            )
        )
    }

    private fun generateGenericCaseObjectives(
        activeCase: CaseEntity?,
        evidence: List<EvidenceEntity>,
        boardLinks: List<BoardLinkEntity>,
        encounters: List<EncounterEntity>,
        notebook: List<NotebookEntry>,
        forensicTargets: List<ForensicExamTarget>
    ): List<CaseObjective> {
        val caseId = activeCase?.id ?: 1
        val discoveredCount = evidence.count { it.isDiscovered }
        val totalEvidenceCount = if (evidence.isNotEmpty()) evidence.size else 3
        val allEvidenceFound = discoveredCount >= totalEvidenceCount.coerceAtLeast(2)
        val hasCorkboardLinks = boardLinks.isNotEmpty()
        val hasNotebookNotes = notebook.any { it.relatedCaseId == caseId || it.relatedCaseId == null }

        val list = mutableListOf<CaseObjective>()

        // 1. Scene Investigation
        list.add(
            CaseObjective(
                id = "obj_gen_${caseId}_scene",
                caseId = caseId,
                title = "معاينة مسرح الجريمة: ${activeCase?.location ?: "القاهرة"}",
                description = "باشر المعاينة الميدانية في موقع البلاغ (${activeCase?.timeLabel ?: "مساءً"}) واكشف القرائن الخفية.",
                category = ObjectiveCategory.CRIME_SCENE,
                priority = ObjectivePriority.CRITICAL,
                isCompleted = allEvidenceFound,
                progressCurrent = discoveredCount,
                progressMax = totalEvidenceCount,
                detectiveTip = "افحص كل تفصيلة في الموقع وراجع تقرير المعاينة الجنائية.",
                rewardInsight = "+15 نقطة عدالة • رفع الأحراز",
                targetTab = GameTab.CASES,
                targetActionLabel = "معاينة المسرح 🔍"
            )
        )

        // 2. Key Clue Search
        val keyClueFound = evidence.any { it.isSecretMirrorClue && it.isDiscovered } || discoveredCount >= 1
        list.add(
            CaseObjective(
                id = "obj_gen_${caseId}_keyclue",
                caseId = caseId,
                title = "استخراج القرينة الحاسمة: ${activeCase?.keyClueTitle ?: "حرز القضية"}",
                description = "اعثر على الدليل المفتاحي الذي يربط مرتكب الجريمة بدوافع خفية.",
                category = ObjectiveCategory.EVIDENCE_ANALYSIS,
                priority = ObjectivePriority.CRITICAL,
                isCompleted = keyClueFound,
                progressCurrent = if (keyClueFound) 1 else 0,
                progressMax = 1,
                detectiveTip = "الدليل الرئيسي يفكك الادعاءات الوهمية للشهود.",
                rewardInsight = "+1 قرينة رئيسية موثقة",
                targetTab = GameTab.CASES,
                targetActionLabel = "فحص الأحراز 🔬"
            )
        )

        // 3. Interrogation & Witness Testimony
        list.add(
            CaseObjective(
                id = "obj_gen_${caseId}_witness",
                caseId = caseId,
                title = "استجواب الشاكي (${activeCase?.client ?: "الشاهد"}) والشهود المقربين",
                description = "طابق أقوال الشاكي مع وقائع المسرح الجنائي وسجل ملاحظات التناقض في الدفتر.",
                category = ObjectiveCategory.INTERROGATION,
                priority = ObjectivePriority.CRITICAL,
                isCompleted = hasNotebookNotes,
                progressCurrent = if (hasNotebookNotes) 1 else 0,
                progressMax = 1,
                detectiveTip = "ابحث عن الثغرات الزمنية وتغير نبرة الصوت أثناء الاستجواب.",
                rewardInsight = "+10 نقاط ثقة • تدوين في الدفتر",
                targetTab = GameTab.INTERROGATION,
                targetActionLabel = "استجواب الشهود 👥"
            )
        )

        // 4. Corkboard Links
        list.add(
            CaseObjective(
                id = "obj_gen_${caseId}_corkboard",
                caseId = caseId,
                title = "تثبيت خيوط التحري وتطابق الفرضيات على لوحة الفلين",
                description = "اربط بين أطراف القضية والقرائن المكتشفة بالخيوط الحمراء والصفراء.",
                category = ObjectiveCategory.CORKBOARD_SYNTHESIS,
                priority = ObjectivePriority.SECONDARY,
                isCompleted = hasCorkboardLinks,
                progressCurrent = if (hasCorkboardLinks) 1 else 0,
                progressMax = 1,
                detectiveTip = "اللوحة تجمع الصورة الكبرى وتكشف الأنماط المتكررة للشبكة.",
                rewardInsight = "+1 رابط جنائي معتمد",
                targetTab = GameTab.CORKBOARD,
                targetActionLabel = "لوحة التحقيق 🧵"
            )
        )

        // 5. Secret Mirror Network Clue (If present)
        if (activeCase?.mirrorClueSnippet?.isNotEmpty() == true) {
            list.add(
                CaseObjective(
                    id = "obj_gen_${caseId}_mirror",
                    caseId = caseId,
                    title = "🎯 رصد خيط المرآة السري: ${activeCase.mirrorClueSnippet}",
                    description = "استخرج الرمز السري المرتبط بتنظيم المرآة ضمن وثائق ومحيط هذه القضية.",
                    category = ObjectiveCategory.FIELD_OPERATIONS,
                    priority = ObjectivePriority.SECRET_DISCOVERY,
                    isCompleted = discoveredCount >= 2,
                    progressCurrent = if (discoveredCount >= 2) 1 else 0,
                    progressMax = 1,
                    unlockedByClueTitle = activeCase.keyClueTitle,
                    isSecretClueUnlocked = true,
                    detectiveTip = "هذا الخيط يقربك من كشف ملف SUBJECT 01 ونهايات اللعبة الكبرى.",
                    rewardInsight = "+5% معرفة بشبكة المرآة 👁️",
                    targetTab = GameTab.OFFICE,
                    targetActionLabel = "غرفة العمليات 📡"
                )
            )
        }

        // 6. Final Verdict
        val isReady = discoveredCount >= 2 && hasCorkboardLinks
        list.add(
            CaseObjective(
                id = "obj_gen_${caseId}_verdict",
                caseId = caseId,
                title = "إغلاق ملف القضية #${caseId} وتقديم مذكرة الاتهام",
                description = "صغ الاستنتاج القضائي واختم ملف القضية رسميًا بالختم المعتمد.",
                category = ObjectiveCategory.FINAL_DEDUCTION,
                priority = ObjectivePriority.CRITICAL,
                isCompleted = isReady,
                progressCurrent = if (isReady) 1 else 0,
                progressMax = 1,
                detectiveTip = "تأكد من استيفاء كافة الأدلة والشهادات قبل النطق بالقرار.",
                rewardInsight = "+1 قضية محلولة في الأرشيف",
                targetTab = GameTab.CASES,
                targetActionLabel = "إغلاق الملف ⚖️"
            )
        )

        return list
    }
}
