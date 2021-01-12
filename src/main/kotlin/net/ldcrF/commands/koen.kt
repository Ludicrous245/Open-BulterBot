package net.ldcrF.commands

import com.Ludicrous245.io.commands.execute.CommandExecutor
import com.github.kimcore.inko.Inko.Companion.asEnglish
import com.github.kimcore.inko.Inko.Companion.asKorean
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel

class koen : CommandExecutor {

    override fun a(args: ArrayList<String>, syntax: String, rawSyntax: String, message: Message, content: String, channel: MessageChannel) {
        if(args.size == 0){
            return
        }

        message.channel.sendMessage(syntax.asEnglish).queue()
    }

    override fun b(): String {
        return "koen"
    }

    override fun c(): String {
       return "영어를 치고싶었으나, 실수로 입력해버린 한국어 타자를 영어로 변환합니다"
    }
}