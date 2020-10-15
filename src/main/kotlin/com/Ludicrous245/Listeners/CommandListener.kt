package com.Ludicrous245.Listeners

import com.Ludicrous245.data.Config
import com.Ludicrous245.data.Storage
import com.Ludicrous245.tools.supporter.Embeded
import com.Ludicrous245.tools.supporter.Presets
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class CommandListener : ListenerAdapter(){

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent){
            val message: Message = event.message
            if (!(message.author.isBot)) {

                if(!Storage.bannedUsr.isEmpty()) {
                    for (bannedID in Storage.bannedUsr) {
                        if (message.author.idLong == bannedID){
                            System.out.println("returned by banned ID")
                            return
                        }
                    }
                }

                //System.out.println(message.author.asTag + "("  + message.author.idLong + ") : " + message.contentRaw + " : " + message.guild.name + "(" + message.guild.idLong + ")")

                if (!Storage.isLoop.containsKey(message.guild)) {
                    Storage.isLoop.put(message.guild, false)
                }

                var prefix = "$"

                if(Config.dev){
                    prefix = "%"
                }

                if (message.contentRaw.startsWith(prefix)) {

                    val ctx: List<String> = message.contentDisplay.replace(prefix, "").split(" ")
                    val cty: List<String> = message.contentRaw.replace(prefix, "").split(" ")

                    val ctxF: ArrayList<String> = ArrayList()
                    val ctxR: ArrayList<String> = ArrayList()

                    var syntax: String = ""
                    var syntaxRaw: String = ""

                    for (cxt in ctx) {
                        ctxF.add(cxt)
                    }

                    ctxF.removeAt(0)

                    for (syn in ctxF) {
                        if (syntax == "") {
                            syntax += syn
                        } else {
                            syntax += " $syn"
                        }
                    }

                    for (cxt in cty) {
                        ctxR.add(cxt)
                    }

                    ctxR.removeAt(0)

                    for (syn in ctxR) {
                        if (syntaxRaw == "") {
                            syntaxRaw += syn
                        } else {
                            syntaxRaw += " $syn"
                        }
                    }

        try {
            for (cmd in Storage.commands) {
                val cl: ArrayList<String> = ArrayList()

                cl.addAll(cmd.b().split(","))
                for (ci in cl) {
                    if (ci.equals(ctx[0])) {

                        cmd.a(ctxF, syntax, syntaxRaw, message, ctx[0], event.channel)

                    }
                }
            }
        }catch (e:Exception){
            val manager = Embeded()
            manager.title("오! 이런,")
            manager.field("자잘한 버그가 발생했나봐요", "Error Code: " + "`" + e.toString() + "`")
            manager.color(Presets.alert)
            manager.send(message.channel)
            return
        }


            }
        }
    }
}