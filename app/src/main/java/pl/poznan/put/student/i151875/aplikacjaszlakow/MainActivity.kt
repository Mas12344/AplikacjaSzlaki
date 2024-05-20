package pl.poznan.put.student.i151875.aplikacjaszlakow

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator


import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.activity.compose.BackHandler
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TabScreen()
        }
    }

}



/*
composable(
            route="details/{trackName}",
            arguments = listOf(navArgument("trackName") {type = NavType.StringType})
            ) { backStackEntry ->
            val trackName = backStackEntry.arguments?.getString("trackName")

            //val dst: DetailsScreenState = SavedDetailStates[trackName] //TODO


            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                DetailsContent(Track(selectedTrackName, selectedTrackDescription, selectedTrackPhoto))

                val timerViewModel = TimerViewModel()

                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyTimerContent(timerViewModel = timerViewModel)

                }
            }

        }
 */

class DetailsScreenState(t: Track, tvm: TimerViewModel) {
    init {
        val track: Track = t
        val timer: TimerViewModel = tvm
    }

}

//TODO ListDetailPaneScaffold

@Composable
fun TabScreen() {
    var tabIndex by rememberSaveable { mutableStateOf(0) }

    val tabs = listOf("O Aplikacji", "Łatwe trasy", "Trudne trasy")

    Column(modifier = Modifier.fillMaxWidth()) {
        TabRow(selectedTabIndex = tabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(text = { Text(title) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    icon = {
                        when (index) {
                            0 -> Icon(imageVector = Icons.Default.Info, contentDescription = null)
                            1 -> Icon(imageVector = Icons.Default.ThumbUp, contentDescription = null)
                            2 -> Icon(imageVector = Icons.Default.Warning, contentDescription = null)
                        }
                    }
                )
            }
        }
        when (tabIndex) {
            0 -> AboutScreen()
            1 -> {
                EasyTracksScreen()
            }
            2 -> {
                HardTracksScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun HardTracksScreen() {
    val tracks: List<Track> = listOf(
        Track(
            "Trasa 7",
            "Opis 7",
            R.drawable.trasa_7
        ),
        Track( "Trasa 8",
            "Opis 8",
            R.drawable.trasa_8
        ),
        Track( "Trasa 9",
            "Opis 9",
            R.drawable.trasa_9
        ),
        Track(
            "Trasa 10",
                "Opis 10",
            R.drawable.trasa_10
        ),
        Track( "Trasa 11",
            "Opis 11",
            R.drawable.trasa_11
        ),
        Track( "Trasa 12",
            "Opis 12",
            R.drawable.trasa_12
        )
    )

    val navigator = rememberListDetailPaneScaffoldNavigator<Nothing>()
    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                CardList(
                    tracks = tracks,
                    onTrackClicked = {
                        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                    }
                )
            }
        },

        detailPane = {
            AnimatedPane {
                navigator.currentDestination?.content?.let {
                    Text("Detale")
                }
            }
        },
    )
    
    
}




@Composable
fun CardList(tracks: List<Track>, onTrackClicked: (track: Track) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp)
    ) {
        items(tracks) { track ->
            TrackItem(track, onTrackClicked)
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun EasyTracksScreen() {

    val tracks: List<Track> = listOf(
        Track(
            "Trasa 1",
            "Opis 1",
            R.drawable.trasa_1
        ),
        Track( "Trasa 2",
            "Opis 2",
            R.drawable.trasa_2
        ),
        Track( "Trasa 3",
            "Opis 3",
            R.drawable.trasa_3
        ),
        Track(
            "Trasa 4",
            "Opis 4",
            R.drawable.trasa_4
        ),
        Track( "Trasa 5",
            "Opis 5",
            R.drawable.trasa_5
        ),
        Track( "Trasa 6",
            "Opis 6",
            R.drawable.trasa_6
        )
    )

    val navigator = rememberListDetailPaneScaffoldNavigator<Nothing>()
    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                CardList(
                    tracks = tracks,
                    onTrackClicked = {
                        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                    }
                )
            }
        },

        detailPane = {
            AnimatedPane {
                navigator.currentDestination?.content?.let {
                    Text("Detale")
                }
            }
        },
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackItem(track: Track, onTrackClicked: (track: Track) -> Unit) {

    Card(onClick = {
        Log.d("Card item", "clicked")
        onTrackClicked(track)

    }) {
        androidx.compose.foundation.Image(painter = painterResource(track.photo) , contentDescription = "")
        Text(text = track.name)
    }
}


class Track(name: String, description: String, photo: Int) {
    var name: String = name
    var description: String = description
    var photo: Int = photo
}

@Composable
fun AboutScreen() {
    Text(text= "Aplikacja Tras")
}
