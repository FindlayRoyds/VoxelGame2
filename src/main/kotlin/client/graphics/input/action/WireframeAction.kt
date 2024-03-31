package client.graphics.input.action

import client.Client
import common.GameEngineProvider

class WireframeAction: Action() {
    override val onHold = false

    override fun execute() {
        val client = GameEngineProvider.getGameEngine() as Client
        client.renderer.toggleWireframe()
    }
}