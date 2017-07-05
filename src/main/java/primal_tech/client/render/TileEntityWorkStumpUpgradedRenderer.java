package primal_tech.client.render;

import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import primal_tech.tiles.TileEntityWorkStumpUpgraded;

public class TileEntityWorkStumpUpgradedRenderer extends TileEntitySpecialRenderer<TileEntityWorkStumpUpgraded> {

	@Override
	public void render(TileEntityWorkStumpUpgraded toolJig, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if(toolJig == null || !toolJig.hasWorld())
			return;
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5D, y + 0.8D, z + 0.5D);
		GlStateManager.scale(0.3F, 0.3F, 0.3F);
		GlStateManager.rotate(90.0F * (-toolJig.rotation), 0.0F, 1F, 0.0F);
		GlStateManager.translate(-1.5F, 0F, -1.0F);
		if(!toolJig.getStackInSlot(0).isEmpty()) {
			renderItemInSlot(toolJig, toolJig.getStackInSlot(0), 1.625F, 0F, -0.125F, 0.5F, toolJig.itemRotation[0], toolJig.itemJump[0], toolJig.itemJumpPrev[0], partialTicks);
			renderStackCount(toolJig, ""+toolJig.getStackInSlot(0).getCount(), 1.625F, 0F, -0.125F);
		}
		if(!toolJig.getStackInSlot(1).isEmpty()) {
			renderItemInSlot(toolJig, toolJig.getStackInSlot(1), 1.625F, 0F, 0.75F, 0.5F, toolJig.itemRotation[1], toolJig.itemJump[1], toolJig.itemJumpPrev[1], partialTicks);
			renderStackCount(toolJig, ""+toolJig.getStackInSlot(1).getCount(), 1.625F, 0F, 0.75F);
		}
		if(!toolJig.getStackInSlot(2).isEmpty()){
			renderItemInSlot(toolJig, toolJig.getStackInSlot(2), 1.625F, 0F, 1.625F, 0.5F, toolJig.itemRotation[2], toolJig.itemJump[2], toolJig.itemJumpPrev[2], partialTicks);
			renderStackCount(toolJig, ""+toolJig.getStackInSlot(2).getCount(), 1.625F, 0F, 1.625F);
		}
		if(!toolJig.getStackInSlot(3).isEmpty()){
			renderItemInSlot(toolJig, toolJig.getStackInSlot(3), 0.75F, 0F, -0.125F, 0.5F, toolJig.itemRotation[3], toolJig.itemJump[3], toolJig.itemJumpPrev[3], partialTicks);
			renderStackCount(toolJig, ""+toolJig.getStackInSlot(3).getCount(), 0.75F, 0F, -0.125F);
		}
		if(!toolJig.getStackInSlot(4).isEmpty()){
			renderItemInSlot(toolJig, toolJig.getStackInSlot(4), 0.75F, 0F, 0.75F, 0.5F, toolJig.itemRotation[4], toolJig.itemJump[4], toolJig.itemJumpPrev[4], partialTicks);
			renderStackCount(toolJig, ""+toolJig.getStackInSlot(4).getCount(), 0.75F, 0F, 0.75F);
		}
		if(!toolJig.getStackInSlot(5).isEmpty()){
			renderItemInSlot(toolJig, toolJig.getStackInSlot(5), 0.75F, 0F, 1.625F, 0.5F, toolJig.itemRotation[5], toolJig.itemJump[5], toolJig.itemJumpPrev[5], partialTicks);
			renderStackCount(toolJig, ""+toolJig.getStackInSlot(5).getCount(), 0.75F, 0F, 1.625F);
		}
		if(!toolJig.getStackInSlot(6).isEmpty()){
			renderItemInSlot(toolJig, toolJig.getStackInSlot(6), -0.125F, 0F, -0.125F, 0.5F, toolJig.itemRotation[6], toolJig.itemJump[6], toolJig.itemJumpPrev[6], partialTicks);
			renderStackCount(toolJig, ""+toolJig.getStackInSlot(6).getCount(), -0.125F, 0F, -0.125F);
		}
		if(!toolJig.getStackInSlot(7).isEmpty()){
			renderItemInSlot(toolJig, toolJig.getStackInSlot(7), -0.125F, 0F, 0.75F, 0.5F, toolJig.itemRotation[7], toolJig.itemJump[7], toolJig.itemJumpPrev[7], partialTicks);
			renderStackCount(toolJig, ""+toolJig.getStackInSlot(7).getCount(), -0.125F, 0F, 0.75F);
		}
		if(!toolJig.getStackInSlot(8).isEmpty()){
			renderItemInSlot(toolJig, toolJig.getStackInSlot(8), -0.125F, 0F, 1.625F, 0.5F, toolJig.itemRotation[8], toolJig.itemJump[8], toolJig.itemJumpPrev[8], partialTicks);
			renderStackCount(toolJig, ""+toolJig.getStackInSlot(8).getCount(), -0.125F, 0F, 1.625F);
		}
		if(!toolJig.getStackInSlot(9).isEmpty())
			renderItemInSlot(toolJig, toolJig.getStackInSlot(9), 0.25F, -1.7F, 0.75F, 1F, toolJig.itemRotation[4], toolJig.itemJump[8], toolJig.itemJumpPrev[8], partialTicks);
		GlStateManager.popMatrix();
	}

	public void renderItemInSlot(TileEntityWorkStumpUpgraded toolJig, @Nullable ItemStack stack, float x, float y, float z, float scale, float rotation, float jump, float prevJump, float partialTicks) {
		if (!stack.isEmpty()) {
			float jumpUP = jump * 0.05F + (jump * 0.05F - prevJump * 0.05F) * partialTicks;
			GlStateManager.pushMatrix();
			bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
			GlStateManager.translate(x, y, z);
			GlStateManager.translate(0.75F, 0.52F + jumpUP, 0.25F);
			GlStateManager.scale(-scale, -scale, scale);
			GlStateManager.rotate(0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(90F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(90F + rotation, 0.0F, 0.0F, 1.0F);
			RenderHelper.disableStandardItemLighting();
			Minecraft.getMinecraft().getRenderItem().renderItem(stack, Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, (World) null, (EntityLivingBase) null));
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.popMatrix();
		}
	}

	private void renderStackCount(TileEntityWorkStumpUpgraded toolJig, String count, double x, double y, double z) {
		float scale = 0.02666667F;
		float height = 0.8F;
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.75D, y + 1D, z + 0.25D);
		GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
		GlStateManager.scale(-scale, -scale, scale);
		GlStateManager.rotate(-90F * (toolJig.rotation), 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
		GlStateManager.disableLighting();
		GlStateManager.depthMask(false);
		GlStateManager.disableDepth();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBuffer();
		GlStateManager.disableTexture2D();
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		FontRenderer fontrenderer = Minecraft.getMinecraft().fontRenderer;
		int width = fontrenderer.getStringWidth(count) / 2;
		vertexbuffer.pos(x - width - 1, y - 1, z).color(0F, 0F, 0, 0.25F).endVertex();
		vertexbuffer.pos(x - width - 1, y + 8, z).color(0F, 0F, 0, 0.25F).endVertex();
		vertexbuffer.pos(x + width + 1, y + 8, z).color(0F, 0F, 0, 0.25F).endVertex();
		vertexbuffer.pos(x + width + 1, y - 1, z).color(0F, 0F, 0, 0.25F).endVertex();
		tessellator.draw();
		GlStateManager.enableTexture2D();
		fontrenderer.drawString(count, -fontrenderer.getStringWidth(count) / 2, 0, 553648127);
		GlStateManager.enableDepth();
		GlStateManager.depthMask(true);
		fontrenderer.drawString(count, -fontrenderer.getStringWidth(count) / 2, 0, -1);
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.popMatrix();
	}

}
