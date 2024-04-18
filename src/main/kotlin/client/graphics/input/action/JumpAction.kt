package client.graphics.input.action

import client.Client
import common.Config
import common.GameEngineProvider
import common.math.Double3

class JumpAction : Action() {
    override val onHold = true

    override fun execute() {
        val client = GameEngineProvider.getGameEngine() as Client
        val speed = Config.characterFlySpeed * client.eventQueue.deltaTimeS
        client.renderer.camera.addPosition(Double3(0.0, speed, 0.0))
    }
}