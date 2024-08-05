package common.world

import common.GameEngineProvider
import common.math.Int2

class HeightmapChunk(val chunkPosition: Int2) {
    var heightmap = HashMap<Int, Int>()
    val gameEngine = GameEngineProvider.getGameEngine()

    fun getHeight(blockPosition: Int2): Int? {
        return heightmap[blockPosition.x * 33 + blockPosition.y]
    }

    fun setHeight(blockPosition: Int2, height: Int) {

    }
}