package com.lh1153866.mcserverstatus.models

data class Players (
    var online : Int? = null, // players online
    var max : Int? = null, // max number of players
    var list : ArrayList<String>? = null // player list (what players are online)
        ) {
    fun getOnlinePlayers(): Int? {
        return online
    }

    fun getMaxPlayers() : Int? {
        return max
    }

    fun getPlayerList() : ArrayList<String>? {
        return list
    }
}