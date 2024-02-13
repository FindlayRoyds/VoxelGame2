package server.networking

import common.EventQueue
import common.GameEngineProvider
import common.event.Event
import common.networking.SocketHandler
import common.player.Player
import server.Server
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread

class ServerNetwork(port: Int, private val eventQueue: EventQueue) {
    private var serverSocket = ServerSocket(port)
    private var socketToUserIDMap = HashMap<Socket, Int>()
    private var userIDToSocketMap = HashMap<Int, Socket>()
    private var socketHandlers = HashMap<Socket, SocketHandler>()
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
        socketHandlers[socket]!!.sendEvent(event)
    }

    fun sendEventToUserID(event: Event, userID: Int) {
        sendEventToSocket(event, getSocketFromUserID(userID))
    }

    fun sendEventToPlayer(event: Event, player: Player) {
        sendEventToUserID(event, player.userID)
    }

    fun sendEventToEveryone(event: Event) {
        val server = GameEngineProvider.getGameEngine() as Server
        for (player in server.players.getPlayerList())
            sendEventToPlayer(event, player)

    }

    fun sendEventToEveryoneExcluding(event: Event, excludedPlayer: Player) {
        val server = GameEngineProvider.getGameEngine() as Server
        for (player in server.players.getPlayerList())
            if (player != excludedPlayer)
                sendEventToPlayer(event, player)
    }

    fun removeUserID(userID: Int) {
        val socket = userIDToSocketMap[userID]
        socketToUserIDMap.remove(socket)
        socketHandlers.remove(socket)
        userIDToSocketMap.remove(userID)
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
                val socketHandler = SocketHandler(clientSocket, eventQueue)
                socketHandlers[clientSocket] = socketHandler
            }
        }
    }
}