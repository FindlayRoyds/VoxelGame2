package common.block.segments

import common.block.Vertex
import common.math.Double2
import common.math.Double3
import java.io.Serializable

class PlaneModelSection(translation: Double3, rotation: Double3, scale: Double3,
                        textureName: String, textureOffset: Double2) : ModelSection(), Serializable {
    override val vertexCollections = listOf(
        Vertex(Double3(1, 0, -1) / 2, Double3.up, textureName, Double2(1, 1) + textureOffset),
        Vertex(Double3(-1, 0, -1) / 2, Double3.up, textureName, Double2(0, 1) + textureOffset),
        Vertex(Double3(1, 0, 1) / 2, Double3.up, textureName, Double2(1, 0) + textureOffset),
        Vertex(Double3(-1, 0, 1) / 2, Double3.up, textureName, Double2(0, 0) + textureOffset),
        Vertex(Double3(1, 0, 1) / 2, Double3.up, textureName, Double2(1, 0) + textureOffset),
        Vertex(Double3(-1, 0, -1) / 2, Double3.up, textureName, Double2(0, 1) + textureOffset),
    )

    init {
        for (vertex in vertexCollections) {
            vertex.scale(scale).rotate(rotation).translate(translation)
        }
    }
}