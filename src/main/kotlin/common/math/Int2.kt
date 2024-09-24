package common.math

import kotlin.math.abs
import kotlin.math.sign

data class Int2(override val x: Int, override val y: Int) : Vector2<Int>(x, y) {
    override val sign: Int2
        get() {
            return Int2(sign(x.toDouble()).toInt(), sign(y.toDouble()).toInt())
        }
    override val normal: Double2
        get() = this.toDouble2() / magnitude
    override val abs: Int2
        get() = Int2(abs(x), abs(y))
    override val minimumComponent: Int2
        get() {
            if (x <= y) {
                return Int2(1, 0)
            }
            return Int2(0, 1)
        }
    override val displayString: String
        get() = "$x, $y"

    override operator fun plus(other: Vector2<Int>): Int2 = Int2(x + other.x, y + other.y)
    override operator fun minus(other: Vector2<Int>): Int2 = Int2(x - other.x, y - other.y)

    override operator fun times(other: Int2): Int2 = Int2(x * other.x, y * other.y)
    override operator fun times(other: Double2): Double2 = Double2(x * other.x, y * other.y)
    override operator fun times(other: Int): Int2 = Int2(x * other, y * other)
    override operator fun times(other: Double): Double2 = Double2(x * other, y * other)

    override operator fun div(other: Int2): Double2 = Double2(x / other.x, y / other.y)
    override operator fun div(other: Double2): Double2 = Double2(x / other.x, y / other.y)
    override operator fun div(other: Int): Double2 = Double2(x.toDouble() / other, y.toDouble() / other)
    override operator fun div(other: Double): Double2 = Double2(x / other, y / other)

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

    override fun dot(other: Vector2<Int>): Int {
        return this.x * other.x + this.y * other.y
    }

    override operator fun unaryMinus(): Int2 {
        return this * -1
    }

    companion object {
        val up: Int2
            get() = Int2(0, 1)
        val down: Int2
            get() = Int2(0, -1)
        val right: Int2
            get() = Int2(1, 0)
        val left: Int2
            get() = Int2(-1, 0)

        val zero: Int2
            get() = Int2(0, 0)
    }
}