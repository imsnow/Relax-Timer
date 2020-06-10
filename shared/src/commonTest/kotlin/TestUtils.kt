import kotlinx.coroutines.CoroutineScope

expect class TestUtils {

    class TestRunner {

        val coroutineScope: CoroutineScope

        fun <T> runTest(block: suspend () -> T)

        fun advanceTimeBy(delayTimeMillis: Long)
    }
}