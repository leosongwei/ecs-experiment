package common

import org.joml.Vector2i
import renderer.Shader
import utils.readTexture
import java.nio.ByteBuffer

class GameMap(private val filepath: String) {
    companion object {
        private fun color(r: Int, g: Int, b: Int, a: Int): UInt {
            return ((r shl 24) or
                    (g shl 16) or
                    (b shl 8) or
                    a
                    ).toUInt()
        }

        fun getTileFromColor(color: UInt): Tile {
            return when (color) {
                color(0, 255, 0, 255) -> Tile(TileType.Grass)
                color(255, 255, 0, 255) -> Tile(TileType.Sand)
                color(255, 0, 0, 255) -> Tile(TileType.Wall)
                color(0, 0, 255, 255) -> Tile(TileType.Water)
                else -> {
                    println("Invalid!")
                    Tile(TileType.Invalid)
                }
            }
        }

        fun getFromImage(x: Int, y: Int, width: Int, buffer: ByteBuffer): UInt {
            val offset = (x + y * width) * 4
            return ((buffer[offset].toUByte().toUInt() shl 24) or
                    (buffer[offset + 1].toUByte().toUInt() shl 16) or
                    (buffer[offset + 2].toUByte().toUInt() shl 8) or
                    buffer[offset + 3].toUByte().toUInt()
                    )
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
            MapNode(Vector2i(x, y))
        }
        for (node in mapNodeArray) {
            val baseX = node.getBaseX()
            val baseY = node.getBaseY()
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

    fun setUpRenderData() {
        this.mapNodeArray.forEach {
            it.setUpRenderData()
        }
    }

    fun render(shader: Shader) {
        this.mapNodeArray.forEach {
            it.render(shader)
        }
    }
}

fun main() {
    GameMap("test_map.png")
}