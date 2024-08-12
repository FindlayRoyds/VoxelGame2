package client.graphics.input.action

import client.Client
import common.Config
import common.GameEngineProvider
import common.block.blocks.Air
import common.block.blocks.Stone
import common.event.commonevents.SetBlockServerEvent

class PlaceAction: Action() {
    override val onHold = false

    override fun execute() {
        val client = GameEngineProvider.getGameEngine() as Client
        val camera = client.renderer.camera
        val raycastResult = client.world.raycast(camera.position, camera.lookVector, Config.characterReachDistance)

        if (raycastResult != null) {
            val (_, placePosition) = raycastResult
            val existingBlock = client.world.chunkManager.getBlock(placePosition)
            if (existingBlock !is Air)
                return

            client.world.chunkManager.setBlock(placePosition, Stone())

            val setBlockEvent = SetBlockServerEvent(placePosition, Stone())
            client.socketHandler.sendEvent(setBlockEvent)
        }
    }
}