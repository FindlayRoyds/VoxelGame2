package common.world.noise

import common.math.Double2

abstract class Noise2D(scale: Double2, amplitude: Double2, seed: Int) {
    abstract fun getValue(position: Double2)
}