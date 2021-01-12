package com.Ludicrous245.io.audio

import com.Ludicrous245.data.BanStackData
import com.Ludicrous245.data.Config
import com.Ludicrous245.data.Storage
import com.Ludicrous245.io.SQL.SQLConnector
import com.Ludicrous245.io.SQL.SQLNullTester
import com.Ludicrous245.io.SQL.SQLProgressResult
import com.Ludicrous245.io.supporter.Embeded
import com.Ludicrous245.io.supporter.Presets
import com.Ludicrous245.io.supporter.queueManager
import com.Ludicrous245.io.xyz.Entity.HumanEntity
import com.Ludicrous245.management.BannedUser
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.TextChannel
import net.ldcrF.functions.recorder
import kotlin.concurrent.timer

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

    fun getGuildMusicManager(server: Guild, channel: MessageChannel, message: Message): GuildMusicManager {
        val guildID = server.idLong
        var gmm:GuildMusicManager? = mm.get(guildID)
        if (gmm == null) {
            gmm = GuildMusicManager(pm, server, channel, message)
            mm.put(guildID, gmm)
        }
        server.audioManager.sendingHandler = gmm.getSendHandler()
        return gmm
    }

    fun playL(channel: TextChannel, mchannel: MessageChannel, track: String, adding: Boolean, message: Message, replaying:Boolean) {

        val musicManager = getGuildMusicManager(channel.guild, mchannel, message)

        Storage.serverQueueRecordTask.get(message.author)!!.clear()

        pm.loadItemOrdered(pm, track, object : AudioLoadResultHandler {
            override fun trackLoaded(track: AudioTrack) {
                val queue = queueManager()

                val pm: PlayerManager = PlayerManager().getInstance()
                val gm: GuildMusicManager = pm.getGuildMusicManager(channel.guild, channel, message)

                val tr = gm.player.playingTrack

                if (queue.isEmpty(channel.guild) && tr == null) {
                    val manager = Embeded()
                    manager.title("재생중")
                    manager.field("제목", track.info.title, true)
                    manager.color(Presets.special)
                    manager.send(channel)

                    if(replaying) {
                        Storage.serverQueueRecordTask.get(message.author)!!.add(track.info.title)

                        if(Storage.isLoading.get(message.author) != null) {
                            Storage.isLoading.put(message.author, Storage.isLoading.get(message.author)!! - 1)
                        }
                    }

                    play(musicManager, track)
                    return
                }


                if (!Storage.isLoop.containsKey(channel.guild)) {
                    Storage.isLoop.put(channel.guild, false)
                }

                if (!adding) {
                    if (Storage.isLoop.get(channel.guild)!!) {
                        val manager = Embeded()
                        manager.title("반복재생 목록에 추가됨")
                        manager.field("제목", track.info.title, true)
                        manager.color(Presets.normal)
                        manager.send(channel)
                    } else {
                        val manager = Embeded()
                        manager.title("대기열에 추가됨")
                        manager.field("제목", track.info.title, true)
                        manager.color(Presets.normal)
                        manager.send(channel)
                    }
                }

                if(replaying) {
                    Storage.serverQueueRecordTask.get(message.author)!!.add(track.info.title)

                    if(Storage.isLoading.get(message.author) != null) {
                        Storage.isLoading.put(message.author, Storage.isLoading.get(message.author)!! - 1)
                    }
                }

                play(musicManager, track)

            }

            override fun playlistLoaded(playlist: AudioPlaylist) {

                val pm: PlayerManager = PlayerManager().getInstance()
                val gm: GuildMusicManager = pm.getGuildMusicManager(channel.guild, channel, message)

                val tr = gm.player.playingTrack

                val queue = queueManager()

                    if (100 < playlist.tracks.size) {

                        val hm: HumanEntity = BanStackData.getHumanEntity(message.author.idLong)!!

                        var si = playlist.tracks.size - 100

                        hm.setSBP(hm.stackByPlaylist + (si / 2))

                        if (100 <= hm.stackByPlaylist) {
                            hm.warn = hm.stackByPlaylist / 100
                        }

                        if (2 < hm.warn) {
                            BannedUser.ban(message.author.id, message)

                            message.channel.sendMessage(message.author.asTag + "님은 밴되었습니다.").queue()

                            return
                        }

                        val manager = Embeded()
                        manager.title("어우 배불러")
                        manager.description("재생목록 안에 들어있는 음악은 안정성의 이유로 100곡을 넘길 수 없습니다")
                        manager.color(Presets.alert)
                        manager.send(channel)
                        return
                    }

                    if (playlist.tracks.isEmpty()) {
                        val manager = Embeded()
                        manager.title("오! 이런,")
                        manager.description("재생목록 안에 들어있는 음악이 없습니다.")
                        manager.color(Presets.alert)
                        manager.send(channel)
                        return
                    }

                    if (queue.isEmpty(channel.guild) && tr == null) {
                        val manager = Embeded()
                        manager.title("재생목록 재생중")
                        manager.field("재생목록 이름", playlist.name, true)
                        manager.color(Presets.special)
                        manager.send(channel)

                        playA(musicManager, playlist)
                        return
                    }

                    val manager = Embeded()
                    manager.title("재생목록이 대기열에 추가되었습니다.")
                    manager.field("추가된 재생목록", playlist.name)
                    manager.color(Presets.special)
                    manager.send(channel)
                    playA(musicManager, playlist)
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

                if (exception.toString().equals("com.sedmelluq.discord.lavaplayer.tools.FriendlyException: Received unexpected response from YouTube.")) {
                    manager.title("유튜브 URL 처리중 문제가 발생한것 같아요!")
                    manager.description("잠깐 기다리신 후에 다시 시도해주세요.")
                } else {
                    manager.title("'제게 큰 병이 생긴건가요... 의사선생님?'")
                    manager.field("재생명령어를 처리하던중 예외가 발생했습니다.", "`" + exception.toString() + "`")
                    manager.footer("버그는 이곳으로 제보해주세요" + "https://discord.gg/XphePuY ")
                }
                manager.color(Presets.alert)
                manager.send(channel)
            }
        })
    }

    fun playL(channel: TextChannel, mchannel: MessageChannel, track: String, adding: Boolean, message: Message) {

        val musicManager = getGuildMusicManager(channel.guild, mchannel, message)

        pm.loadItemOrdered(pm, track, object : AudioLoadResultHandler {
            override fun trackLoaded(track: AudioTrack) {
                val queue = queueManager()

                val pm: PlayerManager = PlayerManager().getInstance()
                val gm: GuildMusicManager = pm.getGuildMusicManager(channel.guild, channel, message)

                val tr = gm.player.playingTrack

                if (queue.isEmpty(channel.guild) && tr == null) {
                    val manager = Embeded()
                    manager.title("재생중")
                    manager.field("제목", track.info.title, true)
                    manager.color(Presets.special)
                    manager.send(channel)

                    play(musicManager, track)
                    return
                }


                if (!Storage.isLoop.containsKey(channel.guild)) {
                    Storage.isLoop.put(channel.guild, false)
                }

                if (!adding) {
                    if (Storage.isLoop.get(channel.guild)!!) {
                        val manager = Embeded()
                        manager.title("반복재생 목록에 추가됨")
                        manager.field("제목", track.info.title, true)
                        manager.color(Presets.normal)
                        manager.send(channel)
                    } else {
                        val manager = Embeded()
                        manager.title("대기열에 추가됨")
                        manager.field("제목", track.info.title, true)
                        manager.color(Presets.normal)
                        manager.send(channel)
                    }
                }

                play(musicManager, track)

            }



            override fun playlistLoaded(playlist: AudioPlaylist) {

                val pm: PlayerManager = PlayerManager().getInstance()
                val gm: GuildMusicManager = pm.getGuildMusicManager(channel.guild, channel, message)

                val tr = gm.player.playingTrack

                val queue = queueManager()

                if (100 < playlist.tracks.size) {

                    val hm: HumanEntity = BanStackData.getHumanEntity(message.author.idLong)!!

                    var si = playlist.tracks.size - 100

                    hm.setSBP(hm.stackByPlaylist + (si / 2))

                    if (100 <= hm.stackByPlaylist) {
                        hm.warn = hm.stackByPlaylist / 100
                    }

                    if (2 < hm.warn) {
                        BannedUser.ban(message.author.id, message)

                        message.channel.sendMessage(message.author.asTag + "님은 밴되었습니다.").queue()

                        return
                    }

                    val manager = Embeded()
                    manager.title("어우 배불러")
                    manager.description("재생목록 안에 들어있는 음악은 안정성의 이유로 100곡을 넘길 수 없습니다")
                    manager.color(Presets.alert)
                    manager.send(channel)
                    return
                }

                if (playlist.tracks.isEmpty()) {
                    val manager = Embeded()
                    manager.title("오! 이런,")
                    manager.description("재생목록 안에 들어있는 음악이 없습니다.")
                    manager.color(Presets.alert)
                    manager.send(channel)
                    return
                }

                if (queue.isEmpty(channel.guild) && tr == null) {
                    val manager = Embeded()
                    manager.title("재생목록 재생중")
                    manager.field("재생목록 이름", playlist.name, true)
                    manager.color(Presets.special)
                    manager.send(channel)

                    playA(musicManager, playlist)
                    return
                }

                val manager = Embeded()
                manager.title("재생목록이 대기열에 추가되었습니다.")
                manager.field("추가된 재생목록", playlist.name)
                manager.color(Presets.special)
                manager.send(channel)
                playA(musicManager, playlist)
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

                if (exception.toString().equals("com.sedmelluq.discord.lavaplayer.tools.FriendlyException: Received unexpected response from YouTube.")) {
                    manager.title("유튜브 URL 처리중 문제가 발생한것 같아요!")
                    manager.description("잠깐 기다리신 후에 다시 시도해주세요.")
                } else {
                    manager.title("'제게 큰 병이 생긴건가요... 의사선생님?'")
                    manager.field("재생명령어를 처리하던중 예외가 발생했습니다.", "`" + exception.toString() + "`")
                    manager.footer("버그는 이곳으로 제보해주세요" + "https://discord.gg/XphePuY ")
                }
                manager.color(Presets.alert)
                manager.send(channel)
            }
        })
    }


    fun play(musicManager: GuildMusicManager, track: AudioTrack) {
        musicManager.scheduler.queue(track)
    }

    fun playA(musicManager: GuildMusicManager, track: AudioPlaylist) {
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
