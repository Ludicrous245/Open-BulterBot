package com.Ludicrous245.tools.kits

class ConverterKit {
    companion object{
        fun convertID(rawID: String): String {
            return rawID.replace("<", "").replace("@", "").replace("!", "").replace(">", "")
        }
    }
}