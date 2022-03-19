package com.lh1153866.mcserverstatus.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.lh1153866.mcserverstatus.R
import com.lh1153866.mcserverstatus.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var toggle : ActionBarDrawerToggle
    private lateinit var binding : ActivityMainBinding
    var ip : String = ""
    var port : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* **************************************************************************************************************
                                                        Navigation Drawer
            Thanks to FoxAndroid for explaining how navigation drawers work (https://www.youtube.com/watch?v=zQh-QGGKPw0)
           ************************************************************************************************************** */
        val drawerLayout : DrawerLayout = binding.mainActivityDrawer // container for the drawer and main content
        val navView : NavigationView = binding.mainNavView // navigation menu in the activity

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
                                                 Radio Buttons/ Server Status Button
           ************************************************************************************************************** */
        binding.javaRadioButton.setOnClickListener{
            binding.serverPortEditText.visibility = View.GONE // don't allow user to enter port when java is selected
        }

        binding.bedrockRadioButton.setOnClickListener{
            binding.serverPortEditText.visibility = View.VISIBLE // allow user to enter a port when bedrock is selected
        }

        binding.findStatusButton.setOnClickListener{
            ip =  binding.serverIpEditText.text.toString().trim()
            port = binding.serverPortEditText.text.toString().trim()

//            Toast.makeText(this, "clicked status button", Toast.LENGTH_LONG).show()
            var intent = Intent(this, ServerStatusActivity::class.java)
//
            if (binding.javaRadioButton.isChecked) { // java edition servers only need ip
//                Toast.makeText(this, "java", Toast.LENGTH_LONG).show()
                if (ip.isNotEmpty()) {
                    intent.putExtra("edition", "Java") // pass the "java" edition to the server status screen
                    intent.putExtra("ip", ip) // pass the entered ip to the server status screen

                    startActivity(intent) // open server status activity
                }
                else {
                    Toast.makeText(this, "ip is required", Toast.LENGTH_LONG).show()
                }
            }
            else if (binding.bedrockRadioButton.isChecked) { // bedrock edition servers need ip and port
//                Toast.makeText(this, "bedrock", Toast.LENGTH_LONG).show()
                if (ip.isNotEmpty() && port.isNotEmpty()) {
                    intent.putExtra("edition", "Bedrock") // pass the "bedrock" edition to the server status screen
                    intent.putExtra("ip", ip) // pass the entered ip to the server status screen
                    intent.putExtra("port", port) // pass the entered port to the server status screen

                    startActivity(intent) // open server status activity
                }
                else { // show error message if ip and port are not entered
                    Toast.makeText(this, "ip and port are required", Toast.LENGTH_LONG).show()
                }
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