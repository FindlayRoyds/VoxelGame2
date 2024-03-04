package common.math

data class Int3(var x: Int, var y: Int, var z: Int) {
    operator fun plus(other: Int3): Int3 = Int3(x + other.x, y + other.y, z + other.z)

    operator fun minus(other: Int3): Int3 = Int3(x - other.x, y - other.y, z - other.z)

    operator fun times(other: Int3): Int3 = Int3(x * other.x, y * other.y, z * other.z)
    operator fun times(other: Int): Int3 = Int3(x * other, y * other, z * other)

    operator fun div(other: Int3): Int3 = Int3(x / other.x, y / other.y, z / other.z)

    operator fun plusAssign(other: Int3) {
        x += other.x
        y += other.y
        z += other.z
    }
    operator fun minusAssign(other: Int3) {
        x -= other.x
        y -= other.y
        z -= other.z
    }
    operator fun timesAssign(other: Int3) {
        x *= other.x
        y *= other.y
        z *= other.z
    }
    operator fun divAssign(other: Int3) {
        x /= other.x
        y /= other.y
        z /= other.z
    }

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
}