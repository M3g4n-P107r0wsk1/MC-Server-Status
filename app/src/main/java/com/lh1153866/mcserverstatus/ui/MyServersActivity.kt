package com.lh1153866.mcserverstatus.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.firebase.ui.auth.AuthUI
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lh1153866.mcserverstatus.R
import com.lh1153866.mcserverstatus.databinding.ActivityMyServersBinding
import com.lh1153866.mcserverstatus.models.Server
import com.lh1153866.mcserverstatus.recycler.ServerAdapter
import com.lh1153866.mcserverstatus.recycler.ServerViewModel

class MyServersActivity : AppCompatActivity(), ServerAdapter.ServerItemListener {

    lateinit var toggle : ActionBarDrawerToggle
    private lateinit var binding : ActivityMyServersBinding
    private lateinit var auth : FirebaseAuth

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyServersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth // setup firebase auth

        /* **************************************************************************************************************
                                                        Navigation Drawer
            Thanks to FoxAndroid for explaining how navigation drawers work (https://www.youtube.com/watch?v=zQh-QGGKPw0)
           ************************************************************************************************************** */
        val drawerLayout : DrawerLayout = binding.myServersDrawer // container for the drawer and main content
        val navView : NavigationView = binding.myServersNavView // navigation menu in the activity

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
            when(menuItem.itemId) { // check which menu item was selected and open activity
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

                else -> {true}
            }
        }


        /* **************************************************************************************************************
                                                            ViewModel
           ************************************************************************************************************** */
        val viewModel : ServerViewModel by viewModels()

        viewModel.getServers().observe(this, { serverList ->
            binding.myServersRecyclerView.adapter = ServerAdapter(this, serverList, this)
            binding.myServersRecyclerView.removeAllViews() // remove any children in the recycler view

            Log.d("ViewModel Server", "Servers in ViewModel: ${serverList.size}") // log # of servers in viewModel
        })


        /* **************************************************************************************************************
                                                       Firebase Auth/ User
           ************************************************************************************************************** */
        if (auth.currentUser == null) { // no user is signed in
            // set text on error textView and button
            binding.myServersErrorTextView.text = "You cannot save servers without signing in"
            binding.fixErrorButton.text = "Sign in"

            // show error box
            binding.errorLinearLayout.visibility = View.VISIBLE

            // create an onClickListener which sends the user to the sign in page
            binding.fixErrorButton.setOnClickListener {
                startActivity(Intent(this, SignInActivity::class.java))
            }
        }
        else { // if the user is signed in
            if (viewModel.getServers().value?.size == 0) { // if the user is signed in but there are no servers saved
                // set text on error textView and button
                binding.myServersErrorTextView.text = "You haven't saved any servers yet"
                binding.fixErrorButton.text = "Browse Popular Servers"

                // show error box
                binding.errorLinearLayout.visibility = View.VISIBLE

                // create an onClickListener which sends the user to the server status main page
                binding.fixErrorButton.setOnClickListener {
                    startActivity(Intent(this, MainActivity::class.java))
                }
            }
            else { // if the user is signed in and there are servers saved, do not show error box
                binding.errorLinearLayout.visibility = View.GONE
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return false
    }

    override fun serverSelected(server: Server) {
        var intent = Intent(this, ServerStatusActivity::class.java) // create an intent to go to the server status screen

        intent.putExtra("edition", server.edition) // add edition to the intent
        intent.putExtra("ip", server.ip) // add ip to the intent
        if (server.port != 0) { // if there is a port to be passed, add it to the intent
            intent.putExtra("port", server.port)
        }

        startActivity(intent)
//        Toast.makeText(this, "Server was selected: ${server.ip}", Toast.LENGTH_LONG).show()
    }
}