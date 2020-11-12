package com.Ludicrous245.io.commands

import com.Ludicrous245.data.Storage
import com.Ludicrous245.io.commands.execute.CommandExecutor
import net.ldcrF.commands.*
import net.ldcrF.commands.help

open class CommandRegisterer() {
   init {
       a(gacha())
       a(get())
       a(help())
       a(join())
       a(leave())
       a(loop())
       a(nowplaying())
       a(pause())
       a(ping())
       a(play())
       a(queue())
       a(record())
       a(replay())
       a(resume())
       a(reverse())
       a(servers())
       a(shuffle())
       a(skip())
       a(stop())
       a(uptime())
       a(volume())
       a(test())
   }
    private fun a(v: CommandExecutor){
        Storage.commands.add(v)
    }
}