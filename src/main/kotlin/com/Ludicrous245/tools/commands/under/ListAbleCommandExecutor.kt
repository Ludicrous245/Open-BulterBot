package com.Ludicrous245.tools.commands.under

import com.Ludicrous245.tools.commands.CommandExecutor
import net.dv8tion.jda.api.entities.Member

open class ListAbleCommandExecutor : CommandExecutor(){

    private val StringList:HashMap<String, ArrayList<String>> = HashMap()

    private val MemberList:HashMap<String, ArrayList<Member>> = HashMap()

    fun addStringList(name:String, list:ArrayList<String>){
        StringList.put(name, list)
    }

    fun pushTo(name:String, value:String){
        if(!StringList.containsKey(name)){
            StringList.put(name, ArrayList())
        }

        StringList.get(name)!!.add(value)
    }

    fun findStringList(name:String):ArrayList<String>?{
        return StringList.get(name)
    }

    fun addMemberList(name:String, list:ArrayList<Member>){
        MemberList.put(name, list)
    }

    fun pushTo(name:String, value:Member){
        if(!MemberList.containsKey(name)){
            MemberList.put(name, ArrayList())
        }

        MemberList.get(name)!!.add(value)
    }

    fun findMemberList(name:String):ArrayList<Member>?{
        return MemberList.get(name)
    }

}