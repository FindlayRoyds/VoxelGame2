package client.graphics.input.action

import client.Client
import common.Config
import common.GameEngineProvider
import common.event.commonevents.SetBlockEvent

class PlaceAction: Action() {
    override val onHold = false

    override fun execute() {
        val client = GameEngineProvider.getGameEngine() as Client
        val camera = client.renderer.camera
        val raycastResult = client.world.raycast(camera.position, camera.lookVector, Config.characterReachDistance)

        if (raycastResult != null) {
            val (_, placePosition) = raycastResult
            val existingBlock = client.world.chunkManager.getBlock(placePosition)
            if (existingBlock != 0.toByte())
                return

            client.world.chunkManager.setBlock(placePosition, 2.toByte())

            val setBlockEvent = SetBlockEvent(placePosition, 2.toByte())
            client.socketHandler.sendEvent(setBlockEvent)
        }
    }
}