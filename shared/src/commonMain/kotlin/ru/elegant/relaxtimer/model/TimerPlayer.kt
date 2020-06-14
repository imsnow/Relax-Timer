package ru.elegant.relaxtimer.model

import ru.elegant.relaxtimer.model.data.TimerSetting

interface TimerPlayer {

    /**
     * Флаг, что таймер в данный момент тикает
     */
    val isActive: Boolean

    fun subscribeCallback(onTimerCallback: OnTimerCallback)

    /**
     * При отсписке последнего, таймер останавливается.
     */
    fun unsubscribeCallback(onTimerCallback: OnTimerCallback)

    /**
     * Если таймер не установить, то запуск приведёт к исключению.
     */
    fun setTimer(timerSetting: TimerSetting)

    fun start()

    fun resume()

    fun pause()

    fun stop()
}