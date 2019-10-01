import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class CloudWindowController implements Initializable {
    @FXML
    TextField tfFileName;

    @FXML
    ListView<String> filesList;

    @FXML
    ListView<String> serfilesList;

    String fileName;


    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void refreshList(ActionEvent actionEvent) {
        selection();
        refreshLocalFilesList();
        refreshServerFilesList();

    }

    public void selection() {
        fileName = null;
        MultipleSelectionModel<String> langsSelectionModel = filesList.getSelectionModel();
        MultipleSelectionModel<String> langsSelectionModel2 = serfilesList.getSelectionModel();
        langsSelectionModel.selectedItemProperty().addListener(new ChangeListener<String>() {
            String str = " ";

            public void changed(ObservableValue<? extends String> changed, String oldValue, String newValue) {
                str = newValue;
                setFileName(str);
                tfFileName.clear();
                tfFileName.appendText(str);
                System.out.println("at client" + fileName);

            }
        });
        langsSelectionModel2.selectedItemProperty().addListener(new ChangeListener<String>() {
            String str = " ";

            public void changed(ObservableValue<? extends String> changed, String oldValue, String newValue) {
                str = newValue;
                setFileName(str);
                tfFileName.clear();
                tfFileName.appendText(str);
                System.out.println("at server " + fileName);
            }
        });
    }

    public void pressDellButton(ActionEvent actionEvent) throws IOException {
        if (tfFileName.getLength() > 0) {
            Files.delete(Paths.get("client_storage\\" + tfFileName.getText()));
            tfFileName.clear();
            refreshLocalFilesList();
        }
    }


    public void refreshLocalFilesList() {
        if (Platform.isFxApplicationThread()) {
            try {
                filesList.getItems().clear();
                Files.list(Paths.get("client_storage")).map(p -> p.getFileName().toString()).forEach(o -> filesList.getItems().add(o));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Platform.runLater(() -> {
                try {
                    filesList.getItems().clear();
                    Files.list(Paths.get("client_storage")).map(p -> p.getFileName().toString()).forEach(o -> filesList.getItems().add(o));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void refreshServerFilesList() {
        if (Platform.isFxApplicationThread()) {
            serfilesList.getItems().clear();
            NettyNetwork.currentChannel.writeAndFlush(new FileRequest("list", "delete"));
//            for (String s : ClientDownload.serverList) {
//                serfilesList.getItems().add(s);
//            }
        } else {
            Platform.runLater(() -> {
                serfilesList.getItems().clear();
                NettyNetwork.currentChannel.writeAndFlush(new FileRequest("list", "delete"));

            });
        }
    }


    public void pressDownKey() {
        NettyNetwork.currentChannel.writeAndFlush(new FileRequest(tfFileName.getText(), "down"));
        refreshLocalFilesList();
        refreshServerFilesList();
    }

    public void pressUploadKey() {
        try {
            NettyNetwork.currentChannel.writeAndFlush(new FileMessage(Paths.get("client_storage/" + tfFileName.getText())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        refreshServerFilesList();
        tfFileName.clear();
    }

    public void pressDellatServerButton(ActionEvent actionEvent) {
        NettyNetwork.currentChannel.writeAndFlush(new FileRequest(tfFileName.getText(), "delete"));
        refreshServerFilesList();
        tfFileName.clear();
    }

    public void getServerFileList() {
        NettyNetwork.currentChannel.writeAndFlush(new FileRequest("list", "delete"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NettyNetwork.getInstance().start(CloudWindowController.this);
            }
        }).start();
    }

}

