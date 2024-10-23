package client.graphics

/**
 * Used for storing the mesh data in a queue before sending the mesh to the GPU.
 * Required as the mesh must be sent to the gpu on the main thread (bruh).
 */
data class MeshData(
//    val vertexIds: IntArray,
//    val blockPositions: IntArray,
//    val blockTypes: IntArray,
    val packedData: IntArray,
    val lightValues: FloatArray
)