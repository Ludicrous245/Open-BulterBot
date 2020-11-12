package net.ldcrF.commands

import com.Ludicrous245.io.commands.execute.CommandExecutor
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.ldcrF.functions.UserFunction

class ping : CommandExecutor {
    override fun a(args: ArrayList<String>, syntax: String, rawSyntax: String, message: Message, content: String, channel: MessageChannel) {
        UserFunction.ping(message)
    }

    override fun b(): String {
        return "ping"
    }

    override fun c(): String {
        return "봇의 응답 속도를 측정합니다."
    }

}