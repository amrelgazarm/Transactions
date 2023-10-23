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
import com.qceccenter.qcec.databinding.FragmentNotificationsBinding
import com.qceccenter.qcec.databinding.FragmentTransactionsBinding
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
 * Use the [NotificationsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotificationsFragment : Fragment(), HomePagerActivity.UpdateNotificationsFragmentCallback,
    TransactionsRVAdapter.TransactionCardClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mTransactionListAdapter: TransactionsRVAdapter;
    private lateinit var binding: FragmentNotificationsBinding;
    private lateinit var transactionsList: List<Transaction>;
    private lateinit var mTransactionsViewModel: HomePagerViewModel;
    private lateinit var USER_ACCESS_TOKEN: String;


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
        var rootView = inflater.inflate(R.layout.fragment_notifications, container, false);
        binding = FragmentNotificationsBinding.bind(rootView);

        mTransactionsViewModel = ViewModelProvider(this).get(HomePagerViewModel::class.java);
        mTransactionsViewModel.init();

        val pref = activity!!.getSharedPreferences(
            getString(R.string.auth_shared_preference),
            AppCompatActivity.MODE_PRIVATE
        )
        USER_ACCESS_TOKEN =
            pref.getString(getString(R.string.auth_shared_preference_authToken), "NO Token")!!;

        transactionsList = ArrayList();
        this.mTransactionListAdapter =
            TransactionsRVAdapter(activity, this.transactionsList, this);
        var layoutManager: LinearLayoutManager = GridLayoutManager(activity, 1);
        binding.fNotificationsRV.layoutManager = layoutManager;
        binding.fNotificationsRV.setHasFixedSize(true);
        binding.fNotificationsRV.adapter = mTransactionListAdapter;
        return rootView;
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.customView?.findViewById<TextView>(R.id.actionBar_tv)
            ?.setText(R.string.fragment_notifications_title)

        mTransactionsViewModel.getNewTransactions(USER_ACCESS_TOKEN, activity)
            .observe(activity as AppCompatActivity, Observer<Transactions> { transactions ->
                updateNotificationsFrag(transactions.transactionsData.transactions.transactionsList);
                (activity as HomePagerActivity).updateNotificationBadgeDrawable(transactions.transactionsData.transactions.transactionsList.size);
                showHideMsg();
            });
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NotificationsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NotificationsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun updateNotificationsFrag(notificationsList: List<Transaction>) {
        this.transactionsList = notificationsList;
        this.mTransactionListAdapter.setTransactionsList(notificationsList);
        showHideMsg();
        this.mTransactionListAdapter.notifyDataSetChanged();
        binding.fNotificationsTransactionsCountTv.setText(getString(R.string.fragment_transactions_count) + this.transactionsList.size.toString());
        binding.notificationsFragProgressBar.visibility = View.GONE;
    }

    override fun onTransactionItemClicked(itemPosition: Int) {
        val intent = Intent(activity, TransactionDetailsActivity::class.java);
        intent.putExtra(
            getString(R.string.transaction_ID_intent_key),
            this.transactionsList.get(itemPosition).transactionID
        );
        startActivity(intent);
    }

    private fun showHideMsg(){
        if (transactionsList.size <= 0) {
            binding.noNotificationsTv.visibility = View.VISIBLE;
        } else {
            binding.noNotificationsTv.visibility = View.GONE;
        }
    }
}