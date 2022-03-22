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
import com.lh1153866.mcserverstatus.databinding.ActivityBugReportBinding

class BugReportActivity : AppCompatActivity() {

    lateinit var toggle : ActionBarDrawerToggle
    private lateinit var binding : ActivityBugReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBugReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                                                     Bug Reporting Guidelines
           ************************************************************************************************************** */
        val bugReportGuide = "<a href=\"#\">Bug Report Website (WIP)</a> Here are some steps to follow if you are new to writing bug reports"
        val bugReportGuide2 = "<ul><li><b>Title</b>: briefly specify the problem that you faced</li><li><b>Environment</b>: ask yourself the following question \"what kind of device am I using the app on, and what operating system version is the device running?\"</li><li><b>Description</b>: give a brief explanation of what the bug does or what is it affecting</li><li><b>Steps to reproduce a Bug</b>: explain the steps leading up to when you experienced the bug or how to make the bug happen again</li><li><b>Expected Result</b>: what you expected to happen when you followed the above steps</li><li><b>Actual Result</b>: what actually happened when you followed the above steps</li><li><b>Visual Proof of Bug</b>: if you have any screenshots, videos, or text related to the bug include them with your report</li></ul>"

        binding.goodBugReportsTextView.text = HtmlCompat.fromHtml(bugReportGuide, HtmlCompat.FROM_HTML_MODE_LEGACY)
        binding.goodBugReportsTextView2.text = HtmlCompat.fromHtml(bugReportGuide2, HtmlCompat.FROM_HTML_MODE_LEGACY)

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return false
    }
}