package pl.poznan.put.student.i151875.aplikacjaszlakow

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
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


import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.ui.tooling.preview.Preview
import java.io.Serializable
import java.sql.Timestamp
import java.util.Calendar
import java.util.Date
import kotlin.reflect.KProperty

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



val DetailPanelCompositeSaver = listSaver<MutableList<DetailPanelComposite>, String>(
    save = { list ->
        list.map { dpc ->
            "${dpc.track.id},${dpc.track.photo},${dpc.track.name},${dpc.track.description}," +
                    "${dpc.timerState.value},${dpc.timerState.exitTimestamp},${dpc.timerState.isPaused}"
        }
    },
    restore = { list ->
        list.mapTo(mutableListOf()) { item ->
            val parts = item.split(',')
            val track = Track(parts[0].toInt(), parts[1].toInt(), parts[2], parts[3])
            val timerState = MyTimerState(parts[4].toLong(), parts[5].toLong(), parts[6].toBoolean())
            DetailPanelComposite(track, timerState)
        }
    }
)

@Composable
fun TabScreen() {

    val initialDpcs = listOf(
        DetailPanelComposite(
        Track(7, R.drawable.trasa_7, "Trasa 7", "Opis 7"),
        MyTimerState(0, 0L, true)
        ),
        DetailPanelComposite(
        Track(8, R.drawable.trasa_8, "Trasa 8", "Opis 8"),
        MyTimerState(0, 0L, true)
        ),
        DetailPanelComposite(
        Track(9, R.drawable.trasa_9, "Trasa 9", "Opis 9"),
        MyTimerState(0, 0L, true)
        ),
        DetailPanelComposite(
        Track(10, R.drawable.trasa_10, "Trasa 10", "Opis 10"),
        MyTimerState(0, 0L, true)
        ),
        DetailPanelComposite(
        Track(11, R.drawable.trasa_11, "Trasa 11", "Opis 11"),
        MyTimerState(0, 0L, true)
        ),
        DetailPanelComposite(
        Track(12, R.drawable.trasa_12, "Trasa 12", "Opis 12"),
        MyTimerState(0, 0L, true)
        )
    )


    val dpcs = rememberSaveable(saver = DetailPanelCompositeSaver) {
        mutableStateListOf(*initialDpcs.toTypedArray())
    }

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
                EasyTracksScreen(dpcs)
            }
            2 -> {
                EasyTracksScreen(dpcs)
            }
        }
    }
}



@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun EasyTracksScreen(dpcs: MutableList<DetailPanelComposite>) {

    var selecteddpcindex by rememberSaveable() {
        mutableStateOf(0)
    }

    val navigator = rememberListDetailPaneScaffoldNavigator<Nothing>()
    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }


    ListDetailPaneScaffold(
        scaffoldState = navigator.scaffoldState,
        listPane = {
            AnimatedPane(Modifier) {
                TrackGrid(dpcs) {
                    selecteddpcindex = it
                    navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                }
            }
        },

        detailPane = {
            AnimatedPane(Modifier) {
                selecteddpcindex.let { index ->
                    TrackDetail(dpcs, index)
                }
            }
        },
    )

}


@Composable
fun TrackItem(track: Track, onClick: () -> Unit) {
    Card(modifier = Modifier
        .padding(2.dp)
        .clickable { onClick() }

    ) {

        androidx.compose.foundation.Image(
            painter = painterResource(id = track.photo),
            contentDescription = null,
            modifier = Modifier
                .height(128.dp)
                .fillMaxWidth()
        )
        Text(text = track.name, style = MaterialTheme.typography.labelMedium)

    }
}

@Composable
fun TrackDetail(dpcs: MutableList<DetailPanelComposite>, index: Int) {
    val track = dpcs[index].track
    val tvm = TimerViewModel(dpcs[index].timerState.value, dpcs[index].timerState.exitTimestamp, dpcs[index].timerState.isPaused)

    DisposableEffect(tvm) {
        onDispose {
            dpcs[index].timerState.value = tvm.getTime()
            dpcs[index].timerState.exitTimestamp = Calendar.getInstance().time.time
            dpcs[index].timerState.isPaused = true
        }
    }

    Column(modifier = Modifier
        .padding(16.dp)
        .verticalScroll(rememberScrollState())) {
        androidx.compose.foundation.Image(
            painter = painterResource(id = track.photo),
            contentDescription = null,
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = track.name, style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = track.description, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        MyTimerContent(timerViewModel = tvm)
    }
}

@Composable
fun TrackGrid(dpcs: List<DetailPanelComposite>, onTrackSelected: (Int) -> Unit) {
    Row {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(128.dp),
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.width(150.dp)
        ) {
            items(dpcs.size) { index ->
                TrackItem(track = dpcs[index].track) {
                    onTrackSelected(index)
                }
            }
        }
    }
}


data class Track(
    val id: Int,
    val photo: Int,
    val name: String,
    val description: String
) : Serializable

data class MyTimerState(
    var value: Long,
    var exitTimestamp: Long,
    var isPaused: Boolean
) : Serializable

data class DetailPanelComposite(
    val track: Track,
    var timerState: MyTimerState
) : Serializable

@Composable
fun AboutScreen() {
    Text(text= "Aplikacja Tras")
}
