package client.graphics

import org.lwjgl.opengl.GL30.*


class SceneRenderer {
    val shaderProgram: ShaderProgram

    init {
        val shaderModuleDataList: MutableList<ShaderProgram.ShaderModuleData> = ArrayList()
        shaderModuleDataList.add(ShaderProgram.ShaderModuleData("/shaders/world.vert", GL_VERTEX_SHADER))
        shaderModuleDataList.add(ShaderProgram.ShaderModuleData("/shaders/world.frag", GL_FRAGMENT_SHADER))
        shaderProgram = ShaderProgram(shaderModuleDataList)
    }

    fun cleanup() {
        shaderProgram.cleanup()
    }

    fun render(scene: Scene) {
        shaderProgram.bind()

        scene.getMeshMap().values.forEach { mesh ->
            glBindVertexArray(mesh.vaoId)
            glDrawElements(GL_TRIANGLES, mesh.numVertices, GL_UNSIGNED_INT, 0)
        }

        glBindVertexArray(0);

        shaderProgram.unbind();
    }
}