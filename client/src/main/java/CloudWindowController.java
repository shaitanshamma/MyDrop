import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;

public class CloudWindowController implements Initializable {
    private FileSplitter fileSplitter;
    @FXML
    TextField tfFileName;

    @FXML
    ListView<String> filesList;

    @FXML
    ListView<String> serverFileList;

    private String fileName;

    private final int MAX_OBJC_SIZE = 500;

    private void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void refreshList() {
        selection();
        refreshLocalFilesList();
        refreshServerFilesList();
    }

    public void selection() {
        MultipleSelectionModel<String> langsSelectionModel = filesList.getSelectionModel();
        MultipleSelectionModel<String> langsSelectionModel2 = serverFileList.getSelectionModel();
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

    public void pressDellButton() {
        if (tfFileName.getLength() > 0) {
            if (Platform.isFxApplicationThread()) {
                try {
                    Files.delete(Paths.get("client_storage/" + tfFileName.getText()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                refreshLocalFilesList();
                tfFileName.clear();
            } else
                Platform.runLater(() -> {
                    try {
                        Files.delete(Paths.get("client_storage/" + tfFileName.getText()));
                        refreshLocalFilesList();
                        tfFileName.clear();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        }
    }


    public void refreshLocalFilesList() {
        filesList.getItems().clear();
        if (Platform.isFxApplicationThread()) {
            try {
                Files.list(Paths.get("client_storage/")).map(p -> p.getFileName().toString()).forEach(o -> filesList.getItems().add(o));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Platform.runLater(() -> {
                try {
                    Files.list(Paths.get("client_storage/")).map(p -> p.getFileName().toString()).forEach(o -> filesList.getItems().add(o));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void refreshServerFilesList() {
        if (Platform.isFxApplicationThread()) {
            NettyNetwork.currentChannel.writeAndFlush(new FileRequest("list", "update"));
        } else {
            Platform.runLater(() -> {
                NettyNetwork.currentChannel.writeAndFlush(new FileRequest("list", "update"));

            });
        }
    }


    public void pressDownloadButton() {
        if (Platform.isFxApplicationThread()) {

            NettyNetwork.currentChannel.writeAndFlush(new FileRequest(tfFileName.getText(), "down"));
        } else {
            Platform.runLater(() -> {
                NettyNetwork.currentChannel.writeAndFlush(new FileRequest(tfFileName.getText(), "down"));
                refreshServerFilesList();
                tfFileName.clear();
            });
        }
    }

    public void pressUploadButton() {

        String currentPath = "client_storage/" + tfFileName.getText();
        File file = new File(currentPath);
        if (file.length() < MAX_OBJC_SIZE) {
            fileSplitter.split(currentPath, 20);
            int count = fileSplitter.getCount() - 1;
            int part = count + 1;
            int parts = count;

            for (; part > 1; part--) {

                if (Platform.isFxApplicationThread()) {
                    try {
                        NettyNetwork.currentChannel.writeAndFlush(new FileMessage(Paths.get(currentPath + (part - 1) + ".sp"), part - 1, parts, tfFileName.getText()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            fileSplitter.removeTemp(currentPath, parts);
        }
        refreshServerFilesList();
    }

    public void pressDellAtServerButton() {
        if (Platform.isFxApplicationThread()) {
            NettyNetwork.currentChannel.writeAndFlush(new FileRequest(tfFileName.getText(), "delete"));
            refreshServerFilesList();
            tfFileName.clear();

        } else {
            Platform.runLater(() -> {
                NettyNetwork.currentChannel.writeAndFlush(new FileRequest(tfFileName.getText(), "delete"));
                refreshServerFilesList();
                tfFileName.clear();
            });
        }
    }

    public void refresh(List<String> list) {
        serverFileList.getItems().clear();
        if (Platform.isFxApplicationThread()) {
            System.out.println(serverFileList.getItems());
            serverFileList.getItems().addAll(list);
        } else {
            Platform.runLater(() -> serverFileList.getItems().addAll(list));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        NettyNetwork.getInstance().startHandler(this);
        refreshList();
        fileSplitter = new FileSplitter();
    }
}

