package com.Ludicrous245.io.SQL

import com.google.api.client.json.Json
import java.sql.*

class SQLConnector(url:String,user:String,pw:String) {

    private lateinit var connection:Connection

    private lateinit var state:Statement

    init{
        try{
            Class.forName("org.mariadb.jdbc.Driver")

            connection = DriverManager.getConnection(url, user, pw)

            state = connection.createStatement()

            System.out.println("연결됨")
        }catch(e:ClassNotFoundException){
            System.out.println("드라이버 로딩 실패");
        }
    }

    fun push(db:String, table:String , s:String, value:String){
        state.execute("INSERT INTO `$db`.`$table` (`$s`) VALUES (\"$value\");")
        state.close()
    }

    fun takeString(s:String, value:String):String{
        val rs:ResultSet = state.executeQuery("select * from $s")
        val ru = rs.getString(value)
        state.close()

        return ru
    }

    fun takeInt(s:String, value:String):Int{
        val rs:ResultSet = state.executeQuery("select * from $s")
        val ru = rs.getInt(value)
        state.close()

        return ru
    }

    fun takeDouble(s:String, value:String):Double{
        val rs:ResultSet = state.executeQuery("select * from $s")
        val ru =  rs.getDouble(value)
        state.close()

        return ru
    }

    fun takeFloat(s:String, value:String):Float{
        val rs:ResultSet = state.executeQuery("select * from $s")
        val ru = rs.getFloat(value)
        state.close()

        return ru
    }

    fun createS(table: String, s:String, t:SQLTypes){
        val type = SQLHelper.decode(t)
        state.execute("ALTER TABLE `$table`  ADD COLUMN `$s`  $type NULL FIRST")
        state.close()
    }

    fun removeS(table: String, s:String){
        state.execute("ALTER TABLE `$table`  DROP COLUMN `$s")
        state.close()
    }

}