package net.ldcrF.commands

import com.Ludicrous245.data.Storage
import com.Ludicrous245.io.audio.GuildMusicManager
import com.Ludicrous245.io.audio.PlayerManager
import com.Ludicrous245.io.commands.execute.CommandExecutor
import com.Ludicrous245.io.supporter.Embeded
import com.Ludicrous245.io.supporter.Presets
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel

class nowplaying : CommandExecutor {
    override fun a(args: ArrayList<String>, syntax: String, rawSyntax: String, message: Message, content: String, channel: MessageChannel) {
        val manager: PlayerManager = PlayerManager().getInstance()
        val gm: GuildMusicManager = manager.getGuildMusicManager(message.guild, message.channel, message)

        val track = gm.player.playingTrack

        if (track == null) {
            channel.sendMessage("아무것도 재생중이지 않습니다.").queue()
            return
        }


        val guild = message.guild

        if(!Storage.isLoop.containsKey(guild)){
            Storage.isLoop.put(guild, false)
        }

        val eb = Embeded()

        if(Storage.isLoop.get(guild)!!){
            eb.title("반복 재생중인 음악", track.info.uri)
            eb.field("제목", track.info.title, true)
            eb.field("길이", "약 " + ((track.info.length / 1000) / 60).toString() + "분", true)
            eb.field("업로더", track.info.author, true)
            if (gm.player.isPaused) {
                eb.field("상태", "일시정지됨", true)
            } else {
                eb.field("상태", "재생중", true)
            }
            eb.color(Presets.repeater)
        }else {
            eb.title("재생중인 음악", track.info.uri)
            eb.field("제목", track.info.title, true)
            eb.field("길이", "약 " + ((track.info.length / 1000) / 60).toString() + "분", true)
            eb.field("업로더", track.info.author, true)
            if (gm.player.isPaused) {
                eb.field("상태", "일시정지됨", true)
            } else {
                eb.field("상태", "재생중", true)
            }
            eb.color(Presets.normal)
        }

        eb.send(channel)
    }

    override fun b(): String {
        return "nowplaying,np"
    }

    override fun c(): String {
        return "현재 재생중인 음악을 조회합니다."
    }
}