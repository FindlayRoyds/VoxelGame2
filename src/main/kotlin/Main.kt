
import client.Client
import server.Server
import kotlin.concurrent.thread


fun startServer(port: Int) {
    println("Server thread: ${Thread.currentThread().name}")
    Server(port)
}

fun startClient(address: String, port: Int) {
    println("Client thread: ${Thread.currentThread().name}")
    Client(address, port)
}

fun startClientWithoutAddress(port: Int) {
    val address = readln()
    Client(address, port)
}

fun startClientAndServer(address: String, port: Int) {
    thread { startServer(port) }
    startClient(address, port)
}

fun main() {
    val address = "localhost"
    val port = 4444

    when (readlnOrNull()) {
        "server" -> startServer(port)
        "client" -> startClientWithoutAddress(port)
        "both" -> startClientAndServer(address, port)
        else -> println("invalid startup type")
    }
}