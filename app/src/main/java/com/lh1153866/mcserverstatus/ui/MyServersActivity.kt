package com.lh1153866.mcserverstatus.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lh1153866.mcserverstatus.R
import com.lh1153866.mcserverstatus.databinding.ActivityMyServersBinding
import com.lh1153866.mcserverstatus.recycler.ServerViewModel

class MyServersActivity : AppCompatActivity() {

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

                else -> {true}
            }
        }

        /* **************************************************************************************************************
                                                            ViewModel
           ************************************************************************************************************** */
        var serverAmount = 0 // number of servers the viewModel is storing
        val viewModel : ServerViewModel by viewModels()

        viewModel.getServers().observe(this, { serverList ->
            for (server in serverList) {
                Log.i("DB_Response", "server in data: $server")
                serverAmount++
            }
        })

        /* **************************************************************************************************************
                                                       Firebase Auth/ User
           ************************************************************************************************************** */
        if (auth.currentUser == null) { // no user is signed in
            // show error box
            binding.errorLinearLayout.visibility = View.VISIBLE

            // set text on error textView and button
            binding.myServersErrorTextView.text = "You cannot save servers without signing in"
            binding.fixErrorButton.text = "Sign in"

            // create an onClickListener which sends the user to the sign in page
            binding.fixErrorButton.setOnClickListener {
                startActivity(Intent(this, SignInActivity::class.java))
            }
        }
        else { // if the user is signed in
            if (serverAmount == 0) { // if the user is signed in but there are no servers saved
                // show error box
                binding.errorLinearLayout.visibility = View.VISIBLE

                // set text on error textView and button
                binding.myServersErrorTextView.text = "You haven't saved any servers yet"
                binding.fixErrorButton.text = "Browse Popular Servers"

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
}