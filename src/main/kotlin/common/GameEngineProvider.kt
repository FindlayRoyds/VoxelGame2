package common

object GameEngineProvider {
    private var currentGameEngine = ThreadLocal<GameEngine>()

    fun setGameEngine(gameEngine: GameEngine) {
        currentGameEngine.set(gameEngine)
    }

    fun getGameEngine() : GameEngine {
        return currentGameEngine.get()
    }
}