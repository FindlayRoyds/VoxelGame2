package server

import common.Config
import common.GameEngine
import common.GameEngineProvider
import common.event.clientevents.ReceiveChunkClientEvent
import common.math.Int3
import server.networking.ServerNetwork


class Server(port: Int) : GameEngine() {
    var serverNetwork: ServerNetwork
    val pointsInCircle: List<Int3> = pointsInCircle(Config.renderDistance)

    init {
        GameEngineProvider.setGameEngine(this)
        println("Server starting...")

        serverNetwork = ServerNetwork(port, eventQueue)
        this.main()
    }

    override fun isServer(): Boolean {
        return true
    }

    override fun isClient(): Boolean {
        return false
    }

    fun pointsInCircle(radius: Int): List<Int3> {
        val points = mutableListOf<Int3>()

        // Iterate over the bounding box of the circle
        for (x in -radius..radius) {
            for (y in -radius..radius) {
                for (z in -radius..radius) {
                    // Check if the point (x, y) is within the circle
                    if (x*x + y*y + z*z <= radius * radius) {
                        points.add(Int3(x, y, z))
                    }
                }
            }
        }

        // Sort points by distance from (0, 0)
        points.sortBy { it.x*it.x + it.y*it.y + it.z*it.z }

        return points
    }

    private fun updateChunks() {
        world.tick += 1

        for (player in players.getPlayerList()) {
            val playerChunkPosition = world.chunkManager.worldPositionToChunkPosition(player.position)
            var count = 0
            for (chunkOffset in pointsInCircle) {
//                if (count >= 20) return

                val chunkPosition = playerChunkPosition + chunkOffset + Int3(0, -1, 0)
                if (world.chunkManager.isChunkLoaded(chunkPosition)) {
                    val chunk = world.chunkManager.getChunk(chunkPosition)!!
                    if (!player.loadedChunks.contains(chunkPosition)) {
                        val event = ReceiveChunkClientEvent(chunk.getTransferObject())
                        serverNetwork.sendEventToPlayer(event, player)
                        player.loadedChunks.add(chunkPosition)
                        count++
                        // println(player.loadedChunks.size)
                    }
                } else {
                    world.chunkManager.generateChunk(chunkPosition)
                }
            }
        }

//        world.chunkManager.unloadChunks()
//        println(world.chunkManager.getLoadedChunks().size)
    }

    private fun main() {
        var startTime = System.currentTimeMillis()
        var delayTime = 0L

        world.chunkManager.chunkGenerationExecutor.run()

        while (true) {
            updateChunks()
            eventQueue.runEvents(delayTime.toDouble())

            delayTime = Config.tickTime - (System.currentTimeMillis() - startTime)
            if (delayTime > 0)
                Thread.sleep(delayTime)
            startTime = System.currentTimeMillis()
        }
    }
}