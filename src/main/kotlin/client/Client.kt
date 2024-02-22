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
        Window.WindowOptions(false, 120, 600, 800)
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

        val positions = byteArrayOf(
            0, 1, 3, 2, 0, 3
        )

        val mesh = Mesh(positions, positions.size)
        scene.addMesh("quad", mesh)

        main()
    }

    fun stop() {
        running = false
        val disconnectEvent = DisconnectEvent()
        socketHandler.sendEvent(disconnectEvent)
        disconnectEvent.run()
    }

    override fun isServer(): Boolean {
        return false
    }

    override fun isClient(): Boolean {
        return true
    }

    private fun main() {
        val wantedFps = window.windowOptions.fps
        var initialTime = System.currentTimeMillis()
        val timeR = if (wantedFps > 0) 1000.0 / wantedFps else 0.0
        var deltaFps = 0.0
        var deltaTimeMillis = 0.0

        while (running && !window.shouldClose()) {
            val now = System.currentTimeMillis()

            deltaTimeMillis += (now - initialTime).toDouble()
            deltaFps += (now - initialTime) / timeR
            if (wantedFps <= 0 || deltaFps >= 1) {
                pollEvents()
                runEvents(deltaTimeMillis / 1000)
                runGraphics()

                deltaFps--
                deltaTimeMillis = 0.0
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

    private fun runEvents(deltaTimeS: Double) {
        eventQueue.runEvents(deltaTimeS)
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