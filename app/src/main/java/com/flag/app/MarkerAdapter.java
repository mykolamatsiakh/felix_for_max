package com.flag.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Marker;

/**
 * Created by Marvin on 2/16/2018.
 */

public class MarkerAdapter extends RecyclerView.Adapter<MarkerAdapter.ViewHolder> {
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
//        holder.cacheMarkers(position);
        holder.loadImageFromStorage(position);
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
                    if (mOnItemClickListener != null) {
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

        private void loadImageFromStorage(int i) {
            File path = Environment.getExternalStorageDirectory();
            File dir = new File(path + "/save/");
            File f = new File(dir, String.valueOf(i)+"photo.jpg");
            Log.d("loaded from storage ", f.getPath());

        }

        void cacheMarkers(final int i) {
            Picasso.with(mImageView.getContext())
                    .load(mMarker.getImage().getUrl())
                    .into(new Target() {
                              @Override
                              public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                  File path = Environment.getExternalStorageDirectory();
                                  File dir = new File(path + "/save/");
                                  dir.mkdirs();
                                  File file = new File(dir, String.valueOf(i) +"photo.jpg");
                                  Log.d("cache image", file.getPath());
                                  OutputStream out = null;
                                  try {
                                      out = new FileOutputStream(file);
                                      bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                                      out.flush();
                                      out.close();
                                  } catch (IOException e) {
                                      e.printStackTrace();
                                  }

                              }

                              @Override
                              public void onBitmapFailed(Drawable errorDrawable) {
                              }

                              @Override
                              public void onPrepareLoad(Drawable placeHolderDrawable) {
                              }
                          }
                    );
        }
    }
}
