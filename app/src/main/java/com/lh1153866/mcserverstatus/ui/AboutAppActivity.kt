package com.lh1153866.mcserverstatus.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.text.HtmlCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.firebase.ui.auth.AuthUI
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lh1153866.mcserverstatus.R
import com.lh1153866.mcserverstatus.databinding.ActivityAboutAppBinding

class AboutAppActivity : AppCompatActivity() {

    lateinit var toggle : ActionBarDrawerToggle
    private lateinit var binding : ActivityAboutAppBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth // setup firebase auth

        /* **************************************************************************************************************
                                                        Navigation Drawer
            Thanks to FoxAndroid for explaining how navigation drawers work (https://www.youtube.com/watch?v=zQh-QGGKPw0)
           ************************************************************************************************************** */
        val drawerLayout : DrawerLayout = binding.aboutDrawer // container for the drawer and main content
        val navView : NavigationView = binding.aboutNavView // navigation menu in the activity

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
                                                     About App
           ************************************************************************************************************** */
        val bugReportGuide = "<p>This app is created to find the online status of both Java and Bedrock Minecraft servers. Please keep in mind that to use this app, you need to have a stable internet or wifi connection.</p><p><b>Version:</b> 1.0.0</p><p><b>Developer & Artist:</b> IceySquidFox<p> <p><b>Special Thanks:</b> to my teacher, friends, and family for all the help and support</p> <p>Server Craftus by IceySquidFox Copyright ©2022</p>"

        binding.aboutAppTextView.text = HtmlCompat.fromHtml(bugReportGuide, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return false
    }
}