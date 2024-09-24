package common.math

import java.lang.Double.doubleToLongBits
import kotlin.math.abs
import kotlin.math.sign

data class Double2(override val x: Double, override val y: Double) : Vector2<Double>(x, y) {
    constructor(x: Number, y: Number) : this(x.toDouble(), y.toDouble())
    override val sign: Int2
        get() {
            return Int2(sign(x).toInt(), sign(y).toInt())
        }
    override val normal: Double2
        get() = this / magnitude
    override val abs: Double2
        get() = Double2(abs(x), abs(y))
    override val minimumComponent: Int2
        get() {
            if (x <= y) {
                return Int2(1, 0)
            }
            return Int2(0, 1)
        }
    override val displayString: String
        get() = "$x, $y"



    override operator fun plus(other: Vector2<Double>): Double2 = Double2(x + other.x, y + other.y)
    override operator fun minus(other: Vector2<Double>): Double2 = Double2(x - other.x, y - other.y)

    override operator fun times(other: Int2): Double2 = Double2(x * other.x, y * other.y)
    override operator fun times(other: Double2): Double2 = Double2(x * other.x, y * other.y)
    override operator fun times(other: Int): Double2 = Double2(x * other, y * other)
    override operator fun times(other: Double): Double2 = Double2(x * other, y * other)

    override operator fun div(other: Int2): Double2 = Double2(x / other.x, y / other.y)
    override operator fun div(other: Double2): Double2 = Double2(x / other.x, y / other.y)
    override operator fun div(other: Int): Double2 = Double2(x / other, y / other)
    override operator fun div(other: Double): Double2 = Double2(x / other, y / other)

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

    override fun dot(other: Vector2<Double>): Double {
        return this.x * other.x + this.y * other.y
    }

    override operator fun unaryMinus(): Double2 {
        return this * -1
    }

    companion object {
        val up: Double2
            get() = Double2(0, 1)
        val down: Double2
            get() = Double2(0, -1)
        val right: Double2
            get() = Double2(1, 0)
        val left: Double2
            get() = Double2(-1, 0)

        val zero: Double2
            get() = Double2(0, 0)
    }
}