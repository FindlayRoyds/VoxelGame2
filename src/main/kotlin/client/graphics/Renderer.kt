package client.graphics

import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*

class Renderer {
    val sceneRenderer: SceneRenderer

    init {
        println("Creating capabilities")
        GL.createCapabilities()
        sceneRenderer = SceneRenderer()
    }

    fun cleanup() {
        sceneRenderer.cleanup()
    }

    fun render(window: Window, scene: Scene) {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        glViewport(0, 0, window.width, window.height)
        sceneRenderer.render(scene)
    }
}