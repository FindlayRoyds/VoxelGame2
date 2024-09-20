package common.math

import org.joml.Vector3d
import org.joml.Vector3f
import org.joml.Vector3i
import kotlin.math.sqrt

abstract class Vector3<Type: Number>(open var x: Type, open var y: Type, open var z: Type) : Vector<Type> {
    override val magnitude: Double
        get() {
            return sqrt(
                x.toDouble() * x.toDouble()
                    + y.toDouble() * y.toDouble()
                    + z.toDouble() * z.toDouble())
        }
    abstract override val sign: Vector3<Int>
    // TODO implement here?
    abstract val xz: Vector2<Type>

    fun toDouble3(): Double3 {
        return Double3(x.toDouble(), y.toDouble(), z.toDouble())
    }
    fun toInt3(): Int3 {
        return Int3(x.toInt(), y.toInt(), z.toInt())
    }
    fun toVector3d(): Vector3d {
        return Vector3d(x.toDouble(), y.toDouble(), z.toDouble())
    }
    fun toVector3f(): Vector3f {
        return Vector3f(x.toFloat(), y.toFloat(), z.toFloat())
    }
    fun toVector3i(): Vector3i {
        return Vector3i(x.toInt(), y.toInt(), z.toInt())
    }

//    fun ceil(): Vector3<Type> {
//        return Double3(kotlin.math.ceil(x), kotlin.math.ceil(y), kotlin.math.ceil(z))
//    }
}