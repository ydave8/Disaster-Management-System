package dms.admin.app;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AlertsScreen {
  public static void show(Stage stage, Backend backend) {
    // Create form fields
    TextField title = new TextField();  title.setPromptText("Title");
    TextArea  msg   = new TextArea();   msg.setPromptText("Message");
    TextField area  = new TextField();  area.setPromptText("Area");
    ComboBox<String> severity = new ComboBox<>();
    severity.getItems().addAll("LOW","MEDIUM","HIGH","CRITICAL");
    TextField expires = new TextField(); expires.setPromptText("ExpiresAt (ISO-8601)");
    Label status = new Label();

    // Submit and back buttons
    Button send = new Button("Create Alert");
    Button update = new Button("Update Alert"); update.setDisable(true);
    Button back = new Button("Back");

    // Alerts table
    TableView<AlertModel> table = new TableView<>();
    ObservableList<AlertModel> data = FXCollections.observableArrayList();
    table.setItems(data);

    table.getColumns().addAll(
      col("ID", "id", 40),
      col("Title", "title", 100),
      col("Area", "area", 80),
      col("Severity", "severity", 80),
      col("Expires", "expiresAt", 140),
      col("Message", "message", 160)
    );

    refreshAlerts(data, backend);

    // Selecting a row fills the form
    table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
      if (newSel != null) {
        title.setText(newSel.getTitle());
        msg.setText(newSel.getMessage());
        area.setText(newSel.getArea());
        severity.setValue(newSel.getSeverity());
        expires.setText(newSel.getExpiresAt());
        send.setDisable(true);
        update.setDisable(false);
        update.setUserData(newSel.getId());
      }
    });

    send.setOnAction(e -> {
      try {
    String sev = severity.getValue();
    if (sev == null || sev.trim().isEmpty()) sev = null;
    backend.createAlert(title.getText(), msg.getText(), area.getText(), sev, expires.getText());
    status.setText("✅ Alert created!");          
//        backend.createAlert(title.getText(), msg.getText(), area.getText(),
//          severity.getValue(), expires.getText());
//        status.setText("✅ Alert created!");
        refreshAlerts(data, backend);
      } catch (Exception ex) {
        status.setText("❌ Error: " + ex.getMessage());
      }
    });

    update.setOnAction(e -> {
      try {
//        int id = (int) update.getUserData();
        Long id = (Long) update.getUserData();
        String sev = severity.getValue();
        if (sev == null || sev.trim().isEmpty()) sev = null;
        backend.updateAlert(id, title.getText(), msg.getText(), area.getText(), sev, expires.getText());
        status.setText("✅ Alert updated!");
        refreshAlerts(data, backend);
        send.setDisable(false);
        update.setDisable(true);
      } catch (Exception ex) {
        status.setText("❌ Error: " + ex.getMessage());
      }
    });

    Button delete = new Button("Delete Selected");
    delete.setOnAction(e -> {
      AlertModel selected = table.getSelectionModel().getSelectedItem();
      if (selected != null) {
        try {
          backend.deleteAlert(selected.getId());
          status.setText("✅ Alert deleted!");
          refreshAlerts(data, backend);
        } catch (Exception ex) {
          status.setText("❌ Error: " + ex.getMessage());
        }
      }
    });

    back.setOnAction(e -> AdminDashboard.show(stage, backend));

    GridPane form = new GridPane();
    form.setHgap(8); form.setVgap(8); form.setPadding(new Insets(10));
    form.add(title, 0, 0); form.add(area, 1, 0);
    form.add(severity, 0, 1); form.add(expires, 1, 1);
    form.add(msg, 0, 2, 2, 1);
    form.add(send, 0, 3); form.add(update, 1, 3);
    form.add(delete, 0, 4); form.add(status, 1, 4);

    VBox root = new VBox(12, form, table, back);
    root.setPadding(new Insets(14));

    stage.setScene(new Scene(root, 750, 600));
    stage.setTitle("Manage Alerts");
  }
private static void refreshAlerts(ObservableList<AlertModel> data, Backend backend) {
  try {
    List<AlertModel> alerts = backend.listAlerts(); // already parsed
    data.setAll(alerts); // refresh TableView
  } catch (Exception ex) {
    ex.printStackTrace();
  }
}


  private static TableColumn<AlertModel, String> col(String title, String prop, int width) {
    TableColumn<AlertModel, String> col = new TableColumn<>(title);
    col.setCellValueFactory(new PropertyValueFactory<>(prop));
    col.setPrefWidth(width);
    return col;
  }
}
