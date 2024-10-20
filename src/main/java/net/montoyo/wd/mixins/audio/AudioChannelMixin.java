package net.montoyo.wd.mixins.audio;

import com.mojang.blaze3d.audio.Channel;
import net.minecraft.client.sounds.AudioStream;
import net.montoyo.wd.client.audio.WDExtendedAudioStream;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import javax.sound.sampled.AudioFormat;

@Mixin(Channel.class)
public class AudioChannelMixin {
	@Shadow
	@Nullable
	private AudioStream stream;
	
	@Shadow private int streamingBufferSize;
	
	@Inject(at = @At("HEAD"), method = "attachBufferStream")
	public void postAttach(AudioStream pStream, CallbackInfo ci) {
		((WDExtendedAudioStream) pStream).attach((Channel) (Object) this);
	}
	
	@Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/audio/Channel;pumpBuffers(I)V", shift = At.Shift.BEFORE), method = "attachBufferStream")
	public void postComputeSize(AudioStream pStream, CallbackInfo ci) {
		streamingBufferSize = ((WDExtendedAudioStream) pStream).computeSize(
				pStream.getFormat(), streamingBufferSize
		);
	}
	
	@ModifyVariable(argsOnly = true, ordinal = 0, at = @At("HEAD"), method = "pumpBuffers")
	public int prePump(int defaultAmount) {
		return ((WDExtendedAudioStream) stream).getPumpCount(defaultAmount);
	}
	
	@Inject(at = @At("HEAD"), method = "stopped", cancellable = true)
	public void preCheckStopped(CallbackInfoReturnable<Boolean> cir) {
		if (stream != null)
			if (!((WDExtendedAudioStream) stream).canStop())
				cir.setReturnValue(false);
	}
}
