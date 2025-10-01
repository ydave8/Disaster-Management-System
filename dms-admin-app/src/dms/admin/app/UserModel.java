package dms.admin.app;

public class UserModel {
  private long id;
  private String email;
  private String name;
  private String role;
  private boolean verified;

  public UserModel() {}

  public UserModel(long id, String email, String name, String role, boolean verified) {
    this.id = id;
    this.email = email;
    this.name = name;
    this.role = role;
    this.verified = verified;
  }

  public long getId() { return id; }
  public void setId(long id) { this.id = id; }

  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public String getRole() { return role; }
  public void setRole(String role) { this.role = role; }

  public boolean isVerified() { return verified; }
  public void setVerified(boolean verified) { this.verified = verified; }
}
