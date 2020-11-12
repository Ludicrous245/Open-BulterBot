package net.ldcrF.commands

import com.Ludicrous245.io.commands.execute.CommandExecutor
import com.Ludicrous245.io.supporter.Embeded
import com.Ludicrous245.io.supporter.Presets
import com.Ludicrous245.io.supporter.queueManager
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel

class reverse : CommandExecutor {
    override fun a(args: ArrayList<String>, syntax: String, rawSyntax: String, message: Message, content: String, channel: MessageChannel) {
        val qm = queueManager()

        if(!qm.isEmpty(message.guild) && qm.getAllQueue(message.guild) != null){
            qm.getAllQueue(message.guild)!!.reverse()
        }else{
            message.channel.sendMessage("대기열이 비어있습니다.").queue()
            return
        }

        val eb = Embeded()
        eb.title("피가 거꾸로 솟는다!!")
        eb.description("대기열이 뒤집혔습니다.")
        eb.color(Presets.special)
        eb.send(message.channel)
    }

    override fun b(): String {
        return "reverse"
    }

    override fun c(): String {
        return "대기열을 뒤집습니다."
    }
}