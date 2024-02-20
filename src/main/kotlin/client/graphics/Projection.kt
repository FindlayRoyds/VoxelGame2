package client.graphics

import org.joml.Matrix4f;

class Projection(width: Int, height: Int) {
    var fov = Math.toRadians(60.0).toFloat()
    var zFar = 1000f
    var zNear = 0.01f
    val matrix = Matrix4f()

    init {
        update(width, height)
    }

    fun update(width: Int, height: Int) {
        matrix.setPerspective(fov, width.toFloat() / height.toFloat(), zNear, zFar)
    }
}