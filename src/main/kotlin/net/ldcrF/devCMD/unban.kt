package net.ldcrF.devCMD

import com.Ludicrous245.data.Config
import com.Ludicrous245.io.SQL.SQLConnector
import com.Ludicrous245.io.commands.execute.DevCommandExecutor
import com.Ludicrous245.io.kits.CheckerKit
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel


class unban:DevCommandExecutor {
    override fun a(args: ArrayList<String>, syntax: String, rawSyntax: String, message: Message, content: String, channel: MessageChannel) {
        val sql = SQLConnector(Config.db_url, Config.db_user, Config.db_pw)

        sql.update("register", "isBanned", "no", "id", args[0])
    }

    override fun b(): String {
        return "unban"
    }

}