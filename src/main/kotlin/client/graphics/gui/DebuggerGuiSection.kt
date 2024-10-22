package client.graphics.gui

import client.Client
import client.graphics.Window
import common.Debugger
import common.GameEngineProvider
import imgui.ImGui

class DebuggerGuiSection : GuiSection(true) {
    override val title = "Debugger"

    override fun declareGui(window: Window) {
        if (!Debugger.menuVisible)
            return

        ImGui.text("fps: ${Debugger.fps}")
        ImGui.text("position: ${Debugger.position.displayString}")
        ImGui.text("chunk: ${Debugger.chunk.displayString}")
        ImGui.text("average chunk meshing time: ${"%.${2}f".format(Debugger.averageChunkMeshingTimeNano / 1_000_000.0)}ms")
        ImGui.text("average chunk generation time: ${"%.${2}f".format(Debugger.averageChunkGenerationTimeNano / 1_000_000.0)}ms")
        ImGui.text("client IPv4 address: ${Debugger.ipAddress}")

        if (ImGui.button("Toggle wireframe")) {
            val client = GameEngineProvider.gameEngine as Client
            client.mainRenderer.toggleWireframe()
        }
    }
}