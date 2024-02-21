package client.graphics

import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f


class Camera {
    var lookVector = Vector3f()
    var upVector = Vector3f()
    var rightVector = Vector3f()
    var position = Vector3f()
    var rotation = Vector3f()
    var viewMatrix = Matrix4f()
        get() {
            return field.identity()
                .rotateX(rotation.x)
                .rotateY(rotation.y)
                .translate(-position.x, -position.y, -position.z)
        }

    fun addRotation(x: Float, y: Float) {
        rotation.add(x, y, 0f)
    }

    fun addRotation(rotation: Vector2f) {
        addRotation(rotation.x, rotation.y)
    }

    fun moveBackwards(inc: Float) {
        viewMatrix.positiveZ(lookVector).negate().mul(inc)
        position.sub(lookVector)
    }

    fun moveDown(inc: Float) {
        viewMatrix.positiveY(upVector).mul(inc)
        position.sub(upVector)
    }

    fun moveForward(inc: Float) {
        viewMatrix.positiveZ(lookVector).negate().mul(inc)
        position.add(lookVector)
    }

    fun moveLeft(inc: Float) {
        viewMatrix.positiveX(rightVector).mul(inc)
        position.sub(rightVector)
    }

    fun moveRight(inc: Float) {
        viewMatrix.positiveX(rightVector).mul(inc)
        position.add(rightVector)
    }

    fun moveUp(inc: Float) {
        viewMatrix.positiveY(upVector).mul(inc)
        position.add(upVector)
    }

    fun addPosition(inc: Vector3f) {
        moveForward(inc.z)
        moveUp(inc.y)
        moveLeft(inc.x)
    }
}