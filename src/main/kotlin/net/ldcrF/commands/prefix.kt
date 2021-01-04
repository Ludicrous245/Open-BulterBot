package net.ldcrF.commands

import com.Ludicrous245.data.Config
import com.Ludicrous245.io.SQL.SQLConnector
import com.Ludicrous245.io.SQL.SQLNullTester
import com.Ludicrous245.io.SQL.SQLProgressResult
import com.Ludicrous245.io.commands.execute.CommandExecutor
import com.Ludicrous245.io.supporter.Embeded
import com.Ludicrous245.io.supporter.Presets
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel

class prefix:CommandExecutor {
    override fun a(args: ArrayList<String>, syntax: String, rawSyntax: String, message: Message, content: String, channel: MessageChannel) {

        try {
            if (args.size >= 1) {

                val prefixNew = rawSyntax

                if(prefixNew.contains("(") ||prefixNew.contains(")")){
                    message.channel.sendMessage("괄호는 접두사로 설정할 수 없습니다.").queue()
                    return
                }

                if(prefixNew.contains("'")){
                    message.channel.sendMessage("안됩니다.").queue()
                    return
                }

                if (prefixNew.contains("<") && prefixNew.contains(">") && prefixNew.contains("@")) {
                    if (!prefixNew.contains("!")) {
                        message.channel.sendMessage("그 접두사는 절대로 사용할 수 없습니다.").queue()
                        return
                    } else {
                        message.channel.sendMessage("요청하신 문자열을 접두사로 설정할 수 없습니다.").queue()
                        return
                    }
                }

                val sql = SQLConnector(Config.db_url, Config.db_user, Config.db_pw)

                var test = object : SQLNullTester() {
                    override fun run(): SQLProgressResult {
                        if (sql.take("guildPrefix", "guildID", message.guild.id, "prefix") == null) return SQLProgressResult.NULL
                        else return SQLProgressResult.SUCCESS
                    }
                }

                val prefixOld = sql.take("guildPrefix", "guildID", message.guild.id, "prefix")

                if (!sql.isNull(test)) {
                    sql.state.execute("UPDATE guildPrefix SET prefix='${rawSyntax}' WHERE guildID='${message.guild.id}';")
                }

                val eb = Embeded()
                eb.title("접두사 변경")
                eb.color(Presets.normal)
                eb.description("접두사가 기존 $prefixOld 에서 $prefixNew 로 바뀌었습니다.")
                eb.send(message.channel)
            } else {
                message.channel.sendMessage("접두사로 설정할 문자를 입력해주세요").queue()
            }
        }catch (e:Exception){
            message.channel.sendMessage("요청하신 접두사를 처리하던중 문제가 발생하였습니다.").queue()
        }

    }

    override fun b(): String {
        return "prefix"
    }

    override fun c(): String {
        return "!prefix <접두사>로 접두사를 변경합니다."
    }
}