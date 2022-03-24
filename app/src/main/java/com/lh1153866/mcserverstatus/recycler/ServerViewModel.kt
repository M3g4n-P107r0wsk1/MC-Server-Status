package com.lh1153866.mcserverstatus.recycler

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.lh1153866.mcserverstatus.models.Server

class ServerViewModel : ViewModel() {
    private val servers = MutableLiveData<List<Server>>()

    init {
        val userId = Firebase.auth.currentUser?.uid

        // connect to firebase db and get all servers associated with the signed in user
        val db = FirebaseFirestore.getInstance().collection("servers")
            .whereEqualTo("uid", userId)
            .orderBy("ip")
            .addSnapshotListener{ documents, exception ->
                if (exception != null) { // print any exceptions to the console if there are any
                    Log.w("DB Response", "Snapshot Listener Failed: ${exception.code}")
                    return@addSnapshotListener
                }

                documents?.let { // create server objects by looping over documents
                    val serverList = ArrayList<Server>()

                    for (d in documents) { // add all servers to arraylist
                        Log.i("DB RESPONSE", "${d.data}")
                        val server = d.toObject(Server::class.java)

                        serverList.add(server)
                    }

                    servers.value = serverList // set the mutable live data to the server list
                }
            }
    }

    fun getServers() : LiveData<List<Server>> {
        return servers
    }
}