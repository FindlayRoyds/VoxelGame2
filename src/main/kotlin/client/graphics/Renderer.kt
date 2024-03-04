package client.graphics

import common.math.Float3
import common.world.World
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL41.*

class Renderer(width: Int, height: Int) {
    private val shaderProgram: ShaderProgram
    private val uniformsMap: UniformsMap
    private val projection = Projection(width, height)
    val camera = Camera()
    var isWireframe = false

    init {
        GL.createCapabilities()
        glEnable(GL_DEPTH_TEST)
        glEnable(GL_CULL_FACE)
        glCullFace(GL_BACK)
        glEnable(GL_MULTISAMPLE)
        val shaderModuleDataList: MutableList<ShaderProgram.ShaderModuleData> = ArrayList()
        shaderModuleDataList.add(ShaderProgram.ShaderModuleData("/shaders/world.vert", GL_VERTEX_SHADER))
        shaderModuleDataList.add(ShaderProgram.ShaderModuleData("/shaders/world.frag", GL_FRAGMENT_SHADER))
        shaderProgram = ShaderProgram(shaderModuleDataList)
        shaderProgram.bind()

        uniformsMap = UniformsMap(shaderProgram.programId)
        uniformsMap.createUniform("projectionMatrix");
        uniformsMap.createUniform("viewMatrix")
        uniformsMap.createUniform("vertexDataArray")
        uniformsMap.createUniform("chunkPosition")

        val vertexDataArray = arrayOf(
            Float3(-0.5f, -0.5f, -0.5f,),
            Float3(0.5f, -0.5f, -0.5f,),
            Float3(0.5f,  0.5f, -0.5f,),
            Float3(0.5f,  0.5f, -0.5f,),
            Float3(-0.5f,  0.5f, -0.5f,),
            Float3(-0.5f, -0.5f, -0.5f,),

            Float3(-0.5f, -0.5f,  0.5f,),
            Float3(0.5f, -0.5f,  0.5f,),
            Float3(0.5f,  0.5f,  0.5f,),
            Float3(0.5f,  0.5f,  0.5f,),
            Float3(-0.5f,  0.5f,  0.5f,),
            Float3(-0.5f, -0.5f,  0.5f,),

            Float3(-0.5f,  0.5f,  0.5f,),
            Float3(-0.5f,  0.5f, -0.5f,),
            Float3(-0.5f, -0.5f, -0.5f,),
            Float3(-0.5f, -0.5f, -0.5f,),
            Float3(-0.5f, -0.5f,  0.5f,),
            Float3(-0.5f,  0.5f,  0.5f,),

            Float3(0.5f,  0.5f,  0.5f,),
            Float3(0.5f,  0.5f, -0.5f,),
            Float3(0.5f, -0.5f, -0.5f,),
            Float3(0.5f, -0.5f, -0.5f,),
            Float3(0.5f, -0.5f,  0.5f,),
            Float3(0.5f,  0.5f,  0.5f,),

            Float3(-0.5f, -0.5f, -0.5f,),
            Float3(0.5f, -0.5f, -0.5f,),
            Float3(0.5f, -0.5f,  0.5f,),
            Float3(0.5f, -0.5f,  0.5f,),
            Float3(-0.5f, -0.5f,  0.5f,),
            Float3(-0.5f, -0.5f, -0.5f,),

            Float3(-0.5f,  0.5f, -0.5f,),
            Float3(0.5f,  0.5f, -0.5f,),
            Float3(0.5f,  0.5f,  0.5f,),
            Float3(0.5f,  0.5f,  0.5f,),
            Float3(-0.5f,  0.5f,  0.5f,),
            Float3(-0.5f,  0.5f, -0.5f,),
        )
        uniformsMap.setUniform("vertexDataArray", vertexDataArray)
    }

    fun cleanup() {
        shaderProgram.cleanup()
    }

    fun render(window: Window, world: World) {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        glViewport(0, 0, window.width, window.height)

        shaderProgram.bind()

        uniformsMap.setUniform("projectionMatrix", projection.matrix)
        uniformsMap.setUniform("viewMatrix", camera.viewMatrix)

        world.chunkManager.getLoadedChunks().forEach { chunk ->
            val chunkMesh = chunk.mesh
            uniformsMap.setUniform("chunkPosition", chunk.chunkPosition)
            if (chunkMesh != null) {
                glBindVertexArray(chunkMesh.vaoId)
                glDrawArrays(GL_TRIANGLES, 0, chunkMesh.numVertices)
            }
        }

        glBindVertexArray(0);

        shaderProgram.unbind();
    }

    fun resize(width: Int, height: Int) {
        projection.update(width, height)
    }

    fun toggleWireframe() {
        isWireframe = !isWireframe
        if (isWireframe) {
            glPolygonMode( GL_FRONT_AND_BACK, GL_LINE );
        } else {
            glPolygonMode( GL_FRONT_AND_BACK, GL_FILL );
        }
    }
}