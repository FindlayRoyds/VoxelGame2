package common.block.modelSegments

import common.block.Vertex
import java.io.Serializable

class EmptyModelSegment() : ModelSegment(), Serializable {
    override val vertices = listOf<Vertex>()
}