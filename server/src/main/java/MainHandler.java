import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainHandler extends ChannelInboundHandlerAdapter {

    List<String> serfilesList = new ArrayList<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client connected...");
        System.out.println(ctx.channel());
//        serfilesList.clear();
//        Files.list(Paths.get("server_storage")).map(p -> p.getFileName().toString()).forEach(o -> serfilesList.add(o));
//        ctx.writeAndFlush(new FileList(serfilesList));
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg == null) {
                return;
            }
            if (msg instanceof FileRequest) {
                FileRequest fr = (FileRequest) msg;
                if (Files.exists(Paths.get("server_storage\\" + fr.getFilename()))&& fr.getCommand().equals("down")) {
                    FileMessage fm = new FileMessage(Paths.get("server_storage/" + fr.getFilename()));
                    System.out.println(Arrays.toString(fm.getData()));
                    ctx.writeAndFlush(fm);
                }
                else if(Files.exists(Paths.get("server_storage\\" + fr.getFilename()))&& fr.getCommand().equals("delete")){
                    Files.delete(Paths.get("server_storage\\" + fr.getFilename()));
                    serfilesList.clear();
                    Files.list(Paths.get("server_storage")).map(p -> p.getFileName().toString()).forEach(o -> serfilesList.add(o));
                    ctx.writeAndFlush(new FileList(serfilesList));
                }
                else if(fr.getFilename().equals("list")){
                    serfilesList.clear();
                    Files.list(Paths.get("server_storage")).map(p -> p.getFileName().toString()).forEach(o -> serfilesList.add(o));
                    ctx.writeAndFlush(new FileList(serfilesList));
                }
            }else if (msg instanceof FileMessage){
                FileMessage fm = (FileMessage) msg;
                if(!Files.exists(Paths.get("server_storage/" + fm.getFilename()))){
                    Files.write(Paths.get("server_storage/" + fm.getFilename()),fm.getData());
                }
            }else if (msg instanceof ClientConnection){
                ClientConnection cl = (ClientConnection) msg;
                if(cl.getLogin().equals("2")){
                    ctx.writeAndFlush(new Approve("ok"));
                }
            }
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
}
