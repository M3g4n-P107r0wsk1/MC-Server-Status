package com.lh1153866.mcserverstatus.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.firebase.ui.auth.AuthUI
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.lh1153866.mcserverstatus.R
import com.lh1153866.mcserverstatus.models.Server
import com.lh1153866.mcserverstatus.databinding.ActivityServerStatusBinding

class ServerStatusActivity : AppCompatActivity() {

    lateinit var toggle : ActionBarDrawerToggle
    private lateinit var binding : ActivityServerStatusBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServerStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth // setup firebase auth
        var server = Server("","","","",null,false,null,0,"") // default server params

        /* **************************************************************************************************************
                                                        Navigation Drawer
            Thanks to FoxAndroid for explaining how navigation drawers work (https://www.youtube.com/watch?v=zQh-QGGKPw0)
           ************************************************************************************************************** */
        val drawerLayout : DrawerLayout = binding.serverStatusDrawer // container for the drawer and main content
        val navView : NavigationView = binding.serverStatusNavView // navigation menu in the activity

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // check if the user is signed in or not and show respective sign in/ out option
        if (auth.currentUser == null) { // if the user is not signed in
            navView.menu.getItem(6).isVisible = false // sign out is hidden
            navView.menu.getItem(5).isVisible = true // sign in is shown
        }
        else { // if the user is signed in
            navView.menu.getItem(5).isVisible = false // sign in is hidden
            navView.menu.getItem(6).isVisible = true // sign out is shown
        }

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) { // check which menu item was selected and open activity
                R.id.serverStatusMenuBar -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.myServersMenuBar -> {
                    startActivity(Intent(this, MyServersActivity::class.java))
                    true
                }
                R.id.helpMenuBar -> {
                    startActivity(Intent(this, HelpActivity::class.java))
                    true
                }
                R.id.aboutMenuBar -> {
                    startActivity(Intent(this, AboutAppActivity::class.java))
                    true
                }
                R.id.bugReportMenuBar -> {
                    startActivity(Intent(this, BugReportActivity::class.java))
                    true
                }
                R.id.signInMenuBar -> {
                    startActivity(Intent(this, SignInActivity::class.java))
                    true
                }
                R.id.signOutMenuBar -> {
                    AuthUI.getInstance().signOut(this).addOnSuccessListener {
                        Toast.makeText(this,"Successfully signed out", Toast.LENGTH_LONG).show()
                    }
                    startActivity(Intent(this, this::class.java))
                    finish()
                    true
                }

                else -> {
                    true
                }
            }
        }


        /* **************************************************************************************************************
                                                         API/ Server Status
           ************************************************************************************************************** */
        // hide all fields before app loads (will be shown once there is information)
        binding.ipTableRow.visibility = View.GONE
        binding.statusTextView.visibility = View.GONE
        binding.versionTableRow.visibility = View.GONE
        binding.motdTableRow.visibility = View.GONE
        binding.playersTableRow.visibility = View.GONE
        binding.playerListTableRow.visibility = View.GONE
        binding.serverStatusErrorTableRow.visibility = View.GONE

        when {
            intent.getStringExtra("edition") == "Java" -> {
                var url = "https://api.mcsrvstat.us/2/${intent.getStringExtra("ip")}" // use the Java edition version of api

                // thanks to Giru Bhai for giving alternatives to AsyncTask (https://stackoverflow.com/questions/19740604/how-to-fix-networkonmainthreadexception-in-android)
                val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null, // create request for json
                    { response -> // show results based on json results
                        Log.d("Json returned", "Response: %s".format(response.toString()))

                        // use gson to parse json
                        val gson = Gson()
                        server = gson.fromJson(response.toString(), Server::class.java)

                        binding.ipTextView.text = "%s".format(intent.getStringExtra("ip"))// show ip regardless if the server is online or offline
                        if (server.online) {
                            binding.statusTextView.text = "%s".format("online")
                            binding.statusTextView.setTextColor(resources.getColor(R.color.online, null))
                            binding.versionTextView.text = server.version

                            if (server.motd != null) { // check if there is a server message
                                binding.motdTextView.text = server.motd.toString()
                            }
                            else { // if there is none, use the default one
                                binding.motdTextView.text = "%s".format("A Minecraft Server")
                            }

                            if (server.players != null) { // check if there is player data
                                // show players online versus max players
                                binding.playerCountTextView.text = "%d / %d".format(server.players!!.online, server.players!!.max)
                                binding.playersTableRow.visibility = View.VISIBLE

                                // show a list of the players online, if one exists
                                if (!server.players!!.list.isNullOrEmpty()) { // check that the player list is not null or empty
                                    var playerList = ""
                                    for(player in server.players!!.list!!) {
                                        playerList += "$player\n"
                                    }
                                    binding.playerListTableRow.visibility = View.VISIBLE
                                    binding.playerListTextView.text = playerList
                                }
                                else {
                                    binding.playerListTableRow.visibility = View.GONE
                                }
                            }
                            else { // do not show rows when there is no player data
                                binding.playersTableRow.visibility = View.GONE
                                binding.playerListTableRow.visibility = View.GONE
                            }

                            // show possibly hidden fields
                            binding.ipTableRow.visibility = View.VISIBLE
                            binding.statusTextView.visibility = View.VISIBLE
                            binding.versionTableRow.visibility = View.VISIBLE
                            binding.motdTableRow.visibility = View.VISIBLE

                            // hide error field
                            binding.serverStatusErrorTableRow.visibility = View.GONE
                        }
                        else {
                            binding.statusTextView.text = "offline"
                            binding.statusTextView.setTextColor(resources.getColor(R.color.offline, null))

                            // hide not used fields
                            binding.serverStatusErrorTableRow.visibility = View.GONE
                            binding.versionTableRow.visibility = View.GONE
                            binding.motdTableRow.visibility = View.GONE
                            binding.playersTableRow.visibility = View.GONE
                            binding.playerListTableRow.visibility = View.GONE
                        }
                    },
                    { error -> // show error message if there is an error
                        Toast.makeText(this, "There was an error finding the server status", Toast.LENGTH_LONG).show()

                        var err = "%s\n%s".format("There was an error find the server status! Please reset your network connection and if the problem persists, please submit a bug report detailing what is happened along with the following error:", error.localizedMessage)
                        binding.serverStatusError.text = err
                        Log.e("API Error", error.localizedMessage)

                        // hide all fields and show error field
                        binding.ipTableRow.visibility = View.GONE
                        binding.statusTextView.visibility = View.GONE
                        binding.versionTableRow.visibility = View.GONE
                        binding.motdTableRow.visibility = View.GONE
                        binding.playersTableRow.visibility = View.GONE
                        binding.playerListTableRow.visibility = View.GONE
                        binding.serverStatusErrorTableRow.visibility = View.VISIBLE
                    }
                )

                var requestQueue : RequestQueue = Volley.newRequestQueue(this) // create request queue
                requestQueue.add(jsonObjectRequest) // send request to queue
            }

            intent.getStringExtra("edition") == "Bedrock" -> {
                var url = "https://api.mcsrvstat.us/bedrock/2/${intent.getStringExtra("ip")}" // use the Bedrock edition version of api

                // thanks to Giru Bhai for giving alternatives to AsyncTask (https://stackoverflow.com/questions/19740604/how-to-fix-networkonmainthreadexception-in-android)
                val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null, // create request for json
                    { response -> // show results based on json results
                        Log.d("Json returned", "Response: %s".format(response.toString()))

                        // use gson to parse json
                        val gson = Gson()
                        server = gson.fromJson(response.toString(), Server::class.java)

                        binding.ipTextView.text = "%s:%s".format(intent.getStringExtra("ip"), intent.getStringExtra("port"))// show ip regardless if the server is online or offline
                        if (server.online) {
                            binding.statusTextView.text = "%s".format("online")
                            binding.statusTextView.setTextColor(resources.getColor(R.color.online, null))
                            binding.versionTextView.text = server.version

                            if (server.motd != null) { // check if there is a server message
                                binding.motdTextView.text = server.motd.toString()
                            }
                            else { // if there is none, use the default one
                                binding.motdTextView.text = "%s".format("A Minecraft Server")
                            }

                            if (server.players != null) { // check if there is player data
                                // show players online versus max players
                                binding.playerCountTextView.text = "%d / %d".format(server.players!!.online, server.players!!.max)
                                binding.playersTableRow.visibility = View.VISIBLE

                                // show a list of the players online, if one exists
                                if (!server.players!!.list.isNullOrEmpty()) { // check that the player list is not null or empty
                                    var playerList = ""
                                    for(player in server.players!!.list!!) {
                                        playerList += "$player\n"
                                    }
                                    binding.playerListTableRow.visibility = View.VISIBLE
                                    binding.playerListTextView.text = playerList
                                }
                                else {
                                    binding.playerListTableRow.visibility = View.GONE
                                }
                            }
                            else { // do not show rows when there is no player data
                                binding.playersTableRow.visibility = View.GONE
                                binding.playerListTableRow.visibility = View.GONE
                            }

                            // show possibly hidden fields
                            binding.ipTableRow.visibility = View.VISIBLE
                            binding.statusTextView.visibility = View.VISIBLE
                            binding.versionTableRow.visibility = View.VISIBLE
                            binding.motdTableRow.visibility = View.VISIBLE

                            // hide error field
                            binding.serverStatusErrorTableRow.visibility = View.GONE
                        }
                        else {
                            binding.statusTextView.text = "offline"
                            binding.statusTextView.setTextColor(resources.getColor(R.color.offline, null))

                            // hide not used fields
                            binding.serverStatusErrorTableRow.visibility = View.GONE
                            binding.versionTableRow.visibility = View.GONE
                            binding.motdTableRow.visibility = View.GONE
                            binding.playersTableRow.visibility = View.GONE
                            binding.playerListTableRow.visibility = View.GONE
                        }
                    },
                    { error -> // show error message if there is an error
                        Toast.makeText(this, "There was an error finding the server status", Toast.LENGTH_LONG).show()

                        var err = "%s\n%s".format("There was an error find the server status! Please reset your network connection and if the problem persists, please submit a bug report detailing what is happened along with the following error:", error.localizedMessage)
                        binding.serverStatusError.text = err
                        Log.e("API Error", error.localizedMessage)

                        // hide all fields and show error field
                        binding.ipTableRow.visibility = View.GONE
                        binding.statusTextView.visibility = View.GONE
                        binding.versionTableRow.visibility = View.GONE
                        binding.motdTableRow.visibility = View.GONE
                        binding.playersTableRow.visibility = View.GONE
                        binding.playerListTableRow.visibility = View.GONE
                        binding.serverStatusErrorTableRow.visibility = View.VISIBLE
                    }
                )

                var requestQueue : RequestQueue = Volley.newRequestQueue(this) // create request queue
                requestQueue.add(jsonObjectRequest) // send request to queue
            }

            else -> {
                Toast.makeText(this, "There was an error loading the page!", Toast.LENGTH_LONG).show()

                binding.serverStatusError.text = "%s".format("No Minecraft edition was chosen, please submit a bug report with this error to the developer including what happened before it showed up")

                // hide all fields and show error field
                binding.ipTableRow.visibility = View.GONE
                binding.statusTextView.visibility = View.GONE
                binding.versionTableRow.visibility = View.GONE
                binding.motdTableRow.visibility = View.GONE
                binding.playersTableRow.visibility = View.GONE
                binding.playerListTableRow.visibility = View.GONE
                binding.serverStatusErrorTableRow.visibility = View.VISIBLE
            }
        }


        /* **************************************************************************************************************
                                                        Write to Firebase
           ************************************************************************************************************** */
        binding.addToMyServersButton.setOnClickListener{
            if (auth.currentUser != null) { // check if a user is signed in
                if (binding.serverStatusError.text.isNullOrBlank() || binding.serverStatusErrorTableRow.visibility == View.GONE) { // if there are no errors
                    // connect to firebase
                    val firebaseDB = FirebaseFirestore.getInstance().collection("servers")

                    // get the id from firebase
                    val id = firebaseDB.document().getId()

                    // the signed in user's id from firebase
                    val uID = auth.currentUser!!.uid

                    // server ip inputted by the user
                    val ip = intent.getStringExtra("ip").toString()

                    server.id = id // set the server id
                    server.uID = uID // set the uID of the current user to the server value
                    server.ip = ip // set the server ip the user entered
                    server.edition = intent.getStringExtra("edition").toString() // set the server edition type

                    // check if the user has already saved the server
                    val firebaseItemSearch = FirebaseFirestore.getInstance().collection("servers")
                        .whereEqualTo("uid", uID)
                        .whereEqualTo("ip", ip)
                        .whereEqualTo("port", server.port)

                    firebaseItemSearch.get()
                        .addOnSuccessListener{ document ->
                            if (document.size() != 0) {
                                Log.d("Server Saved","Server has already been saved: ${document}")
                                Toast.makeText(this, "You have already saved this server", Toast.LENGTH_LONG).show()
                            }
                            else {
                                // save to firebase db
                                firebaseDB.document(id).set(server)
                                    .addOnSuccessListener{
                                        Toast.makeText(this, "Server ip saved to \"My Servers\"", Toast.LENGTH_LONG).show()
                                        Log.d("DB Write", "DB successfully wrote")
                                    }
                                    .addOnFailureListener{ err ->
                                        Toast.makeText(this, "Error saving to database, try again later", Toast.LENGTH_LONG).show()
                                        Log.w("DB Error", "Error saving server to database: ${err.localizedMessage}")
                                    }
                            }
                        }
                        .addOnFailureListener{
                            Toast.makeText(this, "Search Error! Please report this in the bug tracker", Toast.LENGTH_LONG).show()
                        }
                }
                else { // if there was an error finding the status, do not let the user save the server
                    Toast.makeText(this, "There was an error, please try again later", Toast.LENGTH_LONG).show()
                }
            }
            else {
                Toast.makeText(this, "You must be signed in to save servers", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, SignInActivity::class.java))
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return false
    }
}