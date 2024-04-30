package client

import client.graphics.Renderer
import client.graphics.Window
import common.GameEngine
import common.GameEngineProvider
import common.event.commonevents.DisconnectClientEvent
import common.event.commonevents.DisconnectServerEvent
import common.event.serverevents.ConnectionRequestServerEvent
import common.math.Int3
import common.networking.SocketHandler
import java.net.Socket


class Client(serverAddress: String, serverPort: Int): GameEngine() {
    var window = Window(
        "VoxelGame2",
        Window.WindowOptions(false, 120, 550, 800)
    ) { resize() }
    var socketHandler: SocketHandler
    var running = false
    var renderer = Renderer(window.width, window.height)

    init {
        running = true
        GameEngineProvider.setGameEngine(this)

        socketHandler = SocketHandler(Socket(serverAddress, serverPort), eventQueue)
        socketHandler.sendEvent(ConnectionRequestServerEvent("MineOrienteer69"))

        world.chunkManager.chunkGenerationExecutor.run()
        val range = 16
        for (x in -range..range) {
            // thread {
            //     GameEngineProvider.setGameEngine(this)
            for (z in -range..range) {
                for (y in -8..8) {
                    world.chunkManager.generateChunk(Int3(x, y, z))
                }
            }
            // }
        }

        main()
    }

    fun stop() {
        running = false
        val disconnectEvent = DisconnectServerEvent()
        socketHandler.sendEvent(disconnectEvent)
        val disconnectClientEvent = DisconnectClientEvent()
        disconnectClientEvent.run()
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

        var frameCount = 0
        val frameCountStartTime = System.currentTimeMillis()
        while (running && !window.shouldClose()) {
            val currentTime = System.currentTimeMillis()
            val deltaTimeMillis = currentTime - startTime

            pollEvents()
            runEvents(deltaTimeMillis / 1000.toDouble())
            runGraphics()

            startTime = currentTime

            frameCount++

            val sleepTime = (targetFrameTime - deltaTimeMillis).coerceAtLeast(0.0)
            if (sleepTime > 0.0) {
                Thread.sleep(sleepTime.toLong())
            }
        }

        val averageFps = frameCount / ((System.currentTimeMillis() - frameCountStartTime).toDouble()) * 1000
        println("Average FPS: $averageFps")

        cleanup()
    }

    private fun pollEvents() {
        window.mouseInput.pollInput()
        window.keyboardInput.pollInput()
        window.pollEvents()
        renderer.camera.pollEvents()
    }

    private fun runEvents(deltaTimeS: Double) {
        eventQueue.runEvents(deltaTimeS)
    }

    private fun runGraphics() {
        renderer.render(window, world)
        window.update()
        world.chunkManager.sendChunksToGPU()
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