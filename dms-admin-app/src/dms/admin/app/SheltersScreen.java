package dms.admin.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class SheltersScreen {
  public static void show(Stage stage, Backend backend) {
    TextField name = new TextField(); name.setPromptText("Name");
    TextField address = new TextField(); address.setPromptText("Address");
    TextField capacity = new TextField(); capacity.setPromptText("Capacity");
    TextField lat = new TextField(); lat.setPromptText("Latitude");
    TextField lng = new TextField(); lng.setPromptText("Longitude");
    ComboBox<IncidentModel> incidentCombo = new ComboBox<>();
    incidentCombo.setPromptText("Link to Incident");
    incidentCombo.setPrefWidth(200);
    Label msg = new Label();

    Button create = new Button("Create Shelter");
    Button update = new Button("Update Shelter"); update.setDisable(true);
    Button back = new Button("Back");

    TableView<ShelterModel> table = new TableView<>();
    ObservableList<ShelterModel> data = FXCollections.observableArrayList();
    table.setItems(data);
    table.getColumns().addAll(
      col("ID", "id", 40),
      col("Name", "name", 100),
      col("Address", "address", 180),
      col("Capacity", "capacity", 80),
      col("Lat", "lat", 80),
      col("Lng", "lng", 80)
    );

    refresh(data, backend);
    
    try {
  incidentCombo.getItems().addAll(backend.listIncidents());
} catch (Exception ex) {
  ex.printStackTrace(); // or show error
}

//    table.getSelectionModel().selectedItemProperty().addListener((obs, o, i) -> {
//      if (i != null) {
//        name.setText(i.getName());
//        address.setText(i.getAddress());
//        capacity.setText(String.valueOf(i.getCapacity()));
//        lat.setText(String.valueOf(i.getLat()));
//        lng.setText(String.valueOf(i.getLng()));
//        update.setUserData(i.getId());
//        create.setDisable(true);
//        update.setDisable(false);
//      }
//    });


table.getSelectionModel().selectedItemProperty().addListener((obs, o, i) -> {
  if (i != null) {
    name.setText(i.getName());
    address.setText(i.getAddress());
    capacity.setText(String.valueOf(i.getCapacity()));
    lat.setText(String.valueOf(i.getLat()));
    lng.setText(String.valueOf(i.getLng()));
    update.setUserData(i.getId());
    create.setDisable(true);
    update.setDisable(false);

    // Set incident if linked
    for (IncidentModel inc : incidentCombo.getItems()) {
      if (inc.getId() == i.getIncidentId()) {
        incidentCombo.setValue(inc);
        break;
      }
    }
  }
});


//    create.setOnAction(e -> {
//      try {
//        backend.createShelter(name.getText(), address.getText(),
//          Integer.parseInt(capacity.getText()),
//          Double.parseDouble(lat.getText()), Double.parseDouble(lng.getText()));
//        msg.setText("✅ Created!");
//        refresh(data, backend);
//      } catch (Exception ex) {
//        msg.setText("❌ " + ex.getMessage());
//      }
//    });

create.setOnAction(e -> {
  try {
    IncidentModel selectedIncident = incidentCombo.getValue();
    if (selectedIncident == null) {
      msg.setText("❌ Please select an incident.");
      return;
    }

    backend.createShelter(
      name.getText(),
      address.getText(),
      Integer.parseInt(capacity.getText()),
      Double.parseDouble(lat.getText()),
      Double.parseDouble(lng.getText()),
      selectedIncident.getId()
    );
    msg.setText("✅ Created!");
    refresh(data, backend);
  } catch (Exception ex) {
    msg.setText("❌ " + ex.getMessage());
  }
});

//    update.setOnAction(e -> {
//      try {
//        long id = (long) update.getUserData();
//        backend.updateShelter(id, name.getText(), address.getText(),
//          Integer.parseInt(capacity.getText()),
//          Double.parseDouble(lat.getText()), Double.parseDouble(lng.getText()));
//        msg.setText("✅ Updated!");
//        refresh(data, backend);
//        create.setDisable(false);
//        update.setDisable(true);
//      } catch (Exception ex) {
//        msg.setText("❌ " + ex.getMessage());
//      }
//    });

update.setOnAction(e -> {
  try {
    long id = (long) update.getUserData();
    IncidentModel selectedIncident = incidentCombo.getValue();
    if (selectedIncident == null) {
      msg.setText("❌ Please select an incident.");
      return;
    }

    backend.updateShelter(
      id,
      name.getText(),
      address.getText(),
      Integer.parseInt(capacity.getText()),
      Double.parseDouble(lat.getText()),
      Double.parseDouble(lng.getText()),
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


    Button delete = new Button("Delete");
    delete.setOnAction(e -> {
      ShelterModel selected = table.getSelectionModel().getSelectedItem();
      if (selected != null) {
        try {
          backend.deleteShelter(selected.getId());
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
    form.add(name, 0, 0); form.add(address, 1, 0);
    form.add(capacity, 0, 1); form.add(lat, 1, 1); form.add(lng, 2, 1);
    form.add(create, 0, 2); form.add(update, 1, 2); form.add(delete, 2, 2);
    form.add(msg, 1, 3, 2, 1);
    form.add(incidentCombo, 2, 0);

    VBox root = new VBox(12, form, table, back);
    root.setPadding(new Insets(14));

    stage.setScene(new Scene(root, 800, 600));
    stage.setTitle("Manage Shelters");
  }

  private static void refresh(ObservableList<ShelterModel> data, Backend backend) {
    try {
      data.setAll(backend.listShelters());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private static TableColumn<ShelterModel, String> col(String title, String prop, int width) {
    TableColumn<ShelterModel, String> col = new TableColumn<>(title);
    col.setCellValueFactory(new PropertyValueFactory<>(prop));
    col.setPrefWidth(width);
    return col;
  }
}
