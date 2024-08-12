package common.utils

import java.io.Serializable
import kotlin.math.ceil

class PackedArray(val numElements: Int, val bitsPerElement: Int): Serializable {
    val indices: IntRange
        get() {
            return IntRange(0, numElements - 1)
        }
    private val bitsPerInt = 32
    private val elementsPerInt = bitsPerInt / bitsPerElement
    private val intArraySize = ceil(numElements.toDouble() / elementsPerInt).toInt()
    private val intArray = IntArray(intArraySize)

    init {
        require(bitsPerElement in 1..bitsPerInt) {
            "bitsPerElement must be between 1 and $bitsPerInt"
        }
    }

    operator fun get(index: Int): Int {
        require(index in 0..<(elementsPerInt * intArray.size)) {
            "Index out of bounds: $index"
        }
        val intArrayIndex = index / elementsPerInt
        val bitOffset = (index % elementsPerInt) * bitsPerElement
        val data = intArray[intArrayIndex]
        return (data shr bitOffset) and ((1 shl bitsPerElement) - 1)
    }

    operator fun set(index: Int, value: Int) {
        require(index in 0..<(elementsPerInt * intArray.size)) {
            "Index out of bounds: $index"
        }
        require(value in 0..<(1 shl bitsPerElement)) {
            "Value out of bounds: $value (must fit within $bitsPerElement bits)"
        }
        val intArrayIndex = index / elementsPerInt
        val bitOffset = (index % elementsPerInt) * bitsPerElement

        val mask = ((1 shl bitsPerElement) - 1) shl bitOffset
        intArray[intArrayIndex] = (intArray[intArrayIndex] and mask.inv()) or ((value shl bitOffset) and mask)
    }

    fun all(value: Int): Boolean {
        for (i in intArray.indices) {
            if (intArray[i] != value) {
                return false
            }
        }

        return true
    }
}
