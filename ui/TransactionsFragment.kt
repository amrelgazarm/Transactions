package com.qceccenter.qcec.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.qceccenter.qcec.R
import com.qceccenter.qcec.databinding.FragmentTransactionsBinding
import com.qceccenter.qcec.models.TData
import com.qceccenter.qcec.models.Transaction
import com.qceccenter.qcec.models.Transactions
import com.qceccenter.qcec.ui.adpaters.TransactionsRVAdapter
import com.qceccenter.qcec.viewmodels.HomePagerViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [TransactionsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TransactionsFragment : Fragment(), TransactionsRVAdapter.TransactionCardClickListener,
    HomePagerActivity.UpdateTransactionsFragmentCallback {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var mTransactionsViewModel: HomePagerViewModel;
    private lateinit var USER_ACCESS_TOKEN: String;


    private lateinit var binding: FragmentTransactionsBinding;

    //    private lateinit var mTransactionListAdapter: TransactionsRVAdapter;
    private lateinit var tData: TData;
    private lateinit var mTransactionListAdapter: TransactionsRVAdapter;


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
        var rootView = inflater.inflate(R.layout.fragment_transactions, container, false)
        binding = FragmentTransactionsBinding.bind(rootView)
        return rootView
    }

    override fun onStart() {
        super.onStart()
        var bundle = this.arguments
        if (bundle != null) {
            this.tData =
                (bundle.getParcelable<TData>(getString(R.string.transactionsList_argument_key))) as TData;
        } else {
            this.tData = TData();
            tData.transactionsList = ArrayList<Transaction>();
        }
        this.mTransactionListAdapter =
            TransactionsRVAdapter(activity, tData.transactionsList, this);
        var layoutManager: LinearLayoutManager = GridLayoutManager(activity, 1);
        binding.fTranscationsRV.layoutManager = layoutManager;
        binding.fTranscationsRV.setHasFixedSize(true);
        binding.fTranscationsRV.adapter = mTransactionListAdapter;

        mTransactionsViewModel = ViewModelProvider(this).get(HomePagerViewModel::class.java);
        mTransactionsViewModel.init();

        val pref = activity!!.getSharedPreferences(
            getString(R.string.auth_shared_preference),
            AppCompatActivity.MODE_PRIVATE
        )

        USER_ACCESS_TOKEN =
            pref.getString(getString(R.string.auth_shared_preference_authToken), "NO Token")!!;
    }

    override fun onResume() {
        super.onResume()
//        LocaleHelper.updateLocaleFromContext(activity);
        (activity as AppCompatActivity).supportActionBar?.customView?.findViewById<TextView>(R.id.actionBar_tv)
            ?.setText(R.string.fragment_transactions_title)

        mTransactionsViewModel.getAllTransactions(USER_ACCESS_TOKEN, activity)
            .observe(activity as AppCompatActivity, Observer<Transactions> { transactions ->
                updateTransactionsFrag(transactions.transactionsData.transactions)
                showHideMsg();
            })
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProjectsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TransactionsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onTransactionItemClicked(itemPosition: Int) {
        val intent = Intent(activity, TransactionDetailsActivity::class.java);
        intent.putExtra(
            getString(R.string.transaction_ID_intent_key),
            this.tData.transactionsList.get(itemPosition).transactionID
        );
        startActivity(intent);
    }

    override fun updateTransactionsFrag(transData: TData) {
        this.tData = transData;
        mTransactionListAdapter.setTransactionsList(tData.transactionsList);
        showHideMsg();
        this.mTransactionListAdapter.notifyDataSetChanged();
        binding.fTransactionsTransactionsCountTv.setText(getString(R.string.fragment_transactions_count) + this.tData.transactionsList.size.toString());
        binding.transactionsProgressBar.visibility = View.GONE;
    }

    private fun showHideMsg() {
        if (tData.transactionsList.size <= 0) {
            binding.noTransactionTV.visibility = View.VISIBLE;
        } else {
            binding.noTransactionTV.visibility = View.GONE;
        }
    }
}