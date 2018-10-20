package christopher.landsat3;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import christopher.landsat3.Networking.LandsatModel;

public class Bookmarks extends AppCompatActivity {

    private BookmarkAdapter mAdapter;

    private RecyclerView recyclerView;
    private List<LandsatModel> satelliteList;

    private int recyclerViewState;

    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);

        Intent intent = getIntent();

        satelliteList = intent.getParcelableArrayListExtra("savedSatelliteImages");

        loadViews();
    }

    private void loadViews() {
        recyclerView = (RecyclerView) findViewById(R.id.rv_numbers);

        satelliteList = new ArrayList<>();
        mAdapter = new BookmarkAdapter(getApplicationContext(), satelliteList);

        if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new GridLayoutManager(Bookmarks.this, 3);
            recyclerView.setLayoutManager(layoutManager);
        } else {
            layoutManager = new GridLayoutManager(Bookmarks.this, 5);
            recyclerView.setLayoutManager(layoutManager);
        }


        recyclerView.setItemAnimator(new DefaultItemAnimator());


        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}
