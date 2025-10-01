
package dms.admin.app;
import javafx.stage.Stage;

public class AdminDashboard {
  public static void show(Stage stage, Backend backend) {
    DmsAdminApp app = new DmsAdminApp();
    app.showDashboard(stage, backend); // you’ll need to make this method public static
  }
}
