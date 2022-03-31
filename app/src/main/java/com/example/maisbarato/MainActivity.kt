package com.example.maisbarato

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.maisbarato.databinding.ActivityMainBinding
import com.example.maisbarato.util.telasComIconeMenuHamburguer
import com.example.maisbarato.util.telasSemMenuDrawer
import com.example.maisbarato.util.telasSemToolbar
import com.example.maisbarato.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by viewModels()

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

        navegacaoMenuDrawer()
        configDestinationListener()
        verificarUsuarioLogado()
    }

    private fun navegacaoMenuDrawer(){
        binding.navigationView.setNavigationItemSelectedListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)

            if (it.itemId == R.id.sair) {
                sharedViewModel.logout()
                navController.navigate(R.id.loginFragment)
            } else {
                navController.navigate(it.itemId)
            }
            true
        }
    }

    private fun verificarUsuarioLogado() {
        sharedViewModel.currentUser?.also {
            navController.navigate(R.id.listaOfertasFragment)
        }
    }

    private fun configDestinationListener() {
        destinationListener =
            NavController.OnDestinationChangedListener { _, destination, arguments ->
                visibilidadeToolbar(destination.id)
                desabilitaMenuDrawer(destination.id)

//                binding.toolbar.title = destination.id
            }
    }

    private fun visibilidadeToolbar(destinationId: Int) {
        binding.toolbar.visibility = if (destinationId in telasSemToolbar) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun desabilitaMenuDrawer(destinationId: Int) {
        if (destinationId in telasSemMenuDrawer) {
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        } else {
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        }
    }

    override fun onResume() {
        super.onResume()
        navController.addOnDestinationChangedListener(destinationListener)
    }

    override fun onBackPressed() {
        val fragmentAtual = navController.currentDestination?.id
        if(fragmentAtual != R.id.loginFragment && fragmentAtual != R.id.listaOfertasFragment) {
            super.onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}