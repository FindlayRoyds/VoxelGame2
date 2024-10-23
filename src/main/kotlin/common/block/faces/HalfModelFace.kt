package common.block.faces

import common.block.sections.PlaneModelSection
import common.math.Double2
import common.math.Double3

class HalfModelFace(textureName: String): ModelFace() {
    override val segments = listOf(
        PlaneModelSection(Double3(0.0, 0.0, -0.25), Double3.zero, Double3(1.0, 1.0, 0.5), textureName, Double2(0.25, 0.0))
    )

    override fun isCulled(otherFace: ModelFace): Boolean {
        return when (otherFace) {
            is FullModelFace -> true
            is HalfModelFace -> true
            else -> false
        }
    }
}