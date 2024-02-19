package client.graphics

class Scene {
    private val meshMap: MutableMap<String, Mesh>

    init {
        meshMap = HashMap<String, Mesh>()
    }

    fun addMesh(meshId: String, mesh: Mesh) {
        meshMap[meshId] = mesh
    }

    fun cleanup() {
        meshMap.values.forEach(Mesh::cleanup)
    }

    fun getMeshMap(): Map<String, Mesh> {
        return meshMap
    }
}