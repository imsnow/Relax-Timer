package ru.elegant.relaxtimer.model

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.elegant.relaxtimer.model.data.TimerMode
import ru.elegant.relaxtimer.model.data.TimerSetting
import ru.elegant.relaxtimer.model.data.TimerStatus

class TimerPlayerImpl(
    private val scope: CoroutineScope
) : TimerPlayer, CoroutineScope by scope {

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        println("coroutine canceled")
    }

    private lateinit var _timerSetting: TimerSetting
    private val timerSetting: TimerSetting
        get() = _timerSetting

    private var currentDuration: Long = 0L
    private var requestPause = false
    private var requestStop = false
    private val callbacks = mutableListOf<OnTimerCallback>()

    private var _isActive = false
    override val isActive: Boolean
        get() = _isActive

    override fun subscribeCallback(onTimerCallback: OnTimerCallback) {
        callbacks.add(onTimerCallback)
    }

    override fun unsubscribeCallback(onTimerCallback: OnTimerCallback) {
        callbacks.remove(onTimerCallback)
    }

    private fun fireCallbackStatus(status: TimerStatus) {
        callbacks.forEach { it.onStatus(status) }
    }

    override fun setTimer(timerSetting: TimerSetting) {
        _timerSetting = timerSetting
    }

    override fun start() {
        val duration = timerSetting.duration
        val tick = timerSetting.tickTime
        val mode = timerSetting.mode
        launch(coroutineExceptionHandler) {
            runTimer(duration, tick, mode)
        }
    }

    private suspend fun runTimer(duration: Long, tick: Long, mode: TimerMode) {
        currentDuration = duration

        _isActive = true
        fireCallbackStatus(TimerStatus.Start)
        while (callbacks.isNotEmpty()) {

            fireCallbackStatus(TimerStatus.Tick(currentDuration))
            delay(tick)

            if (currentDuration == 0L) {
                fireCallbackStatus(TimerStatus.DurationEnd)
                if (mode != TimerMode.INFINITY) break
            }

            currentDuration -= tick

            if (requestPause) {
                requestPause = false
                _isActive = false
                fireCallbackStatus(TimerStatus.Pause)
                return
            }

            if (requestStop) {
                requestStop = false
                break
            }
        }
        _isActive = false
        fireCallbackStatus(TimerStatus.Stop)
    }

    override fun resume() {
        launch(coroutineExceptionHandler) {
            val duration = currentDuration
            val tick = timerSetting.tickTime
            val mode = timerSetting.mode
            runTimer(duration, tick, mode)
        }
    }

    override fun pause() {
        requestPause = true
    }

    override fun stop() {
        requestStop = true
    }
}