package net.montoyo.wd.mixins.audio;

import net.minecraft.client.sounds.AudioStream;
import net.montoyo.wd.client.audio.WDExtendedAudioStream;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AudioStream.class)
public interface AudioStreamMixin extends WDExtendedAudioStream {
}
