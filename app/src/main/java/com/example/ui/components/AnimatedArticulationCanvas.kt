package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.curriculum.AnimationTheme
import com.example.data.curriculum.TonguePlacementType
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

/**
 * Interactive animated game canvas tailored to each phoneme theme (س، ش، ر، ل، ك، ق...)
 * Encourages child engagement with live motion, sound waves, and star rewards.
 */
@Composable
fun InteractiveSoundGameCanvas(
    animationTheme: AnimationTheme,
    phonemeLetter: String,
    currentPromptText: String,
    progressFraction: Float, // 0f .. 1f across exercise items
    liveEnergy: Float,       // 0f .. 1f from microphone or interactive tap boost
    onCanvasTapped: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "sound_game_anim")
    val wavePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2f * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave_phase"
    )
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 750, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    val animatedProgress by animateFloatAsState(
        targetValue = progressFraction.coerceIn(0.08f, 1f),
        animationSpec = spring(dampingRatio = 0.65f, stiffness = 280f),
        label = "progress_spring"
    )
    val animatedEnergy by animateFloatAsState(
        targetValue = liveEnergy.coerceIn(0f, 1f),
        animationSpec = tween(150),
        label = "energy_smooth"
    )

    val skyColors = when (animationTheme) {
        AnimationTheme.SNAKE_TRAIN -> listOf(Color(0xFFE0F2FE), Color(0xFFBAE6FD), Color(0xFFCCFBF1))
        AnimationTheme.OCEAN_PEARL -> listOf(Color(0xFFEDE9FE), Color(0xFFDDD6FE), Color(0xFFCFFAFE))
        AnimationTheme.RACE_CAR -> listOf(Color(0xFFFFF1F2), Color(0xFFFFE4E6), Color(0xFFFEF3C7))
        AnimationTheme.MUSICAL_BIRD -> listOf(Color(0xFFECFDF5), Color(0xFFD1FAE5), Color(0xFFFEF9C3))
        AnimationTheme.STAR_ROCKET -> listOf(Color(0xFF1E1B4B), Color(0xFF312E81), Color(0xFF0F766E))
    }

    val isDarkScene = animationTheme == AnimationTheme.STAR_ROCKET

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .clickable { onCanvasTapped() }
            .testTag("interactive_sound_game_canvas"),
        shape = RoundedCornerShape(24.dp),
        tonalElevation = 4.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(205.dp)
                .background(Brush.verticalGradient(skyColors))
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                // Draw milestone stars along the track
                val milestoneCount = 5
                for (i in 0 until milestoneCount) {
                    val starX = w * (0.14f + i * 0.18f)
                    val starY = h * 0.24f + sin(wavePhase + i).toFloat() * 6.dp.toPx()
                    val isReached = animatedProgress >= (i.toFloat() / (milestoneCount - 1)) - 0.05f
                    val starColor = if (isReached) Color(0xFFFBBF24) else Color.White.copy(alpha = 0.45f)
                    drawCircle(
                        color = starColor,
                        radius = if (isReached) 11.dp.toPx() * pulseScale else 7.dp.toPx(),
                        center = Offset(starX, starY)
                    )
                    if (isReached) {
                        drawCircle(
                            color = Color(0xFFF59E0B).copy(alpha = 0.3f),
                            radius = 17.dp.toPx() * pulseScale,
                            center = Offset(starX, starY)
                        )
                    }
                }

                when (animationTheme) {
                    AnimationTheme.SNAKE_TRAIN -> {
                        // Track & undulating sibilant sound wave for السين (س)
                        val wavePath = Path()
                        val baseY = h * 0.68f
                        wavePath.moveTo(0f, baseY)
                        val amp = (12.dp.toPx() + animatedEnergy * 24.dp.toPx())
                        var x = 0f
                        while (x <= w) {
                            val y = baseY + sin((x / w) * 4 * PI + wavePhase * 2).toFloat() * amp * 0.45f
                            wavePath.lineTo(x, y)
                            x += 12f
                        }
                        drawPath(
                            path = wavePath,
                            color = Color(0xFF0284C7).copy(alpha = 0.45f),
                            style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
                        )

                        // Friendly Sound-Wave Snake/Train segments moving across
                        val headX = w * (0.15f + animatedProgress * 0.72f)
                        val headY = baseY + sin(wavePhase * 2).toFloat() * (8.dp.toPx() + animatedEnergy * 16.dp.toPx())
                        for (seg in 5 downTo 0) {
                            val segX = headX - seg * 20.dp.toPx()
                            val segY = baseY + sin(wavePhase * 2 - seg * 0.55f).toFloat() * (8.dp.toPx() + animatedEnergy * 15.dp.toPx())
                            val segColor = if (seg == 0) Color(0xFF0D9488) else if (seg % 2 == 0) Color(0xFF14B8A6) else Color(0xFF0EA5E9)
                            drawCircle(
                                color = segColor,
                                radius = (if (seg == 0) 20.dp else 15.dp).toPx() * (1f + animatedEnergy * 0.15f),
                                center = Offset(segX, segY)
                            )
                        }
                        // Cute eyes on head
                        drawCircle(Color.White, radius = 5.dp.toPx(), center = Offset(headX + 6.dp.toPx(), headY - 6.dp.toPx()))
                        drawCircle(Color(0xFF0F172A), radius = 2.5.dp.toPx(), center = Offset(headX + 7.dp.toPx(), headY - 6.dp.toPx()))

                        // Floating sibilant bubbles
                        for (b in 1..4) {
                            val bx = (headX + b * 28.dp.toPx() + cos(wavePhase + b).toFloat() * 10.dp.toPx()) % w
                            val by = headY - 20.dp.toPx() - b * 8.dp.toPx()
                            drawCircle(
                                color = Color.White.copy(alpha = 0.75f),
                                radius = (6 + b * 2).dp.toPx() * (1f + animatedEnergy * 0.3f),
                                center = Offset(bx, by)
                            )
                        }
                    }

                    AnimationTheme.OCEAN_PEARL -> {
                        // Warm spreading ocean waves for الشين (ش) & الباء (ب)
                        for (layer in 0..2) {
                            val path = Path()
                            val layerY = h * (0.58f + layer * 0.12f)
                            path.moveTo(0f, h)
                            path.lineTo(0f, layerY)
                            var x = 0f
                            while (x <= w) {
                                val wy = layerY + sin((x / w) * 3 * PI + wavePhase + layer).toFloat() *
                                    (10.dp.toPx() + animatedEnergy * 18.dp.toPx())
                                path.lineTo(x, wy)
                                x += 15f
                            }
                            path.lineTo(w, h)
                            path.close()
                            val waveColor = when (layer) {
                                0 -> Color(0xFF8B5CF6).copy(alpha = 0.25f)
                                1 -> Color(0xFF06B6D4).copy(alpha = 0.35f)
                                else -> Color(0xFF0EA5E9).copy(alpha = 0.45f)
                            }
                            drawPath(path, color = waveColor)
                        }

                        // Glowing Pearl Shell that opens with speech/progress
                        val shellCenter = Offset(w * (0.2f + animatedProgress * 0.6f), h * 0.60f)
                        val openAngleOffset = (10.dp.toPx() + animatedEnergy * 22.dp.toPx())
                        // Bottom shell
                        drawRoundRect(
                            color = Color(0xFFF43F5E),
                            topLeft = Offset(shellCenter.x - 28.dp.toPx(), shellCenter.y),
                            size = Size(56.dp.toPx(), 20.dp.toPx()),
                            cornerRadius = CornerRadius(14.dp.toPx(), 14.dp.toPx())
                        )
                        // Glowing Pearl inside
                        drawCircle(
                            color = Color(0xFFFEF08A),
                            radius = (13.dp.toPx() * pulseScale) + animatedEnergy * 6.dp.toPx(),
                            center = Offset(shellCenter.x, shellCenter.y - 6.dp.toPx())
                        )
                        // Top shell lid lifting up
                        drawRoundRect(
                            color = Color(0xFFFB7185),
                            topLeft = Offset(shellCenter.x - 28.dp.toPx(), shellCenter.y - 18.dp.toPx() - openAngleOffset),
                            size = Size(56.dp.toPx(), 18.dp.toPx()),
                            cornerRadius = CornerRadius(14.dp.toPx(), 14.dp.toPx())
                        )
                    }

                    AnimationTheme.RACE_CAR -> {
                        // Road track for الراء (ر)
                        val roadY = h * 0.72f
                        drawLine(
                            color = Color(0xFF334155),
                            start = Offset(16.dp.toPx(), roadY),
                            end = Offset(w - 16.dp.toPx(), roadY),
                            strokeWidth = 18.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                        drawLine(
                            color = Color(0xFFFBBF24),
                            start = Offset(24.dp.toPx(), roadY),
                            end = Offset(w - 24.dp.toPx(), roadY),
                            strokeWidth = 3.dp.toPx(),
                            cap = StrokeCap.Round
                        )

                        // Trill vibration effect when speaking or tapping
                        val trillShake = sin(wavePhase * 8f).toFloat() * (2.dp.toPx() + animatedEnergy * 6.dp.toPx())
                        val carX = w * (0.16f + animatedProgress * 0.68f)
                        val carY = roadY - 18.dp.toPx() + trillShake

                        // Exhaust sound puffs (رررر)
                        for (puff in 1..3) {
                            drawCircle(
                                color = Color(0xFFFB923C).copy(alpha = 0.45f / puff),
                                radius = (8 + puff * 5).dp.toPx() * (1f + animatedEnergy * 0.5f),
                                center = Offset(carX - 32.dp.toPx() - puff * 14.dp.toPx(), carY + 4.dp.toPx())
                            )
                        }

                        // Race car body
                        drawRoundRect(
                            color = Color(0xFFE11D48),
                            topLeft = Offset(carX - 30.dp.toPx(), carY - 12.dp.toPx()),
                            size = Size(60.dp.toPx(), 24.dp.toPx()),
                            cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx())
                        )
                        drawRoundRect(
                            color = Color(0xFF38BDF8),
                            topLeft = Offset(carX - 14.dp.toPx(), carY - 24.dp.toPx()),
                            size = Size(30.dp.toPx(), 15.dp.toPx()),
                            cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
                        )
                        // Wheels
                        drawCircle(Color(0xFF0F172A), radius = 9.dp.toPx(), center = Offset(carX - 16.dp.toPx(), carY + 12.dp.toPx()))
                        drawCircle(Color(0xFF0F172A), radius = 9.dp.toPx(), center = Offset(carX + 16.dp.toPx(), carY + 12.dp.toPx()))
                        drawCircle(Color.White, radius = 4.dp.toPx(), center = Offset(carX - 16.dp.toPx(), carY + 12.dp.toPx()))
                        drawCircle(Color.White, radius = 4.dp.toPx(), center = Offset(carX + 16.dp.toPx(), carY + 12.dp.toPx()))
                    }

                    AnimationTheme.MUSICAL_BIRD -> {
                        // Tree branch & hopping musical bird for اللام (ل)
                        val branchY = h * 0.74f
                        drawLine(
                            color = Color(0xFF15803D),
                            start = Offset(20.dp.toPx(), branchY),
                            end = Offset(w - 20.dp.toPx(), branchY),
                            strokeWidth = 8.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                        val hopArc = abs(sin(wavePhase * 1.5f)).toFloat() * (14.dp.toPx() + animatedEnergy * 24.dp.toPx())
                        val birdX = w * (0.16f + animatedProgress * 0.68f)
                        val birdY = branchY - 18.dp.toPx() - hopArc

                        // Bird body
                        drawCircle(
                            color = Color(0xFF10B981),
                            radius = 20.dp.toPx() * (1f + animatedEnergy * 0.12f),
                            center = Offset(birdX, birdY)
                        )
                        // Wing
                        drawCircle(
                            color = Color(0xFF059669),
                            radius = 11.dp.toPx(),
                            center = Offset(birdX - 6.dp.toPx(), birdY + 2.dp.toPx())
                        )
                        // Beak & eye
                        drawCircle(Color(0xFFF59E0B), radius = 6.dp.toPx(), center = Offset(birdX + 19.dp.toPx(), birdY - 2.dp.toPx()))
                        drawCircle(Color.White, radius = 5.dp.toPx(), center = Offset(birdX + 8.dp.toPx(), birdY - 6.dp.toPx()))
                        drawCircle(Color(0xFF0F172A), radius = 2.5.dp.toPx(), center = Offset(birdX + 9.dp.toPx(), birdY - 6.dp.toPx()))
                    }

                    AnimationTheme.STAR_ROCKET -> {
                        // Starry space & rocket for الكاف (ك)، القاف (ق)، الجيم (ج)، العين (ع)
                        val rocketX = w * (0.16f + animatedProgress * 0.68f)
                        val rocketY = h * 0.56f + sin(wavePhase * 1.8f).toFloat() * 10.dp.toPx() - animatedEnergy * 18.dp.toPx()

                        // Flame booster
                        drawCircle(
                            color = Color(0xFFF97316),
                            radius = (14.dp.toPx() * pulseScale) + animatedEnergy * 14.dp.toPx(),
                            center = Offset(rocketX - 28.dp.toPx(), rocketY)
                        )
                        drawCircle(
                            color = Color(0xFFFACC15),
                            radius = 9.dp.toPx() + animatedEnergy * 8.dp.toPx(),
                            center = Offset(rocketX - 24.dp.toPx(), rocketY)
                        )
                        // Rocket body
                        drawRoundRect(
                            color = Color(0xFFF8FAFC),
                            topLeft = Offset(rocketX - 22.dp.toPx(), rocketY - 14.dp.toPx()),
                            size = Size(50.dp.toPx(), 28.dp.toPx()),
                            cornerRadius = CornerRadius(14.dp.toPx(), 14.dp.toPx())
                        )
                        drawCircle(
                            color = Color(0xFF38BDF8),
                            radius = 7.dp.toPx(),
                            center = Offset(rocketX + 4.dp.toPx(), rocketY)
                        )
                    }
                }
            }

            // Top overlay header inside canvas showing theme name & interactive tap hint
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = if (isDarkScene) Color.White.copy(alpha = 0.18f) else Color.White.copy(alpha = 0.85f),
                    shape = RoundedCornerShape(50)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = if (isDarkScene) Color(0xFFFBBF24) else MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = animationTheme.titleArabic,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkScene) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Surface(
                    color = MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(50)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.TouchApp,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "المس للتحريك والصوت",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White
                        )
                    }
                }
            }

            // Bottom floating phoneme & active prompt badge
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Surface(
                    color = Color.White.copy(alpha = 0.92f),
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = phonemeLetter,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Column {
                                Text(
                                    text = currentPromptText,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F172A)
                                )
                                Text(
                                    text = animationTheme.instructionArabic,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF475569),
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Interactive Anatomical Vocal Tract & Tongue Placement Visualizer
 * Helps children and parents see where the tongue tip/blade/back should be placed.
 */
@Composable
fun ArticulatorMouthDiagramCanvas(
    placementType: TonguePlacementType,
    phonemeLetter: String,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "mouth_diagram")
    val animCycle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "tongue_cycle"
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "المحاكي البصري لمخرج حرف ($phonemeLetter)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = "حركة اللسان والهواء",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(125.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFFFF1F2))
            ) {
                val w = size.width
                val h = size.height

                // Roof of mouth (Hard & Soft Palate)
                val palatePath = Path().apply {
                    moveTo(w * 0.22f, h * 0.36f)
                    quadraticTo(w * 0.50f, h * 0.10f, w * 0.82f, h * 0.45f)
                }
                drawPath(
                    path = palatePath,
                    color = Color(0xFFFB7185),
                    style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                )

                // Upper & Lower Front Teeth (on the left side of diagram)
                drawRoundRect(
                    color = Color.White,
                    topLeft = Offset(w * 0.18f, h * 0.32f),
                    size = Size(12.dp.toPx(), 18.dp.toPx()),
                    cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                )
                drawRoundRect(
                    color = Color.White,
                    topLeft = Offset(w * 0.18f, h * 0.62f),
                    size = Size(12.dp.toPx(), 18.dp.toPx()),
                    cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                )

                // Dynamic Tongue Path according to TonguePlacementType
                val tonguePath = Path()
                when (placementType) {
                    TonguePlacementType.ALVEOLAR_GROOVE_SEEN -> {
                        // Tongue tip behind lower teeth, narrow stream through closed teeth
                        tonguePath.moveTo(w * 0.80f, h * 0.78f)
                        tonguePath.quadraticTo(
                            w * 0.50f,
                            h * (0.48f - animCycle * 0.05f),
                            w * 0.23f,
                            h * 0.65f
                        )
                    }
                    TonguePlacementType.PALATAL_WIDE_SHEEN -> {
                        // Middle blade raised toward roof of mouth
                        tonguePath.moveTo(w * 0.80f, h * 0.78f)
                        tonguePath.quadraticTo(
                            w * 0.48f,
                            h * (0.25f - animCycle * 0.06f),
                            w * 0.26f,
                            h * 0.54f
                        )
                    }
                    TonguePlacementType.ALVEOLAR_TRILL_RAA -> {
                        // Tongue tip vibrating rapidly against upper alveolar ridge
                        val trillY = h * (0.34f + sin(animCycle * PI * 4).toFloat() * 0.07f)
                        tonguePath.moveTo(w * 0.80f, h * 0.78f)
                        tonguePath.quadraticTo(w * 0.52f, h * 0.55f, w * 0.25f, trillY)
                    }
                    TonguePlacementType.ALVEOLAR_LATERAL_LAAM -> {
                        // Tongue tip firmly touching upper alveolar ridge
                        tonguePath.moveTo(w * 0.80f, h * 0.78f)
                        tonguePath.quadraticTo(
                            w * 0.52f,
                            h * 0.58f,
                            w * 0.24f,
                            h * (0.35f + animCycle * 0.04f)
                        )
                    }
                    TonguePlacementType.VELAR_BACK_KAAF_QAAF -> {
                        // Back of tongue raised to soft palate while tip stays low
                        tonguePath.moveTo(w * 0.82f, h * 0.75f)
                        tonguePath.quadraticTo(
                            w * 0.68f,
                            h * (0.24f - animCycle * 0.06f),
                            w * 0.24f,
                            h * 0.72f
                        )
                    }
                    TonguePlacementType.BILABIAL_LABIODENTAL,
                    TonguePlacementType.PHARYNGEAL_AYN -> {
                        tonguePath.moveTo(w * 0.82f, h * 0.76f)
                        tonguePath.quadraticTo(
                            w * 0.55f,
                            h * (0.48f - animCycle * 0.06f),
                            w * 0.25f,
                            h * 0.66f
                        )
                    }
                }

                drawPath(
                    path = tonguePath,
                    color = Color(0xFFE11D48),
                    style = Stroke(width = 16.dp.toPx(), cap = StrokeCap.Round)
                )

                // Airflow indicator dots
                for (i in 0..3) {
                    val airX = w * (0.16f - i * 0.035f - animCycle * 0.03f)
                    val airY = h * 0.50f + (i - 1.5f) * 6.dp.toPx()
                    drawCircle(
                        color = Color(0xFF0EA5E9).copy(alpha = 0.75f),
                        radius = 4.dp.toPx(),
                        center = Offset(airX.coerceAtLeast(8f), airY)
                    )
                }
            }

            Text(
                text = placementType.arabicTitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
