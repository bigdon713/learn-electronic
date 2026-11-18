package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.CleaningServices
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.SportsSoccer
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.data.model.RoutineCategory
import com.example.ui.theme.CobaltBlue
import com.example.ui.theme.CobaltSoft
import com.example.ui.theme.CoralBerry
import com.example.ui.theme.CoralSoft
import com.example.ui.theme.LavenderPlum
import com.example.ui.theme.LavenderSoft
import com.example.ui.theme.MeadowEmerald
import com.example.ui.theme.MeadowSoft
import com.example.ui.theme.SkyCerulean
import com.example.ui.theme.SkySoft
import com.example.ui.theme.SunburstGold
import com.example.ui.theme.SunburstSoft

fun RoutineCategory.icon(): ImageVector = when (this) {
    RoutineCategory.STUDY -> Icons.Rounded.School
    RoutineCategory.READING -> Icons.Rounded.AutoStories
    RoutineCategory.CREATIVE -> Icons.Rounded.Palette
    RoutineCategory.PLAY -> Icons.Rounded.SportsSoccer
    RoutineCategory.CHORES -> Icons.Rounded.CleaningServices
    RoutineCategory.REST -> Icons.Rounded.Restaurant
}

fun RoutineCategory.accentColor(): Color = when (this) {
    RoutineCategory.STUDY -> CobaltBlue
    RoutineCategory.READING -> CoralBerry
    RoutineCategory.CREATIVE -> LavenderPlum
    RoutineCategory.PLAY -> MeadowEmerald
    RoutineCategory.CHORES -> SkyCerulean
    RoutineCategory.REST -> SunburstGold
}

fun RoutineCategory.softBackground(): Color = when (this) {
    RoutineCategory.STUDY -> CobaltSoft
    RoutineCategory.READING -> CoralSoft
    RoutineCategory.CREATIVE -> LavenderSoft
    RoutineCategory.PLAY -> MeadowSoft
    RoutineCategory.CHORES -> SkySoft
    RoutineCategory.REST -> SunburstSoft
}

fun isoDayShortName(day: Int): String = when (day) {
    1 -> "Mon"
    2 -> "Tue"
    3 -> "Wed"
    4 -> "Thu"
    5 -> "Fri"
    6 -> "Sat"
    7 -> "Sun"
    else -> "Day"
}

fun isoDayFullName(day: Int): String = when (day) {
    1 -> "Monday"
    2 -> "Tuesday"
    3 -> "Wednesday"
    4 -> "Thursday"
    5 -> "Friday"
    6 -> "Saturday"
    7 -> "Sunday"
    else -> "Today"
}

@Composable
fun StarCounterPill(
    stars: Long,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.primaryContainer,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Icon(
                imageVector = Icons.Rounded.Star,
                contentDescription = "Total stars",
                tint = SunburstGold,
                modifier = Modifier.size(18.dp),
            )
            Text(
                text = "$stars",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }
}

@Composable
fun CategoryBadge(
    category: RoutineCategory,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(category.softBackground())
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(
            imageVector = category.icon(),
            contentDescription = null,
            tint = category.accentColor(),
            modifier = Modifier.size(14.dp),
        )
        Text(
            text = category.label,
            style = MaterialTheme.typography.labelSmall,
            color = category.accentColor(),
        )
    }
}
