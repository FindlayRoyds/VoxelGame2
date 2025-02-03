package client.input.action

import client.Client
import common.Config
import common.GameEngineProvider
import common.block.blocks.Block
import common.event.servernetworkevents.SetBlockServerEvent

class PlaceAction(val block: Block): Action() {
    override val onHold = false

    override fun execute() {
        val client = GameEngineProvider.gameEngine as Client
        val camera = client.mainRenderer.camera
        val raycastResult = client.world.raycast(camera.position, camera.lookVector * Config.characterReachDistance, true)

        if (raycastResult != null) {
            val (_, placePosition) = raycastResult
            val existingBlock = client.world.chunkManager.getBlock(placePosition) ?: return
            if (existingBlock.isSolid)
                return

            client.world.chunkManager.setBlock(placePosition, block)

            val setBlockEvent = SetBlockServerEvent(placePosition, block)
            client.socketHandler.sendEvent(setBlockEvent)
        }
    }
}