package common.block.models

import common.block.faces.EmptyModelFace
import common.block.segments.EmptyModelSection

class EmptyModel: Model(
    EmptyModelFace(), EmptyModelFace(),
    EmptyModelFace(), EmptyModelFace(),
    EmptyModelFace(), EmptyModelFace(),
    EmptyModelSection()
) {
    init {
        id = registerModel(this)
    }

    override fun equals(other: Any?): Boolean {
        return other is EmptyModel
    }

//    override fun hashCode(): Int {
//        return javaClass::hashCode()
//    }
}