package common.block.faces

import common.block.sections.ModelSection

class EmptyModelFace: ModelFace() {
    override val segments = listOf<ModelSection>()

    override fun isCulled(otherFace: ModelFace) = true
}