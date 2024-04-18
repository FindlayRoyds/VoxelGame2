package client.graphics.input.action

import client.Client
import common.GameEngineProvider

class EscapeAction: Action() {
    override val onHold = true

    override fun execute() {
        val client = GameEngineProvider.getGameEngine() as Client
        // GLFW.glfwSetWindowShouldClose(client.window.handle, true)
        client.window.mouseInput.unFocusWindow()
    }
}