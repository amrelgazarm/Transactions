package com.qceccenter.qcec.ui.adpaters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qceccenter.qcec.R;
import com.qceccenter.qcec.databinding.TransactionsListItemBinding;
import com.qceccenter.qcec.models.Transaction;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransactionsRVAdapter extends RecyclerView.Adapter<TransactionsRVAdapter.TransactionListItemViewHolder> {
    private TransactionCardClickListener mTransactionCardClickListener;
    private Context mContext;
    private List<Transaction> transactionsList;

    public TransactionsRVAdapter(Context mContext, List<Transaction> transactionsList, TransactionCardClickListener mTransactionCardClickListener) {
        this.mTransactionCardClickListener = mTransactionCardClickListener;
        this.mContext = mContext;
        this.transactionsList = transactionsList;
    }

    public void setTransactionsList(List<Transaction> transactionsList) {
        this.transactionsList = transactionsList;
    }

    @NonNull
    @Override
    public TransactionListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int gridItemLayoutID = R.layout.transactions_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(gridItemLayoutID, parent, shouldAttachToParentImmediately);
        TransactionListItemViewHolder viewHolder = new TransactionListItemViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionListItemViewHolder holder, int position) {
//        holder.projectTV.setText(transactionsList.get(position).getProjectName());
        holder.projectTV.setText(transactionsList.get(position).getOwnerName());
        holder.locationTV.setText(transactionsList.get(position).getLocation());
        holder.creationDateTV.setText(transactionsList.get(position).getCreationDate());
        holder.visitCountTV.setText(Integer.toString(transactionsList.get(position).getVisitsCount()));
        if (transactionsList.get(position).getVisitsCount() > 0) {
            holder.alertImageView.setVisibility(View.GONE);
            holder.notificationImageView.setImageDrawable(mContext.getDrawable(R.drawable.ic_status_visited));
            holder.notificationImageView.setBackgroundColor(mContext.getResources().getColor(R.color.yellow_design));
        } else {
            holder.alertImageView.setVisibility(View.VISIBLE);
            holder.notificationImageView.setImageDrawable(mContext.getDrawable(R.drawable.ic_status_new));
            holder.notificationImageView.setBackgroundColor(mContext.getResources().getColor(R.color.orange_design));
        }
    }

    @Override
    public int getItemCount() {
        return transactionsList.size();
    }


    public class TransactionListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.transactionsCard_project_tv)
        TextView projectTV;
        @BindView(R.id.textView6)
        TextView locationTV;
        @BindView(R.id.textView7)
        TextView creationDateTV;
        @BindView(R.id.transactionsCard_visitCount_tv)
        TextView visitCountTV;
        @BindView(R.id.transactionsCard_side_imgView)
        ImageView notificationImageView;
        @BindView(R.id.transactionsCard_newT_imgView)
        ImageView alertImageView;

        public TransactionListItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedItemPosition = getAdapterPosition();
            mTransactionCardClickListener.onTransactionItemClicked(clickedItemPosition);
        }
    }

    public interface TransactionCardClickListener {
        public void onTransactionItemClicked(int itemPosition);
    }
}
