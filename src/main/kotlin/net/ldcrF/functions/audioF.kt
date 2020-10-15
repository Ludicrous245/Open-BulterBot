package net.ldcrF.functions

import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.VoiceChannel
import net.dv8tion.jda.api.managers.AudioManager

class audioF {
    companion object{

        fun join(message: Message, channel: MessageChannel){

            val mm: Message = channel.sendMessage("음성채널 접속중...").complete()

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

            val mm: Message = channel.sendMessage("음성채널 나가는중...").complete()

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
}