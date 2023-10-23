package com.qceccenter.qcec.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qceccenter.qcec.R;
import com.qceccenter.qcec.databinding.ActivitySearchBinding;
import com.qceccenter.qcec.models.SearchResult;
import com.qceccenter.qcec.models.Transaction;
import com.qceccenter.qcec.ui.adpaters.TransactionsRVAdapter;
import com.qceccenter.qcec.utils.LocaleHelper;
import com.qceccenter.qcec.viewmodels.SearchViewModel;

import java.util.List;

public class SearchActivity extends AppCompatActivity implements TransactionsRVAdapter.TransactionCardClickListener {

    private ActivitySearchBinding binding;
    private SearchViewModel mSearchViewModel;
    private String USER_ACCESS_TOKEN;
    private String searchStr;
    private List<Transaction> mTransactionList;
    private TransactionsRVAdapter mRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        View rootView = binding.getRoot();
        setContentView(rootView);

        getSupportActionBar().hide();

        getUserAccessToken();

        mSearchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        mSearchViewModel.init();

        binding.searchEt.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        imm.showSoftInput(binding.searchEt, InputMethodManager.SHOW_IMPLICIT);

        binding.searchCancelImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchActivity.this.finish();
            }
        });

        binding.searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });

        binding.searchImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performSearch();
            }
        });
    }

    private void performSearch() {
        binding.searchProgressBar.setVisibility(View.VISIBLE);
        searchStr = binding.searchEt.getText().toString().equals(null) ? "" : binding.searchEt.getText().toString();
        mSearchViewModel.search(SearchActivity.this, USER_ACCESS_TOKEN, searchStr).observe(SearchActivity.this, new Observer<SearchResult>() {
            @Override
            public void onChanged(SearchResult searchResult) {
                mTransactionList = searchResult.getData().getTransactionList();
                initRV();
            }
        });
    }

    private void initRV() {
        mRVAdapter = new TransactionsRVAdapter(this, mTransactionList, this);
        LinearLayoutManager layoutManager = new GridLayoutManager(this, 1);
        binding.searchResultRv.setLayoutManager(layoutManager);
        binding.searchResultRv.setHasFixedSize(true);
        binding.searchResultRv.setAdapter(mRVAdapter);
    }

    private void getUserAccessToken() {
        SharedPreferences pref = getSharedPreferences(getString(R.string.auth_shared_preference), MODE_PRIVATE);
        USER_ACCESS_TOKEN = pref.getString(getString(R.string.auth_shared_preference_authToken), "NO Token");
    }

    public ProgressBar getProgressBar() {
        return binding.searchProgressBar;
    }

    @Override
    public void onTransactionItemClicked(int itemPosition) {
        Intent intent = new Intent(this, TransactionDetailsActivity.class);
        intent.putExtra(getString(R.string.transaction_ID_intent_key), mTransactionList.get(itemPosition).getTransactionID());
        startActivity(intent);
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