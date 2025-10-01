//package dms.admin.app;
//
//import javafx.geometry.Insets;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.layout.*;
//import javafx.stage.Stage;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.scene.control.cell.PropertyValueFactory;
//import java.util.List;
//
//public class IncidentsScreen {
//  public static void show(Stage stage, Backend backend) {
//    TextField type = new TextField(); type.setPromptText("Type");
//    TextArea desc = new TextArea(); desc.setPromptText("Description");
//    TextField lat = new TextField(); lat.setPromptText("Latitude");
//    TextField lng = new TextField(); lng.setPromptText("Longitude");
//    ComboBox<String> status = new ComboBox<>();
//    status.getItems().addAll("OPEN", "ACK", "CLOSED");
//    Label msg = new Label();
//
//    Button create = new Button("Create Incident");
//    Button update = new Button("Update Incident"); update.setDisable(true);
//    Button back = new Button("Back");
//
//    TableView<IncidentModel> table = new TableView<>();
//    ObservableList<IncidentModel> data = FXCollections.observableArrayList();
//    table.setItems(data);
//    table.getColumns().addAll(
//      col("ID", "id", 40),
//      col("Type", "type", 80),
//      col("Status", "status", 80),
//      col("Lat", "lat", 80),
//      col("Lng", "lng", 80),
//      col("Description", "description", 200)
//    );
//
//    table.getSelectionModel().selectedItemProperty().addListener((obs, o, i) -> {
//      if (i != null) {
//        type.setText(i.getType());
//        desc.setText(i.getDescription());
//        lat.setText(String.valueOf(i.getLat()));
//        lng.setText(String.valueOf(i.getLng()));
//        status.setValue(i.getStatus());
//        update.setUserData(i.getId());
//        create.setDisable(true);
//        update.setDisable(false);
//      }
//    });
//
//    create.setOnAction(e -> {
//      try {
//        backend.createIncident(
//          desc.getText(),
//          Double.parseDouble(lat.getText()),
//          Double.parseDouble(lng.getText()),
//          status.getValue(),
//          type.getText()
//        );
//
//        msg.setText("✅ Created!");
//        refresh(data, backend);
//      } catch (Exception ex) {
//        msg.setText("❌ " + ex.getMessage());
//      }
//    });
//
//    update.setOnAction(e -> {
//      try {
//        long id = (long) update.getUserData();
//        backend.updateIncident(
//          id,
//          desc.getText(),
//          Double.parseDouble(lat.getText()),
//          Double.parseDouble(lng.getText()),
//          status.getValue(),
//          type.getText()
//        );
//
//        msg.setText("✅ Updated!");
//        refresh(data, backend);
//        create.setDisable(false);
//        update.setDisable(true);
//      } catch (Exception ex) {
//        msg.setText("❌ " + ex.getMessage());
//      }
//    });
//
//    Button delete = new Button("Delete");
//    delete.setOnAction(e -> {
//      IncidentModel selected = table.getSelectionModel().getSelectedItem();
//      if (selected != null) {
//        try {
//          backend.deleteIncident(selected.getId());
//          msg.setText("✅ Deleted!");
//          refresh(data, backend);
//        } catch (Exception ex) {
//          msg.setText("❌ " + ex.getMessage());
//        }
//      }
//    });
//
//    back.setOnAction(e -> AdminDashboard.show(stage, backend));
//
//    GridPane form = new GridPane();
//    form.setHgap(8); form.setVgap(8); form.setPadding(new Insets(10));
//    form.add(type, 0, 0); form.add(status, 1, 0);
//    form.add(lat, 0, 1); form.add(lng, 1, 1);
//    form.add(desc, 0, 2, 2, 1);
//    form.add(create, 0, 3); form.add(update, 1, 3);
//    form.add(delete, 0, 4); form.add(msg, 1, 4);
//
//    VBox root = new VBox(12, form, table, back);
//    root.setPadding(new Insets(14));
//
//    stage.setScene(new Scene(root, 780, 600));
//    stage.setTitle("Manage Incidents");
//  }
//
//  private static void refresh(ObservableList<IncidentModel> data, Backend backend) {
//    try {
//      data.setAll(backend.listIncidents());
//    } catch (Exception ex) {
//      ex.printStackTrace();
//    }
//  }
//
//  private static TableColumn<IncidentModel, String> col(String title, String prop, int width) {
//    TableColumn<IncidentModel, String> col = new TableColumn<>(title);
//    col.setCellValueFactory(new PropertyValueFactory<>(prop));
//    col.setPrefWidth(width);
//    return col;
//  }
//}

package dms.admin.app;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class IncidentsScreen {
  public static void show(Stage stage, Backend backend) {
    TextField type = new TextField(); type.setPromptText("Type");
    TextArea desc = new TextArea(); desc.setPromptText("Description");
    TextField lat = new TextField(); lat.setPromptText("Latitude");
    TextField lng = new TextField(); lng.setPromptText("Longitude");
    ComboBox<String> status = new ComboBox<>();
    status.getItems().addAll("OPEN", "ACK", "CLOSED");
    Label msg = new Label();

    Button create = new Button("Create Incident");
    Button update = new Button("Update Incident"); update.setDisable(true);
    Button delete = new Button("Delete");
    Button back = new Button("Back");

    TableView<IncidentModel> table = new TableView<>();
    ObservableList<IncidentModel> data = FXCollections.observableArrayList();
    table.setItems(data);
    table.getColumns().addAll(
      col("ID", "id", 40),
      col("Type", "type", 80),
      col("Status", "status", 80),
      col("Lat", "lat", 80),
      col("Lng", "lng", 80),
      col("Description", "description", 200)
    );

    refresh(data, backend);
    
    table.getSelectionModel().selectedItemProperty().addListener((obs, o, i) -> {
      if (i != null) {
        type.setText(i.getType());
        desc.setText(i.getDescription());
        lat.setText(String.valueOf(i.getLat()));
        lng.setText(String.valueOf(i.getLng()));
        status.setValue(i.getStatus());
        update.setUserData(i.getId());
        create.setDisable(true);
        update.setDisable(false);
      }
    });

    create.setOnAction(e -> {
      try {
        backend.createIncident(
          desc.getText(),
          Double.parseDouble(lat.getText()),
          Double.parseDouble(lng.getText()),
          status.getValue(),
          type.getText()
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
        backend.updateIncident(
          id,
          desc.getText(),
          Double.parseDouble(lat.getText()),
          Double.parseDouble(lng.getText()),
          status.getValue(),
          type.getText()
        );

        msg.setText("✅ Updated!");
        refresh(data, backend);
        create.setDisable(false);
        update.setDisable(true);
      } catch (Exception ex) {
        msg.setText("❌ " + ex.getMessage());
      }
    });

    delete.setOnAction(e -> {
      IncidentModel selected = table.getSelectionModel().getSelectedItem();
      if (selected != null) {
        try {
          backend.deleteIncident(selected.getId());
          msg.setText("✅ Deleted!");
          refresh(data, backend);
        } catch (Exception ex) {
          msg.setText("❌ " + ex.getMessage());
        }
      }
    });

    // 🆕 Download Report Button
    Button downloadReport = new Button("⬇ Download Selected Report");
    downloadReport.setOnAction(e -> {
      IncidentModel selected = table.getSelectionModel().getSelectedItem();
      if (selected == null) {
        msg.setText("⚠ Please select an incident.");
        return;
      }

      try {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName("incident-" + selected.getId() + ".pdf");
        File file = fileChooser.showSaveDialog(stage);
        if (file == null) return;

        String urlStr = backend.getBase() + "/api/v1/incidents/" + selected.getId() + "/report";
        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
        conn.setRequestMethod("GET");

        try (InputStream in = conn.getInputStream(); FileOutputStream out = new FileOutputStream(file)) {
          byte[] buffer = new byte[4096];
          int bytesRead;
          while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
          }
          msg.setText("✅ Report saved to: " + file.getAbsolutePath());
        }
      } catch (Exception ex) {
        ex.printStackTrace();
        msg.setText("❌ Error downloading: " + ex.getMessage());
      }
    });

    back.setOnAction(e -> AdminDashboard.show(stage, backend));

    GridPane form = new GridPane();
    form.setHgap(8); form.setVgap(8); form.setPadding(new Insets(10));
    form.add(type, 0, 0); form.add(status, 1, 0);
    form.add(lat, 0, 1); form.add(lng, 1, 1);
    form.add(desc, 0, 2, 2, 1);
    form.add(create, 0, 3); form.add(update, 1, 3);
    form.add(delete, 0, 4); form.add(msg, 1, 4);

    VBox root = new VBox(12, form, table, downloadReport, back);
    root.setPadding(new Insets(14));

    stage.setScene(new Scene(root, 780, 600));
    stage.setTitle("Manage Incidents");
  }

  private static void refresh(ObservableList<IncidentModel> data, Backend backend) {
    try {
      data.setAll(backend.listIncidents());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private static TableColumn<IncidentModel, String> col(String title, String prop, int width) {
    TableColumn<IncidentModel, String> col = new TableColumn<>(title);
    col.setCellValueFactory(new PropertyValueFactory<>(prop));
    col.setPrefWidth(width);
    return col;
  }
}
