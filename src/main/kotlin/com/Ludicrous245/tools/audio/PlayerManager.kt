package com.Ludicrous245.tools.audio

import com.Ludicrous245.data.Storage
import com.Ludicrous245.tools.supporter.Embeded
import com.Ludicrous245.tools.supporter.Presets
import com.Ludicrous245.tools.supporter.queueManager
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.TextChannel

class PlayerManager {
    companion object{
        private var instance:PlayerManager? = null
    }

    var pm: AudioPlayerManager = DefaultAudioPlayerManager()
    val mm = HashMap<Long, GuildMusicManager>()

    init {
        AudioSourceManagers.registerRemoteSources(pm)
        AudioSourceManagers.registerLocalSource(pm)
    }

    fun getGuildMusicManager(server: Guild, channel: MessageChannel): GuildMusicManager {
        val guildID = server.idLong
        var gmm:GuildMusicManager? = mm.get(guildID)
        if (gmm == null) {
            gmm = GuildMusicManager(pm, server, channel)
            mm.put(guildID, gmm)
        }
        server.audioManager.sendingHandler = gmm.getSendHandler()
        return gmm
    }

    fun playL(channel: TextChannel, mchannel: MessageChannel, track: String) {


        val musicManager = getGuildMusicManager(channel.guild, mchannel)

        pm.loadItemOrdered(pm, track, object : AudioLoadResultHandler {
            override fun trackLoaded(track: AudioTrack) {
                val queue = queueManager()

                val pm: PlayerManager = PlayerManager().getInstance()
                val gm: GuildMusicManager = pm.getGuildMusicManager(channel.guild, channel)

                val tr = gm.player.playingTrack

                if(queue.isEmpty(channel.guild) && tr == null){
                    val manager = Embeded()
                    manager.title("재생중")
                    manager.field("제목", track.info.title, true)
                    manager.color(Presets.special)
                    manager.send(channel)

                    play(musicManager, track)
                    return
                }


                if(!Storage.isLoop.containsKey(channel.guild)){
                    Storage.isLoop.put(channel.guild, false)
                }

                if(Storage.isLoop.get(channel.guild)!!){
                    val manager = Embeded()
                    manager.title("반복재생 목록에 추가됨")
                    manager.field("제목", track.info.title, true)
                    manager.color(Presets.normal)
                    manager.send(channel)
                }else {
                    val manager = Embeded()
                    manager.title("대기열에 추가됨")
                    manager.field("제목", track.info.title, true)
                    manager.color(Presets.normal)
                    manager.send(channel)
                }

                play(musicManager, track)
            }

            override fun playlistLoaded(playlist: AudioPlaylist) {
                val manager = Embeded()
                manager.title("죄송합니다")
                manager.field("이 기능은 사용하실 수 없습니다.", "플레이리스트는 정보 누락 문제로 사용하실 수 없습니다.", true)
                manager.color(Presets.alert)
                manager.send(channel)
                return
            }
            override fun noMatches() {
                val manager = Embeded()
                manager.title("오! 이런,")
                manager.description("안타깝게도 입력한 " + track + " 에서 아무 결과도 찾지 못했어요.")
                manager.color(Presets.alert)
                manager.send(channel)
            }
            override fun loadFailed(exception: FriendlyException) {
                val manager = Embeded()
                manager.title("'제게 큰 병이 생긴건가요... 의사선생님?'")
                manager.field("재생하던중 예외가 발생했습니다.", "`" + exception.toString() + "`")
                manager.color(Presets.alert)
                manager.send(channel)
            }
        })
    }

    fun play(musicManager: GuildMusicManager, track: AudioTrack) {
        musicManager.scheduler.queue(track)
    }

    fun playA(musicManager: GuildMusicManager, track: AudioPlaylist){
        musicManager.scheduler.put(track)
        musicManager.scheduler.start()
    }

    @Synchronized
    fun getInstance(): PlayerManager {
        if (instance == null) {
            instance = PlayerManager()
        }
        return instance!!
    }
}