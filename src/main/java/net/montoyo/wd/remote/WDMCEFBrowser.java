package net.montoyo.wd.remote;

import com.cinemamod.mcef.MCEFBrowser;
import com.cinemamod.mcef.MCEFClient;

public class WDMCEFBrowser extends MCEFBrowser implements IWDBrowser {
    public WDMCEFBrowser(MCEFClient client, String url, boolean transparent) {
        super(client, url, transparent);
    }
}
