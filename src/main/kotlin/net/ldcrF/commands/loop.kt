package net.ldcrF.commands

import com.Ludicrous245.data.Storage
import com.Ludicrous245.io.audio.GuildMusicManager
import com.Ludicrous245.io.audio.PlayerManager
import com.Ludicrous245.io.audio.TrackScheduler
import com.Ludicrous245.io.commands.execute.CommandExecutor
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel

class loop : CommandExecutor {
    override fun a(args: ArrayList<String>, syntax: String, rawSyntax: String, message: Message, content: String, channel: MessageChannel) {
        val manager: PlayerManager = PlayerManager().getInstance()
        val gm: GuildMusicManager = manager.getGuildMusicManager(message.guild, message.channel, message)
        val guild = message.guild

        if(!Storage.isLoop.containsKey(guild)){
            Storage.isLoop.put(guild, false)
        }

        if(Storage.isLoop.get(guild)!!){
            channel.sendMessage("반복재생 모드가 꺼졌습니다.").queue()
            Storage.isLoop.put(guild, false)
        }else{
            channel.sendMessage("반복재생 모드가 켜졌습니다.").queue()
            Storage.isLoop.put(guild, true)
        }

}

    override fun b(): String {
        return "loop"
    }

    override fun c(): String {
        return "대기열을 반복합니다."
    }
}