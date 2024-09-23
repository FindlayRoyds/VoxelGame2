package client.graphics.input.action

import common.Debugger

class DebugAction: Action() {
    override val onHold = false

    override fun execute() {
        Debugger.menuVisible = !Debugger.menuVisible
    }
}