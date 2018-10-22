package christopher.landsat3.Data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import christopher.landsat3.Networking.LandsatModel;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<LandsatModel>> satelliteList;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        //satelliteList = database.landsatDao().loadAllRecords();
    }

    public LiveData<List<LandsatModel>> getRecords() {
        return satelliteList;
    }
}
