public class FileRequest extends AbstractMessage {
    private String filename;

    public String getCommand() {
        return command;
    }

    private String command;

    public String getFilename() {
        return filename;
    }

    public FileRequest(String filename, String command) {
        this.filename = filename;
        this.command = command;
    }
}
