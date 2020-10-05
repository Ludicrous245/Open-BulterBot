package com.Ludicrous245.data

import com.Ludicrous245.tools.commands.CommandExecutor
import net.ldcrF.commands.audio
import net.ldcrF.commands.dev
import net.ldcrF.commands.info
import net.ldcrF.functions.UserFunction1
import net.ldcrF.functions.guildManagerFunctions
import net.ldcrF.commands.help

class CommandMother {
    val list:ArrayList<CommandExecutor> = ArrayList()

    init {
        register(audio())
        register(help())
        register(info())
        register(guildManagerFunctions())
        register(UserFunction1())
        register(dev())
    }

    private fun register(cmd: CommandExecutor){
        list.add(cmd)
    }

    fun getCommands(): ArrayList<CommandExecutor>{
        return list
    }
}