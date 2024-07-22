package client.graphics

import client.Client
import common.Config
import common.GameEngineProvider
import common.math.Double3
import common.math.Int3
import common.world.World
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL41.*

class Renderer(width: Int, height: Int) {
    private val blockShaderProgram: ShaderProgram
    private val selectionBoxShaderProgram: ShaderProgram
    private val blockUniformsMap: UniformsMap
    private val selectionBoxUniformsMap: UniformsMap
    private val projection = Projection(width, height)
    val camera = Camera()
    var isWireframe = false

    private val selectionBoxVaoId: Int
    private val selectionBoxVboId: Int
    var selectionBoxPosition: Int3? = null

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
        selectionBoxShaderModuleDataList.add(ShaderProgram.ShaderModuleData("/shaders/selection.vert", GL_VERTEX_SHADER))
        selectionBoxShaderModuleDataList.add(ShaderProgram.ShaderModuleData("/shaders/selection.frag", GL_FRAGMENT_SHADER))
        selectionBoxShaderProgram = ShaderProgram(selectionBoxShaderModuleDataList)
//        selectionBoxShaderProgram.bind()

        blockUniformsMap = UniformsMap(blockShaderProgram.programId)
        blockUniformsMap.createUniform("projectionMatrix");
        blockUniformsMap.createUniform("viewMatrix")
        blockUniformsMap.createUniform("vertexDataArray")
        blockUniformsMap.createUniform("textureDataArray")
        blockUniformsMap.createUniform("textureIndexArray")
        blockUniformsMap.createUniform("normalDataArray")
        blockUniformsMap.createUniform("chunkPosition")

        selectionBoxUniformsMap = UniformsMap(selectionBoxShaderProgram.programId)
        selectionBoxUniformsMap.createUniform("projectionMatrix");
        selectionBoxUniformsMap.createUniform("viewMatrix")
        selectionBoxUniformsMap.createUniform("selectionBoxPosition")

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

            Vector3f(-0.5f, -0.5f, -0.0f,),
            Vector3f(0.5f, -0.5f, -0.0f,),
            Vector3f(0.5f,  0.5f, -0.0f,),
            Vector3f(0.5f,  0.5f, -0.0f,),
            Vector3f(-0.5f,  0.5f, -0.0f,),
            Vector3f(-0.5f, -0.5f, -0.0f,),

            // Back face
            Vector3f(-0.5f, -0.5f,  0.0f,),
            Vector3f(0.5f, -0.5f,  0.0f,),
            Vector3f(0.5f,  0.5f,  0.0f,),
            Vector3f(0.5f,  0.5f,  0.0f,),
            Vector3f(-0.5f,  0.5f,  0.0f,),
            Vector3f(-0.5f, -0.5f,  0.0f,),

            // Left face
            Vector3f(-0.0f,  0.5f,  0.5f,),
            Vector3f(-0.0f,  0.5f, -0.5f,),
            Vector3f(-0.0f, -0.5f, -0.5f,),
            Vector3f(-0.0f, -0.5f, -0.5f,),
            Vector3f(-0.0f, -0.5f,  0.5f,),
            Vector3f(-0.0f,  0.5f,  0.5f,),

            // Right face
            Vector3f(0.0f,  0.5f,  0.5f,),
            Vector3f(0.0f,  0.5f, -0.5f,),
            Vector3f(0.0f, -0.5f, -0.5f,),
            Vector3f(0.0f, -0.5f, -0.5f,),
            Vector3f(0.0f, -0.5f,  0.5f,),
            Vector3f(0.0f,  0.5f,  0.5f,),

            // Bottom face
            Vector3f(-0.5f, -0.0f, -0.5f,),
            Vector3f(0.5f, -0.0f, -0.5f,),
            Vector3f(0.5f, -0.0f,  0.5f,),
            Vector3f(0.5f, -0.0f,  0.5f,),
            Vector3f(-0.5f, -0.0f,  0.5f,),
            Vector3f(-0.5f, -0.0f, -0.5f,),

            // Top face
            Vector3f(-0.5f,  0.0f, -0.5f,),
            Vector3f(0.5f,  0.0f, -0.5f,),
            Vector3f(0.5f,  0.0f,  0.5f,),
            Vector3f(0.5f,  0.0f,  0.5f,),
            Vector3f(-0.5f,  0.0f,  0.5f,),
            Vector3f(-0.5f,  0.0f, -0.5f,),
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

        val normalDataArray = arrayOf(
            Vector3f(0f, 0f, -1f), Vector3f(0f, 0f, -1f), Vector3f(0f, 0f, -1f), Vector3f(0f, 0f, -1f), Vector3f(0f, 0f, -1f),Vector3f(0f, 0f, -1f),
            Vector3f(0f, 0f, 1f), Vector3f(0f, 0f, 1f), Vector3f(0f, 0f, 1f), Vector3f(0f, 0f, 1f), Vector3f(0f, 0f, 1f), Vector3f(0f, 0f, 1f),
            Vector3f(-1f, 0f, 0f), Vector3f(-1f, 0f, -0f), Vector3f(-1f, 0f, -0f), Vector3f(-1f, 0f, -0f), Vector3f(-1f, 0f, -0f), Vector3f(-1f, 0f, -0f),
            Vector3f(1f, 0f, 0f), Vector3f(1f, 0f, 0f), Vector3f(1f, 0f, 0f), Vector3f(1f, 0f, 0f), Vector3f(1f, 0f, 0f), Vector3f(1f, 0f, 0f),
            Vector3f(0f, -1f, 0f), Vector3f(0f, -1f, 0f), Vector3f(0f, -1f, 0f), Vector3f(0f, -1f, 0f), Vector3f(0f, -1f, 0f), Vector3f(0f, -1f, 0f),
            Vector3f(0f, 1f, 0f), Vector3f(0f, 1f, 0f), Vector3f(0f, 1f, 0f), Vector3f(0f, 1f, 0f), Vector3f(0f, 1f, 0f), Vector3f(0f, 1f, 0f),

            Vector3f(0f, 0f, -1f), Vector3f(0f, 0f, -1f), Vector3f(0f, 0f, -1f), Vector3f(0f, 0f, -1f), Vector3f(0f, 0f, -1f),Vector3f(0f, 0f, -1f),
            Vector3f(0f, 0f, 1f), Vector3f(0f, 0f, 1f), Vector3f(0f, 0f, 1f), Vector3f(0f, 0f, 1f), Vector3f(0f, 0f, 1f), Vector3f(0f, 0f, 1f),
            Vector3f(-1f, 0f, 0f), Vector3f(-1f, 0f, -0f), Vector3f(-1f, 0f, -0f), Vector3f(-1f, 0f, -0f), Vector3f(-1f, 0f, -0f), Vector3f(-1f, 0f, -0f),
            Vector3f(1f, 0f, 0f), Vector3f(1f, 0f, 0f), Vector3f(1f, 0f, 0f), Vector3f(1f, 0f, 0f), Vector3f(1f, 0f, 0f), Vector3f(1f, 0f, 0f),
            Vector3f(0f, -1f, 0f), Vector3f(0f, -1f, 0f), Vector3f(0f, -1f, 0f), Vector3f(0f, -1f, 0f), Vector3f(0f, -1f, 0f), Vector3f(0f, -1f, 0f),
            Vector3f(0f, 1f, 0f), Vector3f(0f, 1f, 0f), Vector3f(0f, 1f, 0f), Vector3f(0f, 1f, 0f), Vector3f(0f, 1f, 0f), Vector3f(0f, 1f, 0f),

            Vector3f(0f, 0f, -1f), Vector3f(0f, 0f, -1f), Vector3f(0f, 0f, -1f), Vector3f(0f, 0f, -1f), Vector3f(0f, 0f, -1f),Vector3f(0f, 0f, -1f),
            Vector3f(0f, 0f, 1f), Vector3f(0f, 0f, 1f), Vector3f(0f, 0f, 1f), Vector3f(0f, 0f, 1f), Vector3f(0f, 0f, 1f), Vector3f(0f, 0f, 1f),
            Vector3f(-1f, 0f, 0f), Vector3f(-1f, 0f, -0f), Vector3f(-1f, 0f, -0f), Vector3f(-1f, 0f, -0f), Vector3f(-1f, 0f, -0f), Vector3f(-1f, 0f, -0f),
            Vector3f(1f, 0f, 0f), Vector3f(1f, 0f, 0f), Vector3f(1f, 0f, 0f), Vector3f(1f, 0f, 0f), Vector3f(1f, 0f, 0f), Vector3f(1f, 0f, 0f),
            Vector3f(0f, -1f, 0f), Vector3f(0f, -1f, 0f), Vector3f(0f, -1f, 0f), Vector3f(0f, -1f, 0f), Vector3f(0f, -1f, 0f), Vector3f(0f, -1f, 0f),
            Vector3f(0f, 1f, 0f), Vector3f(0f, 1f, 0f), Vector3f(0f, 1f, 0f), Vector3f(0f, 1f, 0f), Vector3f(0f, 1f, 0f), Vector3f(0f, 1f, 0f),

            Vector3f(0f, 0f, -1f), Vector3f(0f, 0f, -1f), Vector3f(0f, 0f, -1f), Vector3f(0f, 0f, -1f), Vector3f(0f, 0f, -1f),Vector3f(0f, 0f, -1f),
            Vector3f(0f, 0f, 1f), Vector3f(0f, 0f, 1f), Vector3f(0f, 0f, 1f), Vector3f(0f, 0f, 1f), Vector3f(0f, 0f, 1f), Vector3f(0f, 0f, 1f),
            Vector3f(-1f, 0f, 0f), Vector3f(-1f, 0f, -0f), Vector3f(-1f, 0f, -0f), Vector3f(-1f, 0f, -0f), Vector3f(-1f, 0f, -0f), Vector3f(-1f, 0f, -0f),
            Vector3f(1f, 0f, 0f), Vector3f(1f, 0f, 0f), Vector3f(1f, 0f, 0f), Vector3f(1f, 0f, 0f), Vector3f(1f, 0f, 0f), Vector3f(1f, 0f, 0f),
            Vector3f(0f, -1f, 0f), Vector3f(0f, -1f, 0f), Vector3f(0f, -1f, 0f), Vector3f(0f, -1f, 0f), Vector3f(0f, -1f, 0f), Vector3f(0f, -1f, 0f),
            Vector3f(0f, 1f, 0f), Vector3f(0f, 1f, 0f), Vector3f(0f, 1f, 0f), Vector3f(0f, 1f, 0f), Vector3f(0f, 1f, 0f), Vector3f(0f, 1f, 0f),

            )

        val textureIndexArray = arrayOf(
            0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0,
            4, 4, 4, 4, 4, 4,
            1, 1, 1, 1, 1, 1,

            3, 3, 3, 3, 3, 3,
            3, 3, 3, 3, 3, 3,
            3, 3, 3, 3, 3, 3,
            3, 3, 3, 3, 3, 3,
            3, 3, 3, 3, 3, 3,
            3, 3, 3, 3, 3, 3,

//            2, 2, 2, 2, 2, 2,
//            2, 2, 2, 2, 2, 2,
//            2, 2, 2, 2, 2, 2,
//            2, 2, 2, 2, 2, 2,
//            5, 5, 5, 5, 5, 5,
//            5, 5, 5, 5, 5, 5,

            4, 4, 4, 4, 4, 4,
            4, 4, 4, 4, 4, 4,
            4, 4, 4, 4, 4, 4,
            4, 4, 4, 4, 4, 4,
            4, 4, 4, 4, 4, 4,
            4, 4, 4, 4, 4, 4,

            6, 6, 6, 6, 6, 6,
            6, 6, 6, 6, 6, 6,
            6, 6, 6, 6, 6, 6,
            6, 6, 6, 6, 6, 6,
            6, 6, 6, 6, 6, 6,
            6, 6, 6, 6, 6, 6,
            )

        blockUniformsMap.setUniform("vertexDataArray", vertexDataArray)
        blockUniformsMap.setUniform("textureDataArray", textureDataArray)
        blockUniformsMap.setUniform("textureIndexArray", textureIndexArray)
        blockUniformsMap.setUniform("normalDataArray", normalDataArray)

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

        // Enable blending
        glEnable(GL_BLEND)
        // Set blending function
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

//        glDepthMask(false)

        // world.chunkManager.chunksLock.lock()

        world.chunkManager.getLoadedChunks().forEach { chunk ->
            val chunkPosition = (chunk.chunkPosition.toDouble3() + Double3(0.5, 0.5, 0.5)) * Config.chunkSize
            val camera = client.renderer.camera
            val chunkDirection = camera.position - chunkPosition

            if (chunkDirection.normal().dot(camera.lookVector.normal()) < -0.5
                || chunkDirection.magnitude <= Config.chunkSize * 4) {
                val chunkMesh = chunk.mesh
                blockUniformsMap.setUniform("chunkPosition", chunk.chunkPosition)

                if (chunkMesh != null && chunkMesh.numVertices > 0) {
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