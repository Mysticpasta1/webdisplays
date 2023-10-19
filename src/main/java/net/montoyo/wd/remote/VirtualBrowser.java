package net.montoyo.wd.remote;

import com.cinemamod.mcef.MCEF;
import com.cinemamod.mcef.MCEFRenderer;
import com.cinemamod.mcef.listeners.MCEFCursorChangeListener;
import org.cef.CefClient;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.callback.CefPdfPrintCallback;
import org.cef.callback.CefRunFileDialogCallback;
import org.cef.callback.CefStringVisitor;
import org.cef.handler.CefDialogHandler;
import org.cef.handler.CefRenderHandler;
import org.cef.handler.CefWindowHandler;
import org.cef.misc.CefPdfPrintSettings;
import org.cef.network.CefRequest;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Vector;
import java.util.concurrent.CompletableFuture;

public abstract class VirtualBrowser implements CefBrowser, CefRenderHandler, CefWindowHandler, IWDBrowser {
    String url;

    @Override
    public void createImmediately() {

    }

    @Override
    public CefClient getClient() {
        return MCEF.getClient().getHandle();
    }

    @Override
    public CefRenderHandler getRenderHandler() {
        return this;
    }

    @Override
    public CefWindowHandler getWindowHandler() {
        return this;
    }

    @Override
    public boolean canGoBack() {
        return false;
    }

    @Override
    public void goBack() {

    }

    @Override
    public boolean canGoForward() {
        return false;
    }

    @Override
    public void goForward() {

    }

    @Override
    public boolean isLoading() {
        return false;
    }

    @Override
    public void reload() {

    }

    @Override
    public void reloadIgnoreCache() {

    }

    @Override
    public void stopLoad() {

    }

    @Override
    public int getIdentifier() {
        return 0;
    }

    @Override
    public CefFrame getMainFrame() {
        return null;
    }

    @Override
    public CefFrame getFocusedFrame() {
        return null;
    }

    @Override
    public CefFrame getFrame(long l) {
        return null;
    }

    @Override
    public CefFrame getFrame(String s) {
        return null;
    }

    @Override
    public Vector<Long> getFrameIdentifiers() {
        return null;
    }

    @Override
    public Vector<String> getFrameNames() {
        return null;
    }

    @Override
    public int getFrameCount() {
        return 0;
    }

    @Override
    public boolean isPopup() {
        return false;
    }

    @Override
    public boolean hasDocument() {
        return false;
    }

    @Override
    public void viewSource() {

    }

    @Override
    public void getSource(CefStringVisitor cefStringVisitor) {

    }

    @Override
    public void getText(CefStringVisitor cefStringVisitor) {

    }

    @Override
    public void loadRequest(CefRequest cefRequest) {

    }

    @Override
    public void loadURL(String s) {
        this.url = url;
    }

    @Override
    public void executeJavaScript(String s, String s1, int i) {

    }

    @Override
    public String getURL() {
        return url;
    }

    @Override
    public void close(boolean b) {

    }

    @Override
    public void setCloseAllowed() {

    }

    @Override
    public boolean doClose() {
        return true;
    }

    @Override
    public void onBeforeClose() {

    }

    @Override
    public void setFocus(boolean b) {

    }

    @Override
    public void setWindowVisibility(boolean b) {

    }

    @Override
    public double getZoomLevel() {
        return 0;
    }

    @Override
    public void setZoomLevel(double v) {

    }

    @Override
    public void runFileDialog(CefDialogHandler.FileDialogMode fileDialogMode, String s, String s1, Vector<String> vector, int i, CefRunFileDialogCallback cefRunFileDialogCallback) {

    }

    @Override
    public void startDownload(String s) {

    }

    @Override
    public void print() {

    }

    @Override
    public void printToPDF(String s, CefPdfPrintSettings cefPdfPrintSettings, CefPdfPrintCallback cefPdfPrintCallback) {

    }

    @Override
    public void find(String s, boolean b, boolean b1, boolean b2) {

    }

    @Override
    public void stopFinding(boolean b) {

    }

    @Override
    public CefBrowser getDevTools() {
        return this;
    }

    @Override
    public CefBrowser getDevTools(Point point) {
        return this;
    }

    @Override
    public void replaceMisspelling(String s) {

    }

    @Override
    public CompletableFuture<BufferedImage> createScreenshot(boolean b) {
        return null;
    }

    @Override
    public void resize(int y, int x) {

    }

    @Override
    public void setCursorChangeListener(MCEFCursorChangeListener mcefCursorChangeListener) {

    }

    @Override
    public MCEFRenderer getRenderer() {
        return null;
    }
}
