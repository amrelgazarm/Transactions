package com.qceccenter.qcec.ui.adpaters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qceccenter.qcec.R;
import com.qceccenter.qcec.models.Image;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AttachmentsRVAdapter extends RecyclerView.Adapter<AttachmentsRVAdapter.PhotoCardViewHolder> {
    private AttachmentCardClickListener mAttachmentCardClickListener;
    private List<Image> mAttachmentList;
    private Context mContext;

    public AttachmentsRVAdapter(AttachmentCardClickListener mAttachmentCardClickListener, List<Image> mAttachmentList, Context mContext) {
        this.mAttachmentCardClickListener = mAttachmentCardClickListener;
        this.mAttachmentList = mAttachmentList;
        this.mContext = mContext;
    }

    public AttachmentsRVAdapter(List<Image> mAttachmentList, Context mContext) {
        this.mAttachmentList = mAttachmentList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public PhotoCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int attachmentViewLayoutID = R.layout.attachment_photo_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(attachmentViewLayoutID, parent, shouldAttachToParentImmediately);
        PhotoCardViewHolder viewHolder = new PhotoCardViewHolder(view);
        return viewHolder;
    }

    public void setAttachmentList(List<Image> attachmentList) {
        this.mAttachmentList = attachmentList;
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoCardViewHolder holder, int position) {
        holder.cancelImgBtn.setVisibility(View.GONE);
        holder.maxImgBtn.setVisibility(View.GONE);
        if (mAttachmentList.get(position).getImageURL() != null && !mAttachmentList.get(position).getImageURL().equals("")) {
            Picasso.with(mContext).load(mAttachmentList.get(position).getImageURL()).fit().into(holder.photoImgView);
        }
    }

    @Override
    public int getItemCount() {
        return mAttachmentList.size();
    }

    public class PhotoCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.photo_imageView)
        ImageView photoImgView;
        @BindView(R.id.cancel_imgBtn)
        ImageButton cancelImgBtn;
        @BindView(R.id.maximize_imgBtn)
        ImageButton maxImgBtn;

        public PhotoCardViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int itemPosition = getAdapterPosition();
            mAttachmentCardClickListener.onAttachmentItemClicked(itemPosition);
        }
    }

    public interface AttachmentCardClickListener {
        public void onAttachmentItemClicked(int itemPosition);
    }
}
