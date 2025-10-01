package dms.admin.app;

import javafx.collections.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import dms.admin.app.SosModel;
import java.util.List;

public class SosScreen {
  public static void show(Stage stage, Backend backend) {
    TableView<SosModel> table = new TableView<>();
    ObservableList<SosModel> data = FXCollections.observableArrayList();
    table.setItems(data);

    table.getColumns().addAll(
      col("ID", "id", 50),
      col("Email", "userEmail", 160),
      col("Latitude", "latitude", 100),
      col("Longitude", "longitude", 100),
      col("Note", "note", 200),
      col("Time", "createdAt", 180)
    );

    // Refresh data
    refresh(data, backend);

    Button back = new Button("Back");
    back.setOnAction(e -> AdminDashboard.show(stage, backend));

    VBox root = new VBox(12, table, back);
    root.setPadding(new Insets(14));
    stage.setScene(new Scene(root, 800, 600));
    stage.setTitle("SOS Requests");
  }

  private static void refresh(ObservableList<SosModel> data, Backend backend) {
    try {
      List<SosModel> sosList = backend.listSos();  // backend call
      data.setAll(sosList);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private static TableColumn<SosModel, String> col(String title, String prop, int width) {
    TableColumn<SosModel, String> col = new TableColumn<>(title);
    col.setCellValueFactory(new PropertyValueFactory<>(prop));
    col.setPrefWidth(width);
    return col;
  }
}
