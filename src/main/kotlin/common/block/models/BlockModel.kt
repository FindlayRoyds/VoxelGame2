package common.block.models

import common.block.faces.FullModelFace
import common.block.segments.EmptyModelSection

class BlockModel: Model {
    val textureIds: List<Int>

    constructor(textureId: Int) : super(
            bottomFace = FullModelFace(textureId), topFace = FullModelFace(textureId),
            leftFace = FullModelFace(textureId), rightFace = FullModelFace(textureId),
            backFace = FullModelFace(textureId), frontFace = FullModelFace(textureId),
            EmptyModelSection()
    ) {
        textureIds = listOf(textureId, textureId, textureId, textureId, textureId, textureId)
        id = registerModel(this)
    }

    constructor(topTextureId: Int, sidesTextureId: Int, bottomTextureId: Int) : super(
            bottomFace = FullModelFace(bottomTextureId), topFace = FullModelFace(topTextureId),
            leftFace = FullModelFace(sidesTextureId), rightFace = FullModelFace(sidesTextureId),
            backFace = FullModelFace(sidesTextureId), frontFace = FullModelFace(sidesTextureId),
            EmptyModelSection()
    ) {
        textureIds = listOf(
            topTextureId, bottomTextureId,
            sidesTextureId, sidesTextureId,
            sidesTextureId, sidesTextureId)
        id = registerModel(this)
    }

    override fun equals(other: Any?): Boolean {
        return other is BlockModel && textureIds == other.textureIds
    }
}