package dms.admin.app;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class DmsAdminApp extends Application {
  private final Backend backend = new Backend("http://localhost:8080");
  private Stage primaryStage;

  @Override
  public void start(Stage stage) {
    this.primaryStage = stage;
    showLoginScreen();
  }

  public static void showDashboard(Stage stage, Backend backend) {
  Label welcome = new Label("🧭 Disaster Management Dashboard");
  welcome.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

  Button alertsBtn    = new Button("🚨 Manage Alerts");
  Button incidentsBtn = new Button("🧯 Manage Incidents");
  Button sheltersBtn  = new Button("🏠 Manage Shelters");
  Button resourcesBtn = new Button("🔧 Manage Resources");
  Button usersBtn     = new Button("👤 Manage Users");
  Button sosBtn = new Button("🆘 Manage SOS");
  
  sosBtn.setOnAction(e -> SosScreen.show(stage, backend));
  alertsBtn.setOnAction(e -> AlertsScreen.show(stage, backend));
  incidentsBtn.setOnAction(e -> IncidentsScreen.show(stage, backend));
  sheltersBtn.setOnAction(e -> SheltersScreen.show(stage, backend));
  resourcesBtn.setOnAction(e -> ResourcesScreen.show(stage, backend));
  usersBtn.setOnAction(e -> UsersScreen.show(stage, backend));

  VBox root = new VBox(15, welcome, alertsBtn, incidentsBtn, sheltersBtn, resourcesBtn, usersBtn, sosBtn);
  root.setPadding(new Insets(24));
  Scene scene = new Scene(root, 400, 400);
  scene.getStylesheets().add(DmsAdminApp.class.getResource("style.css").toExternalForm());

  stage.setScene(scene);
  stage.setTitle("DMS Dashboard");
}

  private void showLoginScreen() {
    TextField email = new TextField();    email.setPromptText("Email");
    PasswordField password = new PasswordField(); password.setPromptText("Password");
    Button loginBtn = new Button("Login");
    Label loginStatus = new Label();

    loginBtn.setOnAction(e -> {
      try {
        String response = backend.login(email.getText(), password.getText());
        if (response.contains("successful")) {
          loginStatus.setText("Login successful");
          showDashboard(); // ✅ Only go to dashboard on real success
        } else {
          loginStatus.setText("Login failed: " + response);
        }
      } catch (Exception ex) {
        loginStatus.setText("Login error: " + ex.getMessage());
      }
    });

    VBox loginBox = new VBox(10, email, password, loginBtn, loginStatus);
    loginBox.setPadding(new Insets(16));
    Scene scene = new Scene(loginBox, 400, 220);
    scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
    primaryStage.setScene(scene);
    primaryStage.setTitle("DMS Admin Login");
    primaryStage.show();
  }

  private void showDashboard() {
    Label welcome = new Label("🧭 Disaster Management Dashboard");
    welcome.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

    Button alertsBtn    = new Button("🚨 Manage Alerts");
    Button incidentsBtn = new Button("🧯 Manage Incidents");
    Button sheltersBtn  = new Button("🏠 Manage Shelters");
    Button resourcesBtn = new Button("🔧 Manage Resources");
    Button usersBtn     = new Button("👤 Manage Users");
    Button sosBtn = new Button("🆘 Manage SOS");
    
    sosBtn.setOnAction(e -> SosScreen.show(primaryStage, backend));
    alertsBtn.setOnAction(e -> AlertsScreen.show(primaryStage, backend));
    incidentsBtn.setOnAction(e -> IncidentsScreen.show(primaryStage, backend));
    sheltersBtn.setOnAction(e -> SheltersScreen.show(primaryStage, backend));
    resourcesBtn.setOnAction(e -> ResourcesScreen.show(primaryStage, backend));
    usersBtn.setOnAction(e -> UsersScreen.show(primaryStage, backend));

    VBox root = new VBox(15, welcome, alertsBtn, incidentsBtn, sheltersBtn, resourcesBtn, usersBtn, sosBtn);
    root.setPadding(new Insets(24));
    Scene scene = new Scene(root, 400, 400);
    scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

    primaryStage.setScene(scene);
    primaryStage.setTitle("DMS Dashboard");
  }

  public static void main(String[] args) {
    launch(args);
  }
}
