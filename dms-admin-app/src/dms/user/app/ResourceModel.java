package dms.user.app;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResourceModel {
  public long id;
  public String description;
  public String location;
  public int quantity;
  public String type;
  private Long incidentId;  

  public ResourceModel() {}

  public long getId() { return id; }
  public String getDescription() { return description; }
  public String getLocation() { return location; }
  public int getQuantity() { return quantity; }
  public String getType() { return type; }
  public Long getIncidentId() { return incidentId; }




  public void setId(long id) { this.id = id; }
  public void setDescription(String description) { this.description = description; }
  public void setLocation(String location) { this.location = location; }
  public void setQuantity(int quantity) { this.quantity = quantity; }
  public void setType(String type) { this.type = type; }
  public void setIncidentId(Long incidentId) { this.incidentId = incidentId; }

}
