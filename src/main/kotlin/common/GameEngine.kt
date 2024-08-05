package common

import common.player.Players
import common.world.WorldProvider

abstract class GameEngine {
    val world = WorldProvider.world
    val players = Players()
    val eventQueue = EventQueue()

    abstract fun isServer(): Boolean
    abstract fun isClient(): Boolean
}