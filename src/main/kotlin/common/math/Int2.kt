package common.math

import java.io.Serializable
import kotlin.math.sign
import kotlin.math.sqrt

data class Int2(var x: Int, var y: Int) : Serializable {
    val magnitude: Double
        get() {
            return sqrt((x * x + y * y).toDouble())
        }
    val sign: Int2
        get() {
            return Int2(sign(x.toDouble()).toInt(), sign(y.toDouble()).toInt())
        }

    operator fun plus(other: Int2): Int2 = Int2(x + other.x, y + other.y)

    operator fun minus(other: Int2): Int2 = Int2(x - other.x, y - other.y)

    operator fun times(other: Int2): Int2 = Int2(x * other.x, y * other.y)
    operator fun times(other: Int): Int2 = Int2(x * other, y * other)

    operator fun div(other: Int2): Int2 = Int2(x.floorDiv(other.x), y.floorDiv(other.y))
    operator fun div(other: Int): Int2 = Int2(x.floorDiv(other), y.floorDiv(other))
    // operator fun div(other: Float): Int3 = Int3(x / other, y / other, z / other)

    operator fun plusAssign(other: Int2) {
        x += other.x
        y += other.y
    }
    operator fun minusAssign(other: Int2) {
        x -= other.x
        y -= other.y
    }
    operator fun timesAssign(other: Int2) {
        x *= other.x
        y *= other.y
    }
    operator fun divAssign(other: Int2) {
        x /= other.x
        y /= other.y
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + x
        result = prime * result + y
        return result
    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Int2
        if (x != other.x) return false
        return y == other.y
    }

    fun toDouble2(): Double2 {
        return Double2(this.x.toDouble(), this.y.toDouble())
    }
}