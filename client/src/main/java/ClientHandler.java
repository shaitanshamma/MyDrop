import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import javafx.application.Platform;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class ClientHandler extends ChannelOutboundHandlerAdapter {

    private CloudWindowController controller;

    LogOnWindowController logOnWindowController;

    public ClientHandler(CloudWindowController controller) {
        this.controller = controller;

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String currentPath = "client_storage" + File.separator;
        try {
            if (msg == null) {
                return;
            }
            if (msg instanceof FileMessage) {
                FileMessage fr = (FileMessage) msg;
                Files.write(Paths.get(currentPath + fr.getFilename()), fr.getData());
                controller.refreshList();
            } else if (msg instanceof FileList) {
                if (Platform.isFxApplicationThread()) {
                    FileList fl = (FileList) msg;
                    controller.refresh(fl.getServerFileList());
                } else {
                    Platform.runLater(() -> {
                        FileList fl = (FileList) msg;
                        controller.refresh(fl.getServerFileList());
                    });
                }

            } else if (msg instanceof Authorization) {
                Authorization ok = (Authorization) msg;
                if (ok.isAuthorized) {
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

