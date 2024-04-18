package client.graphics.input

import client.Client
import client.graphics.Window
import common.GameEngineProvider
import common.event.clientevents.MouseMovedEvent
import common.math.Double2
import org.lwjgl.glfw.GLFW.*


class MouseInput(private val window: Window) {
    val currentPos = Double2(0, 0)
    val previousPos = Double2(0, 0)
    val displacement = Double2(0, 0)
    var windowFocused = true // glfwGetWindowAttrib(window.handle, GLFW_FOCUSED) == GLFW_TRUE
    var leftButtonPressed = false
    var rightButtonPressed = false

    init {
        glfwSetCursorPos(window.handle, window.width / 2.0, window.height / 2.0)
        glfwSetInputMode(window.handle, GLFW_CURSOR, GLFW_CURSOR_DISABLED)
        resetCursorTracking()

        glfwSetWindowFocusCallback(window.handle) { _: Long, focused: Boolean ->
            windowFocused = focused
            resetCursorTracking()
        }

        glfwSetCursorPosCallback(window.handle) { _: Long, xpos: Double, ypos: Double ->
            currentPos.x = xpos
            currentPos.y = ypos
        }

        glfwSetMouseButtonCallback(window.handle) { _: Long, button: Int, action: Int, _: Int ->
            leftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS
            rightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS

            if (!windowFocused) {
                focusWindow()
            }
        }
    }

    fun pollInput() {
        if (windowFocused) {
            displacement.y = currentPos.x - previousPos.x
            displacement.x = currentPos.y - previousPos.y

            if (displacement.x != 0.0 || displacement.y != 0.0) {
                val mouseMovedEvent = MouseMovedEvent(displacement)
                val client = GameEngineProvider.getGameEngine() as Client
                client.eventQueue.addEvent(mouseMovedEvent)
            }

            previousPos.set(currentPos)
        }
    }

    fun focusWindow() {
        glfwSetCursorPos(window.handle, window.width / 2.0, window.height / 2.0)
        glfwSetInputMode(window.handle, GLFW_CURSOR, GLFW_CURSOR_DISABLED)
        resetCursorTracking()
        windowFocused = true
    }

    fun unFocusWindow() {
        // glfwSetCursorPos(window.handle, window.width / 2.0, window.height / 2.0)
        glfwSetInputMode(window.handle, GLFW_CURSOR, GLFW_CURSOR_NORMAL)
        // resetCursorTracking()
        windowFocused = false
    }

    private fun resetCursorTracking() {
        val xPos = DoubleArray(1)
        val yPos = DoubleArray(1)
        glfwGetCursorPos(window.handle, xPos, yPos)
        previousPos.set(xPos[0], yPos[0])
        currentPos.set(xPos[0], yPos[0])
    }

    fun cleanup() {
        glfwSetInputMode(window.handle, GLFW_CURSOR, GLFW_CURSOR_NORMAL)
    }
}