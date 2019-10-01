import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LogOnWindowController implements Initializable {


    @FXML
    TextField userTextField;

    @FXML
    PasswordField pwBox;

    @FXML
    AnchorPane root;

    @FXML
    public void connect(ActionEvent actionEvent) throws IOException {
        NettyNetwork.currentChannel.writeAndFlush(new ClientConnection(userTextField.getText(), pwBox.getText()));

        if (!ClientDownload.isOk()) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/CloudWindow.fxml"));
//            AnchorPane Loader = FXMLLoader.load(getClass().getResource("/CloudWindow"));
//            root.getChildren().setAll(Loader);
            Parent root;
            Stage primaryStage = new Stage();
            root = fxmlLoader.load();
            primaryStage.setTitle("Box Client");
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
            System.out.println("sdsdsd");
        }
    }

    public void initialize(URL location, ResourceBundle resources) {
//      //  LogOnWindowController controller = new LogOnWindowController();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    NettyNetwork.getInstance().start(CloudWindowController.class.newInstance());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
