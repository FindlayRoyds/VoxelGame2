package client

import common.GameEngine
import common.GameEngineProvider
import common.event.serverevents.ConnectionRequestEvent
import common.networking.SocketHandler
import java.net.Socket

class Client(serverAddress: String, serverPort: Int): GameEngine() {
    private var socketHandler: SocketHandler

    init {
        GameEngineProvider.setGameEngine(this)
        socketHandler = SocketHandler(Socket(serverAddress, serverPort), eventQueue)
        println("Client Starting...")

        socketHandler.sendEvent(ConnectionRequestEvent("MineOrienteer69"))
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