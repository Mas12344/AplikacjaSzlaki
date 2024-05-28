package pl.poznan.put.student.i151875.aplikacjaszlakow

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar


fun Long.formatTime(): String {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    val remainingSeconds = this % 60
    return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
}

@Preview
@Composable
fun MyTimerContentPrev() {
    val mts = MyTimerState(1, Calendar.getInstance().time.time-1, true)
    MyTimerContent(TimerViewModel(mts.value, mts.exitTimestamp, mts.isPaused))
}

@Composable
fun MyTimerContent(timerViewModel: TimerViewModel) {
    val timerValue by timerViewModel.timer.collectAsState()

    MyTimer(
        timerValue = timerValue,
        onStartClick = { timerViewModel.startTimer() },
        onPauseClick = { timerViewModel.pauseTimer() },
        onStopClick = { timerViewModel.stopTimer()})
}


@Composable
fun MyTimer(
    timerValue: Long,
    onStartClick: () -> Unit,
    onPauseClick: () -> Unit,
    onStopClick: () -> Unit
) {
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = timerValue.formatTime(), fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = onStartClick) {
                Text(text = "Start")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = onPauseClick) {
                Text(text = "Pause")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = onStopClick) {
                Text(text = "Stop")
            }

        }
    }
}

