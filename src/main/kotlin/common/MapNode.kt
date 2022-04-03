package common

import org.joml.Vector2f
import renderer.MapNodeRenderData
import renderer.Shader

class MapNode(private val origin: Vector2f) {
    companion object {
        const val SIZE = 128
    }

    private var renderData: MapNodeRenderData? = null
    private val tiles = IntArray(SIZE * SIZE)

    private fun offset(x: Int, y: Int): Int {
        return x + y * SIZE
    }

    fun getTile(x: Int, y: Int): Tile {
        return Tile(tiles[offset(x, y)])
    }

    fun setTile(x: Int, y: Int, tile: Tile) {
        tiles[offset(x, y)] = tile.toInt()
    }

    fun setupRenderData() {
        this.renderData = MapNodeRenderData(this.origin)
        this.renderData!!.setUp(this)
    }

    fun render(shader: Shader) {
        if (this.renderData != null) {
            this.renderData!!.render(shader)
        }
    }
}