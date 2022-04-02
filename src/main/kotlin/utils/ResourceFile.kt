package utils

class ResourceFile {
    fun getAsBytes(filepath: String): ByteArray {
        val classLoader = javaClass.classLoader
        val stream = classLoader.getResourceAsStream(filepath)!!
        return stream.readAllBytes()
    }

    fun getAsString(filepath: String): String {
        val bytes = this.getAsBytes(filepath)
        return bytes.decodeToString()
    }
}