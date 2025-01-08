package net.montoyo.wd.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.montoyo.wd.client.ClientProxy;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class OverlayMixin {
    @Shadow
    @Final
    protected Minecraft minecraft;

    @Shadow
    protected int screenWidth;

    @Shadow
    protected int screenHeight;

    @Inject(at = @At("HEAD"), method = "renderCrosshair", cancellable = true)
    public void preDrawCrosshair(GuiGraphics pGuiGraphics, CallbackInfo ci) {
//		ClientProxy.renderCrosshair(minecraft.options, screenWidth, screenHeight, ((Gui) (Object) this).getBlitOffset(), poseStack, ci);
        ClientProxy.renderCrosshair(minecraft.options, screenWidth, screenHeight, 0, pGuiGraphics, ci);
    }
}