import java.util.List;


public class FileList extends AbstractMessage {

    private List<String> serverFileList;

    public FileList(List<String> fileList) {
        this.serverFileList = fileList;
    }

    public List<String> getServerFileList() {
        return serverFileList;
    }
}
