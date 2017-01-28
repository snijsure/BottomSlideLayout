package com.snijsure.sample.bottomslidelayout.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.snijsure.sample.bottomslidelayout.R;

/**
 * Created by subodhnijsure on 1/10/17.
 */


public class CuratedCollectionAdapter extends RecyclerView.Adapter<CuratedCollectionAdapter.ViewHolder> {
    private String[] mImageArray;
    private OnRecyclerItemClick mOnRecyclerItemClickListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView mCuratedImage;
        public ViewHolder(View v) {
            super(v);
            mCuratedImage = (ImageView) v.findViewById(R.id.curated_collection_image);
            mCuratedImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mCuratedImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnRecyclerItemClickListener != null) {
                int itemPosition = getAdapterPosition();
                mOnRecyclerItemClickListener.onItemClick(itemPosition);
            }
        }
    }

    public CuratedCollectionAdapter(String[] imageArray, OnRecyclerItemClick onRecyclerItemClick) {
        mImageArray = imageArray;
        mOnRecyclerItemClickListener = onRecyclerItemClick;
    }

    @Override
    public CuratedCollectionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.curated_collection, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(holder.mCuratedImage.getContext()).load(mImageArray[position]).into(holder.mCuratedImage);
    }

    @Override
    public int getItemCount() {
        return mImageArray.length;
    }

    public interface OnRecyclerItemClick {
        void onItemClick(int position);
    }
}
