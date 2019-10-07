import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import javafx.application.Platform;

import java.io.IOException;
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
                    //                   controller.refresh(fl.getServerFileList());
                   // server.addAll(fl.serverFileList);
//                        controller.listItems.addAll(fl.serverFileList);
//                    for (String s : fl.getServerFileList()) {
//                      //  server.add(s);
//                        // controller.serverFileList.getItems().add(s);
                    controller.refresh(fl.getServerFileList());
//                    }
                } else {
                    Platform.runLater(() -> {
                        FileList fl = (FileList) msg;
                        // controller.refresh(fl.getServerFileList());
//                        controller.
//                                serverFileList.getItems().clear();
                     //   controller.listItems.addAll(fl.serverFileList);
                    //    server.addAll(fl.serverFileList);
//                        for (String s : fl.getServerFileList()) {
//                            // serverFileList.getItems().add(s);
//                        }
                        controller.refresh(fl.getServerFileList());
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

