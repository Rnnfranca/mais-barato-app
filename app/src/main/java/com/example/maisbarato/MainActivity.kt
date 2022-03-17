package com.example.maisbarato

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.maisbarato.databinding.ActivityMainBinding
import com.example.maisbarato.util.telasComIconeMenuHamburguer
import com.example.maisbarato.util.telasSemToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private lateinit var appBarConfig: AppBarConfiguration
    private lateinit var destinationListener: NavController.OnDestinationChangedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_host_fragment)
        appBarConfig = AppBarConfiguration(telasComIconeMenuHamburguer, binding.drawerLayout)

        binding.navigationView.setupWithNavController(navController)
        binding.toolbar.setupWithNavController(navController, appBarConfig)

        configDestinationListener()
    }

    private fun configDestinationListener() {
        destinationListener =
            NavController.OnDestinationChangedListener { _, destination, arguments ->
                visibilidadeToolbar(destination.id)
            }
    }

    private fun visibilidadeToolbar(destinationId: Int) {
        binding.toolbar.visibility = if (destinationId in telasSemToolbar) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        navController.addOnDestinationChangedListener(destinationListener)
    }

    override fun onBackPressed() {
        val fragmentAtual = navController.currentDestination?.id
        if(fragmentAtual != R.id.listaOfertasFragment) {
            super.onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}