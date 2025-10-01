package dms.user.app;

import dms.admin.app.Backend;
import dms.admin.app.IncidentModel;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class IncidentDetailsScreen {

    public static void show(Stage stage, Backend backend, long incidentId) {
        try {
            IncidentModel incident = backend.getIncidentById(incidentId);

            Label id = new Label("Incident ID: " + incident.getId());
//            Label location = new Label("Location: " + incident.getLocation());
            Label type = new Label("Type: " + incident.getType());
            Label desc = new Label("Description: " + incident.getDescription());

            Button back = new Button("Back");
            back.setOnAction(e -> UserResourcesScreen.show(stage, backend));

            VBox root = new VBox(10, id, type, desc, back);
            root.setPadding(new Insets(20));

            stage.setScene(new Scene(root, 400, 300));
            stage.setTitle("Incident Details");

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to load incident: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
