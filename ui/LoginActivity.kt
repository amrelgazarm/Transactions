package com.qceccenter.qcec.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.qceccenter.qcec.R
import com.qceccenter.qcec.R.string.pref_app_lang_key
import com.qceccenter.qcec.databinding.ActivityLoginBinding
import com.qceccenter.qcec.models.Authentication
import com.qceccenter.qcec.utils.JustStarted
import com.qceccenter.qcec.utils.LocaleHelper
import com.qceccenter.qcec.viewmodels.LogInViewModel

class LoginActivity : AppCompatActivity() {
    private val TAG: String = this::class.java.simpleName
    private lateinit var binding: ActivityLoginBinding
    private lateinit var mSPreferences: SharedPreferences;
    private lateinit var mLogInViewModel: LogInViewModel
    private val isTest: Boolean = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val rootView = binding.root
        setContentView(rootView)
        supportActionBar?.hide()
        mSPreferences = getSharedPreferences(
            getString(R.string.auth_shared_preference),
            MODE_PRIVATE
        );

        setLocale();

        mLogInViewModel = ViewModelProvider(this).get(LogInViewModel::class.java)
        mLogInViewModel.init()

        binding.activityLoginLoginBtn.setOnClickListener {
            binding.activityLoginUsernameTil.isErrorEnabled = false;
            binding.activityLoginPasswordTil.isErrorEnabled = false;
            if (isTest || ((binding.activityLoginUsernameEt.text.toString() != null && !binding.activityLoginUsernameEt.text.toString()
                    .equals("")) && (binding.activityLoginPasswordEt.text.toString() != null && !binding.activityLoginPasswordEt.text.toString()
                    .equals("")))
            ) {
                binding.progressBar.visibility = View.VISIBLE;
                binding.activityLoginLoginBtn.isEnabled = false;
                mLogInViewModel.logIn(
                    binding.activityLoginUsernameEt.text.toString(),
                    binding.activityLoginPasswordEt.text.toString(),
//                    "admin account",
//                    "admin",
//                    "998877",
//                    "11111111",
                    this
                ).observe(this, Observer<Authentication> { authentication ->
                    val prefEditor = mSPreferences.edit()
                    prefEditor.putString(
                        getString(R.string.auth_shared_preference_authToken),
                        getString(R.string.access_token_header) + " " + authentication.userData.accessToken
                    )
                    prefEditor.putBoolean(
                        getString(R.string.auth_shared_preference_is_eng),
                        authentication.userData.userInfo.isEng
                    );
                    if (authentication.userData.userInfo.userTypeID.equals("1")) {
                        prefEditor.putBoolean(
                            getString(R.string.auth_shared_preference_is_eng),
                            true
                        );
                    }
                    prefEditor.putString(
                        getString(R.string.auth_shared_preference_username),
                        binding.activityLoginUsernameEt.text.toString()
                    );
                    prefEditor.putString(
                        getString(R.string.auth_shared_preference_password),
                        binding.activityLoginPasswordEt.text.toString()
                    );
                    prefEditor.apply()

                    val intent = Intent(this, HomePagerActivity::class.java)
                    intent.putExtra(
                        getString(R.string.user_info_intent_key),
                        authentication.userData.userInfo
                    );
                    startActivity(intent)
                    binding.progressBar.visibility = View.GONE;
                    finish()
                })
            } else if ((binding.activityLoginUsernameEt.text.toString() == null || binding.activityLoginUsernameEt.text.toString()
                    .equals("")) && (binding.activityLoginPasswordEt.text.toString() == null || binding.activityLoginPasswordEt.text.toString()
                    .equals(""))
            ) {
                binding.activityLoginUsernameTil.error = getString(R.string.username_empty);
                binding.activityLoginPasswordTil.error = getString(R.string.password_empty);
                Toast.makeText(
                    this,
                    getString(R.string.username_password_empty_msg),
                    Toast.LENGTH_LONG
                ).show();
            } else if (binding.activityLoginPasswordEt.text.toString() == null || binding.activityLoginPasswordEt.text.toString()
                    .equals("")
            ) {
                binding.activityLoginPasswordTil.error = getString(R.string.password_empty);
                Toast.makeText(
                    this,
                    getString(R.string.username_password_empty_msg),
                    Toast.LENGTH_LONG
                ).show();
            } else {
                binding.activityLoginUsernameTil.error = getString(R.string.username_empty);
                Toast.makeText(
                    this,
                    getString(R.string.username_password_empty_msg),
                    Toast.LENGTH_LONG
                ).show();
            }
        }

        binding.activityLoginSignupBtn.setOnClickListener {
            startActivity(Intent(this, CreateAccountActivity::class.java));
        }

    }

    private fun setLocale() {
        if (JustStarted.isJustStarted) {
            val key = getString(pref_app_lang_key)
            val sharedPreferences: SharedPreferences = getSharedPreferences(key, MODE_PRIVATE);
            val lang = sharedPreferences.getString(key, getString(R.string.pref_app_lang_arb_value))
            LocaleHelper.setLocale(this, lang)
            JustStarted.isJustStarted = false
            startActivity(intent)
            finish()
        } else {
            if (!LocaleHelper.getLanguage(this).equals(getString(R.string.locale_check))) {
                LocaleHelper.updateLocaleFromContext(this);
                this.recreate();
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("LOG IN", "On Start");
    }

    override fun onResume() {
        super.onResume()
        this.binding.activityLoginLoginBtn.isEnabled = true
        var username : String? = mSPreferences.getString(getString(R.string.auth_shared_preference_username),"");
        var password : String? = mSPreferences.getString(getString(R.string.auth_shared_preference_password),"");
        if(username != null && password != null){
            binding.activityLoginUsernameEt.setText(username);
            binding.activityLoginPasswordEt.setText(password);
        }
        Log.d("LOG IN", "On Resume");
    }

    public fun getLogInBtnRef(): Button {
        return this.binding.activityLoginLoginBtn
    }

    public fun getProgressBarRef(): ProgressBar {
        return this.binding.progressBar;
    }
}