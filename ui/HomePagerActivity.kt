package com.qceccenter.qcec.ui

import android.annotation.SuppressLint
import android.app.ActionBar
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.qceccenter.qcec.R
import com.qceccenter.qcec.databinding.ActivityHomePagerBinding
import com.qceccenter.qcec.models.TData
import com.qceccenter.qcec.models.Transaction
import com.qceccenter.qcec.models.Transactions
import com.qceccenter.qcec.models.UserInfo
import com.qceccenter.qcec.ui.adpaters.HomePagerAdapter
import com.qceccenter.qcec.utils.LocaleHelper
import com.qceccenter.qcec.viewmodels.HomePagerViewModel
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.tabs.TabLayoutMediator

class HomePagerActivity : AppCompatActivity() {
    private val TAG: String = this::class.java.simpleName
    private lateinit var binding: ActivityHomePagerBinding
    private lateinit var mHomePagerViewModel: HomePagerViewModel
    private lateinit var mPagesFragmentsList: MutableList<Fragment>;
    private lateinit var mPagesAdapter: HomePagerAdapter;
    private var isEng = true;
    private var notificationsCount: Int = 0;
    private lateinit var badgeDrawable: BadgeDrawable;
    private var userInfo: UserInfo = UserInfo();
//    private lateinit var mTransactions: Transactions;

    private lateinit var mUpdateTransactionsFragCallBack: UpdateTransactionsFragmentCallback;
    private lateinit var mUpdateNotificationsFragmentCallback: UpdateNotificationsFragmentCallback;
    private lateinit var mUpdateUserProfileCallback: UpdateUserProfileCallback;
    private lateinit var mUpdateSettingsCallback: UpdateSettingsFragmentCallback;
    private lateinit var USER_ACCESS_TOKEN: String;

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePagerBinding.inflate(layoutInflater)
        val rootView = binding.root
        setContentView(rootView)

        var intent = intent;
        if (intent.hasExtra(getString(R.string.user_info_intent_key))) {
            this.userInfo = intent.getParcelableExtra(getString(R.string.user_info_intent_key))!!;
        }

        initActionBar();

        val pref = getSharedPreferences(getString(R.string.auth_shared_preference), MODE_PRIVATE)
        USER_ACCESS_TOKEN =
            pref.getString(getString(R.string.auth_shared_preference_authToken), "NO Token")!!;
        isEng = pref.getBoolean(getString(R.string.auth_shared_preference_is_eng), false);

        mHomePagerViewModel = ViewModelProvider(this).get(HomePagerViewModel::class.java);
        mHomePagerViewModel.init();
    }

    override fun onStart() {
        super.onStart()
//        LocaleHelper.updateLocaleFromContext(this)
        if(!LocaleHelper.getLanguage(this).equals(getString(R.string.locale_check))){
            LocaleHelper.updateLocaleFromContext(this);
            this.recreate();
        }
        mPagesFragmentsList = ArrayList();
        initPagesFragments();
    }

    @SuppressLint("WrongConstant")
    private fun initActionBar() {
        supportActionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        supportActionBar?.setCustomView(R.layout.actionbar_custom)

        supportActionBar?.customView?.findViewById<ImageButton>(R.id.back_imgBtn)!!
            .setOnClickListener(
                View.OnClickListener {
                    onBackPressed()
                })

        supportActionBar?.customView?.findViewById<ImageButton>(R.id.search_imgBtn)!!
            .setOnClickListener(
                View.OnClickListener {
                    startActivity(Intent(this, SearchActivity::class.java));
                })
    }

    fun initPagesFragments() {
        var bundle = Bundle();
        bundle.putParcelable(getString(R.string.user_info_intent_key), this.userInfo);

        if (isEng) {
            val profileFragment = ProfileFragment();
            profileFragment.arguments = bundle;
            mUpdateUserProfileCallback = profileFragment;
            this.mPagesFragmentsList.add(profileFragment);
        }

        val TransFragment = TransactionsFragment();
        mUpdateTransactionsFragCallBack = TransFragment;
        this.mPagesFragmentsList.add(TransFragment);

        val notificationsFragment = NotificationsFragment();
        mUpdateNotificationsFragmentCallback = notificationsFragment;
        this.mPagesFragmentsList.add(notificationsFragment);

        val settingsFragment = SettingsFragment();
        settingsFragment.arguments = bundle;
        mUpdateSettingsCallback = settingsFragment;
        this.mPagesFragmentsList.add(settingsFragment);

        initPages()
    }

    override fun onBackPressed() {
        if (isEng) {
            binding.activityHomePagerViewPager.currentItem = 1;
        } else {
            binding.activityHomePagerViewPager.currentItem = 0;
        }
    }

    fun initPages() {
        mPagesAdapter = HomePagerAdapter(this);
        mPagesAdapter.setFragList(this.mPagesFragmentsList);

        binding.activityHomePagerViewPager.offscreenPageLimit = 3;
        binding.activityHomePagerViewPager.adapter = mPagesAdapter;
        if (isEng) {
            binding.activityHomePagerViewPager.currentItem = 1;
        }
        TabLayoutMediator(
            binding.activityHomePagerTabLayout,
            binding.activityHomePagerViewPager
        ) { tab, position ->
            if (isEng) {
                when (position) {
                    0 -> tab.icon = getDrawable(R.drawable.ic_profile)
                    1 -> tab.icon = getDrawable(R.drawable.ic_archive)
                    2 -> {
                        tab.icon = getDrawable(R.drawable.ic_notifications)
                        badgeDrawable = tab.orCreateBadge
                        badgeDrawable.backgroundColor =
                            ContextCompat.getColor(this, R.color.orange_design)
                        badgeDrawable.setVisible(false)
                        badgeDrawable.number = notificationsCount
                    }
                    3 -> tab.icon = getDrawable(R.drawable.ic_settings)
                }
            } else {
                when (position) {
                    0 -> tab.icon = getDrawable(R.drawable.ic_archive)
                    1 -> {
                        tab.icon = getDrawable(R.drawable.ic_notifications)
                        badgeDrawable = tab.orCreateBadge
                        badgeDrawable.backgroundColor =
                            ContextCompat.getColor(this, R.color.orange_design)
                        badgeDrawable.setVisible(false)
                        badgeDrawable.number = notificationsCount
                    }
                    2 -> tab.icon = getDrawable(R.drawable.ic_settings)
                }
            }
        }.attach()

//        Handler().postDelayed({
//            getTransactions();
//        }, 1000);
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(getString(R.string.user_info_intent_key), this.userInfo)
    }

//    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
//        super.onRestoreInstanceState(savedInstanceState)
//        if (savedInstanceState != null) {
//            this.userInfo =
//                savedInstanceState.getParcelable<UserInfo>(getString(R.string.user_info_intent_key))!!
//        }
//    }

    private fun getNotificationCount() {
        mHomePagerViewModel.getNewTransactions(USER_ACCESS_TOKEN, this)
            .observe(this, Observer<Transactions> { transactions ->
                if (transactions.transactionsData.transactions.transactionsList.size > 0) {
                    badgeDrawable.isVisible = true
                    badgeDrawable.number = transactions.transactionsData.transactions.transactionsList.size
                }
            })
    }

    private fun getTransactions() {
        mHomePagerViewModel.getAllTransactions(USER_ACCESS_TOKEN, this)
            .observe(this, Observer<Transactions> { transactions ->
//                this.mTransactions = transactions;
//                this.mUpdateTransactionsFragCallBack.updateTransactionsFrag(transactions.transactionsData.transactions);

                var notificationsTransList: MutableList<Transaction> = ArrayList();
                for (i in 0..(transactions.transactionsData.transactions.transactionsList.size - 1)) {
                    if (transactions.transactionsData.transactions.transactionsList.get(i).visitsCount > 0) {
                        continue;
                    } else {
                        notificationsTransList.add(
                            transactions.transactionsData.transactions.transactionsList.get(i)
                        );
                    }
                }
//                if (notificationsTransList.size > 0) {
//                    badgeDrawable.isVisible = true
//                    badgeDrawable.number = notificationsTransList.size
//                }
//                this.mUpdateNotificationsFragmentCallback.updateNotificationsFrag(
//                    notificationsTransList
//                );
                if (isEng) {
                    this.mUpdateUserProfileCallback.updateUserProfile(
                        transactions.transactionsData.userInfo
                    );
                }

                this.mUpdateSettingsCallback.updateSettingsFrag(transactions.transactionsData.userInfo);
            });
    }

    public fun updateNotificationBadgeDrawable(transCount: Int) {
        if (transCount > 0) {
            badgeDrawable.isVisible = true;
            badgeDrawable.number = transCount;
        } else {
            badgeDrawable.isVisible = false;
        }
    }

    override fun onResume() {
        super.onResume()
        getNotificationCount();
//        getTransactions();
    }

    interface UpdateTransactionsFragmentCallback {
        fun updateTransactionsFrag(transData: TData);
    }

    interface UpdateNotificationsFragmentCallback {
        fun updateNotificationsFrag(notificationsList: List<Transaction>);
    }

    interface UpdateUserProfileCallback {
        fun updateUserProfile(userInfo: UserInfo);
    }

    interface UpdateSettingsFragmentCallback {
        fun updateSettingsFrag(userInfo: UserInfo)
    }
}