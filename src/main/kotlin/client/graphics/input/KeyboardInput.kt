package client.graphics.input

import client.Client
import client.graphics.Window
import common.GameEngineProvider
import common.event.clientevents.KeyboardInputEvent
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.*


class KeyboardInput(private val window: Window) {
    // val previousKeyPresses =

    fun pollInput() {
        if (isAnyKeyPressed()) {
            val client = GameEngineProvider.getGameEngine() as Client
            client.eventQueue.addEvent(KeyboardInputEvent())
        }
    }

    fun keyCallBack(key: Int, action: Int) {
        if (action == GLFW_PRESS) {
            // println("pressed")
        } else if (action == GLFW_RELEASE) {
            // println("released")
        }
    }


    fun isKeyPressed(keyCode: Int): Boolean {
        return GLFW.glfwGetKey(window.handle, keyCode) == GLFW.GLFW_PRESS
    }

    fun isAnyKeyPressed(): Boolean {
        for (i in GLFW.GLFW_KEY_SPACE..GLFW.GLFW_KEY_LAST) {
            if (glfwGetKey(window.handle, i) == GLFW_PRESS) {
                return true
            }
        }
        return false
    }
}