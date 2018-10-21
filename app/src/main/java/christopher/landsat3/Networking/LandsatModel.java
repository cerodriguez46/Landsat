package christopher.landsat3.Networking;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


@Entity(tableName = "landsat")
public class LandsatModel implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int Roomid;
    @SerializedName("cloud_score")
    public double cloudScore;
    @SerializedName("date")
    public String date;
    @SerializedName("id")
    public String id;

    @SerializedName("service_version")
    public String serviceVersion;
    @SerializedName("url")
    public String url;

    String latitude;
    String longitude;

    @Ignore
    public LandsatModel(double cloudScore, String date, String id, String serviceVersion, String url, String latitude, String longitude) {
        this.cloudScore = cloudScore;
        this.date = date;
        this.id = id;

        this.serviceVersion = serviceVersion;
        this.url = url;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    //room will use this second constructor with the int id
    public LandsatModel(int roomid, double cloudScore, String date, String id, String serviceVersion, String url, String latitude, String longitude) {
        Roomid = roomid;
        this.cloudScore = cloudScore;
        this.date = date;
        this.id = id;
        this.serviceVersion = serviceVersion;
        this.url = url;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LandsatModel() {
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getRoomid() {
        return Roomid;
    }

    public void setRoomid(int roomid) {
        Roomid = roomid;
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
        dest.writeInt(this.Roomid);
        dest.writeDouble(this.cloudScore);
        dest.writeString(this.date);
        dest.writeString(this.id);
        dest.writeString(this.serviceVersion);
        dest.writeString(this.url);
        dest.writeString(this.latitude);
        dest.writeString(this.longitude);
    }

    protected LandsatModel(Parcel in) {
        this.Roomid = in.readInt();
        this.cloudScore = in.readDouble();
        this.date = in.readString();
        this.id = in.readString();
        this.serviceVersion = in.readString();
        this.url = in.readString();
        this.latitude = in.readString();
        this.longitude = in.readString();
    }

    public static final Creator<LandsatModel> CREATOR = new Creator<LandsatModel>() {
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


