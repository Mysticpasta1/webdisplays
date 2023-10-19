package net.montoyo.wd.remote;

import com.cinemamod.mcef.MCEF;
import com.cinemamod.mcef.MCEFBrowser;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.montoyo.wd.WebDisplays;
import net.montoyo.wd.remote.client.RemoteBrowser;
import net.montoyo.wd.remote.server.BlankBrowser;
import net.montoyo.wd.remote.server.ServerBrowser;
import org.cef.browser.CefBrowser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class BrowserGen {
    private static ArrayList<IWDBrowser> browsers = new ArrayList<>();
    private static HashMap<UUID, IRemoteBrowser> clientBrowsers = new HashMap<>();
    private static HashMap<UUID, IRemoteBrowser> serverBrowsers = new HashMap<>();

    public static IWDBrowser createBrowser(boolean server, String url, boolean transparent) {
        CefBrowser browser;
        if (!server) {
            if (WebDisplays.isSSR()) {
                browser = new RemoteBrowser() {
                    @Override
                    protected void finalize() throws Throwable {
                        synchronized (browsers) {
                            browsers.remove(this);
                            clientBrowsers.remove(getUUID());
                        }
                        super.finalize();
                    }
                };
            } else
                browser = new WDMCEFBrowser(MCEF.getClient(), url, transparent) {
                    @Override
                    public void close() {
                        synchronized (browsers) {
                            browsers.remove(this);
                            super.close();
                        }
                    }
                };
        } else {
            if (WebDisplays.isSSR()) {
                browser = new ServerBrowser(MCEF.getClient(), url, transparent) {
                    @Override
                    public void close() {
                        browsers.remove(this);
                        synchronized (browsers) {
                            serverBrowsers.remove(getUUID());
                        }
                        super.close();
                    }
                };
                serverBrowsers.put(((IRemoteBrowser) browser).getUUID(), (IRemoteBrowser) browser);
            } else return null;
        }
        browser.setCloseAllowed();
        browser.createImmediately();
        browsers.add((IWDBrowser) browser);
        return (IWDBrowser) browser;
    }

    public static void onUUIDAcquired(RemoteBrowser browser) {
        clientBrowsers.put(((IRemoteBrowser) browser).getUUID(), browser);
    }
}
