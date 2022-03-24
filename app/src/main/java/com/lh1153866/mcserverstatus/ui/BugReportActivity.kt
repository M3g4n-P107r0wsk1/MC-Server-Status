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
import com.lh1153866.mcserverstatus.databinding.ActivityBugReportBinding

class BugReportActivity : AppCompatActivity() {

    lateinit var toggle : ActionBarDrawerToggle
    private lateinit var binding : ActivityBugReportBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBugReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth // setup firebase auth

        /* **************************************************************************************************************
                                                        Navigation Drawer
            Thanks to FoxAndroid for explaining how navigation drawers work (https://www.youtube.com/watch?v=zQh-QGGKPw0)
           ************************************************************************************************************** */
        val drawerLayout : DrawerLayout = binding.bugReportDrawer // container for the drawer and main content
        val navView : NavigationView = binding.bugReportNavView // navigation menu in the activity

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
                                                Bug Reporting Guidelines/ Button
           ************************************************************************************************************** */
        val bugReportGuide = "Did you notice a bug with the app, but don't know how to effectively fill out a bug report? Here are some steps to follow if you are new to writing bug reports."
        val bugReportGuide2 = "<p><b>Title</b>: briefly specify the problem that you faced</p><p><b>Environment</b>: ask yourself the following question \"what kind of device am I using the app on, and what operating system version is the device running?\"</p><p><b>Description</b>: give a brief explanation of what the bug does or what is it affecting</p><p><b>Steps to reproduce a Bug</b>: explain the steps leading up to when you experienced the bug or how to make the bug happen again</p><p><b>Expected Result</b>: what you expected to happen when you followed the above steps</p><p><b>Actual Result</b>: what actually happened when you followed the above steps</p><p><b>Visual Proof of Bug</b>: if you have any screenshots, videos, or text related to the bug include them with your report</p>"

        binding.goodBugReportsTextView.text = HtmlCompat.fromHtml(bugReportGuide, HtmlCompat.FROM_HTML_MODE_LEGACY)
        binding.goodBugReportsTextView2.text = HtmlCompat.fromHtml(bugReportGuide2, HtmlCompat.FROM_HTML_MODE_LEGACY)

        binding.bugReportWebsiteButton.setOnClickListener{ // send user to bug tracker JIRA board (not implemented yet)
            Toast.makeText(this, "Bug Tracker Website currently not available (WIP)", Toast.LENGTH_LONG).show()
        }

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return false
    }
}