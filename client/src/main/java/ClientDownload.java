import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.nio.file.Files;
import java.nio.file.Paths;


public class ClientDownload extends ChannelInboundHandlerAdapter {

    public static boolean isOk() {
        return isOk;
    }

    public static void setIsOk(boolean isOk) {
        ClientDownload.isOk = isOk;
    }

    static boolean isOk;

    CloudWindowController controller;


    public ClientDownload(CloudWindowController controller) {
        this.controller = controller;
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
                controller.serfilesList.getItems().clear();
                FileList fl = (FileList) msg;
                for (String s : fl.getSerfilesList()) {
                    controller.serfilesList.getItems().add(s);

                }
            } else if (msg instanceof Approve) {
                Approve ok = (Approve) msg;
                if (ok.isAuthorizated.equals("ok")) {
                    setIsOk(true);
                }
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}

