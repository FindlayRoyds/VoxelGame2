package common.event

import client.Client
import common.GameEngineProvider
import java.io.Serializable

abstract class ClientEvent : Event(), Serializable {
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