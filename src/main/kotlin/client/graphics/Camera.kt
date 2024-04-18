package client.graphics

import client.Client
import common.Config
import common.GameEngineProvider
import common.event.serverevents.UpdatePositionRequestEvent
import common.math.Double2
import common.math.Double3
import org.joml.Matrix4d
import kotlin.math.cos
import kotlin.math.sin


class Camera {
    var upVector = Double3(0, 0, 0)
    var rightVector = Double3(0, 0, 0)
    val position = Double3(0, 50, 0)
    val rotation = Double3(0, 0, 0)
    var viewMatrix = Matrix4d()
        get() {
            return field.identity()
                .rotateX(rotation.x)
                .rotateY(rotation.y)
                .translate(-position.x, -position.y, -position.z)
        }
    val lookVector: Double3
        get() {
            return Double3(
                cos(-rotation.x) * sin(rotation.y),
                sin(-rotation.x),
                cos(-rotation.x) * -cos(rotation.y)
            )
        }

    fun addRotation(x: Double, y: Double) {
        rotation += Double3(x, y, 0.0)

        // Limit rotation around X-axis to prevent camera from going upside down
        rotation.x = rotation.x.coerceIn(Math.toRadians(-Config.cameraVerticalLimit.toDouble()), Math.toRadians(Config.cameraVerticalLimit.toDouble()))
    }

    fun addRotation(rotation: Double2) {
        addRotation(rotation.x, rotation.y)
    }

    private fun moveForward(inc: Double) {
        position.x += inc * sin(rotation.y)
        position.z -= inc * cos(rotation.y)
    }

    private fun moveLeft(inc: Double) {
        position.z -= inc * Math.sin(rotation.y)
        position.x -= inc * Math.cos(rotation.y)
    }

    private fun moveUp(inc: Double) {
        viewMatrix.positiveY(upVector.toVector3d()).mul(inc)
        position += Double3(0.0, inc, 0.0)
    }

    fun addPosition(inc: Double3) {
        moveForward(inc.z)
        moveUp(inc.y)
        moveLeft(inc.x)

        val updatePositionRequestEvent = UpdatePositionRequestEvent(position)
        val client = GameEngineProvider.getGameEngine() as Client
        client.socketHandler.sendEvent(updatePositionRequestEvent)
    }
}