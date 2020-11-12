package com.Ludicrous245.Listeners

import com.Ludicrous245.data.BanStackData
import com.Ludicrous245.data.Config
import com.Ludicrous245.data.Storage
import com.Ludicrous245.io.supporter.Embeded
import com.Ludicrous245.io.supporter.Presets
import com.Ludicrous245.io.xyz.Entity.HumanEntity
import com.Ludicrous245.management.BannedUser
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class CommandListener : ListenerAdapter(){

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent){
        val message: Message = event.message
        if (!(message.author.isBot)) {

            val hm: HumanEntity = object : HumanEntity {
                override var stackByPlaylist: Int = 0

                override var stackByPlay: Int = 0

                override var stackByCommand: Int = 0

                override var warn: Int = 0

                override fun setSBP(int: Int) {
                    stackByPlaylist = int
                }

                override fun setSBL(int: Int) {
                    stackByPlay = int
                }

                override fun setSBC(int: Int) {
                    stackByCommand = int
                }

            }

            BanStackData.registerHumanEntity(message.author.idLong, hm)

            /*   if(message.author.id == Config.owner){
                   BanStackData.getHumanEntity(message.author.idLong)!!.warn = 3
               }*/

            if( 2 < BanStackData.getHumanEntity(message.author.idLong)!!.warn){
                BannedUser.ban(message.author.idLong, message)
                BannedUser.register()
            }

            if(!Storage.bannedUsr.isEmpty()) {
                for (bannedID in Storage.bannedUsr) {
                    if (message.author.idLong == bannedID){
                        System.out.println("returned by banned ID")
                        return
                    }
                }
            }

            System.out.println(message.author.asTag + "("  + message.author.idLong + ") : " + message.contentRaw + " : " + message.guild.name + "(" + message.guild.idLong + ")")

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

                                val hm = BanStackData.getHumanEntity(message.author.idLong)!!

                                cmd.a(ctxF, syntax, syntaxRaw, message, ctx[0], event.channel)

                                if(7 == hm.stackByCommand){
                                    val eb = Embeded()

                                    eb.title("워워 진정하세요 진정")
                                    eb.description("너무 빠르게 명령어를 입력하면 불이익이 발생할 수 있습니다.")
                                    eb.color(Presets.alert)
                                    eb.send(message.channel)
                                }

                                hm.setSBC(hm.stackByCommand+1)

                                if(15 <= hm.stackByCommand){
                                    BannedUser.ban(message.author.idLong, message)
                                    BannedUser.register()
                                }

                            }
                        }
                    }
                }catch (e:Exception){
                    val manager = Embeded()
                    manager.title("오! 이런,")
                    manager.field("자잘한 버그가 발생했나봐요", "Error Code: " + "`" + e.toString() + "`")
                    manager.footer("버그는 이곳으로 제보해주세요" + "https://discord.gg/XphePuY ")
                    manager.color(Presets.alert)
                    manager.send(message.channel)
                    return
                }


            }
        }
    }


}