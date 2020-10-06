package com.Ludicrous245.Listeners

import com.Ludicrous245.data.Config
import com.Ludicrous245.data.Storage
import com.Ludicrous245.tools.kits.MuteKit
import com.Ludicrous245.tools.supporter.Embeded
import com.Ludicrous245.tools.supporter.Presets
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class CommandListener : ListenerAdapter(){

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent){
        val message:Message = event.message
        if(!(message.author.isBot)) {

         /*   if(Storage.playerSearching.containsKey(message.member)){
                if(Storage.playerSearching.get(message.member)!!){
                    try {
                        Storage.playerChoose.put(message.member, ctx[0].toInt())
                        Storage.playerSearching.put(message.member, false)
                    }catch (e:java.lang.Exception){
                        return
                    }
                }
            }*/

            if(MuteKit.isMuted(message.guild.id, message.member!!.id)){
                message.delete().queue()
                return
            }

            if(!Storage.guildPrefix.containsKey(message.guild.id)){
                if(Config.dev) {
                    Storage.guildPrefix.put(message.guild.id, ".$")
                }else {
                    Storage.guildPrefix.put(message.guild.id, "$")
                }
            }

            if(!Storage.isLoop.containsKey(message.guild)){
                Storage.isLoop.put(message.guild, false)
            }

            val prefix:String = Storage.guildPrefix.get(message.guild.id)!!

            if(message.contentRaw.startsWith(prefix)){
                for (cmd in Storage.commands) {
                   // try {

                        val ctx:List<String> = message.contentDisplay.replace(prefix, "").split(" ")
                        val cty:List<String> = message.contentRaw.replace(prefix, "").split(" ")

                        val ctxF:ArrayList<String> = ArrayList()
                        val ctxR:ArrayList<String> = ArrayList()

                        var syntax:String = ""
                        var syntaxRaw:String = ""

                        for(cxt in ctx){
                            ctxF.add(cxt)
                        }

                        ctxF.removeAt(0)

                        for(syn in ctxF){
                            if(syntax == ""){
                                syntax += syn
                            }else {
                                syntax += " $syn"
                            }
                        }

                        for(cxt in cty){
                            ctxR.add(cxt)
                        }

                        ctxR.removeAt(0)

                        for(syn in ctxR){
                            if(syntaxRaw == ""){
                                syntaxRaw += syn
                            }else {
                                syntaxRaw += " $syn"
                            }
                        }

                        cmd.cmdRun(ctxF, syntax, syntaxRaw, message, ctx[0], event.channel)
                        
                    /*} catch (e: Exception) {
                        //콘솔 보고서 작성
                        System.out.println("Error occurred in CommandListener")
                        System.out.println("Reason: " + e.toString())
                        //서버 보고서 작성
                        message.channel.sendMessage("실행오류!").queue()
                        message.channel.sendMessage("에러코드:  " + "`" + e.toString() + "`").queue()
                        return
                    }*/
                }
            }
        }
    }
}