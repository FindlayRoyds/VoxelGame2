package client.graphics.input.action

import client.Client
import common.GameEngineProvider
import common.math.Double3

class JumpAction : Action() {
    override val onHold = true

    override fun execute() {
        val client = GameEngineProvider.getGameEngine() as Client
//        val speed = Config.characterFlySpeed * client.eventQueue.deltaTimeS
//        client.renderer.camera.addPosition(Double3(0.0, speed, 0.0))

        val raycastResult = client.world.raycast(client.renderer.camera.position, Double3(0, -1, 0), 1.6)
        if (raycastResult != null) {
            client.renderer.camera.fallSpeed = -0.20
        }
    }
}