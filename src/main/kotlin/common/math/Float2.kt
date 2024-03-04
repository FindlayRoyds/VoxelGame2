package common.math

import org.joml.Vector2f
import java.lang.Float.floatToIntBits

data class Float2(var x: Float, var y: Float) {
    operator fun plus(other: Float2): Float2 = Float2(x + other.x, y + other.y)

    operator fun minus(other: Float2): Float2 = Float2(x - other.x, y - other.y)

    operator fun times(other: Float2): Float2 = Float2(x * other.x, y * other.y)
    operator fun times(other: Int): Float2 = Float2(x * other, y * other)
    operator fun times(other: Float): Float2 = Float2(x * other, y * other)

    operator fun div(other: Float2): Float2 = Float2(x / other.x, y / other.y)

    operator fun plusAssign(other: Float2) {
        x += other.x
        y += other.y
    }
    operator fun plusAssign(other: Vector2f) {
        x += other.x
        y += other.y
    }
    operator fun minusAssign(other: Float2) {
        x -= other.x
        y -= other.y
    }
    operator fun minusAssign(other: Vector2f) {
        x -= other.x
        y -= other.y
    }
    operator fun timesAssign(other: Float2) {
        x *= other.x
        y *= other.y
    }
    operator fun timesAssign(other: Vector2f) {
        x *= other.x
        y *= other.y
    }
    operator fun divAssign(other: Float2) {
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
        result = prime * result + floatToIntBits(x)
        result = prime * result + floatToIntBits(y)
        return result
    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (javaClass != other.javaClass) return false
        val otherCasted = other as Float2
        if (floatToIntBits(x) != floatToIntBits(otherCasted.x)) return false
        return floatToIntBits(y) == floatToIntBits(otherCasted.y)
    }

    fun toVector2f(): Vector2f {
        return Vector2f(x, y)
    }

    fun set(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    fun set(other: Float2) {
        x = other.x
        y = other.y
    }
}