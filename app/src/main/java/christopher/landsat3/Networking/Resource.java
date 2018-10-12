package christopher.landsat3.Networking;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Resource implements Parcelable {

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

    protected Resource(Parcel in) {
        dataset = in.readString();
        planet = in.readString();
    }

    public static final Creator<Resource> CREATOR = new Creator<Resource>() {
        @Override
        public Resource createFromParcel(Parcel in) {
            return new Resource(in);
        }

        @Override
        public Resource[] newArray(int size) {
            return new Resource[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(dataset);
        parcel.writeString(planet);
    }
}
