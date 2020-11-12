package net.ldcrF.commands

import com.Ludicrous245.data.Storage
import com.Ludicrous245.io.SQL.SQLConnector
import com.Ludicrous245.io.commands.execute.CommandExecutor
import com.Ludicrous245.io.supporter.Embeded
import com.Ludicrous245.io.supporter.Presets
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel

class help : CommandExecutor {
        override fun a(args: ArrayList<String>, syntax: String, rawSyntax: String, message: Message, content: String, channel: MessageChannel) {

        val embed = Embeded()
        embed.title("도움말")
        embed.color(Presets.special)
        for(cmd in Storage.commands){
            if(cmd.b().contains(",")){
                embed.field("$" + cmd.b().replace(",", " 또는 "), cmd.c())
            }else {
                embed.field("$" + cmd.b(), cmd.c().replace("!", "$"))
            }
        }
        embed.send(message.channel)
    }

    override fun b(): String {
        return "help"
    }

    override fun c(): String {
        return "도움말을 확인합니다."
    }
}