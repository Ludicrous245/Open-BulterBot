package net.ldcrF.devCMD

import com.Ludicrous245.io.commands.execute.DevCommandExecutor
import com.Ludicrous245.management.BannedUser
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel

class test:DevCommandExecutor{
    override fun a(args: ArrayList<String>, syntax: String, rawSyntax: String, message: Message, content: String, channel: MessageChannel) {
        message.channel.sendMessage("a").queue()
    }

    override fun b(): String {
        return "test"
    }

}