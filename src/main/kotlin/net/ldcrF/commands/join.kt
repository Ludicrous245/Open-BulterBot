package net.ldcrF.commands

import com.Ludicrous245.io.commands.execute.CommandExecutor
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.ldcrF.functions.audioF
import java.lang.Exception

class join : CommandExecutor {
    override fun a(args: ArrayList<String>, syntax: String, rawSyntax: String, message: Message, content: String, channel: MessageChannel) {
        if (content == "join") {
            try {
                audioF.join(message, channel)
            } catch (e: Exception) {
                System.out.println("Error occurred to run commands")
                System.out.println("Reason: " + e.toString())

                message.channel.sendMessage("실행오류!").queue()
                message.channel.sendMessage("에러코드:  " + "`" + e.toString() + "`").queue()
            }
        }
    }

    override fun b(): String {
        return "join"
    }

    override fun c(): String {
        return "음성 채널에 접속합니다."
    }

}