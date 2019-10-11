public class FileRequest extends AbstractMessage {

    private String filename;
    private String command;

    public FileRequest(String filename, String command) {
        this.filename = filename;
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public String getFilename() {
        return filename;
    }
}
