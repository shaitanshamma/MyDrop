import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileMessage extends AbstractMessage {
    private String filename;
    private byte[] data;
    int part;
    String fileFullName;

    public String getFilename() {
        return filename;
    }

    public byte[] getData() {
        return data;
    }


    public FileMessage(Path path, int part, String fileFullName) throws IOException {
        this.part = part;
        filename = path.getFileName().toString();
        data = Files.readAllBytes(path);
        this.fileFullName = fileFullName;
    }
}
