package dms.user.app;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;
import java.util.List;
import javafx.scene.media.AudioClip;
import javafx.application.Platform;


public class UserDashboard {
    
  private static Timer alertTimer;
private static final AtomicLong lastAlertId = new AtomicLong(0);

private static void startAlertPolling(BackendUser backend) {
  if (alertTimer != null) return; // already running

  alertTimer = new Timer(true);
  alertTimer.scheduleAtFixedRate(new TimerTask() {
    @Override
    public void run() {
      try {
        List<AlertModel> alerts = backend.listAlerts();
        if (!alerts.isEmpty()) {
          AlertModel latest = alerts.get(0);
          if (latest.getId() > lastAlertId.get()) {
            lastAlertId.set(latest.getId());
            Platform.runLater(() -> {
              beep();
              showAlertPopup(latest);
            });
          }
        }
      } catch (Exception e) {
        System.out.println("⚠️ Failed to fetch alerts: " + e.getMessage());
      }
    }
  }, 0, 5000); // poll every 5 seconds
}

private static void logout(Stage stage,BackendUser backend) {
    // Redirect to login scene or close the app
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Logout");
    alert.setHeaderText(null);
    alert.setContentText("You have been logged out.");
    alert.showAndWait();

    // Optionally, return to login scene if you have one
    // stage.setScene(loginScene);  // ← uncomment if you implemented login
    LoginScreen.show(stage, backend);
}


private static void beep() {
  try {
    AudioClip sound = new AudioClip(URI.create("file:beep.wav").toString());
    sound.play();
  } catch (Exception e) {
    System.out.println("⚠️ Failed to play beep: " + e.getMessage());
  }
}

private static void showAlertPopup(AlertModel a) {
  Alert alert = new Alert(Alert.AlertType.WARNING);
  alert.setTitle("🚨 New Alert!");
  alert.setHeaderText(a.getTitle());
  alert.setContentText("📍 Area: " + a.getArea() + "\n⚠️ Severity: " + a.getSeverity());
  alert.show();
}
  
  public static void show(Stage stage, BackendUser backend) {
    Label welcome = new Label("📱 User Dashboard");

    Button alertsBtn = new Button("View Alerts");
    alertsBtn.setOnAction(e -> AlertsScreenUser.show(stage, backend));

    Button reportIncidentBtn = new Button("Report Incident");
    reportIncidentBtn.setOnAction(e -> ReportIncidentScreenUser.show(stage, backend));

    Button viewIncidentsBtn = new Button("View Incidents");
    viewIncidentsBtn.setOnAction(e -> IncidentsScreenUser.show(stage, backend));

    Button sheltersBtn = new Button("View Shelters");
    sheltersBtn.setOnAction(e -> ShelterScreenUser.show(stage, backend));

    Button resourcesBtn = new Button("View Resources");
    resourcesBtn.setOnAction(e -> UserResourcesScreen.show(stage, backend));
    
//    resourcesBtn.setOnAction(e -> UserResourcesScreen.show(stage, backend));
    
    Button mapBtn = new Button("🗺️ Map View");
    mapBtn.setOnAction(e -> MapScreen.show(stage, backend));

    Button sosBtn = new Button("🆘 Send SOS");
    sosBtn.setOnAction(e -> SosScreen.show(stage, backend));

    
    Button logoutBtn = new Button("Logout");
    logoutBtn.setOnAction(e -> logout(stage, backend));

    

    VBox root = new VBox(12, welcome, sosBtn, alertsBtn, reportIncidentBtn, viewIncidentsBtn, sheltersBtn, resourcesBtn, mapBtn, logoutBtn);
    root.setPadding(new Insets(20));

    stage.setScene(new Scene(root, 400, 300));
    stage.setTitle("User Dashboard");
    stage.show();
    startAlertPolling(backend);
  }
}
