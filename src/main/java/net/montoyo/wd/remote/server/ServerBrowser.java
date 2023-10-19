package net.montoyo.wd.remote.server;

import com.cinemamod.mcef.MCEFHeadlessBrowser;
import com.cinemamod.mcef.MCEFClient;
import com.cinemamod.mcef.MCEFRenderer;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.montoyo.wd.remote.IRemoteBrowser;
import net.montoyo.wd.remote.IWDBrowser;
import org.cef.browser.CefBrowser;

import java.awt.*;
import java.nio.ByteBuffer;
import java.util.UUID;

public class ServerBrowser extends MCEFHeadlessBrowser implements IRemoteBrowser, IWDBrowser {
    private int lastWidth = 0;
    private int lastHeight = 0;

    UUID myId;

    ServerRenderer renderer = new ServerRenderer();

    public ServerBrowser(MCEFClient client, String url, boolean transparent) {
        super(client, url, transparent);
        this.myId = UUID.randomUUID();
    }

    @Override
    public UUID getUUID() {
        return myId;
    }

    ByteBuffer graphics;

    void store(ByteBuffer srcBuffer, ByteBuffer dstBuffer, Rectangle dirty, int width, int height) {
        for (int y = dirty.y; y < dirty.height + dirty.y; y++) {
            dstBuffer.position((y * width + dirty.x) * 4);
            srcBuffer.position((y * width + dirty.x) * 4);
            srcBuffer.limit(dirty.width * 4 + (y * width + dirty.x) * 4);
            dstBuffer.put(srcBuffer);
            srcBuffer.position(0).limit(srcBuffer.capacity());
        }
        dstBuffer.position(0).limit(dstBuffer.capacity());
    }

    @Override
    public void onPaint(CefBrowser browser, boolean popup, Rectangle[] dirtyRects, ByteBuffer buffer, int width, int height) {
        if ((width != this.lastWidth && height != this.lastHeight)) {
            renderer.size(width, height);

            renderer.put(buffer, new Rectangle[]{
                    new Rectangle(0, 0, width, height)
            }, width);
            this.lastWidth = width;
            this.lastHeight = height;
        } else {
            renderer.put(buffer, dirtyRects, width);
        }
    }
}
