package com.Ludicrous245.io.commands.execute

import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel

interface CommandBase {
    fun a(args:ArrayList<String>, syntax:String, rawSyntax:String, message: Message, content: String, channel: MessageChannel)

    fun b():String
}