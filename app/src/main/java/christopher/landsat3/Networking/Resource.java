package christopher.landsat3.Networking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Resource {

    @SerializedName("dataset")
    @Expose
    public String dataset;
    @SerializedName("planet")
    @Expose
    public String planet;
}
