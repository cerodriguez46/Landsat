package christopher.landsat3.Networking;

import com.google.gson.annotations.SerializedName;

public class LandsatModel {

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
}


