package common.player

import common.math.Double3

class Player(var userID: Int, var username: String) {
    var position = Double3(0, 0, 0)
}