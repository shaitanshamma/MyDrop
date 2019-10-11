import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class AuthHandler extends ChannelInboundHandlerAdapter {

    public AuthHandler() {

    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        try {
            if (msg == null) {
                return;

            } if (msg instanceof ClientConnection) {
                ClientConnection cl = (ClientConnection) msg;
                for (Client client : Server.clientList) {
                    if (cl.getLogin().equals(client.login)&&cl.getPassword().equals(client.password)) {
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
