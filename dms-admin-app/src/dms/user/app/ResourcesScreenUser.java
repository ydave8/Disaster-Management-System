package dms.user.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class ResourcesScreenUser {

  public static void show(Stage stage, BackendUser backend) {
    Label heading = new Label("📦 Available Resources");

    TableView<ResourceModel> table = new TableView<>();
    ObservableList<ResourceModel> data = FXCollections.observableArrayList();
    table.setItems(data);

    table.getColumns().addAll(
        col("ID", "id", 50),
        col("Description", "description", 200),
        col("Type", "type", 100),
        col("Quantity", "quantity", 100),
        col("Location", "location", 200)
    );

    Label status = new Label("🔄 Loading...");

    Button refresh = new Button("🔄 Refresh");
    refresh.setOnAction(e -> {
      try {
        List<ResourceModel> resources = backend.listResources();
        data.setAll(resources);
        status.setText("✅ Loaded " + resources.size() + " resources");
      } catch (Exception ex) {
        status.setText("❌ Failed to load: " + ex.getMessage());
      }
    });

    Button back = new Button("⬅ Back");
    back.setOnAction(e -> UserDashboard.show(stage, backend));

    VBox root = new VBox(10, heading, table, refresh, status, back);
    root.setPadding(new Insets(20));
    stage.setScene(new Scene(root, 800, 500));
  }

  private static <T> TableColumn<ResourceModel, T> col(String title, String prop, int width) {
    TableColumn<ResourceModel, T> col = new TableColumn<>(title);
    col.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>(prop));
    col.setPrefWidth(width);
    return col;
  }
}
