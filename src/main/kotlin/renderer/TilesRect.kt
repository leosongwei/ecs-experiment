package renderer

import org.joml.Vector2i

data class TilesRect(val bottomLeft: Vector2i, val topRight: Vector2i, val tileTextureCode: Int)
