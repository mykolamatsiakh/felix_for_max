package com.flag.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import model.Marker;

/**c
 * Created by Marvin on 2/16/2018.
 */

public class MarkerDetailsActivity extends AppCompatActivity {
    private static final String EXTRA_MARKER = "marker";

    public static Intent getStartIntent(Context context, Marker marker) {
        Intent startIntent = new Intent(context, MarkerDetailsActivity.class);
        startIntent.putExtra(EXTRA_MARKER, marker);
        return startIntent;
    }


    ImageView mImageView;

    TextView mNameTextView;

    TextView mAuthorTextView;

    TextView mDateTextView;

    TextView mDescriptionTextView;

    Marker mMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_marker);
        mMarker = getIntent().getParcelableExtra(EXTRA_MARKER);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(mMarker.getName());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        mImageView = findViewById(R.id.image_view);
        mAuthorTextView = findViewById(R.id.text_view_author);
        mDateTextView = findViewById(R.id.text_view_date);
        mDescriptionTextView = findViewById(R.id.text_view_description);
        bindMarker(mMarker);
    }

    private void bindMarker(Marker marker) {
        Picasso.with(mImageView.getContext())
                .load(marker.getImage().getUrl())
                .into(mImageView);
        mAuthorTextView.setText(marker.getLocation().getAuthor());
        mDescriptionTextView.setText(marker.getDescription());
        mDateTextView.setText(marker.getDate());

    }
}

