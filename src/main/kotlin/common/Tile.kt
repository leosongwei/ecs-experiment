package common

@JvmInline
value class Tile(private val typeCode: Int)
{
    constructor(tileType: TileType): this(tileType.typeCode)

    fun toInt(): Int {
        return this.typeCode
    }

    fun walkable(): Boolean {
        return TilesWalkable.contains(TileType.fromNumber(this.typeCode))
    }

    fun bulletCanFlyAbove(): Boolean {
        return TilesBulletCanFlyAbove.contains(
            TileType.fromNumber(this.typeCode)
        )
    }

    fun getType(): TileType {
        return TileType.fromNumber(this.typeCode)
    }
}