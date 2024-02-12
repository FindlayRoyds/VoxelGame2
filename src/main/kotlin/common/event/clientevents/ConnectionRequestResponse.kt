package common.event.clientevents

import common.event.ClientEvent

class ConnectionRequestResponse(private val accepted: Boolean): ClientEvent() {
    override fun event() {
        if (accepted)
            println("You have successfully connected to the server")
        else
            println("Connection to server denied")
    }
}