package common.event.clientevents

import common.Config
import common.event.ClientEvent
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*

class KeyboardInputEvent: ClientEvent() {
    override fun event() {
        characterMovement()
    }

    fun characterMovement() {
        val input = client!!.window.keyboardInput
        val increment = Vector3f()

        if (input.isKeyPressed(GLFW_KEY_W)) {
            increment.z += Config.characterFlySpeed.toFloat()
        }
        if (input.isKeyPressed(GLFW_KEY_S)) {
            increment.z -= Config.characterFlySpeed.toFloat()
        }

        if (input.isKeyPressed(GLFW_KEY_A)) {
            increment.x += Config.characterFlySpeed.toFloat()
        }
        if (input.isKeyPressed(GLFW_KEY_D)) {
            increment.x -= Config.characterFlySpeed.toFloat()
        }

        if (input.isKeyPressed(GLFW_KEY_SPACE)) {
            increment.y += Config.characterFlySpeed.toFloat()
        }
        if (input.isKeyPressed(GLFW_KEY_LEFT_SHIFT) || input.isKeyPressed(GLFW_KEY_RIGHT_SHIFT)) {
            increment.y -= Config.characterFlySpeed.toFloat()
        }

        client!!.renderer.camera.addPosition(increment)
    }
}