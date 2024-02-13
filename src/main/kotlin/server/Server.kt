package server

import common.Config
import common.GameEngine
import common.GameEngineProvider
import server.networking.ServerNetwork


class Server(port: Int) : GameEngine() {
    var serverNetwork: ServerNetwork

    init {
        GameEngineProvider.setGameEngine(this)
        println("Server starting...")

        serverNetwork = ServerNetwork(port, eventQueue)
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