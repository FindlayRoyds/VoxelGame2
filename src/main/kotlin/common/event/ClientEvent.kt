package common.event

import client.Client
import common.GameEngineProvider

abstract class ClientEvent : Event() {
    private var _client: Client? = null
    protected val client: Client
        get() {
            if (_client == null)
                _client = GameEngineProvider.getGameEngine() as Client
            return _client!!
        }

    override fun run() {
        event()
    }
}