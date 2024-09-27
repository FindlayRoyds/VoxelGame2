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
        ImGui.text("position: ${Debugger.position.displayString}")
        ImGui.text("chunk: ${Debugger.chunk.displayString}")
        ImGui.text("average chunk meshing time: ${"%.${2}f".format(Debugger.averageChunkMeshingTimeNano / 1_000_000.0)}ms")
        ImGui.text("average chunk generation time: ${"%.${2}f".format(Debugger.averageChunkGenerationTimeNano / 1_000_000.0)}ms")

        if (ImGui.button("Toggle wireframe")) {
            val client = GameEngineProvider.getGameEngine() as Client
            client.mainRenderer.toggleWireframe()
        }

        ImGui.end()
    }
}