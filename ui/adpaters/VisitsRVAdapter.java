package com.qceccenter.qcec.ui.adpaters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qceccenter.qcec.R;
import com.qceccenter.qcec.models.Visit;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VisitsRVAdapter extends RecyclerView.Adapter<VisitsRVAdapter.VisitCardViewHolder> {

    private VisitItemClickListener mVisitItemClickListener;
    private List<Visit> mVisitsList;
    private Context mContext;


    public VisitsRVAdapter(VisitItemClickListener mVisitItemClickListener, List<Visit> mVisitsList, Context mContext) {
        this.mVisitItemClickListener = mVisitItemClickListener;
        this.mVisitsList = mVisitsList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public VisitCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int visitItemLayoutID = R.layout.visits_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(visitItemLayoutID, parent, shouldAttachToParentImmediately);
        VisitCardViewHolder viewHolder = new VisitCardViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VisitCardViewHolder holder, int position) {
        holder.visitDateTV.setText(mVisitsList.get(position).getVisitDate());
        holder.photoCountTV.setText(Integer.toString(mVisitsList.get(position).getAttachedImagesCount()) + " " + mContext.getString(R.string.visit_photo));
    }

    @Override
    public int getItemCount() {
        return mVisitsList.size();
    }

    public class VisitCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.visit_date_tv)
        TextView visitDateTV;
        @BindView(R.id.photos_count_tv)
        TextView photoCountTV;

        public VisitCardViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedItemPosition = getAdapterPosition();
            mVisitItemClickListener.onVisitItemClicked(clickedItemPosition);
        }
    }

    public interface VisitItemClickListener {
        public void onVisitItemClicked(int itemPosition);
    }
}
