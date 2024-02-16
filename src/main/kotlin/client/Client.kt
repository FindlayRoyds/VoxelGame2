package client

import client.graphics.SceneManager
import common.GameEngine
import common.GameEngineProvider
import common.event.serverevents.ConnectionRequestEvent
import common.event.serverevents.SendChatEvent
import common.networking.SocketHandler
import java.net.Socket
import kotlin.concurrent.thread

class Client(serverAddress: String, serverPort: Int): GameEngine() {
    var socketHandler: SocketHandler
    private var sceneManager: SceneManager

    init {
        println("Client Starting...")
        GameEngineProvider.setGameEngine(this)
        socketHandler = SocketHandler(Socket(serverAddress, serverPort), eventQueue)
        thread {
            GameEngineProvider.setGameEngine(this)
            main()
        }

        socketHandler.sendEvent(ConnectionRequestEvent("MineOrienteer69"))

        sceneManager = SceneManager(eventQueue)
        sceneManager.run()
    }

    override fun isServer(): Boolean {
        return false
    }

    override fun isClient(): Boolean {
        return true
    }

    private fun main() {
        thread { inputListener() }
        while (true) {
            eventQueue.runEvents()
        }
    }

    private fun inputListener() {
        while (true) {
            println("Enter message to send: ")
            val sendChatEvent = SendChatEvent(readln())
            socketHandler.sendEvent(sendChatEvent)
        }
    }
}