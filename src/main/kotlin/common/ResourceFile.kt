package common

class ResourceFile {
    fun getAsBytes(filepath: String?): ByteArray? {
        return try {
            val classLoader = javaClass.classLoader
            val stream = classLoader.getResourceAsStream(filepath)!!
            stream.readAllBytes()
        } catch (e: Exception) {
            null
        }
    }
}