package common.block.segments

import common.block.VertexCollection
import java.io.Serializable

class EmptyModelSection() : ModelSection(), Serializable {
    override val vertexCollections = listOf<VertexCollection>()
}