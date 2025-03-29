package com.example.androidcompose.ui.theme.Chap3

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R

data class Day(
    val dayCount: Int,
    @StringRes val toDoRes: Int,
    @DrawableRes val imageRes: Int,
    @StringRes val tipRes: Int
)

object DayData {
    val days = listOf(
        Day(1, R.string.todo1, R.drawable.push_up1, R.string.tip1),
        Day(2, R.string.todo2, R.drawable.push_up2, R.string.tip2),
        Day(3, R.string.todo3, R.drawable.push_up3, R.string.tip3),
        Day(4, R.string.todo4, R.drawable.push_up4, R.string.tip4),
        Day(5, R.string.todo5, R.drawable.push_up5, R.string.tip5),
        Day(6, R.string.todo6, R.drawable.push_up6, R.string.tip6),
        Day(7, R.string.todo7, R.drawable.push_up7, R.string.tip7),
        Day(8, R.string.todo8, R.drawable.push_up8, R.string.tip8),
        Day(9, R.string.todo9, R.drawable.push_up9, R.string.tip9),
        Day(10, R.string.todo10, R.drawable.push_up10, R.string.tip10),
        Day(11, R.string.todo11, R.drawable.push_up11, R.string.tip11),
        Day(12, R.string.todo12, R.drawable.push_up12, R.string.tip12),
        Day(13, R.string.todo13, R.drawable.push_up13, R.string.tip13),
        Day(14, R.string.todo14, R.drawable.push_up14, R.string.tip14),
        Day(15, R.string.todo15, R.drawable.push_up15, R.string.tip15),
        Day(16, R.string.todo16, R.drawable.push_up16, R.string.tip16),
        Day(17, R.string.todo17, R.drawable.push_up17, R.string.tip17),
        Day(18, R.string.todo18, R.drawable.push_up18, R.string.tip18),
        Day(19, R.string.todo19, R.drawable.push_up19, R.string.tip19),
        Day(20, R.string.todo20, R.drawable.push_up20, R.string.tip20),
        Day(21, R.string.todo21, R.drawable.push_up21, R.string.tip21),
        Day(22, R.string.todo22, R.drawable.push_up22, R.string.tip22),
        Day(23, R.string.todo23, R.drawable.push_up23, R.string.tip23),
        Day(24, R.string.todo24, R.drawable.push_up24, R.string.tip24),
        Day(25, R.string.todo25, R.drawable.push_up25, R.string.tip25),
        Day(26, R.string.todo26, R.drawable.push_up26, R.string.tip26),
        Day(27, R.string.todo27, R.drawable.push_up27, R.string.tip27),
        Day(28, R.string.todo28, R.drawable.push_up28, R.string.tip28),
        Day(29, R.string.todo29, R.drawable.push_up29, R.string.tip29),
        Day(30, R.string.todo30, R.drawable.push_up30, R.string.tip30),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThirtyDaysChallenge(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "30 Days Of Push-Ups",
                        fontWeight = FontWeight.W500
                    )
                }
            )
        },
        modifier = modifier
    ) { it ->
        LazyColumn(contentPadding = it) {
            items(DayData.days) {
                DayItem(it, Modifier.padding(8.dp))
            }
        }
    }
}

@Composable
fun DayItem(day: Day, modifier: Modifier = Modifier) {
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
                .background(color = MaterialTheme.colorScheme.secondaryContainer)
                .padding(16.dp)
        ) {
            Text(
                text = "Day ${day.dayCount}",
                style = MaterialTheme.typography.displaySmall
            )
            Text(
                text = stringResource(day.toDoRes),
                style = MaterialTheme.typography.bodyLarge
            )
            Image(
                painter = painterResource(day.imageRes),
                contentDescription = "Push-up image",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = stringResource(day.tipRes),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview
@Composable
fun ThirtyDaysChallengePreview() {
    ThirtyDaysChallenge()
}
