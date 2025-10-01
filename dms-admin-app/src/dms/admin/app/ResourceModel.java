package dms.admin.app;

public class ResourceModel {
  private long id;
  private String description;
  private String location;
  private String type;
  private int quantity;
  private Long incidentId;

  public ResourceModel() {}

  public ResourceModel(long id, String description, String location, String type, int quantity) {
    this.id = id;
    this.description = description;
    this.location = location;
    this.type = type;
    this.quantity = quantity;
  }

  public long getId() { return id; }
  public void setId(long id) { this.id = id; }

  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }

  public String getLocation() { return location; }
  public void setLocation(String location) { this.location = location; }

  public String getType() { return type; }
  public void setType(String type) { this.type = type; }

  public int getQuantity() { return quantity; }
  public void setQuantity(int quantity) { this.quantity = quantity; }
  
  public Long getIncidentId() { return incidentId; }
  public void setIncidentId(Long incidentId) { this.incidentId = incidentId; }
}
