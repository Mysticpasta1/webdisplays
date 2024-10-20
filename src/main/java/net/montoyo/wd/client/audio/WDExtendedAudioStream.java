package net.montoyo.wd.client.audio;

import com.mojang.blaze3d.audio.Channel;

import javax.sound.sampled.AudioFormat;

public interface WDExtendedAudioStream {
	default int getPumpCount(int defaultAmount) {
		return defaultAmount;
	}
	
	default void attach(Channel channel) {
		return;
	}
	
	default int computeSize(AudioFormat format, int streamingBufferSize) {
		return streamingBufferSize;
	}
	
	default boolean canStop() {
		return true;
	}
}
