package common.world.noise
//
//import common.math.Double2
//
//import kotlin.math.floor
//import kotlin.random.Random
//
//class PerlinNoise2D(seed: Int) {
//    private val permutation = List(256) { it }.shuffled(Random(seed)).toIntArray()
//    private val directions = listOf(
//        Double2(1.0, 1.0), Double2(-1.0, 1.0), Double2(1.0, -1.0), Double2(-1.0, -1.0),
//        Double2(1.0, 0.0), Double2(-1.0, 0.0), Double2(0.0, 1.0), Double2(0.0, -1.0)
//    )
//
//    private fun dot(grid: Double2, x: Double, y: Double) = grid.x * x + grid.y * y
//
//    private fun fade(t: Double) = t * t * t * (t * (t * 6 - 15) + 10)
//
//    private fun lerp(t: Double, a: Double, b: Double) = a + t * (b - a)
//
//    private fun grad(hash: Int, x: Double, y: Double) = dot(directions[hash and 7], x, y)
//
//    fun noise(point: Double2): Double {
//        val x = point.x
//        val y = point.y
//
//        val X = (floor(x).toInt().rem(256) + 256) % 256
//        val Y = (floor(y).toInt().rem(256) + 256) % 256
//
//        val xRemainder = x - floor(x)
//        val yRemainder = y - floor(y)
//
//        val u = fade(xRemainder % 1.0)
//        val v = fade(yRemainder % 1.0)
//
//        val A = permutation[X % 256] + Y
//        val B = permutation[(X + 1) % 256] + Y
//
//        return lerp(v,
//            lerp(u, grad(permutation[A % 256], xRemainder, yRemainder), grad(permutation[B % 256], xRemainder - 1, yRemainder)),
//            lerp(u, grad(permutation[(A + 1) % 256], xRemainder, yRemainder - 1), grad(permutation[(B + 1) % 256], xRemainder - 1, yRemainder - 1))
//        )
//    }
//}

class PerlinNoise2D {
    fun noise(x: Double, y: Double, z: Double): Double {
        var x = x
        var y = y
        var z = z
        val X = Math.floor(x).toInt() and 255
        // Fine unit cube that
        val Y = Math.floor(y).toInt() and 255
        // contains point
        val Z = Math.floor(z).toInt() and 255
        x -= Math.floor(x) // Find relative x,y,z
        y -= Math.floor(y) // of point in cube
        z -= Math.floor(z)
        val u = fade(x)
        // Compute fade curves
        val v = fade(y)
        // for each of x,y,z
        val w = fade(z)
        val A = p[X] + Y
        val AA = p[A] + Z
        val AB = p[A + 1] + Z
        // Hash coordinates of
        val B = p[X + 1] + Y
        val BA = p[B] + Z
        val BB = p[B + 1] + Z // the 8 cube corners
        // ... and add blended results from 8 corners of cube
        return lerp(
            w, lerp(
                v, lerp(
                    u, grad(p[AA], x, y, z),
                    grad(p[BA], x - 1, y, z)
                ),
                lerp(
                    u, grad(p[AB], x, y - 1, z),
                    grad(p[BB], x - 1, y - 1, z)
                )
            ),
            lerp(
                v, lerp(
                    u, grad(p[AA + 1], x, y, z - 1),
                    grad(p[BA + 1], x - 1, y, z - 1)
                ),
                lerp(
                    u, grad(p[AB + 1], x, y - 1, z - 1),
                    grad(p[BB + 1], x - 1, y - 1, z - 1)
                )
            )
        )
    }

    fun fade(t: Double): Double {
        return t * t * t * (t * (t * 6 - 15) + 10)
    }

    fun lerp(t: Double, a: Double, b: Double): Double {
        return a + t * (b - a)
    }

    fun grad(hash: Int, x: Double, y: Double, z: Double): Double {
        val h = hash and 15 // Convert low 4 bits of hash code
        val u = if (h < 8) x else y
        // into 12 gradient directions
        val v = if (h < 4) y else if (h == 12 || h == 14) x else z
        return (if (h and 1 == 0) u else -u) + if (h and 2 == 0) v else -v
    }

    val p = IntArray(512)
    val permutation = intArrayOf(
        151, 160, 137, 91, 90, 15
    )

    init {
        for (i in 0..255) {
            p[i] = permutation[i]
            p[256 + i] = p[i]
        }
    }
}