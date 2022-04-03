package renderer

import org.lwjgl.opengl.GL46
import org.lwjgl.BufferUtils
import java.nio.ByteBuffer


class Sprite(
    private val textureCode: Int = 0,
) : GLResource {
    private var vao: Int = 0
    private var vbo: Int = 0
    private var ebo: Int = 0

    private val verticesBuffer: ByteBuffer = BufferUtils.createByteBuffer(5 * 4 * 4)

    private val eboIndices = intArrayOf(0, 3, 1, 0, 2, 3)

    init {
        val vertices = floatArrayOf(
            // x, y, u, v,
            -0.5f, 0.5f, 0.0f, 1.0f,
            0.5f, 0.5f, 1.0f, 1.0f,
            -0.5f, -0.5f, 0.0f, 0.0f,
            0.5f, -0.5f, 1.0f, 0.0f
        )

        for (i in 0 until 4) {
            for (j in 0 until 4) {
                verticesBuffer.putFloat(vertices[i * 4 + j])
            }
            verticesBuffer.putInt(textureCode)
        }

        verticesBuffer.flip()
    }

    override fun setUp() {
        vao = GL46.glGenVertexArrays()
        GL46.glBindVertexArray(vao)

        vbo = GL46.glGenBuffers()
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vbo)
        GL46.glBufferData(GL46.GL_ARRAY_BUFFER, this.verticesBuffer, GL46.GL_STATIC_DRAW)

        ebo = GL46.glGenBuffers()
        GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, ebo)
        GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, this.eboIndices, GL46.GL_STATIC_DRAW)

        val size = 5 * 4
        // bind x, y
        GL46.glVertexAttribPointer(0, 2, GL46.GL_FLOAT, false, size, 0)
        GL46.glEnableVertexAttribArray(0)
        // bind u, v
        GL46.glVertexAttribPointer(1, 2, GL46.GL_FLOAT, false, size, (2 * 4).toLong())
        GL46.glEnableVertexAttribArray(1)
        // bind texture code
        GL46.glVertexAttribIPointer(2, 1, GL46.GL_INT, size, (4 * 4).toLong())
        GL46.glEnableVertexAttribArray(2)
    }

    override fun tearDown() {
        GL46.glDeleteVertexArrays(this.vao)
        GL46.glDeleteBuffers(this.vbo)
        GL46.glDeleteBuffers(this.ebo)
        this.vao = 0
        this.vbo = 0
        this.ebo = 0
    }

    fun bind(shader: Shader) {
        GL46.glBindVertexArray(this.vao)
    }

    fun render() {
        GL46.glDrawElements(GL46.GL_TRIANGLES, eboIndices.size, GL46.GL_UNSIGNED_INT, 0)
    }
}