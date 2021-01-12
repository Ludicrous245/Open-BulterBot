package net.ldcrF.commands

import com.Ludicrous245.io.commands.execute.CommandExecutor
import com.github.kimcore.inko.Inko.Companion.asKorean
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel

class enko : CommandExecutor{

    override fun a(args: ArrayList<String>, syntax: String, rawSyntax: String, message: Message, content: String, channel: MessageChannel) {
        if(args.size == 0){
            return
        }

        message.channel.sendMessage(syntax.asKorean).queue()
    }

    override fun b(): String {
        return "enko"
    }

    override fun c(): String {
        return "한국를 치고싶었으나, 실수로 입력해버린 영어 타자를 한국어 로변환합니다"
    }


}