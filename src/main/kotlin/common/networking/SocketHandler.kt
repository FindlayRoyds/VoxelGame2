package common.networking

import common.EventQueue
import common.GameEngineProvider
import common.event.Event
import common.event.ServerEvent
import common.event.clientevents.DisconnectClientEvent
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.Socket
import java.net.SocketException
import kotlin.concurrent.thread
import kotlin.system.exitProcess

class SocketHandler(private val socket: Socket, private val eventQueue: EventQueue) {
    private val writer = ObjectOutputStream(socket.getOutputStream())
    private var listenerThread: Thread

    init {
        val gameEngine = GameEngineProvider.getGameEngine()
        listenerThread = thread {
            GameEngineProvider.setGameEngine(gameEngine)
            this.listener()
        }
    }

    fun getLocalIPv4Address(): String? {
        return NetworkInterface.getNetworkInterfaces().toList()
            .flatMap { it.inetAddresses.toList() }
            .firstOrNull {
                !it.isLoopbackAddress && it is InetAddress && it.hostAddress.indexOf(':') == -1
            }?.hostAddress
    }

    fun sendEvent(eventObject: Event) {
        try {
            writer.writeObject(eventObject)
        } catch (exception: SocketException) {
            println("Disconnected from server")
            exitProcess(0)
        }
    }

    private fun listener() {
        val reader = ObjectInputStream(socket.getInputStream())

        try {
            while (true) {
                val event = reader.readObject()
                if (event is Event) {
                    if (event is ServerEvent) {
                        event.socket = socket
                    }
                    eventQueue.addEvent(event)
                }
            }
        } catch (exception: Exception) {
            val isServer = GameEngineProvider.getGameEngine().isServer()
            println("Exception in socket handler listener: $exception.")
            println("Is server: ${isServer}")
            if (!isServer) {
                val disconnectEvent = DisconnectClientEvent()
                // disconnectEvent.socket = socket
                eventQueue.addEvent(disconnectEvent)
            }
        } finally {
            socket.close()
        }
    }
}