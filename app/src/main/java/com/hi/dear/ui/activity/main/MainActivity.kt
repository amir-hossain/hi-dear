package com.hi.dear.ui.activity.main

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.hi.dear.R
import com.hi.dear.databinding.ActivityMainBinding
import com.hi.dear.ui.Constant.browseFragmentTitle
import com.hi.dear.ui.Constant.matchFragmentTitle
import com.hi.dear.ui.Constant.messageFragmentTitle
import com.hi.dear.ui.Constant.settingFragmentTitle
import com.hi.dear.ui.Constant.tipsFragmentTitle
import com.hi.dear.ui.activity.chat.ChatActivity
import com.hi.dear.ui.base.BaseActivity


class MainActivity : BaseActivity<ActivityMainBinding, ViewModel>(),
    NavigationRVAdapter.ClickListener {
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navAdapter: NavigationRVAdapter

    private var items = arrayListOf(
        NavigationItemModel(R.drawable.ic_browse, browseFragmentTitle),
        NavigationItemModel(R.drawable.ic_message, messageFragmentTitle),
        NavigationItemModel(R.drawable.ic_heart_2, matchFragmentTitle),
        NavigationItemModel(R.drawable.ic_star, tipsFragmentTitle),
        NavigationItemModel(R.drawable.ic_settings, settingFragmentTitle),
    )

    private val navController by lazy {
        Navigation.findNavController(this, R.id.nav_host_fragment)
    }

    override fun onSupportNavigateUp(): Boolean {
        val topLevelDestinations = setOf(
            R.id.browse_fragment,
            R.id.setting_fragment
        )
        val appBarConfiguration = AppBarConfiguration.Builder(topLevelDestinations)
            .setDrawerLayout(drawerLayout)
            .build()
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    private fun closeKeyboard() {
        try {
            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        } catch (e: Exception) {
            e.stackTrace
        }
    }

    private fun initAdapter() {
        binding.navigationRv.layoutManager = LinearLayoutManager(this)
        binding.navigationRv.setHasFixedSize(true)
        navAdapter = NavigationRVAdapter(items, this)
        binding.navigationRv.adapter = navAdapter
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onClick(view: View, position: Int) {
        when (position) {
            0 -> {
                binding.toolbarLayout.toolbarTitle.text = browseFragmentTitle
                navController.navigate(R.id.browse_fragment)
            }
            1 -> {
                toggle.setHomeAsUpIndicator(R.drawable.ic_toggle)
                binding.toolbarLayout.toolbarTitle.text = messageFragmentTitle
                navController.navigate(R.id.message_fragment)
            }
            2 -> {

                binding.toolbarLayout.toolbarTitle.text = matchFragmentTitle
                navController.navigate(R.id.match_fragment)
            }
            3 -> {
                binding.toolbarLayout.toolbarTitle.text = tipsFragmentTitle
                navController.navigate(R.id.tips_fragment)
            }
            4 -> {
                binding.toolbarLayout.toolbarTitle.text = settingFragmentTitle
                navController.navigate(R.id.setting_fragment)
            }
        }
        toggle.setHomeAsUpIndicator(R.drawable.ic_toggle)
        navAdapter.highlight(position)
        Handler(Looper.myLooper()!!).postDelayed({
            drawerLayout.closeDrawer(GravityCompat.START)
        }, 200)
    }

    private fun setFirstItem() {
        binding.toolbarLayout.toolbarTitle.text = browseFragmentTitle
        navController.navigate(R.id.browse_fragment)
        navAdapter.highlight(0)
    }

    override fun initViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initViewModel(): ViewModel? {
        return null
    }

    override fun initView() {
        drawerLayout = binding.drawerLayout

        val config = AppBarConfiguration(
            setOf(
                R.id.browse_fragment,
                R.id.message_fragment,
                R.id.match_fragment,
                R.id.tips_fragment,
                R.id.setting_fragment
            ), drawerLayout
        )

        NavigationUI.setupWithNavController(binding.toolbarLayout.toolbar, navController, config)
        setSupportActionBar(binding.toolbarLayout.toolbar)
        initAdapter()
        setFirstItem()
        toggle = object : ActionBarDrawerToggle(
            this,
            drawerLayout,
            binding.toolbarLayout.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ) {
            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                closeKeyboard()
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                closeKeyboard()
            }
        }
        toggle.isDrawerIndicatorEnabled = false
        toggle.setHomeAsUpIndicator(R.drawable.ic_toggle)
        drawerLayout.addDrawerListener(toggle)

        toggle.setToolbarNavigationClickListener { drawerLayout.openDrawer(GravityCompat.START) }

        binding.toolbarLayout.messageBtn.setOnClickListener {
            startActivity(Intent(this, ChatActivity::class.java))
        }
    }

    override fun attachObserver(viewModel: ViewModel?) {

    }

    override fun initLoadingView(isLoading: Boolean) {

    }
}