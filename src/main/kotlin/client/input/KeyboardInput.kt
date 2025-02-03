package client.input

import client.graphics.Window
import client.input.action.*
import common.block.blocks.Block
import org.lwjgl.glfw.GLFW.*


class KeyboardInput(private val window: Window) {
    private val keybindings = mutableMapOf(
        GLFW_KEY_Z to mutableListOf(DebugAction()),
        GLFW_KEY_SPACE to mutableListOf(JumpAction()),
        GLFW_KEY_LEFT_SHIFT to mutableListOf(CrouchAction()),
        GLFW_KEY_W to mutableListOf(MoveForwardAction()),
        GLFW_KEY_S to mutableListOf(MoveBackAction()),
        GLFW_KEY_A to mutableListOf(MoveLeftAction()),
        GLFW_KEY_D to mutableListOf(MoveRightAction()),
        GLFW_KEY_ESCAPE to mutableListOf(EscapeAction()),
        GLFW_KEY_E to mutableListOf(PlaceAction(Block.stone)),
        GLFW_KEY_F to mutableListOf(PlaceAction(Block.log)),
        GLFW_KEY_Q to mutableListOf(BreakAction()),
        GLFW_KEY_R to mutableListOf(RunAction()),
    )
    private val pressedKeys = mutableSetOf<Int>()

    fun pollInput() {
        for ((key, actions) in keybindings) {
            if (isKeyPressed(key)) {
                for (action in actions)
                    action.run(true)
                if (!pressedKeys.contains(key)) {
                    pressedKeys.add(key)
                    for (action in actions)
                        action.run(false)
                }
            } else if (pressedKeys.contains(key)) {
                pressedKeys.remove(key)
            }
        }
    }

    fun isKeyPressed(keyCode: Int): Boolean {
        return glfwGetKey(window.handle, keyCode) == GLFW_PRESS
    }
}