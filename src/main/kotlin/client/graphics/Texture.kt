package client.graphics

import org.lwjgl.opengl.GL41.*
import org.lwjgl.stb.STBImage.*
import java.nio.ByteBuffer

class Texture {
    companion object {
        fun loadTextures(filenames: List<String>): Int {
            // Check for valid filenames
            if (filenames.isEmpty()) {
                throw IllegalArgumentException("List of filenames cannot be empty.")
            }

            // Determine texture dimensions
            val widths = IntArray(filenames.size)
            val heights = IntArray(filenames.size)
            val channels = IntArray(filenames.size)

            for ((index, filename) in filenames.withIndex()) {
                val w = IntArray(1)
                val h = IntArray(1)
                val c = IntArray(1)

                stbi_info(filename, w, h, c)
                widths[index] = w[0]
                heights[index] = h[0]
                channels[index] = c[0]
            }

            // Ensure all images have the same dimensions
            val firstWidth = widths[0]
            val firstHeight = heights[0]
            for (i in 1 until widths.size) {
                if (widths[i] != firstWidth || heights[i] != firstHeight) {
                    throw IllegalArgumentException("All images in a texture array must have the same dimensions.")
                }
            }

            // Generate texture array
            val textureArrayId = glGenTextures()
            glBindTexture(GL_TEXTURE_2D_ARRAY, textureArrayId)

//            glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR)
//            glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
//            glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_WRAP_S, GL_REPEAT)
//            glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_WRAP_T, GL_REPEAT)

            // Allocate storage for the texture array
            glTexImage3D(GL_TEXTURE_2D_ARRAY, 0, GL_RGB, firstWidth, firstHeight, filenames.size, 0, GL_RGB, GL_UNSIGNED_BYTE, null as ByteBuffer?)

            // Load image data into the texture array
            for ((index, filename) in filenames.withIndex()) {
                val image = stbi_load(filename, widths, heights, channels, 0)
                if (image == null) {
                    throw RuntimeException("Failed to load image: $filename")
                }

                glTexSubImage3D(GL_TEXTURE_2D_ARRAY, 0, 0, 0, index, firstWidth, firstHeight, 1, GL_RGB, GL_UNSIGNED_BYTE, image)
                stbi_image_free(image)
            }

            glGenerateMipmap(GL_TEXTURE_2D_ARRAY)

            // glBindTexture(GL_TEXTURE_2D_ARRAY, 0)

            return textureArrayId
        }
    }
}