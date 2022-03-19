package com.lh1153866.mcserverstatus.data

import android.app.Application
import android.util.Log
import com.google.gson.Gson
import java.net.URL

class ServerStatusAPI(val app: Application) {

    fun getJavaServerStatus(ip : String) {
        // start a new thread to run api lookup


        // get info from api
        //  var apiResponse = URL("https://api.mcsrvstat.us/2/"+ intent.getStringExtra("ip")).readText()
        var apiResponse = URL("https://api.mcsrvstat.us/2/play.deathorglorysmp.tk").readText()

        // read json and convert to kotlin object
//        var server: Server = Gson().fromJson(apiResponse, Server::class.java)

        // log kotlin object
//    Log.d("Server Object", "server ip: ${server.id} | server motd: ${server.motd.toString()}")
    }

    fun getBedrockServerStatus (ip: String, port: Int) {
        // start a new thread to run api lookup


        // get info from api
//        var apiResponse = URL("https://api.mcsrvstat.us/bedrock/2/" + intent.getStringExtra("ip")).readText()
        var apiResponse = URL("https://api.mcsrvstat.us/2/vanitype.ml:19132").readText()

        // read json and convert to kotlin object
//        var server: Server = Gson().fromJson(apiResponse, Server::class.java)

        // log kotlin object
//        Log.d("Server Object", "server ip: ${server.id} | server motd: ${server.motd.toString()}")
    }
}