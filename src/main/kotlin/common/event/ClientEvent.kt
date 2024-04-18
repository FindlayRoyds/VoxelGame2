package common.event

import client.Client
import common.GameEngineProvider

abstract class ClientEvent : Event() {
    protected var client: Client? = null

    override fun run() {
        val gameEngine = GameEngineProvider.getGameEngine()
        if (gameEngine.isServer())
            throw Exception("Client event attempting to run on a server!")
        client = gameEngine as Client

        event()
    }
}