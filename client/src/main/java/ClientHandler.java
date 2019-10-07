import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import javafx.application.Platform;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


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
                    //                   controller.refresh(fl.getSerfilesList());
                   // server.addAll(fl.serfilesList);
//                        controller.listItems.addAll(fl.serfilesList);
//                    for (String s : fl.getSerfilesList()) {
//                      //  server.add(s);
//                        // controller.serverFileList.getItems().add(s);
                    controller.refresh(fl.getSerfilesList());
//                    }
                } else {
                    Platform.runLater(() -> {
                        FileList fl = (FileList) msg;
                        // controller.refresh(fl.getSerfilesList());
//                        controller.
//                                serverFileList.getItems().clear();
                     //   controller.listItems.addAll(fl.serfilesList);
                    //    server.addAll(fl.serfilesList);
//                        for (String s : fl.getSerfilesList()) {
//                            // serverFileList.getItems().add(s);
//                        }
                        controller.refresh(fl.getSerfilesList());
                    });
                }

            } else if (msg instanceof Approve) {
                Approve ok = (Approve) msg;
                if (ok.isAuthorizated.equals("ok")) {
                    if (Platform.isFxApplicationThread()) {
                        logOnWindowController.changeWindow();
                    } else {
                        Platform.runLater(() -> {
                            try {
                                logOnWindowController.changeWindow();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });

                    }
                }
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}

