package com.example.ui.screens

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
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.SlowMotionVideo
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.audio.OrthophonyAudioEngine
import com.example.data.curriculum.AacCardItem
import com.example.data.curriculum.MinimalPairChallenge

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CommunicationAndAuditoryScreen(
    minimalPairs: List<MinimalPairChallenge>,
    aacCards: List<AacCardItem>,
    audioEngine: OrthophonyAudioEngine,
    onRecordAuditorySuccess: (
        phoneme: String,
        title: String,
        stars: Int,
        note: String
    ) -> Unit
) {
    var currentChallengeIndex by remember { mutableIntStateOf(0) }
    val challenge = minimalPairs.getOrElse(currentChallengeIndex) { minimalPairs.first() }
    var selectedWordChoice by remember(currentChallengeIndex) { mutableStateOf<String?>(null) }
    var correctAnswersCount by remember { mutableIntStateOf(0) }

    // AAC Sentence Strip Builder State
    val sentenceWords = remember { mutableStateListOf("أَنَا", "أُرِيدُ", "المَاءَ", "مِنْ فَضْلِكَ") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("communication_auditory_screen"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Minimal Pairs Auditory Discrimination Game Card
        item {
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
                                imageVector = Icons.Default.Hearing,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Column {
                                Text(
                                    text = "لعبة التمييز السمعي (الثنائيات الصغرى)",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${challenge.targetContrastLabel} • ${challenge.developmentalStage.shortBadge}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = "${currentChallengeIndex + 1} / ${minimalPairs.size}",
                                style = MaterialTheme.typography.labelLarge,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                            )
                        }
                    }

                    Text(
                        text = challenge.promptQuestion,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )

                    // Listen to target word button
                    FilledTonalButton(
                        onClick = {
                            audioEngine.speakArabic(
                                challenge.correctWord,
                                slowSyllabic = false,
                                phonemeHint = challenge.correctPhoneme
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("btn_listen_minimal_pair_target")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("اسمع الكلمة المستهدفة (${challenge.correctMeaningHint})")
                    }

                    // Two Interactive Option Cards
                    val options = remember(challenge.id) {
                        listOf(
                            Triple(challenge.correctWord, challenge.correctSyllables, challenge.correctMeaningHint),
                            Triple(challenge.distractorWord, challenge.distractorSyllables, challenge.distractorMeaningHint)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        options.forEachIndexed { idx, (word, syllables, hint) ->
                            val isChosen = selectedWordChoice == word
                            val isRightOption = word == challenge.correctWord
                            val borderColor = when {
                                !isChosen -> MaterialTheme.colorScheme.outlineVariant
                                isRightOption -> Color(0xFF16A34A)
                                else -> MaterialTheme.colorScheme.error
                            }

                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(18.dp))
                                    .clickable {
                                        selectedWordChoice = word
                                        audioEngine.speakArabic(word, slowSyllabic = false, phonemeHint = challenge.correctPhoneme)
                                        if (word == challenge.correctWord) {
                                            correctAnswersCount++
                                            onRecordAuditorySuccess(
                                                challenge.correctPhoneme,
                                                "تمييز سمعي: ${challenge.correctWord} ≠ ${challenge.distractorWord}",
                                                3,
                                                "نجح الطفل في التمييز الفونولوجي بين ${challenge.targetContrastLabel}"
                                            )
                                        } else {
                                            audioEngine.playGentleTryAgainSound()
                                        }
                                    }
                                    .border(2.dp, borderColor, RoundedCornerShape(18.dp))
                                    .testTag("mp_option_card_$idx"),
                                color = if (isChosen && isRightOption) Color(0xFFDCFCE7)
                                else if (isChosen) Color(0xFFFEE2E2)
                                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = word,
                                        style = MaterialTheme.typography.headlineMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = syllables,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = hint,
                                        style = MaterialTheme.typography.bodySmall,
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    if (selectedWordChoice != null) {
                        val isCorrect = selectedWordChoice == challenge.correctWord
                        Surface(
                            color = if (isCorrect) Color(0xFFDCFCE7) else MaterialTheme.colorScheme.secondaryContainer,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = if (isCorrect) "🌟 أحسنت! تمييز سمعي دقيق (+3 نجوم)"
                                    else "💡 استمع مرة أخرى ولاحظ فرق مخرج الصوت الأول!",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = challenge.clinicalExplanation,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }

                    Button(
                        onClick = {
                            currentChallengeIndex = (currentChallengeIndex + 1) % minimalPairs.size
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("btn_next_minimal_pair")
                    ) {
                        Text("الثنائية الصغرى التالية")
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(Icons.Default.NavigateNext, contentDescription = null)
                    }
                }
            }
        }

        // 2. AAC Functional Sentence Builder Board (لوحة التواصل الوظيفي وبناء الجمل)
        item {
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
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.RecordVoiceOver,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Column {
                            Text(
                                text = "لوحة التواصل الوظيفي وبناء الجمل (AAC)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "ركّب جملة كاملة بالنقر على البطاقات الملونة ثم استمع إليها",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Active Sentence Strip
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f),
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            if (sentenceWords.isEmpty()) {
                                Text(
                                    text = "انقر على الكلمات بالأسفل لتكوين جملة تواصلية...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            } else {
                                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    items(sentenceWords) { word ->
                                        Surface(
                                            color = MaterialTheme.colorScheme.surface,
                                            shape = RoundedCornerShape(12.dp),
                                            tonalElevation = 2.dp
                                        ) {
                                            Text(
                                                text = word,
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        val fullSentence = sentenceWords.joinToString(" ")
                                        if (fullSentence.isNotBlank()) {
                                            audioEngine.speakArabic(fullSentence, slowSyllabic = false)
                                        }
                                    },
                                    enabled = sentenceWords.isNotEmpty(),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("btn_speak_aac_sentence")
                                ) {
                                    Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = null)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("نطق الجملة")
                                }

                                FilledTonalButton(
                                    onClick = {
                                        val fullSentence = sentenceWords.joinToString(" ، ")
                                        if (fullSentence.isNotBlank()) {
                                            audioEngine.speakArabic(fullSentence, slowSyllabic = true)
                                        }
                                    },
                                    enabled = sentenceWords.isNotEmpty()
                                ) {
                                    Icon(Icons.Default.SlowMotionVideo, contentDescription = null)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("بطيء")
                                }

                                IconButton(
                                    onClick = {
                                        if (sentenceWords.isNotEmpty()) {
                                            sentenceWords.removeAt(sentenceWords.lastIndex)
                                        }
                                    }
                                ) {
                                    Icon(Icons.AutoMirrored.Filled.Backspace, contentDescription = "حذف كلمة")
                                }

                                IconButton(
                                    onClick = { sentenceWords.clear() }
                                ) {
                                    Icon(Icons.Default.DeleteSweep, contentDescription = "مسح الجملة")
                                }
                            }
                        }
                    }

                    // Categorized Word Cards
                    val groupedAac = remember(aacCards) { aacCards.groupBy { it.category } }
                    groupedAac.forEach { (category, cards) ->
                        Text(
                            text = category,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            cards.forEach { card ->
                                val bg = when (card.colorRole) {
                                    "PRONOUN" -> Color(0xFFFEF3C7)
                                    "VERB" -> Color(0xFFDCFCE7)
                                    "NOUN" -> Color(0xFFE0F2FE)
                                    else -> Color(0xFFFCE7F3)
                                }
                                Surface(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(14.dp))
                                        .clickable {
                                            sentenceWords.add(card.wordArabic)
                                            audioEngine.speakArabic(card.wordArabic, slowSyllabic = false)
                                        }
                                        .testTag("aac_card_${card.id}"),
                                    color = bg,
                                    shape = RoundedCornerShape(14.dp)
                                ) {
                                    Text(
                                        text = card.wordArabic,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0F172A),
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(28.dp)) }
    }
}
