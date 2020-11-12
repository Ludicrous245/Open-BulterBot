package net.ldcrF.commands

import com.Ludicrous245.data.Storage
import com.Ludicrous245.io.audio.PlayerManager
import com.Ludicrous245.io.commands.execute.CommandExecutor
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.ldcrF.functions.audioF

class replay : CommandExecutor {
    override fun a(args: ArrayList<String>, syntax: String, rawSyntax: String, message: Message, content: String, channel: MessageChannel) {
        if(!Storage.serverQueueRecord.containsKey(message.guild)){
            Storage.serverQueueRecord.put(message.guild, ArrayList())
            Storage.serverQueueRecordTask.put(message.guild, ArrayList())
        }

        val recordedQueue = Storage.serverQueueRecord.get(message.guild)!!

        var msg:Message

        if (recordedQueue.isEmpty()) {
            message.channel.sendMessage("기록된 대기열이 없습니다.").queue()
            return
        }else{
            msg = message.channel.sendMessage("기록 불러오는중").complete()
        }

        if (!message.guild.selfMember.voiceState!!.inVoiceChannel()) {
            if(!message.member!!.voiceState!!.inVoiceChannel()){
                channel.sendMessage("음성채널에 입장하셔야 사용하실 수 있습니다.").queue()
                return
            }
            audioF.join(message, channel)
        }

        for(i in recordedQueue){
            var manager: PlayerManager = PlayerManager().getInstance()

            manager.playL(message.textChannel, message.channel, i, true, message)
        }

        msg.editMessage("기록 불러오기 완료!").queue()
    }

    override fun b(): String {
        return "replay,rep"
    }

    override fun c(): String {
        return "기록된 대기열을 재생합니다."
    }
}