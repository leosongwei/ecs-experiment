package renderer

import org.lwjgl.opengl.GL45


class Sprite (private val texture: Texture): GLResource {
    private var vao: Int = 0
    private var vbo: Int = 0
    private var ebo: Int = 0

    val vertices = floatArrayOf(
        // x, y, u, v
        -0.5f, 0.5f, 0.0f, 1.0f,
        0.5f, 0.5f, 1.0f, 1.0f,
        -0.5f, -0.5f, 0.0f, 0.0f,
        0.5f, -0.5f, 1.0f, 0.0f
    )
    val eboIndices = intArrayOf(0, 3, 1, 0, 2, 3)

    override fun setUp() {
        this.texture.setUp()

        vao = GL45.glGenVertexArrays()
        GL45.glBindVertexArray(vao)

        vbo = GL45.glGenBuffers()
        GL45.glBindBuffer(GL45.GL_ARRAY_BUFFER, vbo)
        GL45.glBufferData(GL45.GL_ARRAY_BUFFER, this.vertices, GL45.GL_STATIC_DRAW)

        ebo = GL45.glGenBuffers()
        GL45.glBindBuffer(GL45.GL_ELEMENT_ARRAY_BUFFER, ebo)
        GL45.glBufferData(GL45.GL_ELEMENT_ARRAY_BUFFER, this.eboIndices, GL45.GL_STATIC_DRAW)

        val size = (3 + 3 + 2) * 4
        // bind x, y, z
        // bind x, y, z
        GL45.glVertexAttribPointer(0, 3, GL45.GL_FLOAT, false, size, 0)
        GL45.glEnableVertexAttribArray(0)
        // bind nx, ny, nz
        // bind nx, ny, nz
        GL45.glVertexAttribPointer(1, 3, GL45.GL_FLOAT, false, size, (3 * 4).toLong())
        GL45.glEnableVertexAttribArray(1)
        // bind u, v
        // bind u, v
        GL45.glVertexAttribPointer(2, 2, GL45.GL_FLOAT, false, size, (6 * 4).toLong())
        GL45.glEnableVertexAttribArray(2)
    }

    override fun tearDown() {
        this.texture.tearDown()

        GL45.glDeleteVertexArrays(this.vao)
        GL45.glDeleteBuffers(this.vbo)
        GL45.glDeleteBuffers(this.ebo)
        this.vao = 0
        this.vbo = 0
        this.ebo = 0
    }

    fun bind(shader: Shader) {
        GL45.glBindVertexArray(this.vao)
        this.texture.bind(shader)
    }

}