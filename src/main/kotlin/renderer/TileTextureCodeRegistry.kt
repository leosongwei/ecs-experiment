package renderer

import common.TileType

class TileTextureCodeRegistry {
    companion object {
        private val tileToTextureCode = hashMapOf<TileType, Int>(
            TileType.Invalid to 0,
            TileType.Grass to 1,
            TileType.Sand to 2,
            TileType.Wall to 3,
            TileType.Water to 4
        )

        fun getCode(tileType: TileType): Int {
            return tileToTextureCode.getOrDefault(
                tileType, tileToTextureCode[TileType.Invalid]
            )!!
        }
    }
}