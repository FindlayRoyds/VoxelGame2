package common

object Config {
    const val initialBlockPalletePackedArrayElementSizeInBits = 3

    const val cameraVerticalAngleLimit = 88
    const val useVSync = true

    // How many times per second a game tick occurs (when all the events in the queue are run)
    const val tickRate = 20
    const val chunkSize = 32

    const val renderDistance = 16
    const val unloadDistance = 2

    const val mouseSensitivity = 0.005
    const val characterFlySpeed = 5.0
    const val characterReachDistance = 5.0

    // AUTOMATIC CONFIG
    private const val MILLISECONDS_PER_SECOND = 1000
    const val tickTime = MILLISECONDS_PER_SECOND / tickRate
}