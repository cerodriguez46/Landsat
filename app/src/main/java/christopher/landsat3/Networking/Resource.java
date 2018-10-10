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

    public Resource(String dataset, String planet) {
        this.dataset = dataset;
        this.planet = planet;
    }

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public String getPlanet() {
        return planet;
    }

    public void setPlanet(String planet) {
        this.planet = planet;
    }

    public Resource() {

    }
}
