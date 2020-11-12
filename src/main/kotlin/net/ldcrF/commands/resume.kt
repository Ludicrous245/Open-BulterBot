package net.ldcrF.commands

import com.Ludicrous245.io.audio.GuildMusicManager
import com.Ludicrous245.io.audio.PlayerManager
import com.Ludicrous245.io.commands.execute.CommandExecutor
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel

class resume : CommandExecutor {
    override fun a(args: ArrayList<String>, syntax: String, rawSyntax: String, message: Message, content: String, channel: MessageChannel) {
        val manager: PlayerManager = PlayerManager().getInstance()
        val gm: GuildMusicManager = manager.getGuildMusicManager(message.guild, message.channel, message)

        gm.player.isPaused = false

        channel.sendMessage("일시정지가 해제되었습니다.").queue()
    }

    override fun b(): String {
        return "resume,rs"
    }

    override fun c(): String {
        return "일시정지된 곡을 다시 재생합니다."
    }
}