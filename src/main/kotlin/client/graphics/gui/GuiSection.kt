package client.graphics.gui

import client.graphics.Window
import imgui.ImGui
import imgui.flag.ImGuiCond
import imgui.flag.ImGuiWindowFlags

abstract class GuiSection(val isFullscreen: Boolean) {
    abstract val title: String

    init {

    }

    protected abstract fun declareGui(window: Window)

    fun display(window: Window) {
        if (isFullscreen) {
            ImGui.setNextWindowPos(0f, 0f, ImGuiCond.Always)
            ImGui.setNextWindowSize(window.width.toFloat(), window.height.toFloat(), ImGuiCond.Always)

            val windowFlags = ImGuiWindowFlags.NoDecoration or ImGuiWindowFlags.AlwaysAutoResize or ImGuiWindowFlags.NoBackground
            ImGui.begin(title, windowFlags)
        }

        declareGui(window)

        if (isFullscreen)
            ImGui.end()
    }
}