package net.ldcrF

import com.Ludicrous245.data.Config
import com.Ludicrous245.data.Storage
import com.Ludicrous245.tools.commands.under.CommonCommandExecutor
import com.Ludicrous245.tools.kits.ConverterKit
import com.Ludicrous245.tools.supporter.Embeded
import com.Ludicrous245.tools.supporter.Presets
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import java.lang.Exception
import java.lang.management.ManagementFactory
import java.lang.management.RuntimeMXBean

class UserFunction1 : CommonCommandExecutor(){
    override fun cmdRun(args: ArrayList<String>, syntax: String, rawSyntax: String, message: Message, content: String, channel: MessageChannel) {
        if(content.equals("ping")){
            ping(message)
        }else if(content.equals("uptime")){
            uptime(message)
        }else if(content.equals("debug")){
            debug(message)
        }else if(content.equals("get")){
            try {
                if (args[0] == "guild") {
                    get(message)
                    return
                } else {

                    if(message.mentionedMembers[0] == null){
                        channel.sendMessage("유저를 찾을 수 없습니다.").queue()
                        return
                    }

                    /*for(members in message.guild.members){
                    if(members.user.id == ConverterKit.convertID(rawSyntax)){
                        System.out.println(members.user.id)
                        get(message, message.guild.getMember(Storage.client!!.getUserById(ConverterKit.convertID(rawSyntax))!!)!!)
                        return
                        break
                    }
                    System.out.println(members.user.id)
                }*/


                    get(message, message.mentionedMembers[0])
                    return
                }
                } catch (e: Exception){
                    //콘솔 보고서 작성
                    System.out.println("Error occurred to run commands")
                    System.out.println("Reason: " + e.toString())
                    //서버 보고서 작성
                    message.channel.sendMessage("실행오류!").queue()
                    message.channel.sendMessage("에러코드:  " + "`" + e.toString() + "`").queue()
                    return
                }

        }else if(content == "picker"){
            if(message.author.id != Config.owner) return
            System.out.println(rawSyntax)
        }
    }

    fun uptime(message: Message){
        val runtime: RuntimeMXBean = ManagementFactory.getRuntimeMXBean()
        val uptimeMS = runtime.uptime
        val uptimeS = runtime.uptime / 1000
        val uptimeM = ((runtime.uptime / 1000) / 60)
        val uptimeH = (((runtime.uptime / 1000) / 60) / 60)
        val uptimeD = ((((runtime.uptime / 1000) / 60) / 60) / 24)

        val eb = Embeded()
        eb.title("업타임")
        eb.field("업타임(ms)", "**"+ uptimeMS + "ms**", true)
        eb.field("업타임(s)", "**"+ uptimeS + "s**", true)
        eb.field("업타임(m)", "**"+ uptimeM + "m**", true)
        eb.field("업타임(h)", "**"+ uptimeH + "h**", true)
        eb.field("업타임(d)", "**"+ uptimeD + "d**", true)
        eb.color(Presets.special)
        eb.send(message.channel)
    }

    fun ping(message: Message){
        val ping = Storage.client!!.gatewayPing
        var now = "집계중"
        var color:Int

        if(1250 <= ping){
            now = "지옥"
            color = 0x000000
        }else if(1050 <= ping){
            now = "매우 나쁨"
            color = 0xf74545
        }else if(750 <= ping){
            now = "나쁨"
            color = 0xfc7553
        }else if(450 <= ping){
            now = "꽤 나쁨"
            color = 0xfc9c53
        }else if(350 <= ping){
            now = "보통"
            color = 0xffe359
        }else if(250 <= ping){
            now = "꽤 좋음"
            color = 0xc5ff59
        }else if(150 <= ping){
            now = "좋음"
            color = 0x59ff90
        }else if(70 <= ping){
            now = "매우 좋음"
            color = 0x59d6ff
        }else{
            now = "천국"
            color = 0xfcfcfc
        }

        val eb = Embeded()
        eb.title("핑")
        eb.field("핑(ms)", "**"+ ping + "ms**", true)
        eb.field("상태", now, true)
        eb.color(color)
        eb.send(message.channel)
    }

    fun debug(message: Message){
        if(message.author.id == Config.owner){
            Config.debug = true
            Storage.client!!.presence.activity = Activity.playing("디버그 모드입니다. 현재 디버그중인 커맨드는 " + Config.debugging + " 입니다. 사용시 유의해주세요!")
            message.channel.sendMessage("디버그모드로 변경되었습니다.").queue()
        }
    }

    fun get(message: Message, user: Member?){
        val eb = Embeded()
        eb.title("**" + user!!.user.name + "** 님의 정보")
        if(user.user.avatarUrl != null) {
            eb.thumb(user.user.avatarUrl!!)
            eb.field("아바타 주소", user.user.avatarUrl!!, true)
        }else{
            eb.field("아바타 주소", "아바타가 없습니다.", true)
        }
        eb.field("id", user.user.id, true)
        eb.field("태그", user.user.asTag, true)
        eb.color(Presets.normal)
        eb.send(message.channel)
    }

    fun get(message: Message){
        val eb = Embeded()
        eb.title("**" + message.guild.name + "** 의 정보")
        if(message.guild.iconUrl != null) {
            eb.thumb(message.guild.iconUrl!!)
            eb.field("아이콘 주소", message.guild.iconUrl!!, true)
        }else{
            eb.field("아이콘 주소", "아이콘이 없습니다.", true)
        }
        eb.field("id", message.guild.id, true)
        val count1 = message.guild.memberCount
        val count2 = message.guild.textChannels.size
        val count3 = message.guild.voiceChannels.size

        eb.field("유저 수", count1.toString(), true)
        eb.field("총 관리자", message.guild.owner!!.user.asTag)
        eb.field("채널 수(채팅)", count2.toString(), true)
        eb.field("채널 수(음성)", count3.toString(), true)
        eb.color(Presets.normal)
        eb.send(message.channel)
    }
}