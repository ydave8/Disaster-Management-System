package dms.admin.app;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;



public class Backend {
  private final String base;
  private final HttpClient http = HttpClient.newHttpClient();

  public Backend(String base){ this.base = base; }

  
  public IncidentModel getIncidentById(long id) throws Exception {
    throw new UnsupportedOperationException("getIncidentById not implemented");
}

public void createAlert(String title, String message, String area,
                        String severity, String expiresAt) throws Exception {
  ObjectMapper mapper = new ObjectMapper();

  var jsonMap = new java.util.HashMap<String, Object>();
  jsonMap.put("title", esc(title));
  jsonMap.put("message", esc(message));
  jsonMap.put("area", esc(area));
  if (severity != null && !severity.trim().isEmpty())
    jsonMap.put("severity", severity);  // only include if not null
  jsonMap.put("expiresAt", esc(expiresAt));

  String json = mapper.writeValueAsString(jsonMap);

  HttpRequest req = HttpRequest.newBuilder(URI.create(base + "/api/v1/alerts"))
      .header("Content-Type", "application/json")
      .POST(HttpRequest.BodyPublishers.ofString(json))
      .build();
  HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
  if (res.statusCode() >= 300) {
    throw new RuntimeException("HTTP " + res.statusCode() + ": " + res.body());
  }
}
  
    public String getBase() {
        return base;
    }
public void updateAlert(Long id, String title, String message, String area,
                        String severity, String expiresAt) throws Exception {
  ObjectMapper mapper = new ObjectMapper();

  var jsonMap = new java.util.HashMap<String, Object>();
  jsonMap.put("title", esc(title));
  jsonMap.put("message", esc(message));
  jsonMap.put("area", esc(area));
  if (severity != null && !severity.trim().isEmpty())
    jsonMap.put("severity", severity);
  jsonMap.put("expiresAt", esc(expiresAt));

  String json = mapper.writeValueAsString(jsonMap);

  HttpRequest req = HttpRequest.newBuilder(URI.create(base + "/api/v1/alerts/" + id))
      .header("Content-Type", "application/json")
      .PUT(HttpRequest.BodyPublishers.ofString(json))
      .build();
  HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
  if (res.statusCode() >= 300) {
    throw new RuntimeException("HTTP " + res.statusCode() + ": " + res.body());
  }
}


  public void deleteAlert(long id) throws Exception {
    HttpRequest req = HttpRequest.newBuilder(URI.create(base + "/api/v1/alerts/" + id))
        .DELETE()
        .build();
    HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
    if (res.statusCode() >= 300) {
      throw new RuntimeException("HTTP " + res.statusCode() + ": " + res.body());
    }
  }

public List<AlertModel> listAlerts() throws Exception {
  HttpRequest req = HttpRequest.newBuilder(URI.create(base + "/api/v1/alerts"))
      .GET()
      .build();

  HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
  if (res.statusCode() >= 300) {
    throw new RuntimeException("HTTP " + res.statusCode() + ": " + res.body());
  }

  ObjectMapper mapper = new ObjectMapper();
  return mapper.readValue(res.body(),
      new TypeReference<List<AlertModel>>() {});
}


  public String login(String email, String password) throws Exception {
    String json = "{\"email\":\"" + esc(email) + "\",\"password\":\"" + esc(password) + "\"}";
    HttpRequest req = HttpRequest.newBuilder(URI.create(base + "/api/v1/auth/login"))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(json))
        .build();
    HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
    if (res.statusCode() >= 300) {
      throw new RuntimeException("HTTP " + res.statusCode() + ": " + res.body());
    }
    return res.body();
  }

  private static String esc(String s) {
    return s == null ? "" : s.replace("\\", "\\\\").replace("\"", "\\\"");
  }

  // Minimal JSON parsing for listAlerts()
  private List<AlertModel> parseAlerts(String json) {
    List<AlertModel> alerts = new ArrayList<>();
    json = json.trim().substring(1, json.length() - 1); // remove [ ]
    if (json.trim().isEmpty()) return alerts;

    String[] items = json.split("\\},\\s*\\{");
    for (String item : items) {
      String cleaned = item.replace("{", "").replace("}", "");
      String[] fields = cleaned.split("\",\"");
      AlertModel alert = new AlertModel();

      for (String f : fields) {
        String[] kv = f.replace("\"", "").split(":", 2);
        if (kv.length < 2) continue;
        String key = kv[0].trim(), value = kv[1].trim();

        switch (key) {
          case "id": alert.setId(Long.parseLong(value)); break;
          case "title": alert.setTitle(value); break;
          case "message": alert.setMessage(value); break;
          case "area": alert.setArea(value); break;
          case "severity": alert.setSeverity(value); break;
          case "expiresAt": alert.setExpiresAt(value); break;
          case "createdAt": alert.setCreatedAt(value); break;
        }
      }
      alerts.add(alert);
    }
    return alerts;
  }
  
  // Incidents
public List<IncidentModel> listIncidents() throws Exception {
  HttpRequest req = HttpRequest.newBuilder(URI.create(base + "/api/v1/incidents"))
      .GET().build();
  HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
  if (res.statusCode() >= 300) throw new RuntimeException("HTTP " + res.statusCode());
  ObjectMapper mapper = new ObjectMapper();
  return mapper.readValue(res.body(), new TypeReference<List<IncidentModel>>() {});
}

public void createIncident(String description, double lat, double lng,
                           String status, String type) throws Exception {
  String json = "{\"description\":\"" + esc(description) + "\",\"lat\":" + lat +
                ",\"lng\":" + lng + ",\"status\":\"" + esc(status) + "\",\"type\":\"" + esc(type) + "\"}";
  HttpRequest req = HttpRequest.newBuilder(URI.create(base + "/api/v1/incidents"))
      .header("Content-Type", "application/json")
      .POST(HttpRequest.BodyPublishers.ofString(json)).build();
  HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
  if (res.statusCode() >= 300) throw new RuntimeException("HTTP " + res.statusCode());
}

public void updateIncident(long id, String description, double lat, double lng,
                           String status, String type) throws Exception {
  String json = "{\"description\":\"" + esc(description) + "\",\"lat\":" + lat +
                ",\"lng\":" + lng + ",\"status\":\"" + esc(status) + "\",\"type\":\"" + esc(type) + "\"}";
  HttpRequest req = HttpRequest.newBuilder(URI.create(base + "/api/v1/incidents/" + id))
      .header("Content-Type", "application/json")
      .PUT(HttpRequest.BodyPublishers.ofString(json)).build();
  HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
  if (res.statusCode() >= 300) throw new RuntimeException("HTTP " + res.statusCode());
}

public void deleteIncident(long id) throws Exception {
  HttpRequest req = HttpRequest.newBuilder(URI.create(base + "/api/v1/incidents/" + id))
      .DELETE().build();
  HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
  if (res.statusCode() >= 300) throw new RuntimeException("HTTP " + res.statusCode());
}


// Shelters
public List<ShelterModel> listShelters() throws Exception {
  HttpRequest req = HttpRequest.newBuilder(URI.create(base + "/api/v1/shelters"))
      .GET().build();
  HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
  if (res.statusCode() >= 300) throw new RuntimeException("HTTP " + res.statusCode());
  ObjectMapper mapper = new ObjectMapper();
  return mapper.readValue(res.body(), new TypeReference<List<ShelterModel>>() {});
}

public void createShelter(String name, String address, int capacity,
                          double lat, double lng, long incidentId) throws Exception {
  String json = "{\"name\":\"" + esc(name) + "\",\"address\":\"" + esc(address) +
                "\",\"capacity\":" + capacity + ",\"lat\":" + lat + ",\"lng\":" + lng + ",\"incidentId\":" + incidentId +  "}";
  
   System.out.println("CREATE SHELTER JSON → " + json);
  HttpRequest req = HttpRequest.newBuilder(URI.create(base + "/api/v1/shelters"))
      .header("Content-Type", "application/json")
      .POST(HttpRequest.BodyPublishers.ofString(json)).build();
  HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
  if (res.statusCode() >= 300) throw new RuntimeException("HTTP " + res.statusCode());
}

public void updateShelter(long id, String name, String address, int capacity,
                          double lat, double lng, long incidentId) throws Exception {
  String json = "{\"name\":\"" + esc(name) + "\",\"address\":\"" + esc(address) +
                "\",\"capacity\":" + capacity + ",\"lat\":" + lat + ",\"lng\":" + lng +",\"incidentId\":" + incidentId + "}";
  HttpRequest req = HttpRequest.newBuilder(URI.create(base + "/api/v1/shelters/" + id))
      .header("Content-Type", "application/json")
      .PUT(HttpRequest.BodyPublishers.ofString(json)).build();
  HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
  if (res.statusCode() >= 300) throw new RuntimeException("HTTP " + res.statusCode());
}

public void deleteShelter(long id) throws Exception {
  HttpRequest req = HttpRequest.newBuilder(URI.create(base + "/api/v1/shelters/" + id))
      .DELETE().build();
  HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
  if (res.statusCode() >= 300) throw new RuntimeException("HTTP " + res.statusCode());
}


// Resources
//public List<ResourceModel> listResources() throws Exception {
//  HttpRequest req = HttpRequest.newBuilder(URI.create(base + "/api/v1/resources"))
//      .GET().build();
//  HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
//  if (res.statusCode() >= 300) throw new RuntimeException("HTTP " + res.statusCode());
//  ObjectMapper mapper = new ObjectMapper();
//  return mapper.readValue(res.body(), new TypeReference<List<ResourceModel>>() {});
//}


public List<ResourceModel> listResources() throws Exception {
  HttpRequest req = HttpRequest.newBuilder(URI.create(base + "/api/v1/resources"))
      .GET()
      .build();

  HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
  if (res.statusCode() >= 300) {
    throw new RuntimeException("HTTP " + res.statusCode() + ": " + res.body());
  }

  ObjectMapper mapper = new ObjectMapper();
  return mapper.readValue(res.body(),
      new TypeReference<List<ResourceModel>>() {});
}
//public void createResource(String description, String location,
//                           int quantity, String type) throws Exception {
//  String json = "{\"description\":\"" + esc(description) + "\",\"location\":\"" + esc(location) +
//                "\",\"quantity\":" + quantity + ",\"type\":\"" + esc(type) + "\"}";
//  HttpRequest req = HttpRequest.newBuilder(URI.create(base + "/api/v1/resources"))
//      .header("Content-Type", "application/json")
//      .POST(HttpRequest.BodyPublishers.ofString(json)).build();
//  HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
//  if (res.statusCode() >= 300) throw new RuntimeException("HTTP " + res.statusCode());
//}
//
//public void updateResource(long id, String description, String location,
//                           int quantity, String type) throws Exception {
//  String json = "{\"description\":\"" + esc(description) + "\",\"location\":\"" + esc(location) +
//                "\",\"quantity\":" + quantity + ",\"type\":\"" + esc(type) + "\"}";
//  HttpRequest req = HttpRequest.newBuilder(URI.create(base + "/api/v1/resources/" + id))
//      .header("Content-Type", "application/json")
//      .PUT(HttpRequest.BodyPublishers.ofString(json)).build();
//  HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
//  if (res.statusCode() >= 300) throw new RuntimeException("HTTP " + res.statusCode());
//}

public void createResource(String description, String location, int quantity, String type, Long incidentId) throws Exception {
  String json = String.format("{\"description\":\"%s\",\"location\":\"%s\",\"quantity\":%d,\"type\":\"%s\",\"incidentId\":%d}",
      esc(description), esc(location), quantity, esc(type), incidentId);

  HttpRequest req = HttpRequest.newBuilder(URI.create(base + "/api/v1/resources"))
      .header("Content-Type", "application/json")
      .POST(HttpRequest.BodyPublishers.ofString(json)).build();

  HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
  if (res.statusCode() >= 300) throw new RuntimeException("HTTP " + res.statusCode());
}

public void updateResource(long id, String description, String location, int quantity, String type, Long incidentId) throws Exception {
  String json = String.format("{\"description\":\"%s\",\"location\":\"%s\",\"quantity\":%d,\"type\":\"%s\",\"incidentId\":%d}",
      esc(description), esc(location), quantity, esc(type), incidentId);

  HttpRequest req = HttpRequest.newBuilder(URI.create(base + "/api/v1/resources/" + id))
      .header("Content-Type", "application/json")
      .PUT(HttpRequest.BodyPublishers.ofString(json)).build();

  HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
  if (res.statusCode() >= 300) throw new RuntimeException("HTTP " + res.statusCode());
}


public void deleteResource(long id) throws Exception {
  HttpRequest req = HttpRequest.newBuilder(URI.create(base + "/api/v1/resources/" + id))
      .DELETE().build();
  HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
  if (res.statusCode() >= 300) throw new RuntimeException("HTTP " + res.statusCode());
}

// --- Users ---
public void createUser(String email, String name, String role) throws Exception {
  String json = String.format("{\"email\":\"%s\",\"name\":\"%s\",\"role\":\"%s\"}",
      esc(email), esc(name), role);
  HttpRequest req = HttpRequest.newBuilder(URI.create(base + "/api/v1/users"))
      .header("Content-Type", "application/json")
      .POST(HttpRequest.BodyPublishers.ofString(json))
      .build();
  HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
  if (res.statusCode() >= 300) throw new RuntimeException("HTTP " + res.statusCode() + ": " + res.body());
}

public void updateUser(long id, String name, String role) throws Exception {
  String json = String.format("{\"name\":\"%s\",\"role\":\"%s\"}", esc(name), role);
  HttpRequest req = HttpRequest.newBuilder(URI.create(base + "/api/v1/users/" + id))
      .header("Content-Type", "application/json")
      .PUT(HttpRequest.BodyPublishers.ofString(json))
      .build();
  HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
  if (res.statusCode() >= 300) throw new RuntimeException("HTTP " + res.statusCode() + ": " + res.body());
}

public void deleteUser(long id) throws Exception {
  HttpRequest req = HttpRequest.newBuilder(URI.create(base + "/api/v1/users/" + id))
      .DELETE().build();
  HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
  if (res.statusCode() >= 300) throw new RuntimeException("HTTP " + res.statusCode() + ": " + res.body());
}

public List<UserModel> listUsers() throws Exception {
  HttpRequest req = HttpRequest.newBuilder(URI.create(base + "/api/v1/users"))
      .GET().build();
  HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
  if (res.statusCode() >= 300) throw new RuntimeException("HTTP " + res.statusCode() + ": " + res.body());

  ObjectMapper mapper = new ObjectMapper();
  return mapper.readValue(res.body(), new TypeReference<List<UserModel>>() {});
}

public List<SosModel> listSos() throws Exception {
  HttpRequest req = HttpRequest.newBuilder(URI.create(base + "/api/v1/sos"))
      .GET().build();

  HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
  if (res.statusCode() >= 300) {
    throw new RuntimeException("HTTP " + res.statusCode() + ": " + res.body());
  }

  ObjectMapper mapper = new ObjectMapper();
  return mapper.readValue(res.body(), new TypeReference<List<SosModel>>() {});
}


}
