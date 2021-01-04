package net.ldcrF.commands

import com.Ludicrous245.io.commands.execute.CommandExecutor
import com.Ludicrous245.io.commands.execute.DevCommandExecutor
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel

class delete : DevCommandExecutor {
    override fun a(args: ArrayList<String>, syntax: String, rawSyntax: String, message: Message, content: String, channel: MessageChannel) {

    }

    override fun b(): String {
        return "delete"
    }

}