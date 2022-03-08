package com.hi.dear.ui.fragment.browse

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.hi.dear.R
import com.hi.dear.data.model.common.UserCore
import com.hi.dear.databinding.FragmentBrowseBinding
import com.hi.dear.repo.BrowseRepository
import com.hi.dear.ui.Constant
import com.hi.dear.ui.PrefsManager
import com.hi.dear.ui.Utils
import com.hi.dear.ui.activity.ViewModelFactory
import com.hi.dear.ui.base.BaseFragment
import com.yuyakaido.android.cardstackview.*
import timber.log.Timber


class BrowseFragment : BaseFragment<FragmentBrowseBinding, BrowseViewModel>(), CardStackListener {
    private val adCounterTarget = 5
    private var swipeCounter = 0
    private var visibleUserData: UserCore? = null
    private val mAdapter by lazy { SwipeStackAdapter() }
    private val manager by lazy { CardStackLayoutManager(requireContext(), this) }
    private var mInterstitialAd: InterstitialAd? = null

    override fun initViewBinding(inflater: LayoutInflater): FragmentBrowseBinding {
        return FragmentBrowseBinding.inflate(inflater)
    }

    override fun initViewModel(): BrowseViewModel? {
        return ViewModelProvider(
            requireActivity(), ViewModelFactory(BrowseRepository())
        ).get(BrowseViewModel::class.java)
    }

    override fun initView() {
        initAd()
        viewModel?.getBrowseData(getPreferredGender(), 5)
        manager.setStackFrom(StackFrom.BottomAndRight)
        manager.setSwipeableMethod(SwipeableMethod.Automatic)
        binding.swipeStack.adapter = mAdapter
        binding.swipeStack.layoutManager = manager
        binding.heartBtn.setOnClickListener {
            if (visibleUserData != null) {
                viewModel?.sendRequest(visibleUserData!!)
            }

        }
        binding.crossBtn.setOnClickListener {
            binding.swipeStack.swipe()
        }
    }

    private fun initAd() {
        MobileAds.initialize(
            requireContext()
        ) {
            val adRequest = AdRequest.Builder().build()
            createintersitial(adRequest)
        }
    }

    private fun createintersitial(adRequest: AdRequest) {
        InterstitialAd.load(
            requireContext(), getString(R.string.test_Interstitial_ad_id),
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    mInterstitialAd = interstitialAd
                    Timber.e("onAdLoaded")
                    mInterstitialAd!!.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                Timber.d("The ad was dismissed.")
                                initAd()
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                // Called when fullscreen content failed to show.
                                Timber.d("The ad failed to show.")
                            }

                            override fun onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                mInterstitialAd = null
                                Timber.d("The ad was shown.")
                            }
                        }
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // Handle the error
                    Timber.e(loadAdError.message)
                    mInterstitialAd = null
                }
            })
    }

    private fun getPreferredGender(): String {
        var gender = PrefsManager.getInstance().readString(PrefsManager.Gender)
        return if (gender == null || gender == Constant.male) {
            Constant.female
        } else {
            Constant.male
        }
    }

    override fun attachObserver(viewModel: BrowseViewModel?) {
        viewModel?.browseDataResult?.observe(this@BrowseFragment, Observer {
            val browseResult = it ?: return@Observer
            if (browseResult.success) {
                mAdapter.addItem(it.data!!)
                mAdapter.notifyDataSetChanged()
                adjustView()
            } else {
                showToast(browseResult.msg)
            }
        })

        viewModel?.requestDataResult?.observe(this@BrowseFragment, Observer {
            val result = it ?: return@Observer
            if (result.success) {
                binding.swipeStack.swipe()
            }
            showToast(result.msg)
            requireActivity().setResult(Activity.RESULT_OK)
        })

        viewModel?.remainingCoin?.observe(this@BrowseFragment, Observer {
            binding.remaningCoins.text=getString(R.string.remaining_coin,it)
        })


    }

    private fun adjustView() {
        if (mAdapter.itemCount == 0) {
            binding.contentGroup.visibility = View.GONE
            binding.emptyText.visibility = View.VISIBLE
        }
    }

    override fun initLoadingView(isLoading: Boolean) {
        if (isLoading) {
            Utils.disableView(binding.heartBtn)
            Utils.disableView(binding.crossBtn)
        } else {
            Utils.enableView(binding.heartBtn)
            Utils.enableView(binding.crossBtn)
        }
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {

    }

    override fun onCardSwiped(direction: Direction?) {

    }

    override fun onCardRewound() {

    }

    override fun onCardCanceled() {

    }

    override fun onCardAppeared(view: View?, position: Int) {
        visibleUserData = mAdapter.getItemBy(position)
        binding.name.text = visibleUserData?.name
    }

    override fun onCardDisappeared(view: View?, position: Int) {
        swipeCounter++
        if (position == mAdapter.itemCount - 1) {
            binding.contentGroup.visibility = View.GONE
            binding.emptyText.visibility = View.VISIBLE
            showAd()
        } else {
            binding.contentGroup.visibility = View.VISIBLE
            binding.emptyText.visibility = View.GONE
        }
        if (swipeCounter >= adCounterTarget) {
            showAd()
        }
    }

    private fun showAd() {
        Timber.d("showing ad")
        if (mInterstitialAd != null) {
            mInterstitialAd!!.show(requireActivity())
            swipeCounter = 0
        } else {
            Timber.d("The interstitial ad wasn't ready yet.")
        }
    }
}