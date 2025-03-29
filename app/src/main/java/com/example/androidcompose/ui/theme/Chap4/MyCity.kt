package com.example.androidcompose.ui.theme.Chap4

import android.content.ContentValues.TAG
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.R

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class CityScreen(@StringRes val title: Int) {
    Start(title = R.string.city_app_name),
    Category(title = R.string.category),
    Place(title = R.string.recommend_place)
}

enum class CityContentType {
    PlaceWithVerticalContent,
    PlaceWithHorizontalContent,
    CategoryAndRecommendPlace
}

@Composable
fun MyCity(
    windowSize: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
    viewModel: MyCityViewModel = viewModel<MyCityViewModel>(),
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
//    set up navigation
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentCityScreen = CityScreen.valueOf(
        backStackEntry?.destination?.route ?: CityScreen.Start.name
    )

//    setup state & waiting while navigate
    val uiState by viewModel.uiState.collectAsState()
    var isNavigating by remember { mutableStateOf(false) }
    LaunchedEffect(isNavigating) {
        if (isNavigating) {
            delay(1000) // Delay bằng animation duration
            isNavigating = false
        }
    }

//    set up content type with windowSize
    val contentType: CityContentType
    when (windowSize) {
        WindowWidthSizeClass.Compact -> {
            contentType = CityContentType.PlaceWithVerticalContent
        }
        WindowWidthSizeClass.Medium -> {
            contentType = CityContentType.PlaceWithHorizontalContent
        }
        WindowWidthSizeClass.Expanded -> {
            contentType = CityContentType.CategoryAndRecommendPlace
            if (navController.currentDestination?.route == CityScreen.Place.name) {
                navController.navigateUp()
            }
        }
        else -> {
            contentType = CityContentType.PlaceWithVerticalContent
        }
    }

    Scaffold(
        topBar = {
            MyCityTopAppBar(
                currentScreen = currentCityScreen,
                uiState = uiState,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = {
                    if (!isNavigating) {
                        isNavigating = true
                        navController.navigateUp()
                    }
                },
                modifier = Modifier
            )
        },
        modifier = modifier,
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = CityScreen.Start.name,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(CityScreen.Start.name) {
                StartScreen(
                    onNextButtonClicked = {
                        viewModel.updateRecommendPlaceCategory(it)
                        if (!isNavigating) {
                            isNavigating = true
                            navController.navigate(CityScreen.Category.name)
                        }
                    },
                    modifier = modifier
                )
            }
            composable(CityScreen.Category.name) {
                CategoryScreen(
                    uiState = uiState,
                    contentType = contentType,
                    onNextButtonClicked = {
                        viewModel.updateRecommendedPlace(it)
                        Log.d(TAG, "Condition ${!isNavigating || contentType!= CityContentType.CategoryAndRecommendPlace}")
                        if (!isNavigating && contentType!= CityContentType.CategoryAndRecommendPlace) {
                            isNavigating = true
                            navController.navigate(CityScreen.Place.name)
                        }
                    }
                )
            }
            composable(CityScreen.Place.name) {
                PlaceScreen(
                    uiState = uiState,
                    contentType = contentType
                )
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCityTopAppBar(
    currentScreen: CityScreen,
    uiState: MyCityUiState,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {

    TopAppBar(
        title = when (currentScreen) {
            CityScreen.Start -> {
                { Text(
                    text = stringResource(CityScreen.Start.title),
                    style = MaterialTheme.typography.displayLarge,
                ) }
            }

            CityScreen.Category -> {
                { Text(
                    text = stringResource(MyCityDatasource.categories[uiState.recommendedPlaceCategory].name),
                    style = MaterialTheme.typography.displayLarge,
                ) }
            }

            CityScreen.Place -> {
                { Text(
                    text = stringResource(uiState.recommendedPlace.title),
                    style = MaterialTheme.typography.displayLarge,
                ) }
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )

}

@Composable
fun StartScreen(
    onNextButtonClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
    ) {
        MyCityDatasource.categories.forEachIndexed { index, item ->
            CategoryItemRow(
                index = index,
                imageVector = item.imageVector,
                titleName = item.name,
                onNextButtonClicked = onNextButtonClicked,
            )
        }
    }

}

@Composable
fun CategoryScreen(
    uiState: MyCityUiState,
    contentType: CityContentType,
    onNextButtonClicked: (RecommendedPlace) -> Unit,
    modifier: Modifier = Modifier
) {
    if (contentType != CityContentType.CategoryAndRecommendPlace) {
        Column(
            modifier = modifier
        ) {
            MyCityDatasource.recommends[uiState.recommendedPlaceCategory].forEachIndexed { index, item ->
                RecommendItemRow(
                    uiState = uiState,
                    index = index,
                    imageVector = item.imageVector,
                    titleName = item.title,
                    onNextButtonClicked = onNextButtonClicked
                )
            }
        }
    } else {
        Row (
            modifier = modifier
        ) {
            Column (
                modifier = Modifier.weight(1f)
            ) {
                MyCityDatasource.recommends[uiState.recommendedPlaceCategory].forEachIndexed { index, item ->
                    RecommendItemRow(
                        uiState = uiState,
                        index = index,
                        imageVector = item.imageVector,
                        titleName = item.title,
                        onNextButtonClicked = onNextButtonClicked
                    )
                }
            }
            PlaceScreen(uiState, contentType, Modifier.weight(1f))
        }
    }

}

@Composable
fun PlaceScreen(
    uiState: MyCityUiState,
    contentType: CityContentType,
    modifier: Modifier = Modifier
) {
    if (contentType == CityContentType.PlaceWithVerticalContent
        || contentType == CityContentType.CategoryAndRecommendPlace
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
        ) {
            Image(
                imageVector = uiState.recommendedPlace.imageVector,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Spacer(Modifier.padding(dimensionResource(R.dimen.detail_content_padding_top)))
            Text(
                text = stringResource(uiState.recommendedPlace.details),
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    } else if (contentType == CityContentType.PlaceWithHorizontalContent) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxWidth()
        ) {
            Image(
                imageVector = uiState.recommendedPlace.imageVector,
                contentDescription = null,
                modifier = Modifier
                    .aspectRatio(1f)
                    .weight(1f)
            )
            Text(
                text = stringResource(uiState.recommendedPlace.details),
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier
                    .weight(1f)
            )
        }
    }

}

@Composable
private fun CategoryItemRow(
    index: Int,
    imageVector: ImageVector,
    @StringRes titleName: Int,
    onNextButtonClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onNextButtonClicked(index)
            },
    ) {
        Image(
            imageVector = imageVector,
            contentDescription = null,
            modifier = Modifier.size(dimensionResource(R.dimen.category_image_size))
        )
        Spacer(Modifier.padding(start = dimensionResource(R.dimen.padding_small)))
        Text(
            text = stringResource(titleName),
            style = MaterialTheme.typography.displaySmall
        )
    }
    HorizontalDivider()
}

@Composable
private fun RecommendItemRow(
    uiState: MyCityUiState,
    index: Int,
    imageVector: ImageVector,
    @StringRes titleName: Int,
    onNextButtonClicked: (RecommendedPlace) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable {
                Log.d(TAG, "Updating category: $index")
                onNextButtonClicked(MyCityDatasource.recommends[uiState.recommendedPlaceCategory][index])
            },
    ) {
        Image(
            imageVector = imageVector,
            contentDescription = null,
            modifier = Modifier.size(dimensionResource(R.dimen.category_image_size))
        )
        Spacer(Modifier.padding(start = dimensionResource(R.dimen.padding_small)))
        Text(
            text = stringResource(titleName),
            style = MaterialTheme.typography.displaySmall
        )
    }
    HorizontalDivider()
}

data class MyCityUiState(
    val recommendedPlaceCategory: Int = 0,
    val recommendedPlace: RecommendedPlace = MyCityDatasource.recommends[0][0],
)
class MyCityViewModel: ViewModel() {

    private val _uiState: MutableStateFlow<MyCityUiState> = MutableStateFlow(MyCityUiState())
    val uiState: StateFlow<MyCityUiState> = _uiState.asStateFlow()

    fun updateRecommendPlaceCategory(newRecommendedPlaceCategory: Int) {
        _uiState.update {
            it.copy(
                recommendedPlaceCategory = newRecommendedPlaceCategory
            )
        }
    }

    fun updateRecommendedPlace(newRecommendedPlace: RecommendedPlace) {
        _uiState.update {
            it.copy(
                recommendedPlace = newRecommendedPlace
            )
        }
    }

}
data class Category(
    val imageVector: ImageVector,
    @StringRes val name: Int,
)

data class RecommendedPlace(
    val imageVector: ImageVector,
    @StringRes val title: Int,
    @StringRes val details: Int,
)

object MyCityDatasource {
    val categories = listOf(
        Category(Icons.Filled.Image, R.string.category_1),
        Category(Icons.Filled.Image, R.string.category_2),
        Category(Icons.Filled.Image, R.string.category_3),
        Category(Icons.Filled.Image, R.string.category_4),
    )
    val recommends = listOf(
//        coffee_shop
        listOf(
            RecommendedPlace(
                Icons.Filled.Image,
                R.string.title_recommend_1_coffee_shop,
                R.string.details_recommend_1_coffee_shop
            ),
            RecommendedPlace(
                Icons.Filled.Image,
                R.string.title_recommend_2_coffee_shop,
                R.string.details_recommend_2_coffee_shop
            ),
        ),
//        restaurant
        listOf(
            RecommendedPlace(
                Icons.Filled.Image,
                R.string.title_recommend_1_restaurant,
                R.string.details_recommend_1_restaurant
            ),
            RecommendedPlace(
                Icons.Filled.Image,
                R.string.title_recommend_2_restaurant,
                R.string.details_recommend_2_restaurant
            ),
        )
    )
}

//@Preview(showBackground = true, widthDp = 540, heightDp = 1200)
//@Composable
//private fun StartScreenPreview() {
//    StartScreen(onNextButtonClicked = {})
//}

@Preview(showBackground = true, widthDp = 540)
@Composable
private fun RecommendScreenPreview() {
//    val uiState = MyCityUiState()
//    PlaceScreen(uiState, CityContentType.PlaceWithHorizontalContent)
    MyCity(windowSize = WindowWidthSizeClass.Expanded)
}
