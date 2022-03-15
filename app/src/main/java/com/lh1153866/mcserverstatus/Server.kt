package com.lh1153866.mcserverstatus

import com.google.firebase.Timestamp

data class Server (
    var id : String? = null,
    var ip : String? = null, // server ip (e.g. hypixel.net)
    var edition : String? = null, // mc edition (Java/ Bedrock)
    var lastChecked : Timestamp? = null, // time that the server status was last checked at
    var motd : String? = null, // server select screen message
    var online : Boolean? = null, // server online status
    var playersMax : Int? = null, // max number of players
    var playersNow : Int? = null, // current number of players online
    var playersOnline : ArrayList<String>? = null, // list of current online players
    var port : Int? = null, // server port (default is 25565)
    var versions : ArrayList<String>? = null // list of compatible mc versions
        )
