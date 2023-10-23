package com.qceccenter.qcec.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.qceccenter.qcec.R;
import com.qceccenter.qcec.databinding.ActivityCreateAccountBinding;
import com.qceccenter.qcec.models.VisitSubmission;
import com.qceccenter.qcec.utils.LocaleHelper;
import com.qceccenter.qcec.viewmodels.CreateAccountViewModel;

public class CreateAccountActivity extends AppCompatActivity {
    private ActivityCreateAccountBinding binding;
    private CreateAccountViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateAccountBinding.inflate(getLayoutInflater());
        View rootView = binding.getRoot();
        setContentView(rootView);

        getSupportActionBar().hide();

        mViewModel = new ViewModelProvider(this).get(CreateAccountViewModel.class);
        mViewModel.init();

        binding.backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccountActivity.this.finish();
            }
        });

        binding.createNewAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    mViewModel.signUp(CreateAccountActivity.this
                            , binding.editTextTextUsername.getText().toString()
                            , binding.editTextTextPhone.getText().toString()
                            , binding.editTextUserID.getText().toString()
                            , binding.editTextTextPassword.getText().toString()).observe(CreateAccountActivity.this, new Observer<VisitSubmission>() {
                        @Override
                        public void onChanged(VisitSubmission visitSubmission) {
                            if (visitSubmission.getVisitSubmission().getSubmittedSuccessfully()) {
                                Toast.makeText(CreateAccountActivity.this, getString(R.string.user_submitted), Toast.LENGTH_LONG).show();
                                CreateAccountActivity.this.finish();
                            }
                        }
                    });
                }

            }
        });

    }

    private boolean validateFields() {
        boolean isOk = true;
        if (binding.editTextTextUsername.getText().toString().length() <= 0
                || binding.editTextTextPassword.getText().toString().length() <= 0
                || binding.editTextTextRetypePassword.getText().toString().length() <= 0
                || binding.editTextTextPhone.getText().toString().length() <= 0
                || binding.editTextUserID.getText().toString().length() <= 0) {
            isOk = false;
            Toast.makeText(this, getString(R.string.all_fields_msg), Toast.LENGTH_LONG).show();
        }
        if (!binding.editTextTextPassword.getText().toString().equals(binding.editTextTextRetypePassword.getText().toString())) {
            isOk = false;
            Toast.makeText(this, getString(R.string.password_match_msg), Toast.LENGTH_LONG).show();
        }
        return isOk;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!LocaleHelper.getLanguage(this).equals(getString(R.string.locale_check))){
            LocaleHelper.updateLocaleFromContext(this);
            this.recreate();
        }
    }
}