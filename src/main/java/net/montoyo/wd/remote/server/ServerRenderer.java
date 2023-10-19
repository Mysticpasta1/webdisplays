package net.montoyo.wd.remote.server;

import java.awt.*;
import java.nio.ByteBuffer;

public class ServerRenderer {
    ByteBuffer contentChange;

    public ServerRenderer() {
    }

    public void size(int width, int height) {
        contentChange = ByteBuffer.allocate(width * height * 4);
    }

    protected void checkGrow(int cap) {
        if (contentChange.position() + cap > contentChange.capacity()) {
            ByteBuffer copy = ByteBuffer.allocate((int) (contentChange.capacity() * 1.5));
            copy.position(0);
            contentChange.flip();
            copy.put(contentChange);
            contentChange = copy;
        }
    }

    public void put(ByteBuffer buffer, Rectangle[] dirtyRects, int width) {
        contentChange.position(0);
        contentChange.limit(contentChange.capacity());
        checkGrow(4);
        contentChange.putInt(dirtyRects.length);
        for (Rectangle dirtyRect : dirtyRects) {
            checkGrow(4 * 4);
            contentChange.putInt(dirtyRect.x);
            contentChange.putInt(dirtyRect.y);
            contentChange.putInt(dirtyRect.width);
            contentChange.putInt(dirtyRect.height);

            for (int y = dirtyRect.y; y < dirtyRect.height + dirtyRect.y; y++) {
                checkGrow(dirtyRect.width * 8);
                buffer.position(
                        dirtyRect.x * 4 +
                                (y * width * 4)
                ).limit(
                        (dirtyRect.x + dirtyRect.width) * 4 +
                                (y * width * 4)
                );
                contentChange.put(buffer);
                buffer.position(0).limit(buffer.capacity());
            }
        }
        contentChange.flip();
    }

    public ByteBuffer get() {
        return contentChange;
    }
}
