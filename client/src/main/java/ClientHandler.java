import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import javafx.application.Platform;

import java.nio.file.Files;
import java.nio.file.Paths;


public class ClientHandler extends ChannelInboundHandlerAdapter {


   CloudWindowController controller;

   LogOnWindowController logOnWindowController;

    public ClientHandler(CloudWindowController controller, LogOnWindowController logOnWindowController) {
        this.controller = controller;
        this.logOnWindowController = logOnWindowController;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg == null) {
                return;
            }
            if (msg instanceof FileMessage) {
                FileMessage fr = (FileMessage) msg;
                Files.write(Paths.get("client_storage/" + fr.getFilename()), fr.getData());
            } else if (msg instanceof FileList) {
                if (Platform.isFxApplicationThread()) {
                    FileList fl = (FileList) msg;
                    controller.refresh(fl.getSerfilesList());
//                    for (String s : fl.getSerfilesList()) {
//                        controller.serfilesList.getItems().add(s);
//                    }
                } else {
                    Platform.runLater(() -> {
                        FileList fl = (FileList) msg;
                        controller.refresh(fl.getSerfilesList());
//                        controller.
//                                serfilesList.getItems().clear();
//                        for (String s : fl.getSerfilesList()) {
//                            controller.
//                                    serfilesList.getItems().add(s);
//                        }
                    });
                }

            } else if (msg instanceof Approve) {
                Approve ok = (Approve) msg;
                if (ok.isAuthorizated.equals("ok")) {
                    if(Platform.isFxApplicationThread()){

                    logOnWindowController.isOk.setText("ok");
                    }else {
                        Platform.runLater(()->{
                    logOnWindowController.isOk.setText("ok");

                        });
                    }
                }
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}

