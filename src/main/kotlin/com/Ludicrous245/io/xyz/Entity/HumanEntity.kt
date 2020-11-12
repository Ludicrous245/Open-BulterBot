package com.Ludicrous245.io.xyz.Entity

interface HumanEntity {

    var stackByPlaylist:Int
    var stackByPlay:Int
    var stackByCommand:Int

    var warn:Int

    fun setSBP(int: Int)

    fun setSBL(int: Int)

    fun setSBC(int: Int)

}