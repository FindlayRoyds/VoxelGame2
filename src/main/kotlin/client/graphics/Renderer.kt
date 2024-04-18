package client.graphics

import common.world.World
import org.joml.Vector2f
import org.joml.Vector3f
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
        uniformsMap.createUniform("textureDataArray")
        uniformsMap.createUniform("textureIndexArray")
        uniformsMap.createUniform("chunkPosition")

        glClearColor(0.5f, 0.7f, 1.0f, 1.0f)

        val vertexDataArray = arrayOf(
            // Front face
            Vector3f(-0.5f, -0.5f, -0.5f,),
            Vector3f(0.5f, -0.5f, -0.5f,),
            Vector3f(0.5f,  0.5f, -0.5f,),
            Vector3f(0.5f,  0.5f, -0.5f,),
            Vector3f(-0.5f,  0.5f, -0.5f,),
            Vector3f(-0.5f, -0.5f, -0.5f,),

            // Back face
            Vector3f(-0.5f, -0.5f,  0.5f,),
            Vector3f(0.5f, -0.5f,  0.5f,),
            Vector3f(0.5f,  0.5f,  0.5f,),
            Vector3f(0.5f,  0.5f,  0.5f,),
            Vector3f(-0.5f,  0.5f,  0.5f,),
            Vector3f(-0.5f, -0.5f,  0.5f,),

            // Left face
            Vector3f(-0.5f,  0.5f,  0.5f,),
            Vector3f(-0.5f,  0.5f, -0.5f,),
            Vector3f(-0.5f, -0.5f, -0.5f,),
            Vector3f(-0.5f, -0.5f, -0.5f,),
            Vector3f(-0.5f, -0.5f,  0.5f,),
            Vector3f(-0.5f,  0.5f,  0.5f,),

            // Right face
            Vector3f(0.5f,  0.5f,  0.5f,),
            Vector3f(0.5f,  0.5f, -0.5f,),
            Vector3f(0.5f, -0.5f, -0.5f,),
            Vector3f(0.5f, -0.5f, -0.5f,),
            Vector3f(0.5f, -0.5f,  0.5f,),
            Vector3f(0.5f,  0.5f,  0.5f,),

            // Bottom face
            Vector3f(-0.5f, -0.5f, -0.5f,),
            Vector3f(0.5f, -0.5f, -0.5f,),
            Vector3f(0.5f, -0.5f,  0.5f,),
            Vector3f(0.5f, -0.5f,  0.5f,),
            Vector3f(-0.5f, -0.5f,  0.5f,),
            Vector3f(-0.5f, -0.5f, -0.5f,),

            // Top face
            Vector3f(-0.5f,  0.5f, -0.5f,),
            Vector3f(0.5f,  0.5f, -0.5f,),
            Vector3f(0.5f,  0.5f,  0.5f,),
            Vector3f(0.5f,  0.5f,  0.5f,),
            Vector3f(-0.5f,  0.5f,  0.5f,),
            Vector3f(-0.5f,  0.5f, -0.5f,),

            Vector3f(-0.5f, -0.5f, -0.5f,),
            Vector3f(0.5f, -0.5f, -0.5f,),
            Vector3f(0.5f,  0.5f, -0.5f,),
            Vector3f(0.5f,  0.5f, -0.5f,),
            Vector3f(-0.5f,  0.5f, -0.5f,),
            Vector3f(-0.5f, -0.5f, -0.5f,),

            // Back face
            Vector3f(-0.5f, -0.5f,  0.5f,),
            Vector3f(0.5f, -0.5f,  0.5f,),
            Vector3f(0.5f,  0.5f,  0.5f,),
            Vector3f(0.5f,  0.5f,  0.5f,),
            Vector3f(-0.5f,  0.5f,  0.5f,),
            Vector3f(-0.5f, -0.5f,  0.5f,),

            // Left face
            Vector3f(-0.5f,  0.5f,  0.5f,),
            Vector3f(-0.5f,  0.5f, -0.5f,),
            Vector3f(-0.5f, -0.5f, -0.5f,),
            Vector3f(-0.5f, -0.5f, -0.5f,),
            Vector3f(-0.5f, -0.5f,  0.5f,),
            Vector3f(-0.5f,  0.5f,  0.5f,),

            // Right face
            Vector3f(0.5f,  0.5f,  0.5f,),
            Vector3f(0.5f,  0.5f, -0.5f,),
            Vector3f(0.5f, -0.5f, -0.5f,),
            Vector3f(0.5f, -0.5f, -0.5f,),
            Vector3f(0.5f, -0.5f,  0.5f,),
            Vector3f(0.5f,  0.5f,  0.5f,),

            // Bottom face
            Vector3f(-0.5f, -0.5f, -0.5f,),
            Vector3f(0.5f, -0.5f, -0.5f,),
            Vector3f(0.5f, -0.5f,  0.5f,),
            Vector3f(0.5f, -0.5f,  0.5f,),
            Vector3f(-0.5f, -0.5f,  0.5f,),
            Vector3f(-0.5f, -0.5f, -0.5f,),

            // Top face
            Vector3f(-0.5f,  0.5f, -0.5f,),
            Vector3f(0.5f,  0.5f, -0.5f,),
            Vector3f(0.5f,  0.5f,  0.5f,),
            Vector3f(0.5f,  0.5f,  0.5f,),
            Vector3f(-0.5f,  0.5f,  0.5f,),
            Vector3f(-0.5f,  0.5f, -0.5f,),

            Vector3f(-0.5f, -0.5f, -0.5f,),
            Vector3f(0.5f, -0.5f, -0.5f,),
            Vector3f(0.5f,  0.5f, -0.5f,),
            Vector3f(0.5f,  0.5f, -0.5f,),
            Vector3f(-0.5f,  0.5f, -0.5f,),
            Vector3f(-0.5f, -0.5f, -0.5f,),

            // Back face
            Vector3f(-0.5f, -0.5f,  0.5f,),
            Vector3f(0.5f, -0.5f,  0.5f,),
            Vector3f(0.5f,  0.5f,  0.5f,),
            Vector3f(0.5f,  0.5f,  0.5f,),
            Vector3f(-0.5f,  0.5f,  0.5f,),
            Vector3f(-0.5f, -0.5f,  0.5f,),

            // Left face
            Vector3f(-0.5f,  0.5f,  0.5f,),
            Vector3f(-0.5f,  0.5f, -0.5f,),
            Vector3f(-0.5f, -0.5f, -0.5f,),
            Vector3f(-0.5f, -0.5f, -0.5f,),
            Vector3f(-0.5f, -0.5f,  0.5f,),
            Vector3f(-0.5f,  0.5f,  0.5f,),

            // Right face
            Vector3f(0.5f,  0.5f,  0.5f,),
            Vector3f(0.5f,  0.5f, -0.5f,),
            Vector3f(0.5f, -0.5f, -0.5f,),
            Vector3f(0.5f, -0.5f, -0.5f,),
            Vector3f(0.5f, -0.5f,  0.5f,),
            Vector3f(0.5f,  0.5f,  0.5f,),

            // Bottom face
            Vector3f(-0.5f, -0.5f, -0.5f,),
            Vector3f(0.5f, -0.5f, -0.5f,),
            Vector3f(0.5f, -0.5f,  0.5f,),
            Vector3f(0.5f, -0.5f,  0.5f,),
            Vector3f(-0.5f, -0.5f,  0.5f,),
            Vector3f(-0.5f, -0.5f, -0.5f,),

            // Top face
            Vector3f(-0.5f,  0.5f, -0.5f,),
            Vector3f(0.5f,  0.5f, -0.5f,),
            Vector3f(0.5f,  0.5f,  0.5f,),
            Vector3f(0.5f,  0.5f,  0.5f,),
            Vector3f(-0.5f,  0.5f,  0.5f,),
            Vector3f(-0.5f,  0.5f, -0.5f,),
        )

        val textureDataArray = arrayOf(
            // Front face
            Vector2f(0.0f, 1.0f),
            Vector2f(1.0f, 1.0f),
            Vector2f(1.0f, 0.0f),
            Vector2f(1.0f, 0.0f),
            Vector2f(0.0f, 0.0f),
            Vector2f(0.0f, 1.0f),

            // Back face
            Vector2f(0.0f, 1.0f),
            Vector2f(1.0f, 1.0f),
            Vector2f(1.0f, 0.0f),
            Vector2f(1.0f, 0.0f),
            Vector2f(0.0f, 0.0f),
            Vector2f(0.0f, 1.0f),

            // Left face (flipped vertically)
            Vector2f(0.0f, 0.0f),
            Vector2f(1.0f, 0.0f),
            Vector2f(1.0f, 1.0f),
            Vector2f(1.0f, 1.0f),
            Vector2f(0.0f, 1.0f),
            Vector2f(0.0f, 0.0f),

            // Right face (flipped vertically)
            Vector2f(0.0f, 0.0f),
            Vector2f(1.0f, 0.0f),
            Vector2f(1.0f, 1.0f),
            Vector2f(1.0f, 1.0f),
            Vector2f(0.0f, 1.0f),
            Vector2f(0.0f, 0.0f),

            // Bottom face
            Vector2f(0.0f, 1.0f),
            Vector2f(1.0f, 1.0f),
            Vector2f(1.0f, 0.0f),
            Vector2f(1.0f, 0.0f),
            Vector2f(0.0f, 0.0f),
            Vector2f(0.0f, 1.0f),

            // Top face
            Vector2f(0.0f, 1.0f),
            Vector2f(1.0f, 1.0f),
            Vector2f(1.0f, 0.0f),
            Vector2f(1.0f, 0.0f),
            Vector2f(0.0f, 0.0f),
            Vector2f(0.0f, 1.0f),

            Vector2f(0.0f, 1.0f),
            Vector2f(1.0f, 1.0f),
            Vector2f(1.0f, 0.0f),
            Vector2f(1.0f, 0.0f),
            Vector2f(0.0f, 0.0f),
            Vector2f(0.0f, 1.0f),

            // Back face
            Vector2f(1.0f, 1.0f),
            Vector2f(0.0f, 1.0f),
            Vector2f(0.0f, 0.0f),
            Vector2f(0.0f, 0.0f),
            Vector2f(1.0f, 0.0f),
            Vector2f(1.0f, 1.0f),

            // Left face (flipped vertically)
            Vector2f(0.0f, 0.0f),
            Vector2f(1.0f, 0.0f),
            Vector2f(1.0f, 1.0f),
            Vector2f(1.0f, 1.0f),
            Vector2f(0.0f, 1.0f),
            Vector2f(0.0f, 0.0f),

            // Right face (flipped vertically)
            Vector2f(1.0f, 0.0f),
            Vector2f(0.0f, 0.0f),
            Vector2f(0.0f, 1.0f),
            Vector2f(0.0f, 1.0f),
            Vector2f(1.0f, 1.0f),
            Vector2f(1.0f, 0.0f),

            // Bottom face
            Vector2f(0.0f, 1.0f),
            Vector2f(1.0f, 1.0f),
            Vector2f(1.0f, 0.0f),
            Vector2f(1.0f, 0.0f),
            Vector2f(0.0f, 0.0f),
            Vector2f(0.0f, 1.0f),

            // Top face
            Vector2f(0.0f, 1.0f),
            Vector2f(1.0f, 1.0f),
            Vector2f(1.0f, 0.0f),
            Vector2f(1.0f, 0.0f),
            Vector2f(0.0f, 0.0f),
            Vector2f(0.0f, 1.0f),

            Vector2f(0.0f, 1.0f),
            Vector2f(1.0f, 1.0f),
            Vector2f(1.0f, 0.0f),
            Vector2f(1.0f, 0.0f),
            Vector2f(0.0f, 0.0f),
            Vector2f(0.0f, 1.0f),

            // Back face
            Vector2f(1.0f, 1.0f),
            Vector2f(0.0f, 1.0f),
            Vector2f(0.0f, 0.0f),
            Vector2f(0.0f, 0.0f),
            Vector2f(1.0f, 0.0f),
            Vector2f(1.0f, 1.0f),

            // Left face (flipped vertically)
            Vector2f(0.0f, 0.0f),
            Vector2f(1.0f, 0.0f),
            Vector2f(1.0f, 1.0f),
            Vector2f(1.0f, 1.0f),
            Vector2f(0.0f, 1.0f),
            Vector2f(0.0f, 0.0f),

            // Right face (flipped vertically)
            Vector2f(1.0f, 0.0f),
            Vector2f(0.0f, 0.0f),
            Vector2f(0.0f, 1.0f),
            Vector2f(0.0f, 1.0f),
            Vector2f(1.0f, 1.0f),
            Vector2f(1.0f, 0.0f),

            // Bottom face
            Vector2f(0.0f, 1.0f),
            Vector2f(1.0f, 1.0f),
            Vector2f(1.0f, 0.0f),
            Vector2f(1.0f, 0.0f),
            Vector2f(0.0f, 0.0f),
            Vector2f(0.0f, 1.0f),

            // Top face
            Vector2f(0.0f, 1.0f),
            Vector2f(1.0f, 1.0f),
            Vector2f(1.0f, 0.0f),
            Vector2f(1.0f, 0.0f),
            Vector2f(0.0f, 0.0f),
            Vector2f(0.0f, 1.0f),
        )

        val textureIndexArray = arrayOf(
            0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0,
            3, 3, 3, 3, 3, 3,
            1, 1, 1, 1, 1, 1,

            2, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2,

            4, 4, 4, 4, 4, 4,
            4, 4, 4, 4, 4, 4,
            4, 4, 4, 4, 4, 4,
            4, 4, 4, 4, 4, 4,
            4, 4, 4, 4, 4, 4,
            4, 4, 4, 4, 4, 4,
            )

        uniformsMap.setUniform("vertexDataArray", vertexDataArray)
        uniformsMap.setUniform("textureDataArray", textureDataArray)
        uniformsMap.setUniform("textureIndexArray", textureIndexArray)

        val textureArrayId = Texture.loadTextures(
            listOf(
                "src/main/resources/textures/blocks/PodzolSide.png",
                "src/main/resources/textures/blocks/Podzol-Top.png",
                "src/main/resources/textures/blocks/Stone-block-texture.png",
                "src/main/resources/textures/blocks/Dirt-block-texture.png",
            )
        )
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D_ARRAY, textureArrayId)
        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR)
        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
    }

    fun cleanup() {
        shaderProgram.cleanup()
    }

    fun render(window: Window, world: World) {
        glFinish()

        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        glViewport(0, 0, window.width, window.height)

        shaderProgram.bind()

        uniformsMap.setUniform("projectionMatrix", projection.matrix)
        uniformsMap.setUniform("viewMatrix", camera.viewMatrix)

        // world.chunkManager.chunksLock.lock()
        world.chunkManager.getLoadedChunks().forEach { chunk ->
            val chunkMesh = chunk.mesh
            uniformsMap.setUniform("chunkPosition", chunk.chunkPosition)
            if (chunkMesh != null) {
                glBindVertexArray(chunkMesh.vaoId)
                glDrawArrays(GL_TRIANGLES, 0, chunkMesh.numVertices)
            }
        }
        // world.chunkManager.chunksLock.unlock()

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