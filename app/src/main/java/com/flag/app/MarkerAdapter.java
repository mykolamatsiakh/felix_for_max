package com.flag.app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import model.Marker;

/**
 * Created by Marvin on 2/16/2018.
 */

public class MarkerAdapter extends RecyclerView.Adapter<MarkerAdapter.ViewHolder>{
    private final LayoutInflater mLayoutInflater;
    private final List<Marker> mMarkerList;

    public interface OnItemClickListener {
        void onItemClick(Marker marker);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public MarkerAdapter(Context MARVIN) {
        mLayoutInflater = LayoutInflater.from(MARVIN);
        mMarkerList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemMarker = mLayoutInflater.inflate(R.layout.item_marker, parent, false);
        return new ViewHolder(itemMarker);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Marker marker = mMarkerList.get(position);
        holder.bindMarker(marker);
    }

    @Override
    public int getItemCount() {
        return mMarkerList.size();
    }

    public void refreshMarkers(List<Marker> markers) {
        mMarkerList.clear();
        mMarkerList.addAll(markers);
        notifyDataSetChanged();
    }

    public List<Marker> getMarkerList() {
        return mMarkerList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView mImageView;
        private final TextView mNameTextView;
        private final TextView mAuthorTextView;

        private Marker mMarker;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.photo_image_view);
            mNameTextView = (TextView) itemView.findViewById(R.id.text_view_name);
            mAuthorTextView = (TextView) itemView.findViewById(R.id.text_view_author);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null){
                        mOnItemClickListener.onItemClick(mMarker);
                    }
                }
            });
        }

        void bindMarker(Marker marker) {
            mMarker = marker;
            mNameTextView.setText(mMarker.getName());
            mAuthorTextView.setText(mMarker.getLocation().getAuthor());
            Picasso.with(mImageView.getContext())
                    .load(mMarker.getImage().getUrl())
                    .placeholder(R.color.grey_400)
                    .into(mImageView);
        }
    }
}
