package common.block.modelSegments

import common.block.Vertex
import java.io.Serializable

abstract class ModelSegment: Serializable {
    abstract val vertices: List<Vertex>
}