package common

object Config {
    // How many times per second a game tick occurs (when all the events in the queue are run)
    private const val tickRate = 20


    // AUTOMATIC CONFIG
    private const val MILLISECONDS_PER_SECOND = 1000
    const val tickTime = MILLISECONDS_PER_SECOND / tickRate
}