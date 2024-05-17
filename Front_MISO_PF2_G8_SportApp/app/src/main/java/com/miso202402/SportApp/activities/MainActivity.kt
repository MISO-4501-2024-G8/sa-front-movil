package com.miso202402.front_miso_pf2_g8_sportapp.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.miso202402.SportApp.src.utils.SharedPreferences
import com.miso202402.SportApp.src.utils.TransferInfo
import com.miso202402.front_miso_pf2_g8_sportapp.R
import com.miso202402.front_miso_pf2_g8_sportapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    TransferInfo {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var toolbar: MaterialToolbar
    private lateinit var fab: FloatingActionButton
    private lateinit var drawerLayout: DrawerLayout
    private var typePlan: String? = ""


    fun showToolbarAndFab() {
        toolbar.visibility = View.VISIBLE
        fab.visibility = View.VISIBLE
    }

    fun hideToolbarAndFab() {
        toolbar.visibility = View.GONE
        fab.visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        fab = findViewById<FloatingActionButton>(R.id.fab)
        toolbar.setTitle("SPORT APP")
        setSupportActionBar(toolbar)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_msg,
            R.string.close_msg
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        fab.setOnClickListener {

        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.calendar -> navigateToFragment(R.id.CalendarFragment, "Calendario", null, typePlan)
            R.id.plans -> navigateToFragment(
                R.id.trainingSessionFragment,
                "Plan de Entrenamiento",
                null,
                typePlan
            )
            R.id.events -> navigateToFragment(R.id.ListEventsFragment, "Eventos", null, typePlan)
            R.id.sport -> navigateToFragment(R.id.SportFragment, "Sesion Deportiva", null, typePlan)
            R.id.goals -> navigateToFragment(R.id.GoalFragment, "Perfil Deportivo", null, typePlan)
            R.id.chat -> navigateToFragment(R.id.ChatFragment, "Sesion y Chats", null, typePlan)
            R.id.nav_logout ->closeApp()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun navigateToFragment(
        fragmentId: Int,
        toolbarTitle: String? = "SPORT APP",
        bundle: Bundle? = null,
        typePlan: String?
    ) {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        val menu = findViewById<NavigationView>(R.id.nav_view).menu
        for (i in 0 until menu.size()) {
            menu.getItem(i).isChecked = false
        }
        toolbar.setTitle(toolbarTitle)
        /* if (bundle != null) {
             navController.navigate(fragmentId, bundle)
         } else {
             navController.navigate(fragmentId)
         }*/
        if (typePlan != "premium" && (fragmentId == R.id.ChatFragment)) {
            mostrarMensaje("Esta opción solo está disponible para el  plan premiun.")
        } else {
            navController.navigate(fragmentId)
        }

    }

    fun closeApp(){
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        navController.navigate(R.id.SecondFragment)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Log.i("MainActivity", "onBackPressed")
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            mostrarMensaje("Utiliza los botones de navegación en la aplicación.")
        }
    }

    private fun mostrarMensaje(mensaje: String) {
        findViewById<View>(android.R.id.content)?.let { contentView ->
            Snackbar.make(contentView, mensaje, Snackbar.LENGTH_SHORT).show()
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

    override fun transferInfo(ms: String) {
        typePlan = ms
    }


}