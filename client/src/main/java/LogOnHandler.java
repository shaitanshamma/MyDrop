import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import javafx.application.Platform;

import java.io.IOException;

public class LogOnHandler extends ChannelHandlerAdapter {

    private LogOnWindowController logOnWindowController;

    LogOnHandler(LogOnWindowController logOnWindowController) {
        this.logOnWindowController = logOnWindowController;

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Authorization) {
            Authorization authorization = (Authorization) msg;
            if (authorization.isAuthorized) {
                if (Platform.isFxApplicationThread()) {
                    ctx.pipeline().remove(this);
                    logOnWindowController.changeWindow();
                } else {
                    Platform.runLater(() -> {
                        try {
                            ctx.pipeline().remove(this);
                            logOnWindowController.changeWindow();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        }
    }
}

