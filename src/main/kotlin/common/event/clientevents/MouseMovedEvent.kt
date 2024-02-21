package common.event.clientevents

import common.Config
import common.event.ClientEvent
import org.joml.Vector2f

class MouseMovedEvent(val displacement: Vector2f): ClientEvent() {
    override fun event() {
        val camera = client!!.renderer.camera
        camera.addRotation(displacement.mul(Config.mouseSensitivity.toFloat()))
    }
}