package net.montoyo.wd.remote.server;

import net.montoyo.wd.remote.VirtualBrowser;
import org.cef.browser.CefBrowser;
import org.cef.callback.CefDragData;
import org.cef.handler.CefScreenInfo;

import java.awt.*;
import java.nio.ByteBuffer;

public class BlankBrowser extends VirtualBrowser {
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

    @Override
    public void onPaint(CefBrowser cefBrowser, boolean b, Rectangle[] rectangles, ByteBuffer byteBuffer, int i, int i1) {

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
