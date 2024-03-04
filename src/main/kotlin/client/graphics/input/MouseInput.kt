package client.graphics.input

import client.Client
import client.graphics.Window
import common.GameEngineProvider
import common.event.clientevents.MouseMovedEvent
import common.math.Float2
import org.lwjgl.glfw.GLFW.*


class MouseInput(private val window: Window) {
    val currentPos = Float2(0f, 0f)
    val previousPos = Float2(0f, 0f)
    val displacement = Float2(0f, 0f)
    var windowFocused = false // glfwGetWindowAttrib(window.handle, GLFW_FOCUSED) == GLFW_TRUE
    var leftButtonPressed = false
    var rightButtonPressed = false

    init {
        glfwSetCursorPos(window.handle, window.width / 2.0, window.height / 2.0)
        glfwSetInputMode(window.handle, GLFW_CURSOR, GLFW_CURSOR_DISABLED)
        resetCursorTracking()

        glfwSetWindowFocusCallback(window.handle) { _: Long, focused: Boolean ->
            windowFocused = focused
            println(focused)
            resetCursorTracking()
        }

        glfwSetCursorPosCallback(window.handle) { _: Long, xpos: Double, ypos: Double ->
            currentPos.x = xpos.toFloat()
            currentPos.y = ypos.toFloat()
        }

        glfwSetMouseButtonCallback(window.handle) { _: Long, button: Int, action: Int, _: Int ->
            leftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS
            rightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS
        }
    }

    fun pollInput() {
        if (windowFocused) {
            displacement.y = currentPos.x - previousPos.x
            displacement.x = currentPos.y - previousPos.y

            if (displacement.x != 0f || displacement.y != 0f) {
                val mouseMovedEvent = MouseMovedEvent(displacement)
                val client = GameEngineProvider.getGameEngine() as Client
                client.eventQueue.addEvent(mouseMovedEvent)
            }

            previousPos.set(currentPos)
        }
    }

    private fun resetCursorTracking() {
        val xPos = DoubleArray(1)
        val yPos = DoubleArray(1)
        glfwGetCursorPos(window.handle, xPos, yPos)
        previousPos.set(xPos[0].toFloat(), yPos[0].toFloat())
        currentPos.set(xPos[0].toFloat(), yPos[0].toFloat())
    }

    fun cleanup() {
        glfwSetInputMode(window.handle, GLFW_CURSOR, GLFW_CURSOR_NORMAL)
    }
}