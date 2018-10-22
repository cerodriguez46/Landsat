package christopher.landsat3.Data;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import christopher.landsat3.Networking.LandsatModel;

@Dao
public interface LandsatDao {

    //loads list of tasks
    @Query("SELECT * FROM   landsat")
    List<LandsatModel> loadAllRecords();

    //inserts new record into the database
    @Insert
    void insertRecord(LandsatModel landsatModel);

    //update records
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateRecord(LandsatModel landsatModel);

    //delete records
    @Delete
    void deleteRecord(LandsatModel landsatModel);
}

/*
@Query("SELECT * FROM   landsat")
    LiveData<List<LandsatModel>> loadAllRecords();


 */
