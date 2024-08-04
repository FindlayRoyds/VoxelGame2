package common.world

object WorldProvider {
    private var currentWorld = ThreadLocal<World>()

    fun setWorld(world: World) {
        currentWorld.set(world)
    }

    fun getWorld() : World {
        return currentWorld.get()
    }
}