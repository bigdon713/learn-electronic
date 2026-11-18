package com.example.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.R
import com.example.auth.authStateFlow
import com.example.auth.signOut
import com.example.data.model.CurriculumCatalog
import com.example.data.repository.SunnyLearnRepository
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.LessonPlayerScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.RoutineScreen
import com.example.ui.screens.SubjectDetailScreen
import com.example.ui.screens.SubjectsScreen
import com.example.ui.screens.TodayScreen
import com.example.ui.viewmodel.SunnyLearnViewModel
import com.example.ui.viewmodel.UiState
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

const val ROUTE_TODAY = "today"
const val ROUTE_SUBJECTS = "subjects"
const val ROUTE_ROUTINE = "routine"
const val ROUTE_PROFILE = "profile"
const val ROUTE_SUBJECT_DETAIL = "subject_detail/{subjectId}"
const val ROUTE_LESSON_PLAYER = "lesson_player/{subjectId}/{lessonId}"

private data class TopTab(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String,
)

private val topTabs = listOf(
    TopTab(
        route = ROUTE_TODAY,
        label = "Today",
        selectedIcon = Icons.Rounded.Home,
        unselectedIcon = Icons.Outlined.Home,
        testTag = "nav_tab_today",
    ),
    TopTab(
        route = ROUTE_SUBJECTS,
        label = "Subjects",
        selectedIcon = Icons.AutoMirrored.Rounded.MenuBook,
        unselectedIcon = Icons.AutoMirrored.Outlined.MenuBook,
        testTag = "nav_tab_subjects",
    ),
    TopTab(
        route = ROUTE_ROUTINE,
        label = "Routine",
        selectedIcon = Icons.Rounded.CalendarMonth,
        unselectedIcon = Icons.Outlined.CalendarMonth,
        testTag = "nav_tab_routine",
    ),
    TopTab(
        route = ROUTE_PROFILE,
        label = "Badges",
        selectedIcon = Icons.Rounded.EmojiEvents,
        unselectedIcon = Icons.Outlined.EmojiEvents,
        testTag = "nav_tab_profile",
    ),
)

@Composable
fun SunnyLearnApp(
    auth: FirebaseAuth = Firebase.auth,
) {
    val currentUser by auth.authStateFlow().collectAsStateWithLifecycle(initialValue = auth.currentUser)
    val user = currentUser

    if (user == null) {
        AuthScreen(onAuthSuccess = {})
    } else {
        key(user.uid) {
            AuthenticatedShell(
                currentUserId = user.uid,
                explorerName = user.displayName ?: "Explorer",
                explorerEmail = user.email ?: "",
            )
        }
    }
}

@Composable
private fun AuthenticatedShell(
    currentUserId: String,
    explorerName: String,
    explorerEmail: String,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val credentialManager = remember(context) { CredentialManager.create(context) }

    val viewModel: SunnyLearnViewModel = viewModel(
        key = currentUserId,
        factory = viewModelFactory {
            initializer {
                val app = checkNotNull(this[APPLICATION_KEY]) {
                    "APPLICATION_KEY missing from CreationExtras"
                }
                val databaseId = app.getString(R.string.firestore_database_id)
                val db = FirebaseFirestore.getInstance(databaseId)
                SunnyLearnViewModel(SunnyLearnRepository(db), currentUserId)
            }
        },
    )

    val dashboardState by viewModel.dashboardState.collectAsStateWithLifecycle()
    val selectedDay by viewModel.selectedDayOfWeek.collectAsStateWithLifecycle()
    val showAllDays by viewModel.showAllDays.collectAsStateWithLifecycle()
    val statusMessage by viewModel.statusMessage.collectAsStateWithLifecycle()

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(statusMessage) {
        val msg = statusMessage
        if (msg != null) {
            snackbarHostState.showSnackbar(msg)
            viewModel.clearStatusMessage()
        }
    }

    val isTopLevelTab = topTabs.any { it.route == currentRoute }

    fun navigateTopTab(route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val useRail = maxWidth >= 600.dp

        Scaffold(
            contentWindowInsets = WindowInsets.safeDrawing,
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            bottomBar = {
                if (!useRail && isTopLevelTab) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ) {
                        topTabs.forEach { tab ->
                            val selected = currentRoute == tab.route
                            NavigationBarItem(
                                selected = selected,
                                onClick = { navigateTopTab(tab.route) },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                                        contentDescription = tab.label,
                                    )
                                },
                                label = { Text(tab.label) },
                                modifier = Modifier.testTag(tab.testTag),
                            )
                        }
                    }
                }
            },
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                if (useRail && isTopLevelTab) {
                    NavigationRail(
                        modifier = Modifier.fillMaxHeight(),
                        containerColor = MaterialTheme.colorScheme.surface,
                    ) {
                        topTabs.forEach { tab ->
                            val selected = currentRoute == tab.route
                            NavigationRailItem(
                                selected = selected,
                                onClick = { navigateTopTab(tab.route) },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                                        contentDescription = tab.label,
                                    )
                                },
                                label = { Text(tab.label) },
                                modifier = Modifier.testTag(tab.testTag),
                            )
                        }
                    }
                }

                Box(modifier = Modifier.weight(1f).fillMaxSize()) {
                    when (val state = dashboardState) {
                        UiState.Loading -> {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                CircularProgressIndicator()
                                Text(
                                    text = "Loading your sunny adventure...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(top = 12.dp),
                                )
                            }
                        }

                        is UiState.Error -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(24.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(
                                    text = "Oops! Something got tangled.",
                                    style = MaterialTheme.typography.titleLarge,
                                )
                                Text(
                                    text = state.message,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(top = 8.dp),
                                )
                            }
                        }

                        is UiState.Success -> {
                            val dashboard = state.data
                            NavHost(
                                navController = navController,
                                startDestination = ROUTE_TODAY,
                                modifier = Modifier.fillMaxSize(),
                            ) {
                                composable(ROUTE_TODAY) {
                                    TodayScreen(
                                        explorerName = explorerName,
                                        dashboard = dashboard,
                                        onToggleRoutineDone = viewModel::toggleRoutineDone,
                                        onOpenRoutineTab = { navigateTopTab(ROUTE_ROUTINE) },
                                        onOpenSubjectsTab = { navigateTopTab(ROUTE_SUBJECTS) },
                                        onSelectSubject = { subject ->
                                            navController.navigate("subject_detail/${subject.id}")
                                        },
                                        onLoadStarterRoutine = viewModel::loadStarterRoutine,
                                    )
                                }

                                composable(ROUTE_SUBJECTS) {
                                    SubjectsScreen(
                                        dashboard = dashboard,
                                        onSelectSubject = { subject ->
                                            navController.navigate("subject_detail/${subject.id}")
                                        },
                                    )
                                }

                                composable(ROUTE_ROUTINE) {
                                    RoutineScreen(
                                        dashboard = dashboard,
                                        selectedDayOfWeek = selectedDay,
                                        showAllDays = showAllDays,
                                        onSelectDay = viewModel::selectDay,
                                        onSelectAllDays = viewModel::selectAllDays,
                                        onSaveRoutine = viewModel::saveRoutine,
                                        onDeleteRoutine = viewModel::deleteRoutine,
                                        onToggleRoutineDone = viewModel::toggleRoutineDone,
                                        onLoadStarterRoutine = viewModel::loadStarterRoutine,
                                        onSelectSubject = { subject ->
                                            navController.navigate("subject_detail/${subject.id}")
                                        },
                                    )
                                }

                                composable(ROUTE_PROFILE) {
                                    ProfileScreen(
                                        explorerName = explorerName,
                                        explorerEmail = explorerEmail,
                                        dashboard = dashboard,
                                        onSignOut = {
                                            signOut(
                                                credentialManager = credentialManager,
                                                scope = scope,
                                            )
                                        },
                                    )
                                }

                                composable(
                                    route = ROUTE_SUBJECT_DETAIL,
                                    arguments = listOf(navArgument("subjectId") { type = NavType.StringType }),
                                ) { backStackEntry ->
                                    val subjectId = backStackEntry.arguments?.getString("subjectId").orEmpty()
                                    val subject = CurriculumCatalog.getSubject(subjectId)
                                    if (subject != null) {
                                        SubjectDetailScreen(
                                            subject = subject,
                                            progressByLessonId = dashboard.progressByLessonId,
                                            onBack = { navController.popBackStack() },
                                            onStartLesson = { lesson ->
                                                navController.navigate("lesson_player/${subject.id}/${lesson.id}")
                                            },
                                        )
                                    }
                                }

                                composable(
                                    route = ROUTE_LESSON_PLAYER,
                                    arguments = listOf(
                                        navArgument("subjectId") { type = NavType.StringType },
                                        navArgument("lessonId") { type = NavType.StringType },
                                    ),
                                ) { backStackEntry ->
                                    val subjectId = backStackEntry.arguments?.getString("subjectId").orEmpty()
                                    val lessonId = backStackEntry.arguments?.getString("lessonId").orEmpty()
                                    val subject = CurriculumCatalog.getSubject(subjectId)
                                    val lesson = CurriculumCatalog.getLesson(lessonId)
                                    if (subject != null && lesson != null) {
                                        LessonPlayerScreen(
                                            subject = subject,
                                            lesson = lesson,
                                            onBack = { navController.popBackStack() },
                                            onLessonFinished = { score, total ->
                                                viewModel.recordLessonResult(
                                                    subjectId = subject.id,
                                                    lessonId = lesson.id,
                                                    score = score,
                                                    totalQuestions = total,
                                                )
                                            },
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
