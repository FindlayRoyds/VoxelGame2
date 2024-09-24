package client.graphics.renderers

import client.Client
import client.graphics.*
import common.Config
import common.GameEngineProvider
import common.block.blocks.Block
import common.block.models.Model
import common.math.Double3
import common.math.Int3
import common.world.World
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL41.*

class MainRenderer(window: Window, width: Int, height: Int) {
    private val blockShaderProgram: ShaderProgram
    private val selectionBoxShaderProgram: ShaderProgram
    private val blockUniformsMap: UniformsMap
    private val selectionBoxUniformsMap: UniformsMap
    private val projection = Projection(width, height)
    private var isWireframe = false
    val camera = Camera()

    private val selectionBoxVaoId: Int
    private val selectionBoxVboId: Int
    var selectionBoxPosition: Int3? = null

    private val renderStartTime = System.nanoTime()

    private val modelMap = HashMap<Model, Int>()

    var vertexPositionArray = arrayOf<Vector3f>()
    var textureCoordArray = arrayOf<Vector2f>()
    var normalVectorArray = arrayOf<Vector3f>()
    var textureIndexArray = arrayOf<Int>()


    private val blockDataArraysElementSize = 42

    private var _client: Client? = null
    private val client: Client
        get() {
            if (_client == null)
                _client = GameEngineProvider.getGameEngine() as Client
            return _client!!
        }

    init {
        GL.createCapabilities()
        glEnable(GL_DEPTH_TEST)
        glEnable(GL_CULL_FACE)
        glCullFace(GL_BACK)
        glEnable(GL_MULTISAMPLE)

        val blockShaderModuleDataList: MutableList<ShaderProgram.ShaderModuleData> = ArrayList()
        blockShaderModuleDataList.add(ShaderProgram.ShaderModuleData("/shaders/block.vert", GL_VERTEX_SHADER))
        blockShaderModuleDataList.add(ShaderProgram.ShaderModuleData("/shaders/block.frag", GL_FRAGMENT_SHADER))
        blockShaderProgram = ShaderProgram(blockShaderModuleDataList)
        blockShaderProgram.bind()
        val selectionBoxShaderModuleDataList: MutableList<ShaderProgram.ShaderModuleData> = ArrayList()
        selectionBoxShaderModuleDataList.add(
            ShaderProgram.ShaderModuleData(
                "/shaders/selection.vert",
                GL_VERTEX_SHADER
            )
        )
        selectionBoxShaderModuleDataList.add(
            ShaderProgram.ShaderModuleData(
                "/shaders/selection.frag",
                GL_FRAGMENT_SHADER
            )
        )
        selectionBoxShaderProgram = ShaderProgram(selectionBoxShaderModuleDataList)
//        selectionBoxShaderProgram.bind()

        blockUniformsMap = UniformsMap(blockShaderProgram.programId)
        blockUniformsMap.createUniform("projectionMatrix");
        blockUniformsMap.createUniform("viewMatrix")
        blockUniformsMap.createUniform("vertexPositionArray")
        blockUniformsMap.createUniform("textureCoordArray")
        blockUniformsMap.createUniform("textureIndexArray")
        blockUniformsMap.createUniform("normalVectorArray")
        blockUniformsMap.createUniform("chunkPosition")
        blockUniformsMap.createUniform("chunkVisibility")
        blockUniformsMap.createUniform("time")

        selectionBoxUniformsMap = UniformsMap(selectionBoxShaderProgram.programId)
        selectionBoxUniformsMap.createUniform("projectionMatrix");
        selectionBoxUniformsMap.createUniform("viewMatrix")
        selectionBoxUniformsMap.createUniform("selectionBoxPosition")

        glClearColor(0.5f, 0.7f, 1.0f, 1.0f)

        generateModelData()

        blockUniformsMap.setUniform("vertexPositionArray", vertexPositionArray)
        blockUniformsMap.setUniform("textureCoordArray", textureCoordArray)
        blockUniformsMap.setUniform("textureIndexArray", textureIndexArray)
        blockUniformsMap.setUniform("normalVectorArray", normalVectorArray)

        val textureArrayId = Texture.loadTextures(
            listOf(
                "src/main/resources/textures/blocks/PodzolSide.png",
                "src/main/resources/textures/blocks/Podzol.png",
                "src/main/resources/textures/blocks/oak-side-shaded.png",
                "src/main/resources/textures/blocks/stone.png",
                "src/main/resources/textures/blocks/dirt.png",
                "src/main/resources/textures/blocks/spruce_log_top.png"
            )
        )
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D_ARRAY, textureArrayId)
        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR)
        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT)

        val cubeVertices = floatArrayOf(
            -0.5f, -0.5f, -0.5f,  0.5f, -0.5f, -0.5f,  // Line 1
            0.5f, -0.5f, -0.5f,  0.5f,  0.5f, -0.5f,  // Line 2
            0.5f,  0.5f, -0.5f, -0.5f,  0.5f, -0.5f,  // Line 3
            -0.5f,  0.5f, -0.5f, -0.5f, -0.5f, -0.5f,  // Line 4
            -0.5f, -0.5f,  0.5f,  0.5f, -0.5f,  0.5f,  // Line 5
            0.5f, -0.5f,  0.5f,  0.5f,  0.5f,  0.5f,  // Line 6
            0.5f,  0.5f,  0.5f, -0.5f,  0.5f,  0.5f,  // Line 7
            -0.5f,  0.5f,  0.5f, -0.5f, -0.5f,  0.5f,  // Line 8
            -0.5f, -0.5f, -0.5f, -0.5f, -0.5f,  0.5f,  // Line 9
            0.5f, -0.5f, -0.5f,  0.5f, -0.5f,  0.5f,  // Line 10
            0.5f,  0.5f, -0.5f,  0.5f,  0.5f,  0.5f,  // Line 11
            -0.5f,  0.5f, -0.5f, -0.5f,  0.5f,  0.5f   // Line 12
        )
        selectionBoxVaoId = glGenVertexArrays()
        selectionBoxVboId = glGenBuffers()
        glBindVertexArray(selectionBoxVaoId)
        glBindBuffer(GL_ARRAY_BUFFER, selectionBoxVboId)
        glBufferData(GL_ARRAY_BUFFER, cubeVertices, GL_STATIC_DRAW)
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)
        glEnableVertexAttribArray(0)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glBindVertexArray(0)
    }

    fun generateModelData() {
        val vertexPositions = arrayListOf<Vector3f>()
        val normalVectors = arrayListOf<Vector3f>()
        val textureCoords = arrayListOf<Vector2f>()
        val textureIndexes = arrayListOf<Int>()

        var modelIndex = 0
        for (block in Block.blockList.sortedBy { it.id }) {
            for (model in block.models) {
                modelMap[model] = modelIndex

                val vertices = model.getVertices()
                var vertexIndex = 0
                for (vertex in vertices) {
                    vertexPositions.add(vertex.position.toVector3f())
                    normalVectors.add(vertex.normal.toVector3f())
                    textureCoords.add(vertex.textureCoord.toVector2f())
                    textureIndexes.add(vertex.textureIndex)
                    vertexIndex += 1
                }
                for (i in 0..<(blockDataArraysElementSize - vertexIndex)) {
                    vertexPositions.add(Vector3f(0f, 20f, 0f))
                    normalVectors.add(Vector3f())
                    textureCoords.add(Vector2f())
                    textureIndexes.add(0)
                }

                modelIndex += 1
            }
        }

        vertexPositionArray = vertexPositions.toArray(arrayOf<Vector3f>())
        normalVectorArray = normalVectors.toArray(arrayOf<Vector3f>())
        textureCoordArray = textureCoords.toArray(arrayOf<Vector2f>())
        textureIndexArray = textureIndexes.toArray(arrayOf<Int>())
    }

    fun getModelIndex(model: Model): Int {
        return modelMap[model] ?: throw IllegalArgumentException("model $model not in model map!")
    }

    fun cleanup() {
        blockShaderProgram.cleanup()
    }

    fun render(window: Window, world: World) {
        glFinish()

        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        glViewport(0, 0, window.width, window.height)

        blockShaderProgram.bind()

        blockUniformsMap.setUniform("projectionMatrix", projection.matrix)
        blockUniformsMap.setUniform("viewMatrix", camera.viewMatrix)
        val time = (System.nanoTime() - renderStartTime) / 1_000_000_000f
        blockUniformsMap.setUniform("time", time)

        // Enable blending
//        glEnable(GL_BLEND)
        glEnable(GL_DEPTH_TEST)
        // Set blending function
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

//        glDepthMask(false)

        // world.chunkManager.chunksLock.lock()

        val currentTime = System.currentTimeMillis()
        world.chunkManager.getLoadedChunks().forEach { chunk ->
            val camera = client.mainRenderer.camera
            val chunkDirection = camera.position - chunk.getWorldPosition()

            val chunkMesh = chunk.mesh
            if (chunkMesh != null && chunkMesh.numVertices > 0) {
                if (chunkDirection.normal.dot(camera.lookVector.normal) < -0.5
                    || chunkDirection.magnitude <= Config.chunkSize * 4
                ) {


                    val chunkVisibility =
                        ((currentTime - chunk.creationTime) / (chunkDirection.magnitude * 5 - 400).coerceIn(
                            0.0,
                            3000.0
                        ))
                    blockUniformsMap.setUniform("chunkVisibility", chunkVisibility.toFloat().coerceIn(0f, 1f))
                    blockUniformsMap.setUniform("chunkPosition", chunk.chunkPosition)


                    glBindVertexArray(chunkMesh.vaoId)
                    glDrawArrays(GL_TRIANGLES, 0, chunkMesh.numVertices)
                    glBindVertexArray(0)
                }
            }
        }
        // world.chunkManager.chunksLock.unlock()

//        glDepthMask(true)

        glBindVertexArray(0)

        blockShaderProgram.unbind()

        if (selectionBoxPosition != null) {
            selectionBoxShaderProgram.bind()

            selectionBoxUniformsMap.setUniform("projectionMatrix", projection.matrix)
            selectionBoxUniformsMap.setUniform("viewMatrix", camera.viewMatrix)
            selectionBoxUniformsMap.setUniform("selectionBoxPosition", selectionBoxPosition!!.toDouble3().toVector3f())

            glEnable(GL_POLYGON_OFFSET_LINE)
            glPolygonOffset(-10.0f, -10.0f)

            glBindVertexArray(selectionBoxVaoId)
            glDrawArrays(GL_LINES, 0, 24)
            glBindVertexArray(0)

            glDisable(GL_POLYGON_OFFSET_LINE)

            selectionBoxShaderProgram.unbind()
        }

        selectionBoxShaderProgram.bind()
        selectionBoxUniformsMap.setUniform("projectionMatrix", projection.matrix)
        selectionBoxUniformsMap.setUniform("viewMatrix", camera.viewMatrix)
        for (player in client.players.getPlayerList()) {
            if (player == client.players.localPlayer)
                continue
            selectionBoxUniformsMap.setUniform("selectionBoxPosition", (player.position - Double3(0.5, 0.5, 0.5)).toVector3f())
            glBindVertexArray(selectionBoxVaoId)
            glDrawArrays(GL_LINES, 0, 24)
            glBindVertexArray(0)
        }
        selectionBoxShaderProgram.unbind()
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