package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.curriculum.DevelopmentalStage
import com.example.data.curriculum.DifficultyLevel
import com.example.data.curriculum.InteractiveExercise
import com.example.data.curriculum.PhonemeInfo
import com.example.data.local.ChildProfileEntity
import com.example.data.local.ExerciseProgressEntity
import com.example.ui.components.ArticulatorMouthDiagramCanvas
import com.example.ui.theme.DifficultyEasyBg
import com.example.ui.theme.DifficultyEasyText
import com.example.ui.theme.DifficultyHardBg
import com.example.ui.theme.DifficultyHardText
import com.example.ui.theme.DifficultyMediumBg
import com.example.ui.theme.DifficultyMediumText
import com.example.ui.theme.PhonemeKaafColor
import com.example.ui.theme.PhonemeLaamColor
import com.example.ui.theme.PhonemeOtherColor
import com.example.ui.theme.PhonemeRaaColor
import com.example.ui.theme.PhonemeSeenColor
import com.example.ui.theme.PhonemeSheenColor

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeAndLibraryScreen(
    selectedChild: ChildProfileEntity?,
    phonemeCatalog: List<PhonemeInfo>,
    exercises: List<InteractiveExercise>,
    progressList: List<ExerciseProgressEntity>,
    selectedPhoneme: String?,
    selectedDifficulty: DifficultyLevel?,
    selectedStage: DevelopmentalStage?,
    searchQuery: String,
    onSelectPhoneme: (String?) -> Unit,
    onSelectDifficulty: (DifficultyLevel?) -> Unit,
    onSelectStage: (DevelopmentalStage?) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onClearFilters: () -> Unit,
    onOpenExercise: (InteractiveExercise) -> Unit,
    onSpeakText: (text: String, slow: Boolean, phoneme: String) -> Unit,
    onAddCustomExercise: (
        phonemeLetter: String,
        title: String,
        targetWord: String,
        syllables: String,
        position: String,
        difficulty: DifficultyLevel,
        stage: DevelopmentalStage,
        tip: String
    ) -> Unit,
    onDeleteCustomExercise: (String) -> Unit
) {
    var showAddExerciseDialog by remember { mutableStateOf(false) }
    val activePhonemeInfo = remember(selectedPhoneme, phonemeCatalog) {
        phonemeCatalog.find { it.letter == selectedPhoneme }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddExerciseDialog = true },
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = Color.White,
                modifier = Modifier.testTag("fab_add_custom_exercise")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "إضافة تمرين نطق")
                    Text(
                        text = "تمرين جديد",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("exercise_library_list"),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Hero Header Card with Child Profile & Stars
            item {
                HeroWelcomeBannerCard(
                    child = selectedChild,
                    totalExercisesCount = exercises.size
                )
            }

            // 2. Phoneme Filter Ribbon (السين، الشين، الراء، اللام، الكاف، القاف...)
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "اختر الصوت المستهدف (مخارج الحروف):",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        if (selectedPhoneme != null || selectedDifficulty != null || selectedStage != null || searchQuery.isNotBlank()) {
                            TextButton(
                                onClick = onClearFilters,
                                modifier = Modifier.testTag("btn_reset_filters")
                            ) {
                                Text("عرض الكل")
                            }
                        }
                    }

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(vertical = 4.dp)
                    ) {
                        item {
                            PhonemeCircleChip(
                                letter = "الكل",
                                label = "جميع الأصوات",
                                accentColor = MaterialTheme.colorScheme.primary,
                                isSelected = selectedPhoneme == null,
                                onClick = { onSelectPhoneme(null) },
                                tag = "phoneme_filter_all"
                            )
                        }
                        items(phonemeCatalog, key = { it.letter }) { info ->
                            PhonemeCircleChip(
                                letter = info.letter,
                                label = info.nameArabic.removePrefix("حرف "),
                                accentColor = phonemeAccentColor(info.letter),
                                isSelected = selectedPhoneme == info.letter,
                                onClick = {
                                    onSelectPhoneme(if (selectedPhoneme == info.letter) null else info.letter)
                                },
                                tag = "phoneme_filter_${info.letter}"
                            )
                        }
                    }
                }
            }

            // 3. Selected Phoneme Clinical & Articulator Guide Card (when a phoneme like س، ش، ر، ل is selected)
            item {
                AnimatedVisibility(visible = activePhonemeInfo != null) {
                    activePhonemeInfo?.let { info ->
                        PhonemeClinicalGuideCard(
                            info = info,
                            onPlaySoundCue = {
                                onSpeakText(info.soundCueOnomatopoeia, true, info.letter)
                            }
                        )
                    }
                }
            }

            // 4. Classification Filters: Difficulty Level & Developmental Stage + Search Bar
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Search bar
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = onSearchQueryChange,
                            placeholder = { Text("ابحث عن صوت، كلمة (مثل: سمكة، شمس، قطار، ليمون)...") },
                            leadingIcon = {
                                Icon(Icons.Default.Search, contentDescription = null)
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { onSearchQueryChange("") }) {
                                        Icon(Icons.Default.Clear, contentDescription = "مسح البحث")
                                    }
                                }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("library_search_input")
                        )

                        // Filter by Difficulty Level
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.FilterAlt,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "التصنيف حسب درجة الصعوبة:",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            item {
                                FilterChip(
                                    selected = selectedDifficulty == null,
                                    onClick = { onSelectDifficulty(null) },
                                    label = { Text("كل المستويات") },
                                    modifier = Modifier.testTag("diff_filter_all")
                                )
                            }
                            items(DifficultyLevel.entries) { diff ->
                                FilterChip(
                                    selected = selectedDifficulty == diff,
                                    onClick = {
                                        onSelectDifficulty(if (selectedDifficulty == diff) null else diff)
                                    },
                                    label = { Text("${diff.labelArabic} (${diff.subtitleArabic.substringBefore(" ")})") },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                                    ),
                                    modifier = Modifier.testTag("diff_filter_${diff.name}")
                                )
                            }
                        }

                        // Filter by Developmental Stage
                        Text(
                            text = "التصنيف حسب المرحلة التطورية (العمر النطقي):",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            item {
                                FilterChip(
                                    selected = selectedStage == null,
                                    onClick = { onSelectStage(null) },
                                    label = { Text("كل الأعمار") },
                                    modifier = Modifier.testTag("stage_filter_all")
                                )
                            }
                            items(DevelopmentalStage.entries) { stage ->
                                FilterChip(
                                    selected = selectedStage == stage,
                                    onClick = {
                                        onSelectStage(if (selectedStage == stage) null else stage)
                                    },
                                    label = { Text(stage.shortBadge) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer
                                    ),
                                    modifier = Modifier.testTag("stage_filter_${stage.name}")
                                )
                            }
                        }
                    }
                }
            }

            // 5. Section Header showing count of matching interactive exercises
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "مكتبة تمارين النطق التفاعلية (${exercises.size})",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = "رسوم وأصوات تفاعلية",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // 6. Empty state if filter matches nothing
            if (exercises.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.RecordVoiceOver,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(44.dp)
                            )
                            Text(
                                text = "لا توجد تمارين مطابقة لخيارات التصفية الحالية",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Button(onClick = onClearFilters) {
                                Text("إعادة ضبط الفلاتر وعرض كل المكتبة")
                            }
                        }
                    }
                }
            } else {
                items(exercises, key = { it.id }) { exercise ->
                    val progress = progressList.find { it.exerciseId == exercise.id }
                    InteractiveExerciseLibraryCard(
                        exercise = exercise,
                        starsEarned = progress?.starsEarned ?: 0,
                        onStartExercise = { onOpenExercise(exercise) },
                        onQuickListen = { word ->
                            onSpeakText(word, false, exercise.phonemeLetter)
                        },
                        onDeleteCustom = if (exercise.id.startsWith("custom_")) {
                            { onDeleteCustomExercise(exercise.id) }
                        } else null
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(72.dp))
            }
        }
    }

    if (showAddExerciseDialog) {
        AddCustomExerciseDialog(
            phonemeCatalog = phonemeCatalog,
            initialPhoneme = selectedPhoneme ?: "س",
            onDismiss = { showAddExerciseDialog = false },
            onConfirm = { phoneme, title, word, syllables, pos, diff, stage, tip ->
                onAddCustomExercise(phoneme, title, word, syllables, pos, diff, stage, tip)
                showAddExerciseDialog = false
            }
        )
    }
}

@Composable
private fun HeroWelcomeBannerCard(
    child: ChildProfileEntity?,
    totalExercisesCount: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = R.drawable.img_hero_banner_1790518407219),
                contentDescription = "لوحة العلاج الأرطوفوني",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(195.dp),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(195.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF042F2E).copy(alpha = 0.55f),
                                Color(0xFF042F2E).copy(alpha = 0.88f)
                            )
                        )
                    )
                    .padding(18.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = Color.White.copy(alpha = 0.22f),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = "المنهج الأرطوفوني التفاعلي الحديث",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                            )
                        }

                        Surface(
                            color = Color(0xFFF59E0B),
                            shape = RoundedCornerShape(50)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "${child?.totalStars ?: 18} نجمة",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "مرحباً بالبطل ${child?.name ?: "ياسين"}!",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = child?.clinicalGoal
                                ?: "مكتبة تفاعلية شاملة لأصوات (س، ش، ر، ل، ك، ق، ج، ف، ب، ع) مصنفة حسب الصعوبة والمرحلة التطورية.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.92f),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = Color.White.copy(alpha = 0.18f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "الأصوات المستهدفة: ${child?.targetPhonemesCsv ?: "س، ش، ر، ل"}",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color(0xFFCCFBF1),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                        Surface(
                            color = Color.White.copy(alpha = 0.18f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "$totalExercisesCount تمرين تفاعلي",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color(0xFFFEF3C7),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PhonemeCircleChip(
    letter: String,
    label: String,
    accentColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    tag: String
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .border(
                width = if (isSelected) 2.5.dp else 1.dp,
                color = if (isSelected) accentColor else MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(18.dp)
            )
            .testTag(tag),
        color = if (isSelected) accentColor.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) accentColor else accentColor.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = letter,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color.White else accentColor
                )
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun PhonemeClinicalGuideCard(
    info: PhonemeInfo,
    onPlaySoundCue: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("phoneme_clinical_guide_card"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${info.nameArabic} (${info.phoneticCategory})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = info.developmentalStage.titleArabic,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Button(
                    onClick = onPlaySoundCue,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = info.soundCueOnomatopoeia.substringBefore("—").trim(),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            ArticulatorMouthDiagramCanvas(
                placementType = info.tonguePlacementType,
                phonemeLetter = info.letter
            )

            Text(
                text = "• موضع اللسان: ${info.tonguePositionGuide}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "• شكل الشفاه والهواء: ${info.lipShapeGuide} — ${info.airflowGuide}",
                style = MaterialTheme.typography.bodySmall
            )
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.55f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "💡 نصيحة التصحيح الأرطوفوني: ${info.commonErrorCorrection}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun InteractiveExerciseLibraryCard(
    exercise: InteractiveExercise,
    starsEarned: Int,
    onStartExercise: () -> Unit,
    onQuickListen: (String) -> Unit,
    onDeleteCustom: (() -> Unit)?
) {
    val accent = phonemeAccentColor(exercise.phonemeLetter)
    val (diffBg, diffText) = when (exercise.difficulty) {
        DifficultyLevel.BEGINNER -> DifficultyEasyBg to DifficultyEasyText
        DifficultyLevel.INTERMEDIATE -> DifficultyMediumBg to DifficultyMediumText
        DifficultyLevel.ADVANCED -> DifficultyHardBg to DifficultyHardText
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onStartExercise() }
            .testTag("exercise_card_${exercise.id}"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Top Row: Phoneme Badge, Title, Stars Earned
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(accent.copy(alpha = 0.15f))
                            .border(1.5.dp, accent, RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = exercise.phonemeLetter,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = accent
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = exercise.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = exercise.subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Star Rating Display
                Row(verticalAlignment = Alignment.CenterVertically) {
                    for (s in 1..3) {
                        Icon(
                            imageVector = if (s <= starsEarned) Icons.Default.Star else Icons.Outlined.StarOutline,
                            contentDescription = null,
                            tint = if (s <= starsEarned) Color(0xFFF59E0B) else MaterialTheme.colorScheme.outlineVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    if (onDeleteCustom != null) {
                        IconButton(
                            onClick = onDeleteCustom,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "حذف التمرين المخصص",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            // Classification Badges Row: Difficulty Level + Developmental Stage + Animation Theme
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Surface(
                    color = diffBg,
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = "المستوى: ${exercise.difficulty.labelArabic}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = diffText,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = "العمر: ${exercise.developmentalStage.shortBadge}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.75f),
                    shape = RoundedCornerShape(50)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = exercise.animationTheme.titleArabic,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            // Quick Tappable Preview Chips of Words/Syllables inside this Exercise
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(exercise.items) { promptItem ->
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onQuickListen(promptItem.textArabic) },
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = "نطق سريع",
                                tint = accent,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = promptItem.textArabic,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            // Action Button to Launch Interactive Animated Exercise
            Button(
                onClick = onStartExercise,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("btn_launch_${exercise.id}"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = accent)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "ابدأ التمرين التفاعلي (${exercise.items.size} خطوات)",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AddCustomExerciseDialog(
    phonemeCatalog: List<PhonemeInfo>,
    initialPhoneme: String,
    onDismiss: () -> Unit,
    onConfirm: (
        phonemeLetter: String,
        title: String,
        targetWord: String,
        syllables: String,
        position: String,
        difficulty: DifficultyLevel,
        stage: DevelopmentalStage,
        tip: String
    ) -> Unit
) {
    var selectedPhoneme by remember { mutableStateOf(initialPhoneme) }
    var title by remember { mutableStateOf("") }
    var targetWord by remember { mutableStateOf("") }
    var syllables by remember { mutableStateOf("") }
    var position by remember { mutableStateOf("أول الكلمة") }
    var difficulty by remember { mutableStateOf(DifficultyLevel.BEGINNER) }
    var stage by remember { mutableStateOf(DevelopmentalStage.STAGE_MIDDLE) }
    var tip by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "إضافة تمرين نطق جديد للمكتبة",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("اختر الحرف المستهدف:", style = MaterialTheme.typography.labelLarge)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(phonemeCatalog) { p ->
                        FilterChip(
                            selected = selectedPhoneme == p.letter,
                            onClick = { selectedPhoneme = p.letter },
                            label = { Text(p.letter) }
                        )
                    }
                }

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("عنوان التمرين (مثال: تدريب كلمة سيارة)") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("custom_ex_title_input")
                )

                OutlinedTextField(
                    value = targetWord,
                    onValueChange = { targetWord = it },
                    label = { Text("الكلمة أو الجملة المستهدفة (مثال: سَيَّارَة)") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("custom_ex_word_input")
                )

                OutlinedTextField(
                    value = syllables,
                    onValueChange = { syllables = it },
                    label = { Text("التقطيع المقطعي (مثال: سَيْ / يَا / رَ / ة)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text("درجة الصعوبة:", style = MaterialTheme.typography.labelLarge)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    DifficultyLevel.entries.forEach { diff ->
                        FilterChip(
                            selected = difficulty == diff,
                            onClick = { difficulty = diff },
                            label = { Text(diff.labelArabic) }
                        )
                    }
                }

                Text("المرحلة التطورية:", style = MaterialTheme.typography.labelLarge)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    DevelopmentalStage.entries.forEach { st ->
                        FilterChip(
                            selected = stage == st,
                            onClick = { stage = st },
                            label = { Text(st.shortBadge) }
                        )
                    }
                }

                OutlinedTextField(
                    value = tip,
                    onValueChange = { tip = it },
                    label = { Text("نصيحة النطق (اختياري)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (targetWord.isNotBlank()) {
                        val finalTitle = title.ifBlank { "تمرين مخصص لحرف ($selectedPhoneme): $targetWord" }
                        val finalSyllables = syllables.ifBlank { targetWord }
                        onConfirm(
                            selectedPhoneme,
                            finalTitle,
                            targetWord,
                            finalSyllables,
                            position,
                            difficulty,
                            stage,
                            tip
                        )
                    }
                },
                modifier = Modifier.testTag("btn_confirm_add_custom_ex")
            ) {
                Text("حفظ في المكتبة")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء")
            }
        }
    )
}

fun phonemeAccentColor(letter: String): Color = when (letter) {
    "س" -> PhonemeSeenColor
    "ش" -> PhonemeSheenColor
    "ر" -> PhonemeRaaColor
    "ل" -> PhonemeLaamColor
    "ك", "ق" -> PhonemeKaafColor
    else -> PhonemeOtherColor
}
