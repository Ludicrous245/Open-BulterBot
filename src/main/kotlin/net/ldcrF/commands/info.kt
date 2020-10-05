package net.ldcrF.commands

import com.Ludicrous245.data.Storage
import com.Ludicrous245.data.botData
import com.Ludicrous245.tools.commands.under.CommonCommandExecutor
import com.Ludicrous245.tools.supporter.Embeded
import com.Ludicrous245.tools.supporter.Presets
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel

class info : CommonCommandExecutor() {

    override fun cmdRun(args: ArrayList<String>, syntax: String, rawSyntax: String, message: Message, content: String, channel: MessageChannel) {
        if(content.equals("info")){
            info(message, channel)
        }
    }

    fun info(message: Message, channel: MessageChannel){
        val prefix:String = Storage.guildPrefix.get(message.guild.id)!!
        val eb: Embeded = Embeded()
        eb.title("ChrolBot")
        eb.description("크롤봇")
        eb.color(Presets.normal)
        eb.field("버전",botData.version)
        eb.field("제작자","Ludicrous245")
        eb.field("현재 접두사", prefix)
        eb.field("도움말", prefix+"help")
        eb.field("작성 언어","Kotlin")
        eb.send(channel)

    }

}