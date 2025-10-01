//package dms.user.app;
//
//import javafx.application.Application;
//import javafx.geometry.Insets;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.layout.VBox;
//import javafx.stage.Stage;
//import dms.admin.app.Backend;
//
//public class DmsUserApp extends Application {
//    private final Backend backend = new Backend("http://localhost:8080"); // reuse your backend
//    private Stage primaryStage;
//
//    @Override
//    public void start(Stage stage) {
//        this.primaryStage = stage;
//        showLanding(stage, backend);
//    }
//
//    public static void showLanding(Stage stage, Backend backend) {
//        Label title = new Label("👤 User Portal - Disaster Management");
//        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
//
//        Button loginBtn = new Button("Login");
//        Button registerBtn = new Button("Register");
//
//        loginBtn.setOnAction(e -> LoginScreen.show(stage, backend));
//        registerBtn.setOnAction(e -> RegisterScreen.show(stage, backend));
//
//        VBox root = new VBox(15, title, loginBtn, registerBtn);
//        root.setPadding(new Insets(20));
//
//        Scene scene = new Scene(root, 400, 250);
//        stage.setScene(scene);
//        stage.setTitle("DMS User App");
//        stage.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}

package dms.user.app;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DmsUserApp extends Application {
  private final BackendUser backend = new BackendUser("http://localhost:8080");

  @Override
  public void start(Stage stage) {
    showLanding(stage);
  }

  private void showLanding(Stage stage) {
    Button loginBtn = new Button("Login");
    Button registerBtn = new Button("Register");

    loginBtn.setOnAction(e -> LoginScreen.show(stage, backend));
    registerBtn.setOnAction(e -> RegisterScreen.show(stage, backend));

    VBox root = new VBox(12, loginBtn, registerBtn);
    root.setPadding(new Insets(20));
    stage.setScene(new Scene(root, 300, 200));
    stage.setTitle("DMS User Portal");
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
