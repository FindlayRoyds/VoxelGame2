package client.graphics.input.action

import client.Client
import common.Config
import common.GameEngineProvider
import common.event.commonevents.SetBlockEvent

class BreakAction: Action() {
    override val onHold = false

    override fun execute() {
        val client = GameEngineProvider.getGameEngine() as Client
        val camera = client.renderer.camera
        val raycastResult = client.world.raycast(camera.position, camera.lookVector, Config.characterReachDistance)

        if (raycastResult != null) {
            val (breakPosition, _) = raycastResult
            client.world.chunkManager.setBlock(breakPosition, 0.toByte())

            val setBlockEvent = SetBlockEvent(breakPosition, 0.toByte())
            client.socketHandler.sendEvent(setBlockEvent)
        }
    }

}