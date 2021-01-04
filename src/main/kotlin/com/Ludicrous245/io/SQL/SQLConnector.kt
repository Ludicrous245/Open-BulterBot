package com.Ludicrous245.io.SQL

import com.google.api.client.json.Json
import org.omg.Messaging.SYNC_WITH_TRANSPORT
import java.lang.Exception
import java.sql.*

class SQLConnector(url:String,user:String,pw:String) {

   lateinit var connection:Connection

   lateinit var state:Statement

    init{
        try{
            Class.forName("org.mariadb.jdbc.Driver")

            connection = DriverManager.getConnection(url, user, pw)

            state = connection.createStatement()

        }catch(e:ClassNotFoundException){
            System.out.println("드라이버 로딩 실패");
        }catch (e:Exception){
            System.out.println("연결 실패")
        }
    }

    fun isNull(tester: SQLNullTester):Boolean{
        try{
            tester.run()
        }catch (e:Exception){
            return true
        }

        if(tester.run() == SQLProgressResult.NULL){
            return true
        }

        return false
    }

    fun take(table:String, from:String,  key:String, value:String):String?{
        val ps = connection.prepareStatement("select * from $table WHERE $from=\"$key\"")
        val rs = ps.executeQuery()
        if(rs.next()) {
            val ru = rs.getString(value)

            return ru
        }
        return null
    }

    fun takeInt(table:String, from:String,  key:String, value:String):Int?{
        val ps = connection.prepareStatement("select * from $table WHERE $from=\"$key\"")
        val rs = ps.executeQuery()
        if(rs.next()) {
            val ru = rs.getInt(value)

            return ru
        }
        return null
    }

    fun update(table:String, what:String, how:String,where:String, key: String){
        state.execute("UPDATE  $table SET $what='$how' WHERE $where='$key';")
    }

    fun update(table:String, what:String, how:Int,where:String, key: String){
        state.execute("UPDATE  $table SET $what='$how' WHERE $where='$key';")
    }
}