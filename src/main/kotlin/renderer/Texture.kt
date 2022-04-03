package renderer

import org.lwjgl.opengl.GL46
import org.lwjgl.stb.STBImage
import utils.readTexture

class Texture(private val filepath: String): GLResource {
    companion object {
        private const val TEXTURE_NUMBER = GL46.GL_TEXTURE1
        private const val TEXTURE_NUMBER_INT = 1
    }

    private var textureID: Int = 0
    private var width: Int = 0
    private var height: Int = 0

    override fun setUp() {
        val (buffer, width, height) = readTexture(this.filepath)
        assert(buffer != null)
        this.width = width
        this.height = height
        this.textureID = GL46.glGenTextures()
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, textureID)
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_WRAP_S, GL46.GL_REPEAT)
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_WRAP_T, GL46.GL_REPEAT)
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MIN_FILTER, GL46.GL_LINEAR_MIPMAP_NEAREST)
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MAG_FILTER, GL46.GL_NEAREST)
        GL46.glTexParameterf(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MAX_ANISOTROPY, 8.0f)
        GL46.glTexImage2D(
            GL46.GL_TEXTURE_2D,
            0,
            GL46.GL_RGBA,
            width,
            height,
            0,
            GL46.GL_RGBA,
            GL46.GL_UNSIGNED_BYTE,
            buffer)
        GL46.glGenerateMipmap(GL46.GL_TEXTURE_2D)
        STBImage.stbi_image_free(buffer!!)
    }

    override fun tearDown() {
        GL46.glActiveTexture(TEXTURE_NUMBER)
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0)
        GL46.glDeleteTextures(this.textureID)
        this.textureID = 0
    }

    fun getWidth(): Int {
        return this.width
    }

    fun getHeight(): Int {
        return this.height
    }

    fun bind(shader: Shader) {
        GL46.glActiveTexture(TEXTURE_NUMBER)
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, this.textureID)
        shader.uniform1i("TEXTURE1", TEXTURE_NUMBER_INT)
    }
}