package dms.user.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;


import java.util.List;

public class AlertsScreenUser {

    public static void show(Stage stage, BackendUser backend) {
        Label title = new Label("🚨 Active Alerts");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Table
        TableView<AlertModel> table = new TableView<>();
        ObservableList<AlertModel> data = FXCollections.observableArrayList();
        table.setItems(data);

        table.getColumns().addAll(
                col("ID", "id", 50),
                col("Title", "title", 120),
                col("Area", "area", 100),
                col("Severity", "severity", 100),
                col("Expires", "expiresAt", 160),
                col("Message", "message", 200)
        );

        // Refresh Button
        Button refresh = new Button("🔄 Refresh");
        Label status = new Label();

        refresh.setOnAction(e -> {
            try {
                List<AlertModel> alerts = backend.listAlerts();
                data.setAll(alerts);
                status.setText("✅ Loaded " + alerts.size() + " alerts");
            } catch (Exception ex) {
                status.setText("❌ Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        // Back button to dashboard
        Button back = new Button("⬅ Back");
        back.setOnAction(e -> UserDashboard.show(stage, backend));

        VBox root = new VBox(12, title, table, refresh, status, back);
        root.setPadding(new Insets(14));

        stage.setScene(new Scene(root, 800, 600));
        stage.setTitle("User - Alerts");
    }

    private static TableColumn<AlertModel, String> col(String title, String prop, int width) {
        TableColumn<AlertModel, String> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(prop));
        col.setPrefWidth(width);
        return col;
    }
}
