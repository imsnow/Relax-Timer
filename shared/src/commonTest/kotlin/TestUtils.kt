import kotlinx.coroutines.CoroutineScope

expect class TestUtils {

    class TestRunner {

        val coroutineScope: CoroutineScope

        fun <T> runTest(block: suspend () -> T)

        fun clear()

        fun advanceTimeBy(delayTimeMillis: Long)
    }
}