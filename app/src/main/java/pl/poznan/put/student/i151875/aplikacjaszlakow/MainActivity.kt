package pl.poznan.put.student.i151875.aplikacjaszlakow

import android.graphics.PathMeasure
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.painterResource
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.core.graphics.PathParser
import java.io.Serializable
import java.util.Calendar
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TabNavigationScreen()
        }
    }
}


val DetailPanelCompositeSaver = listSaver<MutableList<DetailPanelComposite>, String>(
    save = { list ->
        list.map { dpc ->
            "${dpc.track.id};${dpc.track.photo};${dpc.track.name};${dpc.track.description};" +
                    "${dpc.timerState.value};${dpc.timerState.exitTimestamp};${dpc.timerState.isPaused}"
        }
    },
    restore = { list ->
        list.mapTo(mutableListOf()) { item ->
            val parts = item.split(';')
            val track = Track(parts[0].toInt(), parts[1].toInt(), parts[2], parts[3])
            val timerState = MyTimerState(parts[4].toLong(), parts[5].toLong(), parts[6].toBoolean())
            DetailPanelComposite(track, timerState)
        }
    }
)


@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabNavigationScreen() {
    val tabs = listOf("O Aplikacji", "Łatwe Trasy", "Trudne Trasy")
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    val initialEasyDpcs = listOf(
        DetailPanelComposite(
            Track(1, R.drawable.trasa_1, "Szlak Leśnych Opowieści", "Ten 5-kilometrowy szlak wiedzie przez malownicze lasy sosnowe i brzozowe, oferując piękne widoki na otaczającą przyrodę. Trasa jest płaska i dobrze oznakowana, idealna dla rodzin z dziećmi."),
            MyTimerState(0, 0L, true)
        ),
        DetailPanelComposite(
            Track(2, R.drawable.trasa_2, "Ścieżka Ptasiego Śpiewu", "4-kilometrowa trasa prowadząca przez tereny chronione, gdzie można obserwować różnorodne gatunki ptaków. Ścieżka jest szeroka i łatwo dostępna, z licznymi punktami obserwacyjnymi."),
            MyTimerState(0, 0L, true)
        ),
        DetailPanelComposite(
            Track(3, R.drawable.trasa_3, "Szlak Sielskich Wzgórz", "Krótka, 3-kilometrowa trasa po łagodnych, zielonych wzgórzach. Idealna na krótki spacer wśród kwitnących łąk i polnych kwiatów. Na trasie znajdują się ławeczki i tablice informacyjne."),
            MyTimerState(0, 0L, true)
        ),
        DetailPanelComposite(
            Track(4, R.drawable.trasa_4, "Ścieżka Nadmorskiej Bryzy", "Spacerowy szlak o długości 4,5 km biegnący wzdłuż wybrzeża Morza Bałtyckiego. Delikatny morski wiatr i piękne widoki na morze czynią tę trasę wyjątkowo relaksującą."),
            MyTimerState(0, 0L, true)
        ),
        DetailPanelComposite(
            Track(5, R.drawable.trasa_5, "Trasa Jeziornych Krajobrazów", "5-kilometrowa ścieżka wokół malowniczego jeziora, idealna na spokojne wędrówki. Trasa jest płaska i biegnie wzdłuż brzegu, oferując miejsca do piknikowania."),
            MyTimerState(0, 0L, true)
        ),
        DetailPanelComposite(
            Track(6, R.drawable.trasa_6, "Szlak Leśnych Wspomnień", "4-kilometrowa trasa przez historyczny las, gdzie można natknąć się na starożytne ruiny i pozostałości dawnych osad. Szlak jest łatwy i odpowiedni dla wszystkich grup wiekowych."),
            MyTimerState(0, 0L, true)
        )
    )
    val dpcsEasy = rememberSaveable(saver = DetailPanelCompositeSaver) {
        mutableStateListOf(*initialEasyDpcs.toTypedArray())
    }
    val initialHardDpcs = listOf(
        DetailPanelComposite(
            Track(7, R.drawable.trasa_7, "Wielka Pętla Tatrzańska", "25-kilometrowa, wymagająca trasa prowadząca przez najwyższe partie Tatr. Szlak obejmuje strome podejścia, wąskie przejścia i ekspozycje, a także przejścia przez wysokogórskie przełęcze."),
            MyTimerState(0, 0L, true)
        ),
        DetailPanelComposite(
            Track(8, R.drawable.trasa_8, "Szlak Orlich Skał", "20-kilometrowa trasa biegnąca przez skaliste i strome tereny z licznymi przewyższeniami. Szlak jest pełen wyzwań technicznych i wymaga dobrej kondycji fizycznej."),
            MyTimerState(0, 0L, true)
        ),
        DetailPanelComposite(
            Track(9, R.drawable.trasa_9, "Wędrówka Po Dziewiczych Grani", "18-kilometrowa trasa przez surowe i dzikie tereny górskie, z minimalnymi oznaczeniami i trudnymi warunkami terenowymi. Wymaga doświadczenia i umiejętności nawigacyjnych."),
            MyTimerState(0, 0L, true)
        ),
        DetailPanelComposite(
            Track(10, R.drawable.trasa_10, "Ekstremalna Przygoda Karpacka", "22-kilometrowy szlak prowadzący przez niezamieszkane i dzikie tereny Karpat. Trasa jest pełna stromych podejść, skalistych odcinków i wymaga dobrej orientacji w terenie."),
            MyTimerState(0, 0L, true)
        ),
        DetailPanelComposite(
            Track(11, R.drawable.trasa_11, "Szlak Górskich Wyzwań", "23-kilometrowa trasa wiodąca przez wymagające technicznie odcinki górskie, z licznymi ekspozycjami i przepaściami. Odpowiednia dla doświadczonych hikerów."),
            MyTimerState(0, 0L, true)
        ),
        DetailPanelComposite(
            Track(12, R.drawable.trasa_12, "Wielka Korona Beskidów", "26-kilometrowy szlak przez najwyższe szczyty Beskidów, z dużymi różnicami wysokości i trudnymi warunkami terenowymi. Wymaga znakomitej kondycji fizycznej i przygotowania."),
            MyTimerState(0, 0L, true)
        )
    )
    val dpcsHard = rememberSaveable(saver = DetailPanelCompositeSaver) {
        mutableStateListOf(*initialHardDpcs.toTypedArray())
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
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

        HorizontalPager(
            count = tabs.size,
            state = pagerState
        ) { page ->
            when (page) {
                0 -> AboutScreen()
                1 -> EasyTracksScreen(dpcsEasy)
                2 -> EasyTracksScreen(dpcsHard)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun EasyTracksScreen(dpcs: MutableList<DetailPanelComposite>) {
    var selecteddpcindex by rememberSaveable {
        mutableIntStateOf(0)
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
                TrackDetail(dpcs, selecteddpcindex)
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
        Image(
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
    val tvm by rememberSaveable { mutableStateOf( TimerViewModel(dpcs[index].timerState.value, dpcs[index].timerState.exitTimestamp, dpcs[index].timerState.isPaused)) }
    val context = LocalContext.current
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) {
        Toast.makeText(context, "Zdjęcie zrobione!", Toast.LENGTH_SHORT).show()
    }

    DisposableEffect(tvm) {
        onDispose {
            val gt = tvm.timer.value
            val tt = Calendar.getInstance().time.time
            val ip = true
            dpcs[index].timerState.value = gt
            dpcs[index].timerState.exitTimestamp = tt
            dpcs[index].timerState.isPaused = ip
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    cameraLauncher.launch(null)
                },
            ) {
                Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Take a Selfie")
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Image(
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
}

@Composable
fun TrackGrid(dpcs: MutableList<DetailPanelComposite>, onTrackSelected: (Int) -> Unit) {
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
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Text(
            text = "Aplikacja umożliwia przeglądanie i odkrywanie różnorodnych tras hikingowych o różnym stopniu trudności. Użytkownicy mogą przeglądać szczegółowe opisy tras, oglądać zdjęcia oraz śledzić swoje postępy na mapie. Aplikacja oferuje także funkcje rejestrowania czasu oraz dzielenia się wrażeniami z innymi użytkownikami.",
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            color = androidx.compose.ui.graphics.Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Justify
        )
        HikingAnimation()
    }
}

@Preview
@Composable
fun AS() {
    AboutScreen()
}

@Composable
fun HikingAnimation() {
    var targetProgress by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(Unit) {
        while (true) {
            targetProgress = 1f
            kotlinx.coroutines.delay(4000)
            targetProgress = 0f
            kotlinx.coroutines.delay(4000)
        }
    }

    val progress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = TweenSpec(durationMillis = 4000, easing = LinearEasing), label = ""
    )

    val pathData = "M0,50Q150,0 300,50T400,50"
    val path = PathParser.createPathFromPathData(pathData)

    val pathMeasure = PathMeasure(path, false)
    val pathLength = pathMeasure.length
    val pos = floatArrayOf(0f, 0f)

    pathMeasure.getPosTan(progress * pathLength, pos, null)

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.trail),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .wrapContentSize(),
            contentScale = ContentScale.FillWidth
        )
        Image(
            painter = painterResource(id = R.drawable.hiker),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .size(48.dp)
                .offset(x = pos[0].dp - 24.dp, y = pos[1].dp - 410.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}