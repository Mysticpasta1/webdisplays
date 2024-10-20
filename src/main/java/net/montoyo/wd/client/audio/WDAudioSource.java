package net.montoyo.wd.client.audio;

import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.AudioStream;
import net.minecraft.client.sounds.SoundBufferLibrary;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.SampledFloat;
import net.montoyo.wd.entity.ScreenBlockEntity;
import net.montoyo.wd.entity.ScreenData;
import org.cef.misc.CefAudioParameters;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class WDAudioSource implements SoundInstance {
	private static final ResourceLocation location = new ResourceLocation("webdisplays:audio_source");
	private static final WeighedSoundEvents events = new WeighedSoundEvents(
			location, "webdisplays.browser"
	);
	private static final SampledFloat CONST_1 = new SampledFloat() {
		@Override
		public float sample(RandomSource pRandom) {
			return 1.0f;
		}
	};
	private final Sound sound = new Sound(
			"unused",
			CONST_1,
			CONST_1,
			1, Sound.Type.SOUND_EVENT,
			true, false,
			100
	);
	ScreenBlockEntity blockEntity;
	ScreenData data;
	WDAudioStream audioStream = new WDAudioStream();
	
	public WDAudioSource(ScreenBlockEntity blockEntity, ScreenData data) {
		this.blockEntity = blockEntity;
		this.data = data;
	}
	
	public void parameterize(int channels, CefAudioParameters cefAudioParameters) {
		audioStream.setFormat(channels, cefAudioParameters);
	}
	
	public void parameterize(CefAudioParameters cefAudioParameters) {
		audioStream.setFormat(cefAudioParameters);
	}
	
	@Override
	public ResourceLocation getLocation() {
		return location;
	}
	
	@Nullable
	@Override
	public WeighedSoundEvents resolve(SoundManager pManager) {
		return events;
	}
	
	@Override
	public CompletableFuture<AudioStream> getStream(SoundBufferLibrary soundBuffers, Sound sound, boolean looping) {
		return CompletableFuture.completedFuture(audioStream);
	}
	
	@Override
	public Sound getSound() {
		return sound;
	}
	
	@Override
	public SoundSource getSource() {
		return SoundSource.RECORDS;
	}
	
	@Override
	public boolean isLooping() {
		return true;
	}
	
	@Override
	public boolean isRelative() {
		return false;
	}
	
	@Override
	public int getDelay() {
		return 0;
	}
	
	@Override
	public float getVolume() {
		return blockEntity.ytVolume;
	}
	
	@Override
	public float getPitch() {
		return 1;
	}
	
	@Override
	public double getX() {
		return blockEntity.getBlockPos().getX();
	}
	
	@Override
	public double getY() {
		return blockEntity.getBlockPos().getY();
	}
	
	@Override
	public double getZ() {
		return blockEntity.getBlockPos().getZ();
	}
	
	@Override
	public Attenuation getAttenuation() {
		return Attenuation.LINEAR;
	}
}
