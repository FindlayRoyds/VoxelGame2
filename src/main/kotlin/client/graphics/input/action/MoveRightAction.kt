package client.graphics.input.action

import client.Client
import common.Config
import common.GameEngineProvider
import common.math.Double3

class MoveRightAction: Action() {
    override val onHold = true

    override fun execute() {
        val client = GameEngineProvider.gameEngine as Client
        val speed = Config.characterFlySpeed * client.eventQueue.deltaTimeS
        client.mainRenderer.camera.addPosition(Double3(-speed, 0.0, 0.0))
    }
}