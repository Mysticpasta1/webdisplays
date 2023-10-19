package net.montoyo.wd.remote.client;

import net.minecraft.network.FriendlyByteBuf;

import java.nio.ByteBuffer;

public class ImageReader {
    ByteBuffer target;

    public ImageReader(ByteBuffer target) {
        this.target = target;
    }

    public void read(ByteBuffer buf, int bWidth) {
        int lim = buf.limit();

        int count = buf.getInt();
        for (int i = 0; i < count; i++) {
            int x = buf.getInt();
            int y = buf.getInt();
            int width = buf.getInt();
            int height = buf.getInt();

            for (int y1 = 0; y1 < height; y1++) {
                buf.limit(buf.position() + width * 4);
                target.position(
                        x * 4 + (y + y1) * 4 * bWidth
                );
                target.put(buf);
                buf.limit(lim);
            }
        }
    }

    public void read(FriendlyByteBuf buf, int bWidth) {
        int count = buf.readInt();
        for (int i = 0; i < count; i++) {
            int x = buf.readInt();
            int y = buf.readInt();
            int width = buf.readInt();
            int height = buf.readInt();

            for (int y1 = 0; y1 < height; y1++) {
                ByteBuffer buf1 = buf.internalNioBuffer(buf.readerIndex(), width * 4);
                target.position(
                        x * 4 + (y + y1) * 4 * bWidth
                );
                target.put(buf1);
                buf.skipBytes(width * 4);
            }
        }
    }
}
