package dms.user.app;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ReportIncidentScreenUser {
  public static void show(Stage stage, BackendUser backend) {
    Label title = new Label("📢 Report an Incident");

    TextField descriptionField = new TextField();
    descriptionField.setPromptText("Description");

    TextField typeField = new TextField();
    typeField.setPromptText("Type (e.g. Fire, Flood)");

    TextField latField = new TextField();
    latField.setPromptText("Latitude");

    TextField lngField = new TextField();
    lngField.setPromptText("Longitude");

    Label status = new Label();
    Button submit = new Button("📤 Submit");

    submit.setOnAction(e -> {
      try {
        String desc = descriptionField.getText();
        String type = typeField.getText();
        double lat = Double.parseDouble(latField.getText());
        double lng = Double.parseDouble(lngField.getText());

        String res = backend.reportIncident(desc, type, lat, lng);
        status.setText("✅ Submitted: " + res);
      } catch (Exception ex) {
        ex.printStackTrace();
        status.setText("❌ Error: " + ex.getMessage());
      }
    });

    Button back = new Button("⬅ Back");
    back.setOnAction(e -> UserDashboard.show(stage, backend));

    VBox root = new VBox(10, title, descriptionField, typeField, latField, lngField, submit, status, back);
    root.setPadding(new Insets(20));
    stage.setScene(new Scene(root, 400, 400));
    stage.setTitle("User - Report Incident");
  }
}
