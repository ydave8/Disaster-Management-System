package dms.admin.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class UsersScreen {
  public static void show(Stage stage, Backend backend) {
    TextField email = new TextField(); email.setPromptText("Email");
    TextField name  = new TextField(); name.setPromptText("Name");
    ComboBox<String> role = new ComboBox<>();
    role.getItems().addAll("ADMIN", "USER");
    Label status = new Label();

    Button create = new Button("Create User");
    Button update = new Button("Update User"); update.setDisable(true);
    Button delete = new Button("Delete User");
    Button back = new Button("Back");

    TableView<UserModel> table = new TableView<>();
    ObservableList<UserModel> data = FXCollections.observableArrayList();
    table.setItems(data);

    table.getColumns().addAll(
        col("ID", "id", 40),
        col("Email", "email", 160),
        col("Name", "name", 120),
        col("Role", "role", 80),
        col("Verified", "verified", 80)
    );

    refreshUsers(data, backend);

    table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
      if (newSel != null) {
        email.setText(newSel.getEmail());
        name.setText(newSel.getName());
        role.setValue(newSel.getRole());
        create.setDisable(true);
        update.setDisable(false);
        update.setUserData(newSel.getId());
      }
    });

    create.setOnAction(e -> {
      try {
        backend.createUser(email.getText(), name.getText(), role.getValue());
        status.setText("✅ User created!");
        refreshUsers(data, backend);
      } catch (Exception ex) {
        status.setText("❌ Error: " + ex.getMessage());
      }
    });

    update.setOnAction(e -> {
      try {
        long id = (long) update.getUserData();
        backend.updateUser(id, name.getText(), role.getValue());
        status.setText("✅ User updated!");
        refreshUsers(data, backend);
        create.setDisable(false);
        update.setDisable(true);
      } catch (Exception ex) {
        status.setText("❌ Error: " + ex.getMessage());
      }
    });

    delete.setOnAction(e -> {
      UserModel selected = table.getSelectionModel().getSelectedItem();
      if (selected != null) {
        try {
          backend.deleteUser(selected.getId());
          status.setText("✅ User deleted!");
          refreshUsers(data, backend);
        } catch (Exception ex) {
          status.setText("❌ Error: " + ex.getMessage());
        }
      }
    });

    back.setOnAction(e -> AdminDashboard.show(stage, backend));

    GridPane form = new GridPane();
    form.setHgap(8); form.setVgap(8); form.setPadding(new Insets(10));
    form.add(email, 0, 0); form.add(name, 1, 0);
    form.add(role, 0, 1); 
    form.add(create, 0, 2); form.add(update, 1, 2);
    form.add(delete, 0, 3); form.add(status, 1, 3);

    VBox root = new VBox(12, form, table, back);
    root.setPadding(new Insets(14));

    stage.setScene(new Scene(root, 700, 500));
    stage.setTitle("Manage Users");
  }

  private static void refreshUsers(ObservableList<UserModel> data, Backend backend) {
    try {
      List<UserModel> users = backend.listUsers();
      data.setAll(users);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private static <T> TableColumn<UserModel, T> col(String title, String prop, int width) {
    TableColumn<UserModel, T> col = new TableColumn<>(title);
    col.setCellValueFactory(new PropertyValueFactory<>(prop));
    col.setPrefWidth(width);
    return col;
  }
}
