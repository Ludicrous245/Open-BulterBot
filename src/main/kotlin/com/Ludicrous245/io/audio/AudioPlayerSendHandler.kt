package com.Ludicrous245.io.audio

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame
import net.dv8tion.jda.api.audio.AudioSendHandler
import java.nio.Buffer
import java.nio.ByteBuffer


class AudioPlayerSendHandler constructor(audioPlayer: AudioPlayer): AudioSendHandler{

    private val audioPlayer: AudioPlayer = audioPlayer
    private val buffer: ByteBuffer = ByteBuffer.allocate(1024)
    private val frame: MutableAudioFrame

    init {
        frame = MutableAudioFrame()
        frame.setBuffer(buffer)
    }

     override fun provide20MsAudio(): ByteBuffer {
        (buffer as Buffer).flip()
        return buffer
    }

    override fun canProvide(): Boolean {
        return audioPlayer.provide(frame)
    }

    override fun isOpus(): Boolean {
        return true
    }
}