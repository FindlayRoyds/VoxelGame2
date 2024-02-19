package client.graphics

import org.lwjgl.opengl.GL30.*
import org.lwjgl.opengl.GL30
import org.lwjgl.system.MemoryStack


class Mesh(positions: FloatArray, colors: FloatArray, indices: IntArray) {
    val vaoId: Int
    val vboIdList: MutableList<Int>
    val numVertices = indices.size

    init {
        MemoryStack.stackPush().use { stack ->
            vboIdList = ArrayList()
            vaoId = glGenVertexArrays()
            glBindVertexArray(vaoId)

            // Positions VBO
            var vboId = glGenBuffers()
            vboIdList.add(vboId)
            val positionsBuffer = stack.callocFloat(positions.size)
            positionsBuffer.put(0, positions)
            glBindBuffer(GL_ARRAY_BUFFER, vboId)
            glBufferData(GL_ARRAY_BUFFER, positionsBuffer, GL_STATIC_DRAW)
            glEnableVertexAttribArray(0)
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)

            // Color VBO
            vboId = glGenBuffers()
            vboIdList.add(vboId)
            val colorsBuffer = stack.callocFloat(colors.size)
            colorsBuffer.put(0, colors)
            colorsBuffer.put(0, colors)
            glBindBuffer(GL_ARRAY_BUFFER, vboId)
            glBufferData(GL_ARRAY_BUFFER, colorsBuffer, GL_STATIC_DRAW)
            glEnableVertexAttribArray(1)
            glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0)

            // Index VBO
            vboId = glGenBuffers()
            vboIdList.add(vboId)
            val indicesBuffer = stack.callocInt(indices.size)
            indicesBuffer.put(0, indices)
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId)
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW)

            glBindBuffer(GL_ARRAY_BUFFER, 0)
            glBindVertexArray(0)
        }
    }

    fun cleanup() {
        vboIdList.forEach(GL30::glDeleteBuffers);
        glDeleteVertexArrays(vaoId);
    }
}