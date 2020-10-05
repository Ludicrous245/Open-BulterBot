package net.ldcrF.commands

import com.Ludicrous245.data.Config
import com.Ludicrous245.data.Storage
import com.Ludicrous245.tools.commands.under.CommonCommandExecutor
import com.Ludicrous245.tools.supporter.Embeded
import com.Ludicrous245.tools.supporter.Presets
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel

class help : CommonCommandExecutor(){
    override fun cmdRun(args: ArrayList<String>, syntax: String, rawSyntax: String, message: Message, content: String, channel: MessageChannel) {
        if(content.equals("help")){
            if(args.size == 0){
                help(message, channel)
            }else if(args.size == 1) {
                if (args[0] == "music") {
                    helpMusic(message, channel)
                }else if(args[0] == "dev"){
                    devTools(message, channel)
                }
            }else{

            }
        }
    }

    fun help(message:Message, channel: MessageChannel){
        val prefix:String = Storage.guildPrefix.get(message.guild.id)!!
        val eb:Embeded = Embeded()
        eb.title("도움말")
        eb.color(Presets.normal)
        eb.field(prefix+"info", "봇의 정보를 확인합니다.")
        eb.field(prefix+"ping", "핑을 확인합니다.")
        eb.field(prefix+"prefix", "봇의 접두사를 임시로 변경합니다.(길드마다 적용)")
        eb.field(prefix+"mute + <@유저>", "해당 유저에게 임시채금을 선사합니다. (길드 관리자 권한 필요)")
        eb.field(prefix+"unmute + <@유저>", "해당 유저의 임시채금을 해제합니다. (길드 관리자 권한 필요)")
        eb.field(prefix+"kick + <@유저>", "해당 유저를 추방합니다. (주의 요망! 길드 관리자 권한 필요)")
        eb.field(prefix+"uptime", "업타임을 확인합니다.")
        eb.field(prefix+"get + <@유저> 또는 <guild>", "해당 유저 또는 길드의 정보를 확인합니다.")
        eb.field(prefix+"help music", "음악기능 관련 도움말을 확인합니다.")
        eb.footer("쪽괄호는 필수사항, 소괄호는 선택사항")
        eb.send(channel)
    }

    fun helpMusic(message:Message, channel: MessageChannel){
        val prefix:String = Storage.guildPrefix.get(message.guild.id)!!
        val eb:Embeded = Embeded()
        eb.title("음악 도움말")
        eb.color(Presets.normal)
        eb.field(prefix+"play 또는 p + <URL(유튜브 플레이리스트 사용 불가)> 또는 <제목>", "음악을 재생합니다. (URL지원 플랫폼: Youtube, SoundCloud) 제목을 입력하는 경우, 유튜브 검색 최상단부터 5개를 골라 출력한 후, 선택지를 생성합니다.")
        eb.field(prefix+"skip", "음악을 건너뜁니다.)")
        eb.field(prefix+"stop", "큐를 비우고 모든 음악을 종료합니다. (길드 관리자 권한 필요)")
        eb.field(prefix+"queue", "현재 재생중인 음악과 큐에 있는 음악들을 조회합니다.")
        eb.field(prefix+"nowPlaying 또는 np", "현재 재생중인 음악의 정보를 조회합니다.")
        eb.field(prefix+"loop", "큐를 반복합니다.")
        eb.field(prefix+"pause", "일시정지 또는 재생상태로 만듭니다.")
        eb.field(prefix+"volume 또는 vol","볼륨을 변경합니다. 0~100까지 가능.")
        eb.field(prefix+"reverse","대기열을 뒤집습니다.")
        eb.field(prefix+"shuffle","대기열을 무작위로 섞습니다.")
        eb.footer("음악기능의 범위는 모두 '길드단위' 입니다.")
        eb.send(channel)
    }

    fun devTools(message:Message, channel: MessageChannel){
        if(message.author.id != Config.owner) return

        val prefix:String = Storage.guildPrefix.get(message.guild.id)!!
        val eb:Embeded = Embeded()
        eb.title("개발자 도움말")
        eb.color(Presets.special)
        eb.field(prefix+"debug", "디버그모드로 전환(재부팅시 사라짐)")
        eb.field(prefix+"picker", "입력한 인자를 모두 날것인 상태에서 콘솔로 반환(이모지 처리할때 사용)")
        eb.send(channel)
    }
}