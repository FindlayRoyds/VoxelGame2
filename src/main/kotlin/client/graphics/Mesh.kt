package client.graphics

import org.lwjgl.opengl.GL30
import org.lwjgl.opengl.GL42.*
import org.lwjgl.system.MemoryUtil


class Mesh(meshData: MeshData) {
    val vaoId: Int
    val vboIdList: MutableList<Int>
    val numVertices = meshData.packedData.size
//    private var fence: Long? = null
//    private var _isTransferred: Boolean = false
//    val isTransferred: Boolean
//        get() {
//            if (_isTransferred) {
//                return true
//            } else if (fence != null) {
//                val result = glClientWaitSync(fence!!, GL_SYNC_FLUSH_COMMANDS_BIT, 0)
//                if (result == GL_ALREADY_SIGNALED || result == GL_CONDITION_SATISFIED) {
//                    _isTransferred = true
//                    glDeleteSync(fence!!)
//                    fence = null
//                }
//            }
//            return _isTransferred
//        }

    init {
        vboIdList = ArrayList()

        vaoId = glGenVertexArrays()
        glBindVertexArray(vaoId)

//        // Vertex Id VBO
//        var vboId = glGenBuffers()
//        vboIdList.add(vboId)
//        // val vertexIdsBuffer = stack.calloc(vertexIds.size)
//        // vertexIdsBuffer.put(0, vertexIds)
//        glBindBuffer(GL_ARRAY_BUFFER, vboId)
//        glBufferData(GL_ARRAY_BUFFER, meshData.vertexIds, GL_STATIC_DRAW)
//        glEnableVertexAttribArray(0)
//        glVertexAttribPointer(0, 1, GL_INT, false, 0, 0)
//
//        // Block Positions VBO
//        vboId = glGenBuffers()
//        vboIdList.add(vboId)
//        // val blockPositionsBuffer = IntArray(blockPositions.size)
//        // blockPositionsBuffer.put(0, blockPositions)
//        glBindBuffer(GL_ARRAY_BUFFER, vboId)
//        glBufferData(GL_ARRAY_BUFFER, meshData.blockPositions, GL_STATIC_DRAW)
//        glEnableVertexAttribArray(1)
//        glVertexAttribPointer(1, 1, GL_INT, false, 0, 0)
//
//        // Block Types VBO
//        vboId = glGenBuffers()
//        vboIdList.add(vboId)
//        // val blockPositionsBuffer = IntArray(blockPositions.size)
//        // blockPositionsBuffer.put(0, blockPositions)
//        glBindBuffer(GL_ARRAY_BUFFER, vboId)
//        glBufferData(GL_ARRAY_BUFFER, meshData.blockTypes, GL_STATIC_DRAW)
//        glEnableVertexAttribArray(2)
//        glVertexAttribPointer(2, 1, GL_INT, false, 0, 0)

        // Packed vertex Data VBO
        var vboId = glGenBuffers()
        vboIdList.add(vboId)
        // val vertexIdsBuffer = stack.calloc(vertexIds.size)
        // vertexIdsBuffer.put(0, vertexIds)

        val intBuffer = MemoryUtil.memAllocInt(meshData.packedData.size)
        intBuffer.put(meshData.packedData)
        intBuffer.flip()

        glBindBuffer(GL_ARRAY_BUFFER, vboId)
        glBufferData(GL_ARRAY_BUFFER, intBuffer, GL_STATIC_DRAW)
        glEnableVertexAttribArray(0)

        glVertexAttribIPointer(0, 1, GL_INT, 0, 0)

        // Light values
        vboId = glGenBuffers()
        vboIdList.add(vboId)
        // val blockPositionsBuffer = IntArray(blockPositions.size)
        // blockPositionsBuffer.put(0, blockPositions)
        glBindBuffer(GL_ARRAY_BUFFER, vboId)
        glBufferData(GL_ARRAY_BUFFER, meshData.lightValues, GL_STATIC_DRAW)
        glEnableVertexAttribArray(1)
        glVertexAttribPointer(1, 1, GL_FLOAT, false, 0, 0)

        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glBindVertexArray(0)

//        fence = glFenceSync(GL_SYNC_GPU_COMMANDS_COMPLETE, 0)
    }

    fun cleanup() {
        vboIdList.forEach(GL30::glDeleteBuffers);
        glDeleteVertexArrays(vaoId);
    }
}