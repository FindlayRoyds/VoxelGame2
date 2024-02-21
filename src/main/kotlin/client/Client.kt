package client

import client.graphics.Mesh
import client.graphics.Renderer
import client.graphics.Scene
import client.graphics.Window
import common.GameEngine
import common.GameEngineProvider
import common.event.commonevents.DisconnectEvent
import common.event.serverevents.ConnectionRequestEvent
import common.networking.SocketHandler
import java.net.Socket


class Client(serverAddress: String, serverPort: Int): GameEngine() {
    var window = Window(
        "Test",
        Window.WindowOptions(false, 60, 600, 800)
    ) { resize() }
    var socketHandler: SocketHandler
    var running = false
    var renderer = Renderer(window.width, window.height)
    var scene = Scene()

    init {
        running = true
        GameEngineProvider.setGameEngine(this)

        socketHandler = SocketHandler(Socket(serverAddress, serverPort), eventQueue)
        socketHandler.sendEvent(ConnectionRequestEvent("MineOrienteer69"))

        GameEngineProvider.setGameEngine(this)

        val positions = floatArrayOf(
            -0.5f,  0.5f, -1.0f,
            -0.5f, -0.5f, -1.0f,
             0.5f, -0.5f, -1.0f,
             0.5f,  0.5f, -1.0f,
        )
        val colors = floatArrayOf(
            0.5f, 0.0f, 0.0f,
            0.0f, 0.5f, 0.0f,
            0.0f, 0.0f, 0.5f,
            0.0f, 0.5f, 0.5f,
        )
        val indices = intArrayOf(
            0, 1, 3, 3, 1, 2,
        )
        val mesh = Mesh(positions, colors, indices)
        scene.addMesh("quad", mesh)

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
        val wantedFps = 60
        var initialTime = System.currentTimeMillis()
        val timeR = if (wantedFps > 0) 1000.0f / wantedFps else 0f
        var deltaFps = 0f

        while (running && !window.shouldClose()) {
            val now = System.currentTimeMillis()
            deltaFps += (now - initialTime) / timeR
            /*if (targetFps <= 0 || deltaFps >= 1) {
                appLogic.input(window, scene, now - initialTime)
            }*/
            if (wantedFps <= 0 || deltaFps >= 1) {
                pollEvents()
                runEvents()
                runGraphics()
                deltaFps--
            }
            initialTime = now
        }

        cleanup()
    }

    private fun pollEvents() {
        window.mouseInput.pollInput()
        window.keyboardInput.pollInput()
        window.pollEvents()
    }

    private fun runEvents() {
        eventQueue.runEvents()
    }

    private fun runGraphics() {
        renderer.render(window, scene)
        window.update()
    }

    private fun cleanup() {
        window.cleanup()
        stop()
    }

    private fun resize() {
        renderer.resize(window.width, window.height)
        renderer.render(window, scene)
        window.update()
    }
}