package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.NavigateBefore
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SlowMotionVideo
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.audio.OrthophonyAudioEngine
import com.example.data.curriculum.InteractiveExercise
import com.example.ui.components.ArticulatorMouthDiagramCanvas
import com.example.ui.components.InteractiveSoundGameCanvas
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InteractiveExercisePlayerScreen(
    exercise: InteractiveExercise,
    audioEngine: OrthophonyAudioEngine,
    onClose: () -> Unit,
    onCompleteExercise: (stars: Int, accuracy: Int, note: String) -> Unit
) {
    // Mandatory BackHandler for sub-screen navigation
    BackHandler {
        onClose()
    }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var currentStepIndex by remember(exercise.id) { mutableIntStateOf(0) }
    val currentItem = exercise.items.getOrElse(currentStepIndex) { exercise.items.first() }

    var showMouthDiagram by remember { mutableStateOf(true) }
    var tapEnergyBoost by remember { mutableFloatStateOf(0f) }
    var selectedStars by remember { mutableIntStateOf(3) }
    var therapistNote by remember { mutableStateOf("") }
    var showCompletionCelebration by remember { mutableStateOf(false) }
    var micPermissionMessage by remember { mutableStateOf<String?>(null) }

    val isMicListening by audioEngine.isMicListening.collectAsStateWithLifecycle()
    val micAmplitude by audioEngine.micAmplitude.collectAsStateWithLifecycle()
    val micWaveform by audioEngine.micWaveformHistory.collectAsStateWithLifecycle()
    val hasRecordedVoice by audioEngine.hasRecordedChildVoice.collectAsStateWithLifecycle()
    val isPlayingRecordedVoice by audioEngine.isPlayingChildVoice.collectAsStateWithLifecycle()

    val micPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            micPermissionMessage = null
            audioEngine.startMicrophoneRecording(maxSeconds = 5)
        } else {
            micPermissionMessage = "يمكنك استخدام أزرار النطق التفاعلية واللمس مباشرة بدون ميكروفون!"
        }
    }

    fun triggerFunTapAnimation() {
        scope.launch {
            tapEnergyBoost = 0.85f
            delay(450)
            tapEnergyBoost = 0.35f
            delay(350)
            tapEnergyBoost = 0f
        }
    }

    // Play welcome sound when stepping to a new item
    LaunchedEffect(currentStepIndex) {
        triggerFunTapAnimation()
    }

    val effectiveEnergy = maxOf(micAmplitude, tapEnergyBoost)
    val progressFraction = (currentStepIndex + 1).toFloat() / exercise.items.size.coerceAtLeast(1)

    val syllables = remember(currentItem.syllabicBreakdown) {
        currentItem.syllabicBreakdown
            .split("/", "—", "≠")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("exercise_player_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .testTag("close_exercise_player_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "رجوع إلى المكتبة"
                    )
                }
                Column {
                    Text(
                        text = exercise.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${exercise.phonemeName} • ${exercise.difficulty.labelArabic} • ${exercise.developmentalStage.shortBadge}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    text = "${currentStepIndex + 1} / ${exercise.items.size}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }

        // Progress Bar
        LinearProgressIndicator(
            progress = { progressFraction },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(50)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )

        // 1. Interactive Animated Sound Game Canvas
        InteractiveSoundGameCanvas(
            animationTheme = exercise.animationTheme,
            phonemeLetter = exercise.phonemeLetter,
            currentPromptText = currentItem.textArabic,
            progressFraction = progressFraction,
            liveEnergy = effectiveEnergy,
            onCanvasTapped = {
                triggerFunTapAnimation()
                audioEngine.speakArabic(
                    text = currentItem.textArabic,
                    slowSyllabic = false,
                    phonemeHint = exercise.phonemeLetter
                )
            }
        )

        // 2. Stepper Pills for All Items in the Exercise
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 2.dp)
        ) {
            itemsIndexed(exercise.items) { idx, item ->
                val isSelected = idx == currentStepIndex
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .clickable {
                            currentStepIndex = idx
                            audioEngine.speakArabic(
                                item.textArabic,
                                slowSyllabic = false,
                                phonemeHint = exercise.phonemeLetter
                            )
                        }
                        .testTag("prompt_step_chip_$idx"),
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = "${idx + 1}. ${item.textArabic}",
                        style = MaterialTheme.typography.labelLarge,
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                    )
                }
            }
        }

        // 3. Main Interactive Target Word & Syllable Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Position Tag & Visual Cue Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = currentItem.positionTag,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }

                    Surface(
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = currentItem.visualCueLabel,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                }

                // Giant Target Word Display (tappable to speak)
                val wordScale by animateFloatAsState(
                    targetValue = if (effectiveEnergy > 0.25f) 1.06f else 1f,
                    animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                    label = "word_scale"
                )
                Text(
                    text = currentItem.textArabic,
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .scale(wordScale)
                        .clickable {
                            triggerFunTapAnimation()
                            audioEngine.speakArabic(
                                currentItem.textArabic,
                                slowSyllabic = false,
                                phonemeHint = exercise.phonemeLetter
                            )
                        }
                        .padding(vertical = 4.dp)
                )

                // Interactive Tappable Syllable Breakdown Chips
                Text(
                    text = "انقر على كل مقطع صوتي للاستماع والتدريب المقطعي:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    syllables.forEachIndexed { sIdx, syllable ->
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .clickable {
                                    triggerFunTapAnimation()
                                    audioEngine.speakArabic(
                                        text = syllable,
                                        slowSyllabic = true,
                                        phonemeHint = exercise.phonemeLetter
                                    )
                                }
                                .testTag("syllable_chip_$sIdx"),
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                    contentDescription = "نطق المقطع",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = syllable,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                }

                // Helper Articulation Tip for this item
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = currentItem.helperTip,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Dual Audio Action Buttons (Normal Speed + Slow Syllabic Speed)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            triggerFunTapAnimation()
                            audioEngine.speakArabic(
                                text = currentItem.textArabic,
                                slowSyllabic = false,
                                phonemeHint = exercise.phonemeLetter
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_speak_normal"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("نطق طبيعي")
                    }

                    FilledTonalButton(
                        onClick = {
                            triggerFunTapAnimation()
                            audioEngine.speakArabic(
                                text = currentItem.syllabicBreakdown,
                                slowSyllabic = true,
                                phonemeHint = exercise.phonemeLetter
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_speak_slow")
                    ) {
                        Icon(
                            imageVector = Icons.Default.SlowMotionVideo,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("نطق مقطعي بطيء")
                    }
                }
            }
        }

        // 4. Interactive Microphone Voice Recording & Biofeedback Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Column {
                            Text(
                                text = "مختبر صوت الطفل التفاعلي",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (isMicListening) "تحدث الآن! اللعبة تتحرك مع صوتك..."
                                else "سجّل نطق الطفل لتحريك اللعبة ومقارنة صوته",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Live Waveform Indicator Bars
                if (isMicListening) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        micWaveform.forEach { amp ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height((8 + amp * 32).dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(MaterialTheme.colorScheme.secondary)
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            if (isMicListening) {
                                audioEngine.stopMicrophoneRecording()
                            } else {
                                val hasPerm = ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.RECORD_AUDIO
                                ) == PackageManager.PERMISSION_GRANTED
                                if (hasPerm) {
                                    audioEngine.startMicrophoneRecording(maxSeconds = 5)
                                } else {
                                    micPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isMicListening) MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.secondary
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_record_child_voice")
                    ) {
                        Icon(
                            imageVector = if (isMicListening) Icons.Default.Stop else Icons.Default.Mic,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (isMicListening) "إيقاف التسجيل" else "تسجيل صوت الطفل")
                    }

                    if (hasRecordedVoice) {
                        OutlinedButton(
                            onClick = { audioEngine.playRecordedChildVoice() },
                            enabled = !isPlayingRecordedVoice,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_play_recorded_voice")
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (isPlayingRecordedVoice) "جاري التشغيل..." else "سماع صوت الطفل")
                        }
                    }
                }

                micPermissionMessage?.let { msg ->
                    Text(
                        text = msg,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // 5. Previous / Next Item Navigation Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(
                onClick = {
                    if (currentStepIndex > 0) currentStepIndex--
                },
                enabled = currentStepIndex > 0,
                modifier = Modifier.testTag("btn_prev_item")
            ) {
                Icon(Icons.AutoMirrored.Filled.NavigateBefore, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("السابق")
            }

            OutlinedButton(
                onClick = { showMouthDiagram = !showMouthDiagram },
                modifier = Modifier.testTag("btn_toggle_mouth_diagram")
            ) {
                Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (showMouthDiagram) "إخفاء المخرج" else "مخرج الحرف")
            }

            Button(
                onClick = {
                    if (currentStepIndex < exercise.items.lastIndex) {
                        currentStepIndex++
                        audioEngine.playBubblePopSound()
                    } else {
                        showCompletionCelebration = true
                        audioEngine.playStarRewardSound(selectedStars)
                    }
                },
                modifier = Modifier.testTag("btn_next_item")
            ) {
                Text(if (currentStepIndex < exercise.items.lastIndex) "التالي" else "إنهاء وتقييم")
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.AutoMirrored.Filled.NavigateNext, contentDescription = null)
            }
        }

        // 6. Collapsible Anatomical Mouth & Tongue Visualizer + Therapist Guide
        AnimatedVisibility(visible = showMouthDiagram) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                ArticulatorMouthDiagramCanvas(
                    placementType = exercise.tonguePlacementType,
                    phonemeLetter = exercise.phonemeLetter
                )
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "إرشاد الأخصائي الأرطوفوني:",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = exercise.therapistTip,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // 7. Interactive Star Evaluation & Save Progress Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (showCompletionCelebration)
                    MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.7f)
                else MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Celebration,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                    Text(
                        text = "تقييم أداء الطفل وحفظ النجوم",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                // 3-Star Selector
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val starLabels = listOf(
                        1 to "محاولة جيدة (1)",
                        2 to "نطق بمساعدة (2)",
                        3 to "نطق متقن (3)"
                    )
                    starLabels.forEach { (starCount, label) ->
                        val isChosen = selectedStars == starCount
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .clickable {
                                    selectedStars = starCount
                                    audioEngine.playStarRewardSound(starCount)
                                }
                                .border(
                                    width = if (isChosen) 2.dp else 1.dp,
                                    color = if (isChosen) MaterialTheme.colorScheme.tertiary
                                    else MaterialTheme.colorScheme.outlineVariant,
                                    shape = RoundedCornerShape(14.dp)
                                )
                                .testTag("rate_star_option_$starCount"),
                            color = if (isChosen) MaterialTheme.colorScheme.tertiaryContainer
                            else MaterialTheme.colorScheme.surface
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row {
                                    repeat(starCount) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = null,
                                            tint = Color(0xFFF59E0B),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = therapistNote,
                    onValueChange = { therapistNote = it },
                    label = { Text("ملاحظة الأخصائي أو ولي الأمر (اختياري)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("therapist_note_input"),
                    singleLine = true
                )

                Button(
                    onClick = {
                        val accuracy = when (selectedStars) {
                            3 -> 95
                            2 -> 80
                            else -> 65
                        }
                        onCompleteExercise(
                            selectedStars,
                            accuracy,
                            therapistNote.ifBlank { "تم إتمام تمرين ${exercise.title} بنجاح" }
                        )
                        onClose()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_save_exercise_progress"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("حفظ التقدم وإضافة $selectedStars نجوم لملف الطفل")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
