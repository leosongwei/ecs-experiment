package renderer

import common.MapNode
import common.TileType
import org.joml.Matrix4f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL46
import java.nio.ByteBuffer
import org.joml.Vector2f
import org.joml.Vector3f



class MapNodeRenderData(private val origin: Vector2f) {
    private var vao: Int = 0
    private var vbo: Int = 0
    private var ebo: Int = 0
    private var eboSize: Int = 0

    fun setUp(mapNode: MapNode) {

        val (vertexBuffer, eboIndices) = createVertexBuffer(mapNode)
        eboSize = eboIndices.size

        vao = GL46.glGenVertexArrays()
        GL46.glBindVertexArray(vao)

        vbo = GL46.glGenBuffers()
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vbo)
        GL46.glBufferData(GL46.GL_ARRAY_BUFFER, vertexBuffer, GL46.GL_STATIC_DRAW)

        ebo = GL46.glGenBuffers()
        GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, ebo)
        GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, eboIndices, GL46.GL_STATIC_DRAW)

        val vertexSize = (4 + 1) * 4
        // bind x, y
        GL46.glVertexAttribPointer(0, 2, GL46.GL_FLOAT, false, vertexSize, 0)
        GL46.glEnableVertexAttribArray(0)
        // bind u, v
        GL46.glVertexAttribPointer(1, 2, GL46.GL_FLOAT, false, vertexSize, (2 * 4).toLong())
        GL46.glEnableVertexAttribArray(1)
        // bind texture code
        GL46.glVertexAttribIPointer(2, 1, GL46.GL_INT, vertexSize, (4 * 4).toLong())
        GL46.glEnableVertexAttribArray(2)
    }

    private fun createVertexBuffer(mapNode: MapNode): Pair<ByteBuffer, IntArray> {
        // TODO: Optimize Mesh
        val mesh = MapNodeMeshBuilder.naiveBuild(mapNode)
        return MapNodeMeshBuilder.buildVertexBuffer(mesh)
    }

    fun render(shader: Shader) {
        GL46.glBindVertexArray(this.vao)
        shader.uniformMatrix4fv(
            "model",
            Matrix4f().translate(
                Vector3f(this.origin, -1f)
            ))
        GL46.glDrawElements(GL46.GL_TRIANGLES, eboSize, GL46.GL_UNSIGNED_INT, 0)
    }
}