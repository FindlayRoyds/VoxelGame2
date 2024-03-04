package common.player

import common.math.Float3

class Player(var userID: Int, var username: String) {
    var position = Float3(0f, 0f, 0f)
}