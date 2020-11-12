package com.Ludicrous245.io.xyz.Entity

import com.Ludicrous245.io.xyz.GachaMemberType

interface GachaMember {
    var name:String

    fun gn():String{
        return name
    }

    fun gt(): GachaMemberType
}