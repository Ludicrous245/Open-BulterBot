package net.ldcrF.commands

import com.Ludicrous245.data.Storage
import com.Ludicrous245.io.audio.PlayerManager
import com.Ludicrous245.io.commands.execute.CommandExecutor
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.ldcrF.functions.audioF
import net.ldcrF.functions.recorder

class replay : CommandExecutor {
    override fun a(args: ArrayList<String>, syntax: String, rawSyntax: String, message: Message, content: String, channel: MessageChannel) {

        if (!Storage.isLoading.containsKey(message.author)) {
            Storage.isLoading.put(message.author, 0)
        }

        if (Storage.isLoading.get(message.author) == 0) {

            recorder(message.author, message).loadTracks()

            if (!Storage.serverQueueRecord.containsKey(message.author)) {
                Storage.serverQueueRecord.put(message.author, ArrayList())
                Storage.serverQueueRecordTask.put(message.author, ArrayList())
            }

            val recordedQueue = Storage.serverQueueRecord.get(message.author)!!

            var msg: Message

            if (recordedQueue.isEmpty()) {
                message.channel.sendMessage("기록된 대기열이 없습니다.").queue()
                return
            } else {
                msg = message.channel.sendMessage("기록 불러오는중").complete()
            }

            if (!message.guild.selfMember.voiceState!!.inVoiceChannel()) {
                if (!message.member!!.voiceState!!.inVoiceChannel()) {
                    channel.sendMessage("음성채널에 입장하셔야 사용하실 수 있습니다.").queue()
                    return
                }
                audioF.join(message, channel)
            }

            Storage.isLoading.put(message.author, recordedQueue.size)

            for (i in recordedQueue) {
                var manager: PlayerManager = PlayerManager().getInstance()

                manager.playL(message.textChannel, message.channel, i, true, message, true)
            }

            msg.editMessage("기록 불러오기 완료!").queue()
        }else{
            message.channel.sendMessage("아직 불러오는중입니다. 잠시 후에 다시 시도해주세요!").queue()
        }
    }

    override fun b(): String {
        return "replay,rep"
    }

    override fun c(): String {
        return "기록된 대기열을 재생합니다."
    }
}