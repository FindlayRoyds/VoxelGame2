package common

object Config {
    // How many times per second a game tick occurs (when all the events in the queue are run)
    const val tickRate = 20
    const val mouseSensitivity = 0.005
    const val characterFlySpeed = 10

    // AUTOMATIC CONFIG
    private const val MILLISECONDS_PER_SECOND = 1000
    const val tickTime = MILLISECONDS_PER_SECOND / tickRate
}