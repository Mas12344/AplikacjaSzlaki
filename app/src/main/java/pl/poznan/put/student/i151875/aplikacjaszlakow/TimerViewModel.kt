package pl.poznan.put.student.i151875.aplikacjaszlakow

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parceler
import java.util.Calendar

import kotlinx.parcelize.Parcelize

@Parcelize
class TimerViewModel(initvalue: Long = 0L, exitts: Long = Calendar.getInstance().time.time, isPaused: Boolean = true) : ViewModel(),
    Parcelable {
    private companion object : Parceler<TimerViewModel> {
        override fun TimerViewModel.write(parcel: Parcel, flags: Int) {
            parcel.writeLong(_timer.value)
        }

        override fun create(parcel: Parcel): TimerViewModel {
            val initvalue = parcel.readLong()
            return TimerViewModel(initvalue)
        }
    }
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
