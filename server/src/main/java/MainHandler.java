import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class MainHandler extends ChannelHandlerAdapter {

    public MainHandler(String login) {
        this.login = login;
    }

    private final int MAX_OBJC_SIZE = 200;
    private FileSplitter fileSplitter = Server.getFileSplitter();
    private String login;
    private List<String> serverFileList = new ArrayList<>();
    private String currentPath;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        currentPath = "server_" + login + "_storage" + File.separator;
        try {
            if (msg == null) {
                return;
            }
            if (msg instanceof FileRequest) {
                FileRequest fr = (FileRequest) msg;
                File file = new File(currentPath + fr.getFilename());
                if (Files.exists(Paths.get(currentPath + fr.getFilename())) && fr.getCommand().equals("down")
                        && file.length() < MAX_OBJC_SIZE) {
                    FileMessage fm = new FileMessage(Paths.get(currentPath + fr.getFilename()), 0, 0, fr.getFilename());
                    ctx.writeAndFlush(fm);
                } else if (file.length() > MAX_OBJC_SIZE) {
                    splitFileToDownload(ctx, fr);
                } else if (Files.exists(Paths.get(currentPath + fr.getFilename())) && fr.getCommand().equals("delete")) {
                    Files.delete(Paths.get(currentPath + fr.getFilename()));
                    responseServerFiles(ctx);
                } else if (fr.getFilename().equals("list")) {
                    responseServerFiles(ctx);
                }
            } else if (msg instanceof FileMessage) {
                receiveUploadFiles((FileMessage) msg);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    private void receiveUploadFiles(FileMessage msg) throws IOException {
        FileMessage fm = msg;
        if (fm.part > 1) {
            if (!Files.exists(Paths.get(currentPath + fm.getFilename()))) {
                Files.write(Paths.get(currentPath + fm.getFilename()), fm.getData());
            }
        } else if (fm.part == 1) {
            Files.write(Paths.get(currentPath + fm.getFilename()), fm.getData());
            fileSplitter.join(currentPath + fm.fileFullName, currentPath + fm.fileFullName, fm.parts);
            fileSplitter.removeTemp(currentPath + fm.fileFullName, fm.parts);
        }
    }

    private void splitFileToDownload(ChannelHandlerContext ctx, FileRequest fr) throws IOException {
        fileSplitter.split(currentPath, 20);
        int count = fileSplitter.getCount() - 1;
        int part = count + 1;
        int parts = count;
        for (; part > 1; part--) {
            ctx.writeAndFlush(new FileMessage(Paths.get(currentPath + (part - 1) + ".sp"), part - 1, parts, fr.getFilename()));
        }
        fileSplitter.removeTemp(currentPath, parts);
    }

    private void responseServerFiles(ChannelHandlerContext ctx) throws IOException {
        serverFileList.clear();
        Files.list(Paths.get(currentPath)).map(p -> p.getFileName().toString()).forEach(o -> serverFileList.add(o));
        ctx.writeAndFlush(new FileList(serverFileList));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }
}

