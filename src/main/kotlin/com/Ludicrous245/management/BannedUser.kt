package com.Ludicrous245.management

import com.Ludicrous245.data.Config
import com.Ludicrous245.data.Storage
import com.Ludicrous245.io.SQL.SQLConnector
import net.dv8tion.jda.api.entities.Message

class BannedUser {
    companion object {

        fun ban(id:String, message: Message){
            if(!isBanned(id)) {
                val sql = SQLConnector(Config.db_url, Config.db_user, Config.db_pw)

                message.channel.sendMessage(message.author.asTag + "님은 밴되었습니다.").queue()

                sql.update("register", "isBanned", "yes", "id", message.author.id)
            }
        }

        fun isBanned(id:String):Boolean{
            val sql = SQLConnector(Config.db_url, Config.db_user, Config.db_pw)

            val isban = sql.take("register", "id", id, "isBanned")

            return isban == "yes"
        }
    }
}