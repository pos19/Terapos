package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FaceRetouchingNatural
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.audio.OrthophonyAudioEngine
import com.example.data.curriculum.OralMotorExercise
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OralMotorAndBreathingScreen(
    oralMotorExercises: List<OralMotorExercise>,
    audioEngine: OrthophonyAudioEngine,
    onCompleteOralOrBreathActivity: (
        category: String,
        phoneme: String,
        title: String,
        stars: Int,
        note: String
    ) -> Unit
) {
    val context = LocalContext.current

    // Interactive Blowing & Sustained Vowel Game State
    val isMicListening by audioEngine.isMicListening.collectAsStateWithLifecycle()
    val micAmplitude by audioEngine.micAmplitude.collectAsStateWithLifecycle()
    var manualBlowBoost by remember { mutableFloatStateOf(0f) }
    var accumulatedAirScore by remember { mutableFloatStateOf(0f) }
    var poppedBubblesCount by remember { mutableIntStateOf(0) }

    // Active Oral-Motor Timer State
    var activeTimerExerciseId by remember { mutableStateOf<String?>(null) }
    var remainingSeconds by remember { mutableIntStateOf(0) }

    val micPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            audioEngine.startMicrophoneRecording(maxSeconds = 8)
        } else {
            manualBlowBoost = 0.75f
        }
    }

    // Countdown timer loop for oral-motor exercise
    LaunchedEffect(activeTimerExerciseId, remainingSeconds) {
        if (activeTimerExerciseId != null && remainingSeconds > 0) {
            delay(1000L)
            remainingSeconds--
            if (remainingSeconds == 0) {
                val finishedEx = oralMotorExercises.find { it.id == activeTimerExerciseId }
                if (finishedEx != null) {
                    onCompleteOralOrBreathActivity(
                        "الجمباز الفموي",
                        finishedEx.helpsWithPhonemes.firstOrNull() ?: "ر",
                        finishedEx.title,
                        3,
                        "أكمل الطفل تمرين الجمباز الفموي (${finishedEx.targetOrgan}) بنجاح"
                    )
                }
                activeTimerExerciseId = null
            }
        }
    }

    // Accumulate blowing energy when mic amplitude or touch-boost is active
    val currentBlowPower = maxOf(micAmplitude, manualBlowBoost)
    LaunchedEffect(currentBlowPower, isMicListening) {
        if (currentBlowPower > 0.12f) {
            accumulatedAirScore = (accumulatedAirScore + currentBlowPower * 0.08f).coerceAtMost(1f)
            if (accumulatedAirScore >= 1f) {
                poppedBubblesCount++
                audioEngine.playBubblePopSound()
                accumulatedAirScore = 0.15f
            }
        }
        if (manualBlowBoost > 0f) {
            delay(350)
            manualBlowBoost = (manualBlowBoost - 0.2f).coerceAtLeast(0f)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("oral_motor_breathing_screen"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Header Banner with Generated Oral-Motor & Breathing Illustration
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_oral_motor_1790518417674),
                        contentDescription = "تدريبات الجمباز الفموي والتنفس",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(175.dp),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(175.dp)
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color(0xFF0F172A).copy(alpha = 0.45f),
                                        Color(0xFF0F172A).copy(alpha = 0.85f)
                                    )
                                )
                            )
                            .padding(16.dp),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Surface(
                                color = MaterialTheme.colorScheme.secondary,
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(
                                    text = "تأهيل عضلات النطق والتنفس الفموي",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                            Text(
                                text = "الجمباز الفموي ومختبر النفخ التفاعلي",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "تقوية مرونة اللسان والشفاه وتنظيم الزفير الفموي اللازم لمخارج (س، ش، ر، ل، ف).",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }
                }
            }
        }

        // 2. Interactive Blowing Bubbles & Feather Biofeedback Game Card
        item {
            InteractiveBlowingBubblesGameCard(
                blowPower = currentBlowPower,
                accumulatedProgress = accumulatedAirScore,
                poppedBubblesCount = poppedBubblesCount,
                isMicActive = isMicListening,
                onToggleMic = {
                    if (isMicListening) {
                        audioEngine.stopMicrophoneRecording()
                    } else {
                        val granted = ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.RECORD_AUDIO
                        ) == PackageManager.PERMISSION_GRANTED
                        if (granted) {
                            audioEngine.startMicrophoneRecording(maxSeconds = 8)
                        } else {
                            micPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                        }
                    }
                },
                onManualBlowTap = {
                    manualBlowBoost = 0.85f
                    accumulatedAirScore = (accumulatedAirScore + 0.28f).coerceAtMost(1f)
                    if (accumulatedAirScore >= 1f) {
                        poppedBubblesCount++
                        audioEngine.playBubblePopSound()
                        accumulatedAirScore = 0.1f
                    }
                },
                onSaveBreathSession = {
                    onCompleteOralOrBreathActivity(
                        "التنفس والنفخ",
                        "س",
                        "لعبة نفخ الفقاعات والمد الصوتي المتصل",
                        3,
                        "أكمل الطفل تفجير $poppedBubblesCount فقاعات هوائية بتحكم تنفسي ممتاز"
                    )
                }
            )
        }

        // 3. Oral-Motor Gym Exercises Header
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.FaceRetouchingNatural,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "تمارين الجمباز الفموي (اللسان، الشفاه، الخدود):",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // 4. Oral-Motor Exercise Cards with Interactive Timer
        items(oralMotorExercises, key = { it.id }) { exercise ->
            val isRunning = activeTimerExerciseId == exercise.id
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("oral_motor_card_${exercise.id}"),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isRunning)
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
                    else MaterialTheme.colorScheme.surface
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
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = exercise.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "العضو المستهدف: ${exercise.targetOrgan} • ${exercise.repetitions} تكرارات",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        IconButton(
                            onClick = {
                                audioEngine.speakArabic(exercise.childFriendlyMetaphor, slowSyllabic = false)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = "سماع الشرح",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.55f),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "🎯 ${exercise.childFriendlyMetaphor}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(12.dp)
                        )
                    }

                    // Step-by-step instructions
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        exercise.stepByStepInstructions.forEachIndexed { idx, step ->
                            Text(
                                text = "${idx + 1}. $step",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Phonemes helped by this exercise
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "يمهد لمخارج:",
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        exercise.helpsWithPhonemes.forEach { ph ->
                            Surface(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = CircleShape
                            ) {
                                Text(
                                    text = ph,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    if (isRunning) {
                        LinearProgressIndicator(
                            progress = {
                                remainingSeconds.toFloat() / exercise.durationSeconds.coerceAtLeast(1)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(50))
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                if (isRunning) {
                                    activeTimerExerciseId = null
                                } else {
                                    activeTimerExerciseId = exercise.id
                                    remainingSeconds = exercise.durationSeconds
                                    audioEngine.playBubblePopSound()
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_timer_${exercise.id}"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isRunning) MaterialTheme.colorScheme.error
                                else MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(
                                imageVector = if (isRunning) Icons.Default.Stop else Icons.Default.Timer,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                if (isRunning) "إيقاف المؤقت ($remainingSeconds ث)"
                                else "ابدأ المؤقت التفاعلي (${exercise.durationSeconds} ث)"
                            )
                        }

                        FilledTonalButton(
                            onClick = {
                                onCompleteOralOrBreathActivity(
                                    "الجمباز الفموي",
                                    exercise.helpsWithPhonemes.firstOrNull() ?: "ر",
                                    exercise.title,
                                    3,
                                    "إتمام تمرين ${exercise.title}"
                                )
                            }
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("إتقان +3★")
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(28.dp)) }
    }
}

@Composable
private fun IconButton(
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .clickable { onClick() },
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = CircleShape
    ) {
        Box(contentAlignment = Alignment.Center) {
            content()
        }
    }
}

@Composable
private fun InteractiveBlowingBubblesGameCard(
    blowPower: Float,
    accumulatedProgress: Float,
    poppedBubblesCount: Int,
    isMicActive: Boolean,
    onToggleMic: () -> Unit,
    onManualBlowTap: () -> Unit,
    onSaveBreathSession: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "bubbles_anim")
    val floatPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "float_phase"
    )
    val smoothProgress by animateFloatAsState(
        targetValue = accumulatedProgress,
        animationSpec = tween(200),
        label = "smooth_breath"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("blowing_bubbles_game_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
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
                        imageVector = Icons.Default.Air,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Column {
                        Text(
                            text = "لعبة نفخ الفقاعات والريشة الطائرة",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "انفخ في الميكروفون أو انقر لإطالة الزفير الفموي",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Surface(
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = "فقاعات: $poppedBubblesCount 🫧",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            // Animated Canvas showing floating bubbles & rising feather
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFFE0F2FE), Color(0xFFCCFBF1))
                        )
                    )
                    .clickable { onManualBlowTap() }
            ) {
                val w = size.width
                val h = size.height

                // Draw floating bubbles that grow with breath progress
                val bubbleCount = 6
                for (i in 0 until bubbleCount) {
                    val bx = w * (0.14f + i * 0.14f) + cos(floatPhase + i).toFloat() * 10.dp.toPx()
                    val by = h * (0.78f - smoothProgress * 0.55f) + sin(floatPhase + i * 0.7f).toFloat() * 12.dp.toPx()
                    val radius = (12 + (i % 3) * 6).dp.toPx() * (0.8f + smoothProgress * 0.6f + blowPower * 0.4f)
                    drawCircle(
                        color = Color(0xFF0EA5E9).copy(alpha = 0.28f),
                        radius = radius,
                        center = Offset(bx, by)
                    )
                    drawCircle(
                        color = Color.White.copy(alpha = 0.75f),
                        radius = radius * 0.3f,
                        center = Offset(bx - radius * 0.3f, by - radius * 0.3f)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onToggleMic,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isMicActive) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("btn_breath_mic")
                ) {
                    Icon(
                        imageVector = if (isMicActive) Icons.Default.Stop else Icons.Default.Mic,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (isMicActive) "إيقاف الميكروفون" else "النفخ بالميكروفون")
                }

                FilledTonalButton(
                    onClick = onManualBlowTap,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("btn_manual_blow")
                ) {
                    Icon(Icons.Default.Air, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("نفخة تفاعلية 💨")
                }
            }

            if (poppedBubblesCount > 0) {
                Button(
                    onClick = onSaveBreathSession,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("حفظ إنجاز التنفس ($poppedBubblesCount فقاعات) +3 نجوم")
                }
            }
        }
    }
}
