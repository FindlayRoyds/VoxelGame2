package client.input.action

import client.Client
import common.Config
import common.GameEngineProvider
import common.block.blocks.Block
import common.event.servernetworkevents.SetBlockServerEvent

class BreakAction: Action() {
    override val onHold = false

    override fun execute() {
        val client = GameEngineProvider.gameEngine as Client
        val camera = client.mainRenderer.camera
        val raycastResult = client.world.raycast(camera.position, camera.lookVector * Config.characterReachDistance, true)

        if (raycastResult != null) {
            val (breakPosition, _) = raycastResult
            client.world.chunkManager.setBlock(breakPosition, Block.air)

            val setBlockEvent = SetBlockServerEvent(breakPosition, Block.air)
            client.socketHandler.sendEvent(setBlockEvent)
        }
    }

}