package com.Ludicrous245.Listeners

import com.Ludicrous245.data.Storage
import com.Ludicrous245.io.audio.youtube.YoutubeManager
import com.Ludicrous245.io.supporter.Presets
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.lang.Exception

class AudioInteractionListener : ListenerAdapter() {
    val ytmanager: YoutubeManager = YoutubeManager()

    override fun onMessageReactionAdd(event: MessageReactionAddEvent) {
        try {
            if (event.reactionEmote.isEmoji) {
                if (event.user == null || event.member == null) {
                    return
                }
                if (event.user!!.isBot) {
                    return
                }

                if (!Storage.playerSearching.containsKey(event.member)) {
                    Storage.playerSearching.put(event.member!!, false)
                }

                if(!Storage.playerChooseLoaded.containsKey(event.member)){
                    Storage.playerChooseLoaded.put(event.member!!, false)
                }

                if (Storage.playerSearching.get(event.member)!!) {
                    if(event.messageId == Storage.playerChooseBotMessage.get(event.member)!!.id) {
                        val emoji = event.reactionEmote.emoji

                        if (emoji == "1️⃣") {
                            ytmanager.gi(event.member!!, 1)
                        } else if (emoji == "2️⃣") {
                            ytmanager.gi(event.member!!, 2)
                        } else if (emoji == "3️⃣") {
                            ytmanager.gi(event.member!!, 3)
                        } else if (emoji == "4️⃣") {
                            ytmanager.gi(event.member!!, 4)
                        } else if (emoji == "5️⃣") {
                            ytmanager.gi(event.member!!, 5)
                        } else if (emoji == "❎") {

                            Storage.playerSearching.put(event.member, false)

                            val eb = EmbedBuilder()

                            eb.setTitle("어머나!")
                            eb.setDescription("놀라셨죠? 전에 생성되었던 선택지는 깔끔하게 사라졌으니, 안심하셔도 됩니다.")
                            eb.setColor(Presets.alert)

                            Storage.playerChooseBotMessage.get(event.member)!!.editMessage(eb.build()).queue()

                        } else {
                            return
                        }
                    }
                }
            }
        }catch (e:Exception){
            //콘솔 보고서 작성
            System.out.println("Error occurred in CommandListener")
            System.out.println("Reason: " + e.toString())
            //서버 보고서 작성

            val message = Storage.playerChooseMessage.get(event.member)!!

            message.channel.sendMessage("실행오류!").queue()
            message.channel.sendMessage("에러코드:  " + "`" + e.toString() + "`").queue()
            return
        }
    }
}

/*
1️⃣
2️⃣
3️⃣
4️⃣
5️⃣
 */