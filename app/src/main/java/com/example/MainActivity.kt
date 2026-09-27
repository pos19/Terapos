package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Air
import androidx.compose.material.icons.outlined.Hearing
import androidx.compose.material.icons.outlined.LibraryBooks
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.AppDestination
import com.example.ui.OrthophonyViewModel
import com.example.ui.screens.ChildProfileAndProgressScreen
import com.example.ui.screens.CommunicationAndAuditoryScreen
import com.example.ui.screens.HomeAndLibraryScreen
import com.example.ui.screens.InteractiveExercisePlayerScreen
import com.example.ui.screens.OralMotorAndBreathingScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                NutqiOrthophonyApp()
            }
        }
    }
}

private data class NavItemSpec(
    val destination: AppDestination,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

private val navItems = listOf(
    NavItemSpec(
        destination = AppDestination.LIBRARY,
        selectedIcon = Icons.Filled.LibraryBooks,
        unselectedIcon = Icons.Outlined.LibraryBooks,
        testTag = "nav_tab_library"
    ),
    NavItemSpec(
        destination = AppDestination.ORAL_BREATHING,
        selectedIcon = Icons.Filled.Air,
        unselectedIcon = Icons.Outlined.Air,
        testTag = "nav_tab_oral_breathing"
    ),
    NavItemSpec(
        destination = AppDestination.AUDITORY_AAC,
        selectedIcon = Icons.Filled.Hearing,
        unselectedIcon = Icons.Outlined.Hearing,
        testTag = "nav_tab_auditory_aac"
    ),
    NavItemSpec(
        destination = AppDestination.CHILD_PROFILE,
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
        testTag = "nav_tab_child_profile"
    )
)

@Composable
fun NutqiOrthophonyApp(
    viewModel: OrthophonyViewModel = viewModel()
) {
    val currentDestination by viewModel.currentDestination.collectAsStateWithLifecycle()
    val activeExercise by viewModel.activeExercise.collectAsStateWithLifecycle()
    val selectedChild by viewModel.selectedChildProfile.collectAsStateWithLifecycle()
    val allChildren by viewModel.allChildProfiles.collectAsStateWithLifecycle()
    val filteredExercises by viewModel.filteredExercises.collectAsStateWithLifecycle()
    val progressList by viewModel.allExerciseProgress.collectAsStateWithLifecycle()
    val practiceLogs by viewModel.allPracticeLogs.collectAsStateWithLifecycle()
    val selectedPhoneme by viewModel.selectedPhonemeFilter.collectAsStateWithLifecycle()
    val selectedDifficulty by viewModel.selectedDifficultyFilter.collectAsStateWithLifecycle()
    val selectedStage by viewModel.selectedStageFilter.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    // If an interactive exercise is open, display the InteractiveExercisePlayerScreen
    activeExercise?.let { exercise ->
        Scaffold(
            contentWindowInsets = WindowInsets.safeDrawing
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                InteractiveExercisePlayerScreen(
                    exercise = exercise,
                    audioEngine = viewModel.audioEngine,
                    onClose = { viewModel.closeExercisePlayer() },
                    onCompleteExercise = { stars, accuracy, note ->
                        viewModel.completeExerciseWithStars(
                            exercise = exercise,
                            starsEarned = stars,
                            accuracyPercent = accuracy,
                            therapistNote = note
                        )
                    }
                )
            }
        }
        return
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isWideScreen = maxWidth >= 600.dp

        Scaffold(
            contentWindowInsets = WindowInsets.safeDrawing,
            bottomBar = {
                if (!isWideScreen) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface
                    ) {
                        navItems.forEach { item ->
                            val isSelected = currentDestination == item.destination
                            NavigationBarItem(
                                selected = isSelected,
                                onClick = { viewModel.navigateTo(item.destination) },
                                icon = {
                                    Icon(
                                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                        contentDescription = item.destination.labelArabic
                                    )
                                },
                                label = { Text(item.destination.labelArabic) },
                                modifier = Modifier.testTag(item.testTag)
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (isWideScreen) {
                    NavigationRail(
                        containerColor = MaterialTheme.colorScheme.surface
                    ) {
                        navItems.forEach { item ->
                            val isSelected = currentDestination == item.destination
                            NavigationRailItem(
                                selected = isSelected,
                                onClick = { viewModel.navigateTo(item.destination) },
                                icon = {
                                    Icon(
                                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                        contentDescription = item.destination.labelArabic
                                    )
                                },
                                label = { Text(item.destination.labelArabic) },
                                modifier = Modifier.testTag(item.testTag)
                            )
                        }
                    }
                }

                Box(modifier = Modifier.weight(1f)) {
                    when (currentDestination) {
                        AppDestination.LIBRARY -> {
                            HomeAndLibraryScreen(
                                selectedChild = selectedChild,
                                phonemeCatalog = viewModel.phonemeCatalog,
                                exercises = filteredExercises,
                                progressList = progressList,
                                selectedPhoneme = selectedPhoneme,
                                selectedDifficulty = selectedDifficulty,
                                selectedStage = selectedStage,
                                searchQuery = searchQuery,
                                onSelectPhoneme = viewModel::setPhonemeFilter,
                                onSelectDifficulty = viewModel::setDifficultyFilter,
                                onSelectStage = viewModel::setStageFilter,
                                onSearchQueryChange = viewModel::setSearchQuery,
                                onClearFilters = viewModel::clearAllFilters,
                                onOpenExercise = viewModel::openExercisePlayer,
                                onSpeakText = viewModel::speakText,
                                onAddCustomExercise = viewModel::addCustomExercise,
                                onDeleteCustomExercise = viewModel::deleteCustomExercise
                            )
                        }

                        AppDestination.ORAL_BREATHING -> {
                            OralMotorAndBreathingScreen(
                                oralMotorExercises = viewModel.oralMotorExercises,
                                audioEngine = viewModel.audioEngine,
                                onCompleteOralOrBreathActivity = { cat, ph, title, stars, note ->
                                    viewModel.logActivityCompletion(
                                        category = cat,
                                        phonemeLetter = ph,
                                        title = title,
                                        difficultyLabel = "تفاعلي",
                                        stageLabel = "جميع الأعمار",
                                        stars = stars,
                                        accuracy = 95,
                                        note = note
                                    )
                                }
                            )
                        }

                        AppDestination.AUDITORY_AAC -> {
                            CommunicationAndAuditoryScreen(
                                minimalPairs = viewModel.minimalPairChallenges,
                                aacCards = viewModel.aacCards,
                                audioEngine = viewModel.audioEngine,
                                onRecordAuditorySuccess = { ph, title, stars, note ->
                                    viewModel.logActivityCompletion(
                                        category = "التمييز السمعي",
                                        phonemeLetter = ph,
                                        title = title,
                                        difficultyLabel = "متوسط",
                                        stageLabel = "3.5 - 6 سنوات",
                                        stars = stars,
                                        accuracy = 95,
                                        note = note
                                    )
                                }
                            )
                        }

                        AppDestination.CHILD_PROFILE -> {
                            ChildProfileAndProgressScreen(
                                profiles = allChildren,
                                selectedProfile = selectedChild,
                                phonemeCatalog = viewModel.phonemeCatalog,
                                progressList = progressList,
                                practiceLogs = practiceLogs,
                                onSelectProfile = viewModel::selectChildProfile,
                                onAddChildProfile = viewModel::addNewChildProfile
                            )
                        }
                    }
                }
            }
        }
    }
}
