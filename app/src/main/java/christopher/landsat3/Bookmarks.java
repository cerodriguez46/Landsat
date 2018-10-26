package christopher.landsat3;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import java.util.List;

import christopher.landsat3.Data.AppDatabase;
import christopher.landsat3.Data.AppExecutors;
import christopher.landsat3.Data.MainViewModel;
import christopher.landsat3.Networking.LandsatModel;

public class Bookmarks extends AppCompatActivity {

    String TAG = "101";

    public BookmarkAdapter mAdapter;

    private RecyclerView recyclerView;

    private List<LandsatModel> satelliteList;

    private int recyclerViewState;

    RecyclerView.LayoutManager layoutManager;

    private AppDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);


        mDb = AppDatabase.getInstance(getApplicationContext());
        loadSatImages();

        recyclerView = (RecyclerView) findViewById(R.id.rv_numbers);

        mAdapter = new BookmarkAdapter(this, satelliteList);


        if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new GridLayoutManager(Bookmarks.this, 1);
            recyclerView.setLayoutManager(layoutManager);
        } else if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager = new GridLayoutManager(Bookmarks.this, 2);
            recyclerView.setLayoutManager(layoutManager);
        }


        recyclerView.setItemAnimator(new DefaultItemAnimator());


        recyclerView.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<LandsatModel> landsatModels = mAdapter.getRecords();
                        mDb.landsatDao().deleteRecord(landsatModels.get(position));
                        Log.v("DatabaseDelete", "deleting satellite image from database");
                    }
                });
            }
        }).attachToRecyclerView(recyclerView);


    }

    private void loadSatImages() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getRecords().observe(this, new Observer<List<LandsatModel>>() {
            @Override
            public void onChanged(List<LandsatModel> recordEntries) {
                Log.d(TAG, "updating lists of tasks from livedata in viewmodel");
                mAdapter.setRecords(recordEntries);


            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}

/*Thread thread = new Thread(new Runnable){
    @Override
    public void Run(

    )
    };
   }
   });
thread.start();
 */

/*AppExecutors.getInstance().diskIO().execute(new Runnable){
    @Override
    public void Run(){

       final List<LandsatModel> model = mDb.taskDao.loadAllRecords());
        runOnUiThread(new Runnable){
        mAdapter.setTasks(model);
        }
    }
});
}
 */

/*AppExecutors.getInstance().diskIO().execute(new Runnable){
    @Override
    public void Run(){

       final List<LandsatModel> model = mDb.taskDao.insertRecord());
        runOnUiThread(new Runnable){
        mAdapter.setTasks(model);
        }
    }
});
}
 */

/*AppExecutors.getInstance().diskIO().execute(new Runnable){
    @Override
    public void Run(){
//get adaptor position
       final List<LandsatModel> model = mDb.taskDao.deleteRecord());
        runOnUiThread(new Runnable){
        mAdapter.setTasks(model);
        }
    }
});
}
 */

/*AppExecutors.getInstance().diskIO().execute(new Runnable){
    @Override
    public void Run(){
//get adaptor position
       final List<LandsatModel> model = mDb.taskDao.deleteRecord());
        runOnUiThread(new Runnable){
        mAdapter.setTasks(model);
        }
    }
});
}
 */

/*AppExecutors.getInstance().diskIO().execute(new Runnable){
    @Override
    public void Run(){
//get adaptor position
       final List<LandsatModel> model = mDb.taskDao.updateRecord());
        runOnUiThread(new Runnable){
        mAdapter.setTasks(model);
        }
    }
});
}
 */

/*AppExecutors.getInstance().diskIO().execute(new Runnable){
    @Override
    public void Run(){

MainViewModel viewModel =ViewModelProviders.of(this).get(MainViewModel.class);
      viewModel.getRecords.observe(this, new Observer<List<LandsatModel>>(){
       @Override
       public void onChanged(List<LandsatModel> recordEntries){
            Log.d(TAG, "updatin lists of tasks from livedat in viewmodel);
            adapter.setTasks(recordEntries);
       }
       }
        runOnUiThread(new Runnable){
        mAdapter.setTasks(model);
        }
    }
});
}
 */


