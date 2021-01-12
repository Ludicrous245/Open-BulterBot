package net.ldcrF.commands

import com.Ludicrous245.data.Storage
import com.Ludicrous245.io.audio.GuildMusicManager
import com.Ludicrous245.io.audio.PlayerManager
import com.Ludicrous245.io.audio.TrackScheduler
import com.Ludicrous245.io.commands.execute.CommandExecutor
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel

class skip : CommandExecutor {
    override fun a(args: ArrayList<String>, syntax: String, rawSyntax: String, message: Message, content: String, channel: MessageChannel) {
        val manager: PlayerManager = PlayerManager().getInstance()
        val gm: GuildMusicManager = manager.getGuildMusicManager(message.guild, message.channel, message)
        val scheduler: TrackScheduler = gm.scheduler
        val player = gm.player

        if (!Storage.isLoading.containsKey(message.author)) {
            Storage.isLoading.put(message.author, 0)
        }

        if (Storage.isLoading.get(message.author) == 0) {

            if (Storage.isLoop.get(message.guild)!!) {
                if (scheduler.get() == null || scheduler.get()!!.isEmpty()) {
                    message.channel.sendMessage("한곡만 반복재생중일 경우, 스킵이 불가능합니다. 반복재생 모드를 종료한 후, 다시 입력해주세요.").queue()
                    return
                }
            }

            if (player.playingTrack == null) {
                message.channel.sendMessage("재생중인 항목이 없습니다.").queue()
                return
            }

            scheduler.next()
        }else{
            message.channel.sendMessage("아직 작업이 모두 끝나지 않았습니다. 잠시 후에 다시 시도해주세요!").queue()
        }

    }

    override fun b(): String {
        return "skip"
    }

    override fun c(): String {
        return "재생중인 음악을 건너뜁니다."
    }
}