import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.ReferenceCountUtil;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class AuthHandler extends ChannelInboundHandlerAdapter {
    String login;

    public AuthHandler() {

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg == null) {
                return;

            } if (msg instanceof ClientConnection) {
                ClientConnection cl = (ClientConnection) msg;
                for (Client client : Server.clientList) {
                    if (cl.getLogin().equals(client.login)) {
                        ctx.pipeline().remove(this);
                        ctx.pipeline().addLast(new MainHandler(client.login));
                        ctx.writeAndFlush(new Approve("ok"));
                    }
                }
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}
