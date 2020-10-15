package net.ldcrF.commands

import com.Ludicrous245.data.Storage
import com.Ludicrous245.tools.audio.PlayerManager
import com.Ludicrous245.tools.commands.CommandExecutor
import com.Ludicrous245.tools.supporter.Embeded
import com.Ludicrous245.tools.supporter.Presets
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import java.lang.Exception

class volume : CommandExecutor{
    override fun a(args: ArrayList<String>, syntax: String, rawSyntax: String, message: Message, content: String, channel: MessageChannel) {
        if(Storage.serverVol.get(message.guild) == null){
            Storage.serverVol.put(message.guild, 50)
        }

        if(args.size == 0){
            val eb: Embeded = Embeded()
            eb.color(0x008CCD)
            eb.field("현재 볼륨", Storage.serverVol.get(message.guild)!!.toString())
            eb.title("볼륨 마법사")
            eb.send(channel)
        }else if(args.size == 1){
            try {
                if(100 < args[0].toInt()){
                    val eb: Embeded = Embeded()
                    eb.color(Presets.alert)
                    eb.description("볼륨이 너무 커요")
                    eb.title("어우 내귀!!")
                    eb.send(channel)
                    return
                }else if(args[0].toInt() < 0){
                    val eb: Embeded = Embeded()
                    eb.color(Presets.alert)
                    eb.description("볼륨이 너무 작아요")
                    eb.title("지금 뭐 나오고있냐?")
                    eb.send(channel)
                    return
                }

                Storage.serverVol.put(message.guild, args[0].toInt())

                val manager: PlayerManager = PlayerManager().getInstance()

                manager.getGuildMusicManager(message.guild, message.channel, message).player.volume = Storage.serverVol.get(message.guild)!!

                val eb: Embeded = Embeded()
                eb.color(Presets.special)
                eb.description("볼륨이 적용되었습니다.")
                eb.title("볼륨 마법사")
                eb.footer("시간이 소요되거나 다음곡부터 적용될 가능성 있음.")
                eb.send(channel)

            }catch (e: Exception){
                val eb: Embeded = Embeded()
                eb.color(Presets.alert)
                eb.description("혹시 볼륨 대신 이상한걸 적으셨나요?")
                eb.title("이상한걸 먹은것 같아요...")
                eb.send(channel)
                return
            }
        }else{
            message.channel.sendMessage("뭐 잘못치신거 아닙니까? ㅋㅋㅋ").queue()
            return
        }
    }

    override fun b(): String {
        return "volume,vol"
    }

    override fun c(): String {
        return "재생중인 음악의 소리 크기를 조절합니다."
    }
}