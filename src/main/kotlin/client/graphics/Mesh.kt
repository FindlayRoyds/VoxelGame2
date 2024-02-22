package client.graphics

import org.lwjgl.opengl.GL30
import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryStack


class Mesh(positions: ByteArray, val numVertices: Int) {
    val vaoId: Int
    val vboIdList: MutableList<Int>

    init {
        MemoryStack.stackPush().use { stack ->
            vboIdList = ArrayList()

            vaoId = glGenVertexArrays()
            glBindVertexArray(vaoId)

            // Positions VBO
            val vboId = glGenBuffers()
            vboIdList.add(vboId)
            val positionsBuffer = stack.calloc(positions.size)
            positionsBuffer.put(0, positions)
            glBindBuffer(GL_ARRAY_BUFFER, vboId)
            glBufferData(GL_ARRAY_BUFFER, positionsBuffer, GL_STATIC_DRAW)
            glEnableVertexAttribArray(0)
            glVertexAttribPointer(0, 1, GL_BYTE, false, 0, 0)

            glBindBuffer(GL_ARRAY_BUFFER, 0)
            glBindVertexArray(0)
        }
    }

    fun cleanup() {
        vboIdList.forEach(GL30::glDeleteBuffers);
        glDeleteVertexArrays(vaoId);
    }
}