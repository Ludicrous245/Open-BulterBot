package net.ldcrF.commands

import com.Ludicrous245.tools.audio.GuildMusicManager
import com.Ludicrous245.tools.audio.PlayerManager
import com.Ludicrous245.tools.commands.CommandExecutor
import com.Ludicrous245.tools.supporter.Embeded
import com.Ludicrous245.tools.supporter.Presets
import com.Ludicrous245.tools.supporter.queueManager
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.managers.AudioManager

class shuffle : CommandExecutor {
    override fun a(args: ArrayList<String>, syntax: String, rawSyntax: String, message: Message, content: String, channel: MessageChannel) {
        val qm = queueManager()

        if(!qm.isEmpty(message.guild) && qm.getAllQueue(message.guild) != null){
            qm.getAllQueue(message.guild)!!.shuffle()
        }else{
            message.channel.sendMessage("대기열이 비어있습니다.").queue()
            return
        }

        val eb = Embeded()
        eb.title("으아악 어지러워요!")
        eb.description("대기열이 섞였습니다.")
        eb.color(Presets.special)
        eb.send(message.channel)
    }

    override fun b(): String {
        return "shuffle"
    }

    override fun c(): String {
        return "대기열을 무작위로 섞습니다."
    }
}