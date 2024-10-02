package common.block.sections

import common.block.VertexCollection
import java.io.Serializable

abstract class ModelSection: Serializable, VertexCollection() {
    protected abstract val vertexCollections: List<VertexCollection>

    override fun getVertices() = vertexCollections.flatMap { it.getVertices() }
}