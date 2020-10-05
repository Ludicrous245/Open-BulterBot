package com.Ludicrous245.tools.audio

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.MessageChannel


class GuildMusicManager constructor(manager: AudioPlayerManager, server:Guild, channel: MessageChannel) {
    var player: AudioPlayer = manager.createPlayer()

    var scheduler: TrackScheduler = TrackScheduler(player, server, channel)

    init {
        player.addListener(scheduler)
    }

    fun getSendHandler(): AudioPlayerSendHandler {
        return AudioPlayerSendHandler(player)
    }
}