package com.example.androidcompose.ui.theme.Chap3

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R


data class Topic(
    @StringRes val stringResourceId: Int,
    val numOfAssociatedCourse: Int,
    @DrawableRes val imageResourceId: Int
)

class TopicDatasource {
    fun loadTopic(): List<Topic> {
        return listOf(
            Topic(R.string.architecture, 58, R.drawable.architecture),
            Topic(R.string.automotive, 30, R.drawable.automotive),
            Topic(R.string.biology, 90, R.drawable.biology),
            Topic(R.string.crafts, 121, R.drawable.crafts),
            Topic(R.string.business, 78, R.drawable.business),
            Topic(R.string.culinary, 118, R.drawable.culinary),
            Topic(R.string.design, 423, R.drawable.design),
            Topic(R.string.ecology, 28, R.drawable.ecology),
            Topic(R.string.engineering, 67, R.drawable.engineering),
            Topic(R.string.fashion, 92, R.drawable.fashion),
            Topic(R.string.finance, 100, R.drawable.finance),
            Topic(R.string.film, 165, R.drawable.film),
            Topic(R.string.gaming, 37, R.drawable.gaming),
            Topic(R.string.geology, 290, R.drawable.geology),
            Topic(R.string.drawing, 326, R.drawable.drawing),
            Topic(R.string.history, 189, R.drawable.history),
            Topic(R.string.journalism, 96, R.drawable.journalism),
            Topic(R.string.law, 58, R.drawable.law),
            Topic(R.string.lifestyle, 305, R.drawable.lifestyle),
            Topic(R.string.music, 212, R.drawable.music),
            Topic(R.string.painting, 172, R.drawable.painting),
            Topic(R.string.photography, 321, R.drawable.photography),
            Topic(R.string.physics, 41, R.drawable.physics),
            Topic(R.string.tech, 118, R.drawable.tech),
        )
    }
}

@Composable
fun Topics() {
    val layoutDirection = LocalLayoutDirection.current
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(
                start = WindowInsets.safeDrawing.asPaddingValues()
                    .calculateStartPadding(layoutDirection),
                end = WindowInsets.safeDrawing.asPaddingValues()
                    .calculateEndPadding(layoutDirection)
            )
    ) {
        TopicGrid(
            topicList = TopicDatasource().loadTopic()
        )
    }
}

@Composable
fun TopicGrid(topicList: List<Topic>, modifier: Modifier = Modifier) {

    LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = modifier) {
        items(topicList) { topic ->
            TopicCard(topic, Modifier.padding(8.dp))
        }
    }

}

@Composable
fun TopicCard(topic: Topic, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Row {
            Image(
                painter = painterResource(topic.imageResourceId),
                contentDescription = stringResource(topic.stringResourceId),
                modifier = Modifier.width(68.dp).aspectRatio(1f),
                contentScale = ContentScale.Crop
            )
            Column {
                Text(
                    text = LocalContext.current.getString(topic.stringResourceId),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(
                        start = 16.dp,
                        top = 16.dp,
                        end = 16.dp,
                        bottom = 8.dp
                    )
                )
                Row {
                    Image(
                        painter = painterResource(R.drawable.ic_grain),
                        contentDescription = "icon",
                        modifier = Modifier.padding(start = 16.dp)
                    )
                    Text(
                        text = "${topic.numOfAssociatedCourse}",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(start = 8.dp)
                            .align(Alignment.CenterVertically)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "New Year")
@Composable
fun CoursesPreview() {

        Topics()

}
