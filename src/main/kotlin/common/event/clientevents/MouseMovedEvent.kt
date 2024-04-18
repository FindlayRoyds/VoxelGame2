package common.event.clientevents

import common.Config
import common.event.ClientEvent
import common.math.Double2

class MouseMovedEvent(val displacement: Double2): ClientEvent() {
    override fun event() {
        val camera = client!!.renderer.camera
        camera.addRotation(displacement * Config.mouseSensitivity)
    }
}