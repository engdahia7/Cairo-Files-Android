package com.example.data.model

object SolvedCaseArchiveData {

    /**
     * قضايا محلولة أولية من أرشيف مباحث القاهرة الخديوية ١٩٤٨
     * لتكون مرجعاً تاريخياً أولياً للمحقق عند بدء اللعبة.
     */
    fun getInitialHistoricalArchives(): List<SolvedCaseArchiveEntity> {
        return listOf(
            SolvedCaseArchiveEntity(
                caseId = 101, // قضية أرشيفية مرجعية
                caseTitle = "قضية قتيل لوكاندة شبرد السرية",
                caseSubtitle = "تسمم بمركب السيانيد • سرقة وثائق قناة السويس",
                chapter = 1,
                crimeDate = "١٢ مايو ١٩٤٧",
                location = "القاهرة - لوكاندة شبرد الكبرى، الغرفة ٢٠٤",
                culpritName = "أندريه ديمتريوس (تاجر أقطان ومخبر سري)",
                culpritRole = "عميل مزدوج لشبكة تجسس دولية",
                culpritMotive = "ابتزاز المندوب السامي البريطاني بوثائق سرية تتعلق بامتياز حفر قناة السويس وتسليح الجيش المصري.",
                culpritConfession = "«لم أكن أنوي قتله في غرفته، لكنه حاول الصراخ واستدعاء أمن اللوكاندة حين اكتشف أنني استبدلت ملف الأوراق بالحقيبة، فوضعت مسحوق السيانيد في قدح الشاي وتركت الغرفة عبر شرفة الحديقة.»",
                sentenceVerdict = "الإعدام شنقاً حتى الموت بأمر محكمة جنايات عابدين المختلطة",
                leadingEvidenceSummary = "١. فنجان شاي خزفي به ترسبات بيضاء (حمض الهيدروسيانيك).\n٢. ولاعة فضية محفور عليها الحرفان (A.D) سقطت تحت المقعد المخملي.\n٣. تذكرة قطار الصعيد السريع باسم مستعار عُثر عليها في جيب معطفه.",
                forensicProofDetails = "أثبت تقرير الطب الشرعي بمشرحة قصر العيني أن الوفاة حدثت نتيجة شلل فوري في المراكز التنفسية بفعل السيانيد. كما طابقت مصلحة الأدلة الجنائية بصمات الإبهام على الفنجان مع سجلات المراقبة الخاصة بالمتهم.",
                deductionSummary = "ربط المحقق بين توقيت شرب الشاي وشهادة بواب اللوكاندة الذي شاهد خروج رجل يرتدي معطفاً رمادياً قبيل منتصف الليل بعشر دقائق، مما دحض ادعاء المتهم بوجوده في الإسكندرية.",
                officialStampCode = "ج-١٩٤٧/١٠١-مُغلق",
                ratingStars = 5,
                justiceScoreEarned = 35,
                investigatorNotes = "ملف كلاسيكي يُدرس في مدرسة البوليس: دائماً ما يترك الجاني المتعجل أثراً شخصياً في مسرح الجريمة المغلق."
            ),
            SolvedCaseArchiveEntity(
                caseId = 102, // قضية أرشيفية مرجعية ثانية
                caseTitle = "قضية تزوير أختام مصلحة المساحة والدمغة",
                caseSubtitle = "تزييف سندات ملكية أراضي الأوقاف في حلوان",
                chapter = 1,
                crimeDate = "٢٨ نوفمبر ١٩٤٧",
                location = "القاهرة - خان الخليلي وشارع المعز",
                culpritName = "الشيخ منصور الحبال (خطاط ونحات أختام)",
                culpritRole = "صانع الأختام المزيفة لشبكات السمسرة العقارية",
                culpritMotive = "تحقيق مكاسب مالية طائلة عبر تزوير حجج ملكية مئات الأفدنة من أراضي الأوقاف الخديوية بحلوان وبيعها لشركات التطوير.",
                culpritConfession = "«استخدمت مجهراً سويسرياً ومحلول الزنك المركز لنحت خريطة التضاريس والختم البيضاوي لمصلحة المساحة الملكية بدقة لا يمكن لعين موظف الشباك تفريقها.»",
                sentenceVerdict = "الأشغال الشاقة لمدة ١٥ سنة ومصادرة جميع أدوات النحت والسبائك",
                leadingEvidenceSummary = "١. قالب شمعي به بقايا حبر أزرق بروسي خاص بديوان المساحة.\n٢. سند ملكية أرض حلوان وبه تذبذب في انحناء حرف (الميم) في الختم الملكي.\n٣. مجهر ألماني الصنع وأحبار كيميائية مسرطنة وراء جدار ورشته بالخان.",
                forensicProofDetails = "فحصت مصلحة التزييف والتزوير خطوط الختم وثبت أن الزوايا تم حفرها بواسطة مبرد ميكانيكي وليس بالصب الرسمي لدار سك العملة المصرية.",
                deductionSummary = "كشف المحقق الاختلاف الطفيف في تركيز كبريتات النحاس في الحبر الأزرق، وهو مركب لم تستخدمه الحكومة المصرية قط في سجلاتها الرسمية.",
                officialStampCode = "ج-١٩٤٧/١٠٢-مُغلق",
                ratingStars = 5,
                justiceScoreEarned = 30,
                investigatorNotes = "درس مستفاد: لا يوجد تزوير كامل مهما بلغت مهارة الصانع، فالمواد الكيميائية لا تكذب."
            )
        )
    }

    /**
     * إنشاء سجل أرشيفي جنائي متكامل لقضية تم حلها في اللعبة بناءً على بياناتها
     */
    fun buildArchiveForCase(
        caseEntity: CaseEntity,
        evidenceList: List<EvidenceEntity>,
        suspects: List<SuspectEntity>
    ): SolvedCaseArchiveEntity {
        val puzzle = ForensicPuzzleRegistry.getPuzzleForCase(caseEntity.id)
        val correctHypothesis = puzzle.hypotheses.firstOrNull { it.isCorrect }
        val correctClues = puzzle.triangulationClues.filter { it.isCorrect }

        // تحديد اسم المتهم والدور بناءً على القضية
        val (culpritName, culpritRole, culpritMotive, confession, sentence) = when (caseEntity.id) {
            1 -> CulpritDetails(
                name = "عزت الدرملي",
                role = "المراقب السري لفرع شبكة المرآة بوسط البلد",
                motive = "إخفاء ميكروفيلم وثائق الدفاع الوطني وتهريب المتعاونين قبل مداهمة البوليس السياسي.",
                confession = "«كنت أراقب عمارة الإيموبيليا من السيارة البيضاء، وعندما أحسست باقتراب مأمور التحريات أعطيت إشارة شفرة زنبرك الساعة M-01 وأمّنت هروبه عبر منور الخدمة الخلفي.»",
                sentence = "السجن المؤبد مع الشغل والنفاذ بسجن طرة بتهمة التخابر وتعطيل سير العدالة."
            )
            2 -> CulpritDetails(
                name = "رفعت القبانجي",
                role = "أمين وثائق ومسؤول أرشيف شركة النيل للتجارة",
                motive = "بيع إحداثيات مخازن السلاح السرية للوسطاء في روكسي.",
                confession = "«قمت بنسخ الأشرطة السرية وتخبئتها داخل صندوق الكاميرا المدمجة بالسيارة قبل أن تضبطني المباحث.»",
                sentence = "السجن المشدد ١٠ سنوات وتجريده من رتبه الوظيفية."
            )
            3 -> CulpritDetails(
                name = "مدام فالنتينا ليفينسكاي",
                role = "مديرة صالون بار كابريس وعميلة سرية",
                motive = "استدراج الشخصيات العامة وابتزازهم بملفات سرية.",
                confession = "«الصالون كان محطة استقبال الشفرات اللاسلكية، والتسجيلات كانت تُخبأ خلف مرآة قاعة الاستقبال.»",
                sentence = "المصادرة الكاملة للمنشأة والإبعاد الفوري عن الأراضي المصرية بعد قضاء ٧ سنوات حبس."
            )
            4 -> CulpritDetails(
                name = "الأسطى محروس الجوهري",
                role = "سائق قطار خط الصعيد ومهرّب محترف",
                motive = "الحصول على مبالغ مالية لتهريب حقائب الوثائق بين المحطات.",
                confession = "«كنت ألقي بالحقيبة الجلدية عند الكيلو ٤٢ قبل دخول محطة بني سويف ليتسلمها رجال الشبكة.»",
                sentence = "السجن المشدد ٨ سنوات مع العزل من الخدمة بالسكة الحديد."
            )
            5 -> CulpritDetails(
                name = "الدكتور شريف المنشاوي",
                role = "كيميائي بمختبرات معامل باب اللوق",
                motive = "تجهيز أحبار سريّة ومركبات تخدير لصالح عناصر مجهولة.",
                confession = "«طلبوا مني تركيب حبر يختفي بعد ٢٤ ساعة مقابل تمويل أبحاثي الخاصة، ولم أكن أعلم أنه سيُستخدم في الاغتيالات.»",
                sentence = "السجن المؤبد مع الأشغال الشاقة."
            )
            else -> CulpritDetails(
                name = "عنصر مجهول تم تتبعه واعتقاله (الرمز: C-${caseEntity.id})",
                role = "عضو خلايا شبكة المرآة في ${caseEntity.location}",
                motive = "تنفيذ عمليات التضليل والتشويش على أجهزة التحري ومحاولة تعطيل قضية يوسف كامل.",
                confession = "«اعترفت بتفاصيل خطة التضليل بعد مواجهتي بالأدلة الجنائية التي وثّقها مأمور الضبط القضائي.»",
                sentence = "السجن المشدد مع الأشغال الشاقة بأمر محكمة الجنايات."
            )
        }

        val leadingEvidenceText = if (correctClues.isNotEmpty()) {
            correctClues.mapIndexed { idx, c -> "${idx + 1}. ${c.title}: ${c.description}" }.joinToString("\n")
        } else {
            "١. ${caseEntity.keyClueTitle}\n٢. ${caseEntity.mirrorClueSnippet}\n٣. تطابق مسرح الجريمة مع شهادات الشهود"
        }

        val forensicProof = "فحصت الأدلة الجنائية المعثور عليها في ${caseEntity.location} وثبت قطعية صلتها بالمتهم عبر تطابق البصمات، وفك شفرات المراسلات السرية، مما أسقط كافة الدفوع بالبراءة وألزم النيابة بتوجيه الاتهام المباشر."

        val deduction = correctHypothesis?.explanation ?: caseEntity.outcomeSummary.ifBlank {
            "تمكن المحقق ببراعة من كشف ملابسات الحادث وتفنيد المزاعم الباطلة وربط الخيوط الجنائية حتى إغلاق ملف القضية."
        }

        val stampCode = String.format("ج-١٩٤٨/%02d-مُغلق", caseEntity.id)

        return SolvedCaseArchiveEntity(
            caseId = caseEntity.id,
            caseTitle = caseEntity.title,
            caseSubtitle = caseEntity.subtitle,
            chapter = caseEntity.chapter,
            crimeDate = caseEntity.timeLabel,
            location = caseEntity.location,
            culpritName = culpritName,
            culpritRole = culpritRole,
            culpritMotive = culpritMotive,
            culpritConfession = confession,
            sentenceVerdict = sentence,
            leadingEvidenceSummary = leadingEvidenceText,
            forensicProofDetails = forensicProof,
            deductionSummary = deduction,
            officialStampCode = stampCode,
            ratingStars = if (caseEntity.ratingStars > 0) caseEntity.ratingStars else 5,
            justiceScoreEarned = 25,
            investigatorNotes = "أُحيل ملف الدعوى لمحكمة الجنايات وحُفظت صورة رسمية بالدفتر المرجعي لوزارة الداخلية."
        )
    }

    private data class CulpritDetails(
        val name: String,
        val role: String,
        val motive: String,
        val confession: String,
        val sentence: String
    )
}
