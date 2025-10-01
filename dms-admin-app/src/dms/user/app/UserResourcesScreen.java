package dms.user.app;

import dms.admin.app.Backend;
import dms.admin.app.ResourceModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class UserResourcesScreen {

    
    public static void show(Stage stage, BackendUser backendUser) {
  show(stage, new BackendAdapter(backendUser));
}

    
  public static void show(Stage stage, Backend backend) {
    TableView<ResourceModel> table = new TableView<>();
    ObservableList<ResourceModel> data = FXCollections.observableArrayList();

        TableColumn<ResourceModel, Void> incidentCol = new TableColumn<>("Incident ID");
incidentCol.setPrefWidth(100);
incidentCol.setCellFactory(col -> new TableCell<>() {
    private final Button viewBtn = new Button();

    {
        viewBtn.setOnAction(e -> {
            ResourceModel resource = getTableView().getItems().get(getIndex());
            Long incidentId = resource.getIncidentId();
            if (incidentId != null) {
                IncidentDetailsScreen.show(stage, backend, incidentId);
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "No incident assigned.");
                alert.showAndWait();
            }
        });
    }

    @Override
    protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            ResourceModel resource = getTableView().getItems().get(getIndex());
            Long incidentId = resource.getIncidentId();
            viewBtn.setText(incidentId == null ? "-" : String.valueOf(incidentId));
            setGraphic(viewBtn);
        }
    }
});

    
    table.setItems(data);
    table.getColumns().addAll(
        col("ID", "id", 50),
        col("Type", "type", 100),
        col("Quantity", "quantity", 100),
        col("Location", "location", 120),
        col("Description", "description", 200)
    );
    table.getColumns().add(incidentCol); 
    
    
    
    Button back = new Button("Back");
    back.setOnAction(e -> new DmsUserApp().start(stage));

    VBox root = new VBox(10, table, back);
    root.setPadding(new Insets(12));

    stage.setScene(new Scene(root, 800, 600));
    stage.setTitle("Available Resources");

    refresh(data, backend);
  }

  private static void refresh(ObservableList<ResourceModel> data, Backend backend) {
    try {
      List<ResourceModel> resources = backend.listResources();
      data.setAll(resources);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static TableColumn<ResourceModel, String> col(String title, String prop, int width) {
    TableColumn<ResourceModel, String> col = new TableColumn<>(title);
    col.setCellValueFactory(new PropertyValueFactory<>(prop));
    col.setPrefWidth(width);
    return col;
  }
}
