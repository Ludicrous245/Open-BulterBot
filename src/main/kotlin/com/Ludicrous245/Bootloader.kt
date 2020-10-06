package com.Ludicrous245

import com.Ludicrous245.Listeners.BotInviteListener
import com.Ludicrous245.data.CommandMother
import com.Ludicrous245.data.Config
import com.Ludicrous245.data.Storage
import com.Ludicrous245.data.botData
import com.Ludicrous245.tools.commands.CommandExecutor
import com.Ludicrous245.Listeners.CommandListener
import com.Ludicrous245.Listeners.InteractionListener
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import java.lang.management.ManagementFactory
import java.lang.management.RuntimeMXBean
import kotlin.concurrent.timer

fun main(args:Array<String>){
    /*이름에 걸맞게 봇 부팅을 도와줌
    이름은 사실 그냥 있어보여서 붙임*/
    var mode = 0

    val builder: JDABuilder

    if(Config.dev) {
        builder = JDABuilder.createDefault(Config.devtoken)
    }else{
        builder = JDABuilder.createDefault(Config.token)
    }
    builder.setActivity(Activity.playing("시작중..."))
    builder.addEventListeners(CommandListener())
    builder.addEventListeners(InteractionListener())
    builder.addEventListeners(BotInviteListener())
    val cm = CommandMother()
    CommandExecutor.registerCommands(cm.getCommands())

    val client: JDA = builder.build()

    timer(period = 5000, initialDelay = 1000) {
        if(!Config.debug) {
            when (mode) {
                0 -> {

                    client.presence.activity = Activity.playing(botData.version + "")
                    mode = 1
                }
                1 -> {
                    val runtime: RuntimeMXBean = ManagementFactory.getRuntimeMXBean()
                    val uptime = (((runtime.uptime / 1000) / 60) / 60)

                    client.presence.activity = Activity.playing(uptime.toString() + " 시간 켜져있었어요! ")
                    mode = 2
                }
                2 -> {

                    client.presence.activity = Activity.playing("by Ludicrous245")
                    mode = 0
                }
            }
        }
    }

    Storage.client = client
    System.out.println("봇 켜짐")
    System.out.println(cm.list)
}

