package net.ldcrF.commands

import com.Ludicrous245.data.Storage
import com.Ludicrous245.tools.commands.CommandExecutor
import com.Ludicrous245.tools.supporter.Embeded
import com.Ludicrous245.tools.supporter.Presets
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.ldcrF.functions.UserFunction1
import java.sql.Connection
import java.sql.DriverManager

class dev : CommandExecutor(){
    var con:Connection? = null;

    override fun cmdRun(args: ArrayList<String>, syntax: String, rawSyntax: String, message: Message, content: String, channel: MessageChannel) {

    }

}