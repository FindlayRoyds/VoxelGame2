package client.graphics

import client.graphics.input.KeyboardInput
import client.graphics.input.MouseInput
import imgui.ImGui
import imgui.gl3.ImGuiImplGl3
import imgui.glfw.ImGuiImplGlfw
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.system.MemoryUtil


class Window(title: String, windowOptions: WindowOptions, private val resizeFunction: () -> Unit) {
    val mouseInput: MouseInput
    val keyboardInput: KeyboardInput
    var handle: Long = -1
    var width: Int = -1
    var height: Int = -1

    private lateinit var glslVersion: String
    private var imGuiGlfw = ImGuiImplGlfw()
    private var imGuiGl3 = ImGuiImplGl3()

    class WindowOptions(
        val compatibleProfile: Boolean,
        val fps: Int,
        val height: Int,
        val width: Int,
    )

    init {
        initGLFW(title, windowOptions)

        mouseInput = MouseInput(this)
        keyboardInput = KeyboardInput(this)

        initImGui()
        imGuiGlfw.init(handle, true)
        imGuiGl3.init()
    }

    fun initImGui() {
        ImGui.createContext()

//        val io = ImGui.getIO()
//        io.fonts.addFontDefault()
//        io.fonts.build()
        ImGui.newFrame()
    }

    fun initGLFW(title: String, windowOptions: WindowOptions) {
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

        glfwMakeContextCurrent(handle);
        GL.createCapabilities()
        glfwSwapInterval(GLFW_TRUE);

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
    }

    fun newFrame() {
        imGuiGlfw.newFrame()
        ImGui.newFrame()
    }

    fun renderImGui() {
        ImGui.render()
        imGuiGl3.renderDrawData(ImGui.getDrawData())
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

//        imGuiGl3.dispose()
//        imGuiGlfw.dispose()
//        ImGui.destroyContext()
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
        renderImGui()
        glfwSwapBuffers(handle)
    }

    fun shouldClose(): Boolean {
        return glfwWindowShouldClose(handle)
    }
}