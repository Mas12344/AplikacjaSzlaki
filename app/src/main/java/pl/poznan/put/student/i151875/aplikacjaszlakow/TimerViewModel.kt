package pl.poznan.put.student.i151875.aplikacjaszlakow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TimerViewModel : ViewModel() {
    private val _timer = MutableStateFlow(0L)
    val timer = _timer.asStateFlow()

    private var timerJob: Job? = null
    private var selectedTrack: Track? = null

    fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _timer.value++
            }
        }
    }

    fun pauseTimer() {
        timerJob?.cancel()
    }

    fun stopTimer() {
        _timer.value = 0
        timerJob?.cancel()
    }

    fun setSelectedTrack(track: Track) {
        selectedTrack = track
    }

    fun getTrackByName(name: String): Track? {
        return if (selectedTrack?.name == name) selectedTrack else null
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}

class TrackTimerViewModelFactory(
    private val track: Track,
    private val parentViewModel: TimerViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.cast(TrackTimerViewModel(track, parentViewModel))!!
    }
}

class TrackTimerViewModel(
    private val track: Track,
    private val parentViewModel: TimerViewModel
) : ViewModel() {
    private val _timer = MutableStateFlow(0L)
    val timer = _timer.asStateFlow()

    private var timerJob: Job? = null

    init {
        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _timer.value++
                parentViewModel.setSelectedTrack(track)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }

}