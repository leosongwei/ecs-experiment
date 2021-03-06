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
                    val textureCode = TileTextureCodeRegistry.getCode(tile.getType())
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
            val result = ArrayList<TilesRect>()

            for (tileType in TileType.values()) {
                val textureCode = TileTextureCodeRegistry.getCode(tileType)
                val mask = BooleanArray(MapNode.SIZE * MapNode.SIZE) { false }
                fun getMask(x: Int, y: Int): Boolean {
                    return mask[x + y * MapNode.SIZE]
                }

                fun setMask(x: Int, y: Int, value: Boolean) {
                    mask[x + y * MapNode.SIZE] = value
                }

                for (x in 0 until MapNode.SIZE) {
                    for (y in 0 until MapNode.SIZE) {
                        if (mapNode.getTile(x, y).getType() == tileType) {
                            setMask(x, y, true)
                        }
                    }
                }

                fun scanRow(row: Int) {
                    for (xScan in 0 until MapNode.SIZE) {
                        val isCurrent = getMask(xScan, row)
                        if (isCurrent) {
                            val bottomEdge = row
                            val leftEdge = xScan
                            var rightEdge = xScan
                            for (x in leftEdge until MapNode.SIZE) {
                                if (getMask(x, row)) {
                                    rightEdge = x + 1
                                } else {
                                    break
                                }
                            }
                            var topEdge = row + 1
                            for (y in row until MapNode.SIZE) {
                                var allSame = true
                                for (x in leftEdge until rightEdge) {
                                    if (!getMask(x, y)) {
                                        allSame = false
                                        break
                                    }
                                }
                                if (allSame) {
                                    topEdge = y + 1
                                } else {
                                    break
                                }
                            }
                            result.add(TilesRect(
                                Vector2i(leftEdge, bottomEdge),
                                Vector2i(rightEdge, topEdge),
                                textureCode
                            ))
                            for (x in leftEdge until rightEdge) {
                                for (y in bottomEdge until topEdge) {
                                    setMask(x, y, false)
                                }
                            }
                        }
                    }
                }

                for (row in 0 until MapNode.SIZE) {
                    scanRow(row)
                }
            }
            return result
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
                    val u = uvs[i * 2]
                    val v = uvs[i * 2 + 1]
                    val x = u - 0.5f + rect.bottomLeft.x
                    val y = v - 0.5f + rect.bottomLeft.y
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