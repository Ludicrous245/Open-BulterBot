package net.ldcrF.commands

import com.Ludicrous245.data.Storage
import com.Ludicrous245.tools.audio.GuildMusicManager
import com.Ludicrous245.tools.commands.CommandExecutor
import com.Ludicrous245.tools.audio.PlayerManager
import com.Ludicrous245.tools.audio.TrackScheduler
import com.Ludicrous245.tools.audio.youtube.YoutubeManager
import com.Ludicrous245.tools.kits.CheckerKit
import com.Ludicrous245.tools.supporter.Embeded
import com.Ludicrous245.tools.supporter.Presets
import com.Ludicrous245.tools.supporter.queueManager
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.VoiceChannel
import net.dv8tion.jda.api.managers.AudioManager
import java.lang.Exception

class audio : CommandExecutor() {
    /*
    오디오 관련 커맨드를 포괄하는 클래스다
    기술력이 달리는것도 있고, 의도한것도 있는데,
    보면 알겠지만 cmdRun함수 안에서 콘텐트가 맞는지를 비교한다.
    그말인 즉슨 하나의 클래스 안에서 여러개의 명령어를 만들 수 있다는건데,
    원래는 클래스 이름을 읽어와서 그걸로 명령어 비교를 하려고 했으나,(JS처럼)
    근데 기술력이 부족한것도 있고, 여러 복합적인 이유때문에 클래스는 일종의 카테고리로써 작용을 하고
    그 안에서 명령어 비교나 처리같은걸 한다.
    그래도 명령어 내부는 예외처리랑 메소드 호출해둔거 밖에 없어서 깔끔하다.
    메소드가 좀 더러워 보이는게 제일 문제.
    그래도 나름 신경써서 줄바꿈도 하고 잘 했다.
    노오오오력 만 하면 충분히 읽을 수 있을정도
     */

    val ytmanager:YoutubeManager = YoutubeManager()

    override fun cmdRun(args: ArrayList<String>, syntax: String, rawSyntax: String, message: Message, content: String, channel: MessageChannel) {
        if(content == "join") {
            try {
                join(message, channel)
            }catch (e:Exception){
                System.out.println("Error occurred to run commands")
                System.out.println("Reason: " + e.toString())

                message.channel.sendMessage("실행오류!").queue()
                message.channel.sendMessage("에러코드:  " + "`" + e.toString() + "`").queue()
            }
        }else if(content == "leave"){
            try {
                leave(message, channel)
            }catch (e:Exception){
                System.out.println("Error occurred to run commands")
                System.out.println("Reason: " + e.toString())

                message.channel.sendMessage("실행오류!").queue()
                message.channel.sendMessage("에러코드:  " + "`" + e.toString() + "`").queue()
            }
        }else if(content == "play" || content == "p"){
            if(!message.member!!.voiceState!!.inVoiceChannel()){
                channel.sendMessage("음성채널에 입장하셔야 사용하실 수 있습니다.").queue()
                return
            }
            if (!message.guild.selfMember.voiceState!!.inVoiceChannel()) {
                join(message, channel)
            }

            if(args.size == 0){
                message.channel.sendMessage("URL이 필요합니다.").queue()
                return
            }

            if(CheckerKit.isURL(args[0])) {

                if (Storage.serverVol.get(message.guild) == null) {
                    Storage.serverVol.put(message.guild, 50)
                }

                val manager: PlayerManager = PlayerManager().getInstance()

                manager.playL(message.textChannel, message.channel, args[0])

                manager.getGuildMusicManager(message.guild, message.channel).player.volume = Storage.serverVol.get(message.guild)!!

            }else{
                if(!Storage.playerSearching.containsKey(message.member)){
                    Storage.playerSearching.put(message.member, false)
                }

                if(Storage.playerSearching.get(message.member)!!){
                    val manager = EmbedBuilder()
                    manager.setTitle("맙소사!")
                    manager.setDescription("전에 생성되었던 선택지를 파기하고 새 선택지를 만들고 있어요. 이번엔 마음에 들길 바랄께요.")
                    manager.setColor(Presets.alert)
                    Storage.playerChooseBotMessage.get(message.member)!!.editMessage(manager.build()).queue()
                }

                Storage.playerSearching.put(message.member, true)
                ytmanager.youtubeSearch(syntax, message)
            }

        }else if(content == "skip"){

            val manager: PlayerManager = PlayerManager().getInstance()
            val gm:GuildMusicManager = manager.getGuildMusicManager(message.guild, message.channel)
            val scheduler:TrackScheduler = gm.scheduler
            val player = gm.player

            if(Storage.isLoop.get(message.guild)!!){
               if(scheduler.get() == null || scheduler.get()!!.isEmpty()){
                   message.channel.sendMessage("한곡만 반복재생중일 경우, 스킵이 불가능합니다.").queue()
                   return
               }
            }

            if(player.playingTrack == null){
                message.channel.sendMessage("재생중인 항목이 없습니다.").queue()
                return
            }

            scheduler.next()

        }else if(content == "stop"){
            if(!message.member!!.permissions.contains(Permission.ADMINISTRATOR)){
                message.channel.sendMessage("관리자 전용 기능입니다.").queue()
                return
            }

            channel.sendMessage("대기열을 비우고, 모든 재생을 중단합니다.").queue()

            val manager: PlayerManager = PlayerManager().getInstance()
            val gm:GuildMusicManager = manager.getGuildMusicManager(message.guild, message.channel)
            val qm = queueManager()
            val am: AudioManager = message.guild.audioManager

            if(!qm.isEmpty(message.guild)){
                qm.clear(message.guild)
            }

            am.closeAudioConnection()
            gm.player.stopTrack()
            gm.player.isPaused = false
        }else if(content == "volume" || content == "vol"){
            if(Storage.serverVol.get(message.guild) == null){
                Storage.serverVol.put(message.guild, 50)
            }

            if(args.size == 0){
                val eb:Embeded = Embeded()
                eb.color(0x008CCD)
                eb.field("현재 볼륨", Storage.serverVol.get(message.guild)!!.toString())
                eb.title("볼륨 마법사")
                eb.send(channel)
            }else if(args.size == 1){
                try {
                    if(100 < args[0].toInt()){
                        val eb: Embeded = Embeded()
                        eb.color(Presets.alert)
                        eb.description("볼륨이 너무 커요")
                        eb.title("어우 내귀!!")
                        eb.send(channel)
                        return
                    }else if(args[0].toInt() < 0){
                        val eb: Embeded = Embeded()
                        eb.color(Presets.alert)
                        eb.description("볼륨이 너무 작아요")
                        eb.title("지금 뭐 나오고있냐?")
                        eb.send(channel)
                        return
                    }

                    Storage.serverVol.put(message.guild, args[0].toInt())

                    val manager: PlayerManager = PlayerManager().getInstance()

                    manager.getGuildMusicManager(message.guild, message.channel).player.volume = Storage.serverVol.get(message.guild)!!

                    val eb: Embeded = Embeded()
                    eb.color(Presets.special)
                    eb.description("볼륨이 적용되었습니다.")
                    eb.title("볼륨 마법사")
                    eb.footer("시간이 소요되거나 다음곡부터 적용될 가능성 있음.")
                    eb.send(channel)

                }catch (e:Exception){
                    val eb: Embeded = Embeded()
                    eb.color(Presets.alert)
                    eb.description("혹시 볼륨 대신 이상한걸 적으셨나요?")
                    eb.title("이상한걸 먹은것 같아요...")
                    eb.send(channel)
                    return
                }
            }else{
                message.channel.sendMessage("뭐 잘못치신거 아닙니까? ㅋㅋㅋ").queue()
                return
            }
        }else if(content == "queue"){
            val manager: PlayerManager = PlayerManager().getInstance()
            val gm:GuildMusicManager = manager.getGuildMusicManager(message.guild, message.channel)
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

        }else if(content == "pause"){
            val manager: PlayerManager = PlayerManager().getInstance()
            val gm:GuildMusicManager = manager.getGuildMusicManager(message.guild, message.channel)

            if(!Storage.isPause.containsKey(message.guild)){
                Storage.isPause.put(message.guild, false)
            }

            if(Storage.isPause.get(message.guild)!!){
                gm.player.isPaused = false

                Storage.isPause.put(message.guild, false)
                channel.sendMessage("일시정지가 해제되었습니다.").queue()
            }else{
                gm.player.isPaused = true

                Storage.isPause.put(message.guild, true)
                channel.sendMessage("일시정지 되었습니다.").queue()
            }
        }else if(content == "nowPlaying" || content == "np") {
            val manager: PlayerManager = PlayerManager().getInstance()
            val gm: GuildMusicManager = manager.getGuildMusicManager(message.guild, message.channel)

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
        }else if(content == "loop"){

            val manager: PlayerManager = PlayerManager().getInstance()
            val gm:GuildMusicManager = manager.getGuildMusicManager(message.guild, message.channel)
            val scheduler:TrackScheduler = gm.scheduler
            val player = gm.player
            val guild = message.guild

            if(!Storage.isLoop.containsKey(guild)){
                Storage.isLoop.put(guild, false)
            }

            /*if(Storage.isLoop.get(message.guild)!!){
                if(scheduler.get() == null || scheduler.get()!!.isEmpty()){
                    scheduler.queue(queueManager.getRecorded(guild)!!.makeClone())
                    return
                }
            }*/

            if(Storage.isLoop.get(guild)!!){
                channel.sendMessage("반복재생 모드가 꺼졌습니다.").queue()
                Storage.isLoop.put(guild, false)
            }else{
                channel.sendMessage("반복재생 모드가 켜졌습니다.").queue()
                Storage.isLoop.put(guild, true)
            }
        }else if(content.equals("reverse")){
            val manager: PlayerManager = PlayerManager().getInstance()
            val gm: GuildMusicManager = manager.getGuildMusicManager(message.guild, message.channel)
            val queue: ArrayList<AudioTrack>? = gm.scheduler.get()

            val track = gm.player.playingTrack

            if (track == null) {
                channel.sendMessage("아무것도 재생중이지 않습니다.").queue()
                return
            }

            if (queue == null || queue.isEmpty()) {
                channel.sendMessage("대기열 없음").queue()
                return
            }
            try {
                queue.reverse()

                val eb = Embeded()
                eb.title("피가 거꾸로 솟는다!!")
                eb.description("대기열이 뒤집혔습니다.")
                eb.color(Presets.special)
                eb.send(channel)
            }catch (e:Exception){
                val manager = Embeded()
                manager.title("'제게 큰 병이 생긴건가요... 의사선생님?'")
                manager.field("명령어 실행중 예외가 발생했습니다.", "`" + e.toString() + "`")
                manager.color(Presets.alert)
                manager.send(channel)
            }

        }else if(content.equals("shuffle")){
            val manager: PlayerManager = PlayerManager().getInstance()
            val gm: GuildMusicManager = manager.getGuildMusicManager(message.guild, message.channel)
            val queue: ArrayList<AudioTrack>? = gm.scheduler.get()

            val track = gm.player.playingTrack

            if (track == null) {
                channel.sendMessage("아무것도 재생중이지 않습니다.").queue()
                return
            }

            if (queue == null || queue.isEmpty()) {
                channel.sendMessage("대기열 없음").queue()
                return
            }

            try {
                queue.shuffle();

                val eb = Embeded()
                eb.title("으아아 어지러워요!!")
                eb.description("대기열이 섞였습니다.")
                eb.color(Presets.special)
                eb.send(channel)
            }catch (e:Exception){
                val manager = Embeded()
                manager.title("'제게 큰 병이 생긴건가요... 의사선생님?'")
                manager.field("명령어 실행중 예외가 발생했습니다.", "`" + e.toString() + "`")
                manager.color(Presets.alert)
                manager.send(channel)
            }

        }

    }

    fun join(message: Message, channel: MessageChannel){

        val mm:Message = channel.sendMessage("음성채널 접속중...").complete()

        val am: AudioManager = message.guild.audioManager

        if (!message.member!!.voiceState!!.inVoiceChannel()) {
            channel.sendMessage("음성채널에 입장하셔야 사용하실 수 있습니다.").queue()
            return
        }

        val vc: VoiceChannel = message.member!!.voiceState!!.channel!!

        if (!message.guild.selfMember.hasPermission()) {
            channel.sendMessage("봇이 음성채널 입장 권한을 가지고 있지 않습니다.").queue()
            return
        }

        if (am.isAttemptingToConnect) {
            mm.editMessage("이미 " + "'" + vc.name + "'" + " 에 접속 시도중입니다.").queue()
            return
        }

        if (message.guild.selfMember.voiceState!!.inVoiceChannel()) {
            mm.editMessage("이미 " + "'" + message.guild.selfMember.voiceState!!.channel!!.name + "'" + " 에 접속헸습니다.").queue()
            return
        }

        am.openAudioConnection(vc)

        mm.editMessage("'" + vc.name + "'" + " 에 접속했습니다.").queue()
    }

    fun leave(message: Message, channel: MessageChannel){

        val mm:Message = channel.sendMessage("음성채널 나가는중...").complete()

        val am: AudioManager = message.guild.audioManager

        val vc: VoiceChannel = message.member!!.voiceState!!.channel!!

        if (!message.guild.selfMember.voiceState!!.inVoiceChannel()) {
            mm.editMessage("봇이 음성채널에 없습니다..").queue()
            return
        }

        am.closeAudioConnection()

        mm.editMessage("'" + vc.name + "'" + " 에서 나왔습니다.").queue()
    }


}