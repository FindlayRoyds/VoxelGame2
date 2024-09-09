package common.block.models

import common.block.faces.EmptyModelFace
import common.block.faces.FullModelFace
import common.block.faces.HalfModelFace
import common.block.segments.PlaneModelSection
import common.math.Double2
import common.math.Double3

class SlabModel: Model {
    val textureIds: List<Int>

    constructor(textureId: Int) : super(
            bottomFace = FullModelFace(textureId), topFace = EmptyModelFace(),
            leftFace = HalfModelFace(textureId), rightFace = HalfModelFace(textureId),
            backFace = HalfModelFace(textureId), frontFace = HalfModelFace(textureId),
            centerModel = PlaneModelSection(Double3.zero, Double3.zero, Double3(1, 1, 1), textureId, Double2.zero)
    ) {
        textureIds = listOf(textureId, textureId, textureId, textureId, textureId, textureId)
        id = registerModel(this)
    }

    constructor(topTextureId: Int, sidesTextureId: Int, bottomTextureId: Int) : super(
            bottomFace = FullModelFace(bottomTextureId), topFace = EmptyModelFace(),
            leftFace = HalfModelFace(sidesTextureId), rightFace = HalfModelFace(sidesTextureId),
            backFace = HalfModelFace(sidesTextureId), frontFace = HalfModelFace(sidesTextureId),
            centerModel = PlaneModelSection(Double3.zero, Double3.zero, Double3(1, 1, 1), topTextureId, Double2.zero)
    ) {
        textureIds = listOf(
            topTextureId, bottomTextureId,
            sidesTextureId, sidesTextureId,
            sidesTextureId, sidesTextureId)
        id = registerModel(this)
    }

    override fun equals(other: Any?): Boolean {
        return other is SlabModel && textureIds == other.textureIds
    }
}