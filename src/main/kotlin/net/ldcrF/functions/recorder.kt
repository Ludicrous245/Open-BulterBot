package net.ldcrF.functions

import com.Ludicrous245.data.Config
import com.Ludicrous245.data.Storage
import com.Ludicrous245.io.SQL.SQLConnector
import com.Ludicrous245.io.SQL.SQLNullTester
import com.Ludicrous245.io.SQL.SQLProgressResult
import com.Ludicrous245.io.audio.GuildMusicManager
import com.Ludicrous245.io.audio.PlayerManager
import com.Ludicrous245.io.supporter.Embeded
import com.Ludicrous245.io.supporter.Presets
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.User

class recorder(author: User, msg:Message) {

    private val user = author
    private val message = msg

    private val recordedQueueTask:ArrayList<String>
    private val recordedQueue:ArrayList<String>

    init{
        if(!Storage.serverQueueRecord.containsKey(author)){
            Storage.serverQueueRecord.put(author, ArrayList())
            Storage.serverQueueRecordTask.put(author, ArrayList())
        }

        recordedQueue = Storage.serverQueueRecord.get(author)!!
        recordedQueueTask = Storage.serverQueueRecordTask.get(author)!!

        if(!Storage.isLoading.containsKey(message.author)){
            Storage.isLoading.put(message.author, 0)
        }
    }

    fun sendRecordedList(){

        if(recordedQueue.isEmpty()){
            loadTracks()
        }

        if(recordedQueue.isEmpty()){
            message.channel.sendMessage("기록된 대기열이 없습니다.").queue()
            return
        }

        if(Storage.isLoading.get(message.author) == 0) {
            if (recordedQueueTask.isEmpty()) {
                var eb = Embeded()

                eb.title("기록된 대기열")
                for (i in 1..recordedQueue.size) {
                    eb.field(i.toString() + "번 트랙 : " + "-", recordedQueue.get(i - 1))
                }
                eb.footer("제목이 보이지 않으신다면 저장된 목록을 한번 불러와주세요.(목록은 25번까지만 출력됩니다.)")
                eb.color(Presets.normal)
                eb.send(message.channel)
            } else {
                var eb = Embeded()

                eb.title("기록된 대기열")
                for (i in 1..recordedQueue.size) {
                    eb.field(i.toString() + "번 트랙 : " + recordedQueueTask.get(i - 1), recordedQueue.get(i - 1))
                }
                eb.footer("제목이 보이지 않으신다면 저장된 목록을 한번 불러와주세요. (목록은 25번까지만 출력됩니다.)")
                eb.color(Presets.normal)
                eb.send(message.channel)
            }
        }else{
            message.channel.sendMessage("제목을 불러오는중입니다. 잠시 후 다시 시도해주세요.").queue()
        }
    }

    fun recordTrack() {
        val manager: PlayerManager = PlayerManager().getInstance()
        val gm: GuildMusicManager = manager.getGuildMusicManager(message.guild, message.channel, message)

        val queue: ArrayList<AudioTrack>? = gm.scheduler.get()
        val track = gm.player.playingTrack

        if(track == null){
            message.channel.sendMessage("아무것도 재생중이지 않습니다.").queue()
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

        val sql = SQLConnector(Config.db_url, Config.db_user, Config.db_pw)

        var test2 = object : SQLNullTester() {
            override fun run(): SQLProgressResult {
                if (sql.take("playList", "userID", message.author.id, "playList") == null) return SQLProgressResult.NULL
                else return SQLProgressResult.SUCCESS
            }
        }

        recordedQueue.clear()
        recordedQueueTask.clear()

        recordedQueue.add(track.info.uri)
        recordedQueueTask.add(track.info.title)

        var listforSQL:String = track.info.uri

        if (queue != null) {
            if (!queue.isEmpty()) {
                for (tracks in queue) {

                    listforSQL += ","
                    listforSQL += tracks.info.uri

                    recordedQueue.add(tracks.info.uri)
                    recordedQueueTask.add(tracks.info.title)
                }

                if (sql.isNull(test2)) {

                    sql.state.execute("INSERT INTO `BulterBot`.`playList`  VALUES ('${message.author.id}', '${listforSQL}');")

                }else{
                    sql.update("playList", "playList", listforSQL, "userID", message.author.id)
                }

                if(isNulled) {
                    msg.editMessage("대기열 기록 완료!").queue()
                }else{
                    msg.editMessage("기록 덮어쓰기 완료!").queue()
                }

            }
        }
    }

    fun loadTracks(){

        if(recordedQueue.isEmpty()) {

            val sql = SQLConnector(Config.db_url, Config.db_user, Config.db_pw)

            var test2 = object : SQLNullTester() {
                override fun run(): SQLProgressResult {
                    if (sql.take(
                            "playList",
                            "userID",
                            message.author.id,
                            "playList"
                        ) == null
                    ) return SQLProgressResult.NULL
                    else return SQLProgressResult.SUCCESS
                }
            }

            if (sql.isNull(test2)) {
                message.channel.sendMessage("기록된 대기열이 없습니다.")
            } else {
                val rawList: String = sql.take("playList", "userID", message.author.id, "playList")!!
                val fixedList: List<String> = rawList.split(",")

                for (url in fixedList) {
                    recordedQueue.add(url)
                }
            }
        }
    }
}