package common.math

import org.joml.Vector3f
import java.lang.Float.floatToIntBits


data class Float3(var x: Float, var y: Float, var z: Float) {
    operator fun plus(other: Float3): Float3 = Float3(x + other.x, y + other.y, z + other.z)

    operator fun minus(other: Float3): Float3 = Float3(x - other.x, y - other.y, z - other.z)

    operator fun times(other: Float3): Float3 = Float3(x * other.x, y * other.y, z * other.z)
    operator fun times(other: Int): Float3 = Float3(x * other, y * other, z * other)
    operator fun times(other: Float): Float3 = Float3(x * other, y * other, z * other)

    operator fun div(other: Float3): Float3 = Float3(x / other.x, y / other.y, z / other.z)

    operator fun plusAssign(other: Float3) {
        x += other.x
        y += other.y
        z += other.z
    }
    operator fun plusAssign(other: Vector3f) {
        x += other.x
        y += other.y
        z += other.z
    }
    operator fun minusAssign(other: Float3) {
        x -= other.x
        y -= other.y
        z -= other.z
    }
    operator fun minusAssign(other: Vector3f) {
        x -= other.x
        y -= other.y
        z -= other.z
    }
    operator fun timesAssign(other: Float3) {
        x *= other.x
        y *= other.y
        z *= other.z
    }
    operator fun timesAssign(other: Vector3f) {
        x *= other.x
        y *= other.y
        z *= other.z
    }
    operator fun divAssign(other: Float3) {
        x /= other.x
        y /= other.y
        z /= other.z
    }
    operator fun divAssign(other: Vector3f) {
        x /= other.x
        y /= other.y
        z /= other.z
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + floatToIntBits(x)
        result = prime * result + floatToIntBits(y)
        result = prime * result + floatToIntBits(z)
        return result
    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (javaClass != other.javaClass) return false
        val otherCasted = other as Float3
        if (floatToIntBits(x) != floatToIntBits(otherCasted.x)) return false
        if (floatToIntBits(y) != floatToIntBits(otherCasted.y)) return false
        return floatToIntBits(z) == floatToIntBits(otherCasted.z)
    }

    fun toVector3f(): Vector3f {
        return Vector3f(x, y, z)
    }
}