package com.lh1153866.mcserverstatus.data

data class Server (
    var id : String? = null,
    var ip : String? = null, // server ip (e.g. hypixel.net)
//    var edition : String? = null, // mc edition (Java/ Bedrock)
//    var lastChecked : Timestamp? = null, // time that the server status was last checked at
    var motd : Motd? = null, // server select screen message of the day
    var online : Boolean? = null, // server online status
    var players : Players? = null, // players online, max players, and player list
    var port : Int? = null, // server port (default is 25565)
    var versions : String? = null // compatible mc version
        )
