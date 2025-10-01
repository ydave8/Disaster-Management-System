// RegisterScreen.java
package dms.user.app;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RegisterScreen {
  public static void show(Stage stage, BackendUser backend) {
    TextField email = new TextField(); email.setPromptText("Email");
    TextField name  = new TextField(); name.setPromptText("Name");
    PasswordField pass = new PasswordField(); pass.setPromptText("Password");
    Label status = new Label();
    Button registerBtn = new Button("Register");

    registerBtn.setOnAction(e -> {
      try {
        String res = backend.register(email.getText(), name.getText(), pass.getText());
        status.setText("✅ Registered: " + res);
        OtpScreen.show(stage, backend, email.getText()); // go to OTP screen
      } catch (Exception ex) {
        status.setText("❌ Error: " + ex.getMessage());
      }
    });

    VBox root = new VBox(10, email, name, pass, registerBtn, status);
    root.setPadding(new Insets(20));
    stage.setScene(new Scene(root, 400, 250));
    stage.setTitle("User Registration");
  }
}
