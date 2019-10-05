import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class MainHandler extends ChannelInboundHandlerAdapter {

    public MainHandler(String login) {
        this.login = login;
    }

    String login;
    List<String> serfilesList = new ArrayList<>();
    List<String> path = new ArrayList<>();
    String currentPath;
    Map<Channel, String> pathOn = new HashMap<>();
    final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client connected..." + login);
        channels.add(ctx.channel());
        for (Channel channel : channels) {
            System.out.println(channel.id());
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        currentPath = "server_" + login + "_storage/";
        channels.add(ctx.channel());
        System.out.println(ctx.channel().id() +"____" +  login);
        try {
            if (msg == null) {
                return;
            }
            if (msg instanceof FileRequest) {
                FileRequest fr = (FileRequest) msg;
               // setCurrentPath(ctx);
                if (Files.exists(Paths.get(currentPath + fr.getFilename())) && fr.getCommand().equals("down")) {
                 //   setCurrentPath(ctx);
                    FileMessage fm = new FileMessage(Paths.get(path + fr.getFilename()));
                    System.out.println(Arrays.toString(fm.getData()));
                    ctx.writeAndFlush(fm);
                } else if (Files.exists(Paths.get(currentPath + fr.getFilename())) && fr.getCommand().equals("delete")) {
                  //  setCurrentPath(ctx);
                    Files.delete(Paths.get(currentPath + fr.getFilename()));
                    serfilesList.clear();
                    Files.list(Paths.get(currentPath)).map(p -> p.getFileName().toString()).forEach(o -> serfilesList.add(o));
                    ctx.writeAndFlush(new FileList(serfilesList));
                } else if (fr.getFilename().equals("list")) {
//                    for (Channel channel : channels) {
//                        Server.pathOn.put(channel.toString(),login);
//                        setCurrentPath(Server.pathOn.get(channel));
//                    }
                    //setCurrentPath(ctx);
                   // System.out.println(pathOn.get(ctx.channel()));
                    serfilesList.clear();
                    Files.list(Paths.get(currentPath)).map(p -> p.getFileName().toString()).forEach(o -> serfilesList.add(o));
                    ctx.writeAndFlush(new FileList(serfilesList));
                }
            } else if (msg instanceof FileMessage) {
                //setCurrentPath(ctx);
                FileMessage fm = (FileMessage) msg;
                if (!Files.exists(Paths.get(currentPath + fm.getFilename()))) {
                    Files.write(Paths.get(currentPath + fm.getFilename()), fm.getData());
                }
            }
//            if (msg instanceof ClientConnection) {
//                ClientConnection cl = (ClientConnection) msg;
//                for (Client client : Server.clientList) {
//                    if (cl.getLogin().equals(client.login)) {
//                        ctx.writeAndFlush(new Approve("ok"));
//                        for (Channel channel : channels) {
//                        pathOn.put(channel, client.login);
//                        }
//                        //setCurrentPath(ctx);
//                    }
//                }
//            }
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
