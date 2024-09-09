package client.graphics.input.action

import client.Client
import common.Config
import common.GameEngineProvider
import common.block.blocks.Air
import common.block.blocks.Block
import common.event.servernetworkevents.SetBlockServerEvent

class PlaceAction(val block: Block): Action() {
    override val onHold = false

    override fun execute() {
        val client = GameEngineProvider.getGameEngine() as Client
        val camera = client.mainRenderer.camera
        val raycastResult = client.world.raycast(camera.position, camera.lookVector, Config.characterReachDistance)

        if (raycastResult != null) {
            val (_, placePosition) = raycastResult
            val existingBlock = client.world.chunkManager.getBlock(placePosition)
            if (existingBlock !is Air)
                return

            client.world.chunkManager.setBlock(placePosition, block)

            val setBlockEvent = SetBlockServerEvent(placePosition, block)
            client.socketHandler.sendEvent(setBlockEvent)
        }
    }
}