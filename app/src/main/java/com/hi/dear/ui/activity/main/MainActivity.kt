package com.hi.dear.ui.activity.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.hi.dear.R
import com.hi.dear.data.model.common.UserCore
import com.hi.dear.databinding.ActivityMainBinding
import com.hi.dear.repo.BrowseRepository
import com.hi.dear.ui.Constant.CurrentCoin
import com.hi.dear.ui.Constant.GiftCoint
import com.hi.dear.ui.Constant.boostProfileFragmentTitle
import com.hi.dear.ui.Constant.browseFragmentTitle
import com.hi.dear.ui.Constant.giftFragmentTitle
import com.hi.dear.ui.Constant.matchFragmentTitle
import com.hi.dear.ui.Constant.settingFragmentTitle
import com.hi.dear.ui.Constant.tipsFragmentTitle
import com.hi.dear.ui.Constant.topProfileFragmentTitle
import com.hi.dear.ui.DialogFactory
import com.hi.dear.ui.PrefsManager
import com.hi.dear.ui.activity.ViewModelFactory
import com.hi.dear.ui.activity.message.MessageActivity
import com.hi.dear.ui.activity.profile.ProfileActivity
import com.hi.dear.ui.base.BaseActivity
import com.hi.dear.ui.fragment.browse.BrowseViewModel
import timber.log.Timber


class MainActivity : BaseActivity<ActivityMainBinding, BrowseViewModel>(),
    NavigationRVAdapter.ClickListener {
    private var exitConfirm = false
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navAdapter: NavigationRVAdapter
    private var mRewardedAd: RewardedAd? = null
    private val exitDialogListener = object : DialogFactory.ITwoBtnListener {
        override fun onPositiveBtnClicked() {
            exitConfirm = true
            onBackPressed()
        }

        override fun onNegativeBtnClicked() {

        }
    }
    private var items = arrayListOf(
        NavigationItemModel(R.drawable.ic_browse, browseFragmentTitle),
        NavigationItemModel(R.drawable.ic_heart_2, matchFragmentTitle),
        NavigationItemModel(R.drawable.ic_star, tipsFragmentTitle),
        NavigationItemModel(R.drawable.ic_boost, boostProfileFragmentTitle),
        NavigationItemModel(R.drawable.ic_top, topProfileFragmentTitle),
        NavigationItemModel(R.drawable.ic_gift, giftFragmentTitle),
        NavigationItemModel(R.drawable.ic_settings, settingFragmentTitle),
    )

    private val navController by lazy {
        Navigation.findNavController(this, R.id.nav_host_fragment)
    }

    private val noAdListener = object : DialogFactory.ISingleBtnListener {
        override fun onPositiveBtnClicked() {

        }
    }

    private val rewardListener = object : DialogFactory.ISingleBtnListener {
        override fun onPositiveBtnClicked() {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initRewardAd()
    }

    private fun addRewardAdCallBack() {
        mRewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdShowedFullScreenContent() {
                initRewardAd()
                // Called when ad is shown.
                Timber.d("Ad was shown.")
                viewModel?.adButtonVisibility?.value = false
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                // Called when ad fails to show.
                Timber.d("Ad failed to show.")
            }

            override fun onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                Timber.d("Ad was dismissed.")
                mRewardedAd = null
            }
        }
    }

    fun showRewardAd() {
        if (mRewardedAd != null) {
            mRewardedAd?.show(this) {
                initRewardAd()
                viewModel?.adButtonVisibility?.value = false

                viewModel?.giftCoin(
                    GiftCoint,
                    PrefsManager.getInstance().readString(PrefsManager.UserId)!!
                )
            }
        } else {
            DialogFactory.makeDialog(
                getString(R.string.no_ad_available),
                noAdListener,
                R.drawable.ic_empty_box
            )
                .showDialog(supportFragmentManager)
        }
    }

    private fun initRewardAd() {
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(
            this,
            getString(R.string.reward_add_test_id),
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Timber.d(adError.message)
                    mRewardedAd = null
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    Timber.d("Ad was loaded.")
                    mRewardedAd = rewardedAd
                    viewModel?.adButtonVisibility?.value = true
                }
            })
        addRewardAdCallBack()
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
        when {
            drawerLayout.isDrawerOpen(GravityCompat.START) -> {
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            exitConfirm -> {
                exitConfirm = false
                super.onBackPressed()
            }
            else -> {
                DialogFactory.makeDialog(getString(R.string.exit_confirm_msg), exitDialogListener)
                    .showDialog(supportFragmentManager)
            }
        }
    }

    override fun onClick(view: View, position: Int) {
        when (position) {
            0 -> {
                binding.toolbarLayout.toolbarTitle.text = browseFragmentTitle
                navController.navigate(R.id.browse_fragment)
            }
            1 -> {

                binding.toolbarLayout.toolbarTitle.text = matchFragmentTitle
                navController.navigate(R.id.match_fragment)
            }
            2 -> {
                binding.toolbarLayout.toolbarTitle.text = tipsFragmentTitle
                navController.navigate(R.id.tips_fragment)
            }
            3 -> {
                binding.toolbarLayout.toolbarTitle.text = boostProfileFragmentTitle
                navController.navigate(R.id.boost_profile_fragment)
            }
            4 -> {
                binding.toolbarLayout.toolbarTitle.text = topProfileFragmentTitle
                navController.navigate(R.id.top_profile_fragment)
            }
            5 -> {
                binding.toolbarLayout.toolbarTitle.text = giftFragmentTitle
                navController.navigate(R.id.gift_fragment)
            }
            6 -> {
                binding.toolbarLayout.toolbarTitle.text = settingFragmentTitle
                navController.navigate(R.id.setting_fragment)
            }
        }
        toggleDrawer()
        navAdapter.highlight(position)
        Handler(Looper.myLooper()!!).postDelayed({
            drawerLayout.closeDrawer(GravityCompat.START)
        }, 200)
    }

    private fun setFirstItem() {
        binding.toolbarLayout.toolbarTitle.text = browseFragmentTitle
        navAdapter.highlight(0)
    }

    override fun initViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initViewModel(): BrowseViewModel {
        return ViewModelProvider(
            this, ViewModelFactory(BrowseRepository())
        ).get(BrowseViewModel::class.java)
    }

    override fun initView() {
        viewModel?.getRemainingCoin(PrefsManager.getInstance().readString(PrefsManager.UserId)!!)
        drawerLayout = binding.drawerLayout
        NavigationUI.setupWithNavController(binding.toolbarLayout.toolbar, navController)
        setSupportActionBar(binding.toolbarLayout.toolbar)
        initAdapter()
        setFirstItem()

        binding.toolbarLayout.messageBtn.setOnClickListener {
            startActivity(Intent(this, MessageActivity::class.java))
        }

        binding.toolbarLayout.toggleBtn.setOnClickListener {
            toggleDrawer()
        }
        binding.toolbarLayout.toolbar.navigationIcon = null

        val myData = getMyUserData()
        Glide
            .with(this)
            .load(myData.picture)
            .into(binding.profileImage)

        binding.name.text = myData.name
        binding.editBtn.setOnClickListener {
            ProfileActivity.start(this, myData,ProfileActivity.Mode.EDIT)
        }
    }

    private fun getMyUserData(): UserCore {
        var userData = UserCore()
        userData.picture = PrefsManager.getInstance().readString(PrefsManager.Pic)!!
        userData.id = PrefsManager.getInstance().readString(PrefsManager.UserId)!!
        userData.name = PrefsManager.getInstance().readString(PrefsManager.userName)!!
        userData.gender = PrefsManager.getInstance().readString(PrefsManager.Gender)!!
        return userData
    }

    private fun toggleDrawer() {
        if (binding.drawerLayout.isOpen) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        closeKeyboard()
        binding.toolbarLayout.toolbar.navigationIcon = null
    }

    override fun attachObserver(viewModel: BrowseViewModel?) {
        viewModel?.remainingCoinDataResult?.observe(this, Observer {
            val result = it ?: return@Observer
            if (result.success) {
                binding.remaningCoins.text = getString(R.string.remaining_coin, CurrentCoin)
                setRemainingCoin(it.data!!)
            } else {
                showToast(result.msg)
            }
        })

        viewModel?.remainingCoin?.observe(this, Observer {
            binding.remaningCoins.text = getString(R.string.remaining_coin, it)
        })

        viewModel?.giftCoinResult?.observe(this, Observer {
            val result = it ?: return@Observer
            if (result.success) {
                setRemainingCoin(it.data!!)
                DialogFactory.makeDialog(
                    getString(R.string.coin_reward_msg, it.data),
                    rewardListener,
                    R.drawable.img_gift_box
                )
                    .showDialog(supportFragmentManager)
            } else {
                showToast(it.msg)
            }
        })
    }

    fun setRemainingCoin(coin: Int) {
        CurrentCoin = coin
        viewModel?.remainingCoin?.value = coin
    }

    override fun initLoadingView(isLoading: Boolean) {

    }
}