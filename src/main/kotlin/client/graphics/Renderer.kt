package client.graphics

import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL41.*

class Renderer(width: Int, height: Int) {
    private val shaderProgram: ShaderProgram
    private val uniformsMap: UniformsMap
    private val projection = Projection(width, height)
    val camera = Camera()

    init {
        GL.createCapabilities()
        glEnable(GL_DEPTH_TEST);

        val shaderModuleDataList: MutableList<ShaderProgram.ShaderModuleData> = ArrayList()
        shaderModuleDataList.add(ShaderProgram.ShaderModuleData("/shaders/world.vert", GL_VERTEX_SHADER))
        shaderModuleDataList.add(ShaderProgram.ShaderModuleData("/shaders/world.frag", GL_FRAGMENT_SHADER))
        shaderProgram = ShaderProgram(shaderModuleDataList)

        uniformsMap = UniformsMap(shaderProgram.programId)
        uniformsMap.createUniform("projectionMatrix");
        uniformsMap.createUniform("viewMatrix")
    }

    fun cleanup() {
        shaderProgram.cleanup()
    }

    fun render(window: Window, scene: Scene) {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        glViewport(0, 0, window.width, window.height)

        shaderProgram.bind()

        uniformsMap.setUniform("projectionMatrix", projection.matrix)
        uniformsMap.setUniform("viewMatrix", camera.viewMatrix)

        scene.getMeshMap().values.forEach { mesh ->
            glBindVertexArray(mesh.vaoId)
            glDrawElements(GL_TRIANGLES, mesh.numVertices, GL_UNSIGNED_INT, 0)
        }

        glBindVertexArray(0);

        shaderProgram.unbind();
    }

    fun resize(width: Int, height: Int) {
        projection.update(width, height)
    }
}