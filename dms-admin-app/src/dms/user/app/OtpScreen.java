// OtpScreen.java
package dms.user.app;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class OtpScreen {
  public static void show(Stage stage, BackendUser backend, String email) {
    TextField otp = new TextField(); otp.setPromptText("Enter OTP");
    Label status = new Label();
    Button verifyBtn = new Button("Verify OTP");

    verifyBtn.setOnAction(e -> {
      try {
        String res = backend.verifyOtp(email, otp.getText());
        status.setText(res);
        if (res.contains("verified")) {
          LoginScreen.show(stage, backend); // go to login
        }
      } catch (Exception ex) {
        status.setText("❌ Error: " + ex.getMessage());
      }
    });

    VBox root = new VBox(10, otp, verifyBtn, status);
    root.setPadding(new Insets(20));
    stage.setScene(new Scene(root, 300, 180));
    stage.setTitle("OTP Verification");
  }
}
