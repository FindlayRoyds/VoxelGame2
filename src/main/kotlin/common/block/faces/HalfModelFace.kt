package common.block.faces

import common.block.segments.PlaneModelSection
import common.math.Double2
import common.math.Double3

class HalfModelFace(textureId: Int): ModelFace() {
    override val segments = listOf(
        PlaneModelSection(Double3(0.0, 0.0, -0.25), Double3.zero, Double3(1.0, 1.0, 0.5), textureId, Double2(0.25, 0.0))
    )

    override fun isCulled(otherFace: ModelFace): Boolean {
        return when (otherFace) {
            is FullModelFace -> true
            is HalfModelFace -> true
            else -> false
        }
    }
}