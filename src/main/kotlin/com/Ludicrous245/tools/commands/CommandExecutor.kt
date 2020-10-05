package com.Ludicrous245.tools.commands

import com.Ludicrous245.data.Storage
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import java.lang.Exception

open class CommandExecutor {
    //커맨드 실행이랑 이벤트 관리
    //아까 그 저장소로 커맨드를 송출하는 메소드도 여기 있음

    private val commandlist:ArrayList<String> = ArrayList()

    companion object{
        fun registerCommand(command: CommandExecutor){
            //커맨드 단일 등록
            Storage.commands.add(command)
        }

        fun registerCommands(commands:ArrayList<CommandExecutor>){
            try {
                /*이건 리스트로 압축된걸 압축을 풀어서 다시 리스트로 넣는 방식인데
                비효율적인거 처럼 보일지는 몰라도
                내가 구현할 수 있는건 이게 한계다.
                잘 굴러가는거에 만족하자.*/

                for (cmd in commands) {
                    Storage.commands.add(cmd)
                }

            }catch (e:Exception){
                System.out.println("Failed to Load commands")
                System.out.println("Reason: " + e.toString())
            }
        }
    }

    open fun cmdRun(args:ArrayList<String>, syntax:String, rawSyntax:String, message:Message, content: String, channel: MessageChannel){

    }
}