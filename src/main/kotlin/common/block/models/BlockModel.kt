package common.block.models

import common.block.faces.FullModelFace
import common.block.sections.EmptyModelSection

class BlockModel: Model {
    val textureNames: List<String>

    constructor(textureId: String) : super(
            bottomFace = FullModelFace(textureId), topFace = FullModelFace(textureId),
            leftFace = FullModelFace(textureId), rightFace = FullModelFace(textureId),
            backFace = FullModelFace(textureId), frontFace = FullModelFace(textureId),
            EmptyModelSection()
    ) {
        textureNames = listOf(textureId, textureId, textureId, textureId, textureId, textureId)
        id = registerModel(this)
    }

    constructor(topTextureId: String, sidesTextureId: String, bottomTextureId: String) : super(
            bottomFace = FullModelFace(bottomTextureId), topFace = FullModelFace(topTextureId),
            leftFace = FullModelFace(sidesTextureId), rightFace = FullModelFace(sidesTextureId),
            backFace = FullModelFace(sidesTextureId), frontFace = FullModelFace(sidesTextureId),
            EmptyModelSection()
    ) {
        textureNames = listOf(
            topTextureId, bottomTextureId,
            sidesTextureId, sidesTextureId,
            sidesTextureId, sidesTextureId)
        id = registerModel(this)
    }

    override fun equals(other: Any?): Boolean {
        return other is BlockModel && textureNames == other.textureNames
    }
}