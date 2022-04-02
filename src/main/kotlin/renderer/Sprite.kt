package renderer

import org.lwjgl.opengl.GL46


class Sprite (private val texture: Texture): GLResource {
    private var vao: Int = 0
    private var vbo: Int = 0
    private var ebo: Int = 0

    private val vertices = floatArrayOf(
        // x, y, u, v
        -0.5f, 0.5f, 0.0f, 1.0f,
        0.5f, 0.5f, 1.0f, 1.0f,
        -0.5f, -0.5f, 0.0f, 0.0f,
        0.5f, -0.5f, 1.0f, 0.0f
    )
    private val eboIndices = intArrayOf(0, 3, 1, 0, 2, 3)

    override fun setUp() {
        this.texture.setUp()

        vao = GL46.glGenVertexArrays()
        GL46.glBindVertexArray(vao)

        vbo = GL46.glGenBuffers()
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vbo)
        GL46.glBufferData(GL46.GL_ARRAY_BUFFER, this.vertices, GL46.GL_STATIC_DRAW)

        ebo = GL46.glGenBuffers()
        GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, ebo)
        GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, this.eboIndices, GL46.GL_STATIC_DRAW)

        val size = 2 * 2 * 4
        // bind x, y, z
        GL46.glVertexAttribPointer(0, 2, GL46.GL_FLOAT, false, size, 0)
        GL46.glEnableVertexAttribArray(0)
        // bind u, v
        GL46.glVertexAttribPointer(1, 2, GL46.GL_FLOAT, false, size, (2 * 4).toLong())
        GL46.glEnableVertexAttribArray(1)
    }

    override fun tearDown() {
        this.texture.tearDown()

        GL46.glDeleteVertexArrays(this.vao)
        GL46.glDeleteBuffers(this.vbo)
        GL46.glDeleteBuffers(this.ebo)
        this.vao = 0
        this.vbo = 0
        this.ebo = 0
    }

    fun bind(shader: Shader) {
        GL46.glBindVertexArray(this.vao)
        this.texture.bind(shader)
    }
    
    fun render() {
        GL46.glDrawElements(GL46.GL_TRIANGLES, eboIndices.size, GL46.GL_UNSIGNED_INT, 0)
    }

}