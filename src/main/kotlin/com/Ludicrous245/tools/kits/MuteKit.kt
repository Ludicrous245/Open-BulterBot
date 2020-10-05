package com.Ludicrous245.tools.kits

import com.Ludicrous245.data.Storage

class MuteKit {
    companion object {

        fun isMuted(guildID: String, userID: String):Boolean{
            if (!Storage.guildMutedUser.containsKey(guildID)) {
                Storage.guildMutedUser.put(guildID, ArrayList())
            }

            return Storage.guildMutedUser.get(guildID)!!.contains(userID)
        }

        fun makeMutedUser(guildID: String, userID: String){
            if (!Storage.guildMutedUser.containsKey(guildID)) {
                Storage.guildMutedUser.put(guildID, ArrayList())
            }

            Storage.guildMutedUser.get(guildID)!!.add(userID)
        }

        fun makeUnMutedUser(guildID: String, userID: String){
            if (!Storage.guildMutedUser.containsKey(guildID)) {
                Storage.guildMutedUser.put(guildID, ArrayList())
            }

            Storage.guildMutedUser.get(guildID)!!.remove(userID)
        }
    }
}
