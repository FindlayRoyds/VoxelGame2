package common.block.sections

import common.block.VertexCollection
import common.math.Double2
import common.math.Double3
import java.io.Serializable

class PlaneCrossModelSection(translation: Double3, rotation: Double3, scale: Double3,
                             textureName: String, textureOffset: Double2) : ModelSection(), Serializable {
    override val vertexCollections = listOf<VertexCollection>(
        PlaneModelSection(translation, rotation + Double3(90, 0, 0), scale, textureName, Double2.zero),
        PlaneModelSection(translation, rotation + Double3(90, 0, 0), scale * Double3(-1, 1, 1), textureName, Double2.zero),
        PlaneModelSection(translation, rotation + Double3(90, 90, 0), scale, textureName, Double2.zero),
        PlaneModelSection(translation, rotation + Double3(90, 90, 0), scale * Double3(-1, 1, 1), textureName, Double2.zero),
    )
}