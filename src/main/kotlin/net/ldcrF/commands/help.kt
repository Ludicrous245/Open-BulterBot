package net.ldcrF.commands

import com.Ludicrous245.data.Config
import com.Ludicrous245.data.Storage
import com.Ludicrous245.data.botData
import com.Ludicrous245.io.SQL.SQLConnector
import com.Ludicrous245.io.SQL.SQLNullTester
import com.Ludicrous245.io.SQL.SQLProgressResult
import com.Ludicrous245.io.commands.execute.CommandExecutor
import com.Ludicrous245.io.commands.execute.DevCommandExecutor
import com.Ludicrous245.io.supporter.Embeded
import com.Ludicrous245.io.supporter.Presets
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel

class help : CommandExecutor {
        override fun a(args: ArrayList<String>, syntax: String, rawSyntax: String, message: Message, content: String, channel: MessageChannel) {

        val embed = Embeded()
        embed.title("도움말")
        embed.color(Presets.special)

            val sql = SQLConnector(Config.db_url, Config.db_user, Config.db_pw)

            var test = object : SQLNullTester(){
                override fun run(): SQLProgressResult {
                    if(sql.take("guildPrefix", "guildID", message.guild.id, "prefix") == null) return SQLProgressResult.NULL
                    else return SQLProgressResult.SUCCESS
                }
            }

            if(sql.isNull(test)){
                sql.state.execute("INSERT INTO `BulterBot`.`guildPrefix`  VALUES ('${message.guild.id}', '$');")
            }

            var prefix = sql.take("guildPrefix", "guildID", message.guild.id, "prefix")!!

            if(Config.dev){
                val st = "%" + prefix
                prefix = st
            }

        for(cmd in Storage.commands) {
            if (cmd is CommandExecutor) {
                if (cmd.b().contains(",")) {
                    if (cmd.c().contains("!")) {
                        embed.field(prefix + cmd.b().replace(",", " 또는 "), cmd.c().replace("!", prefix))
                    } else {
                        embed.field(prefix + cmd.b().replace(",", " 또는 "), cmd.c())
                    }
                } else {
                    embed.field(prefix + cmd.b(), cmd.c().replace("!", prefix))
                }
            }
        }
            embed.footer("now version in ${botData.version}")
            embed.send(message.channel)
    }

    override fun b(): String {
        return "help"
    }

    override fun c(): String {
        return "도움말을 확인합니다."
    }
}