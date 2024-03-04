package client.graphics

import client.Client
import common.Config
import common.GameEngineProvider
import common.event.serverevents.UpdatePositionRequestEvent
import common.math.Float2
import common.math.Float3
import org.joml.Matrix4f


class Camera {
    var lookVector = Float3(0f, 0f, 0f)
    var upVector = Float3(0f, 0f, 0f)
    var rightVector = Float3(0f, 0f, 0f)
    val position = Float3(0f, 0f, 0f)
    val rotation = Float3(0f, 0f, 0f)
    var viewMatrix = Matrix4f()
        get() {
            return field.identity()
                .rotateX(rotation.x)
                .rotateY(rotation.y)
                .translate(-position.x, -position.y, -position.z)
        }

    fun addRotation(x: Float, y: Float) {
        rotation += Float3(x, y, 0f)

        // Limit rotation around X-axis to prevent camera from going upside down
        rotation.x = rotation.x.coerceIn(Math.toRadians((-Config.cameraVerticalLimit).toDouble()).toFloat(), Math.toRadians(Config.cameraVerticalLimit.toDouble()).toFloat())
    }

    fun addRotation(rotation: Float2) {
        addRotation(rotation.x, rotation.y)
    }

    private fun moveBackwards(inc: Float) {
        position += viewMatrix.positiveZ(lookVector.toVector3f()).mul(inc)
    }

    private fun moveDown(inc: Float) {
        viewMatrix.positiveY(upVector.toVector3f()).mul(inc)
        position -= Float3(0f, inc, 0f)
    }

    private fun moveForward(inc: Float) {
        position -= viewMatrix.positiveZ(lookVector.toVector3f()).mul(inc)
    }

    private fun moveLeft(inc: Float) {
        position -= viewMatrix.positiveX(rightVector.toVector3f()).mul(inc)
    }

    private fun moveRight(inc: Float) {
        position += viewMatrix.positiveX(rightVector.toVector3f()).mul(inc)
    }

    private fun moveUp(inc: Float) {
        viewMatrix.positiveY(upVector.toVector3f()).mul(inc)
        position += Float3(0f, inc, 0f)
    }

    fun addPosition(inc: Float3) {
        moveForward(inc.z)
        moveUp(inc.y)
        moveLeft(inc.x)

        val updatePositionRequestEvent = UpdatePositionRequestEvent(position)
        val client = GameEngineProvider.getGameEngine() as Client
        client.socketHandler.sendEvent(updatePositionRequestEvent)
    }
}