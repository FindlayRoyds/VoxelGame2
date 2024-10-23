package common.block.models

import common.block.faces.EmptyModelFace
import common.block.faces.FullModelFace
import common.block.faces.HalfModelFace
import common.block.sections.PlaneModelSection
import common.math.Double2
import common.math.Double3

class SlabModel: Model {
    val textureNames: List<String>

    constructor(textureName: String) : super(
            bottomFace = FullModelFace(textureName), topFace = EmptyModelFace(),
            leftFace = HalfModelFace(textureName), rightFace = HalfModelFace(textureName),
            backFace = HalfModelFace(textureName), frontFace = HalfModelFace(textureName),
            centerModel = PlaneModelSection(Double3.zero, Double3.zero, Double3(1, 1, 1), textureName, Double2.zero)
    ) {
        textureNames = listOf(textureName, textureName, textureName, textureName, textureName, textureName)
        id = registerModel(this)
    }

    constructor(topTextureName: String, sidesTextureName: String, bottomTextureName: String) : super(
            bottomFace = FullModelFace(bottomTextureName), topFace = EmptyModelFace(),
            leftFace = HalfModelFace(sidesTextureName), rightFace = HalfModelFace(sidesTextureName),
            backFace = HalfModelFace(sidesTextureName), frontFace = HalfModelFace(sidesTextureName),
            centerModel = PlaneModelSection(Double3.zero, Double3.zero, Double3(1, 1, 1), topTextureName, Double2.zero)
    ) {
        textureNames = listOf(
            topTextureName, bottomTextureName,
            sidesTextureName, sidesTextureName,
            sidesTextureName, sidesTextureName)
        id = registerModel(this)
    }

    override fun equals(other: Any?): Boolean {
        return other is SlabModel && textureNames == other.textureNames
    }
}