package common.block.models

import common.block.Vertex
import common.block.VertexCollection
import common.block.faces.ModelFace
import common.math.Double3
import common.math.Int3
import java.io.Serializable

abstract class Model(
    bottomFace: ModelFace, topFace: ModelFace,
    leftFace: ModelFace, rightFace: ModelFace,
    backFace: ModelFace, frontFace: ModelFace,
    val centerModel: VertexCollection
): Serializable, VertexCollection() {
    val faces = hashMapOf<Int3, ModelFace>(
        Int3.down to bottomFace.rotate(Double3(180, 0, 0)).translate(Double3.down / 2),
        Int3.up to topFace.translate(Double3.up / 2),
        Int3.left to leftFace.rotate(Double3(90, -90, 0)).translate(Double3.left / 2),
        Int3.right to rightFace.rotate(Double3(90, 90, 0)).translate(Double3.right / 2),
        Int3.backwards to backFace.rotate(Double3(90, 0, 0)).translate(Double3.backwards / 2),
        Int3.forwards to frontFace.rotate(Double3(90, 180, 0)).translate(Double3.forwards / 2),
    )
    var id: Int? = null

    override fun getVertices(): List<Vertex> {
        val result = mutableListOf<Vertex>()
        for (face in faces.values) {
            result += face.getVertices().toList()
        }
        result += centerModel.getVertices()
        return result
    }

    abstract override fun equals(other: Any?): Boolean

    companion object {
        private val modelList = arrayListOf<Model>()

        fun registerModel(model: Model): Int {
            for ((index, otherModel) in modelList.withIndex()) {
                if (otherModel == model) {
                    return index
                }
            }

            modelList.add(model)
            return modelList.size - 1
        }
    }
}