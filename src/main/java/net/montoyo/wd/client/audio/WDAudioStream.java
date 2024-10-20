package net.montoyo.wd.client.audio;

import com.mojang.blaze3d.audio.Channel;
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

public class WDAudioStream implements AudioStream, WDExtendedAudioStream {
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
	
	public void setData(DataPointer data) {
		int cap = fpb;
		DataPointer ptr = data
				.forCapacity(currentFormat.getChannels() << 3)
				.withAlignment(3);
		
		for (int i = 0; i < 1; i++) {
			DataPointer subPtr = ptr.getData(i)
					.withAlignment(2)
					.forCapacity(cap << 2);
			
			float[] flts = new float[cap];
			for (int i1 = 0; i1 < cap; i1++)
				flts[i1] = subPtr.getFloat(i1);
			
			buffers.add(flts);
		}
	}
	
	boolean checking = false;
	
	@Override
	public ByteBuffer read(int pSize) throws IOException {
		System.out.println(buffers.size());
		int sz = 2048 * buffers.size();
		if (sz < 2048) sz = 4096;
		pSize = sz;
		ByteBuffer buffer = ByteBuffer.allocateDirect(pSize);
		if (!buffers.isEmpty()) {
			final int MAX_16_BIT = 32767;
			final int MIN_16_BIT = -32768;
			
			int i0 = 0;
			loopBufs:
			while (true) {
				if (!buffers.isEmpty()) {
					for (float v : buffers.pop()) {
						if (buffer.position() >= pSize) break loopBufs;
						
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
					i0++;
				} else break;
			}
			buffer.position(0);
			System.out.println("Consumed " + i0 + " buffers");
		}
		return buffer;
	}
	
	@Override
	public void close() throws IOException {
	}
	
	Channel channel;
	
	@Override
	public void attach(Channel channel) {
		WDExtendedAudioStream.super.attach(channel);
		this.channel = channel;
	}
	
	@Override
	public int getPumpCount(int defaultAmount) {
		return defaultAmount;
	}
	
	@Override
	public int computeSize(AudioFormat format, int streamingBufferSize) {
		return 2048;
	}
	
	@Override
	public boolean canStop() {
		return true;
	}
}
