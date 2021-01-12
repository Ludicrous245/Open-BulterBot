package net.ldcrF.commands

import com.Ludicrous245.data.Config
import com.Ludicrous245.data.Storage
import com.Ludicrous245.io.audio.GuildMusicManager
import com.Ludicrous245.io.audio.PlayerManager
import com.Ludicrous245.io.commands.execute.CommandExecutor
import com.Ludicrous245.io.kits.CheckerKit
import com.Ludicrous245.io.supporter.queueManager
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.managers.AudioManager

class stop : CommandExecutor {
    override fun a(args: ArrayList<String>, syntax: String, rawSyntax: String, message: Message, content: String, channel: MessageChannel) {
        if (!CheckerKit.isOP(message.author.id)) {
            if (!message.member!!.permissions.contains(Permission.ADMINISTRATOR)) {
                message.channel.sendMessage("관리자 전용 기능입니다.").queue()
                return
            }
        }

        if (!Storage.isLoading.containsKey(message.author)) {
            Storage.isLoading.put(message.author, 0)
        }

        if (Storage.isLoading.get(message.author) == 0) {

            channel.sendMessage("대기열을 비우고, 모든 재생을 중단합니다.").queue()

            val manager: PlayerManager = PlayerManager().getInstance()
            val gm: GuildMusicManager = manager.getGuildMusicManager(message.guild, message.channel, message)
            val qm = queueManager()
            val am: AudioManager = message.guild.audioManager

            if (!qm.isEmpty(message.guild)) {
                qm.clear(message.guild)
            }

            am.closeAudioConnection()
            gm.player.stopTrack()
            gm.player.isPaused = false
        }else{
            message.channel.sendMessage("아직 작업이 모두 끝나지 않았습니다. 잠시 후에 다시 시도해주세요!").queue()
        }
    }

    override fun b(): String {
        return "stop"
    }

    override fun c(): String {
        return "대기열을 비우고, 노래를 종료한 뒤 음성 채널에서 나갑니다.(길드 관리자 권한 필요)"
    }
}