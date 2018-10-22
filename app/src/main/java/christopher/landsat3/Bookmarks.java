package christopher.landsat3;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import christopher.landsat3.Data.AppDatabase;
import christopher.landsat3.Networking.LandsatModel;

public class Bookmarks extends AppCompatActivity {

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


        recyclerView = (RecyclerView) findViewById(R.id.rv_numbers);

        mAdapter = new BookmarkAdapter(this, satelliteList);

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


