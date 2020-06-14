import ru.elegant.relaxtimer.model.OnTimerCallback
import ru.elegant.relaxtimer.model.TimerPlayer
import ru.elegant.relaxtimer.model.TimerPlayerImpl
import ru.elegant.relaxtimer.model.data.TimerMode
import ru.elegant.relaxtimer.model.data.TimerSetting
import ru.elegant.relaxtimer.model.data.TimerStatus
import kotlin.test.*

open class TimerPlayerTest(private val testRunner: TestUtils.TestRunner) {

    private val statusSequence = mutableListOf<TimerStatus>()

    private val onTimerCallback = object : OnTimerCallback {
        override fun onStatus(status: TimerStatus) {
            statusSequence.add(status)
        }
    }

    private val timerPlayer: TimerPlayer = TimerPlayerImpl(
        scope = testRunner.coroutineScope
    )

    @BeforeTest
    fun onBefore() {
        timerPlayer.subscribeCallback(onTimerCallback)
    }

    @AfterTest
    fun onAfter() {
        statusSequence.clear()
        timerPlayer.unsubscribeCallback(onTimerCallback)
    }

    /**
     * Создается Strict таймер. Один подписчик
     * Выполняется запуск таймера
     * Таймер активен
     */
    @Test
    fun testStartAndOneSubscriber() {
        // When
        val timerSetting = getDefaultTimerSettings()
        timerPlayer.setTimer(timerSetting)
        // Do
        timerPlayer.start()
        // Verify
        assertTrue { timerPlayer.isActive }
    }

    /**
     * Создается Strict таймер. Без подписчиков
     * Выполняется запуск таймера
     * Таймер не запустится.
     */
    @Test
    fun testStartAndWithoutSubscriber() {
        // When
        val timerSetting = getDefaultTimerSettings()
        timerPlayer.setTimer(timerSetting)
        timerPlayer.unsubscribeCallback(onTimerCallback)
        // Do
        timerPlayer.start()
        // Verify
        assertFalse { timerPlayer.isActive }
    }

    /**
     * Настройки таймера: длительность 1 минута, тик 1 секунда, строгий режим
     * Таймер запускается
     * Подписчик получает статусы Start, 61 Tick, DurationEnd и Stop
     */
    @Test
    fun testStartStrict() {
        // when
        val timerSetting = getDefaultTimerSettings()
        val tickList = getTickListForSettings(timerSetting)
        val rightSequence = mutableListOf<TimerStatus>().apply {
            add(TimerStatus.Start)
            addAll(tickList)
            add(TimerStatus.DurationEnd)
            add(TimerStatus.Stop)
        }
        timerPlayer.setTimer(timerSetting = timerSetting)
        // do
        timerPlayer.start()
        testRunner.advanceTimeBy(timerSetting.duration + ONE_SECOND)
        // verify
        assertEquals(rightSequence.size, statusSequence.size)
        rightSequence.forEachIndexed { index, element ->
            assertTrue { statusSequence[index] == element }
        }
    }

    /**
     * Создается Strict таймер. Два подписчика
     * Выполняется запуск таймера
     * Подписчики получают статусы Start, 60 Tick, DurationEnd и Stop
     */
    @Test
    fun testStartAndTwoSubscriber() {
        // when
        val timerSetting = getDefaultTimerSettings()
        val tickList = getTickListForSettings(timerSetting)
        val rightSequence = mutableListOf<TimerStatus>().apply {
            add(TimerStatus.Start)
            addAll(tickList)
            add(TimerStatus.DurationEnd)
            add(TimerStatus.Stop)
        }
        val statusSequence2 = mutableListOf<TimerStatus>()
        val onTimerCallback2 = object : OnTimerCallback {
            override fun onStatus(status: TimerStatus) {
                statusSequence2.add(status)
            }
        }
        timerPlayer.subscribeCallback(onTimerCallback2)
        timerPlayer.setTimer(timerSetting = timerSetting)
        // do
        timerPlayer.start()
        testRunner.advanceTimeBy(timerSetting.duration + ONE_SECOND)
        // verify
        assertEquals(rightSequence.size, statusSequence.size)
        assertEquals(rightSequence.size, statusSequence2.size)
        rightSequence.forEachIndexed { index, element ->
            assertTrue { statusSequence[index] == element }
        }
        rightSequence.forEachIndexed { index, element ->
            assertTrue { statusSequence2[index] == element }
        }
    }

    /**
     * Дефолтный таймер запускается с одним подписчиком.
     * Через 30 секунд подключается второй.
     * Второй подписчик должен получить Tick 30, DurationEnd, Stop
     */
    fun startTestAndLaterSecond() {
        // when
        val timerSetting = getDefaultTimerSettings()
        val tickList = getTickListForSettings(timerSetting)
        val secondTickList = tickList.subList(30, tickList.size)
        val rightSequence = mutableListOf<TimerStatus>().apply {
            addAll(secondTickList)
            add(TimerStatus.DurationEnd)
            add(TimerStatus.Stop)
        }
        val statusSequence2 = mutableListOf<TimerStatus>()
        val onTimerCallback2 = object : OnTimerCallback {
            override fun onStatus(status: TimerStatus) {
                statusSequence2.add(status)
            }
        }
        timerPlayer.setTimer(timerSetting = timerSetting)
        // do
        timerPlayer.start()
        testRunner.advanceTimeBy(HALF_MINUTE)
        timerPlayer.subscribeCallback(onTimerCallback2)
        // verify
        assertTrue { timerPlayer.isActive }
        rightSequence.forEachIndexed { index, element ->
            assertTrue { statusSequence2[index] == element }
        }
    }

    /**
     * Настройки таймера: длительность 1 минута, тик 1 секунда, бесконечный режим
     * Таймер запускается
     * Должен быть вызваны методы [onTimerCallback]
     * - onStart один раз
     * - onTick 60 раз
     * - onDurationEnd 1 раз
     * - onEnd один раз
     */
    @Test
    fun testStartInfinity() {
        // when
        val timerSetting = getDefaultTimerSettings(mode = TimerMode.INFINITY)
        val ticks = getTickListForSettings(timerSetting)
        val rightSequence = mutableListOf<TimerStatus>().apply {
            add(TimerStatus.Start)
            addAll(ticks)
            add(TimerStatus.DurationEnd)
            addAll(ticks)
            add(TimerStatus.Stop)
        }
        timerPlayer.setTimer(timerSetting)
        // do
        timerPlayer.start()
        testRunner.advanceTimeBy((timerSetting.duration + ONE_SECOND) * 2)
        timerPlayer.stop()
        // verify
        assertTrue { timerPlayer.isActive }
        assertEquals(rightSequence.size, statusSequence.size)
    }

    /**
     * Дефолтный таймер. Один подписчик
     * Таймер запускается и через 30 секунд останавливается паузой
     * Подписчик получает статусы Start, 31 Tick, Pause
     */
    @Test
    fun testPause() {
        // when
        val timeToPause = HALF_MINUTE
        val timerSetting = getDefaultTimerSettings(mode = TimerMode.INFINITY)
        val ticks = getTickListForSettings(timerSetting, downTo = timeToPause)
        val rightSequence = mutableListOf<TimerStatus>().apply {
            add(TimerStatus.Start)
            addAll(ticks)
            add(TimerStatus.Pause)
        }
        timerPlayer.setTimer(timerSetting)
        // do
        timerPlayer.start()
        testRunner.advanceTimeBy(timeToPause)
        timerPlayer.pause()
        testRunner.advanceTimeBy(ONE_SECOND)
        // verify
        assertFalse { timerPlayer.isActive }
        assertEquals(rightSequence.size, statusSequence.size)
        rightSequence.forEachIndexed { index, element ->
            assertTrue { statusSequence[index] == element }
        }
    }

    /**
     * Запущен дефолтный таймер. Один подписчик
     * Таймер ставится на паузу, через секунду снова запускается
     * Подписчик получает статусы Start, 31 Tick, Pause, Start, 30 Tick, DurationEnd, Stop
     */
    @Test
    fun resumeTest() {
        // when
        val timeToPause = HALF_MINUTE
        val timerSetting = getDefaultTimerSettings()
        val ticks = getTickListForSettings(timerSetting)
        val ticksBeforePause = ticks.subList(0, 31)
        val ticksAfterPause = ticks.subList(31, ticks.size)
        val rightSequence = mutableListOf<TimerStatus>().apply {
            add(TimerStatus.Start)
            addAll(ticksBeforePause)
            add(TimerStatus.Pause)
            add(TimerStatus.Start)
            addAll(ticksAfterPause)
            add(TimerStatus.DurationEnd)
            add(TimerStatus.Stop)
        }
        timerPlayer.setTimer(timerSetting)
        // do
        timerPlayer.start()
        testRunner.advanceTimeBy(timeToPause)
        timerPlayer.pause()
        testRunner.advanceTimeBy(ONE_SECOND)
        timerPlayer.resume()
        testRunner.advanceTimeBy(timeToPause + ONE_SECOND)
        // verify
        assertFalse { timerPlayer.isActive }
        rightSequence.forEachIndexed { index, element ->
            if (statusSequence[index] != element) println("status ${statusSequence[index]} element $element")
            assertTrue { statusSequence[index] == element }
        }
    }

    companion object {
        private const val ONE_MINUTE = 1000L * 60
        private const val HALF_MINUTE = 1000L * 30
        private const val ONE_SECOND = 1000L

        private fun getDefaultTimerSettings(mode: TimerMode = TimerMode.STRICT) =
            TimerSetting(
                duration = ONE_MINUTE,
                tickTime = ONE_SECOND,
                mode = mode
            )

        private fun getTickListForSettings(
            timerSetting: TimerSetting,
            downTo: Long = 0L
        ): List<TimerStatus.Tick> {
            val tickList = mutableListOf<TimerStatus.Tick>()
            for (i in timerSetting.duration downTo downTo step timerSetting.tickTime) {
                tickList.add(TimerStatus.Tick(i))
            }
            return tickList
        }
    }
}