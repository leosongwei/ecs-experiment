package common

enum class TileType(var typeCode: Int) {
    Invalid(0),
    Grass(1),
    Sand(2),
    Wall(3),
    Water(4);

    companion object {
        fun fromNumber(typeCode: Int):TileType {
            return when(typeCode) {
                Grass.typeCode -> Grass
                Sand.typeCode -> Sand
                Wall.typeCode -> Wall
                Water.typeCode -> Water
                else -> Invalid
            }
        }
    }
}

fun main() {
    println(TileType.fromNumber(0))
    println(TileType.fromNumber(1))
    println(TileType.Sand.typeCode)
}