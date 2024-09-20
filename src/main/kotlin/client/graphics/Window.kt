package client.graphics

import client.graphics.input.KeyboardInput
import client.graphics.input.MouseInput
import imgui.ImGui
import imgui.app.Color
import imgui.app.Configuration
import imgui.flag.ImGuiConfigFlags
import imgui.gl3.ImGuiImplGl3
import imgui.glfw.ImGuiImplGlfw
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.GL_DEPTH_TEST
import org.lwjgl.opengl.GL11.glDisable
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import java.util.*


class Window(windowOptions: Window.WindowOptions, private val resizeFunction: () -> Unit) {
    val mouseInput: MouseInput
    val keyboardInput: KeyboardInput
    var width: Int = -1
    var height: Int = -1

    val colorBg: Color = Color(.5f, .5f, .5f, 1f)
    var imGuiGlfw = ImGuiImplGlfw()
    var imGuiGl3 = ImGuiImplGl3()
    private var glslVersion: String? = null

    var handle: Long = 0

    class WindowOptions(
        val compatibleProfile: Boolean,
        val title: String,
        val fps: Int,
        val height: Int,
        val width: Int,
        val fullscreen: Boolean
    )

    init {
        initWindow(windowOptions)
        initImGui(windowOptions)
        imGuiGlfw.init(handle, true)
        imGuiGl3.init(glslVersion)

        mouseInput = MouseInput(this)
        keyboardInput = KeyboardInput(this)

        preRun()
        run()
//        postRun()

//        dispose()

    }

    fun configure(config: Configuration) {
        config.title = "Dear ImGui is Awesome!";
    }

    fun preRun() {}

    fun postRun() {}

    fun process() {
        ImGui.text("Hello, World!");
    }

//    fun dispose() {
//        imGuiGl3.shutdown()
//        imGuiGlfw.shutdown()
//        disposeImGui()
//        disposeWindow()
//    }

    fun initWindow(windowOptions: WindowOptions) {
        GLFWErrorCallback.createPrint(System.err).set()
        check(GLFW.glfwInit()) { "Unable to initialize GLFW" }
        decideGlGlslVersions()

        GLFW.glfwDefaultWindowHints()
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE)
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE)
        GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, 4)
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4)
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 1)
        if (windowOptions.compatibleProfile) {
            GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_COMPAT_PROFILE)
        } else {
            GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE)
            GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE)
        }
        if (windowOptions.width > 0 && windowOptions.height > 0) {
            width = windowOptions.width
            height = windowOptions.height
//        } else {
//            GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE)
//            val vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor())!!
//            width = vidMode.width()
//            height = vidMode.height()
        }

        handle = GLFW.glfwCreateWindow(windowOptions.width, windowOptions.height, windowOptions.title, MemoryUtil.NULL, MemoryUtil.NULL)
        if (handle == MemoryUtil.NULL) {
            throw RuntimeException("Failed to create the GLFW window")
        }

        MemoryStack.stackPush().use { stack ->
            val pWidth = stack.mallocInt(1) // int*
            val pHeight = stack.mallocInt(1) // int*
            GLFW.glfwGetWindowSize(handle, pWidth, pHeight)
            val vidmode =
                Objects.requireNonNull(GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor()))
            GLFW.glfwSetWindowPos(
                handle,
                (vidmode!!.width() - pWidth[0]) / 2,
                (vidmode.height() - pHeight[0]) / 2
            )
        }

        GLFW.glfwSetFramebufferSizeCallback(
            handle
        ) { _: Long, w: Int, h: Int -> resized(w, h) }

        GLFW.glfwSetErrorCallback { errorCode: Int, msgPtr: Long ->
            throw RuntimeException(
                "Error code [{}], msg [{}], $errorCode, ${MemoryUtil.memUTF8(msgPtr)}"
            )
        }

        GLFW.glfwSetWindowCloseCallback(handle) {
            windowCloseCallBack() // Doesn't work :(
        }

        GLFW.glfwMakeContextCurrent(handle)
        val currentThread = Thread.currentThread()
        println("Current thread: ${currentThread.name}")
        println("Made context current")
        println(GLFW.glfwGetCurrentContext())
        GL.createCapabilities()
        GLFW.glfwSwapInterval(GLFW.GLFW_TRUE)
        if (windowOptions.fullscreen) {
            GLFW.glfwMaximizeWindow(handle)
        } else {
            GLFW.glfwShowWindow(handle)
        }
        clearBuffer()
        renderBuffer()
//        GLFW.glfwSetWindowSizeCallback(handle, object : GLFWWindowSizeCallback() {
//            override fun invoke(window: Long, width: Int, height: Int) {
//                runFrame()
//            }
//        })
    }

    private fun decideGlGlslVersions() {
        val isMac = System.getProperty("os.name").lowercase(Locale.getDefault()).contains("mac")
        if (isMac) {
            glslVersion = "#version 150"
            GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3)
            GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2)
            GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE) // 3.2+ only
            GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE) // Required on Mac
        } else {
            glslVersion = "#version 130"
            GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3)
            GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 0)
        }
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

    private fun windowCloseCallBack() {
        GLFW.glfwSetWindowShouldClose(handle, true)
    }

    fun initImGui(config: WindowOptions) {
        ImGui.createContext()
    }

    fun preProcess() {}

    fun postProcess() {}

    fun run() {
//        while (!GLFW.glfwWindowShouldClose(handle)) {
//            runFrame()
//        }
    }

    fun pollEvents() {
        GLFW.glfwPollEvents()
    }

    fun runFrame() {
        startFrame()
        preProcess()
        process()
        postProcess()
        endFrame()
    }

    fun update() {
//        update()
//        renderImGui()
        glDisable(GL_DEPTH_TEST)
        runFrame()
        GLFW.glfwSwapBuffers(handle)
    }

    private fun clearBuffer() {
//        GL32.glClearColor(colorBg.red, colorBg.green, colorBg.blue, colorBg.alpha)
//        GL32.glClear(GL32.GL_COLOR_BUFFER_BIT or GL32.GL_DEPTH_BUFFER_BIT)
    }

    fun startFrame() {
        clearBuffer()
        imGuiGl3.newFrame()
        imGuiGlfw.newFrame()
        ImGui.newFrame()
    }

    fun endFrame() {
        ImGui.render()
        imGuiGl3.renderDrawData(ImGui.getDrawData())

        // Update and Render additional Platform Windows
        // (Platform functions may change the current OpenGL context, so we save/restore it to make it easier to paste this code elsewhere.
        //  For this specific demo app we could also call glfwMakeContextCurrent(window) directly)
        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            val backupCurrentContext = GLFW.glfwGetCurrentContext()
            ImGui.updatePlatformWindows()
            ImGui.renderPlatformWindowsDefault()
            GLFW.glfwMakeContextCurrent(backupCurrentContext)
        }
        renderBuffer()
    }

    private fun renderBuffer() {
        GLFW.glfwSwapBuffers(handle)
        GLFW.glfwPollEvents()
    }

    fun disposeImGui() {
        ImGui.destroyContext()
    }

    fun disposeWindow() {
        Callbacks.glfwFreeCallbacks(handle)
        GLFW.glfwDestroyWindow(handle)
        GLFW.glfwTerminate()
        Objects.requireNonNull(GLFW.glfwSetErrorCallback(null))?.free()
    }
}