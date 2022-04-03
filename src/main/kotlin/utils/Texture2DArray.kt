package utils

import org.lwjgl.BufferUtils
import java.nio.ByteBuffer

fun makeTexture2DArray(bufferNotFlipped: ByteBuffer, largeTextureSize: Int, subTextureSize: Int): ByteBuffer {
    // only support RGBA8, fetched in int32
    val depth = 4
    val colorElementSizeInByte = 1
    assert(bufferNotFlipped.capacity() == largeTextureSize * largeTextureSize * colorElementSizeInByte * depth)
    val outputBuffer = BufferUtils.createByteBuffer(bufferNotFlipped.capacity())

    fun fetchPixel(x: Int, y: Int): Int {
        val offset = (x + y * largeTextureSize) * 4
        return bufferNotFlipped.getInt(offset)
    }

    for (row in 0 until largeTextureSize / subTextureSize) {
        for (col in 0 until largeTextureSize / subTextureSize) {
            for (y in subTextureSize-1 downTo 0) {
                for (x in 0 until subTextureSize) {
                    val xInLarge = col * subTextureSize + x
                    val yInLarge = row * subTextureSize + y
                    val pixel = fetchPixel(xInLarge, yInLarge)
                    outputBuffer.putInt(pixel)
                }
            }
        }
    }
    outputBuffer.flip()
    return outputBuffer
}

fun main() {
    val (buffer, _, _) = readTexture("tiles.png", false)
    makeTexture2DArray(buffer!!, 512, 64)
}