import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class MainHandler extends ChannelInboundHandlerAdapter {

    public MainHandler(String login) {
        this.login = login;
    }
    int part=1;

    FileSplitter fileSplitter = Server.getFileSplitter();
    String login;
    List<String> serverFileList = new ArrayList<>();
    List<String> path = new ArrayList<>();
    String currentPath;
    Map<Channel, String> pathOn = new HashMap<>();
    final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client connected..." + login);
        //      channels.add(ctx.channel());
//        for (Channel channel : channels) {
//            System.out.println(channel.id());
//        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        currentPath = "server_" + login + "_storage/";
        channels.add(ctx.channel());
        System.out.println(ctx.channel().id() + "____" + login);
        try {
            if (msg == null) {
                return;
            }
            if (msg instanceof FileRequest) {
                FileRequest fr = (FileRequest) msg;
                if (Files.exists(Paths.get(currentPath + fr.getFilename())) && fr.getCommand().equals("down")) {

                    FileMessage fm = new FileMessage(Paths.get(currentPath + fr.getFilename()), 0, fr.getFilename());
                    System.out.println(Arrays.toString(fm.getData()));
                    ctx.writeAndFlush(fm);
                } else if (Files.exists(Paths.get(currentPath + fr.getFilename())) && fr.getCommand().equals("delete")) {

                    Files.delete(Paths.get(currentPath + fr.getFilename()));
                    serverFileList.clear();
                    Files.list(Paths.get(currentPath)).map(p -> p.getFileName().toString()).forEach(o -> serverFileList.add(o));
                    ctx.writeAndFlush(new FileList(serverFileList));
                } else if (fr.getFilename().equals("list")) {
                    serverFileList.clear();
                    Files.list(Paths.get(currentPath)).map(p -> p.getFileName().toString()).forEach(o -> serverFileList.add(o));
                    ctx.writeAndFlush(new FileList(serverFileList));
                }
            } else if (msg instanceof FileMessage) {
                FileMessage fm = (FileMessage) msg;
                if (fm.part > 1) {
                    if (!Files.exists(Paths.get(currentPath + fm.getFilename()))) {
                        Files.write(Paths.get(currentPath + fm.getFilename()), fm.getData());
                    part++;
                    }
                } else if (fm.part == 1) {
                    Files.write(Paths.get(currentPath + fm.getFilename()), fm.getData());
                    fileSplitter.join(currentPath + fm.fileFullName, currentPath + fm.fileFullName);
                    part++;
                    fileSplitter.removeTemp(currentPath + fm.fileFullName, part);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    protected void setPath() {
        for (Client client : Server.clientList) {
            path.add("server_" + client.login + "_storage/");
        }
    }

    public void setCurrentPath(String login) {
        currentPath = "server_" + login + "_storage/";
    }
}
