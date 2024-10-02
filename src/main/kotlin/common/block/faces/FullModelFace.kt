package common.block.faces

import common.block.sections.PlaneModelSection
import common.math.Double2
import common.math.Double3

class FullModelFace(textureId: Int): ModelFace() {
    override val segments = listOf(
        PlaneModelSection(Double3.zero, Double3.zero, Double3(1, 1, 1), textureId, Double2.zero)
    )

    override fun isCulled(otherFace: ModelFace): Boolean {
        return when (otherFace) {
            is FullModelFace -> true
            else -> false
        }
    }
}