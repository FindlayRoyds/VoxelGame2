package client

import client.graphics.Window
import client.graphics.renderers.MainRenderer
import common.GameEngine
import common.GameEngineProvider
import common.event.clientevents.DisconnectClientEvent
import common.event.servernetworkevents.ConnectionRequestServerEvent
import common.event.servernetworkevents.DisconnectServerEvent
import common.networking.SocketHandler
import java.net.Socket


class Client(serverAddress: String, serverPort: Int): GameEngine() {
    var window = Window(
        Window.WindowOptions(false, "Findlaycraft", 120, 550, 800, false)
    ) { resize() }
//    var window = Window(
//        "test",
//        Window.WindowOptions(false, 120, 550, 800)
//    ) { resize() }
    var socketHandler: SocketHandler
    var running = false
    var mainRenderer = MainRenderer(window, window.width, window.height)

    init {
        running = true
        GameEngineProvider.setGameEngine(this)

        socketHandler = SocketHandler(Socket(serverAddress, serverPort), eventQueue)
        socketHandler.sendEvent(ConnectionRequestServerEvent("MineOrienteer69"))

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
//        launch(Test())
//        Window2()
        world.chunkManager.createChunkMeshingExecutor()
        world.chunkManager.chunkMeshingExecutor!!.run()

        val targetFps = 60
        var startTime = System.currentTimeMillis()
        val targetFrameTime = 1000.0 / targetFps

        var frameCount = 0
        val frameCountStartTime = System.currentTimeMillis()
        while (running && !window.shouldClose) {
            val currentTime = System.currentTimeMillis()
            val deltaTimeMillis = currentTime - startTime


            pollEvents()
            runEvents(deltaTimeMillis / 1000.toDouble())
            val start = System.currentTimeMillis()
            updateChunkMeshes()
            runGraphics()
            val end = System.currentTimeMillis()

            if (end - start > 100) {
                println("Frame took a long time: ${end - start}")
            }

//            world.chunkManager.unloadChunks()

            startTime = currentTime

            frameCount++

//            val sleepTime = (targetFrameTime - deltaTimeMillis).coerceAtLeast(0.0)
//            if (sleepTime > 0.0) {
//                Thread.sleep(sleepTime.toLong())
//            }
        }

        val averageFps = frameCount / ((System.currentTimeMillis() - frameCountStartTime).toDouble()) * 1000
        println("Average FPS: $averageFps")

        cleanup()
    }

    private fun pollEvents() {
        window.mouseInput.pollInput()
        window.keyboardInput.pollInput()
        window.pollEvents()
        mainRenderer.camera.pollEvents()
    }

    private fun runEvents(deltaTimeS: Double) {
        eventQueue.runEvents(deltaTimeS)
    }

    private fun runGraphics() {
        mainRenderer.render(window, world)
        window.update()
        world.chunkManager.sendChunksToGPU()
//        GLFW.glfwSwapBuffers(window.handle)
    }

    private fun updateChunkMeshes() {
        for (chunk in world.chunkManager.getLoadedChunks()) {
            if (chunk.changed) {
                chunk.changed = false
                chunk.buildMesh()
            }
        }
    }

    private fun cleanup() {
//        window.cleanup()
        stop()
    }

    private fun resize() {
        mainRenderer.resize(window.width, window.height)
        mainRenderer.render(window, world)
        window.update()
    }
}