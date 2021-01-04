package com.Ludicrous245.io.kits

import com.Ludicrous245.data.Config
import com.Ludicrous245.io.SQL.SQLConnector
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

        fun isOP(id:String):Boolean{
            val sql = SQLConnector(Config.db_url, Config.db_user, Config.db_pw)
            val isop = sql.take("register", "id", id, "isOP")!!

            return isop == "yes"
        }
    }
}