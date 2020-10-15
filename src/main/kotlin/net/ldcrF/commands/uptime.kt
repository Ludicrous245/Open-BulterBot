package net.ldcrF.commands

import com.Ludicrous245.tools.commands.CommandExecutor
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.ldcrF.functions.UserFunction

class uptime : CommandExecutor {
    override fun a(args: ArrayList<String>, syntax: String, rawSyntax: String, message: Message, content: String, channel: MessageChannel) {
        UserFunction.uptime(message)
    }

    override fun b(): String {
        return "uptime"
    }

    override fun c(): String {
        return "봇의 업타임을 확인합니다."
    }
}