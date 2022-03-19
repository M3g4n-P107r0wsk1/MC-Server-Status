package com.lh1153866.mcserverstatus.data

data class Motd (
    var clean : ArrayList<String>?= null // message of the day can have more than one line
        )
{
    override
    fun toString() : String {
        var motd : String = ""
        if (!clean.isNullOrEmpty()) {
            for (line in clean!!) {
                motd += "$line "
            }
        }

        return motd
    }
}