package renderer

import org.lwjgl.opengl.GL46
import org.lwjgl.stb.STBImage
import utils.makeTexture2DArray
import utils.readTexture
import org.lwjgl.BufferUtils


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
        val mipLevelCount = 4
        val layerCount = 64
        val (buffer, width, height) =  readTexture(this.filepath, false)
        assert(buffer != null)
        val buffer2d = makeTexture2DArray(buffer!!, 512, 64)
        STBImage.stbi_image_free(buffer)

        this.width = width
        this.height = height
        assert(this.width == 512)
        assert(this.height == 512)

        this.textureID = GL46.glGenTextures()
        GL46.glBindTexture(GL46.GL_TEXTURE_2D_ARRAY, this.textureID)
        GL46.glTexStorage3D(
            GL46.GL_TEXTURE_2D_ARRAY, mipLevelCount, GL46.GL_RGBA8,
            subTextureSize, subTextureSize, layerCount
        )

        GL46.glTexSubImage3D(
            GL46.GL_TEXTURE_2D_ARRAY, 0, 0, 0, 0,
            subTextureSize, subTextureSize, layerCount,
            GL46.GL_RGBA, GL46.GL_UNSIGNED_BYTE,
            buffer2d
        )

        GL46.glTexParameteri(GL46.GL_TEXTURE_2D_ARRAY, GL46.GL_TEXTURE_WRAP_S, GL46.GL_REPEAT)
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D_ARRAY, GL46.GL_TEXTURE_WRAP_T, GL46.GL_REPEAT)
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D_ARRAY, GL46.GL_TEXTURE_MIN_FILTER, GL46.GL_LINEAR_MIPMAP_NEAREST)
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D_ARRAY, GL46.GL_TEXTURE_MAG_FILTER, GL46.GL_NEAREST)

        GL46.glGenerateMipmap(GL46.GL_TEXTURE_2D_ARRAY)
    }

    override fun tearDown() {
        TODO("Not yet implemented")
    }

    fun bind(shader: Shader) {
        GL46.glActiveTexture(TEXTURE_NUMBER)
        GL46.glBindTexture(GL46.GL_TEXTURE_2D_ARRAY, this.textureID)
        shader.uniform1i("TEXTURE2", TEXTURE_NUMBER_INT)
    }
}