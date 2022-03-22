package com.lh1153866.mcserverstatus.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.text.HtmlCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.lh1153866.mcserverstatus.R
import com.lh1153866.mcserverstatus.databinding.ActivityHelpBinding

class HelpActivity : AppCompatActivity() {

    lateinit var toggle : ActionBarDrawerToggle
    private lateinit var binding : ActivityHelpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* **************************************************************************************************************
                                                        Navigation Drawer
            Thanks to FoxAndroid for explaining how navigation drawers work (https://www.youtube.com/watch?v=zQh-QGGKPw0)
           ************************************************************************************************************** */
        val drawerLayout : DrawerLayout = binding.helpDrawer // container for the drawer and main content
        val navView : NavigationView = binding.helpNavView // navigation menu in the activity

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
                                                     App Help/ Tips
           ************************************************************************************************************** */
        val startingTips = "<p>To use the app, first you need to go to the Server Status home page. From there you chose which version of Minecraft that the server is on, either \"Java\" for Minecraft java edition, or \"Bedrock\" for any other version of Minecraft. Once you have chose a version, simply put in the server's ip (and port if you are looking up a bedrock server) and press \"Go\" to find the status.</p> <p>If you would like to save a server to look up the status later, please create an account and click \"Add to My Servers\". Once you do this, you can go to \"My Servers\" when you are logged in and you will see any servers that you have saved</p>"
        val keepInMind = "<p>These are a few things to be mindful of when using the app. Please do not contact the developer about these things, as they are already working on fixes for them, or are minor enough to be ignored.</p> <ul><li>A stable internet or wifi connection is required to use this app</li><li>Information will not load immediately and will take a few seconds to load</li><li>Server lookup information will only change once every <b>10 minutes</b> (this is so that the app and server can keep up with all the other people who also want to check server statuses)</li><li>Currently the bug report is in beta and is not going to be linked until it has been finished, please hang tight until it is done!</li></ul>"

        binding.startingTipsTextView.text = HtmlCompat.fromHtml(startingTips, HtmlCompat.FROM_HTML_MODE_LEGACY)
        binding.keepInMindTextView.text = HtmlCompat.fromHtml(keepInMind, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return false
    }
}