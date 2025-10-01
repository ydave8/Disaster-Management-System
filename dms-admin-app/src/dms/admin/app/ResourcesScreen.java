package dms.admin.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ResourcesScreen {
  public static void show(Stage stage, Backend backend) {
    TextField type = new TextField(); type.setPromptText("Type");
    TextField location = new TextField(); location.setPromptText("Location");
    TextField desc = new TextField(); desc.setPromptText("Description");
    TextField quantity = new TextField(); quantity.setPromptText("Quantity");
    ComboBox<IncidentModel> incidentCombo = new ComboBox<>();
    incidentCombo.setPromptText("Link to Incident");
    incidentCombo.setPrefWidth(200);
    Label msg = new Label();

    Button create = new Button("Create Resource");
    Button update = new Button("Update Resource"); update.setDisable(true);
    Button back = new Button("Back");

    TableView<ResourceModel> table = new TableView<>();
    ObservableList<ResourceModel> data = FXCollections.observableArrayList();
    table.setItems(data);
    table.getColumns().addAll(
      col("ID", "id", 40),
      col("Type", "type", 100),
      col("Location", "location", 100),
      col("Description", "description", 150),
      col("Quantity", "quantity", 80)
    );
    
    refresh(data, backend);
    
    try {
  incidentCombo.getItems().addAll(backend.listIncidents());
} catch (Exception ex) {
  ex.printStackTrace();  // or show error
}


//    table.getSelectionModel().selectedItemProperty().addListener((obs, o, i) -> {
//      if (i != null) {
//        type.setText(i.getType());
//        location.setText(i.getLocation());
//        desc.setText(i.getDescription());
//        quantity.setText(String.valueOf(i.getQuantity()));
//        update.setUserData(i.getId());
//        create.setDisable(true);
//        update.setDisable(false);
//      }
//    });

table.getSelectionModel().selectedItemProperty().addListener((obs, o, i) -> {
  if (i != null) {
    type.setText(i.getType());
    location.setText(i.getLocation());
    desc.setText(i.getDescription());
    quantity.setText(String.valueOf(i.getQuantity()));
    update.setUserData(i.getId());
    create.setDisable(true);
    update.setDisable(false);

    // Set incident dropdown
    for (IncidentModel inc : incidentCombo.getItems()) {
      if (inc.getId() == i.getIncidentId()) {
        incidentCombo.setValue(inc);
        break;
      }
    }
  }
});


    create.setOnAction(e -> {
  try {
    IncidentModel selectedIncident = incidentCombo.getValue();
    if (selectedIncident == null) {
      msg.setText("❌ Please select an incident.");
      return;
    }

    backend.createResource(
      desc.getText(),
      location.getText(),
      Integer.parseInt(quantity.getText()),
      type.getText(),
      selectedIncident.getId()
    );
    msg.setText("✅ Created!");
    refresh(data, backend);
  } catch (Exception ex) {
    msg.setText("❌ " + ex.getMessage());
  }
});

    update.setOnAction(e -> {
  try {
    long id = (long) update.getUserData();
    IncidentModel selectedIncident = incidentCombo.getValue();
    if (selectedIncident == null) {
      msg.setText("❌ Please select an incident.");
      return;
    }

    backend.updateResource(
      id,
      desc.getText(),
      location.getText(),
      Integer.parseInt(quantity.getText()),
      type.getText(),
      selectedIncident.getId()
    );
    msg.setText("✅ Updated!");
    refresh(data, backend);
    create.setDisable(false);
    update.setDisable(true);
  } catch (Exception ex) {
    msg.setText("❌ " + ex.getMessage());
  }
});

//    create.setOnAction(e -> {
//      try {
//        backend.createResource(desc.getText(), location.getText(),
//          Integer.parseInt(quantity.getText()), type.getText());
//        
//        msg.setText("✅ Created!");
//        refresh(data, backend);
//      } catch (Exception ex) {
//        msg.setText("❌ " + ex.getMessage());
//      }
//    });

//    update.setOnAction(e -> {
//      try {
//        long id = (long) update.getUserData();
//        backend.updateResource(id, desc.getText(), location.getText(),
//          Integer.parseInt(quantity.getText()), type.getText());
//        msg.setText("✅ Updated!");
//        refresh(data, backend);
//        create.setDisable(false);
//        update.setDisable(true);
//      } catch (Exception ex) {
//        msg.setText("❌ " + ex.getMessage());
//      }
//    });

    Button delete = new Button("Delete");
    delete.setOnAction(e -> {
      ResourceModel selected = table.getSelectionModel().getSelectedItem();
      if (selected != null) {
        try {
          backend.deleteResource(selected.getId());
          msg.setText("✅ Deleted!");
          refresh(data, backend);
        } catch (Exception ex) {
          msg.setText("❌ " + ex.getMessage());
        }
      }
    });

    back.setOnAction(e -> AdminDashboard.show(stage, backend));

    GridPane form = new GridPane();
    form.setHgap(8); form.setVgap(8); form.setPadding(new Insets(10));
    form.add(type, 0, 0); form.add(location, 1, 0);
    form.add(desc, 0, 1); form.add(quantity, 1, 1);
    form.add(create, 0, 2); form.add(update, 1, 2); form.add(delete, 2, 2);
    form.add(msg, 1, 3, 2, 1);
    form.add(incidentCombo, 2, 0);

    VBox root = new VBox(12, form, table, back);
    root.setPadding(new Insets(14));

    stage.setScene(new Scene(root, 800, 600));
    stage.setTitle("Manage Resources");
  }

  private static void refresh(ObservableList<ResourceModel> data, Backend backend) {
    try {
      data.setAll(backend.listResources());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private static TableColumn<ResourceModel, String> col(String title, String prop, int width) {
    TableColumn<ResourceModel, String> col = new TableColumn<>(title);
    col.setCellValueFactory(new PropertyValueFactory<>(prop));
    col.setPrefWidth(width);
    return col;
  }
}
