package common.utils

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL40.*
import org.lwjgl.stb.STBImage
import java.nio.ByteBuffer

class Utils {
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
        val textureId = glGenTextures()
        glBindTexture(GL_TEXTURE_2D, textureId)

        // Wrap parameters
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)

        // Filter parameters
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)

        // Upload texture
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, widthBuffer[0], heightBuffer[0], 0, GL_RGBA, GL_UNSIGNED_BYTE, image)
        glGenerateMipmap(GL_TEXTURE_2D)

        // Free image buffer
        STBImage.stbi_image_free(image)

        return textureId
    }
}