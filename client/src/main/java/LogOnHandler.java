import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import javafx.application.Platform;

import java.nio.file.Files;
import java.nio.file.Paths;

public class LogOnHandler extends ChannelInboundHandlerAdapter {
//
//    LogOnWindowController logOnWindowController;
//    CloudWindowController controller;
//
//    public LogOnHandler(LogOnWindowController logOnWindowController, CloudWindowController controller) {
//        this.logOnWindowController = logOnWindowController;
//        this.controller = controller;
//    }
//
//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        try {
//            if (msg == null) {
//                return;
//            }
//            else if (msg instanceof Approve) {
//                Approve ok = (Approve) msg;
//                if (ok.isAuthorizated.equals("ok")) {
//                    if(Platform.isFxApplicationThread()){
//                        logOnWindowController.isOk.setText("ok");
//                    }else {
//                        Platform.runLater(()->{
//                            logOnWindowController.isOk.setText("ok");
//
//                        });
//                    }
//                }
//            }
//        } finally {
//            ReferenceCountUtil.release(msg);
//        }
//        ctx.pipeline().remove(this);
//        ctx.pipeline().addFirst(new ClientHandler(controller));
//    }

    }

