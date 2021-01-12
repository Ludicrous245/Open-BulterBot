package net.ldcrF.commands

import com.Ludicrous245.data.Storage
import com.Ludicrous245.io.audio.GuildMusicManager
import com.Ludicrous245.io.audio.PlayerManager
import com.Ludicrous245.io.commands.execute.CommandExecutor
import com.Ludicrous245.io.supporter.Embeded
import com.Ludicrous245.io.supporter.Presets
import com.Ludicrous245.io.supporter.queueManager
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.ldcrF.functions.recorder

class record : CommandExecutor{
    override fun a(args: ArrayList<String>, syntax: String, rawSyntax: String, message: Message, content: String, channel: MessageChannel) {
        if(args.size == 1){
            if(args[0].equals("list")) {
                recorder(message.author, message).sendRecordedList()
            }
            return
        }

        recorder(message.author, message).recordTrack()
    }

    override fun b(): String {
        return "record,rec"
    }

    override fun c(): String {
        return "현재 대기열을 기록합니다. (기록이 이미 있다면, 기존에 기록된 대기열에 덮어쓰기됩니다. 곡이 많으면, 에러가 날 수 있습니다.) !record(rec) list로 기록된 음악들을 확인할 수 있습니다."
    }
}