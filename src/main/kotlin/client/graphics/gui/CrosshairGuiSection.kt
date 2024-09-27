package client.graphics.gui

import client.graphics.Window
import imgui.ImGui
import imgui.flag.ImGuiCol

class CrosshairGuiSection : GuiSection(true) {
    override val title = "Crosshair"

    override fun declareGui(window: Window) {
        val io = ImGui.getIO()
        val centerX = io.displaySize.x / 2
        val centerY = io.displaySize.y / 2

        // Get the draw list for the current window
        val drawList = ImGui.getWindowDrawList()

        // Define the size of the crosshair
        val crosshairSize = 10.0f

        // Draw the crosshair
        drawList.addLine(centerX - crosshairSize, centerY, centerX + crosshairSize, centerY, ImGui.getColorU32(ImGuiCol.Text))
        drawList.addLine(centerX, centerY - crosshairSize, centerX, centerY + crosshairSize, ImGui.getColorU32(ImGuiCol.Text))
    }
}