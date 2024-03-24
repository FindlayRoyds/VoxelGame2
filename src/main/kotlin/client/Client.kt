package client

import client.graphics.Renderer
import client.graphics.Window
import common.GameEngine
import common.GameEngineProvider
import common.event.commonevents.DisconnectEvent
import common.event.serverevents.ConnectionRequestEvent
import common.math.Int3
import common.networking.SocketHandler
import java.net.Socket


class Client(serverAddress: String, serverPort: Int): GameEngine() {
    var window = Window(
        "VoxelGame2",
        Window.WindowOptions(false, 120, 600, 800)
    ) { resize() }
    var socketHandler: SocketHandler
    var running = false
    var renderer = Renderer(window.width, window.height)

    init {
        running = true
        GameEngineProvider.setGameEngine(this)

        socketHandler = SocketHandler(Socket(serverAddress, serverPort), eventQueue)
        socketHandler.sendEvent(ConnectionRequestEvent("MineOrienteer69"))

        val range = 10
        for (x in -range..range) {
            for (z in -range..range) {
                for (y in -2..3) {
                    world.chunkManager.generateChunk(Int3(x, y, z))
                }
            }
        }
        for (chunk in world.chunkManager.getLoadedChunks()) {
            chunk.buildMesh()
        }

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
        val targetFps = 60
        var startTime = System.currentTimeMillis()
        val targetFrameTime = 1000.0 / targetFps

        while (running && !window.shouldClose()) {
            val currentTime = System.currentTimeMillis()
            val deltaTimeMillis = currentTime - startTime

            pollEvents()
            runEvents(deltaTimeMillis / 1000.toDouble())
            runGraphics()

            startTime = currentTime

            val sleepTime = (targetFrameTime - deltaTimeMillis).coerceAtLeast(0.0)
            if (sleepTime > 0.0) {
                Thread.sleep(sleepTime.toLong())
            }
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
        renderer.render(window, world)
        window.update()
    }

    private fun cleanup() {
        window.cleanup()
        stop()
    }

    private fun resize() {
        renderer.resize(window.width, window.height)
        renderer.render(window, world)
        window.update()
    }
}