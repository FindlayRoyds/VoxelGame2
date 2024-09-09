package common.math

import org.joml.Vector2d
import org.joml.Vector2f
import java.io.Serializable
import java.lang.Double.doubleToLongBits

data class Double2(var x: Double, var y: Double) : Serializable {
    constructor(x: Int, y: Int) : this(x.toDouble(), y.toDouble())

    operator fun plus(other: Double2): Double2 = Double2(x + other.x, y + other.y)

    operator fun minus(other: Double2): Double2 = Double2(x - other.x, y - other.y)

    operator fun times(other: Double2): Double2 = Double2(x * other.x, y * other.y)
    operator fun times(other: Int): Double2 = Double2(x * other, y * other)
    operator fun times(other: Double): Double2 = Double2(x * other, y * other)

    operator fun div(other: Double2): Double2 = Double2(x / other.x, y / other.y)

    operator fun plusAssign(other: Double2) {
        x += other.x
        y += other.y
    }
    operator fun plusAssign(other: Vector2f) {
        x += other.x
        y += other.y
    }
    operator fun minusAssign(other: Double2) {
        x -= other.x
        y -= other.y
    }
    operator fun minusAssign(other: Vector2f) {
        x -= other.x
        y -= other.y
    }
    operator fun timesAssign(other: Double2) {
        x *= other.x
        y *= other.y
    }
    operator fun timesAssign(other: Vector2f) {
        x *= other.x
        y *= other.y
    }
    operator fun divAssign(other: Double2) {
        x /= other.x
        y /= other.y
    }
    operator fun divAssign(other: Vector2f) {
        x /= other.x
        y /= other.y
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + doubleToLongBits(x).toInt()
        result = prime * result + doubleToLongBits(y).toInt()
        return result
    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (javaClass != other.javaClass) return false
        val otherCasted = other as Double2
        if (doubleToLongBits(x) != doubleToLongBits(otherCasted.x)) return false
        return doubleToLongBits(y) == doubleToLongBits(otherCasted.y)
    }

    fun toVector2d(): Vector2d {
        return Vector2d(x, y)
    }
    fun toVector2f(): Vector2f {
        return Vector2f(x.toFloat(), y.toFloat())
    }

    fun set(x: Double, y: Double) {
        this.x = x
        this.y = y
    }

    fun set(other: Double2) {
        x = other.x
        y = other.y
    }

    companion object {
        val zero = Double2(0, 0)
    }
}