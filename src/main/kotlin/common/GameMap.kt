package common

import org.joml.Vector2i
import utils.readTexture
import java.nio.ByteBuffer

class GameMap(private val filepath: String) {
    companion object {
        private fun color(r: Int, g: Int, b: Int, a: Int): UInt {
            return ((r ushr 24) or
                    (g ushr 16) or
                    (b ushr 8) or
                    a
                    ).toUInt()
        }

        fun getTileFromColor(color: UInt): Tile {
            return when (color) {
                color(0, 255, 0, 255) -> Tile(TileType.Grass)
                color(255, 255, 0, 255) -> Tile(TileType.Sand)
                color(255, 0, 0, 255) -> Tile(TileType.Wall)
                color(0, 0, 255, 255) -> Tile(TileType.Water)
                else -> Tile(TileType.Invalid)
            }
        }

        fun getFromImage(x: Int, y: Int, size: Int, buffer: ByteBuffer): UInt {
            val offset = (x + y * size) * 4
            return ((buffer[offset].toInt() ushr 24) or
                    (buffer[offset + 1].toInt() ushr 16) or
                    (buffer[offset + 2].toInt() ushr 8) or
                    buffer[offset + 3].toInt()
                    ).toUInt()
        }
    }

    private val mapNodeArray: Array<MapNode>
    private val size: Int

    init {
        val (mapNodeArray, size) = loadMap(filepath)
        this.mapNodeArray = mapNodeArray
        this.size = size
    }

    private fun loadMap(filepath: String): Pair<Array<MapNode>, Int> {
        val (buffer, width, height) = readTexture(filepath, true)
        assert(buffer != null)
        assert(width == height)
        assert(width.mod(MapNode.SIZE) == 0)
        val sizeInMapNode = width / MapNode.SIZE
        val mapNodeArray = Array<MapNode>(sizeInMapNode * sizeInMapNode) {
            val x = it.mod(sizeInMapNode) * MapNode.SIZE
            val y = (it / sizeInMapNode) * MapNode.SIZE
            println("$x, $y")
            MapNode(Vector2i(x, y))
        }
        for (node in mapNodeArray) {
            val baseX = node.getBaseX()
            val baseY = node.getBaseY()
            println("baseX: $baseX, baseY: $baseY")
            for (x in 0 until MapNode.SIZE) {
                for (y in 0 until MapNode.SIZE) {
                    val color = getFromImage(baseX + x, baseY + y, width, buffer!!)
                    val tile = getTileFromColor(color)
                    node.setTile(x, y, tile)
                }
            }
        }

        return Pair(mapNodeArray, width)
    }
}

fun main() {
    GameMap("test_map.png")
}