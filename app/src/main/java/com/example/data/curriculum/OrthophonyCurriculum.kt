package com.example.data.curriculum

enum class DifficultyLevel(
    val labelArabic: String,
    val subtitleArabic: String,
    val levelOrder: Int
) {
    BEGINNER("مبتدئ", "الصوت المعزول والمقاطع الصوتية", 1),
    INTERMEDIATE("متوسط", "الكلمات حسب موضع الحرف وتقطيعها", 2),
    ADVANCED("متقدم", "الجمل التفاعلية والتتابع الصوتي", 3)
}

enum class DevelopmentalStage(
    val titleArabic: String,
    val subtitleArabic: String,
    val shortBadge: String
) {
    STAGE_EARLY(
        "المرحلة المبكرة (2 - 3.5 سنوات)",
        "الأصوات الشفوية واللثوية الأساسية (ب، ف، ل)",
        "2 - 3.5 سنوات"
    ),
    STAGE_MIDDLE(
        "المرحلة المتوسطة (3.5 - 5 سنوات)",
        "أصوات الصفير والغار والطبق (س، ش، ك، ج)",
        "3.5 - 5 سنوات"
    ),
    STAGE_LATE(
        "المرحلة المتقدمة (5 - 7 سنوات)",
        "الأصوات التكرارية واللهوية والحلقية (ر، ق، ع)",
        "5 - 7 سنوات"
    )
}

enum class AnimationTheme(
    val titleArabic: String,
    val instructionArabic: String
) {
    SNAKE_TRAIN(
        "قطار الصفير وثعبان السين",
        "انقر على البطاقات أو انطق في الميكروفون لتحريك قطار الصفير وتفجير فقاعات السين!"
    ),
    OCEAN_PEARL(
        "أمواج البحر ولؤلؤة الشين",
        "أصدر صوت الموج الدافئ (ششش) لتفتح الصدفة البحرية وتجمع اللآلئ المضيئة!"
    ),
    RACE_CAR(
        "سيارة السباق السريعة للراء",
        "حرّك طرف لسانك كالمحرك (ررر) لتنطلق سيارة السباق وتجمع النجوم الذهبية!"
    ),
    MUSICAL_BIRD(
        "عصفور الألحان السعيد للام",
        "ارفع طرف اللسان خلف الأسنان العليا وغنِّ مع عصفور الألحان فوق الأغصان!"
    ),
    STAR_ROCKET(
        "صاروخ النجوم الإيقاعي",
        "انطق المقاطع والكلمات بوضوح ليحلق الصاروخ بين الكواكب المضيئة!"
    )
}

enum class TonguePlacementType(val arabicTitle: String) {
    ALVEOLAR_GROOVE_SEEN("مجرى هوائي دقيق خلف الأسنان السفلى مع إغلاق الأسنان (س)"),
    PALATAL_WIDE_SHEEN("رفع وسط اللسان نحو غار الحنك مع ضم خفيف للشفاه (ش)"),
    ALVEOLAR_TRILL_RAA("طرقات اهتزازية لطرف اللسان خلف اللثة العليا (ر)"),
    ALVEOLAR_LATERAL_LAAM("ارتكاز طرف اللسان على اللثة العليا ومرور الهواء جانبياً (ل)"),
    VELAR_BACK_KAAF_QAAF("ارتفاع مؤخرة اللسان نحو الطبق اللين أو اللهاة (ك / ق)"),
    BILABIAL_LABIODENTAL("انطباق الشفتين أو تلامس الشفة السفلى مع الأسنان العليا (ب / ف)"),
    PHARYNGEAL_AYN("انقباض جدار البلعوم مع جهر الأوتار الصوتية (ع / ج)")
}

data class PhonemeInfo(
    val letter: String,
    val nameArabic: String,
    val phoneticCategory: String,
    val developmentalStage: DevelopmentalStage,
    val tonguePlacementType: TonguePlacementType,
    val tonguePositionGuide: String,
    val lipShapeGuide: String,
    val airflowGuide: String,
    val commonErrorCorrection: String,
    val soundCueOnomatopoeia: String,
    val animationTheme: AnimationTheme
)

data class InteractivePromptItem(
    val textArabic: String,
    val syllabicBreakdown: String,
    val positionTag: String, // "مقطع", "أول الكلمة", "وسط الكلمة", "آخر الكلمة", "جملة", "ثنائية"
    val visualCueLabel: String,
    val helperTip: String
)

data class InteractiveExercise(
    val id: String,
    val phonemeLetter: String,
    val phonemeName: String,
    val title: String,
    val subtitle: String,
    val difficulty: DifficultyLevel,
    val developmentalStage: DevelopmentalStage,
    val animationTheme: AnimationTheme,
    val tonguePlacementType: TonguePlacementType,
    val clinicalGoal: String,
    val therapistTip: String,
    val items: List<InteractivePromptItem>
)

data class OralMotorExercise(
    val id: String,
    val title: String,
    val targetOrgan: String, // "اللسان", "الشفاه", "الخدود والفك"
    val durationSeconds: Int,
    val repetitions: Int,
    val childFriendlyMetaphor: String,
    val stepByStepInstructions: List<String>,
    val helpsWithPhonemes: List<String>
)

data class MinimalPairChallenge(
    val id: String,
    val targetContrastLabel: String, // e.g., "س / ش" or "ر / ل"
    val developmentalStage: DevelopmentalStage,
    val promptQuestion: String,
    val correctWord: String,
    val correctSyllables: String,
    val correctPhoneme: String,
    val correctMeaningHint: String,
    val distractorWord: String,
    val distractorSyllables: String,
    val distractorPhoneme: String,
    val distractorMeaningHint: String,
    val clinicalExplanation: String
)

data class AacCardItem(
    val id: String,
    val wordArabic: String,
    val category: String, // "الضمائر", "الأفعال", "الاحتياجات والأشياء", "المشاعر والمهذبات"
    val colorRole: String
)

object OrthophonyCurriculum {

    val phonemeCatalog: List<PhonemeInfo> = listOf(
        PhonemeInfo(
            letter = "س",
            nameArabic = "حرف السين",
            phoneticCategory = "لثوي صفيري مهموس",
            developmentalStage = DevelopmentalStage.STAGE_MIDDLE,
            tonguePlacementType = TonguePlacementType.ALVEOLAR_GROOVE_SEEN,
            tonguePositionGuide = "طرف اللسان يستقر خلف الأسنان الأمامية السفلى مع تكوين مجرى وسطي ضيق يمر منه الهواء.",
            lipShapeGuide = "الشفاه في وضع ابتسامة خفيفة والأسنان منطبقة فوق بعضها دون خروج اللسان.",
            airflowGuide = "تيار هوائي مستمر بارد يخرج من وسط الأسنان الأمامية (سسسس).",
            commonErrorCorrection = "لعلاج اللثغة الأمامية (إبدال السين ثاء): اطلب من الطفل إغلاق أسنانه كالسياج وإبقاء اللسان داخل البيت خلف الأسنان السفلى.",
            soundCueOnomatopoeia = "سسسس — صوت الصفير الهادئ",
            animationTheme = AnimationTheme.SNAKE_TRAIN
        ),
        PhonemeInfo(
            letter = "ش",
            nameArabic = "حرف الشين",
            phoneticCategory = "غاري احتكاكي متفشٍّ مهموس",
            developmentalStage = DevelopmentalStage.STAGE_MIDDLE,
            tonguePlacementType = TonguePlacementType.PALATAL_WIDE_SHEEN,
            tonguePositionGuide = "يرتفع وسط ومقدمة اللسان نحو غار الحنك الأعلى دون ملامسته التامة مع رجوع بسيط عن مخرج السين.",
            lipShapeGuide = "استدارة وضم خفيف للشفاه للأمام لتفخيم انتشار الهواء.",
            airflowGuide = "تيار هوائي دافئ ومنتشر (التفشي) يتوزع في الفم (شششش).",
            commonErrorCorrection = "للتفريق بين السين والشين: ضع ظهر اليد أمام فم الطفل ليلاحظ الهواء الدافئ المنتشر في (ش) مع ضم الشفتين مقارنة بالهواء البارد الرفيع في (س).",
            soundCueOnomatopoeia = "شششش — صوت أمواج البحر والنسيم",
            animationTheme = AnimationTheme.OCEAN_PEARL
        ),
        PhonemeInfo(
            letter = "ر",
            nameArabic = "حرف الراء",
            phoneticCategory = "لثوي تكراري مجهور",
            developmentalStage = DevelopmentalStage.STAGE_LATE,
            tonguePlacementType = TonguePlacementType.ALVEOLAR_TRILL_RAA,
            tonguePositionGuide = "يرتفع طرف اللسان المرن ليقرع اللثة العليا خلف الأسنان الأمامية العليا عدة طرقات سريعة خفيفة.",
            lipShapeGuide = "الشفاه منفرجة بشكل طبيعي والفك السفلي مسترخٍ للسماح بحركة اللسان.",
            airflowGuide = "تيار هوائي قوي يحرك طرف اللسان المرن مع اهتزاز الأوتار الصوتية (رررر).",
            commonErrorCorrection = "لعلاج اللثغة اللامية (ر -> ل) أو الغينية (ر -> غ): ابدأ بالتدريب المقطعي المساعد (دَ-دَ-دَ سريعة ثم دْرَ / تْرَ / نْرَ) مع رفع طرف اللسان فقط ومنع مؤخرة اللسان من الارتفاع.",
            soundCueOnomatopoeia = "رررر — صوت محرك سيارة السباق",
            animationTheme = AnimationTheme.RACE_CAR
        ),
        PhonemeInfo(
            letter = "ل",
            nameArabic = "حرف اللام",
            phoneticCategory = "لثوي جانبي مجهور",
            developmentalStage = DevelopmentalStage.STAGE_EARLY,
            tonguePlacementType = TonguePlacementType.ALVEOLAR_LATERAL_LAAM,
            tonguePositionGuide = "يلتصق طرف اللسان بثبات خلف الأسنان العليا مباشرة (على اللثة العليا).",
            lipShapeGuide = "الشفاه مفتوحة باعتدال بحيث يظهر ارتفاع اللسان للطفل في المرآة.",
            airflowGuide = "يمر الصوت والهواء من جانبي اللسان الأيمن والأيسر مع اهتزاز الأوتار الصوتية (لَا لَا).",
            commonErrorCorrection = "إذا كان الطفل يبدل اللام ياءً (ل -> ي): شجعه على لمس النقطة السحرية خلف الأسنان العليا بطرف اللسان بمساعدة خافض لسان أو قطرة عسل.",
            soundCueOnomatopoeia = "لَا لَا لَا — نغمة عصفور الألحان",
            animationTheme = AnimationTheme.MUSICAL_BIRD
        ),
        PhonemeInfo(
            letter = "ك",
            nameArabic = "حرف الكاف",
            phoneticCategory = "طبقي انفجاري مهموس",
            developmentalStage = DevelopmentalStage.STAGE_MIDDLE,
            tonguePlacementType = TonguePlacementType.VELAR_BACK_KAAF_QAAF,
            tonguePositionGuide = "ينخفض طرف اللسان خلف الأسنان السفلى بينما ترتفع مؤخرة اللسان لتلامس الحنك اللين (الطبق).",
            lipShapeGuide = "الفم مفتوح بوضوح لرؤية انخفاض مقدمة اللسان.",
            airflowGuide = "انحباس لحظي للهواء في أقصى الفم يليه انفجار هوائي خفيف (كْـ - كَ).",
            commonErrorCorrection = "لعلاج الاستبدال الأمامي (ك -> ت مثل: تتاب بدل كتاب): ثبت طرف اللسان برفق خلف الأسنان السفلى ليضطر الطفل لرفع أقصى اللسان.",
            soundCueOnomatopoeia = "كَ كَ — نقرات الإيقاع السعيد",
            animationTheme = AnimationTheme.STAR_ROCKET
        ),
        PhonemeInfo(
            letter = "ق",
            nameArabic = "حرف القاف",
            phoneticCategory = "لهوي انفجاري مفخم",
            developmentalStage = DevelopmentalStage.STAGE_LATE,
            tonguePlacementType = TonguePlacementType.VELAR_BACK_KAAF_QAAF,
            tonguePositionGuide = "يرتفع أقصى اللسان ليلامس اللهاة وأقصى الحنك اللين.",
            lipShapeGuide = "انفتاح الفم عمودياً لتفخيم الصوت.",
            airflowGuide = "حبس الهواء في أقصى الحلق ثم إطلاقه بقوة دون همس.",
            commonErrorCorrection = "للتفريق بين (ق) و(ك) أو (ت): تدرب على التثاؤب والغرغرة الخفيفة لإحساس الطفل بحركة أقصى اللسان واللهاة.",
            soundCueOnomatopoeia = "قَ قُ — دقات الطبل القوية",
            animationTheme = AnimationTheme.STAR_ROCKET
        ),
        PhonemeInfo(
            letter = "ج",
            nameArabic = "حرف الجيم",
            phoneticCategory = "غاري مركب مجهور",
            developmentalStage = DevelopmentalStage.STAGE_MIDDLE,
            tonguePlacementType = TonguePlacementType.PHARYNGEAL_AYN,
            tonguePositionGuide = "يلامس وسط اللسان غار الحنك الأعلى انغلاقاً ثم ينفرج باحتكاك مجهور.",
            lipShapeGuide = "الشفاه متعادلة مع انفتاح بسيط بين الفكين.",
            airflowGuide = "حبس يتبعه احتكاك صوتي مجهور واضح.",
            commonErrorCorrection = "إذا أبدل الطفل الجيم دالاً (دمل بدل جمل): دربه على رفع وسط اللسان كما في حرف الياء مع الدال (دْيَ -> جَ).",
            soundCueOnomatopoeia = "جَ جُ جِ — جرس القطار",
            animationTheme = AnimationTheme.STAR_ROCKET
        ),
        PhonemeInfo(
            letter = "ف",
            nameArabic = "حرف الفاء",
            phoneticCategory = "شفوي أسناني احتكاكي مهموس",
            developmentalStage = DevelopmentalStage.STAGE_EARLY,
            tonguePlacementType = TonguePlacementType.BILABIAL_LABIODENTAL,
            tonguePositionGuide = "اللسان مسترخٍ في قاع الفم لا يشارك في الإغلاق.",
            lipShapeGuide = "تلامس أطراف الأسنان الأمامية العليا باطن الشفة السفلى برفق (حركة الأرنب اللطيف).",
            airflowGuide = "مرور الهواء المستمر بين الشفة السفلى والأسنان العليا (فففف).",
            commonErrorCorrection = "استخدم لعبة نفخ الريشة مع وضع الأسنان العليا على الشفة السفلى.",
            soundCueOnomatopoeia = "فففف — نفخة الهواء اللطيفة",
            animationTheme = AnimationTheme.SNAKE_TRAIN
        ),
        PhonemeInfo(
            letter = "ب",
            nameArabic = "حرف الباء",
            phoneticCategory = "شفوي انفجاري مجهور",
            developmentalStage = DevelopmentalStage.STAGE_EARLY,
            tonguePlacementType = TonguePlacementType.BILABIAL_LABIODENTAL,
            tonguePositionGuide = "اللسان مستقر في قاع الفم.",
            lipShapeGuide = "انطباق الشفتين معاً بإحكام ثم فتحهما فجأة (فرقعة الفقاعة).",
            airflowGuide = "حبس الهواء خلف الشفتين ثم إطلاقه مع اهتزاز الأوتار الصوتية.",
            commonErrorCorrection = "مثالي للأطفال في بداية الكلام؛ شجع الطفل على ضم الشفتين وتفجير فقاعات الصابون.",
            soundCueOnomatopoeia = "بَ بُ بِ — فرقعة الفقاعات",
            animationTheme = AnimationTheme.OCEAN_PEARL
        ),
        PhonemeInfo(
            letter = "ع",
            nameArabic = "حرف العين",
            phoneticCategory = "حلقي احتكاكي مجهور",
            developmentalStage = DevelopmentalStage.STAGE_LATE,
            tonguePlacementType = TonguePlacementType.PHARYNGEAL_AYN,
            tonguePositionGuide = "رجوع جذر اللسان نحو الجدار الخلفي للبلعوم.",
            lipShapeGuide = "الفم مفتوح بشكل طبيعي.",
            airflowGuide = "تيار صوتي مجهور عميق من وسط الحلق.",
            commonErrorCorrection = "لعلاج إبدال العين همزة (أين بدل عين): ضع يد الطفل بلطف على الرقبة ليحس بالاهتزاز العميق مع تقليد صوت (عَـ عَـ).",
            soundCueOnomatopoeia = "عَ عُ عِ — صدى الصوت العميق",
            animationTheme = AnimationTheme.STAR_ROCKET
        )
    )

    val interactiveExercises: List<InteractiveExercise> = listOf(
        // ==================== حرف السين (س) ====================
        InteractiveExercise(
            id = "seen_beg_1",
            phonemeLetter = "س",
            phonemeName = "حرف السين",
            title = "قطار الصفير والمقاطع الصوتية (س)",
            subtitle = "إصدار صوت السين المنفرد ومقاطع الحركات والمدود",
            difficulty = DifficultyLevel.BEGINNER,
            developmentalStage = DevelopmentalStage.STAGE_MIDDLE,
            animationTheme = AnimationTheme.SNAKE_TRAIN,
            tonguePlacementType = TonguePlacementType.ALVEOLAR_GROOVE_SEEN,
            clinicalGoal = "تثبيت طرف اللسان خلف الأسنان السفلى وإخراج تيار هوائي صفيري متصل دون بروز اللسان.",
            therapistTip = "اطلب من الطفل الابتسام وإغلاق الأسنان كالسياج، ثم نفخ هواء بارد على إصبعه السبابة أمام فمه.",
            items = listOf(
                InteractivePromptItem("سْـ سْـ سْـ", "سْـ — سْـ — سْـ", "صوت منفرد", "صفير القطار", "أطبق أسنانك وابتسم وأخرج هواءً بارداً مستمراً"),
                InteractivePromptItem("سَ", "سَ (فتحة)", "مقطع قصير", "فتح الفم بعد الصفير", "ابدأ بالصفير ثم افتح فمك: سسسـَ"),
                InteractivePromptItem("سُ", "سُ (ضمة)", "مقطع قصير", "ضم الشفتين", "ابدأ بالصفير ثم اضمم شفتيك: سسسـُ"),
                InteractivePromptItem("سِ", "سِ (كسرة)", "مقطع قصير", "ابتسامة الكسرة", "حافظ على الأسنان قريبة: سسسـِ"),
                InteractivePromptItem("سَا", "سَـ + ا = سَا", "مد بالألف", "مد طويل مفتوح", "مدّ الصوت مع الحفاظ على صفاء السين"),
                InteractivePromptItem("سُو", "سُـ + و = سُو", "مد بالواو", "مد طويل مضموم", "انتقل بسلاسة من الابتسامة إلى الضم"),
                InteractivePromptItem("سِي", "سِـ + ي = سِي", "مد بالياء", "مد طويل مكسور", "أسهل المدود لضبط السين لثبات الابتسامة")
            )
        ),
        InteractiveExercise(
            id = "seen_int_1",
            phonemeLetter = "س",
            phonemeName = "حرف السين",
            title = "كلمات السين في أول ووسط وآخر الكلمة",
            subtitle = "تدرج مقطعي للكلمات المصورة حسب موضع حرف السين",
            difficulty = DifficultyLevel.INTERMEDIATE,
            developmentalStage = DevelopmentalStage.STAGE_MIDDLE,
            animationTheme = AnimationTheme.SNAKE_TRAIN,
            tonguePlacementType = TonguePlacementType.ALVEOLAR_GROOVE_SEEN,
            clinicalGoal = "تعميم نطق السين الصحيحة داخل الكلمات الثلاثية والرباعية في جميع المواضع.",
            therapistTip = "استخدم النطق المقطعي البطيء أولاً ثم ادمج المقاطع بسرعة طبيعية.",
            items = listOf(
                InteractivePromptItem("سَمَكَة", "سَ / مَ / كَ / ة", "أول الكلمة", "سمكة تسبح", "ابدأ الكلمة بصفير واضح قبل فتح الميم"),
                InteractivePromptItem("سَاعَة", "سَا / عَ / ة", "أول الكلمة", "ساعة الحائط", "مدّ مقطع (سَا) بوضوح"),
                InteractivePromptItem("سُلَحْفَاة", "سُ / لَحْ / فَا / ة", "أول الكلمة", "سلحفاة لطيفة", "احرص على عدم إخراج اللسان في بداية الكلمة"),
                InteractivePromptItem("عَسَل", "عَ / سَ / لْ", "وسط الكلمة", "عسل النحل", "انتقل من العين إلى السين مع إغلاق الأسنان"),
                InteractivePromptItem("مَسْبَح", "مَسْ / بَ / حْ", "وسط الكلمة", "مسبح مائي", "قف قليلاً على السين الساكنة (مَسْـ) لتأكيد مخرجها"),
                InteractivePromptItem("جَرَس", "جَ / رَ / سْ", "آخر الكلمة", "جرس المدرسة", "اختم الكلمة بصفير واضح وممتد"),
                InteractivePromptItem("خَسّ", "خَ / سّْ", "آخر الكلمة", "أوراق الخس", "شدّد السين في نهاية الكلمة مع الثبات")
            )
        ),
        InteractiveExercise(
            id = "seen_adv_1",
            phonemeLetter = "س",
            phonemeName = "حرف السين",
            title = "جمل السين التفاعلية والتمييز السمعي",
            subtitle = "توظيف السين في جمل مترابطة وثنائيات صغرى (س / ث / ش)",
            difficulty = DifficultyLevel.ADVANCED,
            developmentalStage = DevelopmentalStage.STAGE_MIDDLE,
            animationTheme = AnimationTheme.SNAKE_TRAIN,
            tonguePlacementType = TonguePlacementType.ALVEOLAR_GROOVE_SEEN,
            clinicalGoal = "أتمتة نطق حرف السين في الكلام التلقائي وتمييزه عن الثاء والشين.",
            therapistTip = "راقب ثبات اللسان خلف الأسنان عند انتقال الطفل السريع بين الكلمات.",
            items = listOf(
                InteractivePromptItem("سَبَحَ سَمِيرٌ فِي المَسْبَحِ", "سَـبَـحَ / سَـمِـيـرٌ / فِـي / المَـسْـبَـحِ", "جملة", "سباحة سمير", "ثلاث كلمات تحتوي على السين متتالية"),
                InteractivePromptItem("رَسَمَتْ سَارَةُ سَمَكَةً جَمِيلَةً", "رَ / سَـمَـتْ / سَـارَةُ / سَـمَـكَـةً", "جملة", "رسمة سارة", "حافظ على صفاء السين في كل كلمة"),
                InteractivePromptItem("سَارَ — ثَارَ", "سَا / رَ  ≠  ثَا / رَ", "ثنائية صغرى", "التمييز بين السين والثاء", "في (سَارَ) الأسنان مغلقة، وفي (ثَارَ) طرف اللسان يظهر قليلاً"),
                InteractivePromptItem("سَمْسَم — شَمْس", "سِـمْـسِـمْ  ≠  شَـمْـسْ", "ثنائية صغرى", "التمييز بين السين والشين", "ابتسم في (سِمْسِمْ) واضمم شفتيك في (شَمْس)")
            )
        ),

        // ==================== حرف الشين (ش) ====================
        InteractiveExercise(
            id = "sheen_beg_1",
            phonemeLetter = "ش",
            phonemeName = "حرف الشين",
            title = "أمواج البحر ومقاطع الشين (ش)",
            subtitle = "التدريب على التفشي الهوائي الدافئ ومقاطع الشين",
            difficulty = DifficultyLevel.BEGINNER,
            developmentalStage = DevelopmentalStage.STAGE_MIDDLE,
            animationTheme = AnimationTheme.OCEAN_PEARL,
            tonguePlacementType = TonguePlacementType.PALATAL_WIDE_SHEEN,
            clinicalGoal = "رفع وسط اللسان نحو غار الحنك مع استدارة خفيفة للشفاه لإنتاج صوت التفشي.",
            therapistTip = "ضع إصبع السبابة عمودياً أمام الشفتين في إشارة (الهدوء: ششش) لمساعدة الطفل على ضم الشفتين تلقائياً.",
            items = listOf(
                InteractivePromptItem("شْـ شْـ شْـ", "شْـ — شْـ — شْـ", "صوت منفرد", "همس الأمواج", "اضمم شفتيك قليلاً وأخرج هواءً دافئاً منتشراً"),
                InteractivePromptItem("شَ", "شَ (فتحة)", "مقطع قصير", "شروق الشمس", "ابدأ بـ (ششش) ثم افتح الفم"),
                InteractivePromptItem("شُ", "شُ (ضمة)", "مقطع قصير", "ضم الشفاه", "حافظ على استدارة الشفتين: شُ"),
                InteractivePromptItem("شِ", "شِ (كسرة)", "مقطع قصير", "شراع القارب", "ارفع وسط اللسان نحو السقف: شِ"),
                InteractivePromptItem("شَا", "شَـ + ا = شَا", "مد بالألف", "مدّ مفتوح", "شَا — صوت واضح ومنتشر"),
                InteractivePromptItem("شُو", "شُـ + و = شُو", "مد بالواو", "مدّ مضموم", "شُو — ممتاز لضبط ضم الشفاه"),
                InteractivePromptItem("شِي", "شِـ + ي = شِي", "مد بالياء", "مدّ مكسور", "شِي — حافظ على ارتفاع وسط اللسان")
            )
        ),
        InteractiveExercise(
            id = "sheen_int_1",
            phonemeLetter = "ش",
            phonemeName = "حرف الشين",
            title = "كلمات الشين المصورة والتقطيع المقطعي",
            subtitle = "نطق الشين في أول ووسط وآخر الكلمة",
            difficulty = DifficultyLevel.INTERMEDIATE,
            developmentalStage = DevelopmentalStage.STAGE_MIDDLE,
            animationTheme = AnimationTheme.OCEAN_PEARL,
            tonguePlacementType = TonguePlacementType.PALATAL_WIDE_SHEEN,
            clinicalGoal = "إتقان حرف الشين داخل الكلمات اليومية دون إبداله سيناً.",
            therapistTip = "ذكّر الطفل بحركة (ضم الشفتين كالبطة الصغيرة) عند الوصول لمقطع الشين.",
            items = listOf(
                InteractivePromptItem("شَمْس", "شَمْ / سْ", "أول الكلمة", "شمس مشرقة", "ابدأ بضم الشفتين في (شَـ) ثم ابتسم في (سْ)"),
                InteractivePromptItem("شَجَرَة", "شَ / جَ / رَ / ة", "أول الكلمة", "شجرة خضراء", "انطق المقاطع الأربعة بإيقاع متوازن"),
                InteractivePromptItem("شُرْطِي", "شُرْ / طِي", "أول الكلمة", "شرطي المرور", "الضمة تساعد على بروز الشفتين بوضوح"),
                InteractivePromptItem("فَرَاشَة", "فَ / رَا / شَ / ة", "وسط الكلمة", "فراشة ملونة", "أبرز صوت الشين في المقطع الثالث"),
                InteractivePromptItem("مِنْشَفَة", "مِنْ / شَ / فَ / ة", "وسط الكلمة", "منشفة ناعمة", "انتقل بوضوح من النون الساكنة إلى الشين"),
                InteractivePromptItem("عُشّ", "عُ / شّْ", "آخر الكلمة", "عش العصفور", "أطل صوت الشين في نهاية الكلمة"),
                InteractivePromptItem("رِيش", "رِي / شْ", "آخر الكلمة", "ريش ناعم", "اختم الكلمة بصوت هوائي دافئ (ششش)")
            )
        ),
        InteractiveExercise(
            id = "sheen_adv_1",
            phonemeLetter = "ش",
            phonemeName = "حرف الشين",
            title = "جمل الشين والتفريق بين (ش / س)",
            subtitle = "تدريبات التناوب السمعي والنطقي في الجمل",
            difficulty = DifficultyLevel.ADVANCED,
            developmentalStage = DevelopmentalStage.STAGE_MIDDLE,
            animationTheme = AnimationTheme.OCEAN_PEARL,
            tonguePlacementType = TonguePlacementType.PALATAL_WIDE_SHEEN,
            clinicalGoal = "المرونة الحركية في الانتقال بين مخرج الشين ومخرج السين في نفس الجملة.",
            therapistTip = "هذا التمرين يطور الدقة الحركية الفموية والتمييز الفونولوجي المتقدم.",
            items = listOf(
                InteractivePromptItem("أَشْرَقَتِ الشَّمْسُ عَلَى الشَّجَرَةِ", "أَشْـرَقَـتِ / الشَّـمْـسُ / عَـلَـى / الشَّـجَـرَةِ", "جملة", "شروق الصباح", "ثلاث شينات مع سين واحدة في كلمة الشمس"),
                InteractivePromptItem("شَرِبَ شَادِي الشَّايَ الدَّافِئَ", "شَـرِبَ / شَـادِي / الشَّـايَ / الدَّافِـئَ", "جملة", "شادي والشاي", "حافظ على تفشي الشين في كل الكلمات"),
                InteractivePromptItem("شَاهَدَتْ رَشَا فَرَاشَةً فِي البُسْتَانِ", "شَـاهَـدَتْ / رَشَـا / فَـرَاشَـةً", "جملة", "فراشة البستان", "ركز على الشين في وسط الكلمات")
            )
        ),

        // ==================== حرف الراء (ر) ====================
        InteractiveExercise(
            id = "raa_beg_1",
            phonemeLetter = "ر",
            phonemeName = "حرف الراء",
            title = "محرك الراء السريع والمقاطع المساعدة (دْرَ - تْرَ)",
            subtitle = "المنهج الأرطوفوني الحديث لتوليد اهتزاز طرف اللسان",
            difficulty = DifficultyLevel.BEGINNER,
            developmentalStage = DevelopmentalStage.STAGE_LATE,
            animationTheme = AnimationTheme.RACE_CAR,
            tonguePlacementType = TonguePlacementType.ALVEOLAR_TRILL_RAA,
            clinicalGoal = "تدريب طرف اللسان على الطرق السريع خلف اللثة العليا باستخدام الأصوات الانفجارية المساعدة (د / ت / ن).",
            therapistTip = "لا تطلب من الطفل ترديد (ررر) مباشرة إذا كان يبدلها غيناً؛ ابدأ بـ (دَ-دَ-دَ-دَ) سريعة مع اللثة العليا ثم ادفع الهواء لتتحول إلى (دْرَ)!",
            items = listOf(
                InteractivePromptItem("دَ - دَ - دَ - دْرَ", "دَ / دَ / دَ / دْرَ", "تدريب تمهيدي", "نقر اللثة العليا", "انقر بطرف اللسان خلف الأسنان العليا بسرعة مثل المطرقة الصغيرة"),
                InteractivePromptItem("تَ - تَ - تَ - تْرَ", "تَ / تَ / تَ / تْرَ", "تدريب تمهيدي", "تشغيل المحرك", "انفخ الهواء بقوة أثناء نقر التاء ليظهر اهتزاز الراء"),
                InteractivePromptItem("رَ — رُ — رِ", "رَ / رُ / رِ", "مقطع قصير", "انطلاق السيارة", "طرقات خفيفة سريعة بطرف اللسان فقط"),
                InteractivePromptItem("أَرْ — أُرْ — إِرْ", "أَرْ / أُرْ / إِرْ", "مقطع ساكن", "الراء الساكنة", "الراء الساكنة أسهل في إظهار التكرار الاهتزازي"),
                InteractivePromptItem("رَا — رُو — رِي", "رَا / رُو / رِي", "مد طويل", "سباق المسافات", "اربط اهتزاز الراء بحروف المد الطويلة")
            )
        ),
        InteractiveExercise(
            id = "raa_int_1",
            phonemeLetter = "ر",
            phonemeName = "حرف الراء",
            title = "كلمات الراء المتدرجة حسب الموضع",
            subtitle = "من الراء الساكنة والوسطية إلى الراء في أول الكلمة",
            difficulty = DifficultyLevel.INTERMEDIATE,
            developmentalStage = DevelopmentalStage.STAGE_LATE,
            animationTheme = AnimationTheme.RACE_CAR,
            tonguePlacementType = TonguePlacementType.ALVEOLAR_TRILL_RAA,
            clinicalGoal = "تثبيت حرف الراء داخل الكلمات بدءاً بالموضع الأسهل (الساكن والوسطي) وصولاً لأول الكلمة.",
            therapistTip = "وفق المناهج الأرطوفونية الحديثة، يسهل على الطفل نطق الراء بعد الفتحة أو السكون (مثل: بَرْد، جَرَس) قبل نطقها في بداية الكلمة.",
            items = listOf(
                InteractivePromptItem("بَرْد", "بَرْ / دْ", "وسط الكلمة (ساكن)", "طقس بارد", "الراء الساكنة قبل الدال تساعد اللسان على البقاء في اللثة العليا"),
                InteractivePromptItem("قِطَار", "قِ / طَا / رْ", "آخر الكلمة", "قطار سريع", "اختم الكلمة بطرقة رشيقة لطرف اللسان"),
                InteractivePromptItem("جَزَر", "جَ / زَ / رْ", "آخر الكلمة", "جزر برتقالي", "ارفع طرف اللسان في نهاية الكلمة"),
                InteractivePromptItem("فَرَاشَة", "فَ / رَا / شَ / ة", "وسط الكلمة", "فراشة الحديقة", "انطق مقطع (رَا) بطرقة واحدة واضحة"),
                InteractivePromptItem("كُرَة", "كُ / رَ / ة", "وسط الكلمة", "كرة القدم", "الضمة في (كُ) تهيئ اللسان للارتفاع السريع في (رَ)"),
                InteractivePromptItem("رَمْل", "رَمْ / لْ", "أول الكلمة", "رمل الشاطئ", "ابدأ بطرف اللسان مرتفعاً خلف الأسنان العليا"),
                InteractivePromptItem("رُمَّان", "رُمَّ / ا / نْ", "أول الكلمة", "رمان أحمر", "استخدم ضم الشفتين لدعم انطلاقة الراء")
            )
        ),
        InteractiveExercise(
            id = "raa_adv_1",
            phonemeLetter = "ر",
            phonemeName = "حرف الراء",
            title = "جمل الراء والتفريق بين (ر / ل) و (ر / غ)",
            subtitle = "تثبيت الراء في الجمل والثنائيات الصغرى المتقدمة",
            difficulty = DifficultyLevel.ADVANCED,
            developmentalStage = DevelopmentalStage.STAGE_LATE,
            animationTheme = AnimationTheme.RACE_CAR,
            tonguePlacementType = TonguePlacementType.ALVEOLAR_TRILL_RAA,
            clinicalGoal = "التخلص النهائي من اللثغة اللامية أو الغينية أثناء سرد الجمل المتصلة.",
            therapistTip = "في الثنائيات (رامي / لامي)، ذكّر الطفل أن اللام تلتصق باللثة بينما الراء تقفز وتطرق اللثة بخفة.",
            items = listOf(
                InteractivePromptItem("رَكِبَ رَامِي القِطَارَ السَّرِيعَ", "رَكِبَ / رَامِي / القِطَارَ / السَّرِيعَ", "جملة", "رحلة القطار", "أربع كلمات غنية بصوت الراء"),
                InteractivePromptItem("طَارَ العُصْفُورُ فَوْقَ الشَّجَرَةِ", "طَارَ / العُصْفُورُ / فَوْقَ / الشَّجَرَةِ", "جملة", "تحليق العصفور", "تدرج بين الراء المفتوحة والمضمومة والمنونة"),
                InteractivePromptItem("رَمْل — نَمْل", "رَمْـلْ  ≠  نَمْـلْ", "ثنائية صغرى", "تمييز الراء عن النون", "طرق سريع في (رَمْل) مقابل ثبات أنفي في (نَمْل)"),
                InteractivePromptItem("حَبْر — حَبْل", "حَبْـرْ  ≠  حَبْـلْ", "ثنائية صغرى", "تمييز الراء عن اللام", "اهتزاز في نهاية (حَبْر) مقابل التصاق جانبي في (حَبْل)")
            )
        ),

        // ==================== حرف اللام (ل) ====================
        InteractiveExercise(
            id = "laam_beg_1",
            phonemeLetter = "ل",
            phonemeName = "حرف اللام",
            title = "عصفور الألحان ومقاطع اللام (لَا - لُو - لِي)",
            subtitle = "تدريب رفع طرف اللسان إلى النقطة اللثوية العليا",
            difficulty = DifficultyLevel.BEGINNER,
            developmentalStage = DevelopmentalStage.STAGE_EARLY,
            animationTheme = AnimationTheme.MUSICAL_BIRD,
            tonguePlacementType = TonguePlacementType.ALVEOLAR_LATERAL_LAAM,
            clinicalGoal = "تعليم الطفل رفع طرف اللسان للمس اللثة العليا خلف الأسنان الأمامية العليا بثبات.",
            therapistTip = "سمِّ منطقة اللثة خلف الأسنان العليا (عش العصفور) واطلب من الطفل أن يرفع لسانه ليلمس العش في كل مقطع.",
            items = listOf(
                InteractivePromptItem("لْـ — لَا — لَا — لَا", "لَا / لَا / لَا", "مقطع غنائي", "أغنية العصفور", "افتح فمك وارفع طرف لسانك عالياً نحو اللثة العليا"),
                InteractivePromptItem("لَ — لُ — لِ", "لَ / لُ / لِ", "مقطع قصير", "الحركات الثلاث", "تأكد من رؤية ارتفاع اللسان وعدم تحريك الشفة السفلى"),
                InteractivePromptItem("أَلْ — أُلْ — إِلْ", "أَلْ / أُلْ / إِلْ", "مقطع ساكن", "اللام القمرية", "اثبت بطرف اللسان فوق اللثة عند السكون"),
                InteractivePromptItem("لَا — لُو — لِي", "لَا / لُو / لِي", "مد طويل", "المدود الموسيقية", "صوت غنائي متصل وواضح")
            )
        ),
        InteractiveExercise(
            id = "laam_int_1",
            phonemeLetter = "ل",
            phonemeName = "حرف اللام",
            title = "كلمات اللام في أول ووسط وآخر الكلمة",
            subtitle = "تطبيق نطق اللام في الكلمات المصورة المحببة للأطفال",
            difficulty = DifficultyLevel.INTERMEDIATE,
            developmentalStage = DevelopmentalStage.STAGE_EARLY,
            animationTheme = AnimationTheme.MUSICAL_BIRD,
            tonguePlacementType = TonguePlacementType.ALVEOLAR_LATERAL_LAAM,
            clinicalGoal = "نطق اللام بوضوح في الكلمات اليومية دون حذفها أو إبدالها ياءً أو نوناً.",
            therapistTip = "استخدم المرآة أو الكاميرا الأمامية ليلاحظ الطفل ارتفاع لسانه في كل كلمة.",
            items = listOf(
                InteractivePromptItem("لَيْمُون", "لَيْ / مُو / نْ", "أول الكلمة", "ليمونة صفراء", "ارفع اللسان منذ اللحظة الأولى للكلمة"),
                InteractivePromptItem("لُعْبَة", "لُعْ / بَ / ة", "أول الكلمة", "لعبة ممتعة", "ضم الشفتين في (لُ) مع بقاء طرف اللسان عالياً"),
                InteractivePromptItem("قَلَم", "قَ / لَ / مْ", "وسط الكلمة", "قلم تلوين", "انتقل من أقصى اللسان (ق) إلى طرف اللسان (ل)"),
                InteractivePromptItem("حَلِيب", "حَ / لِي / بْ", "وسط الكلمة", "كوب حليب", "مدّ مقطع (لِي) بوضوح وجمال"),
                InteractivePromptItem("عَسَل", "عَ / سَ / لْ", "آخر الكلمة", "عسل لذيذ", "اختم الكلمة برفع اللسان ولمس اللثة العليا"),
                InteractivePromptItem("جَمَل", "جَ / مَ / لْ", "آخر الكلمة", "جمل الصحراء", "اثبت على اللام في آخر الكلمة ثانيتين")
            )
        ),
        InteractiveExercise(
            id = "laam_adv_1",
            phonemeLetter = "ل",
            phonemeName = "حرف اللام",
            title = "جمل اللام واللام القمرية والتتابع الصوتي",
            subtitle = "توظيف اللام في الجمل والقصص القصيرة",
            difficulty = DifficultyLevel.ADVANCED,
            developmentalStage = DevelopmentalStage.STAGE_EARLY,
            animationTheme = AnimationTheme.MUSICAL_BIRD,
            tonguePlacementType = TonguePlacementType.ALVEOLAR_LATERAL_LAAM,
            clinicalGoal = "إتقان اللام المتكررة واللام القمرية في سياق الجمل الوظيفية.",
            therapistTip = "شجع الطفل على ترديد الجملة بنغمة إيقاعية محببة لتعزيز الطلاقة اللفظية.",
            items = listOf(
                InteractivePromptItem("لَعِبَ بِلَالٌ بِالكُرَةِ المُلَوَّنَةِ", "لَعِبَ / بِلَالٌ / بِالكُرَةِ / المُلَوَّنَةِ", "جملة", "لعب بلال", "خمس لامات متتالية في جملة واحدة"),
                InteractivePromptItem("أَكَلَتْ لَيْلَى اللَّيْمُونَ وَالعَسَلَ", "أَكَلَتْ / لَيْلَى / اللَّيْمُونَ / وَالعَسَلَ", "جملة", "ليلى والعسل", "تدريب ممتاز على اللام المشددة واللام في آخر الكلمة")
            )
        ),

        // ==================== حرف الكاف (ك) والقاف (ق) ====================
        InteractiveExercise(
            id = "kaaf_beg_1",
            phonemeLetter = "ك",
            phonemeName = "حرف الكاف",
            title = "إيقاع الكاف الطبقي (كَ - كُ - كِ)",
            subtitle = "علاج إبدال الكاف تاءً (الاستبدال الأمامي)",
            difficulty = DifficultyLevel.BEGINNER,
            developmentalStage = DevelopmentalStage.STAGE_MIDDLE,
            animationTheme = AnimationTheme.STAR_ROCKET,
            tonguePlacementType = TonguePlacementType.VELAR_BACK_KAAF_QAAF,
            clinicalGoal = "خفض طرف اللسان خلف الأسنان السفلى ورفع أقصى اللسان لإصدار صوت الكاف الصحيح.",
            therapistTip = "اطلب من الطفل وضع طرف إصبعه النظيف أو طرف ملعقة صغيرة على مقدمة اللسان لمنعها من الارتفاع لنطق (ت)، فينطلق صوت (ك) فوراً!",
            items = listOf(
                InteractivePromptItem("كْـ — كَ — كُ — كِ", "كَ / كُ / كِ", "مقطع قصير", "فتح الفم وإبقاء طرف اللسان بالأسفل", "اترك طرف لسانك نائماً بالأسفل وارفع آخر اللسان"),
                InteractivePromptItem("كَا — كُو — كِي", "كَا / كُو / كِي", "مد طويل", "صاروخ الكاف", "انطق بصوت واضح من خلف الفم"),
                InteractivePromptItem("كِتَاب", "كِ / تَا / بْ", "أول الكلمة", "كتاب القصص", "انتبه: نبدأ بـ (كِ) وليس (تِ) ثم ننتقل إلى (تَا)"),
                InteractivePromptItem("سَمَكَة", "سَ / مَ / كَ / ة", "وسط الكلمة", "سمكة البحر", "أبرز صوت (كَ) في المقطع الثالث"),
                InteractivePromptItem("دِيك", "دِي / كْ", "آخر الكلمة", "ديك المزرعة", "اختم الكلمة بهمزة هوائية لطيفة للكاف")
            )
        ),
        InteractiveExercise(
            id = "qaaf_int_1",
            phonemeLetter = "ق",
            phonemeName = "حرف القاف",
            title = "تدريبات القاف القوية والكلمات والتمييز (ق / ك)",
            subtitle = "تقوية المخرج اللهوي وتفخيم صوت القاف",
            difficulty = DifficultyLevel.INTERMEDIATE,
            developmentalStage = DevelopmentalStage.STAGE_LATE,
            animationTheme = AnimationTheme.STAR_ROCKET,
            tonguePlacementType = TonguePlacementType.VELAR_BACK_KAAF_QAAF,
            clinicalGoal = "إنتاج صوت القاف المفخم من أقصى اللسان مع اللهاة وتمييزه عن الكاف والتاء.",
            therapistTip = "شجع الطفل على فتح فمه عمودياً وتقليد صوت قطرة الماء القوية (قُـ قُـ).",
            items = listOf(
                InteractivePromptItem("قَ — قُ — قِ — قَا", "قَ / قُ / قِ / قَا", "مقاطع صوتية", "دقات القاف", "صوت قوي مفخم من أقصى الفم"),
                InteractivePromptItem("قَمَر", "قَ / مَ / رْ", "أول الكلمة", "قمر مضيء", "ابدأ بقاف مفخمة واضحة"),
                InteractivePromptItem("قِطَار", "قِ / طَا / رْ", "أول الكلمة", "قطار المحطة", "حافظ على قوة القاف في بداية الكلمة"),
                InteractivePromptItem("بُرْتُقَال", "بُرْ / تُ / قَا / لْ", "وسط الكلمة", "برتقال طازج", "انتقل بسلاسة من التاء الأمامية إلى القاف الخلفية"),
                InteractivePromptItem("قَلْب — كَلْب", "قَلْـبْ  ≠  كَلْـبْ", "ثنائية صغرى", "تمييز القاف عن الكاف", "القاف مفخمة عميقة والكاف مرققة مهموسة")
            )
        ),

        // ==================== حرف الباء (ب) والفاء (ف) والجيم (ج) والعين (ع) ====================
        InteractiveExercise(
            id = "baa_beg_1",
            phonemeLetter = "ب",
            phonemeName = "حرف الباء",
            title = "فرقعة الفقاعات والأصوات الشفوية المبكرة (ب)",
            subtitle = "تنمية النطق المبكر وإغلاق الشفتين (2 - 3.5 سنوات)",
            difficulty = DifficultyLevel.BEGINNER,
            developmentalStage = DevelopmentalStage.STAGE_EARLY,
            animationTheme = AnimationTheme.OCEAN_PEARL,
            tonguePlacementType = TonguePlacementType.BILABIAL_LABIODENTAL,
            clinicalGoal = "تحفيز التقليد الصوتي المبكر وتقوية عضلات الشفتين عند الأطفال الصغار.",
            therapistTip = "اجعل التمرين لعبياً بالكامل: اضمم شفتيك ثم افتحهما مع صوت فرقعة الفقاعة (بَ بَ بَ)!",
            items = listOf(
                InteractivePromptItem("بَ — بُ — بِ — بَابَا", "بَ / بُ / بِ / بَا / بَا", "مقاطع مبكرة", "فقاعات الصابون", "أطبق الشفتين معاً ثم افتحهما بفرح"),
                InteractivePromptItem("بَطَّة", "بَطْ / طَ / ة", "أول الكلمة", "بطة تسبح", "انطلاقة شفوية واضحة"),
                InteractivePromptItem("بَيْت", "بَيْ / تْ", "أول الكلمة", "بيت دافئ", "بَ + يْ + تْ"),
                InteractivePromptItem("أَرْنَب", "أَرْ / نَ / بْ", "آخر الكلمة", "أرنب يقفز", "أغلق شفتيك في نهاية الكلمة ثم أطلقهما")
            )
        ),
        InteractiveExercise(
            id = "faa_beg_1",
            phonemeLetter = "ف",
            phonemeName = "حرف الفاء",
            title = "نسيم الفاء اللطيف ومقاطعها وكلماتها (ف)",
            subtitle = "تلامس الأسنان العليا مع الشفة السفلى",
            difficulty = DifficultyLevel.BEGINNER,
            developmentalStage = DevelopmentalStage.STAGE_EARLY,
            animationTheme = AnimationTheme.SNAKE_TRAIN,
            tonguePlacementType = TonguePlacementType.BILABIAL_LABIODENTAL,
            clinicalGoal = "ضبط مخرج الفاء الشفوي الأسناني وتوجيه تيار الهواء الفموي.",
            therapistTip = "اطلب من الطفل عمل (أسنان الأرنب) بوضع الأسنان العليا على الشفة السفلى ثم النفخ بلطف.",
            items = listOf(
                InteractivePromptItem("فْـ — فَ — فُ — فِ", "فَ / فُ / فِ", "مقاطع صوتية", "نفخة الريشة", "أسنانك العليا تلمس شفتك السفلى بلطف"),
                InteractivePromptItem("فِيل", "فِي / لْ", "أول الكلمة", "فيل لطيف", "مدّ مقطع (فِي) ثم ارفع لسانك للام"),
                InteractivePromptItem("فَرَاشَة", "فَ / رَا / شَ / ة", "أول الكلمة", "فراشة الربيع", "ابدأ بنفخة الفاء اللطيفة"),
                InteractivePromptItem("تُفَّاح", "تُفْ / فَا / حْ", "وسط الكلمة", "تفاحة حمراء", "فاء مشددة واضحة في وسط الكلمة"),
                InteractivePromptItem("خَرُوف", "خَ / رُو / فْ", "آخر الكلمة", "خروف المزرعة", "اختم الكلمة بصوت الفاء المهموس")
            )
        ),
        InteractiveExercise(
            id = "jeem_int_1",
            phonemeLetter = "ج",
            phonemeName = "حرف الجيم",
            title = "جرس الجيم الغاري وتدريبات الكلمات (ج)",
            subtitle = "علاج إبدال الجيم دالاً أو ياءً",
            difficulty = DifficultyLevel.INTERMEDIATE,
            developmentalStage = DevelopmentalStage.STAGE_MIDDLE,
            animationTheme = AnimationTheme.STAR_ROCKET,
            tonguePlacementType = TonguePlacementType.PHARYNGEAL_AYN,
            clinicalGoal = "رفع وسط اللسان نحو غار الحنك الأعلى لإصدار صوت الجيم المركب المجهور.",
            therapistTip = "إذا نطق الطفل (دمل) بدل (جمل)، اطلب منه دمج الدال مع الياء سريعاً (دْيَ -> جَ).",
            items = listOf(
                InteractivePromptItem("جَ — جُ — جِ — جَا", "جَ / جُ / جِ / جَا", "مقاطع صوتية", "رنين الجرس", "ارفع وسط اللسان نحو سقف الفم"),
                InteractivePromptItem("جَمَل", "جَ / مَ / لْ", "أول الكلمة", "سفينة الصحراء", "انطق (جَ) من وسط اللسان وليس من طرفه"),
                InteractivePromptItem("جَزَر", "جَ / زَ / رْ", "أول الكلمة", "جزر طازج", "جَ + زَ + رْ"),
                InteractivePromptItem("شَجَرَة", "شَ / جَ / رَ / ة", "وسط الكلمة", "شجرة مثمرة", "انتقل من الشين الاحتكاكية إلى الجيم"),
                InteractivePromptItem("ثَلْج", "ثَلْ / جْ", "آخر الكلمة", "ثلج أبيض", "أظهر الجيم بوضوح في ختام الكلمة")
            )
        ),
        InteractiveExercise(
            id = "ayn_adv_1",
            phonemeLetter = "ع",
            phonemeName = "حرف العين",
            title = "صدى العين الحلقي وتدريبات الكلمات والجمل (ع)",
            subtitle = "علاج إبدال العين همزة وتقوية الرنين الحلقي",
            difficulty = DifficultyLevel.ADVANCED,
            developmentalStage = DevelopmentalStage.STAGE_LATE,
            animationTheme = AnimationTheme.STAR_ROCKET,
            tonguePlacementType = TonguePlacementType.PHARYNGEAL_AYN,
            clinicalGoal = "تنشيط عضلات البلعوم وجذر اللسان لإصدار صوت العين المجهور بدلاً من الهمزة.",
            therapistTip = "اجعل الطفل يضع كفه على مقدمة عنقه ليشعر باهتزاز الصوت العميق عند قول (عَـ عَـ عَـ).",
            items = listOf(
                InteractivePromptItem("عَ — عُ — عِ — عَا — عُو — عِي", "عَ / عُ / عِ / عَا", "مقاطع صوتية", "الرنين الحلقي", "صوت عميق دافئ من وسط الحلق"),
                InteractivePromptItem("عُصْفُور", "عُصْ / فُو / رْ", "أول الكلمة", "عصفور يغرد", "ابدأ بالعين المضمومة العميقة"),
                InteractivePromptItem("عِنَب", "عِ / نَ / بْ", "أول الكلمة", "عنقود عنب", "عِـ مكسورة واضحة وليست (إِنب)"),
                InteractivePromptItem("مِلْعَقَة", "مِلْ / عَ / قَ / ة", "وسط الكلمة", "ملعقة الطعام", "أبرز العين بعد اللام الساكنة"),
                InteractivePromptItem("شَمْعَة", "شَمْ / عَ / ة", "وسط الكلمة", "شمعة مضيئة", "انتقل من الميم الساكنة إلى العين"),
                InteractivePromptItem("عَامَ عُمَرُ فِي المَسْبَحِ الوَاسِعِ", "عَامَ / عُمَرُ / فِي / المَسْبَحِ / الوَاسِعِ", "جملة", "سباحة عمر", "تطبيق العين في أول ووسط وآخر الكلمات")
            )
        )
    )

    val oralMotorExercises: List<OralMotorExercise> = listOf(
        OralMotorExercise(
            id = "om_tongue_elevator",
            title = "المصعد اللساني (فوق وتحت)",
            targetOrgan = "اللسان",
            durationSeconds = 20,
            repetitions = 10,
            childFriendlyMetaphor = "لنجعل لساننا يصعد كالمصعد إلى الطابق العلوي خلف الأسنان العليا ثم ينزل للطابق الأرضي!",
            stepByStepInstructions = listOf(
                "افتح فمك باتساع مريح أمام المرآة.",
                "ارفع طرف اللسان ليلمس اللثة خلف الأسنان الأمامية العليا واثبت 3 ثوانٍ.",
                "أنزل طرف اللسان خلف الأسنان السفلى واثبت 3 ثوانٍ.",
                "كرر الحركة ببطء دون تحريك الفك السفلي."
            ),
            helpsWithPhonemes = listOf("ر", "ل", "ن", "ت", "د")
        ),
        OralMotorExercise(
            id = "om_horse_clicks",
            title = "ركض الحصان (طقطقة اللسان)",
            targetOrgan = "اللسان",
            durationSeconds = 25,
            repetitions = 15,
            childFriendlyMetaphor = "لنقلد صوت حوافر الحصان السريع (تك - تك - تك) بلصق اللسان في سقف الفم!",
            stepByStepInstructions = listOf(
                "ألصق ظهر وطرف اللسان بالكامل في سقف الحنك الأعلى.",
                "اسحب اللسان للأسفل بقوة ليصدر صوت فرقعة واضحة (كليك).",
                "نوع بين الإيقاع البطيء والإيقاع السريع لتقوية لجام اللسان."
            ),
            helpsWithPhonemes = listOf("ر", "ل", "ش", "ج")
        ),
        OralMotorExercise(
            id = "om_tongue_pendulum",
            title = "بندول الساعة (يمين ويسار)",
            targetOrgan = "اللسان",
            durationSeconds = 20,
            repetitions = 12,
            childFriendlyMetaphor = "لساننا يتحرك مثل بندول الساعة من زاوية الفم اليمنى إلى الزاوية اليسرى!",
            stepByStepInstructions = listOf(
                "ابتسم قليلاً وأخرج طرف اللسان ليلمس زاوية الشفة اليمنى.",
                "انقل طرف اللسان بسلاسة ليلمس زاوية الشفة اليسرى.",
                "حافظ على استقامة الرأس وثبات الفك."
            ),
            helpsWithPhonemes = listOf("س", "ش", "ل", "ر")
        ),
        OralMotorExercise(
            id = "om_lip_smile_pucker",
            title = "الابتسامة العريضة والقبلة (إي — أو)",
            targetOrgan = "الشفاه",
            durationSeconds = 20,
            repetitions = 10,
            childFriendlyMetaphor = "ابتسامة عريضة مثل المهرج السعيد (إييي) ثم ضم الشفاه مثل السمكة الصغيرة (أووو)!",
            stepByStepInstructions = listOf(
                "شدّ الشفتين في ابتسامة عريضة مع إطباق الأسنان لمدة 3 ثوانٍ.",
                "اضمم الشفتين للأمام في شكل دائري محكم لمدة 3 ثوانٍ.",
                "بدّل بين الحركتين لزيادة مرونة الشفاه في التمييز بين (س) و(ش)."
            ),
            helpsWithPhonemes = listOf("س", "ش", "ب", "ف", "و")
        ),
        OralMotorExercise(
            id = "om_cheek_puff",
            title = "بالونات الخدود المنتفخة",
            targetOrgan = "الخدود والفك",
            durationSeconds = 25,
            repetitions = 8,
            childFriendlyMetaphor = "انفخ خديك بالهواء كالبالون الكبير وحافظ على الهواء داخل الفم دون أن يهرب!",
            stepByStepInstructions = listOf(
                "خذ شهيقاً من الأنف وأغلق شفتيك بإحكام.",
                "املأ الخدين بالهواء واثبت لمدة 5 ثوانٍ.",
                "انقل كرة الهواء من الخد الأيمن إلى الخد الأيسر بالتناوب.",
                "أخرج الهواء في النهاية بنفخة طويلة منتظمة."
            ),
            helpsWithPhonemes = listOf("ب", "ف", "ك", "ق", "س")
        )
    )

    val minimalPairChallenges: List<MinimalPairChallenge> = listOf(
        MinimalPairChallenge(
            id = "mp_seen_sheen_1",
            targetContrastLabel = "تمييز السين والشين (س / ش)",
            developmentalStage = DevelopmentalStage.STAGE_MIDDLE,
            promptQuestion = "استمع جيداً واختر الكلمة التي تبدأ بصوت الأمواج الدافئ (ش):",
            correctWord = "شَمْس",
            correctSyllables = "شَمْ / سْ",
            correctPhoneme = "ش",
            correctMeaningHint = "تشرق في السماء وتمنحنا الدفء والضوء",
            distractorWord = "سَمَك",
            distractorSyllables = "سَ / مَ / كْ",
            distractorPhoneme = "س",
            distractorMeaningHint = "يعيش ويسبح في ماء البحر",
            clinicalExplanation = "يهدف التمرين لتدريب الأذن على تمييز التفشي في (ش) مقابل الصفير الحاد في (س)."
        ),
        MinimalPairChallenge(
            id = "mp_raa_laam_1",
            targetContrastLabel = "تمييز الراء واللام (ر / ل)",
            developmentalStage = DevelopmentalStage.STAGE_LATE,
            promptQuestion = "اختر الكلمة التي تنتهي بصوت المحرك المتكرر (ر):",
            correctWord = "حَبْر",
            correctSyllables = "حَبْ / رْ",
            correctPhoneme = "ر",
            correctMeaningHint = "سائل ملون نكتب به داخل القلم",
            distractorWord = "حَبْل",
            distractorSyllables = "حَبْ / لْ",
            distractorPhoneme = "ل",
            distractorMeaningHint = "خيط سميك نقفز به في اللعب",
            clinicalExplanation = "ثنائية صغرى أساسية لعلاج اللثغة اللامية (إبدال الراء لاماً)."
        ),
        MinimalPairChallenge(
            id = "mp_kaaf_taa_1",
            targetContrastLabel = "تمييز الكاف والتاء (ك / ت)",
            developmentalStage = DevelopmentalStage.STAGE_MIDDLE,
            promptQuestion = "اختر الكلمة التي تبدأ من أقصى اللسان بصوت (ك):",
            correctWord = "كُوب",
            correctSyllables = "كُو / بْ",
            correctPhoneme = "ك",
            correctMeaningHint = "نشرب فيه الماء والحليب والعصير",
            distractorWord = "تُوت",
            distractorSyllables = "تُو / تْ",
            distractorPhoneme = "ت",
            distractorMeaningHint = "فاكهة صغيرة حلوة المذاق",
            clinicalExplanation = "يدرب الطفل على التمييز السمعي بين المخرج الطبقي الخلفي (ك) والمخرج اللثوي الأمامي (ت)."
        ),
        MinimalPairChallenge(
            id = "mp_seen_thaa_1",
            targetContrastLabel = "تمييز السين والثاء (س / ث)",
            developmentalStage = DevelopmentalStage.STAGE_MIDDLE,
            promptQuestion = "اختر الكلمة التي تنطق والأسنان مغلقة بصوت الصفير (س):",
            correctWord = "سَارَ",
            correctSyllables = "سَا / رَ",
            correctPhoneme = "س",
            correctMeaningHint = "مشى في الطريق بهدوء",
            distractorWord = "ثَارَ",
            distractorSyllables = "ثَا / رَ",
            distractorPhoneme = "ث",
            distractorMeaningHint = "انتشر الغبار أو البركان",
            clinicalExplanation = "يعزز الوعي السمعي لعلاج اللثغة الأمامية بين الأسنان (Interdental Lisp)."
        ),
        MinimalPairChallenge(
            id = "mp_qaaf_kaaf_1",
            targetContrastLabel = "تمييز القاف والكاف (ق / ك)",
            developmentalStage = DevelopmentalStage.STAGE_LATE,
            promptQuestion = "اختر الكلمة التي تبدأ بالصوت المفخم العميق (ق):",
            correctWord = "قَلْب",
            correctSyllables = "قَلْ / بْ",
            correctPhoneme = "ق",
            correctMeaningHint = "ينبض في صدرنا بالمحبة والحياة",
            distractorWord = "كَلْب",
            distractorSyllables = "كَلْ / بْ",
            distractorPhoneme = "ك",
            distractorMeaningHint = "حيوان أليف وفيّ يحرس المزرعة",
            clinicalExplanation = "تمييز التفخيم اللهوي في (ق) عن الترقيق المهموس في (ك)."
        ),
        MinimalPairChallenge(
            id = "mp_raa_ghayn_1",
            targetContrastLabel = "تمييز الراء والغين (ر / غ)",
            developmentalStage = DevelopmentalStage.STAGE_LATE,
            promptQuestion = "اختر الكلمة التي تبدأ بطرقة طرف اللسان الأمامية (ر):",
            correctWord = "رَامِي",
            correctSyllables = "رَا / مِي",
            correctPhoneme = "ر",
            correctMeaningHint = "اسم ولد نشيط يحب الرياضة",
            distractorWord = "غَامِي",
            distractorSyllables = "غَا / مِي",
            distractorPhoneme = "غ",
            distractorMeaningHint = "صوت حلقي خلفي من أقصى الفم",
            clinicalExplanation = "يساعد الطفل الذي يعاني من اللثغة الغينية (الفرنسية) على التمييز السمعي بين الراء الأمامية والغين الخلفية."
        )
    )

    val aacCommunicationCards: List<AacCardItem> = listOf(
        // الضمائر
        AacCardItem("aac_1", "أَنَا", "الضمائر", "PRONOUN"),
        AacCardItem("aac_2", "نَحْنُ", "الضمائر", "PRONOUN"),
        AacCardItem("aac_3", "أَنْتَ", "الضمائر", "PRONOUN"),
        // الأفعال
        AacCardItem("aac_4", "أُرِيدُ", "الأفعال", "VERB"),
        AacCardItem("aac_5", "أُحِبُّ", "الأفعال", "VERB"),
        AacCardItem("aac_6", "أَشْرَبُ", "الأفعال", "VERB"),
        AacCardItem("aac_7", "آكُلُ", "الأفعال", "VERB"),
        AacCardItem("aac_8", "أَلْعَبُ", "الأفعال", "VERB"),
        AacCardItem("aac_9", "أَسْمَعُ", "الأفعال", "VERB"),
        // الاحتياجات والأشياء
        AacCardItem("aac_10", "المَاءَ", "الاحتياجات والأشياء", "NOUN"),
        AacCardItem("aac_11", "الحَلِيبَ", "الاحتياجات والأشياء", "NOUN"),
        AacCardItem("aac_12", "التُّفَّاحَةَ", "الاحتياجات والأشياء", "NOUN"),
        AacCardItem("aac_13", "بِالكُرَةِ", "الاحتياجات والأشياء", "NOUN"),
        AacCardItem("aac_14", "القِصَّةَ", "الاحتياجات والأشياء", "NOUN"),
        AacCardItem("aac_15", "السَّيَّارَةَ", "الاحتياجات والأشياء", "NOUN"),
        // المشاعر والمهذبات
        AacCardItem("aac_16", "مِنْ فَضْلِكَ", "المشاعر والمهذبات", "SOCIAL"),
        AacCardItem("aac_17", "شُكْراً لَكَ", "المشاعر والمهذبات", "SOCIAL"),
        AacCardItem("aac_18", "أَنَا سَعِيدٌ", "المشاعر والمهذبات", "SOCIAL"),
        AacCardItem("aac_19", "سَاعِدْنِي", "المشاعر والمهذبات", "SOCIAL")
    )
}
