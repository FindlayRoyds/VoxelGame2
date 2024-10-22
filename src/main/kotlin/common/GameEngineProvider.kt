package common

object GameEngineProvider {
    private var currentGameEngine = ThreadLocal<GameEngine>()
    var gameEngine: GameEngine
        get() = currentGameEngine.get()
        set(value) = currentGameEngine.set(value)
}