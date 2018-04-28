package com.flag.app;

/**
 * Created by Marvin on 2/16/2018.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.facebook.CallbackManager;
import com.flag.FelixApplication;
import com.flag.app.pref.UserPref;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.Marker;

public class MarkerActivity extends AppCompatActivity {

    private static final String TAG = "MarkerActivity";

    User mUser;

    private static final String EXTRA_USER = "EXTRA_USER";

    private DatabaseReference mDatabaseReference;
    CallbackManager callbackManager;


    private ViewSwitcher mViewSwitcher;

    private FloatingActionButton mMapButton;

    private MarkerAdapter mMarkerAdapter;


    public static Intent getStartIntent(Context context, User user) {
        Intent intent = new Intent(context,MarkerActivity.class);
        intent.putExtra(EXTRA_USER, user);
        return intent;
    }

    private final ValueEventListener mValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Log.d(TAG, dataSnapshot.toString());

            List<Marker> markers = new ArrayList<Marker>();
            Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
            Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
            while (iterator.hasNext()) {
                Marker marker = iterator.next().getValue(Marker.class);
                markers.add(marker);
            }
            mMarkerAdapter.refreshMarkers(markers);
            mViewSwitcher.setDisplayedChild(1);
            mMapButton.show();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.d(TAG, databaseError.toString());

        }
    } ;

    @Override
    protected void onStart() {
        super.onStart();
        mDatabaseReference.addValueEventListener(mValueEventListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDatabaseReference.removeEventListener(mValueEventListener);
    }

    //Викликає метод Main Activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_markers);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final String PREFS_NAME = "MyPrefsUser";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, 1).apply();

        setSupportActionBar(toolbar);
        ((FelixApplication) this.getApplication()).setFirstStart(1);
        ActivityCompat.requestPermissions(MarkerActivity.this,
                new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        mUser = getIntent().getParcelableExtra(EXTRA_USER);
        Toast.makeText(MarkerActivity.this,"Вітаємо, "+
                UserPref.get(MarkerActivity.this).getUser().getName(), Toast.LENGTH_SHORT).show();
        callbackManager = CallbackManager.Factory.create();

        mViewSwitcher = (ViewSwitcher) findViewById(R.id.view_switcher);
        mMapButton = (FloatingActionButton) findViewById(R.id.map_button);
        mMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(MapActivity.getStartIntent(MarkerActivity.this, mMarkerAdapter.getMarkerList()));
            }
        });
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        mMarkerAdapter = new MarkerAdapter(this) ;
        mMarkerAdapter.setOnItemClickListener(new MarkerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Marker marker) {
                startActivity(MarkerDetailsActivity.getStartIntent(MarkerActivity.this, marker));
            }
        });
        recyclerView.setAdapter(mMarkerAdapter);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("markers");

    }

}
