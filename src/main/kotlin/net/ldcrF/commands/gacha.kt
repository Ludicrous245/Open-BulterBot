package net.ldcrF.commands

import com.Ludicrous245.data.Storage
import com.Ludicrous245.io.commands.execute.CommandExecutor
import com.Ludicrous245.io.supporter.Embeded
import com.Ludicrous245.io.supporter.Presets
import com.Ludicrous245.io.xyz.Entity.GachaMember
import com.Ludicrous245.io.xyz.GachaMemberType
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import java.lang.Exception

class gacha : CommandExecutor {
    override fun a(args: ArrayList<String>, syntax: String, rawSyntax: String, message: Message, content: String, channel: MessageChannel) {
        if(!Storage.gachaMembers.containsKey(message.guild)){
            Storage.gachaMembers.put(message.guild, ArrayList())
            Storage.showableGachaMembers.put(message.guild, ArrayList())
        }

        if(args.size == 0){
            return
        }else if(args.size == 1){
            if(args[0].equals("queue")){

                if(Storage.gachaMembers.get(message.guild)!!.isEmpty()){
                    message.channel.sendMessage( "추첨 리스트가 비어있습니다.").queue()
                    return
                }

                Storage.gachaMembers.get(message.guild)!!.shuffle()

                message.channel.sendMessage(Storage.gachaMembers.get(message.guild)!!.get(0).name + " 이(가) 당첨되었습니다.").queue()

                val gm = Storage.gachaMembers.get(message.guild)!!.removeAt(0)
                Storage.showableGachaMembers.get(message.guild)!!.remove(gm)
            }else if(args[0].equals("master")){

                if(Storage.gachaMembers.get(message.guild)!!.isEmpty()){
                    message.channel.sendMessage( "추첨 리스트가 비어있습니다.").queue()
                    return
                }

                Storage.gachaMembers.get(message.guild)!!.shuffle()

                message.channel.sendMessage(Storage.gachaMembers.get(message.guild)!!.get(0).name + " 이(가) 당첨되었습니다.").queue()
            }else if(args[0].equals("clear")) {
                Storage.gachaMembers.get(message.guild)!!.clear()
                Storage.showableGachaMembers.get(message.guild)!!.clear()
                message.channel.sendMessage( "추첨 리스트가 초기화되었습니다.").queue()
            }else if(args[0].equals("list")){
                val eb = Embeded()
                var i=1
                eb.title("추첨 리스트")
               for(gm in Storage.showableGachaMembers.get(message.guild)!!) {
                    eb.field("" + i, gm.name)
                   i++
               }
                eb.color(Presets.special)

                eb.send(message.channel)
            } else{
                return
            }
        }else if(2 <= args.size){
            if(args[0].equals("add")){

                try {

                    val gm: GachaMember = object : GachaMember {
                        override var name: String = message.mentionedMembers.get(0).user.name

                        override fun gt(): GachaMemberType {
                            return GachaMemberType.HUMAN_ENTITY
                        }

                    }

                    Storage.gachaMembers.get(message.guild)!!.add(gm)
                    Storage.showableGachaMembers.get(message.guild)!!.add(gm)

                    message.channel.sendMessage(gm.name + "이(가) 추가되었습니다.").queue()

                }catch (e : Exception){
                    val gm: GachaMember = object : GachaMember {
                        override var name: String = rawSyntax.replace(args[0], "")

                        override fun gt(): GachaMemberType {
                            return GachaMemberType.TEXT_SCRIPT
                        }

                    }

                    Storage.gachaMembers.get(message.guild)!!.add(gm)
                    Storage.showableGachaMembers.get(message.guild)!!.add(gm)

                    message.channel.sendMessage( gm.name + "이(가) 추가되었습니다.").queue()
                }

            }else if(args[0].equals("remove")){
                try {

                    for (gm in Storage.gachaMembers.get(message.guild)!!) {
                        if(gm.name == message.mentionedMembers.get(0).user.name){
                            message.channel.sendMessage( gm.name + "이(가) 제거되었습니다.").queue()

                            Storage.gachaMembers.get(message.guild)!!.remove(gm)
                            Storage.showableGachaMembers.get(message.guild)!!.remove(gm)
                            break
                        }
                    }

                }catch (e : Exception){

                    for (gm in Storage.gachaMembers.get(message.guild)!!) {
                        if(gm.name == rawSyntax.replace(args[0], "")){
                            message.channel.sendMessage( gm.name + "이(가) 제거되었습니다.").queue()

                            Storage.gachaMembers.get(message.guild)!!.remove(gm)
                            Storage.showableGachaMembers.get(message.guild)!!.remove(gm)
                            break
                        }
                    }

                }
            }else{
                return
            }
        }else{
            return
        }
    }

    override fun b(): String {
        return "gacha"
    }

    override fun c(): String {
        return "!gacha queue로 한명씩 빠지는 추첨을 진행합니다. !gacha master로 빠지지 않는 추첨을 진행합니다 !gacha add <이름 또는 멘션>으로 추첨 목록에 추가합니다. !gacha remove <이름 또는 멘션>으로 추첨 목록에서 제거합니다. !gacha clear 로 추첨 목록을 초기화합니다. !gacha list 로 추첨 목록을 확인합니다."
    }
}