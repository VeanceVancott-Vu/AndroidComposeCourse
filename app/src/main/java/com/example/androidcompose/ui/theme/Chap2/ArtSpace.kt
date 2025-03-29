package com.example.androidcompose.ui.theme.Chap2

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R


data class Art(val imageRes: Int, val contentDescription: String,
               val title: String, val author: String)

@Composable
fun ArtSpace(modifier: Modifier = Modifier) {


    val allImages = listOf(
        Art(
            R.drawable.girl, "girl",
            "Girl Shine", "Nguyễn Văn Luận(2023)"),
        Art(R.drawable.restaurant, "restaurant",
            "Hoi An Restaurant", "Hoi An photographer(2020)")
    )

    var idOfImage by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier.fillMaxSize()
            .padding(horizontal = 20.dp)
            .safeDrawingPadding()
    ) {

        Surface(
            modifier = Modifier.weight(0.7f)
                .padding(top = 40.dp)
                .background(Color.White)
                .border(2.dp, Color.LightGray, RectangleShape),
            shadowElevation = 5.dp
        ) {
            Image(
                painter = painterResource(allImages[idOfImage].imageRes),
                contentDescription = allImages[idOfImage].contentDescription,
                modifier = Modifier.padding(horizontal = 40.dp, vertical = 40.dp)
            )
        }

        Column(
            modifier = Modifier.weight(0.2f)
                .fillMaxWidth()
                .padding(top = 40.dp)
                .background(Color(0xffecebf4)),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = allImages[idOfImage].title,
                fontSize = 24.sp,
                fontWeight = FontWeight.W300,
                modifier = Modifier.padding(start = 30.dp, bottom = 10.dp)
            )
            Text(
                text = allImages[idOfImage].author,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 30.dp, bottom = 10.dp)
            )
        }

        Row(
            modifier = Modifier.weight(0.1f)
                .fillMaxWidth()
                .padding(bottom = 10.dp, start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    previousButton(idOfImage, allImages.size) { idOfImage = it }
                },
                modifier = Modifier.width(150.dp)
                    .align(Alignment.Bottom)
            ) {
                Text(text = "Previous")
            }
            Button(
                onClick = {
                    nextButton(idOfImage, allImages.size) { idOfImage = it }
                },
                modifier = Modifier.width(150.dp)
                    .align(Alignment.Bottom)
            ) {
                Text(text = "Next")
            }
        }

    }

}

fun previousButton(currentPic: Int, totalPic: Int, updatePic: (Int) -> Unit) {
    val newPic = if (currentPic == 0) {
        totalPic - 1
    } else {
        currentPic - 1
    }
    return updatePic(newPic)
}

fun nextButton(currentPic: Int, totalPic: Int, updatePic: (Int) -> Unit) {
    val newPic = if (currentPic == totalPic - 1) {
        0
    } else {
        currentPic + 1
    }
    return updatePic(newPic)
}

@Preview(showBackground = true, name = "New Year")
@Composable
fun ArtSpacePreview() {

        ArtSpace()

}
