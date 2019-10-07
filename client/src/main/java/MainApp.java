import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    public void start(Stage primaryStage) throws Exception {
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LogOnWindow.fxml"));
 //       FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LogOnWindow.fxml"));
//        Parent root = fxmlLoader.load();
        Parent root = FXMLLoader.load(getClass().getResource("LogOnWindow.fxml"));
        primaryStage.setTitle("Box Client");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
