package christopher.landsat3.Networking;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitInterface {

    @GET("/imagery/?lon=100.75&lat=1.5&date=2014-02-01&cloud_score=True")
    Call<String> geLandsatData(
            @Query("api_key") String apiKey);


}
