package net.ldcrF.commands

import com.Ludicrous245.data.Storage
import com.Ludicrous245.tools.audio.GuildMusicManager
import com.Ludicrous245.tools.audio.PlayerManager
import com.Ludicrous245.tools.commands.CommandExecutor
import com.Ludicrous245.tools.supporter.Embeded
import com.Ludicrous245.tools.supporter.Presets
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel

class queue : CommandExecutor{
    override fun a(args: ArrayList<String>, syntax: String, rawSyntax: String, message: Message, content: String, channel: MessageChannel) {
        val manager: PlayerManager = PlayerManager().getInstance()
        val gm: GuildMusicManager = manager.getGuildMusicManager(message.guild, message.channel, message)
        val queue: ArrayList<AudioTrack>? = gm.scheduler.get()

        val track = gm.player.playingTrack

        if(track == null){
            channel.sendMessage("아무것도 재생중이지 않습니다.").queue()
            return
        }

        val guild = message.guild

        if(!Storage.isLoop.containsKey(guild)){
            Storage.isLoop.put(guild, false)
        }

        val eb = Embeded()

        if(Storage.isLoop.get(guild)!!){
            eb.title("반복 재생 목록")
            eb.field("현재 재생중", track.info.title)
            eb.color(Presets.repeater)

            if (queue == null || queue.isEmpty()) {
                eb.description("반복 재생 목록 없음")
            } else if (5 < queue.size) {
                for (i in 1..5) {
                    eb.description("반복 재생 목록은 최대 5번까지만 출력합니다.")
                    eb.field("목록" + i + " 번", queue.get(i - 1).info.title, true)
                }
                eb.footer("목록 크기 : " + queue.size.toString())
            } else {
                for (i in 1..queue.size) {
                    eb.field("목록 " + i + "번", queue.get(i - 1).info.title, true)
                }
                eb.footer("목록 크기 : " + queue.size.toString())
            }
        }else {
            eb.title("대기열")
            eb.field("현재 재생중", track.info.title)
            eb.color(Presets.normal)

            if (queue == null || queue.isEmpty()) {
                eb.description("대기열 없음")
            } else if (5 < queue.size) {
                for (i in 1..5) {
                    eb.description("대기열은 최대 5번까지만 출력합니다.")
                    eb.field("대기열 " + i + " 번", queue.get(i - 1).info.title, true)
                }
                eb.footer("대기열 크기 : " + queue.size.toString())
            } else {
                for (i in 1..queue.size) {
                    eb.field("대기열 " + i + "번", queue.get(i - 1).info.title, true)
                }
                eb.footer("대기열 크기 : " + queue.size.toString())
            }
        }


        eb.send(channel)

    }

    override fun b(): String {
        return "queue"
    }

    override fun c(): String {
        return "재생중인 대기열을 확인합니다. (최대 5번까지 출력)"
    }
}