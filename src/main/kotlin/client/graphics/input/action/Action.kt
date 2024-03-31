package client.graphics.input.action

abstract class Action {
    // if true method will continuously be called each tick
    abstract val onHold: Boolean

    fun run(isHold: Boolean) {
        if (isHold && onHold) {
            execute()
        } else if (!isHold && !onHold) {
            execute()
        }
    }

    abstract fun execute()
}