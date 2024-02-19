package client.graphics

import common.world.World
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*

class Renderer {
    init {
        GL.createCapabilities()
    }

    fun cleanup() {
    }

    fun render(window: Window, world: World) {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
    }
}