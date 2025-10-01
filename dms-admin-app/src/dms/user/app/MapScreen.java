package dms.user.app;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.geometry.Insets;

import java.util.List;

public class MapScreen {

  public static void show(Stage stage, BackendUser backend) {
    Label heading = new Label("🗺️ Incident Map");

    WebView webView = new WebView();
    WebEngine webEngine = webView.getEngine();

    Label status = new Label("🔄 Loading...");

    Button back = new Button("⬅ Back");
    back.setOnAction(e -> UserDashboard.show(stage, backend));

    Button reload = new Button("🔁 Reload Map");
    reload.setOnAction(e -> {
      try {
        List<IncidentModel> incidents = backend.listIncidents();

        StringBuilder markers = new StringBuilder();
        for (IncidentModel inc : incidents) {
          String safeDescription = inc.getDescription()
              .replace("%", "%%")
              .replace("'", "\\'")
              .replace("\"", "&quot;");

          markers.append(String.format(
              "L.marker([%f, %f]).addTo(map).bindPopup('%s');\n",
              inc.getLat(), inc.getLng(), safeDescription
          ));
        }

        String html = String.format("""
          <html>
          <head>
            <link rel="stylesheet" href="https://unpkg.com/leaflet/dist/leaflet.css" />
            <script src="https://unpkg.com/leaflet/dist/leaflet.js"></script>
            <style>
              #map { height: 100%%; width: 100%%; }
              body { margin: 0; }
            </style>
          </head>
          <body>
            <div id="map"></div>
            <script>
              var map = L.map('map').setView([20.5937, 78.9629], 4);
              L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                attribution: 'Map data © <a href="https://openstreetmap.org">OpenStreetMap</a>'
              }).addTo(map);
              %s
            </script>
          </body>
          </html>
          """, markers.toString());

        webEngine.loadContent(html);
        status.setText("✅ Loaded " + incidents.size() + " incidents");
      } catch (Exception ex) {
        status.setText("⚠️ Error loading map: " + ex.getMessage());
      }
    });

    // Initial load
    reload.fire();

    VBox root = new VBox(10, heading, webView, reload, status, back);
    root.setPadding(new Insets(10));
    stage.setScene(new Scene(root, 800, 600));
    stage.setTitle("Incident Map");
    stage.show();
  }
}
