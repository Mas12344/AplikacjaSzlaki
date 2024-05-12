package pl.poznan.put.student.i151875.aplikacjaszlakow

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            Navig(navController)
        }
    }

}

@Composable
fun Navig(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "main") {
        var selectedTrack: Track = Track("test","test",R.drawable.ic_launcher_background)
        composable(route="main") {
            TabScreen() {
                track -> selectedTrack = track
                navController.navigate(route = "details")
            }
        }
        composable(route="details") {
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                DetailsContent(selectedTrack)

                val timerViewModel = MyTimerViewModel()
                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyTimerContent(timerViewModel = timerViewModel)

                }
            }

        }
    }
}



@Composable
fun TabScreen(onTrackClicked: (track: Track) -> Unit) {
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
            1 -> EasyTracksScreen(onTrackClicked)
            2 -> HardTracksScreen(onTrackClicked)
        }
    }
}

@Composable
fun HardTracksScreen(onTrackClicked: (track: Track) -> Unit) {
    val tracks: List<Track> = listOf(
        Track(
            "Trasa 1",
            "Opis 1",
            R.drawable.track1
        ),
        Track( "Trasa 2",
            "Opis 2",
            R.drawable.track2
        ),
        Track( "Trasa 3",
            "Opis 3",
            R.drawable.track3)
    )
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp)
    ) {
        items(tracks) { track ->
            TrackItem(track, onTrackClicked)
        }
    }
}

@Composable
fun EasyTracksScreen(onTrackClicked: (track: Track) -> Unit) {

    //var trackIndex by rememberSaveable { mutableStateOf(-1) }

    val tracks: List<Track> = listOf(
        Track(
            "Trasa 1",
            "Opis 1",
            R.drawable.track1
        ),
        Track( "Trasa 2",
            "Opis 2",
            R.drawable.track2
        ),
        Track( "Trasa 3",
            "Opis 3",
            R.drawable.track3)
    )
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp)
    ) {
        items(tracks) { track ->
            TrackItem(track, onTrackClicked)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackItem(track: Track, onTrackClicked: (track: Track) -> Unit) {
    val context = LocalContext.current
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