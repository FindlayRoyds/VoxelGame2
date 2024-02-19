package client

import client.graphics.Renderer
import client.graphics.Window
import common.GameEngine
import common.GameEngineProvider
import common.event.commonevents.DisconnectEvent
import common.event.serverevents.ConnectionRequestEvent
import common.networking.SocketHandler
import java.net.Socket

class Client(serverAddress: String, serverPort: Int): GameEngine() {
    var socketHandler: SocketHandler
    var running = false
    private var window = Window(
        "Test",
        Window.WindowOptions(false, 60, 400, 400)
    ) { resize() }
    private var renderer = Renderer()
    // private var sceneManager: SceneManager

    init {
        running = true
        GameEngineProvider.setGameEngine(this)

        socketHandler = SocketHandler(Socket(serverAddress, serverPort), eventQueue)
        socketHandler.sendEvent(ConnectionRequestEvent("MineOrienteer69"))

        GameEngineProvider.setGameEngine(this)

        main()
    }

    fun stop() {
        running = false
        val disconnectEvent = DisconnectEvent()
        socketHandler.sendEvent(disconnectEvent)
    }

    override fun isServer(): Boolean {
        return false
    }

    override fun isClient(): Boolean {
        return true
    }

    private fun main() {
        var initialTime = System.currentTimeMillis()
        val timeR = if (window.windowOptions.fps > 0) 1000.0f / window.windowOptions.fps else 0f
        var deltaFps = 0f

        while (running && !window.shouldClose()) {
            window.pollEvents()
            eventQueue.runEvents()
            val now = System.currentTimeMillis()
            deltaFps += (now - initialTime) / timeR
            /*if (targetFps <= 0 || deltaFps >= 1) {
                appLogic.input(window, scene, now - initialTime)
            }*/
            if (window.windowOptions.fps <= 0 || deltaFps >= 1) {
                renderer.render(window, world)
                deltaFps--
                window.update()
            }
            initialTime = now
        }

        cleanup()
    }

    private fun cleanup() {
        window.cleanup()
        stop()
    }

    private fun resize() {
        // Nothing to be done yet
    }
}