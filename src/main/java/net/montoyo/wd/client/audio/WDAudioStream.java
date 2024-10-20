package net.montoyo.wd.client.audio;

import net.minecraft.client.sounds.AudioStream;
import org.cef.misc.CefAudioParameters;
import org.cef.misc.CefChannelLayout;
import org.cef.misc.DataPointer;
import org.checkerframework.checker.units.qual.A;
import org.lwjgl.system.MemoryUtil;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;

public class WDAudioStream implements AudioStream {
	AudioFormat currentFormat = new AudioFormat(
			AudioFormat.Encoding.PCM_SIGNED,
			44100, 16,
			1, (4096 / 8) * 2, 44100,
			false
	);
	
	@Override
	public AudioFormat getFormat() {
		return currentFormat;
	}
	
	int fpb;
	
	public void setFormat(int channels, CefAudioParameters cefAudioParameters) {
		currentFormat = new AudioFormat(
				AudioFormat.Encoding.PCM_SIGNED,
				cefAudioParameters.sampleRate, 16,
				1, (16 / 8) * channels, cefAudioParameters.sampleRate,
				false
		);
		fpb = cefAudioParameters.framesPerBuffer;
	}
	
	public void setFormat(CefAudioParameters cefAudioParameters) {
		CefChannelLayout layout = cefAudioParameters.channelLayout;
		int channels = 0;
		if (layout == CefChannelLayout.CEF_CHANNEL_LAYOUT_MONO)
			channels = 1;
		else if (layout == CefChannelLayout.CEF_CHANNEL_LAYOUT_STEREO)
			channels = 2;
		setFormat(channels, cefAudioParameters);
	}
	
	ArrayDeque<float[]> buffers = new ArrayDeque<>();
	
	public void setData(long data) {
//		DataPointer ptr = data.forCapacity(currentFormat.getChannels() << 3);
//		ptr.setAlignment(3);
		long baseAddr = data;
		for (int i = 0; i < 1; i++) {
			long addr = MemoryUtil.memGetLong(baseAddr + (i << 3));
			int cap = fpb;
			float[] flts = new float[cap];
			for (int i1 = 0; i1 < cap; i1++) {
				flts[i1] = MemoryUtil.memGetFloat(addr + (i1 << 2));
			}
			buffers.add(flts);
		}
	}
	
	@Override
	public ByteBuffer read(int pSize) throws IOException {
		System.out.println(buffers.size());
		ByteBuffer buffer = ByteBuffer.allocateDirect(pSize);
		if (!buffers.isEmpty()) {
			final int MAX_16_BIT = 32767;
			final int MIN_16_BIT = -32768;
			
			loopBufs:
			while (true) {
				if (!buffers.isEmpty()) {
					for (float v : buffers.pop()) {
						if (buffer.position() >= pSize) break loopBufs;
//						buffer.putFloat(v);
						
						// Scale and clip the float value to the range of a signed 16-bit int
						float floatSample = v;
						int intSample = (int) (floatSample * MAX_16_BIT);
						
						if (intSample > MAX_16_BIT) {
							intSample = MAX_16_BIT;
						} else if (intSample < MIN_16_BIT) {
							intSample = MIN_16_BIT;
						}
						
						// Convert the int sample to bytes (little-endian format)
						buffer.put((byte) (intSample & 0xFF));
						buffer.put((byte) ((intSample >> 8) & 0xFF));
					}
				} else break;
			}
			buffer.position(0);
		}
		return buffer;
	}
	
	@Override
	public void close() throws IOException {
	}
}
