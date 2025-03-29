package com.example.androidcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidcompose.ui.theme.AndroidComposeTheme
import com.example.androidcompose.ui.theme.Chap3.Woof
import com.example.androidcompose.ui.theme.Chap4.MyCity
import com.example.androidcompose.ui.theme.Chap5.MarsPhoto
import com.example.androidcompose.ui.theme.Chap5.MarsPhotos
import com.example.androidcompose.ui.theme.Chap5.MarsPhotosApplication
import com.example.androidcompose.ui.theme.Chap5.MarsTopAppBar
import com.example.androidcompose.ui.theme.Chap5.MarsViewModel
import com.example.myapplication.ui.practice.AmphibiansApp
import com.example.myapplication.ui.practice.LunchTrayApp
import com.example.myapplication.ui.practice.RaceTrackerApp
import com.example.myapplication.ui.practice.Unscramble


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidComposeTheme {
                // DiceRoller()
               //  TipCalculator()
                // LemonGame()
                //   ArtSpace()
               //  Affirmations()
              //  Topics()
            //    SupperHeroes()
               // ThirtyDaysChallenge()
          //  DessertClickerApp()
               // Unscramble()
              //  CupcakeApp()
                // LunchTrayApp()
            //    ReplyApp()
          //  SportsApp()
         //  MyCity()
             //   RaceTrackerApp()

            // Woof()



                // failed

//                val app = applicationContext as MarsPhotosApplication
//
//                val marsPhotosRepository = app.container.marsPhotosRepository
//                val marsViewModel: MarsViewModel = viewModel(
//                    factory = MarsViewModel.provideFactory(marsPhotosRepository)
//                )
//                MarsPhotos()

              //  AmphibiansApp()







            }
        }
    }
}
