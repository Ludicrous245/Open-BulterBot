package com.Ludicrous245.tools.kits

import java.lang.Exception
import java.net.URL

class CheckerKit {
    companion object{
        fun isURL(text:String):Boolean{
            try{
                URL(text)
                return true
            }catch (e:Exception){
                return false
            }
        }


    }
}