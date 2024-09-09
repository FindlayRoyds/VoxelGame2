package common.block.faces

import common.block.segments.ModelSection

class EmptyModelFace: ModelFace() {
    override val segments = listOf<ModelSection>()

    override fun isCulled(otherFace: ModelFace) = true
}