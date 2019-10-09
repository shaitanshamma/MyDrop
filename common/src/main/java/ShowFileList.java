import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class ShowFileList {

    List<String> list = new ArrayList<>();

    public List<String> createTable(String path) {

        Path file = Paths.get(path);
        try {
            Files.walkFileTree(file, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    list.add(file.getFileName().toString());
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

//    Platform.runLater(new Runnable() {
//        @Override
//        public void run() {
//            clientList.getItems().clear();
//            for (int i = 1; i < tokens.length; i++) {
//                clientList.getItems().add(tokens[i]);
//            }
//        }
//    });

        return list;
    }

}
