package common.event.clientevents

import common.event.ClientEvent

class ConnectionAccept: ClientEvent() {
    override fun event() {
        println("You have successfully connected to the server")
    }
}