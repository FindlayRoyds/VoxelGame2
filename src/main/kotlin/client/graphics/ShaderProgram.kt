package client.graphics

import common.utils.Utils
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30.*


class ShaderProgram(shaderModuleDataList: MutableList<ShaderModuleData>) {
    private val programId = glCreateProgram()

    init {
        if (programId == 0) {
            throw RuntimeException("Could not create Shader")
        }

        val shaderModules: MutableList<Int> = ArrayList()
        for ((shaderFile, shaderType) in shaderModuleDataList) {
            val shader = createShader(Utils().readFile(shaderFile), shaderType)
            shaderModules.add(shader)
        }

        link(shaderModules)
    }

    fun bind() {
        glUseProgram(programId)
    }

    fun cleanup() {
        unbind()
        if (programId != 0) {
            glDeleteProgram(programId)
        }
    }

    fun createShader(shaderCode: String, shaderType: Int): Int {
        val shaderId = glCreateShader(shaderType)
        if (shaderId == 0) {
            throw RuntimeException("Error creating shader. Type: $shaderType")
        }

        glShaderSource(shaderId, shaderCode)
        glCompileShader(shaderId)

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw RuntimeException("Error compiling shader code: ${glGetShaderInfoLog(shaderId, 1024)}")
        }

        glAttachShader(programId, shaderId)

        return shaderId
    }

    private fun link(shaderModules: List<Int>) {
        glLinkProgram(programId)
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw RuntimeException("Error linking shader code: ${GL20.glGetProgramInfoLog(programId, 1024)}")
        }

        shaderModules.forEach { s -> glDetachShader(programId, s) }
        shaderModules.forEach { shader: Int -> glDeleteShader(shader) }
    }

    fun unbind() {
        glUseProgram(0)
    }

    fun validate() {
        glValidateProgram(programId)
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            throw RuntimeException("Error validating shader code: ${GL20.glGetProgramInfoLog(programId, 1024)}")
        }
    }

    @JvmRecord
    data class ShaderModuleData(val shaderFile: String, val shaderType: Int)
}