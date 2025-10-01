package dms.user.app;

public class ShelterModel {
  public long id;
  public String name;
  public String address;
  public int capacity;
  public double lat;
  public double lng;
  private Long incidentId;

  public ShelterModel() {}

  public long getId() { return id; }
  public String getName() { return name; }
  public String getAddress() { return address; }
  public int getCapacity() { return capacity; }
  public double getLat() { return lat; }
  public double getLng() { return lng; }
  public Long getIncidentId() { return incidentId; }

  public void setId(long id) { this.id = id; }
  public void setName(String name) { this.name = name; }
  public void setAddress(String address) { this.address = address; }
  public void setCapacity(int capacity) { this.capacity = capacity; }
  public void setLat(double lat) { this.lat = lat; }
  public void setLng(double lng) { this.lng = lng; }
  public void setIncidentId(Long incidentId) { this.incidentId = incidentId; }
}
