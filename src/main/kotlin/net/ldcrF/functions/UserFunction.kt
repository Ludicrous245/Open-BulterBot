package net.ldcrF.functions

import com.Ludicrous245.data.Config
import com.Ludicrous245.data.Storage
import com.Ludicrous245.data.botData
import com.Ludicrous245.io.supporter.Embeded
import com.Ludicrous245.io.supporter.Presets
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import java.lang.management.ManagementFactory
import java.lang.management.RuntimeMXBean

class UserFunction{

    companion object {

        fun uptime(message: Message) {
            val runtime: RuntimeMXBean = ManagementFactory.getRuntimeMXBean()
            val uptimeMS = runtime.uptime
            val uptimeS = runtime.uptime / 1000
            val uptimeM = ((runtime.uptime / 1000) / 60)
            val uptimeH = (((runtime.uptime / 1000) / 60) / 60)
            val uptimeD = ((((runtime.uptime / 1000) / 60) / 60) / 24)

            val eb = Embeded()
            eb.title("업타임")
            eb.field("업타임(ms)", "**" + uptimeMS + "ms**", true)
            eb.field("업타임(s)", "**" + uptimeS + "s**", true)
            eb.field("업타임(m)", "**" + uptimeM + "m**", true)
            eb.field("업타임(h)", "**" + uptimeH + "h**", true)
            eb.field("업타임(d)", "**" + uptimeD + "d**", true)
            eb.color(Presets.special)
            eb.send(message.channel)
        }

        fun ping(message: Message) {


            //message.channel.send("퐁! " + "`" + ping + "`") 이거 적어 ㅇㅋ?

            val ping = Storage.client!!.gatewayPing
            var now = "집계중"
            var color: Int

            if (1250 <= ping) {
                now = "지옥"
                color = 0x000000
            } else if (1050 <= ping) {
                now = "매우 나쁨"
                color = 0xf74545
            } else if (750 <= ping) {
                now = "나쁨"
                color = 0xfc7553
            } else if (450 <= ping) {
                now = "꽤 나쁨"
                color = 0xfc9c53
            } else if (350 <= ping) {
                now = "보통"
                color = 0xffe359
            } else if (250 <= ping) {
                now = "꽤 좋음"
                color = 0xc5ff59
            } else if (150 <= ping) {
                now = "좋음"
                color = 0x59ff90
            } else if (70 <= ping) {
                now = "매우 좋음"
                color = 0x59d6ff
            } else {
                now = "천국"
                color = 0xfcfcfc
            }

            val eb = Embeded()
            eb.title("퐁!")
            eb.field("핑(ms)", "**" + ping + "ms**", true)
            eb.field("상태", now, true)
            eb.color(color)
            eb.send(message.channel)
        }

        fun get(message: Message, user: Member?) {
            val eb = Embeded()
            eb.title("**" + user!!.user.name + "** 님의 정보")
            if (user.user.avatarUrl != null) {
                eb.thumb(user.user.avatarUrl!!)
                eb.field("아바타 주소", user.user.avatarUrl!!, true)
            } else {
                eb.field("아바타 주소", "아바타가 없습니다.", true)
            }
            eb.field("id", user.user.id, true)
            eb.field("태그", user.user.asTag, true)
            eb.color(Presets.normal)
            eb.send(message.channel)
        }

        fun get(message: Message) {
            val eb = Embeded()
            eb.title("**" + message.guild.name + "** 의 정보")
            if (message.guild.iconUrl != null) {
                eb.thumb(message.guild.iconUrl!!)
                eb.field("아이콘 주소", message.guild.iconUrl!!, true)
            } else {
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

        fun info(message: Message, channel: MessageChannel){
            val eb: Embeded = Embeded()
            eb.title("Bulterbot")
            eb.color(Presets.normal)
            eb.field("버전", botData.version)
            eb.field("제작자","Ludicrous245(Myuk)")
            eb.field("도움말", "$"+"help")
            eb.field("작성 언어","Kotlin")
            eb.send(channel)

        }
    }
}