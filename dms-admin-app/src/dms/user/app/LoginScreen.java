// LoginScreen.java
package dms.user.app;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginScreen {
  public static void show(Stage stage, BackendUser backend) {
    TextField email = new TextField(); email.setPromptText("Email");
    PasswordField pass = new PasswordField(); pass.setPromptText("Password");
    Label status = new Label();
    Button loginBtn = new Button("Login");

    loginBtn.setOnAction(e -> {
      try {
        String res = backend.login(email.getText(), pass.getText());
        if (res.contains("successful")) {
          status.setText("✅ Login ok");
          UserDashboard.show(stage, backend);
        } else {
          status.setText("❌ " + res);
        }
      } catch (Exception ex) {
        status.setText("❌ Error: " + ex.getMessage());
      }
    });

    VBox root = new VBox(10, email, pass, loginBtn, status);
    root.setPadding(new Insets(20));
    stage.setScene(new Scene(root, 350, 200));
    stage.setTitle("User Login");
  }
}
