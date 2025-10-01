package dms.user.app;

import java.net.URI;
import java.net.http.*;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;


public class BackendUser {
  private final String base;
  private final HttpClient http = HttpClient.newHttpClient();
  
  private String currentUserEmail;

  public BackendUser(String base){ this.base = base; }

public String register(String email, String name, String password) throws Exception {
  String json = String.format(
      "{\"email\":\"%s\",\"name\":\"%s\",\"password\":\"%s\",\"role\":\"USER\"}",
      esc(email), esc(name), esc(password));
  HttpRequest req = HttpRequest.newBuilder(URI.create(base + "/api/v1/auth/register"))
      .header("Content-Type", "application/json")
      .POST(HttpRequest.BodyPublishers.ofString(json))
      .build();
  return http.send(req, HttpResponse.BodyHandlers.ofString()).body();
}

public String verifyOtp(String email, String otp) throws Exception {
  String json = String.format("{\"email\":\"%s\",\"otp\":\"%s\"}", esc(email), esc(otp));
  HttpRequest req = HttpRequest.newBuilder(URI.create(base + "/api/v1/auth/verify"))
      .header("Content-Type", "application/json")
      .POST(HttpRequest.BodyPublishers.ofString(json))
      .build();
  return http.send(req, HttpResponse.BodyHandlers.ofString()).body();
}

public String login(String email, String password) throws Exception {
    this.currentUserEmail = email;
  String json = String.format("{\"email\":\"%s\",\"password\":\"%s\"}", esc(email), esc(password));
  HttpRequest req = HttpRequest.newBuilder(URI.create(base + "/api/v1/auth/login"))
      .header("Content-Type", "application/json")
      .POST(HttpRequest.BodyPublishers.ofString(json))
      .build();
  return http.send(req, HttpResponse.BodyHandlers.ofString()).body();
}

  private static String esc(String s) {
    return s == null ? "" : s.replace("\\","\\\\").replace("\"","\\\"");
  }
  
  public List<AlertModel> listAlerts() throws Exception {
    HttpRequest req = HttpRequest.newBuilder(URI.create(base + "/api/v1/alerts"))
            .GET()
            .build();
    HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(res.body(), new TypeReference<List<AlertModel>>() {});
}
// List incidents
public List<IncidentModel> listIncidents() throws Exception {
  HttpRequest req = HttpRequest.newBuilder(URI.create(base + "/api/v1/incidents"))
      .header("Content-Type", "application/json")
      .GET()
      .build();

  HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
  ObjectMapper mapper = new ObjectMapper();
  return mapper.readValue(res.body(), new TypeReference<List<IncidentModel>>() {});
}

public IncidentModel getIncidentById(long id) throws Exception {
  HttpRequest req = HttpRequest.newBuilder(
      URI.create(base + "/api/v1/incidents/" + id)
    )
    .header("Content-Type", "application/json")
    .GET()
    .build();

  HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());

  ObjectMapper mapper = new ObjectMapper();
  return mapper.readValue(res.body(), IncidentModel.class);
}


// Report an incident
public String reportIncident(String description, String type, double lat, double lng) throws Exception {
  String json = String.format(
      "{\"description\":\"%s\",\"type\":\"%s\",\"lat\":%f,\"lng\":%f}",
      esc(description), esc(type), lat, lng
  );

  HttpRequest req = HttpRequest.newBuilder(URI.create(base + "/api/v1/incidents"))
      .header("Content-Type", "application/json")
      .POST(HttpRequest.BodyPublishers.ofString(json))
      .build();

  return http.send(req, HttpResponse.BodyHandlers.ofString()).body();
}

public List<ShelterModel> listShelters() throws Exception {
  HttpRequest req = HttpRequest.newBuilder(URI.create(base + "/api/v1/shelters"))
      .header("Content-Type", "application/json")
      .GET()
      .build();
  HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
  ObjectMapper mapper = new ObjectMapper();
  return mapper.readValue(res.body(), new TypeReference<List<ShelterModel>>() {});
}

public List<ResourceModel> listResources() throws Exception {
  HttpRequest req = HttpRequest.newBuilder(URI.create(base + "/api/v1/resources"))
      .header("Content-Type", "application/json")
      .GET()
      .build();

  HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
  ObjectMapper mapper = new ObjectMapper();
  return mapper.readValue(res.body(), new TypeReference<List<ResourceModel>>() {});
}

public List<AlertModel> fetchAllAlerts() throws Exception {
  HttpRequest req = HttpRequest.newBuilder(URI.create(base + "/api/v1/alerts"))
      .header("Content-Type", "application/json")
      .GET()
      .build();

  HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
  ObjectMapper mapper = new ObjectMapper();
  return mapper.readValue(res.body(), new TypeReference<List<AlertModel>>() {});
}

public String getBase() {
  return base;
}

public void sendSos(String userEmail, double lat, double lng, String note) throws Exception {
  String json = String.format(
    "{\"userEmail\":\"%s\",\"latitude\":%f,\"longitude\":%f,\"note\":\"%s\"}",
    esc(userEmail), lat, lng, esc(note)
  );

  HttpRequest req = HttpRequest.newBuilder(URI.create(base + "/api/v1/sos"))
    .header("Content-Type", "application/json")
    .POST(HttpRequest.BodyPublishers.ofString(json))
    .build();

  HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());

  if (res.statusCode() >= 300) {
    throw new RuntimeException("HTTP " + res.statusCode() + ": " + res.body());
  }
}

  public String getCurrentUserEmail() {
    return currentUserEmail;
  }

}
