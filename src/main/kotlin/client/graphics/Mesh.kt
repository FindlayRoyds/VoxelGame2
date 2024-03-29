package client.graphics

import org.lwjgl.opengl.GL30
import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryStack


class Mesh(vertexIds: IntArray, blockPositions: IntArray) {
    val vaoId: Int
    val vboIdList: MutableList<Int>
    val numVertices = vertexIds.size

    init {
        MemoryStack.stackPush().use { stack ->
            vboIdList = ArrayList()

            vaoId = glGenVertexArrays()
            glBindVertexArray(vaoId)

            // Vertex Id VBO
            var vboId = glGenBuffers()
            vboIdList.add(vboId)
            // val vertexIdsBuffer = stack.calloc(vertexIds.size)
            // vertexIdsBuffer.put(0, vertexIds)
            glBindBuffer(GL_ARRAY_BUFFER, vboId)
            glBufferData(GL_ARRAY_BUFFER, vertexIds, GL_STATIC_DRAW)
            glEnableVertexAttribArray(0)
            glVertexAttribPointer(0, 1, GL_INT, false, 0, 0)

            // Block Positions VBO
            vboId = glGenBuffers()
            vboIdList.add(vboId)
            // val blockPositionsBuffer = IntArray(blockPositions.size)
            // blockPositionsBuffer.put(0, blockPositions)
            glBindBuffer(GL_ARRAY_BUFFER, vboId)
            glBufferData(GL_ARRAY_BUFFER, blockPositions, GL_STATIC_DRAW)
            glEnableVertexAttribArray(1)
            glVertexAttribPointer(1, 1, GL_INT, false, 0, 0)

            glBindBuffer(GL_ARRAY_BUFFER, 0)
            glBindVertexArray(0)
        }
    }

    fun cleanup() {
        vboIdList.forEach(GL30::glDeleteBuffers);
        glDeleteVertexArrays(vaoId);
    }
}