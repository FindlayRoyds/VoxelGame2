package client.graphics

import client.graphics.input.KeyboardInput
import client.graphics.input.MouseInput
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.system.MemoryUtil


class Window(title: String, windowOptions: WindowOptions, private val resizeFunction: () -> Unit) {
    val mouseInput: MouseInput
    val keyboardInput: KeyboardInput
    val handle: Long
    var width: Int
    var height: Int

    class WindowOptions(
        val compatibleProfile: Boolean,
        val fps: Int,
        val height: Int,
        val width: Int,
    )

    init {
        GLFWErrorCallback.createPrint(System.err).set()
        check(glfwInit()) { "Unable to initialize GLFW" }

        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)

        glfwWindowHint(GLFW_SAMPLES, 4)

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1)
        if (windowOptions.compatibleProfile) {
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_COMPAT_PROFILE)
        } else {
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE)
        }

        if (windowOptions.width > 0 && windowOptions.height > 0) {
            width = windowOptions.width
            height = windowOptions.height
        } else {
            glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE)
            val vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor())!!
            width = vidMode.width()
            height = vidMode.height()
        }

        handle = glfwCreateWindow(windowOptions.width, windowOptions.height, title, MemoryUtil.NULL, MemoryUtil.NULL)
        if (handle == MemoryUtil.NULL) {
            throw RuntimeException("Failed to create the GLFW window")
        }

        glfwSetFramebufferSizeCallback(
            handle
        ) { _: Long, w: Int, h: Int -> resized(w, h) }

        glfwSetErrorCallback { errorCode: Int, msgPtr: Long ->
            throw RuntimeException(
                "Error code [{}], msg [{}], $errorCode, ${MemoryUtil.memUTF8(msgPtr)}"
            )
        }

        glfwSetWindowCloseCallback(handle) {
            windowCloseCallBack() // Doesn't work :(
        }

        println("Making context current")
        glfwMakeContextCurrent(handle);

        if (windowOptions.fps > 0) {
            glfwSwapInterval(0)
        } else {
            glfwSwapInterval(1)
        }

        glfwShowWindow(handle)

        val arrayWidth = IntArray(1)
        val arrayHeight = IntArray(1)
        glfwGetFramebufferSize(handle, arrayWidth, arrayHeight)
        width = arrayWidth[0]
        height = arrayHeight[0]

        mouseInput = MouseInput(this)
        keyboardInput = KeyboardInput(this)
    }

    private fun windowCloseCallBack() {
        glfwSetWindowShouldClose(handle, true)
    }

    fun cleanup() {
        mouseInput.cleanup()
        glfwFreeCallbacks(handle)
        glfwDestroyWindow(handle)
        glfwTerminate()
        glfwSetErrorCallback(null)?.free()
    }

    fun pollEvents() {
        glfwPollEvents()
    }

    private fun resized(newWidth: Int, newHeight: Int) {
        width = newWidth
        height = newHeight
        try {
            resizeFunction()
        } catch (exception: Exception) {
            throw RuntimeException("Error calling resize callback", exception)
        }
    }

    fun update() {
        glfwSwapBuffers(handle)
    }

    fun shouldClose(): Boolean {
        return glfwWindowShouldClose(handle)
    }
}