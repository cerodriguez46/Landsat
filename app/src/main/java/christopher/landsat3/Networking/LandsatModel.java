package christopher.landsat3.Networking;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


@Entity(tableName = "landsat")
public class LandsatModel implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("cloud_score")
    public double cloudScore;
    @SerializedName("date")
    public String date;
    @SerializedName("id")
    public String id;
    @SerializedName("resource")
    public Resource resource;
    @SerializedName("service_version")
    public String serviceVersion;
    @SerializedName("url")
    public String url;

    public LandsatModel(double cloudScore, String date, String id, Resource resource, String serviceVersion, String url) {
        this.cloudScore = cloudScore;
        this.date = date;
        this.id = id;
        this.resource = resource;
        this.serviceVersion = serviceVersion;
        this.url = url;
    }

    public LandsatModel() {

    }

    public double getCloudScore() {
        return cloudScore;
    }

    public void setCloudScore(double cloudScore) {
        this.cloudScore = cloudScore;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public String getServiceVersion() {
        return serviceVersion;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.cloudScore);
        dest.writeString(this.date);
        dest.writeString(this.id);
        dest.writeParcelable(this.resource, flags);
        dest.writeString(this.serviceVersion);
        dest.writeString(this.url);
    }

    protected LandsatModel(Parcel in) {
        this.cloudScore = in.readDouble();
        this.date = in.readString();
        this.id = in.readString();
        this.resource = in.readParcelable(Resource.class.getClassLoader());
        this.serviceVersion = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<LandsatModel> CREATOR = new Parcelable.Creator<LandsatModel>() {
        @Override
        public LandsatModel createFromParcel(Parcel source) {
            return new LandsatModel(source);
        }

        @Override
        public LandsatModel[] newArray(int size) {
            return new LandsatModel[size];
        }
    };
}


