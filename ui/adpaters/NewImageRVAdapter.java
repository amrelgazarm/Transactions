package com.qceccenter.qcec.ui.adpaters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qceccenter.qcec.R;
import com.qceccenter.qcec.ui.NewVisitActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewImageRVAdapter extends RecyclerView.Adapter<NewImageRVAdapter.NEWPhotoCardViewHolder> {
//    private List<Bitmap> mAttachmentList;
    private List<String> mAttachmentList;
    private Context mContext;

//    public NewImageRVAdapter(List<Bitmap> mAttachmentList, Context mContext) {
//        this.mAttachmentList = mAttachmentList;
//        this.mContext = mContext;
//    }


    public NewImageRVAdapter(List<String> mAttachmentList, Context mContext) {
        this.mAttachmentList = mAttachmentList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public NewImageRVAdapter.NEWPhotoCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int attachmentViewLayoutID = R.layout.attachment_photo_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(attachmentViewLayoutID, parent, shouldAttachToParentImmediately);
        NewImageRVAdapter.NEWPhotoCardViewHolder viewHolder = new NewImageRVAdapter.NEWPhotoCardViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NEWPhotoCardViewHolder holder, int position) {
        holder.cancelImgBtn.setVisibility(View.VISIBLE);
        holder.cancelImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAttachmentList.remove(position);
                if (mContext instanceof NewVisitActivity) {
                    ((NewVisitActivity) mContext).increaseImageCount();
                    notifyDataSetChanged();
                    if (getItemCount() == 0) {
                        ((NewVisitActivity) mContext).showNoAttachmentIcon();
                    }
                }
            }
        });
//        holder.photoImgView.setImageBitmap(this.mAttachmentList.get(position));

        if (mAttachmentList.get(position) != null && !mAttachmentList.get(position).equals("")) {
            Picasso.with(mContext).load(mAttachmentList.get(position)).fit().into(holder.photoImgView);
        }
    }

//    public void setAttachmentList(List<Bitmap> attachmentList) {
//        this.mAttachmentList = attachmentList;
//    }

    public void setAttachmentList(List<String> attachmentList) {
        this.mAttachmentList = attachmentList;
    }

//    public List<Bitmap> getmAttachmentList() {
//        return mAttachmentList;
//    }

    public List<String> getmAttachmentList() {
        return mAttachmentList;
    }

    @Override
    public int getItemCount() {
            return mAttachmentList.size();
    }

    public class NEWPhotoCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.photo_imageView)
        ImageView photoImgView;
        @BindView(R.id.cancel_imgBtn)
        ImageButton cancelImgBtn;
        @BindView(R.id.maximize_imgBtn)
        ImageButton maxImgBtn;

        public NEWPhotoCardViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int itemPosition = getAdapterPosition();
        }
    }
}
