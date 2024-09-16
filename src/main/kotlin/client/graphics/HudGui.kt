//import client.graphics.Window
//import org.lwjgl.nuklear.*
//import org.lwjgl.nuklear.Nuklear.*
//import org.lwjgl.system.MemoryStack
//import org.lwjgl.system.MemoryUtil
//import org.lwjgl.opengl.GL11.*
//import org.lwjgl.opengl.GL15.*
//import org.lwjgl.opengl.GL20.*
//import org.lwjgl.opengl.GL30.*
//
//class HudGui(private val window: Window) {
//    private val ctx: NkContext = NkContext.create()
//    private val BUFFER_INITIAL_SIZE = 4 * 1024
//
//    private var vbo: Int = 0
//    private var vao: Int = 0
//    private var ebo: Int = 0
//
//    init {
//        setupContext()
//        setupBuffers()
//    }
//
//    private fun setupContext() {
//        val fontAtlas = NkFontAtlas.create()
//        val config = NkAllocator.create()
//        config.alloc { (_: Long, _: Long, size: Long) -> MemoryUtil.nmemAllocChecked(size) }
//        config.free { (_: Long, ptr: Long) -> MemoryUtil.nmemFree(ptr) }
//
//
//        nk_init(ctx, config, null)
//        nk_font_atlas_init_default(fontAtlas)
//        nk_font_atlas_begin(fontAtlas)
//        val defaultFont = nk_font_atlas_add_default(fontAtlas, 13f, null)
//        val texture = nk_font_atlas_bake(fontAtlas, IntArray(1), IntArray(1))
//        nk_font_atlas_end(fontAtlas, nk_handle_id(texture), null)
//        nk_style_set_font(ctx, defaultFont)
//    }
//
//    private fun setupBuffers() {
//        vbo = glGenBuffers()
//        ebo = glGenBuffers()
//        vao = glGenVertexArrays()
//
//        glBindVertexArray(vao)
//        glBindBuffer(GL_ARRAY_BUFFER, vbo)
//        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo)
//
//        glEnableVertexAttribArray(0)
//        glEnableVertexAttribArray(1)
//        glEnableVertexAttribArray(2)
//        glVertexAttribPointer(0, 2, GL_FLOAT, false, 20, 0)
//        glVertexAttribPointer(1, 2, GL_FLOAT, false, 20, 8)
//        glVertexAttribPointer(2, 4, GL_UNSIGNED_BYTE, true, 20, 16)
//
//        glBindVertexArray(0)
//        glBindBuffer(GL_ARRAY_BUFFER, 0)
//        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)
//    }
//
//    fun newFrame() {
//        nk_input_begin(ctx)
//        // Handle input events here if needed
//        nk_input_end(ctx)
//    }
//
//    fun layout() {
//        if (nk_begin(ctx, "HUD", nk_rect(50f, 50f, 200f, 200f,
//                NK_WINDOW_BORDER or NK_WINDOW_MOVABLE or NK_WINDOW_SCALABLE or
//                        NK_WINDOW_MINIMIZABLE or NK_WINDOW_TITLE)) {
//                nk_layout_row_dynamic(ctx, 20f, 1)
//                nk_label(ctx, "Hello, HUD!", NK_TEXT_LEFT)
//                // Add more UI elements here
//            }
//            nk_end(ctx)
//    }
//
//    fun render() {
//        val width = window.width
//        val height = window.height
//
//        glViewport(0, 0, width, height)
//        glClear(GL_COLOR_BUFFER_BIT)
//        glEnable(GL_BLEND)
//        glBlendEquation(GL_FUNC_ADD)
//        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
//        glDisable(GL_CULL_FACE)
//        glDisable(GL_DEPTH_TEST)
//        glEnable(GL_SCISSOR_TEST)
//
//        MemoryStack.stackPush().use { stack ->
//            val cmds = NkBuffer.mallocCallocFloat(BUFFER_INITIAL_SIZE, stack)
//            nk_convert(ctx, cmds, null, null)
//
//            glBindVertexArray(vao)
//            glBindBuffer(GL_ARRAY_BUFFER, vbo)
//            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo)
//
//            val offset = 0L
//            val cmd = NkDrawCommand.create(cmds.address() + offset)
//            while (cmd.elem_count() != 0) {
//                glScissor(
//                    cmd.clip_rect().x().toInt(),
//                    height - (cmd.clip_rect().y() + cmd.clip_rect().h()).toInt(),
//                    cmd.clip_rect().w().toInt(),
//                    cmd.clip_rect().h().toInt()
//                )
//                glDrawElements(GL_TRIANGLES, cmd.elem_count().toInt(), GL_UNSIGNED_SHORT, cmd.offset().toLong())
//                offset += NkDrawCommand.SIZEOF
//                cmd.address(cmds.address() + offset)
//            }
//        }
//
//        glBindVertexArray(0)
//        glBindBuffer(GL_ARRAY_BUFFER, 0)
//        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)
//        glDisable(GL_BLEND)
//        glDisable(GL_SCISSOR_TEST)
//    }
//
//    fun cleanup() {
//        glDeleteBuffers(vbo)
//        glDeleteBuffers(ebo)
//        glDeleteVertexArrays(vao)
//        nk_free(ctx)
//    }
//}