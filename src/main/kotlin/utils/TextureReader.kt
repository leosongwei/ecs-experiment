package utils

import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer

data class TextureLoad(val buffer: ByteBuffer?, val width: Int, val height: Int)

fun readTexture(filepath: String): TextureLoad {
    val xRef = intArrayOf(0)
    val yRef = intArrayOf(0)
    val channelsInFile = intArrayOf(0)
    val resource = ResourceFile()
    val bytes: ByteArray = resource.getAsBytes(filepath)
    val fileBuffer = MemoryUtil.memAlloc(bytes.size)
    fileBuffer.put(0, bytes)
    STBImage.stbi_set_flip_vertically_on_load(true)
    val buffer = STBImage.stbi_load_from_memory(fileBuffer, xRef, yRef, channelsInFile, 4)
    MemoryUtil.memFree(fileBuffer)
    if (buffer == null) {
        println(STBImage.stbi_failure_reason())
    }
    return TextureLoad(buffer, xRef[0], yRef[0])
}