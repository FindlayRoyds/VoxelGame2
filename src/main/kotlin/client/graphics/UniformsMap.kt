package client.graphics

import common.math.Int3
import org.joml.Matrix4d
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL41.*
import org.lwjgl.system.MemoryStack


class UniformsMap(private val programId: Int) {
    private val uniforms = HashMap<String, Int>()

    fun createUniform(uniformName: String) {
        val uniformLocation = glGetUniformLocation(programId, uniformName)
        if (uniformLocation < 0) {
            throw RuntimeException("Could not find uniform [$uniformName] in shader program [$programId]")
        }
        uniforms.put(uniformName, uniformLocation)
    }

    fun setUniform(uniformName: String, value: Matrix4d) {
        val location = uniforms[uniformName]
            ?: throw RuntimeException("Could not find uniform [$uniformName]")

        val buffer = BufferUtils.createFloatBuffer(16)
        value.get(buffer)

        glUniformMatrix4fv(location, false, buffer)
    }

    fun setUniform(uniformName: String, value: Array<Vector3f>) {
        val location = uniforms[uniformName]
            ?: throw java.lang.RuntimeException("Could not find uniform [$uniformName]")

        val bufferSize = value.size *  3
        MemoryStack.stackPush().use { stack ->
            val buffer = stack.mallocFloat(bufferSize)
            for (i in value.indices) {
                buffer.put(value[i].x).put(value[i].y).put(value[i].z)
            }
            buffer.flip()

            glUniform3fv(location, buffer)
        }
    }

    fun setUniform(uniformName: String, value: Int3) {
        val location = uniforms[uniformName]
            ?: throw java.lang.RuntimeException("Could not find uniform [$uniformName]")

        MemoryStack.stackPush().use { stack ->
            glUniform3i(location, value.x, value.y, value.z)
        }
    }

    fun setUniform(uniformName: String, value: Array<Vector2f>) {
        val location = uniforms[uniformName]
            ?: throw java.lang.RuntimeException("Could not find uniform [$uniformName]")

        val bufferSize = value.size *  2
        MemoryStack.stackPush().use { stack ->
            val buffer = stack.mallocFloat(bufferSize)
            for (i in value.indices) {
                buffer.put(value[i].x).put(value[i].y)
            }
            buffer.flip()

            glUniform2fv(location, buffer)
        }
    }

    fun setUniform(uniformName: String, value: Array<Int>) {
        val location = uniforms[uniformName]
            ?: throw java.lang.RuntimeException("Could not find uniform [$uniformName]")

        val bufferSize = value.size * 1
        MemoryStack.stackPush().use { stack ->
            val buffer = stack.mallocInt(bufferSize)
            for (i in value.indices) {
                buffer.put(value[i])
            }
            buffer.flip()

            glUniform1iv(location, buffer)
        }
    }
}