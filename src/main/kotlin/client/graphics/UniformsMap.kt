package client.graphics

import org.joml.Matrix4f
import org.lwjgl.opengl.GL40.glGetUniformLocation
import org.lwjgl.opengl.GL40.glUniformMatrix4fv
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
        MemoryStack.stackPush().use { stack ->
            val location = uniforms[uniformName]
                ?: throw java.lang.RuntimeException("Could not find uniform [$uniformName]")
            glUniformMatrix4fv(location, false, value[stack.mallocFloat(16)])
        }
    }
}