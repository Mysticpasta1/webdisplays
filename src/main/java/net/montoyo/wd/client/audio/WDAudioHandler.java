package net.montoyo.wd.client.audio;

import net.montoyo.wd.WebDisplays;
import net.montoyo.wd.client.ClientProxy;
import net.montoyo.wd.entity.ScreenBlockEntity;
import net.montoyo.wd.utilities.Log;
import org.cef.browser.CefBrowser;
import org.cef.handler.CefAudioHandler;
import org.cef.misc.CefAudioParameters;
import org.cef.misc.DataPointer;

public class WDAudioHandler implements CefAudioHandler {
	public static final WDAudioHandler INSTANCE = new WDAudioHandler();
	
	@Override
	public boolean getAudioParameters(CefBrowser cefBrowser, CefAudioParameters cefAudioParameters) {
		ClientProxy proxy = ((ClientProxy) WebDisplays.PROXY);
		
		boolean didParameterize = false;
		
		for (ScreenBlockEntity tes : proxy.getScreens()) {
			WDAudioSource source = tes.getSoundSource(cefBrowser);
			if (source != null) {
				source.parameterize(cefAudioParameters);
				didParameterize = true;
			}
		}
		
		return didParameterize;
	}
	
	@Override
	public void onAudioStreamStarted(CefBrowser cefBrowser, CefAudioParameters cefAudioParameters, int channels) {
		ClientProxy proxy = ((ClientProxy) WebDisplays.PROXY);
		
		if (cefAudioParameters == null) return;
		
		for (ScreenBlockEntity tes : proxy.getScreens()) {
			WDAudioSource source = tes.getSoundSource(cefBrowser);
			if (source != null) {
				source.parameterize(channels, cefAudioParameters);
			}
		}
	}
	
	//
	// Called on the audio stream thread when a PCM packet is received for the
	// stream. |data| is an array representing the raw PCM data as a floating
	// point type, i.e. 4-byte value(s). |frames| is the number of frames in the
	// PCM packet. |pts| is the presentation timestamp (in milliseconds since the
	// Unix Epoch) and represents the time at which the decompressed packet
	// should be presented to the user. Based on |frames| and the
	// |channel_layout| value passed to OnAudioStreamStarted you can calculate
	// the size of the |data| array in bytes.
	//
	@Override
	public void onAudioStreamPacket(CefBrowser cefBrowser, DataPointer pointer, int frames, long pts) {
		ClientProxy proxy = ((ClientProxy) WebDisplays.PROXY);
		
		for (ScreenBlockEntity tes : proxy.getScreens()) {
			WDAudioSource source = tes.getSoundSource(cefBrowser);
			if (source != null) {
				source.audioStream.setData(pointer);
			}
		}
	}
	
	@Override
	public void onAudioStreamStopped(CefBrowser cefBrowser) {
	
	}
	
	@Override
	public void onAudioStreamError(CefBrowser cefBrowser, String s) {
		ClientProxy proxy = ((ClientProxy) WebDisplays.PROXY);
		
		for (ScreenBlockEntity tes : proxy.getScreens()) {
			WDAudioSource source = tes.getSoundSource(cefBrowser);
			if (source != null) {
				Log.warning("Audio stream errored: " + s);
			}
		}
	}
}
