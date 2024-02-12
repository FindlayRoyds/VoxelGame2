package common.event

import common.GameEngineProvider

abstract class ClientEvent : Event() {
    override fun run() {
        val gameEngine = GameEngineProvider.getGameEngine()
        if (gameEngine.isServer())
            throw Exception("Client event attempting to run on a server!")

        event()
    }
}