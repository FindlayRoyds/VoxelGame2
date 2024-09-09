package common.block.faces

import common.block.segments.ModelSection
import common.math.Double3
import java.io.Serializable

abstract class ModelFace: Serializable {
    abstract val segments: List<ModelSection>

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

    fun getVertices() = segments.flatMap { it.getVertices() }
}