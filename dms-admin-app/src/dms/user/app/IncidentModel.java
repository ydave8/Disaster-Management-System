package dms.user.app;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IncidentModel {
    private int id;
    private String description;
    private String type;
    private String status;

    @JsonProperty("created_at")
    private String createdAt;

    private double lat;
    private double lng;

    public IncidentModel() {}

    public int getId() { return id; }
    public String getDescription() { return description; }
    public String getType() { return type; }
    public String getStatus() { return status; }
    public String getCreatedAt() { return createdAt; }
    public double getLat() { return lat; }
    public double getLng() { return lng; }

    public void setId(int id) { this.id = id; }
    public void setDescription(String description) { this.description = description; }
    public void setType(String type) { this.type = type; }
    public void setStatus(String status) { this.status = status; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setLat(double lat) { this.lat = lat; }
    public void setLng(double lng) { this.lng = lng; }
}
