package client.graphics.input.action

import client.Client
import common.GameEngineProvider
import common.math.Double3

class JumpAction : Action() {
    override val onHold = true

    override fun execute() {
        val client = GameEngineProvider.gameEngine as Client
//        val speed = Config.characterFlySpeed * client.eventQueue.deltaTimeS
//        client.mainRenderer.camera.addPosition(Double3(0.0, speed, 0.0))

        val raycastResult = client.world.raycast(client.mainRenderer.camera.position, Double3(0, -1.6, 0), false)
        if (raycastResult != null) {
            client.mainRenderer.camera.fallSpeed = -0.065
        }
    }
}