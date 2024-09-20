package common.math

import kotlin.math.sqrt

interface Vector2<T: Number> {
    var x: T
    var y: T
    var z: T

    val magnitude: Double
        get() {
            return sqrt(
                x.toDouble() * x.toDouble()
                        + y.toDouble() * y.toDouble()
                        + z.toDouble() * z.toDouble())
        }

    // TODO sign

    val xz: Vector3<T>

    fun toDouble3(): Double3 {
        return Double3(x.toDouble(), y.toDouble(), z.toDouble())
    }

    fun normal(): Double3 {
        return toDouble3() / magnitude
    }
}