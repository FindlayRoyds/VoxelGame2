package common.math

import java.lang.Double.doubleToLongBits
import kotlin.math.abs
import kotlin.math.sign


data class Double3(override val x: Double, override val y: Double, override val z: Double) : Vector3<Double>(x, y, z) {
    constructor(x: Number, y: Number, z: Number) : this(x.toDouble(), y.toDouble(), z.toDouble())
    override val sign: Int3
        get() {
            return Int3(sign(x).toInt(), sign(y).toInt(), sign(z).toInt())
        }
    override val xz: Double2
        get() = Double2(x, z)
    override val normal: Double3
        get() = this / magnitude
    override val abs: Double3
        get() = Double3(abs(x), abs(y), abs(z))
    override val minimumComponent: Int3
        get() {
            if (x <= y && x <= z) {
                return Int3(1, 0, 0)
            }
            if (y <= x && y <= z) {
                return Int3(0, 1, 0)
            }
            return Int3(0, 0, 1)
        }
    override val displayString: String
        get() = "$x, $y, $z"

    override operator fun plus(other: Vector3<Double>): Double3 = Double3(x + other.x, y + other.y, z + other.z)
    override operator fun minus(other: Vector3<Double>): Double3 = Double3(x - other.x, y - other.y, z - other.z)

    override operator fun times(other: Int3): Double3 = Double3(x * other.x, y * other.y, z * other.z)
    override operator fun times(other: Int): Double3 = Double3(x * other, y * other, z * other)

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

    override fun dot(other: Vector3<Double>): Double {
        return this.x * other.x + this.y * other.y + this.z * other.z
    }

    override operator fun unaryMinus(): Double3 {
        return this * -1
    }

    companion object {
        val up: Double3
            get() = Double3(0, 1, 0)
        val down: Double3
            get() = Double3(0, -1, 0)
        val right: Double3
            get() = Double3(1, 0, 0)
        val left: Double3
            get() = Double3(-1, 0, 0)
        val forwards: Double3
            get() = Double3(0, 0, 1)
        val backwards: Double3
            get() = Double3(0, 0, -1)

        val zero: Double3
            get() = Double3(0, 0, 0)
    }
}