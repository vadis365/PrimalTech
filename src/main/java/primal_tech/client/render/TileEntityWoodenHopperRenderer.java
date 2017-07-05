package primal_tech.client.render;

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
import primal_tech.tiles.TileEntityWoodenHopper;

public class TileEntityWoodenHopperRenderer extends TileEntitySpecialRenderer<TileEntityWoodenHopper> {

	@Override
	public void render(TileEntityWoodenHopper hopper, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if(hopper == null || !hopper.hasWorld())
			return;
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5D, y + 1.625D, z + 0.5D);
		GlStateManager.scale(0.5F, 0.5F, 0.5F);
		GlStateManager.rotate(90.0F * (-hopper.rotation), 0.0F, 1.0F, 0.0F);
		GlStateManager.translate(-2.5F, -1F, -1.0F);
		if(!hopper.getStackInSlot(0).isEmpty()) {
			renderItemInSlot(hopper, hopper.getStackInSlot(0), 2.875F, -0.625F, 0.625F, 0.5F);
			renderStackCount(hopper, ""+hopper.getStackInSlot(0).getCount(), 2.875F, -0.3F, 0.625F);
		}
		if(!hopper.getStackInSlot(1).isEmpty()) {
			renderItemInSlot(hopper, hopper.getStackInSlot(1), 2.875F, -0.625F, 1.3625F, 0.5F);
			renderStackCount(hopper, ""+hopper.getStackInSlot(1).getCount(), 2.875F, -0.3F, 1.3625F);
		}
		if(!hopper.getStackInSlot(2).isEmpty()) {
			renderItemInSlot(hopper, hopper.getStackInSlot(2), 2.125F, -0.625F, 0.625F, 0.5F);
			renderStackCount(hopper, ""+hopper.getStackInSlot(2).getCount(), 2.125F, -0.3F, 0.625F);
		}
		if(!hopper.getStackInSlot(3).isEmpty()) {
			renderItemInSlot(hopper, hopper.getStackInSlot(3), 2.125F, -0.625F, 1.3625F, 0.5F);
			renderStackCount(hopper, ""+hopper.getStackInSlot(3).getCount(), 2.125F, -0.3F, 1.3625F);
		}
		GlStateManager.popMatrix();
	}

	public void renderItemInSlot(TileEntityWoodenHopper hopper, ItemStack stack, float x, float y, float z, float scale) {
		if (!stack.isEmpty()) {
			GlStateManager.pushMatrix();
			bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
			GlStateManager.translate(x, y, z);
			GlStateManager.scale(-scale, -scale, scale);
			GlStateManager.rotate(-90F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(180F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(0F, 0.0F, 0.0F, 1.0F);
			RenderHelper.disableStandardItemLighting();
			Minecraft.getMinecraft().getRenderItem().renderItem(stack, Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, (World) null, (EntityLivingBase) null));
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.popMatrix();
		}
	}

	private void renderStackCount(TileEntityWoodenHopper hopper, String count, double x, double y, double z) {
		float scale = 0.02666667F;
		float height = 0.8F;
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y + 0.25D, z);
		GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
		GlStateManager.scale(-scale, -scale, scale);
		GlStateManager.rotate(-90F * (hopper.rotation), 0.0F, 1.0F, 0.0F);
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
