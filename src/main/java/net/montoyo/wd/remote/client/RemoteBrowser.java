package net.montoyo.wd.remote.client;

import com.cinemamod.mcef.MCEFRenderer;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.montoyo.wd.remote.IRemoteBrowser;
import net.montoyo.wd.remote.IWDBrowser;
import net.montoyo.wd.remote.VirtualBrowser;
import org.cef.browser.CefBrowser;
import org.cef.callback.CefDragData;
import org.cef.handler.CefScreenInfo;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;
import java.util.UUID;

import static org.lwjgl.opengl.GL11C.*;

public class RemoteBrowser extends VirtualBrowser implements IRemoteBrowser, IWDBrowser {
    UUID myId;

    MCEFRenderer renderer;

    public RemoteBrowser() {
        try {
            Constructor<MCEFRenderer> ctor = MCEFRenderer.class.getDeclaredConstructor(boolean.class);
            ctor.setAccessible(true);
            renderer = ctor.newInstance(false);
            Minecraft.getInstance().submit(renderer::initialize);
        } catch (Throwable err) {
            throw new RuntimeException(err);
        }
    }

    @Override
    public MCEFRenderer getRenderer() {
        return renderer;
    }

    @Override
    public UUID getUUID() {
        return myId;
    }

    public void setUUID(UUID uuid) {
        myId = uuid;
    }

    @Override
    public Rectangle getViewRect(CefBrowser cefBrowser) {
        return null;
    }

    @Override
    public boolean getScreenInfo(CefBrowser cefBrowser, CefScreenInfo cefScreenInfo) {
        return false;
    }

    @Override
    public Point getScreenPoint(CefBrowser cefBrowser, Point point) {
        return null;
    }

    @Override
    public void onPopupShow(CefBrowser cefBrowser, boolean b) {

    }

    @Override
    public void onPopupSize(CefBrowser cefBrowser, Rectangle rectangle) {

    }

    int lastWidth, lastHeight;

    @Override
    public void onPaint(CefBrowser browser, boolean popup, Rectangle[] dirtyRects, ByteBuffer buffer, int width, int height) {
        if (width != lastWidth || height != lastHeight) {
            renderer.onPaint(buffer, width, height);
            lastWidth = width;
            lastHeight = height;
        } else {
            if (renderer.getTextureID() == 0) return;

            RenderSystem.bindTexture(renderer.getTextureID());
            if (renderer.isTransparent()) RenderSystem.enableBlend();

            RenderSystem.pixelStore(GL_UNPACK_ROW_LENGTH, width);
            for (Rectangle dirtyRect : dirtyRects) {
                GlStateManager._pixelStore(GL_UNPACK_SKIP_PIXELS, dirtyRect.x);
                GlStateManager._pixelStore(GL_UNPACK_SKIP_ROWS, dirtyRect.y);
                renderer.onPaint(buffer, dirtyRect.x, dirtyRect.y, dirtyRect.width, dirtyRect.height);
            }
        }
    }

    @Override
    public boolean onCursorChange(CefBrowser cefBrowser, int i) {
        return false;
    }

    @Override
    public boolean startDragging(CefBrowser cefBrowser, CefDragData cefDragData, int i, int i1, int i2) {
        return false;
    }

    @Override
    public void updateDragCursor(CefBrowser cefBrowser, int i) {

    }

    @Override
    public Rectangle getRect(CefBrowser cefBrowser) {
        return null;
    }

    @Override
    public void onMouseEvent(CefBrowser cefBrowser, int i, int i1, int i2, int i3, int i4) {

    }

    @Override
    public void sendMouseRelease(int x, int y, int button) {
    }

    @Override
    public void sendMouseMove(int x, int y) {

    }

    @Override
    public void sendMousePress(int x, int y, int button) {

    }
}
