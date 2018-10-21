package christopher.landsat3;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import christopher.landsat3.Data.AppDatabase;

public class Bookmarks extends AppCompatActivity {

    public BookmarkAdapter mAdapter;

    private RecyclerView recyclerView;


    private int recyclerViewState;

    RecyclerView.LayoutManager layoutManager;

    private AppDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);


        mDb = AppDatabase.getInstance(getApplicationContext());


        recyclerView = (RecyclerView) findViewById(R.id.rv_numbers);



        if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new GridLayoutManager(Bookmarks.this, 3);
            recyclerView.setLayoutManager(layoutManager);
        } else {
            layoutManager = new GridLayoutManager(Bookmarks.this, 5);
            recyclerView.setLayoutManager(layoutManager);
        }


        recyclerView.setItemAnimator(new DefaultItemAnimator());


        mAdapter.setRecords(mDb.landsatDao().loadAllRecords());

        recyclerView.setAdapter(mAdapter);


    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
