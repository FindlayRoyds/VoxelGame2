package client.graphics.gui

import client.graphics.Window

interface GuiSection {
    fun declareGui(window: Window)
}