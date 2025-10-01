package dms.user.app;


import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SosScreen {
  public static void show(Stage stage, BackendUser backend) {
    Label title = new Label("🆘 Send Emergency SOS");

    TextField noteField = new TextField();
    noteField.setPromptText("Describe the emergency");

    TextField latField = new TextField();
    latField.setPromptText("Latitude");

    TextField lngField = new TextField();
    lngField.setPromptText("Longitude");

    Button sendBtn = new Button("Submit SOS");
    Button backBtn = new Button("← Back");
    
    Label status = new Label();

    sendBtn.setOnAction(e -> {
      try {
        String note = noteField.getText();
        double lat = Double.parseDouble(latField.getText());
        double lng = Double.parseDouble(lngField.getText());

        String email = backend.getCurrentUserEmail(); // Assume this method exists
        backend.sendSos(email, lat, lng, note);

        status.setText("✅ SOS sent successfully!");
      } catch (Exception ex) {
        status.setText("❌ Error: " + ex.getMessage());
      }
    });
    
     backBtn.setOnAction(e -> {
      UserDashboard.show(stage, backend);
    });

    VBox root = new VBox(10, title, noteField, latField, lngField, sendBtn, status);
    root.setStyle("-fx-padding: 20");
    stage.setScene(new Scene(root, 400, 250));
    stage.setTitle("Send SOS");
  }
}
