package com.lh1153866.mcserverstatus.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lh1153866.mcserverstatus.R
import com.lh1153866.mcserverstatus.models.Server

class ServerAdapter (
    val context: Context,
    val servers : List<Server>,
    val itemListener : ServerItemListener
        ) : RecyclerView.Adapter<ServerAdapter.ServerViewHolder>() {
    /**
     * Allows connections to elements in the template, item_server.xml file
     */
    inner class ServerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val serverIpTextView = itemView.findViewById<TextView>(R.id.serverIpTextView)
        val serverEditionTextView = itemView.findViewById<TextView>(R.id.serverEditionTextView)
        val checkServerStatusButton = itemView.findViewById<Button>(R.id.checkStatusButton)
    }

    /**
     * Connects the ViewHolder with the RecyclerView
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_server, parent, false)
        return ServerViewHolder(view)
    }

    /**
     *  Binds the ViewHolder to the given server object
     */
    override fun onBindViewHolder(holder: ServerViewHolder, position: Int) {
        val server = servers[position]
        with(holder) {
            serverIpTextView.text = server.ip // set the ip text view value
            serverEditionTextView.text = server.edition // set the server edition text view value

            checkServerStatusButton.setOnClickListener{ // set the onclick of the button
                itemListener.serverSelected(server)
            }
        }
    }

    /**
     * Returns the number of Server in the server list
     */
    override fun getItemCount(): Int {
        return servers.size
    }

    interface ServerItemListener {
        fun serverSelected(server : Server)
    }
}
