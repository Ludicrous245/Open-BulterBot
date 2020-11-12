package com.Ludicrous245.management

import com.Ludicrous245.data.Config
import com.Ludicrous245.data.Storage
import net.dv8tion.jda.api.entities.Message

class BannedUser {
    companion object {
        fun register() {

        }

        private fun banP(id:Long){
            Storage.bannedUsr.add(id)
        }

        fun ban(id:Long, message: Message){
            if(!isBanned(id)) {
                Storage.bannedUsr.add(id)

                val pc = Storage.client!!.getUserById(Config.owner)!!.openPrivateChannel().complete()

                pc.sendMessage(id.toString() + " 님이 밴당했습니다.").queue()
                message.channel.sendMessage(message.author.asTag + "님은 밴되었습니다.").queue()
            }
        }

        fun isBanned(id:Long):Boolean{
            return Storage.bannedUsr.contains(id)
        }
    }
}