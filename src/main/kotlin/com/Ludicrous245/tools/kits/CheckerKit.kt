package com.Ludicrous245.tools.kits

import java.lang.Exception
import java.net.URL

class CheckerKit {
    companion object{
        fun isURL(text:String):Boolean{
          try{
              val url = URL(text)
              val yul = URL("https://www.youtube.com/")
              val ybul = URL("https://youtu.be/")
              val sul = URL("https://soundcloud.com/")

              if(url.host.equals(yul.host) || url.host.equals(sul.host) || url.host.equals(ybul.host)) {

                  if(text.contains("/channel/")){
                      return false
                  }
                  return true
              }

              return false
            }catch (e:Exception){
                return false
            }

        }


    }
}