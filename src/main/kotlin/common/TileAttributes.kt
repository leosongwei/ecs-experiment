package common

val TilesWalkable = setOf<TileType>(
    TileType.Grass,
    TileType.Sand
)

val TilesBulletCanFlyAbove = setOf<TileType>(
    TileType.Invalid,
    TileType.Grass,
    TileType.Sand,
    TileType.Water
)