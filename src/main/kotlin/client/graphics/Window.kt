package client.graphics

import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.system.MemoryUtil
import org.pmw.tinylog.Logger


class Window(title: String, var windowOptions: WindowOptions, private val resizeFunction: () -> Unit) {
    val windowHandle: Long
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

        windowHandle = glfwCreateWindow(windowOptions.width, windowOptions.height, "Hello World!", MemoryUtil.NULL, MemoryUtil.NULL)
        if (windowHandle == MemoryUtil.NULL) {
            throw RuntimeException("Failed to create the GLFW window")
        }

        glfwSetFramebufferSizeCallback(
            windowHandle
        ) { window: Long, w: Int, h: Int -> resized(w, h) }

        glfwSetErrorCallback { errorCode: Int, msgPtr: Long ->
            Logger.error(
                "Error code [{}], msg [{}]", errorCode, MemoryUtil.memUTF8(msgPtr)
            )
        }

        glfwSetKeyCallback(windowHandle) { _: Long, key: Int, scancode: Int, action: Int, mods: Int ->
            keyCallBack(key, action)
        }

        println("Making context current")
        glfwMakeContextCurrent(windowHandle);

        if (windowOptions.fps > 0) {
            glfwSwapInterval(0)
        } else {
            glfwSwapInterval(1)
        }

        glfwShowWindow(windowHandle)

        val arrayWidth = IntArray(1)
        val arrayHeight = IntArray(1)
        glfwGetFramebufferSize(windowHandle, arrayWidth, arrayHeight)
        width = arrayWidth[0]
        height = arrayHeight[0]
    }

    private fun keyCallBack(key: Int, action: Int) {
        if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
            glfwSetWindowShouldClose(windowHandle, true) // We will detect this in the rendering loop
        }
    }

    fun cleanup() {
        glfwFreeCallbacks(windowHandle)
        glfwDestroyWindow(windowHandle)
        glfwTerminate()
        glfwSetErrorCallback(null)?.free()
    }

    fun isKeyPressed(keyCode: Int): Boolean {
        return glfwGetKey(windowHandle, keyCode) == GLFW_PRESS
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
            Logger.error("Error calling resize callback", exception)
        }
    }

    fun update() {
        glfwSwapBuffers(windowHandle)

        // GL.createCapabilities()

        // glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

//        while (!glfwWindowShouldClose(windowHandle)) {
//            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
//            glfwSwapBuffers(windowHandle)
//
//            glfwPollEvents()
//        }
    }

    fun shouldClose(): Boolean {
        return glfwWindowShouldClose(windowHandle)
    }
}