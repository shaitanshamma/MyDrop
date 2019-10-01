import java.util.List;


public class FileList extends AbstractMessage {
    List<String> serfilesList;

    public FileList(List<String> serfilesList) {
        this.serfilesList = serfilesList;
    }

   public List<String> getSerfilesList() {
        return serfilesList;
    }
}
