package com.lh1153866.mcserverstatus.models

data class Server (
    var id : String? = null,
    @Transient // thanks to Quan Lam for explaining how to exclude fields from GSON serialization using the @Transient modifier (https://stackoverflow.com/questions/53425600/exclude-a-kotlin-data-class-property-field-from-serialization-deserialization-us)
    var ip : String, // server ip (e.g. hypixel.net), always given even when server is offline. NOT THE SAME AS THE IP RETURNED FROM THE API!!!
    var edition : String? = null, // mc edition (Java/ Bedrock)
    var motd : Motd? = null, // server select screen message of the day
    var online : Boolean, // server online status, always given even when server is offline
    var players : Players? = null, // players online, max players, and player list
    var port : Int, // server port (default is 25565), always given even when server is offline
    var version : String? = null // compatible mc version
        )
