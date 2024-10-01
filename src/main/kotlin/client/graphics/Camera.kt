package client.graphics

import client.Client
import common.Config
import common.Debugger
import common.GameEngineProvider
import common.event.servernetworkevents.UpdatePlayerPositionServerEvent
import common.math.Double2
import common.math.Double3
import org.joml.Matrix4d
import kotlin.math.cos
import kotlin.math.sin


class Camera() {
    var upVector = Double3(0, 0, 0)
    var rightVector = Double3(0, 0, 0)
    var position = Double3(0.5, 0.5, 0.5)
        set(value) {
            Debugger.position = value
            Debugger.chunk = client.world.chunkManager.worldPositionToChunkPosition(value)
            field = value
        }
    var rotation = Double3(0, 0, 0)
    var viewMatrix = Matrix4d()
        get() {
            return field.identity()
                .rotateX(rotation.x)
                .rotateY(rotation.y)
                .translate(-position.x, -position.y, -position.z)
        }
    val lookVector: Double3
        get() {
            return Double3(
                cos(-rotation.x) * sin(rotation.y),
                sin(-rotation.x),
                cos(-rotation.x) * -cos(rotation.y)
            )
        }
    var fallSpeed = 0.0 // remove this is temporary lol

//    private var _client: Client? = null
//    private val client: Client
//        get() {
//            if (_client == null)
//                _client = GameEngineProvider.getGameEngine() as Client
//            return _client!!
//        }

    private val client: Client by lazy { GameEngineProvider.getGameEngine() as Client }

    fun addRotation(x: Double, y: Double) {
        rotation += Double3(x, y, 0.0)

        // Limit rotation around X-axis to prevent camera from going upside down
        val clampedX = rotation.x.coerceIn(Math.toRadians(-Config.cameraVerticalAngleLimit.toDouble()), Math.toRadians(Config.cameraVerticalAngleLimit.toDouble()))
        rotation = Double3(clampedX, rotation.y, rotation.z)
    }

    fun addRotation(rotation: Double2) {
        addRotation(rotation.x, rotation.y)
    }

    private fun moveForward(inc: Double) {
        position += Double3(inc * sin(rotation.y), 0.0, -inc * cos(rotation.y))
    }

    private fun moveLeft(inc: Double) {
        position -= Double3(inc * cos(rotation.y), 0.0, inc * sin(rotation.y))
    }

    private fun moveUp(inc: Double) {
        viewMatrix.positiveY(upVector.toVector3d()).mul(inc)
        position += Double3(0.0, inc, 0.0)
    }

    fun addPosition(inc: Double3) {
        moveForward(inc.z)
        moveUp(inc.y)
        moveLeft(inc.x)
    }

    fun setSelectionBox() {
        val raycastResult = client.world.raycast(position, lookVector, Config.characterReachDistance)
        client.mainRenderer.selectionBoxPosition = raycastResult?.first
    }

    fun pollEvents() {
        setSelectionBox()

        val oldPosition = position.copy()
//        val raycastResult = client.world.raycast(position, Double3(0, -1, 0), 1.6)
//        if (raycastResult == null || fallSpeed < 0.0) {
//            fallSpeed = min(fallSpeed + 0.012, 1.2)
//            // position.y -= fallSpeed
//        } else {
//            fallSpeed = 0.0
//            position.y = raycastResult.first.y.toDouble() + 2.55
//        }

        // if (oldPosition != position) {
            // println("sending")
        val updatePositionRequestEvent = UpdatePlayerPositionServerEvent(position)
        val client = GameEngineProvider.getGameEngine() as Client
        client.socketHandler.sendEvent(updatePositionRequestEvent)
        // }
    }
}