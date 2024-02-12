package client

import common.GameEngine
import common.GameEngineProvider
import common.event.serverevents.ConnectionRequest
import common.networking.NetworkHandler
import java.net.Socket

class Client(serverAddress: String, serverPort: Int): GameEngine() {
    private var networkHandler: NetworkHandler

    init {
        GameEngineProvider.setGameEngine(this)
        networkHandler = NetworkHandler(Socket(serverAddress, serverPort), eventQueue)
        println("Client Starting...")

        networkHandler.sendEvent(ConnectionRequest("MineOrienteer69"))
        main()
    }

    override fun isServer(): Boolean {
        return false
    }

    override fun isClient(): Boolean {
        return true
    }

    private fun main() {
        while (true) {
            eventQueue.runEvents()
        }
    }
}