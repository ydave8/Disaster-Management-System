package dms.user.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class IncidentsScreenUser {
  public static void show(Stage stage, BackendUser backend) {
    Label title = new Label("🧯 Reported Incidents");

    TableView<IncidentModel> table = new TableView<>();
    ObservableList<IncidentModel> data = FXCollections.observableArrayList();

    TableView<ResourceModel> resourceTable = new TableView<>();
resourceTable.setPrefHeight(200);
resourceTable.setPlaceholder(new Label("No resources linked to this incident"));

resourceTable.getColumns().addAll(
    resourceCol("ID", "id", 50),
    resourceCol("Type", "type", 100),
    resourceCol("Quantity", "quantity", 80),
    resourceCol("Location", "location", 120),
    resourceCol("Description", "description", 200)
);

TableView<ShelterModel> shelterTable = new TableView<>();
shelterTable.setPrefHeight(120);
shelterTable.setPlaceholder(new Label("No shelter linked to this incident"));

shelterTable.getColumns().addAll(
    shelterCol("ID", "id", 50),
    shelterCol("Name", "name", 150),
    shelterCol("Address", "address", 200),
    shelterCol("Capacity", "capacity", 80),
    shelterCol("Latitude", "lat", 80),
    shelterCol("Longitude", "lng", 80)
);
    

    table.getColumns().addAll(
        col("ID", "id", 40),
        col("Description", "description", 200),
        col("Type", "type", 80),
        col("Status", "status", 80),
        col("Latitude", "lat", 80),
        col("Longitude", "lng", 80)
//        col("Created At", "createdAt", 150)
    );
    table.setItems(data);
    
    table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
  if (newSel != null) {
    long incidentId = newSel.getId();
    try {
      List<ResourceModel> allResources = backend.listResources();
      List<ResourceModel> filtered = allResources.stream()
          .filter(r -> r.getIncidentId() == incidentId)
          .toList();
      resourceTable.setItems(FXCollections.observableArrayList(filtered));
      
            List<ShelterModel> allShelters = backend.listShelters();
            System.out.println("DEBUG: Fetched " + allShelters.size() + " shelters:");
for (ShelterModel s : allShelters) {
    System.out.println("ID: " + s.getId() + " name=" + s.getName() + " incidentId=" + s.getIncidentId());
}
List<ShelterModel> matchingShelter = allShelters.stream()
        .filter(s -> Long.valueOf(incidentId).equals(s.getIncidentId()))
//    .filter(s -> s.getIncidentId() != null && s.getIncidentId() == incidentId)
    .toList();
      shelterTable.setItems(FXCollections.observableArrayList(matchingShelter));

    } catch (Exception e) {
      e.printStackTrace();
      resourceTable.setItems(FXCollections.observableArrayList()); // clear in case of error
    }
  }
});


    Button refresh = new Button("🔄 Refresh");
    Label status = new Label();

    refresh.setOnAction(e -> {
      try {
        List<IncidentModel> incidents = backend.listIncidents();
        data.setAll(incidents);
        status.setText("✅ Loaded " + incidents.size() + " incidents");
      } catch (Exception ex) {
        ex.printStackTrace();
        status.setText("❌ Failed to load incidents");
      }
    });

    Button back = new Button("⬅ Back");
    back.setOnAction(e -> UserDashboard.show(stage, backend));

    VBox root = new VBox(10, title, table, resourceTable, shelterTable, refresh, status, back);
    root.setPadding(new Insets(20));
    stage.setScene(new Scene(root, 800, 500));
    stage.setTitle("User - Incidents");
  }

  private static TableColumn<IncidentModel, String> col(String title, String prop, int width) {
    TableColumn<IncidentModel, String> col = new TableColumn<>(title);
    col.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>(prop));
    col.setPrefWidth(width);
    return col;
  }
  

private static TableColumn<ResourceModel, String> resourceCol(String title, String prop, int width) {
  TableColumn<ResourceModel, String> col = new TableColumn<>(title);
  col.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>(prop));
  col.setPrefWidth(width);
  return col;
}

private static TableColumn<ShelterModel, String> shelterCol(String title, String prop, int width) {
  TableColumn<ShelterModel, String> col = new TableColumn<>(title);
  col.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>(prop));
  col.setPrefWidth(width);
  return col;
}


}
