package me.bsu.moovgroovfinal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.activeandroid.query.Select;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import me.bsu.moovgroovfinal.adapters.TracksListCursorAdapter;
import me.bsu.moovgroovfinal.models.Project;
import me.bsu.moovgroovfinal.models.Track;
import me.bsu.moovgroovfinal.other.RecyclerItemClickListener;
import me.bsu.moovgroovfinal.other.Utils;

public class TracksActivity extends AppCompatActivity {

    public static final String TAG = "TRACKS_ACTIVITY";

    public static final String INTENT_PROJECT_ID = "TRACKS_ACTIVITY.PROJECT_ID";

    public long projectID;

    public RecyclerView mRecyclerView;
    public TracksListCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracks);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        projectID = getIntent().getLongExtra(INTENT_PROJECT_ID, 0);
        Log.d(TAG, String.format("Got project ID %d", projectID));

        setupFAB();
        setupRecyclerView();
        populateTracksIfNecessary();
        populateRecyclerView();
    }

    private void setupFAB() {
        FloatingActionButton FABaddVocalMelody = (FloatingActionButton) findViewById(R.id.fab_add_vocal_melody_track);
        FloatingActionButton FABaddBeatLoop = (FloatingActionButton) findViewById(R.id.fab_add_beat_loop_track);
        FABaddVocalMelody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "FAB Add Vocal Melody clicked");
                startAddVocalTrackActivity();
            }
        });
        FABaddBeatLoop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "FAB Add Beat Loop clicked");

            }
        });
    }

    private void startAddVocalTrackActivity() {
        Intent i = new Intent(TracksActivity.this, RecordActivity.class);
        startActivity(i);
    }

    private void setupRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.listview_tracks);
        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, String.format("%d clicked", position));
                // play sound here
                ArrayList<Integer> list = new ArrayList<>();
                list.add(0);
                list.add(1000);
                list.add(2000);

                Utils.playBeats(TracksActivity.this, list );
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Log.d(TAG, String.format("%d long press", position));
            }
        }));
    }

    private void populateRecyclerView() {
        mAdapter = new TracksListCursorAdapter(Track.getTracksCursor(projectID), this);
        mRecyclerView.swapAdapter(mAdapter, true);
    }

    private void populateTracksIfNecessary() {
        if (Track.getTracks(projectID).size() < 5) {
            Project p = new Select().from(Project.class).where("Id = ?", projectID).executeSingle();
            Log.d(TAG, "Got Project Name: " + p.name);

            String name = p.name + " kick ass";
            String filename = "bogus file name";
            Track t = new Track(name, filename, p);
            t.save();

            String name2 = p.name + " momo";
            String filename2 = "haha";
            Track t2 = new Track(name2, filename2, p);
            t2.save();
        } else {
            Log.d(TAG, Track.getTracks(projectID).size() + " items for project");
        }
    }




}
