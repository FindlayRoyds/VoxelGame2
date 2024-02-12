package server.networking

import common.EventQueue
import common.GameEngineProvider
import common.event.Event
import common.networking.NetworkHandler
import common.player.Player
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread

class ServerSocketHandler(port: Int, private val eventQueue: EventQueue) {
    private var serverSocket = ServerSocket(port)
    private var socketToUserIDMap = HashMap<Socket, Int>()
    private var userIDToSocketMap = HashMap<Int, Socket>()
    private var networkHandlers = HashMap<Socket, NetworkHandler>()
    private var listenerThread: Thread

    init {
        val gameEngine = GameEngineProvider.getGameEngine()
        listenerThread = thread {
            GameEngineProvider.setGameEngine(gameEngine)
            this.listener()
        }
    }

    fun getUserIDFromSocket(socket: Socket): Int {
        return socketToUserIDMap[socket]!!
    }

    private fun getSocketFromUserID(userID: Int): Socket {
        return userIDToSocketMap[userID]!!
    }

    private fun sendEventToSocket(event: Event, socket: Socket) {
        networkHandlers[socket]!!.sendEvent(event)
    }

    fun sendEventToUserID(event: Event, userID: Int) {
        sendEventToSocket(event, getSocketFromUserID(userID))
    }

    fun sendEventToPlayer(event: Event, player: Player) {
        sendEventToUserID(event, player.userID)
    }

    private fun listener() {
        while (true) {
            val clientSocket = serverSocket.accept()
            val gameEngine = GameEngineProvider.getGameEngine()
            val userID = gameEngine.players.getNextUserID()
            socketToUserIDMap[clientSocket] = userID
            userIDToSocketMap[userID] = clientSocket

            thread {
                GameEngineProvider.setGameEngine(gameEngine)
                val networkHandler = NetworkHandler(clientSocket, eventQueue)
                networkHandlers[clientSocket] = networkHandler
            }
        }
    }
}