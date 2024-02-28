package common.event.clientevents

import common.Config
import common.event.ClientEvent
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL41.glPolygonMode

class KeyboardInputEvent: ClientEvent() {
    override fun event() {
        characterMovement()
        wireframeToggle()
    }

    fun characterMovement() {
        val input = client!!.window.keyboardInput
        val moveDirection = Vector3f()

        if (input.isKeyPressed(GLFW_KEY_W)) {
            moveDirection.z += 1
        }
        if (input.isKeyPressed(GLFW_KEY_S)) {
            moveDirection.z -= 1
        }

        if (input.isKeyPressed(GLFW_KEY_A)) {
            moveDirection.x += 1
        }
        if (input.isKeyPressed(GLFW_KEY_D)) {
            moveDirection.x -= 1
        }

        if (input.isKeyPressed(GLFW_KEY_SPACE)) {
            moveDirection.y += 1
        }
        if (input.isKeyPressed(GLFW_KEY_LEFT_SHIFT) || input.isKeyPressed(GLFW_KEY_RIGHT_SHIFT)) {
            moveDirection.y -= 1
        }

        val speed = Config.characterFlySpeed * client!!.eventQueue.deltaTimeS
        client!!.renderer.camera.addPosition(moveDirection.mul(speed.toFloat()))
    }

    fun wireframeToggle() {
        val input = client!!.window.keyboardInput

        if (input.isKeyPressed(GLFW_KEY_Z)) {
            client!!.renderer.toggleWireframe();
        }
    }
}