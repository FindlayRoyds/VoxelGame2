package client.graphics

import client.Client
import common.EventQueue
import common.GameEngineProvider
import common.event.commonevents.DisconnectEvent
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWVidMode
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil

class SceneManager(eventQueue: EventQueue) {
    var window: Long = 0
    private var renderer = Renderer()
    private var client = GameEngineProvider.getGameEngine() as Client

    fun run() {
        initialise()
        renderer.loop(window)

        // Free the window callbacks and destroy the window
        Callbacks.glfwFreeCallbacks(window)
        GLFW.glfwDestroyWindow(window)

        // Terminate GLFW and free the error callback
        GLFW.glfwTerminate()
        GLFW.glfwSetErrorCallback(null)?.free()

        val disconnectEvent = DisconnectEvent()
        client.socketHandler.sendEvent(disconnectEvent)
    }

    private fun initialise() {
        GLFWErrorCallback.createPrint(System.err).set()

        check(GLFW.glfwInit()) { "Unable to initialize GLFW" }

        GLFW.glfwDefaultWindowHints() // optional, the current window hints are already the default
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE) // the window will stay hidden after creation
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE) // the window will be resizable

        window = GLFW.glfwCreateWindow(300, 300, "Hello World!", MemoryUtil.NULL, MemoryUtil.NULL)
        if (window === MemoryUtil.NULL) throw RuntimeException("Failed to create the GLFW window")

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        GLFW.glfwSetKeyCallback(window) { window, key, scancode, action, mods ->
            if (key === GLFW.GLFW_KEY_ESCAPE && action === GLFW.GLFW_RELEASE) GLFW.glfwSetWindowShouldClose(
                window,
                true
            )
        }
        MemoryStack.stackPush().use { stack ->
            val pWidth = stack.mallocInt(1) // int*
            val pHeight = stack.mallocInt(1) // int*

            GLFW.glfwGetWindowSize(window, pWidth, pHeight)

            val vidmode: GLFWVidMode? = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor())

            if (vidmode != null) {
                GLFW.glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth[0]) / 2,
                    (vidmode.height() - pHeight[0]) / 2
                )
            }
        }

        GLFW.glfwMakeContextCurrent(window)
        GLFW.glfwSwapInterval(1) // Enable v-sync

        GLFW.glfwShowWindow(window)
    }
}