package server

import common.Config
import common.GameEngine
import common.GameEngineProvider
import server.networking.ServerSocketHandler


class Server(port: Int) : GameEngine() {
    var serverSocketHandler: ServerSocketHandler

    init {
        GameEngineProvider.setGameEngine(this)
        println("Server starting...")

        serverSocketHandler = ServerSocketHandler(port, eventQueue)
        this.main()
    }

    override fun isServer(): Boolean {
        return true
    }

    override fun isClient(): Boolean {
        return false
    }

    private fun main() {
        var startTime = System.currentTimeMillis()
        while (true) {
            eventQueue.runEvents()

            val delayTime = Config.tickTime - (System.currentTimeMillis() - startTime)
            Thread.sleep(delayTime)
            startTime = System.currentTimeMillis()
        }
    }
}