package ru.elegant.relaxtimer.model

import ru.elegant.relaxtimer.model.data.TimerStatus

interface OnTimerCallback {

    fun onStatus(status: TimerStatus)
}