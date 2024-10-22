package client.graphics

import imgui.ImGui
import imgui.app.Configuration
import imgui.app.Window

class Window2 : Window() {
    init {
        preRun()
        run()
        postRun()
        dispose()

        val config = Configuration()
        configure(config)
        init(config)
    }

    protected fun configure(config: Configuration) {
        config.title = "Dear ImGui is Awesome!";
    }

    protected fun preRun() {}

    protected fun postRun() {}

    override fun process() {
        ImGui.text("Hello, World!");
    }
}