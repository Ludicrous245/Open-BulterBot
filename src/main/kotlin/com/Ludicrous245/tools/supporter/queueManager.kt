package com.Ludicrous245.tools.supporter

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import net.dv8tion.jda.api.entities.Guild

class queueManager {
    companion object{
        private val queue:HashMap<Guild, ArrayList<AudioTrack>> = HashMap()
    }

    fun getAllQueue(guild: Guild):ArrayList<AudioTrack>?{
        return queue.get(guild)
    }

    fun poll(guild: Guild):AudioTrack?{
        val polled = queue.get(guild)!!.get(0)
        queue.get(guild)!!.removeAt(0)
        return polled
    }

    fun offer(guild: Guild, track:AudioTrack){
        if(!queue.containsKey(guild)){
            queue.put(guild, ArrayList())
        }
        queue.get(guild)!!.add(track)
    }

    fun get(guild: Guild):AudioTrack?{
        return queue.get(guild)!!.get(0)
    }

    fun clear(guild: Guild){
        if(!queue.containsKey(guild)){
            queue.put(guild, ArrayList())
            return
        }
        if(queue.get(guild).isNullOrEmpty()){
            return
        }
        queue.get(guild)!!.clear()
    }

    fun put(guild: Guild, list:AudioPlaylist){
        for(track in list.tracks){
            offer(guild, track)
        }
    }

    fun isEmpty(guild: Guild):Boolean{
        if(!queue.containsKey(guild)){
            queue.put(guild, ArrayList())
        }

        if(queue.get(guild)!!.isEmpty()){
            return true
        }

        else return queue.size == 0
    }
}