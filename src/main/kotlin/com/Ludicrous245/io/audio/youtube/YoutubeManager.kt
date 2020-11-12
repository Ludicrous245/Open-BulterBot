package com.Ludicrous245.io.audio.youtube

import com.Ludicrous245.data.Config
import com.Ludicrous245.data.Storage
import com.Ludicrous245.io.audio.PlayerManager
import com.Ludicrous245.io.supporter.Embeded
import com.Ludicrous245.io.supporter.Presets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpRequest
import com.google.api.client.http.HttpRequestInitializer
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.SearchListResponse
import com.google.api.services.youtube.model.SearchResult
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import java.lang.Exception

class YoutubeManager(){
    var youtube:YouTube? = null
    init {
        var temp: YouTube? = null

        try{
            temp = YouTube.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                HttpRequestInitializer { fun initialize(requset:HttpRequest) {} }

            ).setApplicationName("Chrol Bot").build()

        }catch (e: Exception){

        }
        youtube=temp
    }

    fun youtubeSearch(text:String, message: Message){
        try{
            val search:YouTube.Search.List = youtube!!.search().list("id,snippet")

            search.setKey(Config.key)
            search.setQ(text)
            search.setType("video")
            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)")
            search.setMaxResults(5L)

            val response:SearchListResponse = search.execute()

            val results:List<SearchResult>? = response.items



            if(results == null){
                message.channel.sendMessage("검색결과가 없습니다.").queue()
                return
            }

            if(!results.isEmpty()){

                if(!Storage.playerSearching.containsKey(message.member)){
                    Storage.playerSearching.put(message.member!!, true)
                }

                if(!Storage.playerChooseLoaded.containsKey(message.member)){
                    Storage.playerChooseLoaded.put(message.member, false)
                }

                val eb = Embeded()

                eb.title("원하시는 음악을 골라주세요!")
                for(i in 0 until results.size){
                    var num:Int = i+1
                    eb.field("트랙 [" + num + "]", results.get(i).snippet.title)
                }

                Storage.playerChooseMessage.put(message.member, message)

                Storage.playerChooseItem.put(message.member, results)

                eb.color(Presets.special)
                eb.footer("선택지가 모두 로딩되지 않은 상태에서 선택하면 봇에 무리가 갈 수 있습니다. 주의하세요!")

                val msg = eb.sendAndGet(message.channel)

                try {
                    val size = results.size

                    if(5 == size){
                        msg.addReaction("1️⃣").queue()
                        msg.addReaction("2️⃣").queue()
                        msg.addReaction("3️⃣").queue()
                        msg.addReaction("4️⃣").queue()
                        msg.addReaction("5️⃣").queue()
                        msg.addReaction("❎").queue()
                    }else if(4 == size){
                        msg.addReaction("1️⃣").queue()
                        msg.addReaction("2️⃣").queue()
                        msg.addReaction("3️⃣").queue()
                        msg.addReaction("4️⃣").queue()
                        msg.addReaction("❎").queue()
                    }else if(3 == size){
                        msg.addReaction("1️⃣").queue()
                        msg.addReaction("2️⃣").queue()
                        msg.addReaction("3️⃣").queue()
                        msg.addReaction("❎").queue()
                    }else if(2 == size){
                        msg.addReaction("1️⃣").queue()
                        msg.addReaction("2️⃣").queue()
                        msg.addReaction("❎").queue()
                    }else if(1 == size){
                        msg.addReaction("1️⃣").queue()
                        msg.addReaction("❎").queue()
                    }

                    Storage.playerChooseBotMessage.put(message.member, msg)
                }catch (e:Exception){
                    return
                }

            }else{
                Storage.playerSearching.put(message.member, false)
                message.channel.sendMessage("검색결과가 없습니다.").queue()
                return
            }

        }catch (e:Exception){
            //콘솔 보고서 작성
            System.out.println("Error occurred to run commands")
            System.out.println("Reason: " + e.toString())
            //서버 보고서 작성
            message.channel.sendMessage("실행오류!").queue()
            message.channel.sendMessage("에러코드:  " + "`" + e.toString() + "`").queue()

            return
        }
        return
    }

    fun gi(member: Member, answer:Int) {
        try {

            val message = Storage.playerChooseMessage.get(member)

            val msg = Storage.playerChooseBotMessage.get(member)

            val vid = Storage.playerChooseItem.get(member)!!.get(answer - 1).id.videoId

            val manager: PlayerManager = PlayerManager().getInstance()

            if (message == null) {
                throw Exception()
            }

            val eb = EmbedBuilder()

            eb.setTitle("선택됨")
            eb.setDescription(answer.toString() + " 번에 있던 " + Storage.playerChooseItem.get(member)!!.get(answer - 1).snippet.title + " 을 선택하셨어요!")
            eb.setColor(Presets.special)

            msg!!.editMessage(eb.build()).queue()

            manager.playL(message.textChannel, message.channel, "https://www.youtube.com/watch?v=" + vid, false, message)

            if (!Storage.serverVol.containsKey(message.guild)) {
                Storage.serverVol.put(message.guild, 50)
            }

            manager.getGuildMusicManager(message.guild, message.channel, message).player.volume = Storage.serverVol.get(message.guild)!!

            Storage.playerSearching.put(member, false)
            Storage.playerChooseItem.put(member, null)

        }catch (e:Exception){
            //콘솔 보고서 작성
            System.out.println("Error occurred in CommandListener")
            System.out.println("Reason: " + e.toString())
            //서버 보고서 작성

            val message = Storage.playerChooseMessage.get(member)!!

            message.channel.sendMessage("실행오류!").queue()
            message.channel.sendMessage("에러코드:  " + "`" + e.toString() + "`").queue()
            return
        }
    }

}