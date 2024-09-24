package common.math

import kotlin.math.abs
import kotlin.math.sign

data class Int3(override val x: Int, override val y: Int, override val z: Int) : Vector3<Int>(x, y, z) {
    override val sign: Int3
        get() {
            return Int3(sign(x.toDouble()).toInt(), sign(y.toDouble()).toInt(), sign(z.toDouble()).toInt())
        }
    override val xz: Int2
        get() {
            return Int2(x, z)
        }
    override val normal: Double3
        get() = this.toDouble3() / magnitude
    override val abs: Int3
        get() = Int3(abs(x), abs(y), abs(z))
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

    override operator fun plus(other: Vector3<Int>): Int3 = Int3(x + other.x, y + other.y, z + other.z)
    override operator fun minus(other: Vector3<Int>): Int3 = Int3(x - other.x, y - other.y, z - other.z)

    override operator fun times(other: Int3): Int3 = Int3(x * other.x, y * other.y, z * other.z)
    override operator fun times(other: Int): Int3 = Int3(x * other, y * other, z * other)

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + x
        result = prime * result + y
        result = prime * result + z
        return result
    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Int3
        if (x != other.x) return false
        if (y != other.y) return false
        return z == other.z
    }

    override fun dot(other: Vector3<Int>): Int {
        return this.x * other.x + this.y * other.y + this.z * other.z
    }

    override operator fun unaryMinus(): Int3 {
        return this * -1
    }

    companion object {
        val up: Int3
            get() = Int3(0, 1, 0)
        val down: Int3
            get() = Int3(0, -1, 0)
        val right: Int3
            get() = Int3(1, 0, 0)
        val left: Int3
            get() = Int3(-1, 0, 0)
        val forwards: Int3
            get() = Int3(0, 0, 1)
        val backwards: Int3
            get() = Int3(0, 0, -1)

        val zero: Int3
            get() = Int3(0, 0, 0)
    }
}