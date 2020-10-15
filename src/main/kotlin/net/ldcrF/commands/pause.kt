package net.ldcrF.commands

import com.Ludicrous245.data.Storage
import com.Ludicrous245.tools.audio.GuildMusicManager
import com.Ludicrous245.tools.audio.PlayerManager
import com.Ludicrous245.tools.commands.CommandExecutor
import com.Ludicrous245.tools.commands.CommandRegisterer
import com.Ludicrous245.tools.supporter.Embeded
import com.Ludicrous245.tools.supporter.Presets
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel

class pause : CommandExecutor{
    override fun a(args: ArrayList<String>, syntax: String, rawSyntax: String, message: Message, content: String, channel: MessageChannel) {
        val manager: PlayerManager = PlayerManager().getInstance()
        val gm: GuildMusicManager = manager.getGuildMusicManager(message.guild, message.channel, message)

        if(!Storage.isPause.containsKey(message.guild)){
            Storage.isPause.put(message.guild, false)
        }

        if(Storage.isPause.get(message.guild)!!){
            gm.player.isPaused = false

            Storage.isPause.put(message.guild, false)
            channel.sendMessage("일시정지가 해제되었습니다.").queue()
        }else{
            gm.player.isPaused = true

            Storage.isPause.put(message.guild, true)
            channel.sendMessage("일시정지 되었습니다.").queue()
        }
    }

    override fun b(): String {
        return "pause"
    }

    override fun c(): String {
        return "재생중인 음악을 일시정지 시킵니다."
    }
}