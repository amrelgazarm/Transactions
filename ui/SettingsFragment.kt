package com.qceccenter.qcec.ui

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.qceccenter.qcec.R
import com.qceccenter.qcec.databinding.FragmentSettingsBinding
import com.qceccenter.qcec.models.Profile
import com.qceccenter.qcec.models.UserInfo
import com.qceccenter.qcec.utils.LocaleHelper
import com.qceccenter.qcec.viewmodels.ProfileViewModel
import com.squareup.picasso.Picasso

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment(), HomePagerActivity.UpdateSettingsFragmentCallback {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mViewModel: ProfileViewModel;
    private lateinit var USER_ACCESS_TOKEN: String;
    private lateinit var binding: FragmentSettingsBinding;
    private lateinit var userInfo: UserInfo;

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
        var rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        binding = FragmentSettingsBinding.bind(rootView);

        binding.logOutCardview.setOnClickListener {
            startActivity(Intent(activity, LoginActivity::class.java));
            activity!!.finish();
        }

        binding.aboutCompanyCardview.setOnClickListener {
            startActivity(Intent(activity, AboutCompanyActivity::class.java));
        }

        binding.changeLanguageCardview.setOnClickListener {
            createChangeLanguageDialog();
        }

        binding.contactUsCardview.setOnClickListener(View.OnClickListener {
            startActivity(Intent(activity, ContactUsActivity::class.java));
        })

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
                binding.progressBar6.visibility = View.GONE;
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
            ?.setText(R.string.fragment_settings_title)
    }

    private fun createChangeLanguageDialog() {
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_add_attachments)
        dialog.findViewById<TextView>(R.id.dialog_title)
            .setText(activity!!.getString(R.string.change_language_title));
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val dialogArr = activity!!.resources.getStringArray(R.array.language_list);
        val dialogArrValues = activity!!.resources.getStringArray(R.array.language_list_values);
        val listView = dialog.findViewById<ListView>(R.id.dialog_listView)
        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(
            activity!!,
            R.layout.attachment_dialog_list_item,
            R.id.attachment_dialog_tv,
            dialogArr
        )
        listView.adapter = arrayAdapter
        listView.onItemClickListener =
            OnItemClickListener { adapterView, view, i, l ->
                val key = getString(R.string.pref_app_lang_key)
                val mSPreferences: SharedPreferences = activity!!.getSharedPreferences(
                    key,
                    AppCompatActivity.MODE_PRIVATE
                )
                val prefEditor = mSPreferences.edit()
                prefEditor.putString(
                    key,
                    dialogArrValues[i]
                )
                prefEditor.apply()

                LocaleHelper.setLocale(activity, dialogArrValues[i]);
                dialog.dismiss()
                startActivity(Intent(activity, HomePagerActivity::class.java));
                activity!!.finish();
            }
        dialog.show()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun updateViews() {
        binding.fSettingsUsername.setText(this.userInfo.name);
        binding.fSettingsSubscription.setText(this.userInfo.userTypeName);
        Picasso.with(activity).load(this.userInfo.profileImage).fit()
            .into(binding.fSettingsProfileImgView);
    }

    override fun updateSettingsFrag(userInfo: UserInfo) {
        this.userInfo = userInfo;
        updateViews()
    }
}