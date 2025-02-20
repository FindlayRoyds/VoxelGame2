package common.block

import common.math.Double2
import common.math.Double3
import org.joml.Matrix4d
import org.joml.Vector4d
import java.io.Serializable
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sign
import kotlin.math.sin

class Vertex(
    var position: Double3, var normal: Double3,
    val textureName: String, var textureCoord: Double2
): VertexCollection(), Serializable {

    fun translate(translation: Double3): Vertex {
        position += translation
        return this
    }

    fun scale(scale: Double3): Vertex {
        position *= scale
        textureCoord *= Double2(scale.x, scale.z)
        normal *= sign(scale.x * scale.y * scale.z) // Shouldn't work like this lol
        return this
    }

    fun rotate(rotationDegrees: Double3): Vertex {
        val rotation = rotationDegrees * PI / 180
        val (x, y, z) = listOf(rotation.x, rotation.y, rotation.z)

        val zMatrix = Matrix4d(
            Vector4d(cos(z), -sin(z), 0.0, 0.0),
            Vector4d(sin(z),  cos(z), 0.0, 0.0),
            Vector4d(0.0,     0.0,    1.0, 0.0),
            Vector4d(0.0,     0.0,    0.0, 1.0)
        )
        val xMatrix = Matrix4d(
            Vector4d(1.0, 0.0,     0.0,    0.0),
            Vector4d(0.0, cos(x), -sin(x), 0.0),
            Vector4d(0.0, sin(x),  cos(x), 0.0),
            Vector4d(0.0, 0.0,     0.0,    1.0)
        )
        val yMatrix = Matrix4d(
            Vector4d( cos(y), 0.0, sin(y), 0.0),
            Vector4d( 0.0,    1.0, 0.0,    0.0),
            Vector4d(-sin(y), 0.0, cos(y), 0.0),
            Vector4d( 0.0,    0.0, 0.0,    1.0)
        )

        val newPosition = position.toVector3d()
        newPosition.mulPosition(zMatrix).mulPosition(xMatrix).mulPosition(yMatrix)
        position = Double3(newPosition.x, newPosition.y, newPosition.z)

        val newNormal = normal.toVector3d()
        newNormal.mulPosition(zMatrix).mulPosition(xMatrix).mulPosition(yMatrix)
        normal = Double3(newNormal.x, newNormal.y, newNormal.z)

        return this
    }

    override fun getVertices(): List<Vertex> {
        return listOf(this)
    }
}