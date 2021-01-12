package com.Ludicrous245.io.commands

import com.Ludicrous245.data.Storage
import com.Ludicrous245.io.commands.execute.CommandBase
import com.Ludicrous245.io.commands.execute.CommandExecutor
import net.ldcrF.commands.*
import net.ldcrF.commands.help
import net.ldcrF.devCMD.test
import net.ldcrF.devCMD.unban

open class CommandRegisterer() {
   init {
       a(enko())
       a(gacha())
       a(get())
       a(help())
       a(join())
       a(koen())
       a(leave())
       a(loop())
       a(nowplaying())
       a(pause())
       a(ping())
       a(play())
       a(prefix())
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
       a(unban())
   }
    private fun a(v: CommandBase){
        Storage.commands.add(v)
    }
}