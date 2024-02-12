package common

import common.player.Players
import common.world.World

abstract class GameEngine {
    val world = World()
    val players = Players()
    protected val eventQueue = EventQueue()

    abstract fun isServer(): Boolean
    abstract fun isClient(): Boolean
}