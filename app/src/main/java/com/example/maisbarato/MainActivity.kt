package com.example.maisbarato

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.maisbarato.databinding.ActivityMainBinding
import com.example.maisbarato.util.telasComIconeMenuHamburguer
import com.example.maisbarato.util.telasSemMenuDrawer
import com.example.maisbarato.util.telasSemToolbar
import com.example.maisbarato.view.BottomSheetMaisInfo
import com.example.maisbarato.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by viewModels()

    private lateinit var navController: NavController
    private lateinit var appBarConfig: AppBarConfiguration
    private lateinit var destinationListener: NavController.OnDestinationChangedListener
    private lateinit var headerView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_host_fragment)
        appBarConfig = AppBarConfiguration(telasComIconeMenuHamburguer, binding.drawerLayout)
        headerView = binding.navigationView.getHeaderView(0)

        binding.navigationView.setupWithNavController(navController)
        binding.toolbar.setupWithNavController(navController, appBarConfig)

        navegacaoMenuDrawer()
        configDestinationListener()
        verificarUsuarioLogado()
        carregaUsuarioUID()
        clickListener()
        carregaImagemUsuario()

    }

    private fun carregaImagemUsuario() {
        lifecycleScope.launch {
            sharedViewModel.urlImagemUsuario.collect { urlImagem ->
                val imgUsuario = headerView.findViewById<ImageView>(R.id.imagem_usuario)

                if (urlImagem.isNullOrEmpty()) {
                    Glide.with(imgUsuario)
                        .load(R.drawable.profile_picture_default)
                        .into(imgUsuario)
                } else {
                    Glide.with(imgUsuario)
                        .load(urlImagem)
                        .into(imgUsuario)
                }
            }
        }
    }

    private fun clickListener() {
        headerView.setOnClickListener {
            val bottomSheetMaisInfo = BottomSheetMaisInfo()
            bottomSheetMaisInfo.show(supportFragmentManager, BottomSheetMaisInfo.TAG)
        }
    }

    private fun carregaUsuarioUID(dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        lifecycleScope.launch(dispatcher) {
            sharedViewModel.carregaDadosUsuario()
        }

        lifecycleScope.launch(dispatcher) {
            sharedViewModel.carregaFotoUsuario()
        }
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

        lifecycleScope.launch {
            sharedViewModel.dadosUsuario.collect { usuario ->
                val tvNomeUsuario = headerView.findViewById<TextView>(R.id.nome_usuario)
                tvNomeUsuario.text = usuario?.nome
            }
        }

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