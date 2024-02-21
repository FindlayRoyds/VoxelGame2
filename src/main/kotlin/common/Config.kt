package common

object Config {
    // How many times per second a game tick occurs (when all the events in the queue are run)
    val tickRate = 20
    val mouseSensitivity = 0.01
    val characterFlySpeed = 0.05

    // AUTOMATIC CONFIG
    private const val MILLISECONDS_PER_SECOND = 1000
    val tickTime = MILLISECONDS_PER_SECOND / tickRate
}