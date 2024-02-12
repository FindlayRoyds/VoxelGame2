package common.networking

import common.EventQueue
import common.GameEngineProvider
import common.event.Event
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.Socket
import kotlin.concurrent.thread

class NetworkHandler(private val socket: Socket, private val eventQueue: EventQueue) {
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

        while (true) {
            val event: Event = reader.readObject() as Event
            event.socket = socket
            eventQueue.addEvent(event)
        }
    }
}