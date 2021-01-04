package net.ldcrF.commands

import com.Ludicrous245.io.commands.execute.CommandExecutor
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.ldcrF.functions.UserFunction

class get : CommandExecutor {
    override fun a(args: ArrayList<String>, syntax: String, rawSyntax: String, message: Message, content: String, channel: MessageChannel) {
        if(args.size == 0){
            UserFunction.get(message, message.member)
        }else if(args.size == 1){
                if (args[0].equals("guild")) {
                    UserFunction.get(message)
                } else {
                    UserFunction.get(message, message.mentionedMembers.get(0))
                }
        }else{
            return
        }
    }

    override fun b(): String {
        return "get"
    }

    override fun c(): String {
        return "!get 으로 자신의 정보를 확인합니다. !get guild로 현재 길드의 정보를 확인합니다. !get <@유저>로 헤당 유저의 정보를 확인합니다."
    }
}