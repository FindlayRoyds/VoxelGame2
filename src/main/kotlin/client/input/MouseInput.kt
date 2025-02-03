package client.input

import client.Client
import client.graphics.Window
import common.Config
import common.Debugger
import common.GameEngineProvider
import common.math.Double2
import org.lwjgl.glfw.GLFW.*


class MouseInput(private val window: Window) {
    var currentPos = Double2(0, 0)
    var previousPos = Double2(0, 0)
    var displacement = Double2(0, 0)
    var windowFocused = false // glfwGetWindowAttrib(window.handle, GLFW_FOCUSED) == GLFW_TRUE
    var leftButtonPressed = false
    var rightButtonPressed = false

    private var _client: Client? = null
    private val client: Client
        get() {
            if (_client == null)
                _client = GameEngineProvider.gameEngine as Client
            return _client!!
        }

    init {
        // glfwSetCursorPos(window.handle, window.width / 2.0, window.height / 2.0)
        // glfwSetInputMode(window.handle, GLFW_CURSOR, GLFW_CURSOR_DISABLED)
        // resetCursorTracking()
        var windowStartupDebounce = true

        glfwSetWindowFocusCallback(window.handle) { _: Long, focused: Boolean ->
//            if (!windowStartupDebounce) {
//                windowFocused = focused
//            }
            windowStartupDebounce = false
            resetCursorTracking()
//            focusWindow()
        }

        glfwSetCursorPosCallback(window.handle) { _: Long, xpos: Double, ypos: Double ->
            currentPos = Double2(xpos, ypos)
        }

        glfwSetMouseButtonCallback(window.handle) { _: Long, button: Int, action: Int, _: Int ->
            leftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS
            rightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS

            if (!windowFocused && !Debugger.menuVisible) {
                focusWindow()
            }
        }
    }

    fun pollInput() {
        if (windowFocused) {
            displacement = Double2(currentPos.y - previousPos.y, currentPos.x - previousPos.x)

            if (displacement.x != 0.0 || displacement.y != 0.0) {
//                val mouseMovedEvent = MouseMovedEvent(displacement)
//                val client = GameEngineProvider.gameEngine as Client
//                client.eventQueue.addEvent(mouseMovedEvent)

                client.mainRenderer.camera.addRotation(displacement * Config.mouseSensitivity)
            }

            previousPos = currentPos
        }
    }

    fun focusWindow() {
        glfwSetCursorPos(window.handle, window.width / 2.0, window.height / 2.0)
        glfwSetInputMode(window.handle, GLFW_CURSOR, GLFW_CURSOR_DISABLED)
        resetCursorTracking()
        windowFocused = true
    }

    fun unFocusWindow() {
        glfwSetInputMode(window.handle, GLFW_CURSOR, GLFW_CURSOR_NORMAL)
        // resetCursorTracking()
        windowFocused = false
        glfwSetCursorPos(window.handle, window.width / 2.0, window.height / 2.0)
    }

    private fun resetCursorTracking() {
        val xPos = DoubleArray(1)
        val yPos = DoubleArray(1)
        glfwGetCursorPos(window.handle, xPos, yPos)
        previousPos = Double2(xPos[0], yPos[0])
        currentPos = Double2(xPos[0], yPos[0])
    }

    fun cleanup() {
        glfwSetInputMode(window.handle, GLFW_CURSOR, GLFW_CURSOR_NORMAL)
    }
}