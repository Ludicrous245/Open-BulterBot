package com.Ludicrous245.io.commands.execute

import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel

interface CommandExecutor : CommandBase {
    fun c():String
}