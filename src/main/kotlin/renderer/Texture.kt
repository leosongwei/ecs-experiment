package model

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13
import org.lwjgl.opengl.GL30
import org.lwjgl.opengl.GL46
import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryUtil
import common.ResourceFile

class Texture(filepath: String) {
    private val textureID: Int
    private val width: Int
    private val height: Int

    init {
        val xRef = intArrayOf(0)
        val yRef = intArrayOf(0)
        val channelsInFile = intArrayOf(0)
        val resource = ResourceFile()
        val bytes: ByteArray = resource.getAsBytes(filepath)
        val fileBuffer = MemoryUtil.memAlloc(bytes.size)
        fileBuffer.put(0, bytes)
        STBImage.stbi_set_flip_vertically_on_load(true)
        val buffer = STBImage.stbi_load_from_memory(fileBuffer, xRef, yRef, channelsInFile, 3)
        MemoryUtil.memFree(fileBuffer)
        if (buffer == null) {
            println(STBImage.stbi_failure_reason())
        }
        assert(buffer != null)
        width = xRef[0]
        height = yRef[0]
        textureID = GL11.glGenTextures()
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_NEAREST)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST)
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL46.GL_TEXTURE_MAX_ANISOTROPY, 8.0f)
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D,
            0,
            GL11.GL_RGB,
            width,
            height,
            0,
            GL11.GL_RGB,
            GL11.GL_UNSIGNED_BYTE,
            buffer)
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D)
        STBImage.stbi_image_free(buffer!!)
    }

    fun getWidth(): Int {
        return this.width
    }

    fun getHeight(): Int {
        return this.height
    }

    fun bind(textureNumber: Int) {
        GL13.glActiveTexture(textureNumber)
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID)
    }
}