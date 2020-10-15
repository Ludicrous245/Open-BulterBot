package net.ldcrF.commands

import com.Ludicrous245.data.Storage
import com.Ludicrous245.tools.audio.PlayerManager
import com.Ludicrous245.tools.audio.youtube.YoutubeManager
import com.Ludicrous245.tools.commands.CommandExecutor
import com.Ludicrous245.tools.kits.CheckerKit
import com.Ludicrous245.tools.supporter.Presets
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.ldcrF.functions.audioF

class play : CommandExecutor {
    val ytmanager: YoutubeManager = YoutubeManager()

    override fun a(args: ArrayList<String>, syntax: String, rawSyntax: String, message: Message, content: String, channel: MessageChannel) {
        if (!message.guild.selfMember.voiceState!!.inVoiceChannel()) {
            if(!message.member!!.voiceState!!.inVoiceChannel()){
                channel.sendMessage("음성채널에 입장하셔야 사용하실 수 있습니다.").queue()
                return
            }
            audioF.join(message, channel)
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

            manager.playL(message.textChannel, message.channel, args[0], false, message)

            manager.getGuildMusicManager(message.guild, message.channel, message).player.volume = Storage.serverVol.get(message.guild)!!

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
    }

    override fun b(): String {
        return "play,p"
    }

    override fun c(): String {
        return "링크를 이용해 음악을 재생합니다. 제목을 입력하는 경우, 유튜브 검색 최상단부터 5개를 골라 출력한 후, 선택지를 생성합니다."
    }
}