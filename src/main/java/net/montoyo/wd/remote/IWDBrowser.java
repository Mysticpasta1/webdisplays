package net.montoyo.wd.remote;

import com.cinemamod.mcef.MCEFRenderer;
import com.cinemamod.mcef.listeners.MCEFCursorChangeListener;

public interface IWDBrowser {
    void close(boolean b);

    void loadURL(String url);

    void resize(int y, int x);

    void setCursorChangeListener(MCEFCursorChangeListener mcefCursorChangeListener);

    MCEFRenderer getRenderer();

    void setFocus(boolean b);

    void sendMouseRelease(int x, int y, int button);

    void sendMouseMove(int x, int y);

    void sendMousePress(int x, int y, int button);
}
