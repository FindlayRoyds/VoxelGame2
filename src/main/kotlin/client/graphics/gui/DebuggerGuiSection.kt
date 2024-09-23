package client.graphics.gui

import client.Client
import client.graphics.Window
import common.Debugger
import common.GameEngineProvider
import imgui.ImGui
import imgui.flag.ImGuiCond
import imgui.flag.ImGuiWindowFlags

class DebuggerGuiSection : GuiSection {
    override fun declareGui(window: Window) {
        if (!Debugger.menuVisible)
            return

        ImGui.setNextWindowPos(0f, 0f, ImGuiCond.Always)
        ImGui.setNextWindowSize(window.width.toFloat(), window.height.toFloat(), ImGuiCond.Always)

        val windowFlags = ImGuiWindowFlags.NoDecoration or ImGuiWindowFlags.AlwaysAutoResize or ImGuiWindowFlags.NoBackground
        ImGui.begin("HUD", windowFlags)

        ImGui.text("fps: ${Debugger.fps}")
        ImGui.text("position: ${Debugger.position}")
        ImGui.text("chunk: ${Debugger.chunk}")

        if (ImGui.button("Toggle wireframe")) {
            val client = GameEngineProvider.getGameEngine() as Client
            client.mainRenderer.toggleWireframe()
        }

        ImGui.end()
    }
}