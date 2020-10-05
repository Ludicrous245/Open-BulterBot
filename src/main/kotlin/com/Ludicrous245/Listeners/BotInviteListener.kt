package com.Ludicrous245.Listeners

import com.Ludicrous245.data.Storage
import com.Ludicrous245.tools.supporter.Embeded
import com.Ludicrous245.tools.supporter.Presets
import net.dv8tion.jda.api.events.guild.GuildJoinEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import kotlin.concurrent.timer

class BotInviteListener : ListenerAdapter() {

    override fun onGuildJoin(event: GuildJoinEvent) {
            val eb = Embeded()
            eb.description("크롤봇을 이용해주셔서 감사합니다.")
            eb.field("초대링크", "https://discord.com/api/oauth2/authorize?client_id=696869028051943434&permissions=8&scope=bot")
            eb.footer("문제점들은 디스코드 Ludicrous245#1486으로 문의해주세요!")
            eb.color(Presets.special)

            eb.send(event.guild.textChannels.get(0))
    }
}