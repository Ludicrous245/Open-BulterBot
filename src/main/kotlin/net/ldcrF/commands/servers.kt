package net.ldcrF.commands

import com.Ludicrous245.data.Storage
import com.Ludicrous245.tools.commands.CommandExecutor
import com.Ludicrous245.tools.supporter.Embeded
import com.Ludicrous245.tools.supporter.Presets
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel

class servers : CommandExecutor {
    override fun a(args: ArrayList<String>, syntax: String, rawSyntax: String, message: Message, content: String, channel: MessageChannel) {

        val servers:Int = Storage.client!!.guilds.size

        val eb = Embeded()

        eb.color(Presets.normal)

        var i = 1
        for(sv in Storage.client!!.guilds){
            eb.field(i.toString() + "번 서버", sv.name)
            i++
        }

        eb.title(servers .toString() + "개의 서버에서 저를 사용하고있어요!")

        eb.send(message.channel)
    }

    override fun b(): String {
        return "servers"
    }

    override fun c(): String {
        return "봇이 접속해 있는 서버를 확인합니다. "
    }
}