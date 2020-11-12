package net.ldcrF.commands

import com.Ludicrous245.data.Config
import com.Ludicrous245.io.SQL.SQLConnector
import com.Ludicrous245.io.SQL.SQLTypes
import com.Ludicrous245.io.commands.execute.CommandExecutor
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel

class test:CommandExecutor{
    override fun a(args: ArrayList<String>, syntax: String, rawSyntax: String, message: Message, content: String, channel: MessageChannel) {
        val sql = SQLConnector(Config.db_url, Config.db_user, Config.db_pw)

        //sql.createS("guildPrefix", "", SQLTypes.TEXT)
        //sql.push("Ludicrous245","guildPrefix","test2", "minjae")
        
    }

    override fun b(): String {
        return "test"
    }

    override fun c(): String {
        return "no show"
    }

}