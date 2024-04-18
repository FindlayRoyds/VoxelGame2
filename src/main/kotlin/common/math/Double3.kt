package common.math

import org.joml.Vector3d
import java.io.Serializable
import java.lang.Double.doubleToLongBits
import kotlin.math.*


data class Double3(var x: Double, var y: Double, var z: Double) : Serializable {
    constructor(x: Int, y: Int, z: Int) : this(x.toDouble(), y.toDouble(), z.toDouble())

    val magnitude: Double
        get() {
            return sqrt((x * x + y * y + z * z))
        }
    val sign: Double3
        get() {
            return Double3(sign(x), sign(y), sign(z))
        }

    operator fun plus(other: Double3): Double3 = Double3(x + other.x, y + other.y, z + other.z)

    operator fun minus(other: Double3): Double3 = Double3(x - other.x, y - other.y, z - other.z)

    operator fun times(other: Double3): Double3 = Double3(x * other.x, y * other.y, z * other.z)
    operator fun times(other: Int): Double3 = Double3(x * other, y * other, z * other)
    operator fun times(other: Double): Double3 = Double3(x * other, y * other, z * other)

    operator fun div(other: Double3): Double3 = Double3(x / other.x, y / other.y, z / other.z)
    operator fun div(other: Int): Double3 = Double3(x / other, y / other, z / other)
    operator fun div(other: Double): Double3 = Double3(x / other, y / other, z / other)

    operator fun plusAssign(other: Double3) {
        x += other.x
        y += other.y
        z += other.z
    }
    operator fun plusAssign(other: Vector3d) {
        x += other.x
        y += other.y
        z += other.z
    }
    operator fun minusAssign(other: Double3) {
        x -= other.x
        y -= other.y
        z -= other.z
    }
    operator fun minusAssign(other: Vector3d) {
        x -= other.x
        y -= other.y
        z -= other.z
    }
    operator fun timesAssign(other: Double3) {
        x *= other.x
        y *= other.y
        z *= other.z
    }
    operator fun timesAssign(other: Vector3d) {
        x *= other.x
        y *= other.y
        z *= other.z
    }
    operator fun divAssign(other: Double3) {
        x /= other.x
        y /= other.y
        z /= other.z
    }
    operator fun divAssign(other: Vector3d) {
        x /= other.x
        y /= other.y
        z /= other.z
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + doubleToLongBits(x).toInt()
        result = prime * result + doubleToLongBits(y).toInt()
        result = prime * result + doubleToLongBits(z).toInt()
        return result
    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (javaClass != other.javaClass) return false
        val otherCasted = other as Double3
        if (doubleToLongBits(x) != doubleToLongBits(otherCasted.x)) return false
        if (doubleToLongBits(y) != doubleToLongBits(otherCasted.y)) return false
        return doubleToLongBits(z) == doubleToLongBits(otherCasted.z)
    }

    fun toVector3d(): Vector3d {
        return Vector3d(x, y, z)
    }
    fun toInt3(): Int3 {
        return Int3(floor(x).toInt(), floor(y).toInt(), floor(z).toInt())
    }

    fun ceil(): Double3 {
        return Double3(ceil(x), ceil(y), ceil(z))
    }

    fun abs(): Double3 {
        return Double3(abs(x), abs(y), abs(z))
    }

    fun minimumComponent(): Double3 {
        if (x <= y && x <= z) {
            return Double3(1, 0, 0)
        }
        if (y <= x && y <= z) {
            return Double3(0, 1, 0)
        }
        return Double3(0, 0, 1)
    }
}