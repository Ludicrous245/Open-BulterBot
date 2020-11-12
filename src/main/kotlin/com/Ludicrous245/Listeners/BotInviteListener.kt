package com.Ludicrous245.Listeners

import com.Ludicrous245.io.supporter.Embeded
import com.Ludicrous245.io.supporter.Presets
import net.dv8tion.jda.api.events.guild.GuildJoinEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class BotInviteListener : ListenerAdapter() {

    override fun onGuildJoin(event: GuildJoinEvent) {
            val eb = Embeded()
            eb.description("Bulter를 이용해주셔서 감사합니다.")
            eb.field("초대링크", "https://discord.com/api/oauth2/authorize?client_id=696869028051943434&permissions=8&scope=bot")
            eb.footer("문제점들은 https://discord.gg/XphePuY 으로 문의해주세요!")
            eb.color(Presets.special)

            eb.send(event.guild.textChannels.get(0))
    }
}