package net.ldcrF.commands

import com.Ludicrous245.io.commands.execute.CommandExecutor
import com.Ludicrous245.io.commands.execute.DevCommandExecutor
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel

class invite: CommandExecutor {
    override fun a(args: ArrayList<String>, syntax: String, rawSyntax: String, message: Message, content: String, channel: MessageChannel) {
        message.channel.sendMessage("")
    }

    override fun b(): String {
        return "invite"
    }

    override fun c(): String {
        return "봇 채널의 초대링크를 전송합니다."
    }
}