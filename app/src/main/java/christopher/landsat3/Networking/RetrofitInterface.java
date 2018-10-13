package christopher.landsat3.Networking;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface RetrofitInterface {


    @GET("planetary/earth/imagery/")
    Call<LandsatModel> geLandsatData(
            @Query("lon") String longCoord,
            @Query("lat") String latCoord,
            @Query("dim") String dim,
            @Query("date") String date,
            @Query("cloud_score") String cloudScore,
            @Query("api_key") String apiKey);


}
