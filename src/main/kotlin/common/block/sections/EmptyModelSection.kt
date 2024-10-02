package common.block.sections

import common.block.VertexCollection
import java.io.Serializable

class EmptyModelSection() : ModelSection(), Serializable {
    override val vertexCollections = listOf<VertexCollection>()
}