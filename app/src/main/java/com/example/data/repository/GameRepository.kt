package com.example.data.repository

import com.example.data.db.InvestigationDao
import com.example.data.model.BoardLinkEntity
import com.example.data.model.CaseEntity
import com.example.data.model.CaseStatus
import com.example.data.model.EndingEntity
import com.example.data.model.EncounterEntity
import com.example.data.model.EvidenceCategory
import com.example.data.model.EvidenceEntity
import com.example.data.model.EvidencePhase
import com.example.data.model.PlayerStatsEntity
import com.example.data.model.SuspectEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class GameRepository(private val dao: InvestigationDao) {

    suspend fun updateStats(stats: PlayerStatsEntity) = withContext(Dispatchers.IO) {
        dao.updatePlayerStats(stats)
    }

    val allCases: Flow<List<CaseEntity>> = dao.getAllCases()
    val allSuspects: Flow<List<SuspectEntity>> = dao.getAllSuspects()
    val playerStats: Flow<PlayerStatsEntity?> = dao.getPlayerStats()
    val boardLinks: Flow<List<BoardLinkEntity>> = dao.getAllBoardLinks()
    val encounters: Flow<List<EncounterEntity>> = dao.getAllEncounters()
    val endings: Flow<List<EndingEntity>> = dao.getAllEndings()
    val discoveredEvidence: Flow<List<EvidenceEntity>> = dao.getAllDiscoveredEvidence()
    val secretClues: Flow<List<EvidenceEntity>> = dao.getSecretMirrorClues()

    fun getCaseById(caseId: Int): Flow<CaseEntity?> = dao.getCaseById(caseId)

    fun getEvidenceForCase(caseId: Int): Flow<List<EvidenceEntity>> = dao.getEvidenceForCase(caseId)

    suspend fun initializeGameIfEmpty() = withContext(Dispatchers.IO) {
        val existingStats = dao.getPlayerStats().firstOrNull()
        if (existingStats == null) {
            dao.insertPlayerStats(
                PlayerStatsEntity(
                    id = 1,
                    justiceScore = 14,
                    empathyScore = 16,
                    cautionScore = 12,
                    obsessionScore = 15,
                    currentChapter = 1,
                    activeCaseId = 1,
                    totalCasesSolved = 0,
                    mirrorNetworkKnowledge = 15,
                    secretCluesFoundCount = 1,
                    officeLevel = 1
                )
            )
            dao.insertCases(generateInitial50Cases())
            dao.insertSuspects(generateInitialSuspects())
            dao.insertEvidence(generateInitialEvidence())
            dao.insertEncounters(generateInitialEncounters())
            dao.insertEndings(generateInitialEndings())
            dao.insertBoardLink(
                BoardLinkEntity(
                    sourceId = "mohamed",
                    targetId = "ev_01_clock",
                    linkDescription = "الساعة المتوقفة عمداً عند 11:47 تشير لرسالة مشفرة",
                    isValidConnection = true
                )
            )
        }
    }

    suspend fun advanceActiveCase(caseId: Int, newProgress: Int, newStatus: CaseStatus) = withContext(Dispatchers.IO) {
        dao.updateCaseStatus(caseId, newStatus, newProgress)
        val stats = dao.getPlayerStats().firstOrNull() ?: PlayerStatsEntity()
        if (newStatus == CaseStatus.RESOLVED) {
            val updatedSolved = stats.totalCasesSolved + 1
            val nextCase = caseId + 1
            if (nextCase <= 50) {
                dao.updateCaseStatus(nextCase, CaseStatus.UNLOCKED, 0)
            }
            dao.updatePlayerStats(
                stats.copy(
                    totalCasesSolved = updatedSolved,
                    activeCaseId = if (nextCase <= 50) nextCase else caseId,
                    mirrorNetworkKnowledge = (stats.mirrorNetworkKnowledge + 6).coerceAtMost(100),
                    officeLevel = when {
                        updatedSolved >= 20 -> 3
                        updatedSolved >= 10 -> 2
                        else -> 1
                    }
                )
            )
        }
    }

    suspend fun discoverEvidence(evidenceId: String) = withContext(Dispatchers.IO) {
        dao.markEvidenceDiscovered(evidenceId)
    }

    suspend fun confirmEvidenceOnBoard(evidenceId: String) = withContext(Dispatchers.IO) {
        dao.markEvidenceConfirmed(evidenceId)
    }

    suspend fun makeMoralChoice(
        justiceDelta: Int,
        empathyDelta: Int,
        cautionDelta: Int,
        obsessionDelta: Int
    ) = withContext(Dispatchers.IO) {
        val stats = dao.getPlayerStats().firstOrNull() ?: PlayerStatsEntity()
        val updated = stats.copy(
            justiceScore = (stats.justiceScore + justiceDelta).coerceAtLeast(0),
            empathyScore = (stats.empathyScore + empathyDelta).coerceAtLeast(0),
            cautionScore = (stats.cautionScore + cautionDelta).coerceAtLeast(0),
            obsessionScore = (stats.obsessionScore + obsessionDelta).coerceAtLeast(0)
        )
        dao.updatePlayerStats(updated)
    }

    suspend fun updateSuspectRelationship(
        suspectId: String,
        trustDelta: Int,
        fearDelta: Int,
        loyaltyDelta: Int
    ) = withContext(Dispatchers.IO) {
        val suspect = dao.getSuspectById(suspectId).firstOrNull() ?: return@withContext
        val newTrust = (suspect.trustScore + trustDelta).coerceIn(0, 100)
        val newFear = (suspect.fearScore + fearDelta).coerceIn(0, 100)
        val newLoyalty = (suspect.loyaltyScore + loyaltyDelta).coerceIn(0, 100)
        dao.updateSuspectMeters(suspectId, newTrust, newFear, newLoyalty)
    }

    suspend fun connectBoardNodes(
        sourceId: String,
        targetId: String,
        hypothesis: String,
        isValid: Boolean
    ): Boolean = withContext(Dispatchers.IO) {
        dao.insertBoardLink(
            BoardLinkEntity(
                sourceId = sourceId,
                targetId = targetId,
                linkDescription = hypothesis,
                isValidConnection = isValid
            )
        )
        if (isValid) {
            val stats = dao.getPlayerStats().firstOrNull() ?: PlayerStatsEntity()
            dao.updatePlayerStats(
                stats.copy(
                    mirrorNetworkKnowledge = (stats.mirrorNetworkKnowledge + 3).coerceAtMost(100)
                )
            )
        }
        isValid
    }

    suspend fun visitEncounter(encounterId: String) = withContext(Dispatchers.IO) {
        dao.markEncounterVisited(encounterId)
        val stats = dao.getPlayerStats().firstOrNull() ?: PlayerStatsEntity()
        dao.updatePlayerStats(
            stats.copy(
                secretCluesFoundCount = stats.secretCluesFoundCount + 1,
                mirrorNetworkKnowledge = (stats.mirrorNetworkKnowledge + 4).coerceAtMost(100)
            )
        )
    }

    suspend fun unlockEndingById(endingId: Int) = withContext(Dispatchers.IO) {
        dao.unlockEnding(endingId)
    }

    suspend fun resetGameProgress() = withContext(Dispatchers.IO) {
        dao.clearPlayerStats()
        dao.clearCases()
        dao.clearEvidence()
        dao.clearSuspects()
        dao.clearEncounters()
        dao.clearBoardLinks()
        dao.clearEndings()

        // Re-initialize fresh starting state
        dao.insertPlayerStats(
            PlayerStatsEntity(
                id = 1,
                justiceScore = 14,
                empathyScore = 16,
                cautionScore = 12,
                obsessionScore = 15,
                currentChapter = 1,
                activeCaseId = 1,
                totalCasesSolved = 0,
                mirrorNetworkKnowledge = 15,
                secretCluesFoundCount = 1,
                officeLevel = 1
            )
        )
        dao.insertCases(generateInitial50Cases())
        dao.insertSuspects(generateInitialSuspects())
        dao.insertEvidence(generateInitialEvidence())
        dao.insertEncounters(generateInitialEncounters())
        dao.insertEndings(generateInitialEndings())
        dao.insertBoardLink(
            BoardLinkEntity(
                sourceId = "mohamed",
                targetId = "ev_01_clock",
                linkDescription = "الساعة المتوقفة عمداً عند 11:47 تشير لرسالة مشفرة",
                isValidConnection = true
            )
        )
    }

    suspend fun triggerManualSave() = withContext(Dispatchers.IO) {
        val currentStats = dao.getPlayerStats().firstOrNull()
        if (currentStats != null) {
            dao.updatePlayerStats(currentStats)
        }
    }

    // --- Initial Dataset Creation ---
    private fun generateInitial50Cases(): List<CaseEntity> {
        val list = mutableListOf<CaseEntity>()

        // Chapter 1: التعلم وبناء شخصية المحقق (1 - 10)
        list.add(
            CaseEntity(
                id = 1,
                chapter = 1,
                title = "الشقة المقفولة",
                subtitle = "اختفاء غامض وباب خدمة سري وساعة مقصودة",
                description = "نورا حمدي تأتي لمكتب محمد مؤكدة أن شقيقها لم يهرب. الشقة مقفولة من الداخل ورسالة غريبة تصل هاتفه بعد اختفائه.",
                client = "نورا حمدي",
                location = "وسط البلد — عمارة اللواء",
                timeLabel = "07:32 ص",
                coldOpenNarrative = "ليل القاهرة.. هاتف المكتب يرن في 02:13 ص.. صوت مجهول يقول: 'أنا يوسف كامل.. لا تثق بأول شخص يخبرك أنني مت'. ثم في الصباح تأتي نورا حمدي بقضية شقيقها المختفي داخل شقة مقفولة بإحكام.",
                keyClueTitle = "ساعة متوقفة عمداً عند 11:47",
                mirrorClueSnippet = "ورقة بيضاء تحت حافة المكتب تحمل الرمز المريب: M-01",
                status = CaseStatus.IN_PROGRESS,
                isKeyPlayableCase = true,
                progressPercent = 55
            )
        )
        list.add(
            CaseEntity(
                id = 2,
                chapter = 1,
                title = "الصورة الأخيرة",
                subtitle = "حادث سيارة مصور صحفي والظل المألوف",
                description = "مصور صحفي يموت في حادث مدبر. آخر صورة التقطها في شارع قصر النيل تظهر يوسف كامل حياً في الخلفية بعد سنتين من إعلان وفاته.",
                client = "أسرة المصور خالد فهمي",
                location = "الزمالك — محل تحميض كيميا القديم",
                timeLabel = "10:15 ص",
                coldOpenNarrative = "صوت اصطدام مروع في نفق الجيزة.. الكاميرا تطير على الرصيف.. بطاقة الذاكرة التالفة تخفي صورة ظل لا يمكن أن يكون إلا يوسف كامل.",
                keyClueTitle = "شريحة الذاكرة المستعادة ومطابقة ملامح يوسف",
                mirrorClueSnippet = "رقم لوحة سيارة دبلوماسية سوداء مجهولة المالك",
                status = CaseStatus.UNLOCKED,
                isKeyPlayableCase = true,
                progressPercent = 0
            )
        )
        list.add(
            CaseEntity(
                id = 3,
                chapter = 1,
                title = "الملف المفقود",
                subtitle = "اختفاء ملف حساس من خزينة شركة استثمارية",
                description = "شركة كبرى تفقد وثيقة استحواذ حرجة. ثلاثة موظفين مشتبه بهم، لكن النسخة الأصلية موجودة في أرشيف ورقي باسم NILE DATA.",
                client = "رئيس الشؤون القانونية",
                location = "الدقي — برج النيل",
                timeLabel = "01:00 م",
                coldOpenNarrative = "إنذار الخزنة صامت.. الملف استُبدل بورق أبيض مزيف.. لا توجد بصمات اقتحام، والاسم الوحيد المسجل هو NILE DATA.",
                keyClueTitle = "ختم أرشيف ورقي منسي ورمز شركة بيانات وهمية",
                mirrorClueSnippet = "اسم NILE DATA كواجهة تقنية لجمع ملفات شخصية للمسؤولين",
                status = CaseStatus.LOCKED,
                isKeyPlayableCase = true
            )
        )
        list.add(
            CaseEntity(
                id = 4,
                chapter = 1,
                title = "الشاهد الصامت",
                subtitle = "عامل نظافة يرى الجريمة ويخشى على ابنه",
                description = "عامل نظافة شاهد عملية خطف، لكنه صامت تماماً لحماية ابنه. هل تحميه أم تضغط عليه؟ قرار يغير مجرى 4 قضايا قادمة.",
                client = "بلاغ سري / قسم قصر النيل",
                location = "المهندسين — شارع جامعة الدول",
                timeLabel = "08:45 م",
                coldOpenNarrative = "صوت صفير الرياح في زقاق معتم.. عامل النظافة يمسك مكنسته ويرتجف بعدما رأى رجلاً يُسحب داخل شاحنة مغلقة.",
                keyClueTitle = "قصاصة رقم هاتف مرتبط بالشقة المقفولة",
                mirrorClueSnippet = "رسالة تهديد صوتية مشوهة تنبه الشاهد بعدم التعاون مع محمد",
                status = CaseStatus.LOCKED,
                isKeyPlayableCase = true
            )
        )
        list.add(
            CaseEntity(
                id = 5,
                chapter = 1,
                title = "الرجل الذي لم يكن هناك",
                subtitle = "أربع كاميرات توثق نفس الشخص في دقيقة واحدة",
                description = "تسجيلات متزامنة في 4 ميادين تظهر مشتبهاً به واحداً. كشف تلاعب احترافي عبر خادم أرشيف كاميرات العاصمة.",
                client = "الرائد عمر الديب",
                location = "ميدان التحرير وميدان طلعت حرب",
                timeLabel = "03:20 ص",
                coldOpenNarrative = "شاشات المراقبة تومض في غرفة المراقبة.. الضابط يفرك عينيه: كيف يعبر المشتبه به 4 ميادين في نفس الدقيقة؟",
                keyClueTitle = "ثغرة التلاعب الزمني في خادم التسجيلات",
                mirrorClueSnippet = "برمجية خبيثة تحمل بصمة رقمية لمروان صبري القديمة",
                status = CaseStatus.LOCKED
            )
        )
        list.add(
            CaseEntity(
                id = 6,
                chapter = 1,
                title = "مقهى آخر الليل",
                subtitle = "اختفاء زبون غامض والبداية الفعلية للوحة الربط",
                description = "عم صابر يطلب عون محمد بعد اختفاء زبون يومي كان يدون ملاحظات غريبة. بداية تدريب اللاعب على ربط الخيوط الحمراء.",
                client = "عم صابر (صاحب المقهى)",
                location = "السيدة زينب — حارة الحناوي",
                timeLabel = "11:30 م",
                coldOpenNarrative = "صوت نرد الطاولة ودخان الشيشة.. عم صابر يقدم كوب شاي مظبوط لمحمد ويهمس: 'الراجل ده بقاله 3 أيام مجاش، وساب الأجندة دي تحت الطقطوقة'.",
                keyClueTitle = "أجندة مقهى مشفرة برابط: الزبون ← الهاتف ← السيارة ← يوسف",
                mirrorClueSnippet = "اسم مقهى عم صابر مدون كنقطة رصد رقم 4 في شبكة المرآة",
                status = CaseStatus.LOCKED,
                isKeyPlayableCase = true
            )
        )
        list.add(
            CaseEntity(
                id = 7,
                chapter = 1,
                title = "الدور السابع",
                subtitle = "شقة مراقبة مهجورة تكشف ملف محمد عبده الشخصي",
                description = "شقة مهجورة تستهلك كهرباء هائلة. محمد يقتحمها ليجد مصفوفة مراقبة، وملفاً سرياً مفصلاً عن حياته وتحركاته السابقة.",
                client = "تحقيق ذاتي لمحمد عبده",
                location = "العباسية — عمارة الأوقاف القديمة",
                timeLabel = "01:15 ص",
                coldOpenNarrative = "صوت مراوح الخوادم تدوي خلف باب حديدي بالدور السابع.. صور معلقة بخيوط.. وفي منتصف الغرفة ملف ضخم مكتوب عليه: 'الهدف: محمد عبده'.",
                keyClueTitle = "ملف رصد محمد عبده المؤرخ قبل استقالته من الشرطة",
                mirrorClueSnippet = "شعار المرآة المكسورة محفور على ظهر شاشات المراقبة",
                status = CaseStatus.LOCKED,
                isKeyPlayableCase = true
            )
        )
        list.add(
            CaseEntity(
                id = 8,
                chapter = 1,
                title = "الرسالة التي وصلت متأخرة",
                subtitle = "طرد بريدي من يوسف كامل بعد 3 أشهر من اختفائه",
                description = "رسالة مختومة تصل لمكتب محمد: 'إذا وصلت إليك هذه الرسالة فمعنى ذلك أنني فشلت. لا تبحث عن المرآة.. ابحث عن أول مرآة'.",
                client = "طرد مسجل من يوسف كامل",
                location = "وسط البلد — هيئة البريد الرئيسية",
                timeLabel = "09:00 ص",
                coldOpenNarrative = "ساعي بريد عجوز يوقع الاستلام.. ختم الطرد يعود إلى 90 يوماً مضت.. الحبر باهت والكلمات تحذيرية كجرس إنذار.",
                keyClueTitle = "مفتاح خزانة أمانات قديمة وشفرة 'أول مرآة'",
                mirrorClueSnippet = "إحداثيات سرية لمستودع مهجور على أطراف حلوان",
                status = CaseStatus.LOCKED
            )
        )
        list.add(
            CaseEntity(
                id = 9,
                chapter = 1,
                title = "موت على النيل",
                subtitle = "جثة مجهولة الهوية تحمل بطاقة شركة NILE DATA",
                description = "د. سلمى تستدعي محمد لمشرحة زينهم. جثة انتشلت من كورنيش النيل تحمل بطاقة دخول سرية لنفس الشركة بالقضية الثالثة.",
                client = "د. سلمى فؤاد (الطب الشرعي)",
                location = "مشرحة زينهم وكورنيش المعادي",
                timeLabel = "04:00 ص",
                coldOpenNarrative = "أضواء كشافات شرطة المسطحات المائية تنعكس على مياه النيل الباردة.. د. سلمى ترفع الغطاء: 'محمد، الشخص ده اتقتل بحقنة نادرة مش بالغرق'.",
                keyClueTitle = "تقرير السموم الجنائي وبطاقة الدخول الممغنطة لشركة NILE DATA",
                mirrorClueSnippet = "الضحية كان مبرمجاً حاول تسريب قاعدة بيانات المرآة قبل تصفيته",
                status = CaseStatus.LOCKED
            )
        )
        list.add(
            CaseEntity(
                id = 10,
                chapter = 1,
                title = "آخر خيط — ظهور المرآة",
                subtitle = "تجمع خيوط القضايا التسع واكتشاف الملف رقم 00",
                description = "محمد يضع جميع الأدلة على اللوحة لتتصل الخيوط تلقائياً وتكشف شبكة الظل المسماة 'المرآة' ورسالة: 'أنت الآن داخل القضية'.",
                client = "محمد عبده وفريق التحقيق",
                location = "مكتب محمد — فوق المحل القديم",
                timeLabel = "02:13 ص",
                coldOpenNarrative = "خيوط حمراء تغطي الجدار بالكامل.. من الشقة المقفولة إلى صورة يوسف، إلى أرشيف نايل داتا.. وفجأة شاشة الكمبيوتر تضيء برسالة: 'أهلاً بك داخل القضية يا محمد'.",
                keyClueTitle = "الملف المرجعي رقم 00 وهيكل الخلايا المنفصلة",
                mirrorClueSnippet = "الإعلان الرسمي عن وجود شبكة المرآة المعلوماتية بالعاصمة",
                status = CaseStatus.LOCKED,
                isKeyPlayableCase = true
            )
        )

        // Chapter 2: المرآة تبدأ بالظهور (11 - 20)
        val ch2Titles = listOf(
            "الحساب الوهمي" to "تحويلات مالية متناهية الصغر ترسم خريطة سرية للقاهرة",
            "الرجل الثاني" to "المدير الظاهر للمرآة يتبين أنه مجرد دمية لواجهة أخرى",
            "دفتر الأسماء" to "دفتر قديم يضم أسماء قضاة وضباط وصحفيين ومنهم عمر الديب",
            "شاهد من الماضي" to "امرأة تفجر تفاصيل القضية التي دمرت مسيرة محمد قبل 3 سنوات",
            "القضية القديمة" to "فلاشباك للماضي: هل كان خطأ محمد المهني مدبراً من المرآة؟",
            "الملف 27" to "الصفحة المفقودة من أرشيف وزارة الداخلية والوصول المصرح",
            "صوت يوسف" to "تسجيل صوتي نادر: 'لو محمد وصل للملف.. هيعرف كل حاجة'",
            "الخيانة الأولى" to "تسريب أسرار التحقيق للصحافة والشبهات تدور حول مروان أو ليلى",
            "شبكة الخلايا" to "اكتشاف البنية العنقودية للمرآة: لا خلية تعرف الأخرى",
            "مروان صبري" to "مواجهة مروان بحقيقة عمله السابق مع البنية البرمجية للمرآة"
        )
        ch2Titles.forEachIndexed { index, (title, sub) ->
            val caseNum = 11 + index
            list.add(
                CaseEntity(
                    id = caseNum,
                    chapter = 2,
                    title = title,
                    subtitle = sub,
                    description = "ضمن الفصل الثاني: المرآة تبدأ بالظهور وتتشابك علاقات محمد بالرائد عمر ومروان وليلى.",
                    client = if (caseNum == 13) "الرائد عمر الديب" else if (caseNum == 20) "مروان صبري" else "مكتب التحقيق",
                    location = if (caseNum == 20) "معمل مروان السري — الدقي" else "مصر الجديدة والقاهرة الفاطمية",
                    timeLabel = "06:00 م",
                    coldOpenNarrative = "خيوط المرآة تتمدد وتخترق الدوائر المقربة من محمد عبده.",
                    keyClueTitle = "دليل القضية رقم $caseNum المتقاطع مع المرآة",
                    mirrorClueSnippet = "رمز تشفير الخلية رقم 0${caseNum - 10}",
                    status = CaseStatus.LOCKED,
                    isKeyPlayableCase = caseNum == 13 || caseNum == 20
                )
            )
        }

        // Chapter 3: محمد يصبح جزءاً من القضية (21 - 30)
        val ch3Titles = listOf(
            "الهدف" to "محمد يكتشف أنه مراقب على مدار الساعة من شخص مجهول",
            "مكتب التحقيق" to "اقتحام ليلي لمكتب محمد بدون سرقة.. فقط إعادة ترتيب ملفاته!",
            "المرأة ذات المعطف" to "عميلة غامضة تظهر وتختفي وتحذر محمد من فخ قادم",
            "الملف الأسود" to "ملف توثيقي مريب يوضح سبب اختيار محمد بالتحديد لهذه القضايا",
            "القضية التي لم تحدث" to "جريمة مصطنعة بالكامل ومسرح وهمي لاستدراج محمد لفخ",
            "الفخ" to "أدلة مزروعة تجعل محمد المشتبه به الأول في جريمة قتل",
            "المطاردة" to "ملاحقة ساخنة لشاهد مفتاحي بين محطة مصر وسوق العتبة",
            "القطار" to "دليل داخل حقيبة في قطار الإسكندرية وساعة حاسمة للقرار",
            "الرجل الذي يعرف محمد" to "مشتبه به يقول: 'أنا كنت بدور عليك من 3 سنين مش أنت اللي بتدور عليا'",
            "الحقيقة الأولى" to "كشف اللغز: قضية محمد القديمة كانت أول تجربة ميدانية للمرآة!"
        )
        ch3Titles.forEachIndexed { index, (title, sub) ->
            val caseNum = 21 + index
            list.add(
                CaseEntity(
                    id = caseNum,
                    chapter = 3,
                    title = title,
                    subtitle = sub,
                    description = "ضمن الفصل الثالث: محمد عبده يتحول من محقق خارجي إلى محور الصراع والهدف الأساسي للمنظمة.",
                    client = "أحداث الصراع المباشر",
                    location = "العتبة ومحطة مصر وحلوان",
                    timeLabel = "10:30 م",
                    coldOpenNarrative = "المرآة تضيق الخناق على محمد وتجبره على الاختيار بين سمعته وحياته.",
                    keyClueTitle = "وثيقة ربط مباشرة بحياة محمد المهنية",
                    mirrorClueSnippet = "بصمة مشروع 'عين القاهرة' في رصد المستهدفين",
                    status = CaseStatus.LOCKED,
                    isKeyPlayableCase = caseNum == 26 || caseNum == 30
                )
            )
        }

        // Chapter 4: الخيانة والصراع (31 - 40)
        val ch4Titles = listOf(
            "الخائن" to "كشف خيانة حقيقية داخل الدائرة المقربة تعتمد على مسار الثقة",
            "ليلى نجيب" to "اختفاء ليلى وترك حقيبتها وفي داخلها وثائق خطيرة ضد شبكة المرآة",
            "عمر الديب" to "أمر رسمي يمنع الرائد عمر من التعاون مع محمد تحت طائلة التجريد",
            "د. سلمى" to "د. سلمى تعترف بتعديل تقرير طبي قديم تحت ابتزاز عائلي من المرآة",
            "المدينة المراقبة" to "استخدام شبكة كاميرات القاهرة السرية لتعقب تحركات قيادات الظل",
            "الاسم الحقيقي" to "اعتراف عضو بارز: المرآة مجرد واجهة.. الاسم الحقيقي 'مشروع عين القاهرة'",
            "يوسف كامل" to "فيديو مسجل يثبت أن يوسف كامل حي لكنه مقيد بخيار مستحيل",
            "آخر صحفي" to "اغتيال صحفي زميل ليوسف كان يملك خيط التسريب الحاسم",
            "الانقسام الداخلي" to "انشقاق أجنحة المرآة بين جناح يرغب في تصفية محمد وجناح يريد تجنيده",
            "الخيانة الكبرى" to "مواجهة حاسمة مع الشخصية الأكثر ولاءً واختبار الروابط الأخلاقية"
        )
        ch4Titles.forEachIndexed { index, (title, sub) ->
            val caseNum = 31 + index
            list.add(
                CaseEntity(
                    id = caseNum,
                    chapter = 4,
                    title = title,
                    subtitle = sub,
                    description = "ضمن الفصل الرابع: التفكك الداخلي واختبار ولاء الشخصيات الأساسية والوصول لعمق مشروع عين القاهرة.",
                    client = "أزمة الفريق",
                    location = "الزمالك وضفاف النيل والقلعة",
                    timeLabel = "02:00 ص",
                    coldOpenNarrative = "الشك ينهش الفريق.. من هو الصديق ومن هو زرعة المرآة؟",
                    keyClueTitle = "وثيقة انشقاق خلايا مشروع عين القاهرة",
                    mirrorClueSnippet = "ملف 'SUBJECT 01' يظهر كإشارة مشفرة",
                    status = CaseStatus.LOCKED,
                    isKeyPlayableCase = caseNum == 36 || caseNum == 40
                )
            )
        }

        // Chapter 5: النهاية الكبرى (41 - 50)
        val ch5Titles = listOf(
            "المدينة بلا ذاكرة" to "حذف السجلات الرسمية للأبرياء والتحكم في الحقيقة الرقمية",
            "الملف صفر (CASE 00)" to "العثور على الملف التأسيسي الأول الذي انطلقت منه الشبكة",
            "مؤسس المرآة" to "كشف هوية مؤسس المشروع والمفاجأة بأنه فقد السيطرة عليه",
            "من يدير المرآة؟" to "الصراع لمعرفة هوية القائد الجديد المجهول خلف الكواليس",
            "المواجهة مع يوسف" to "لقاء مباشر مع يوسف كامل: 'لو رجعت ناس كتير هتموت يا محمد'",
            "الحقيقة الكاملة" to "كشف خيانة التحقيق القديم وكيف تحول يوسف ومحمد إلى بيادق",
            "آخر شاهد" to "القرار المصيري: حماية الشاهد الأخير أم تسليمه أم استخدامه كطعم",
            "المرآة تنكسر" to "الهجوم المضاد: استخدام كافة الأدلة والشهادات لتفكيك الشبكة",
            "آخر خيط" to "تناقض مروع بين براءة يوسف وتورطه كشريك مؤسس.. من تصدق؟",
            "ملفات القاهرة — الموعد الأخير" to "الجلسة الختامية أمام لوحة التحقيق الـ 50 واختيار النهاية من بين النهايات الـ 7"
        )
        ch5Titles.forEachIndexed { index, (title, sub) ->
            val caseNum = 41 + index
            list.add(
                CaseEntity(
                    id = caseNum,
                    chapter = 5,
                    title = title,
                    subtitle = sub,
                    description = "الفصل الختامي: إغلاق الملفات الخمسين وتحديد مصير القاهرة ومحمد ويوسف عبر النهايات المتعددة.",
                    client = "مصير ملفات القاهرة",
                    location = "مكتب محمد عبده — وسط البلد",
                    timeLabel = "02:13 ص",
                    coldOpenNarrative = "50 قضية.. مئات الأدلة.. يوسف كامل أمام محمد.. والرائد عمر ينتظر القرار النهائي.. اللحظة التي تنتهي عندها كل الخيوط.",
                    keyClueTitle = "الملف الشامل لـ 50 قضية متصلة",
                    mirrorClueSnippet = "المفتاح النهائي لكشف هوية SUBJECT 01",
                    status = CaseStatus.LOCKED,
                    isKeyPlayableCase = caseNum == 49 || caseNum == 50
                )
            )
        }

        return list
    }

    private fun generateInitialSuspects(): List<SuspectEntity> {
        return listOf(
            SuspectEntity(
                id = "mohamed",
                name = "محمد عبده",
                roleTitle = "المحقق الخاص (31 سنة)",
                description = "ضابط شرطة سابق ترك الخدمة بعد قضية غامضة قبل 3 سنوات. ذكي، حذر، وتطارده رغبة عارمة في معرفة الحقيقة.",
                trustScore = 100,
                fearScore = 10,
                loyaltyScore = 100,
                secretsLevel = "سري للغاية",
                statusTag = "المحقق الأساسي",
                secretDossier = "ملفه القديم يحتوي على ثغرة تم التستر عليها لحمايته.. أو لتوريطه لاحقاً.",
                avatarEmoji = "🕵️"
            ),
            SuspectEntity(
                id = "marwan",
                name = "مروان صبري",
                roleTitle = "خبير التكنولوجيا ومساعد محمد",
                description = "شاب عبقري في الشبكات والتحليل الرقمي. يساعد محمد بإخلاص، لكن ماضيه يخفي تعاملاً مع شركة NILE DATA التابعة للمرآة.",
                trustScore = 78,
                fearScore = 35,
                loyaltyScore = 82,
                secretsLevel = "مرتفع",
                statusTag = "حليف مشكوك في ماضيه",
                secretDossier = "كان أحد المطورين للخوارزمية التي ركبت مقاطع الكاميرات في القضية الخامسة.",
                avatarEmoji = "💻"
            ),
            SuspectEntity(
                id = "leila",
                name = "ليلى نجيب",
                roleTitle = "صحفية استقصائية حرة",
                description = "صحفية شجاعة تبحث عن سر اختفاء يوسف كامل. علاقتها بمحمد متقلبة بين التحالف والشك والتعاون الوثيق.",
                trustScore = 72,
                fearScore = 21,
                loyaltyScore = 80,
                secretsLevel = "مرتفع",
                statusTag = "شريكة بحث",
                secretDossier = "تخفي آخر مكالمة استلمتها من هاتف يوسف قبل 48 ساعة فقط من القضية الأولى.",
                avatarEmoji = "📰"
            ),
            SuspectEntity(
                id = "omar",
                name = "الرائد عمر الديب",
                roleTitle = "ضابط تحقيقات مباحث العاصمة",
                description = "ضابط مهني ملتزم بالقانون بحذافيره. يحترم محمد لكنه مقيد بتعليمات عليا غامضة تدفعه أحياناً للصدام معه.",
                trustScore = 65,
                fearScore = 15,
                loyaltyScore = 70,
                secretsLevel = "حرج",
                statusTag = "شريك قانوني متردد",
                secretDossier = "اسمه مدرج في دفتر الأسماء القديم (القضية 13) كشخص تم تحييده بالترقية المشروطة.",
                avatarEmoji = "👮"
            ),
            SuspectEntity(
                id = "salma",
                name = "د. سلمى فؤاد",
                roleTitle = "طبيبة شرعية بمشرحة زينهم",
                description = "هادئة، دقيقة، تمثل العين العلمية لمحمد في فحص الجثث والسموم. تخفي سراً حول تقرير وفاة مشبوه تم التلاعب به.",
                trustScore = 85,
                fearScore = 40,
                loyaltyScore = 88,
                secretsLevel = "مرتفع",
                statusTag = "مصدر طبي موثوق",
                secretDossier = "تعرضت لابتزاز بقضية طبية قديمة تخص شقيقها من قبل خلية للمرآة.",
                avatarEmoji = "🔬"
            ),
            SuspectEntity(
                id = "saber",
                name = "عم صابر",
                roleTitle = "صاحب مقهى آخر الليل بالسيدة زينب",
                description = "رجل شعبي مرح المظهر، لكن مقهاه يمثل صرة المعلومات الليلية للعاصمة: من جاء ومن تشاجر ومن كان يراقب.",
                trustScore = 90,
                fearScore = 10,
                loyaltyScore = 95,
                secretsLevel = "متوسط",
                statusTag = "عين الشارع",
                secretDossier = "يعرف وجوه رجال المرآة الذين كانوا يرصدون مكتب محمد منذ 6 أشهر.",
                avatarEmoji = "☕"
            ),
            SuspectEntity(
                id = "youssef",
                name = "يوسف كامل",
                roleTitle = "الصحفي الاستقصائي المختفي",
                description = "المحرك الصامت لكل أحداث اللعبة. صوته يظهر في المكالمات والتسجيلات منذ اتصال 02:13 ص. ضحية أم مهندس الشبكة؟",
                trustScore = 45,
                fearScore = 80,
                loyaltyScore = 50,
                secretsLevel = "سري للغاية",
                statusTag = "الشبح المختفي",
                secretDossier = "هو أول من وثق مشروع عين القاهرة، لكنه اختار الاختفاء بعدما أدرك أنه مراقب.",
                avatarEmoji = "🎙️"
            ),
            SuspectEntity(
                id = "noura",
                name = "نورا حمدي",
                roleTitle = "شاكية القضية الأولى",
                description = "أخت الشاب المختفي بالشقة المقفولة. تتحدث بصدق وتملك مفتاحاً إضافياً يكشف باب خدمة خفي.",
                trustScore = 80,
                fearScore = 60,
                loyaltyScore = 75,
                secretsLevel = "منخفض",
                statusTag = "عميلة القضية 1",
                secretDossier = "شقيقها كان يحمل ملفاً رقمياً يحوي رمز M-01.",
                avatarEmoji = "👩"
            ),
            SuspectEntity(
                id = "witness_cleaner",
                name = "عامل النظافة (الشاهد)",
                roleTitle = "الشاهد الصامت بالقضية الرابعة",
                description = "عامل مسن يرتجف خوفاً على ابنه الوحيد. يملك رقم هاتف حاسم يربط خطف الشاب بالشقة المقفولة.",
                trustScore = 55,
                fearScore = 90,
                loyaltyScore = 60,
                secretsLevel = "حرج",
                statusTag = "شاهد مهدد",
                secretDossier = "إذا تمت حمايته من محمد يعود في القضية 14 و 31 و 48 كحليف منقذ.",
                avatarEmoji = "🧹"
            )
        )
    }

    private fun generateInitialEvidence(): List<EvidenceEntity> {
        return listOf(
            EvidenceEntity(
                id = "ev_01_clock",
                caseId = 1,
                title = "الساعة المتوقفة (11:47)",
                description = "ساعة حائط كلاسيكية تم إيقاف ترسها عمداً من الخلف بدقة، لتوثيق توقيت زمني محدد.",
                category = EvidenceCategory.PHYSICAL_OBJECT,
                phase = EvidencePhase.CONFIRMED,
                analysisInsight = "الترس تم حشوه بقطعة شمع مجهري تذوب بعد ساعة، ما يثبت نية تزييف توقيت الغياب.",
                linkedPersonId = "noura",
                isDiscovered = true,
                isConfirmedOnBoard = true
            ),
            EvidenceEntity(
                id = "ev_01_phone",
                caseId = 1,
                title = "هاتف الشقة المتروك",
                description = "هاتف محمول وُجد فوق طاولة السفرة، استقبل رسالة بعد 4 ساعات من وقت الاختفاء الرسمي.",
                category = EvidenceCategory.DIGITAL_RECORD,
                phase = EvidencePhase.ANALYSIS,
                analysisInsight = "الرسالة مرسلة عبر بوابة خادم وهمي يحمل عنوان بروتوكول مشفر في الدقي.",
                linkedPersonId = "marwan",
                isDiscovered = true
            ),
            EvidenceEntity(
                id = "ev_01_code_m01",
                caseId = 1,
                title = "قصاصة الورق M-01",
                description = "ورقة صغيرة مخبأة تحت إطار الباب الداخلي تحمل رمزاً مطبوعاً بحبر مائي: M-01.",
                category = EvidenceCategory.DOCUMENT,
                phase = EvidencePhase.CONFIRMED,
                analysisInsight = "أول خيط تنظيمي لمنظمة المرآة (Mirror Cell 01)، ويشير لبداية سلسلة العمليات.",
                linkedPersonId = "mohamed",
                isSecretMirrorClue = true,
                isDiscovered = true,
                isConfirmedOnBoard = true
            ),
            EvidenceEntity(
                id = "ev_02_photo",
                caseId = 2,
                title = "الصورة المسترجعة من كاميرا الحادث",
                description = "صورة ملتقطة قبل ثوانٍ من دهس المصور الصحفي خالد فهمي. في الركن المظلم يظهر يوسف كامل حياً!",
                category = EvidenceCategory.PHOTO,
                phase = EvidencePhase.ANALYSIS,
                analysisInsight = "د. سلمى ومروان أكدا عدم وجود تلاعب رقمي: يوسف كامل كان حياً بعد عامين من اختفائه.",
                linkedPersonId = "youssef",
                isDiscovered = true
            ),
            EvidenceEntity(
                id = "ev_03_nile_data",
                caseId = 3,
                title = "سجل شركة NILE DATA المنسي",
                description = "ملف استثماري ورقي من أرشيف 2019 يربط شركة نقل بيانات بتمويلات خارجية غامضة.",
                category = EvidenceCategory.DOCUMENT,
                phase = EvidencePhase.SEARCH,
                analysisInsight = "الشركة مسؤولة عن إدارة كاميرات خاصة في 14 نقطة حيوية بالقاهرة القديمة.",
                linkedPersonId = "marwan",
                isDiscovered = true
            ),
            EvidenceEntity(
                id = "ev_secret_subject01",
                caseId = 50,
                title = "وثيقة 'PROJECT MIRROR — SUBJECT 01'",
                description = "وثيقة شديدة السرية تم العثور على أجزائها في الأماكن المهملة والمكالمات الجانبية.",
                category = EvidenceCategory.DOCUMENT,
                phase = EvidencePhase.OBSERVATION,
                analysisInsight = "الصورة المرفقة بالملف تعود لمحمد عبده وهو شاب صغير قبل التحاقه بالشرطة.. هو الموضوع رقم 01!",
                linkedPersonId = "mohamed",
                isSecretMirrorClue = true,
                isDiscovered = false
            )
        )
    }

    private fun generateInitialEncounters(): List<EncounterEntity> {
        return listOf(
            EncounterEntity(
                id = "enc_taxi",
                district = "وسط البلد — ميدان طلعت حرب",
                characterName = "سائق التاكسي عم إبراهيم",
                characterRole = "سائق تاكسي أبيض",
                dialogueText = "يا باشا أنا فاكر الزبون ده.. ركب معايا الساعة 11:30 م من قدام عمارة اللواء وكان معاه شنطة جلد وقالي اطلع على النيل.",
                hiddenClueUnlocked = "خيط تحرك شقيق نورا باتجاه مرسى الزمالك قبل اختفائه",
                relatedCaseId = 1,
                isVisited = true
            ),
            EncounterEntity(
                id = "enc_newsstand",
                district = "الزمالك — شارع البرازيل",
                characterName = "عم جلال بائع الجرائد",
                characterRole = "بائع جرائد عتيق",
                dialogueText = "كل يوم الساعة سبعة الصبح، يجي شاب معاه نضارة يشتري جورنال الأهرام ويكتب أرقام على الهامش باللون الأحمر ويمشي علطول.",
                hiddenClueUnlocked = "شفرة إعلانات الجرائد السرية التي تستخدمها المرآة للتواصل",
                relatedCaseId = 2,
                isVisited = false
            ),
            EncounterEntity(
                id = "enc_doorman",
                district = "العباسية — عمارة الأوقاف",
                characterName = "عم عوض بواب العمارة",
                characterRole = "حارس عقار الدور السابع",
                dialogueText = "شقة الدور السابع دي مقفولة رسمياً من سنة، بس كل أسبوع يجي فني كمبيوتر معاه شنطة كبيرة ومحدش بيشوف وشه أبداً.",
                hiddenClueUnlocked = "موعد الزيارة الدورية لخادم المراقبة في الدور السابع",
                relatedCaseId = 7,
                isVisited = false
            ),
            EncounterEntity(
                id = "enc_cafe_saber",
                district = "السيدة زينب — حارة الحناوي",
                characterName = "صباح الجرسون بمقهى صابر",
                characterRole = "شاب يخدم رواد المقهى",
                dialogueText = "الراجل اللي اختفى كان بيقعد دايماً في الركن اللي في وش الشارع، وكان بيسجل نمر العربيات اللي بتقف قدام صيدلية الأمل.",
                hiddenClueUnlocked = "قائمة بأرقام سيارات المراقبة التابعة لمشروع عين القاهرة",
                relatedCaseId = 6,
                isVisited = false
            ),
            EncounterEntity(
                id = "enc_nile_fisherman",
                district = "المعادي — ضفاف النيل",
                characterName = "الريس رجب الصياد",
                characterRole = "صياد نهري ليلي",
                dialogueText = "الساعة اتنين بالليل شفت لانش سريع رمى حاجة تقيلة في المية قرب الجزيرة، وقبل ما يتحرك سمعت صوت تليفون بيرن بنغمة قديمة.",
                hiddenClueUnlocked = "موقع إلقاء هاتف مبرمج شركة نايل داتا في النيل",
                relatedCaseId = 9,
                isVisited = false
            )
        )
    }

    private fun generateInitialEndings(): List<EndingEntity> {
        return listOf(
            EndingEntity(
                id = 1,
                title = "النهاية 1 — العدالة",
                subtitle = "تسليم الملفات الكاملة للقضاء والشرطة",
                description = "محمد يسلم كل ما جمعه من أدلة للرائد عمر والنيابة العامة. تنهار منظمة المرآة قانونياً، لكن بعض الأبرياء يتضررون نتيجة نشر الوثائق للعامة.",
                requirementText = "العدالة ≥ 25 + حل القضية 50 بمسار القانون",
                isUnlocked = false
            ),
            EndingEntity(
                id = 2,
                title = "النهاية 2 — الحقيقة",
                subtitle = "كشف شبكة المرآة عبر وسائل الإعلام للرأي العام",
                description = "محمد وليلى ينشرون كل شيء في تحقيق صحفي مدوٍ يهز القاهرة. المدينة تعرف الحقيقة، لكن محمد يصبح مطارداً ومعرضاً للخطر الدائم.",
                requirementText = "التعاطف ≥ 20 + ولاء ليلى نجيب ≥ 75",
                isUnlocked = false
            ),
            EndingEntity(
                id = 3,
                title = "النهاية 3 — الصفقة",
                subtitle = "عقد اتفاق سري مع قيادة المرآة للمساومة",
                description = "محمد يعقد صفقة مع أحد قادة المرآة. يحصل على معلومات سرية لتأمين أصدقائه، لكن جزءاً كبيراً من الحقيقة يظل مدفوناً في الظلام.",
                requirementText = "الحذر ≥ 22 + مساومة قيادة الخلية 00",
                isUnlocked = false
            ),
            EndingEntity(
                id = 4,
                title = "النهاية 4 — الانتقام",
                subtitle = "سحق المتورطين باستخدام ملفات الابتزاز الخاصة بهم",
                description = "محمد يستخدم أدلة المرآة لتدمير كل من تورط في تشويه مسيرته واختطاف الأبرياء. ينجح في القضاء عليهم، لكنه يتحول تدريجياً إلى وحش يشبه من حاربهم.",
                requirementText = "الهوس ≥ 28 + التعاطف ≤ 8",
                isUnlocked = false
            ),
            EndingEntity(
                id = 5,
                title = "النهاية 5 — التضحية",
                subtitle = "تحمل المسؤولية الجنائية لحماية الشركاء والأبرياء",
                description = "محمد يتبنى بعض التهم لحماية مروان وليلى وعامل النظافة. يساق إلى السجن بكرامة بعدما أسقط رؤوس المرآة الكبرى.",
                requirementText = "التعاطف ≥ 30 + العدالة ≥ 20",
                isUnlocked = false
            ),
            EndingEntity(
                id = 6,
                title = "النهاية 6 — حماية يوسف",
                subtitle = "إخفاء يوسف كامل في ملاذ آمن للأبد",
                description = "محمد يختار تصديق يوسف وإخفائه خارج حدود مصر. يوسف يختفي مجدداً ويبقى أثر صغير من شبكة المرآة كامناً تحت الرماد.",
                requirementText = "الثقة بيوسف كامل ≥ 70 في القضية 49",
                isUnlocked = false
            ),
            EndingEntity(
                id = 7,
                title = "النهاية 7 — المرآة",
                subtitle = "تولي محمد عبده قيادة المنظومة لإصلاحها من الداخل",
                description = "محمد يجلس في مكتبه الجديد. الهاتف يرن: 'عندنا ملف جديد يا فندم'. محمد يبتسم بهدوء.. السيطرة على الشبكة أفضل من تركها للأسوأ.",
                requirementText = "الهوس ≥ 25 + الحذر ≥ 20 + العدالة ≤ 12",
                isUnlocked = false
            ),
            EndingEntity(
                id = 8,
                title = "النهاية السرية — آخر خيط (SUBJECT 01)",
                subtitle = "اكتشاف الحقيقة الصادمة: محمد عبده هو النواة الأولى",
                description = "عند جمع كافة الأدلة المخفية في القاهرة، تظهر صورة قديمة تعود لعمر أصغر لمحمد عبده: 'PROJECT MIRROR — SUBJECT 01'. صوت يوسف يهمس: 'دلوقتي فهمت ليه اخترتك يا محمد'.",
                requirementText = "جمع كل الأدلة السرية الـ 5 وزيارة كل لقاءات القاهرة",
                isUnlocked = false,
                isSecretEnding = true
            )
        )
    }
}
