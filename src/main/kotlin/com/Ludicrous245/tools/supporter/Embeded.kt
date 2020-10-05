package com.Ludicrous245.tools.supporter

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.PrivateChannel
import net.dv8tion.jda.api.entities.TextChannel
import java.awt.Color

class Embeded() {
    /*
    굳이 이걸 쓸 메리트는 없었지만 굳이 찾자면 기존거보다 약간 편하다는것 정도?
    근데 사용 가능한 메소드가 기존보다 많이 제한적임...
    메소드들도 속도에 친화적이다 보니까 직관적이라고 보기는 힘듬.
    그냥 기존거보다 살짝 만지기 빠르다에서 그 이상도 이하도 아님.
    사실 일일이 푸터 달기 귀찮아서 만들긴 했다만...
     */
    private var eb: EmbedBuilder = EmbedBuilder()

    fun title(content: String) {
        eb.setTitle(content)
    }

    fun title(content: String, link:String) {
        eb.setTitle(content, link)
    }

    fun description(content: String) {
        eb.setDescription(content)
    }

    fun field(val1: String, val2: String) {
        eb.addField(val1, val2, false)
    }

    fun field(val1: String, val2: String, inLine: Boolean) {
        eb.addField(val1, val2, inLine)
    }

    fun color(color: Int) {
        eb.setColor(color)
    }

    fun color(color: Color) {
        eb.setColor(color)
    }

    fun thumb(url: String) {
        eb.setThumbnail(url)
    }

    fun img(url: String) {
        eb.setImage(url)
    }

    fun footer(desc:String){
        eb.setFooter(desc)
    }

    //여기 디프리케이티드는 그냥 내가 잘 안쓸 메소드라 걸어둠
    //별 이유는 없음
    @Deprecated("")
    fun author(name: String) {
        eb.setAuthor(name, null, null)
    }

    fun author(name: String, yourIconURL: String) {
        eb.setAuthor(name, null, yourIconURL)
    }

    //여기도 위와 동일
    @Deprecated("")
    fun author(name: String, yourURL: String, yourIconURL: String) {
        eb.setAuthor(name, yourURL, yourIconURL)
    }

    fun send(channel: MessageChannel) {
        channel.sendMessage(eb!!.build()).queue()
    }

    fun send(channel: TextChannel) {
        channel.sendMessage(eb.build()).queue()
    }

    fun send(channel: PrivateChannel) {
        channel.sendMessage(eb.build()).queue()
    }

    fun sendAndGet(channel: MessageChannel):Message{
        return channel.sendMessage(eb.build()).complete()
    }
}