package common.block.faces

import common.block.Vertex
import common.block.sections.ModelSection
import common.math.Double3
import java.io.Serializable

abstract class ModelFace: Serializable {
    abstract val segments: List<ModelSection>
    private val _vertices by lazy { segments.flatMap { it.getVertices() } }

    abstract fun isCulled(otherFace: ModelFace): Boolean

    fun translate(translation: Double3): ModelFace {
        for (vertex in getVertices()) {
            vertex.translate(translation)
        }
        return this
    }

    fun applyScale(scale: Double3): ModelFace {
        for (vertex in getVertices()) {
            vertex.scale(scale)
        }
        return this
    }

    fun rotate(rotationDegrees: Double3): ModelFace {
        for (vertex in getVertices()) {
            vertex.rotate(rotationDegrees)
        }
        return this
    }

    fun getVertices(): List<Vertex> = _vertices
}