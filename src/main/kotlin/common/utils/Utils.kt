package common.utils

class Utils {
    fun readFile(filePath: String): String {
        return try {
            // String(Files.readAllBytes(Paths.get(filePath)))
            javaClass.getResource(filePath)!!.readText()
        } catch (exception: NullPointerException) {
            throw RuntimeException("Error reading file [$filePath]", exception)
        }
    }
}