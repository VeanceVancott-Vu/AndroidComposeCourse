package com.example.androidcompose.ui.theme.Chap2

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R

@Composable
fun EachStep(
    text: String,
    imageResource: Int,
    imageDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(imageResource),
            contentDescription = imageDescription,
            modifier = Modifier.wrapContentSize()
                .clickable { onClick() }
                .size(200.dp)
                .border(2.dp, Color(105,205,216), shape = RoundedCornerShape(30.dp))
                .background(MaterialTheme.colorScheme.tertiaryContainer, shape = RoundedCornerShape(30.dp))
                .clip(shape = RoundedCornerShape(30.dp))
        )
        Spacer(Modifier.height(32.dp))
        Text(
            text = text,
            fontSize = 18.sp,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LemonGame(modifier: Modifier = Modifier) {

    var currentStep by remember { mutableIntStateOf(1) }
    var squeezeCount by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Lemonade",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = modifier.fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.tertiaryContainer),
            color = MaterialTheme.colorScheme.background
        ) {

            when (currentStep) {
                1-> {
                    EachStep(
                        text = "Tap the lemon tree to select a lemon",
                        imageResource = R.drawable.lemon_tree,
                        imageDescription = "Lemon tree",
                        onClick = {
                            currentStep = 2
                            squeezeCount = (2..4).random()
                        }
                    )
                }
                2-> {
                    EachStep(
                        text = "Keep tapping the lemon to squeeze it",
                        imageResource = R.drawable.lemon_squeeze,
                        imageDescription = "Lemon",
                        onClick = {
                            squeezeCount--
                            if (squeezeCount == 0) {
                                currentStep = 3
                            }
                        }
                    )
                }
                3-> {
                    EachStep(
                        text = "Tap the lemonade to drink it",
                        imageResource = R.drawable.lemon_drink,
                        imageDescription = "Glass of lemonade",
                        onClick = {
                            currentStep = 4
                        }
                    )
                }
                4-> {
                    EachStep(
                        text = "Tap the empty glass to start again",
                        imageResource = R.drawable.lemon_restart,
                        imageDescription = "Empty glass",
                        onClick = {
                            currentStep = 1
                        }
                    )
                }
            }

        }
    }

}


@Preview(showBackground = true, name = "New Year")
@Composable
fun TreeGamePreview() {

    LemonGame()

}
