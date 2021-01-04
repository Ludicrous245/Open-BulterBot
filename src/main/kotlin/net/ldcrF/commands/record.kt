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

class record : CommandExecutor{
    override fun a(args: ArrayList<String>, syntax: String, rawSyntax: String, message: Message, content: String, channel: MessageChannel) {
        if(!Storage.serverQueueRecord.containsKey(message.author)){
            Storage.serverQueueRecord.put(message.author, ArrayList())
            Storage.serverQueueRecordTask.put(message.author, ArrayList())
        }

        val manager: PlayerManager = PlayerManager().getInstance()
        val gm: GuildMusicManager = manager.getGuildMusicManager(message.guild, message.channel, message)

        val queue: ArrayList<AudioTrack>? = gm.scheduler.get()
        val track = gm.player.playingTrack

        val recordedQueue = Storage.serverQueueRecord.get(message.author)!!
        val recordedQueueTask = Storage.serverQueueRecordTask.get(message.author)!!

        if(args.size == 1){
            if(args[0].equals("list")){

                if(recordedQueue.isEmpty()){
                    message.channel.sendMessage("기록된 대기열이 없습니다.").queue()
                    return
                }

                var eb = Embeded()

                eb.title("기록된 대기열")
                for(i in 1..recordedQueue.size){
                    eb.field(i.toString() +"번 트랙 : " + recordedQueueTask.get(i-1), recordedQueue.get(i-1))
                }
                eb.color(Presets.normal)
                eb.send(message.channel)
            }
            return
        }

        if(track == null){
            channel.sendMessage("아무것도 재생중이지 않습니다.").queue()
            return
        }

        var msg:Message
        var isNulled:Boolean

        if(recordedQueue.isEmpty()){
            msg = message.channel.sendMessage("대기열 기록중").complete()
            isNulled = true
        }else{
            msg = message.channel.sendMessage("기록 덮어쓰는중").complete()
            isNulled = false
        }

        recordedQueue.clear()
        recordedQueueTask.clear()

        recordedQueue.add(track.info.uri)
        recordedQueueTask.add(track.info.title)

        if (queue != null) {
            if(!queue.isEmpty()) {
                for (track in queue) {
                    recordedQueue.add(track.info.uri)
                    recordedQueueTask.add(track.info.title)
                }
            }
        }

        if(isNulled) {
            msg.editMessage("대기열 기록 완료!").queue()
        }else{
            msg.editMessage("기록 덮어쓰기 완료!").queue()
        }
    }

    override fun b(): String {
        return "record,rec"
    }

    override fun c(): String {
        return "현재 대기열을 기록합니다. (기록이 이미 있다면, 기존에 기록된 대기열에 덮어쓰기됩니다) !record(rec) list로 기록된 음악들을 확인할 수 있습니다."
    }
}