package com.miso202402.front_miso_pf2_g8_sportapp.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CalendarView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.miso202402.SportApp.fragments.CalendarFragment
import com.miso202402.SportApp.fragments.ChatFragment
import com.miso202402.SportApp.fragments.EditEventsFragment
import com.miso202402.SportApp.fragments.GoalFragment
import com.miso202402.SportApp.fragments.ListEventsFragment
import com.miso202402.SportApp.fragments.SportFragment
import com.miso202402.SportApp.fragments.TrainingSessionFragment
import com.miso202402.front_miso_pf2_g8_sportapp.R
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.ActivityMainBinding
import com.miso202402.front_miso_pf2_g8_sportapp.fragments.LoginFragment
import com.miso202402.front_miso_pf2_g8_sportapp.src.models.request.LoginRequest
import com.miso202402.front_miso_pf2_g8_sportapp.src.models.response.LoginResponse
import com.miso202402.front_miso_pf2_g8_sportapp.src.services.ApiService
import com.miso202402.front_miso_pf2_g8_sportapp.src.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var toolbar: MaterialToolbar
    private lateinit var fab: FloatingActionButton
    private lateinit var drawerLayout: DrawerLayout


    fun showToolbarAndFab() {
        toolbar.visibility = View.VISIBLE
        fab.visibility = View.VISIBLE
    }

    fun hideToolbarAndFab() {
        toolbar.visibility = View.GONE
        fab.visibility = View.GONE
    }

    /*
    fun enableBack(){
        actionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
    }

    fun disableBack(){
        actionBar?.setDisplayHomeAsUpEnabled(false)
        toolbar.setNavigationIcon(null)
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //binding = ActivityMainBinding.inflate(layoutInflater)
        //setContentView(binding.root)
        //setSupportActionBar(binding.toolbar)
        drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        fab = findViewById<FloatingActionButton>(R.id.fab)
        setSupportActionBar(toolbar)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.open_msg,R.string.close_msg)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        //val navController = findNavController(R.id.nav_host_fragment_content_main)
        //appBarConfiguration = AppBarConfiguration(navController.graph)
        //setupActionBarWithNavController(navController, appBarConfiguration)
        fab.setOnClickListener {}

       /* if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_content_main, LoginFragment()).commit()
        }*/
    }

    /*
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.calendar -> supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_content_main, CalendarFragment()).commit()
            R.id.plans -> supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_content_main, TrainingSessionFragment()).commit()
            R.id.events -> supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_content_main, ListEventsFragment()).commit()
            R.id.sport -> supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_content_main, SportFragment()).commit()
            R.id.goals -> supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_content_main, GoalFragment()).commit()
            R.id.chat -> supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_content_main, ChatFragment()).commit()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }*/

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.calendar -> navigateToFragment(R.id.CalendarFragment)
            R.id.plans -> navigateToFragment(R.id.trainingSessionFragment)
            R.id.events -> navigateToFragment(R.id.ListEventsFragment)
            R.id.sport -> navigateToFragment(R.id.SportFragment)
            R.id.goals -> navigateToFragment(R.id.GoalFragment)
            R.id.chat -> navigateToFragment(R.id.ListProgramSessionsConsultationsFragment)
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun navigateToFragment(fragmentId: Int, bundle: Bundle? = null) {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        val menu = findViewById<NavigationView>(R.id.nav_view).menu
        for (i in 0 until menu.size()) {
            menu.getItem(i).isChecked = false
        }

        if (bundle != null) {
            navController.navigate(fragmentId, bundle)
        } else {
            navController.navigate(fragmentId)
        }
    }

    override fun onBackPressed(){
        super.onBackPressed()
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }


}