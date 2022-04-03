package renderer

import common.MapNode
import common.TileType
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL46
import java.nio.ByteBuffer

val tileToTextureCode = hashMapOf<TileType, Int>(
    TileType.Invalid to 0,
    TileType.Grass to 1,
    TileType.Sand to 2,
    TileType.Wall to 3,
    TileType.Water to 4
)

class MapNodeRenderData {
    private var vao: Int = 0
    private var vbo: Int = 0
    private var ebo: Int = 0

    fun setUp(mapNode: MapNode) {
        val (vertexBuffer, eboIndices) = createVertexBuffer(mapNode)

        vao = GL46.glGenVertexArrays()
        GL46.glBindVertexArray(vao)

        vbo = GL46.glGenBuffers()
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vbo)
        GL46.glBufferData(GL46.GL_ARRAY_BUFFER, vertexBuffer, GL46.GL_STATIC_DRAW)

        ebo = GL46.glGenBuffers()
        GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, ebo)
        GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, eboIndices, GL46.GL_STATIC_DRAW)

        val vertexSize = (4+1) * 4
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
        val vertexBuffer = BufferUtils.createByteBuffer((4+1) * 4 * 4)

        val indexArray = intArrayOf(6 * MapNode.SIZE * MapNode.SIZE)
        var indexArrayPosition = 0
        var indexStart = 0
        val indexPattern = intArrayOf(0, 3, 1, 0, 2, 3)

        for (x in 0 until MapNode.SIZE) {
            for (y in 0 until MapNode.SIZE) {
                val tile = mapNode.getTile(x, y)
                val vertices = floatArrayOf(
                    // x, y
                    x - 0.5f, y + 0.5f,
                    x + 0.5f, y + 0.5f,
                    x - 0.5f, y - 0.5f,
                    x + 0.5f, y - 0.5f
                )
                val uvs = floatArrayOf(
                    0f, 1f,
                    1f, 1f,
                    0f, 0f,
                    1f, 0f
                )
                val textureCode = tileToTextureCode.getOrDefault(
                    tile.getType(), tileToTextureCode[TileType.Invalid]
                )!!
                for(i in 0 until 4) {
                    vertexBuffer.putFloat(vertices[i*2])
                    vertexBuffer.putFloat(vertices[i*2+1])
                    vertexBuffer.putFloat(uvs[i*2])
                    vertexBuffer.putFloat(uvs[i*2+1])
                    vertexBuffer.putInt(textureCode)
                }
                for(i in 0 until 6) {
                    indexArray[i + indexArrayPosition] = indexPattern[i] + indexStart
                }
                indexStart += 4
                indexArrayPosition += 6
            }
        }
        vertexBuffer.flip()
        return Pair(vertexBuffer, indexArray)
    }
}