package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.data.model.Lesson
import com.example.data.model.Subject
import com.example.ui.theme.MeadowEmerald
import com.example.ui.theme.MeadowSoft
import com.example.ui.theme.SunburstGold

private enum class LessonStage {
    LEARN,
    QUIZ,
    CELEBRATION,
}

@Composable
fun LessonPlayerScreen(
    subject: Subject,
    lesson: Lesson,
    onBack: () -> Unit,
    onLessonFinished: (score: Int, totalQuestions: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    BackHandler(onBack = onBack)

    var stage by remember(lesson.id) { mutableStateOf(LessonStage.LEARN) }
    var questionIndex by remember(lesson.id) { mutableIntStateOf(0) }
    var selectedOption by remember(lesson.id) { mutableIntStateOf(-1) }
    var answerChecked by remember(lesson.id) { mutableStateOf(false) }
    var score by remember(lesson.id) { mutableIntStateOf(0) }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 720.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Top Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("lesson_back_button"),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back to ${subject.title}",
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = subject.title,
                        style = MaterialTheme.typography.labelLarge,
                        color = subject.accentColor,
                    )
                    Text(
                        text = lesson.title,
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
            }

            when (stage) {
                LessonStage.LEARN -> {
                    Surface(
                        shape = RoundedCornerShape(24.dp),
                        color = subject.containerColor,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Text(
                                text = "Story Adventure",
                                style = MaterialTheme.typography.titleSmall,
                                color = subject.accentColor,
                            )
                            Text(
                                text = lesson.storyIntro,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color(0xFF22223B),
                            )
                        }
                    }

                    Text(
                        text = "What You'll Discover",
                        style = MaterialTheme.typography.titleMedium,
                    )

                    lesson.keyPoints.forEachIndexed { idx, point ->
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = MaterialTheme.colorScheme.surface,
                            tonalElevation = 1.dp,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.Top,
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = subject.accentColor,
                                    modifier = Modifier.size(28.dp),
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "${idx + 1}",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = Color.White,
                                        )
                                    }
                                }
                                Text(
                                    text = point,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.weight(1f),
                                )
                            }
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.Top,
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Lightbulb,
                                contentDescription = null,
                                tint = SunburstGold,
                            )
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = "Fun Fact!",
                                    style = MaterialTheme.typography.titleSmall,
                                )
                                Text(
                                    text = lesson.funFact,
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = MeadowSoft,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.Top,
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Explore,
                                contentDescription = null,
                                tint = MeadowEmerald,
                            )
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = "Holiday Try-It Challenge",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = Color(0xFF1B4332),
                                )
                                Text(
                                    text = lesson.tryItActivity,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF1B4332),
                                )
                            }
                        }
                    }

                    Button(
                        onClick = {
                            stage = LessonStage.QUIZ
                            questionIndex = 0
                            selectedOption = -1
                            answerChecked = false
                            score = 0
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = subject.accentColor,
                            contentColor = Color.White,
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .minimumInteractiveComponentSize()
                            .testTag("start_quiz_button"),
                    ) {
                        Icon(Icons.Rounded.AutoAwesome, contentDescription = null)
                        Spacer(Modifier.size(8.dp))
                        Text("Take Mini Quiz (${lesson.quiz.size} Questions)")
                    }
                }

                LessonStage.QUIZ -> {
                    val question = lesson.quiz[questionIndex]
                    val progress = (questionIndex + 1).toFloat() / lesson.quiz.size

                    LinearProgressIndicator(
                        progress = { progress },
                        color = subject.accentColor,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(50)),
                    )

                    Text(
                        text = "Question ${questionIndex + 1} of ${lesson.quiz.size}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    Surface(
                        shape = RoundedCornerShape(24.dp),
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = 2.dp,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = question.prompt,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(20.dp),
                        )
                    }

                    question.options.forEachIndexed { idx, optionText ->
                        val isSelected = selectedOption == idx
                        val isCorrect = idx == question.correctIndex
                        val borderColor = when {
                            answerChecked && isCorrect -> MeadowEmerald
                            answerChecked && isSelected && !isCorrect -> MaterialTheme.colorScheme.error
                            isSelected -> subject.accentColor
                            else -> MaterialTheme.colorScheme.outline
                        }
                        val bgColor = when {
                            answerChecked && isCorrect -> MeadowSoft
                            answerChecked && isSelected && !isCorrect -> MaterialTheme.colorScheme.errorContainer
                            isSelected -> subject.containerColor
                            else -> MaterialTheme.colorScheme.surface
                        }

                        Surface(
                            onClick = {
                                if (!answerChecked) selectedOption = idx
                            },
                            shape = RoundedCornerShape(18.dp),
                            color = bgColor,
                            border = BorderStroke(if (isSelected || answerChecked) 2.dp else 1.dp, borderColor),
                            modifier = Modifier
                                .fillMaxWidth()
                                .minimumInteractiveComponentSize()
                                .testTag("quiz_option_$idx"),
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text(
                                    text = optionText,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = if (answerChecked && isCorrect) Color(0xFF1B4332)
                                    else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.weight(1f),
                                )
                                if (answerChecked && isCorrect) {
                                    Icon(
                                        imageVector = Icons.Rounded.CheckCircle,
                                        contentDescription = "Correct answer",
                                        tint = MeadowEmerald,
                                    )
                                }
                            }
                        }
                    }

                    if (answerChecked) {
                        val gotItRight = selectedOption == question.correctIndex
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = if (gotItRight) MeadowSoft else MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = if (gotItRight) "Awesome! That's right!" else "Nice try! Here's why:",
                                    style = MaterialTheme.typography.titleSmall,
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = question.explanation,
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }
                        }
                    }

                    Button(
                        onClick = {
                            if (!answerChecked) {
                                if (selectedOption == question.correctIndex) {
                                    score += 1
                                }
                                answerChecked = true
                            } else {
                                if (questionIndex + 1 < lesson.quiz.size) {
                                    questionIndex += 1
                                    selectedOption = -1
                                    answerChecked = false
                                } else {
                                    onLessonFinished(score, lesson.quiz.size)
                                    stage = LessonStage.CELEBRATION
                                }
                            }
                        },
                        enabled = selectedOption != -1,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = subject.accentColor,
                            contentColor = Color.White,
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .minimumInteractiveComponentSize()
                            .testTag("quiz_submit_next_button"),
                    ) {
                        Text(
                            text = when {
                                !answerChecked -> "Check Answer"
                                questionIndex + 1 < lesson.quiz.size -> "Next Question"
                                else -> "Finish Lesson"
                            }
                        )
                    }
                }

                LessonStage.CELEBRATION -> {
                    val earnedStars = 10 + score * 5
                    Surface(
                        shape = RoundedCornerShape(28.dp),
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = 3.dp,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(14.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.EmojiEvents,
                                contentDescription = "Trophy",
                                tint = SunburstGold,
                                modifier = Modifier.size(64.dp),
                            )
                            Text(
                                text = "Lesson Complete!",
                                style = MaterialTheme.typography.headlineMedium,
                                textAlign = TextAlign.Center,
                            )
                            Text(
                                text = "You scored $score out of ${lesson.quiz.size} on \"${lesson.title}\"!",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = MaterialTheme.colorScheme.primaryContainer,
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Star,
                                        contentDescription = null,
                                        tint = SunburstGold,
                                    )
                                    Text(
                                        text = "+$earnedStars Stars Earned!",
                                        style = MaterialTheme.typography.titleMedium,
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        stage = LessonStage.LEARN
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .minimumInteractiveComponentSize()
                                        .testTag("review_lesson_button"),
                                ) {
                                    Text("Review")
                                }
                                Button(
                                    onClick = onBack,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = subject.accentColor,
                                        contentColor = Color.White,
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .minimumInteractiveComponentSize()
                                        .testTag("done_lesson_button"),
                                ) {
                                    Text("Back to Subject")
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}
