package client.graphics

import org.joml.Matrix4d

class Projection(width: Int, height: Int) {
    var fov = Math.toRadians(60.0)
    var zFar = 1000.0
    var zNear = 0.1
    val matrix = Matrix4d()

    init {
        update(width, height)
    }

    fun update(width: Int, height: Int) {
        matrix.setPerspective(fov, width.toDouble() / height.toDouble(), zNear, zFar)
    }
}