package client.graphics.input.action

import client.Client
import common.GameEngineProvider

class WireframeAction: Action() {
    override val onHold = false

    override fun execute() {
        val client = GameEngineProvider.getGameEngine() as Client
        client.mainRenderer.toggleWireframe()

        // print whatever debugging stuff
        val camera = client.mainRenderer.camera
        println("camera position: ${camera.position}")
        println("block: ${camera.position.toInt3()}")
        println("camera look: ${camera.lookVector}")
        println("selection box position: ${client.mainRenderer.selectionBoxPosition}")
    }
}