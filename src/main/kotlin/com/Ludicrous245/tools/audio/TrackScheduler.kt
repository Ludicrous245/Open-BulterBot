package com.Ludicrous245.tools.audio

import com.Ludicrous245.data.Storage
import com.Ludicrous245.tools.supporter.Embeded
import com.Ludicrous245.tools.supporter.Presets
import com.Ludicrous245.tools.supporter.queueManager
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.managers.AudioManager

class TrackScheduler constructor(player:AudioPlayer, server:Guild, channel:MessageChannel) : AudioEventAdapter() {

    private var player: AudioPlayer = player
    private var queue: queueManager = queueManager()
    private var guild:Guild = server
    private var channel = channel
    private val record:HashMap<Guild, AudioTrack> = HashMap()


    fun queue(track: AudioTrack){
        if(!player.startTrack(track, true)){
            queue.offer(guild, track)
        }
    }

    fun put(track:AudioPlaylist){
        queue.put(guild, track)
    }

    fun start(){

        val manager: PlayerManager = PlayerManager().getInstance()
        val gm:GuildMusicManager = manager.getGuildMusicManager(guild, channel)

        val track = gm.player.playingTrack

        if(track == null){
            player.startTrack(queue.poll(guild), false)
        }
    }

    fun next (){
        if(!Storage.isLoop.containsKey(guild)){
            Storage.isLoop.put(guild, false)
        }
        if (queue.getAllQueue(guild)!!.isEmpty()) {
            if(!Storage.isLoop.get(guild)!!) {
                player.stopTrack()
                val eb = Embeded()
                eb.title("재생이 끝났습니다")
                eb.color(Presets.normal)
                eb.send(channel)
                val am: AudioManager = guild.audioManager
                am.closeAudioConnection()
                return
            }
        }
            val eb = Embeded()
            eb.title("재생중")
            eb.field("제목", queue.get(guild)!!.info.title, true)
            eb.color(Presets.special)
            eb.send(channel)
            player.startTrack(queue.poll(guild), false)
    }

    fun loop(track: AudioTrack?){
        if(!Storage.isLoop.containsKey(guild)){
            Storage.isLoop.put(guild, false)
        }

        queue.offer(guild, track!!.makeClone())
        player.startTrack(queue.poll(guild), false)

    }

    fun get():ArrayList<AudioTrack>?{
        return queue.getAllQueue(guild)
    }

    override fun onTrackStart(player: AudioPlayer?, track: AudioTrack?) {
    }

    override fun onTrackEnd(player: AudioPlayer?, track: AudioTrack?, endReason: AudioTrackEndReason?) {
        if(endReason!!.mayStartNext){
            if(!Storage.isLoop.containsKey(guild)){
                Storage.isLoop.put(guild, false)
            }
            if(Storage.isLoop.get(guild)!!){
                loop(track)
            }else{
                next()
            }
        }
    }

}