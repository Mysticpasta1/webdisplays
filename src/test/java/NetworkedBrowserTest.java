import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.EmptyByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.montoyo.wd.remote.client.ImageReader;
import net.montoyo.wd.remote.server.ServerRenderer;

import java.awt.*;
import java.nio.ByteBuffer;
import java.util.Random;

public class NetworkedBrowserTest {
    public static void main(String[] args) {
        int w = 45;
        ByteBuffer src = ByteBuffer.allocate((w * w) * 4);
        Random rng = new Random();
        byte[] bbyte = new byte[1];
        for (int i = 0; i < (w * w) * 4; i++) {
            rng.nextBytes(bbyte);
            src.put(bbyte[0]);
        }

        ServerRenderer renderer = new ServerRenderer();
        renderer.size(w, w);
        renderer.put(src, new Rectangle[]{
                new Rectangle(0, 0, w, w)
        }, w);

        ByteBuffer buffer = renderer.get();

        ByteBuffer dst = ByteBuffer.allocate((w * w) * 4);
        ImageReader reader = new ImageReader(dst);
        reader.read(buffer, w);

        dst.position(0).limit(dst.capacity());
        src.position(0).limit(src.capacity());
        for (int i = 0; i < src.capacity(); i++) {
            if (src.get(i) != dst.get(i)) System.err.println("Mismatch at " + i);
//            else System.out.println(" Correct at " + i);
        }
    }
}
