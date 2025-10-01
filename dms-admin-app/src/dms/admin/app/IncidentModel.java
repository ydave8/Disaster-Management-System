package dms.admin.app;

public class IncidentModel {
  private long id;
  private String description;
  private String status;
  private String type;
  private double lat;
  private double lng;
  private String createdAt;

  public IncidentModel() {}

  public IncidentModel(long id, String description, String status, String type, double lat, double lng, String createdAt) {
    this.id = id;
    this.description = description;
    this.status = status;
    this.type = type;
    this.lat = lat;
    this.lng = lng;
    this.createdAt = createdAt;
  }

  public long getId() { return id; }
  public void setId(long id) { this.id = id; }

  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }

  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }

  public String getType() { return type; }
  public void setType(String type) { this.type = type; }

  public double getLat() { return lat; }
  public void setLat(double lat) { this.lat = lat; }

  public double getLng() { return lng; }
  public void setLng(double lng) { this.lng = lng; }

  public String getCreatedAt() { return createdAt; }
  public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
  
  @Override
public String toString() {  return description; }

}
