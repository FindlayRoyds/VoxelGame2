package common.block.models

import common.block.faces.EmptyModelFace
import common.block.sections.PlaneCrossModelSection
import common.math.Double2
import common.math.Double3

class VerticalPlaneCrossModel: Model {
    val textureIds: List<Int>

    constructor(textureId: Int) : super(
            bottomFace = EmptyModelFace(), topFace = EmptyModelFace(),
            leftFace = EmptyModelFace(), rightFace = EmptyModelFace(),
            backFace = EmptyModelFace(), frontFace = EmptyModelFace(),
            centerModel = PlaneCrossModelSection(Double3.zero, Double3.zero, Double3(1, 1, 1), textureId, Double2.zero)
    ) {
        textureIds = listOf(textureId, textureId, textureId, textureId, textureId, textureId)
        id = registerModel(this)
    }

    override fun equals(other: Any?): Boolean {
        return other is VerticalPlaneCrossModel && textureIds == other.textureIds
    }
}