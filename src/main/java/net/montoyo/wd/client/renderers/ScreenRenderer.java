/*
 * Copyright (C) 2018 BARBOTIN Nicolas
 */

package net.montoyo.wd.client.renderers;

import com.cinemamod.mcef.MCEFBrowser;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.montoyo.wd.entity.TileEntityScreen;
import net.montoyo.wd.remote.IWDBrowser;
import net.montoyo.wd.utilities.Vector3f;
import net.montoyo.wd.utilities.Vector3i;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import static com.mojang.math.Axis.*;

public class ScreenRenderer implements BlockEntityRenderer<TileEntityScreen> {
	public ScreenRenderer() {
	}
	
	public static class ScreenRendererProvider implements BlockEntityRendererProvider<TileEntityScreen> {
		@Override
		public @NotNull BlockEntityRenderer<TileEntityScreen> create(@NotNull Context arg) {
			return new ScreenRenderer();
		}
	}
	
	private final Vector3f mid = new Vector3f();
	private final Vector3i tmpi = new Vector3i();
	private final Vector3f tmpf = new Vector3f();
	
	@Override
	public void render(TileEntityScreen te, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
		if (!te.isLoaded())
			return;
		
		//Disable lighting
//		RenderSystem.enableTexture();
//        RenderSystem.disableCull();
		RenderSystem.disableBlend();
		
		for (int i = 0; i < te.screenCount(); i++) {
			TileEntityScreen.Screen scr = te.getScreen(i);
			if (scr.browser == null) {
				scr.createBrowser(false, true);
			}
			
			// TODO: manually backface cull the screens
			
			tmpi.set(scr.side.right);
			tmpi.mul(scr.size.x);
			tmpi.addMul(scr.side.up, scr.size.y);
			tmpf.set(tmpi);
			mid.set(0.5, 0.5, 0.5);
			mid.addMul(tmpf, 0.5f);
			tmpf.set(scr.side.left);
			mid.addMul(tmpf, 0.5f);
			tmpf.set(scr.side.down);
			mid.addMul(tmpf, 0.5f);
			
			poseStack.pushPose();
			poseStack.translate(mid.x, mid.y, mid.z);
			
			switch (scr.side) {
				case BOTTOM:
					poseStack.mulPose(XP.rotation(90.f + 49.8f));
					break;
				
				case TOP:
					poseStack.mulPose(XN.rotation(90.f + 49.8f));
					break;
				
				case NORTH:
					poseStack.mulPose(YN.rotationDegrees(180.f));
					break;
				
				case SOUTH:
					break;
				
				case WEST:
					poseStack.mulPose(YN.rotationDegrees(90.f));
					break;
				
				case EAST:
					poseStack.mulPose(YP.rotationDegrees(90.f));
					break;
			}
			
			if (scr.doTurnOnAnim) {
				long lt = System.currentTimeMillis() - scr.turnOnTime;
				float ft = ((float) lt) / 100.0f;
				
				if (ft >= 1.0f) {
					ft = 1.0f;
					scr.doTurnOnAnim = false;
				}
				
				poseStack.scale(ft, ft, 1.0f);
			}
			
			if (!scr.rotation.isNull)
				poseStack.mulPose(ZP.rotationDegrees(scr.rotation.angle));
			
			float sw = ((float) scr.size.x) * 0.5f - 2.f / 16.f;
			float sh = ((float) scr.size.y) * 0.5f - 2.f / 16.f;
			
			if (scr.rotation.isVertical) {
				float tmp = sw;
				sw = sh;
				sh = tmp;
			}
			
			Tesselator tesselator = Tesselator.getInstance();
			BufferBuilder builder = tesselator.getBuilder();
			//TODO: don't use tesselator
			RenderSystem.enableDepthTest();
			RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
			RenderSystem._setShaderTexture(0, ((IWDBrowser) scr.browser).getRenderer().getTextureID());
			RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
			builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
			builder.vertex(poseStack.last().pose(), -sw, -sh, 0.505f).uv(0.f, 1.f).color(1.f, 1.f, 1.f, 1.f).endVertex();
			builder.vertex(poseStack.last().pose(), sw, -sh, 0.505f).uv(1.f, 1.f).color(1.f, 1.f, 1.f, 1.f).endVertex();
			builder.vertex(poseStack.last().pose(), sw, sh, 0.505f).uv(1.f, 0.f).color(1.f, 1.f, 1.f, 1.f).endVertex();
			builder.vertex(poseStack.last().pose(), -sw, sh, 0.505f).uv(0.f, 0.f).color(1.f, 1.f, 1.f, 1.f).endVertex();
			tesselator.end();//Minecraft does shit with mah texture otherwise...
			RenderSystem.disableDepthTest();
			
			// TODO: it'd be neat to draw a mouse cursor on the screen
//			// debug hit2pixels
//			HitResult result = Minecraft.getInstance().hitResult;
//			VertexConsumer consumer = bufferSource.getBuffer(RenderType.lines());
//			poseStack.translate(-sw, -sh, 0);
//			if (result instanceof BlockHitResult hit) {
//				BlockPos bpos = hit.getBlockPos();
//
//				Vector3i pos = new Vector3i(hit.getBlockPos());
//				float hitX = ((float) result.getLocation().x) - (float) te.getBlockPos().getX();
//				float hitY = ((float) result.getLocation().y) - (float) te.getBlockPos().getY();
//				float hitZ = ((float) result.getLocation().z) - (float) te.getBlockPos().getZ();
//				Vector2i tmp = new Vector2i();
//
//				if (BlockScreen.hit2pixels(scr.side, bpos, pos, scr, hitX, hitY, hitZ, tmp)) {
//					float x = tmp.x / (float) scr.resolution.x * scr.size.x;
//					float y = tmp.y / (float) scr.resolution.y * scr.size.y;
//					y = scr.size.y - y;
//
//					x /= scr.size.x;
//					y /= scr.size.y;
//					x *= sw * 2;
//					y *= sh * 2;
//
//					LevelRenderer.renderLineBox(
//							poseStack,
//							consumer, new AABB(
//									x - 0.01, y - 0.01, 0.5 - 0.01,
//									x + 0.01, y + 0.01, 0.5 + 0.01
//							),
//							1f, 0, 0, 1f
//					);
//				}
//			}
			
			poseStack.popPose();
		}


//        //Bounding box debugging
//        poseStack.pushPose();
//        poseStack.translate(-te.getBlockPos().getX(), -te.getBlockPos().getY(), -te.getBlockPos().getZ());
//        LevelRenderer.renderLineBox(
//                poseStack, bufferSource.getBuffer(RenderType.LINES),
//                te.getRenderBoundingBox(), 1, 1, 1, 1f
//        );
//        poseStack.popPose();
		
		//Re-enable lighting
//        RenderSystem.enableCull();
	}
}
