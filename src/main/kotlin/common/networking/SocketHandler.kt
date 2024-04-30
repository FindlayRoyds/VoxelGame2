package common.networking

import common.EventQueue
import common.GameEngineProvider
import common.event.Event
import common.event.ServerEvent
import common.event.commonevents.DisconnectClientEvent
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.Socket
import kotlin.concurrent.thread

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

    fun sendEvent(eventObject: Event) {
        writer.writeObject(eventObject)
    }

    private fun listener() {
        val reader = ObjectInputStream(socket.getInputStream())

        try {
            while (true) {
                val event = reader.readObject()
                if (event is ServerEvent) {
                    event.socket = socket
                    eventQueue.addEvent(event)
                }
            }
        } catch (exception: Exception) {
            val disconnectEvent = DisconnectClientEvent()
            // disconnectEvent.socket = socket
            eventQueue.addEvent(disconnectEvent)
        } finally {
            socket.close()
        }
    }
}