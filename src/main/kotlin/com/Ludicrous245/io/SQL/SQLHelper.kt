package com.Ludicrous245.io.SQL

class SQLHelper {
    companion object{
        fun decode(type:SQLTypes):String{
            if(type.equals(SQLTypes.DOUBLE)){
                return "DOUBLE"
            }else if(type.equals(SQLTypes.FLOAT)){
                return "FLOAT"
            }else if(type.equals(SQLTypes.INT)){
                return "INT"
            }else if(type.equals(SQLTypes.TEXT)){
                return "TEXT"
            }else{
                return ""
            }
        }

    }
}