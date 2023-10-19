package net.montoyo.wd.net.client_bound;

import net.minecraftforge.network.NetworkEvent;
import net.montoyo.wd.WebDisplays;
import net.montoyo.wd.net.Packet;

public class S2CMessageEnableSSR extends Packet {
    @Override
    public void handle(NetworkEvent.Context ctx) {
        if (checkClient(ctx)) {
            WebDisplays.markSSR();
        }
    }
}
