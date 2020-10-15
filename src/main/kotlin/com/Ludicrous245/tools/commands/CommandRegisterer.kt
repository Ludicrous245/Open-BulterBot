package com.Ludicrous245.tools.commands

import com.Ludicrous245.data.Storage
import net.ldcrF.commands.*
import net.ldcrF.commands.help

open class CommandRegisterer() {
   init {
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
       a(reverse())
       a(servers())
       a(shuffle())
       a(skip())
       a(stop())
       a(uptime())
       a(volume())
   }
    private fun a(v:CommandExecutor){
        Storage.commands.add(v)
    }
}