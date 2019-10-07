import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class LogOnWindowController implements Initializable {


    @FXML
    TextField userTextField;

    @FXML
    PasswordField pwBox;

    @FXML
    Label isOk;

    @FXML
    Button Button;

    @FXML
    public void connect() throws IOException {

        if (Platform.isFxApplicationThread()) {
            NettyNetwork.currentChannel.writeAndFlush(new ClientConnection(userTextField.getText(), pwBox.getText()));
        } else {
            Platform.runLater(() -> {
                        NettyNetwork.currentChannel.writeAndFlush(new ClientConnection(userTextField.getText(), pwBox.getText()));
                    }
            );
        }
    }

    public void changeWindow() throws IOException {
        Stage stage = (Stage) Button.getScene().getWindow();
        stage.close();
        FXMLLoader Loader = new FXMLLoader(getClass().getResource("/CloudWindow.fxml"));
        Parent root;
        Stage primaryStage = new Stage();
        root = Loader.load();
        primaryStage.setTitle("Box Client");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        System.out.println("sdsdsd");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    NettyNetwork.getInstance().start(CloudWindowController.class.newInstance(), LogOnWindowController.this);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
