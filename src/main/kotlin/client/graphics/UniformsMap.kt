package client.graphics

import org.joml.Matrix4f
import org.joml.Vector3f
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

    fun setUniform(uniformName: String, value: Matrix4f) {
        val location = uniforms[uniformName]
            ?: throw java.lang.RuntimeException("Could not find uniform [$uniformName]")

        MemoryStack.stackPush().use { stack ->
            glUniformMatrix4fv(location, false, value[stack.mallocFloat(16)])
        }
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
}