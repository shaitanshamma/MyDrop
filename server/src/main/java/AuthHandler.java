import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

public class AuthHandler extends ChannelHandlerAdapter {

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
                        ctx.writeAndFlush(new Authorization(true));
                    }
                }
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}
