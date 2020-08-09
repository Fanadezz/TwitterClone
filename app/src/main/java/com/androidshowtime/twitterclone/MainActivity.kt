package com.androidshowtime.twitterclone

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.parse.ParseAnalytics

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //helps to see how much the user is using our app


        //helps to see how much the user is using our app
        ParseAnalytics.trackAppOpenedInBackground(intent)

        // use nav controller to add Up-Button to the app
        val navController = this.findNavController(R.id.nav_host_fragment)

        // link the navigation controller to the app bar
        NavigationUI.setupActionBarWithNavController(this, navController)


    }

    //override onSupportNavigateUp() to call navigateUp() in the navigation controller
    override fun onSupportNavigateUp(): Boolean {

        val navController = this.findNavController(R.id.nav_host_fragment)
        return navController.navigateUp()
    }
}