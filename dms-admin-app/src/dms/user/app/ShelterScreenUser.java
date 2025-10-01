//package dms.user.app;
//
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.geometry.Insets;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.layout.VBox;
//import javafx.stage.Stage;
//
//import java.util.List;
//
//public class ShelterScreenUser {
//
//  public static void show(Stage stage, BackendUser backend) {
//    Label heading = new Label("🏠 Nearby Shelters");
//
//    TableView<ShelterModel> table = new TableView<>();
//    ObservableList<ShelterModel> data = FXCollections.observableArrayList();
//    table.setItems(data);
//
//    table.getColumns().addAll(
//        col("ID", "id", 50),
//        col("Name", "name", 150),
//        col("Address", "address", 200),
//        col("Capacity", "capacity", 100),
//        col("Lat", "lat", 100),
//        col("Lng", "lng", 100)
//    );
//
//    Label status = new Label("🔄 Loading...");
//
//    Button refresh = new Button("🔄 Refresh");
//    refresh.setOnAction(e -> {
//      try {
//        List<ShelterModel> shelters = backend.listShelters();
//        data.setAll(shelters);
//        status.setText("✅ Loaded " + shelters.size() + " shelters");
//      } catch (Exception ex) {
//        status.setText("❌ Failed to load: " + ex.getMessage());
//      }
//    });
//
//    Button back = new Button("⬅ Back");
//    back.setOnAction(e -> UserDashboard.show(stage, backend));
//
//    VBox root = new VBox(10, heading, table, refresh, status, back);
//    root.setPadding(new Insets(20));
//    stage.setScene(new Scene(root, 800, 500));
//  }
//
//  private static <T> TableColumn<ShelterModel, T> col(String title, String prop, int width) {
//    TableColumn<ShelterModel, T> col = new TableColumn<>(title);
//    col.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>(prop));
//    col.setPrefWidth(width);
//    return col;
//  }
//}

package dms.user.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class ShelterScreenUser {

  public static void show(Stage stage, BackendUser backend) {
    Label heading = new Label("🏠 Nearby Shelters");

    TableView<ShelterModel> table = new TableView<>();
    ObservableList<ShelterModel> data = FXCollections.observableArrayList();
    table.setItems(data);

    table.getColumns().addAll(
        col("ID", "id", 50),
        col("Name", "name", 150),
        col("Address", "address", 200),
        col("Capacity", "capacity", 100),
        col("Lat", "lat", 100),
        col("Lng", "lng", 100)
    );

    // 🔥 Incident Button Column (Click to view)
    TableColumn<ShelterModel, Void> incidentCol = new TableColumn<>("Incident ID");
    incidentCol.setPrefWidth(100);
    incidentCol.setCellFactory(col -> new TableCell<>() {
      private final Button viewBtn = new Button();

      {
        viewBtn.setOnAction(e -> {
          ShelterModel shelter = getTableView().getItems().get(getIndex());
          Long incidentId = shelter.getIncidentId();
          if (incidentId != null) {
            IncidentDetailsScreen.show(stage, new BackendAdapter(backend), incidentId);
          } else {
            new Alert(Alert.AlertType.INFORMATION, "No incident linked").showAndWait();
          }
        });
      }

      @Override
      protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
          setGraphic(null);
        } else {
          ShelterModel shelter = getTableView().getItems().get(getIndex());
          Long incidentId = shelter.getIncidentId();
          viewBtn.setText(incidentId == null ? "-" : String.valueOf(incidentId));
          setGraphic(viewBtn);
        }
      }
    });

    table.getColumns().add(incidentCol); // ✅ Add to table

    Label status = new Label("🔄 Loading...");

    Button refresh = new Button("🔄 Refresh");
    refresh.setOnAction(e -> {
      try {
        List<ShelterModel> shelters = backend.listShelters();
        data.setAll(shelters);
        status.setText("✅ Loaded " + shelters.size() + " shelters");
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

  private static <T> TableColumn<ShelterModel, T> col(String title, String prop, int width) {
    TableColumn<ShelterModel, T> col = new TableColumn<>(title);
    col.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>(prop));
    col.setPrefWidth(width);
    return col;
  }
}
