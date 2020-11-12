package com.Ludicrous245.io.xyz

import java.util.*

class RandomManager {
    val random = Random()

    fun createRandom(from:Int, to:Int):Int{
        return random.nextInt(to - from) + from
    }

    fun createRandom(to:Int):Int{
        return random.nextInt(to - 1) + 1
    }

}