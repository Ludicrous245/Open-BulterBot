package net.ldcrF.functions

import com.Ludicrous245.data.Storage
import com.Ludicrous245.tools.commands.CommandExecutor
import com.Ludicrous245.tools.kits.ConverterKit
import com.Ludicrous245.tools.kits.MuteKit
import com.Ludicrous245.tools.supporter.Embeded
import com.Ludicrous245.tools.supporter.Presets
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.*
import java.lang.Exception

class guildManagerFunctions : CommandExecutor() {

    override fun cmdRun(args: ArrayList<String>, syntax: String, rawSyntax: String, message: Message, content: String, channel: MessageChannel) {
        if (content.equals("mute")) {
            try {
                if(!message.member!!.permissions.contains(Permission.ADMINISTRATOR)){
                    message.channel.sendMessage("관리자 전용 기능입니다.").queue()
                    return
                }

                if (args.size == 0) {
                    channel.sendMessage("잘못된 사용방법입니다").queue()
                } else {
                    if(Storage.client!!.getUserById(ConverterKit.convertID(rawSyntax)) == null){
                        channel.sendMessage("유저를 찾을 수 없습니다.").queue()
                        return
                    }

                    if(Storage.client!!.getUserById(ConverterKit.convertID(rawSyntax))!!.isBot){
                        channel.sendMessage("봇은 뮤트할 수 없습니다.").queue()
                        return
                    }

                    MuteKit.makeMutedUser(message.guild.id, ConverterKit.convertID(rawSyntax))
                    message.channel.sendMessage("뮤트됨 " + rawSyntax).queue()
                }
            }catch (e:Exception){
                //콘솔 보고서 작성
                System.out.println("Error occurred to run commands")
                System.out.println("Reason: " + e.toString())
                //서버 보고서 작성
                message.channel.sendMessage("실행오류!").queue()
                message.channel.sendMessage("에러코드:  " + "`" + e.toString() + "`").queue()
                return
            }
        }else if(content.equals("unmute")){
            if(!message.member!!.permissions.contains(Permission.ADMINISTRATOR)){
                message.channel.sendMessage("관리자 전용 기능입니다.").queue()
                return
            }
            try {
                if (args.size == 0) {
                    channel.sendMessage("잘못된 사용방법입니다").queue()
                } else {
                    if(Storage.client!!.getUserById(ConverterKit.convertID(rawSyntax)) == null){
                        channel.sendMessage("유저를 찾을 수 없습니다.").queue()
                        return
                    }

                    if(Storage.client!!.getUserById(ConverterKit.convertID(rawSyntax))!!.isBot){
                        channel.sendMessage("봇은 뮤트할 수 없습니다.").queue()
                        return
                    }

                    MuteKit.makeUnMutedUser(message.guild.id, ConverterKit.convertID(rawSyntax))
                    message.channel.sendMessage("뮤트 해제됨 " + rawSyntax).queue()
                }
            }catch (e:Exception){
                //콘솔 보고서 작성
                System.out.println("Error occurred to run commands")
                System.out.println("Reason: " + e.toString())
                //서버 보고서 작성
                message.channel.sendMessage("실행오류!").queue()
                message.channel.sendMessage("에러코드:  " + "`" + e.toString() + "`").queue()
                return
            }
        }else if(content.equals("kick")){
            if(args.size == 0){
                channel.sendMessage("잘못된 사용방법입니다").queue()
            }else{
                try{
                    if(message.mentionedMembers[0] == null){
                        channel.sendMessage("유저를 찾을 수 없습니다.").queue()
                        return
                    }

                    kick(message, message.mentionedMembers[0], null)

                }catch (e:Exception){
                    //콘솔 보고서 작성
                    System.out.println("Error occurred to run commands")
                    System.out.println("Reason: " + e.toString())
                    //서버 보고서 작성
                    message.channel.sendMessage("실행오류!").queue()
                    message.channel.sendMessage("에러코드:  " + "`" + e.toString() + "`").queue()
                    return
                }
            }
        }else if(content.equals("prefix")){
            if(args.size == 0){
                channel.sendMessage("잘못된 사용방법입니다").queue()
            }else{
                prefix(message, syntax)
            }
        }
    }

    fun prefix(message:Message, prefix:String){
        val prefixOrigin:String = Storage.guildPrefix.get(message.guild.id)!!
        if(prefixOrigin == prefix){
            message.channel.sendMessage("이미 해당 접두사로 설정되어있습니다.").queue()
            return
        }

        if(10 < prefix.length){
            message.channel.sendMessage("너무 깁니다. 10자 이내로 설정해주세요.").queue()
            return
        }

        if(prefix.contains("prefix") || prefix.contains("p")){
            message.channel.sendMessage("그렇게하면 이 방에서 이 봇을 사용할 수 없게 될겁니다.").queue()
            return
        }

        Storage.guildPrefix.put(message.guild.id, prefix)
        val eb = Embeded()
        eb.title("접두사 변경 안내")
        eb.description("접두사가 기존 " + prefixOrigin + " 에서 " + prefix + " (으)로 변경되었습니다.")
        eb.color(Presets.special)
        eb.send(message.channel)
    }

    fun kick(message: Message, user:Member, reason: String?){
        if(user.user.isBot){
            message.channel.sendMessage("봇은 추방할 수 없습니다.").queue()
            return
        }

        if(reason == null){
            user.kick().queue()
            message.channel.sendMessage("해당 유저를 추방했습니다.").queue()
        }else{
            user.kick(reason)
            message.channel.sendMessage("해당 유저를 다음 이유로 추방했습니다. " + reason).queue()
        }
    }


}