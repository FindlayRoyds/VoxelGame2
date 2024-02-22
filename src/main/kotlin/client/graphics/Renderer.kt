package client.graphics

import org.joml.Vector3f
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
        glEnable(GL_CULL_FACE)
        glCullFace(GL_BACK)

        val shaderModuleDataList: MutableList<ShaderProgram.ShaderModuleData> = ArrayList()
        shaderModuleDataList.add(ShaderProgram.ShaderModuleData("/shaders/world.vert", GL_VERTEX_SHADER))
        shaderModuleDataList.add(ShaderProgram.ShaderModuleData("/shaders/world.frag", GL_FRAGMENT_SHADER))
        shaderProgram = ShaderProgram(shaderModuleDataList)
        shaderProgram.bind()

        uniformsMap = UniformsMap(shaderProgram.programId)
        uniformsMap.createUniform("projectionMatrix");
        uniformsMap.createUniform("viewMatrix")
        uniformsMap.createUniform("vertexDataArray")

        val vertexDataArray = arrayOf(
            Vector3f(-0.5f,  0.5f, -1.0f),
            Vector3f(-0.5f, -0.5f, -1.0f),
            Vector3f(0.5f,  0.5f, -1.0f),
            Vector3f(0.5f, -0.5f, -1.0f),
        )
        uniformsMap.setUniform("vertexDataArray", vertexDataArray)
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
            glDrawArrays(GL_TRIANGLES, 0, mesh.numVertices)
        }

        glBindVertexArray(0);

        shaderProgram.unbind();
    }

    fun resize(width: Int, height: Int) {
        projection.update(width, height)
    }
}