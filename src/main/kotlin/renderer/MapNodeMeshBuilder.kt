package renderer

import common.MapNode
import common.TileType
import org.joml.Vector2i
import org.lwjgl.BufferUtils
import java.nio.ByteBuffer


class MapNodeMeshBuilder {
    companion object {
        fun naiveBuild(mapNode: MapNode): ArrayList<TilesRect> {
            val mesh = ArrayList<TilesRect>()
            for (x in 0 until MapNode.SIZE) {
                for (y in 0 until MapNode.SIZE) {
                    val tile = mapNode.getTile(x, y)
                    val textureCode = tileToTextureCode.getOrDefault(
                        tile.getType(), tileToTextureCode[TileType.Invalid]
                    )!!
                    val rect = TilesRect(
                        Vector2i(x, y),
                        Vector2i(x + 1, y + 1),
                        textureCode
                    )
                    mesh.add(rect)
                }
            }
            return mesh
        }

        fun mergeBuild(mapNode: MapNode): ArrayList<TilesRect> {
            TODO()
        }

        fun buildVertexBuffer(mapNodeMesh: ArrayList<TilesRect>): Pair<ByteBuffer, IntArray> {
            // 2f coordinate, 2f uv, 1i tileCode
            val rectVerticesSize = (2 + 2 + 1) * 4 * 4
            val vertexBuffer = BufferUtils.createByteBuffer(rectVerticesSize * mapNodeMesh.size)
            val indexArray = IntArray(6 * mapNodeMesh.size)
            var indexArrayPosition = 0
            var indexStart = 0
            val indexPattern = intArrayOf(0, 3, 1, 0, 2, 3)

            for (rect in mapNodeMesh) {
                val w = (rect.topRight.x - rect.bottomLeft.x).toFloat()
                val h = (rect.topRight.y - rect.bottomLeft.y).toFloat()
                val uvs = floatArrayOf(
                    0f, h,
                    w, h,
                    0f, 0f,
                    w, 0f
                )
                for (i in 0 until 4) {
                    val u = uvs[i*2]
                    val v = uvs[i*2 + 1]
                    val x = u - 0.5f
                    val y = v - 0.5f
                    vertexBuffer.putFloat(x)
                    vertexBuffer.putFloat(y)
                    vertexBuffer.putFloat(u)
                    vertexBuffer.putFloat(v)
                    vertexBuffer.putInt(rect.tileTextureCode)
                }
                for (i in 0 until 6) {
                    indexArray[i + indexArrayPosition] = indexPattern[i] + indexStart
                }
                indexStart += 4
                indexArrayPosition += 6
            }
            vertexBuffer.flip()
            return Pair(vertexBuffer, indexArray)
        }
    }
}