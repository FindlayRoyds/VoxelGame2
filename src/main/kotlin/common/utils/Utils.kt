package common.utils

import common.math.Int3
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL40
import org.lwjgl.stb.STBImage
import java.nio.ByteBuffer

class Utils {
    companion object {
        val blockNeighbors = arrayListOf(Int3(0, 0, -1), Int3(0, 0, 1), Int3(-1, 0, 0), Int3(1, 0, 0), Int3(0, -1, 0), Int3(0, 1, 0))

        fun readFile(filePath: String): String {
            return try {
                // String(Files.readAllBytes(Paths.get(filePath)))
                javaClass.getResource(filePath)!!.readText()
            } catch (exception: NullPointerException) {
                throw RuntimeException("Error reading file [$filePath]", exception)
            }
        }

        fun loadTextureBMP(path: String): Int {
            val widthBuffer = BufferUtils.createIntBuffer(1)
            val heightBuffer = BufferUtils.createIntBuffer(1)
            val channelsBuffer = BufferUtils.createIntBuffer(1)

            val image: ByteBuffer = STBImage.stbi_load(path, widthBuffer, heightBuffer, channelsBuffer, 4)
                ?: throw RuntimeException("Failed to load a texture file!" + System.lineSeparator() + STBImage.stbi_failure_reason())

            // Generate texture ID
            val textureId = GL40.glGenTextures()
            GL40.glBindTexture(GL40.GL_TEXTURE_2D, textureId)

            // Wrap parameters
            GL40.glTexParameteri(GL40.GL_TEXTURE_2D, GL40.GL_TEXTURE_WRAP_S, GL40.GL_CLAMP_TO_EDGE)
            GL40.glTexParameteri(GL40.GL_TEXTURE_2D, GL40.GL_TEXTURE_WRAP_T, GL40.GL_CLAMP_TO_EDGE)

            // Filter parameters
            GL40.glTexParameteri(GL40.GL_TEXTURE_2D, GL40.GL_TEXTURE_MIN_FILTER, GL40.GL_LINEAR)
            GL40.glTexParameteri(GL40.GL_TEXTURE_2D, GL40.GL_TEXTURE_MAG_FILTER, GL40.GL_LINEAR)

            // Upload texture
            GL40.glTexImage2D(
                GL40.GL_TEXTURE_2D,
                0,
                GL40.GL_RGBA,
                widthBuffer[0],
                heightBuffer[0],
                0,
                GL40.GL_RGBA,
                GL40.GL_UNSIGNED_BYTE,
                image
            )
            GL40.glGenerateMipmap(GL40.GL_TEXTURE_2D)

            // Free image buffer
            STBImage.stbi_image_free(image)

            return textureId
        }
    }
}