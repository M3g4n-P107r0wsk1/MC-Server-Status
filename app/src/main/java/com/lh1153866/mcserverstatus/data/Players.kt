package com.lh1153866.mcserverstatus.data

data class Players (
    var online : Int? = null, // players online
    var max : Int? = null, // max number of players
    var list : ArrayList<String>? = null // player list (what players are online)
        )