package dms.admin.app;

public class ShelterModel {
  private long id;
  private String name;
  private String address;
  private int capacity;
  private double lat;
  private double lng;
  private Long incidentId;

  public ShelterModel() {}

  public ShelterModel(long id, String name, String address, int capacity, double lat, double lng) {
    this.id = id;
    this.name = name;
    this.address = address;
    this.capacity = capacity;
    this.lat = lat;
    this.lng = lng;
  }

  public long getId() { return id; }
  public void setId(long id) { this.id = id; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public String getAddress() { return address; }
  public void setAddress(String address) { this.address = address; }

  public int getCapacity() { return capacity; }
  public void setCapacity(int capacity) { this.capacity = capacity; }

  public double getLat() { return lat; }
  public void setLat(double lat) { this.lat = lat; }

  public double getLng() { return lng; }
  public void setLng(double lng) { this.lng = lng; }
  
  public Long getIncidentId() { return incidentId; }
  public void setIncidentId(Long incidentId) { this.incidentId = incidentId; }
}
