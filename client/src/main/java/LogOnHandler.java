import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import javafx.application.Platform;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LogOnHandler extends ChannelInboundHandlerAdapter {

    private LogOnWindowController logOnWindowController;

    LogOnHandler(LogOnWindowController logOnWindowController) {
        this.logOnWindowController = logOnWindowController;

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Approve) {
            Approve ok = (Approve) msg;
            if (ok.isAuthorizated.equals("ok")) {
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

