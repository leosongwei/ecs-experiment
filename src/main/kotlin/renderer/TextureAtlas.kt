package renderer

import org.lwjgl.opengl.GL46
import org.lwjgl.stb.STBImage
import utils.readTexture

// https://en.wikibooks.org/wiki/OpenGL_Programming/Modern_OpenGL_Tutorial_Text_Rendering_02

class TextureAtlas(private val filepath: String) : GLResource {
    companion object {
        private const val TEXTURE_NUMBER = GL46.GL_TEXTURE2
        private const val TEXTURE_NUMBER_INT = 2
    }

    private var textureID: Int = 0
    private var width: Int = 0
    private var height: Int = 0

    override fun setUp() {
        val subTextureSize = 64
        val mipLevelCount = 1
        val layerCount = 64
        val (buffer, width, height) = readTexture(this.filepath)
        assert(buffer != null)
        this.width = width
        this.height = height
        assert(this.width == 512)
        assert(this.height == 512)

        this.textureID = GL46.glGenTextures()
        GL46.glBindTexture(GL46.GL_TEXTURE_2D_ARRAY, this.textureID)
        GL46.glTexStorage3D(
            GL46.GL_TEXTURE_2D_ARRAY, mipLevelCount, GL46.GL_RGBA8,
            subTextureSize, subTextureSize, 64
        )
        for (i in 0 until 8*8) {
            val xOffset = 0
            val yOffset = 64*7
            GL46.glTexSubImage3D(
                GL46.GL_TEXTURE_2D_ARRAY, 0, xOffset, yOffset, i,
                subTextureSize, subTextureSize, layerCount,
                GL46.GL_RGBA, GL46.GL_UNSIGNED_BYTE,
                buffer!!
            )
        }
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_WRAP_S, GL46.GL_REPEAT)
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_WRAP_T, GL46.GL_REPEAT)
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MIN_FILTER, GL46.GL_LINEAR_MIPMAP_NEAREST)
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MAG_FILTER, GL46.GL_NEAREST)

        STBImage.stbi_image_free(buffer!!)
    }

    override fun tearDown() {
        TODO("Not yet implemented")
    }

    fun bind(shader: Shader) {
        GL46.glActiveTexture(TEXTURE_NUMBER)
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, this.textureID)
        shader.uniform1i("TEXTURE2", TEXTURE_NUMBER_INT)
    }
}