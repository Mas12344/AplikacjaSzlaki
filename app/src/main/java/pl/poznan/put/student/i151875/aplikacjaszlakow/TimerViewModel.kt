package pl.poznan.put.student.i151875.aplikacjaszlakow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.sql.Time
import java.sql.Timestamp
import java.util.Calendar
import java.util.Date

class TimerViewModel(initvalue: Long = 0L, exitts: Long = Calendar.getInstance().time.time, isPaused: Boolean = true) : ViewModel() {
    private val _timer = MutableStateFlow(0L)
    val timer = _timer.asStateFlow()

    private var timerJob: Job? = null

    init {
        _timer.value = initvalue
        if(!isPaused) {
            _timer.value += Calendar.getInstance().time.time - exitts
            startTimer()
        }
    }
    fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _timer.value++
            }
        }
    }

    fun getTime(): Long {
        return _timer.value
    }

    fun pauseTimer() {
        timerJob?.cancel()
    }

    fun stopTimer() {
        _timer.value = 0
        timerJob?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
