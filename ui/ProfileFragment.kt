package com.qceccenter.qcec.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.qceccenter.qcec.R
import com.qceccenter.qcec.databinding.FragmentProfileBinding
import com.qceccenter.qcec.models.Profile
import com.qceccenter.qcec.models.UserInfo
import com.qceccenter.qcec.viewmodels.ProfileViewModel
import com.squareup.picasso.Picasso

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment(), HomePagerActivity.UpdateUserProfileCallback {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mViewModel: ProfileViewModel;
    private lateinit var USER_ACCESS_TOKEN: String;
    private lateinit var binding: FragmentProfileBinding;
    private lateinit var userInfo: UserInfo;
    private var mVisitsCount: Int = 0;
    private var mTransCount: Int = 0;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        binding = FragmentProfileBinding.bind(rootView);
        val pref = activity!!.getSharedPreferences(
            getString(R.string.auth_shared_preference),
            AppCompatActivity.MODE_PRIVATE
        )
        USER_ACCESS_TOKEN =
            pref.getString(getString(R.string.auth_shared_preference_authToken), "NO Token")!!;
        mViewModel = ViewModelProvider(activity!!).get(ProfileViewModel::class.java);
        mViewModel.init();
        return rootView;
    }

    override fun onStart() {
        super.onStart()
        mViewModel.getUserInfo(activity!!, USER_ACCESS_TOKEN)
            .observe(activity!!, Observer<Profile> { profile ->
                this.userInfo = profile.userProfile.userInfo;
                updateViews()
                binding.progressBar7.visibility = View.GONE;
            })
//        var bundle = arguments;
//        if (bundle != null) {
//            this.userInfo =
//                bundle.getParcelable<UserInfo>(getString(R.string.user_info_intent_key))!!;
//            updateViews();
//        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.customView?.findViewById<TextView>(R.id.actionBar_tv)
            ?.setText(R.string.fragment_profile_title)


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun updateViews() {
        binding.cardUserInfoUsernameTv.setText(this.userInfo.name);
        binding.profileCardSubuscriptionTv.setText(this.userInfo.userTypeName);
        binding.profileCardNationalIDTv.setText("ID: " + this.userInfo.nationalID);
        binding.fProfileNoOfVisitsTv.setText(this.userInfo.visitsCount.toString());
        binding.fProfileNoOfTransactionsTv.setText(this.userInfo.transactionsCount.toString());
        Picasso.with(activity).load(this.userInfo.profileImage).fit()
            .into(binding.fProfileUserImgView);
    }


    override fun updateUserProfile(
        userInfo: UserInfo
    ) {
        this.userInfo = userInfo;
        updateViews();
    }
}