package server.networking

import common.EventQueue
import common.GameEngineProvider
import common.networking.NetworkHandler
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread

class ServerSocketHandler(port: Int, private val eventQueue: EventQueue) {
    private var socketToUserIDMap = HashMap<Socket, Int>()
    private var listenerThread: Thread
    private var serverSocket: ServerSocket

    init {
        serverSocket = ServerSocket(port)
        val gameEngine = GameEngineProvider.getGameEngine()
        listenerThread = thread {
            GameEngineProvider.setGameEngine(gameEngine)
            this.listener()
        }
    }

    fun getUserIDFromSocket(socket: Socket): Int {
        return socketToUserIDMap[socket]!!
    }

    private fun listener() {
        while (true) {
            val clientSocket = serverSocket.accept()
            val gameEngine = GameEngineProvider.getGameEngine()
            socketToUserIDMap[clientSocket] = gameEngine.players.getNextUserID()
            thread {
                GameEngineProvider.setGameEngine(gameEngine)
                NetworkHandler(clientSocket, eventQueue)
            }
        }
    }
}