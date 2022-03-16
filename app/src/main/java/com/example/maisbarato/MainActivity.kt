package com.example.maisbarato

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.maisbarato.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private lateinit var destinationListener: NavController.OnDestinationChangedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.myToolbar.setupWithNavController(navController)
//        setupActionBarWithNavController(navController)

        destinationListener =
            NavController.OnDestinationChangedListener { _, destination, arguments ->

                binding.myToolbar.visibility = if (destination.id == R.id.loginFragment || destination.id == R.id.cadastroFragment) {
                    View.GONE
                } else {
                    View.VISIBLE
                }

                if (destination.id == R.id.listaOfertasFragment) {
                    binding.myToolbar.navigationIcon = null
                }

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