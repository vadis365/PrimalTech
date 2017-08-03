package primal_tech.client.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import primal_tech.tiles.TileEntityWorkStump;


public class TileEntityWorkStumpRenderer extends TileEntitySpecialRenderer<TileEntityWorkStump> {

	@Override
	public void render(TileEntityWorkStump toolJig, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if(toolJig == null || !toolJig.hasWorld())
			return;
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5D, y + 0.8D, z + 0.5D);
		GlStateManager.scale(0.3F, 0.3F, 0.3F);
		GlStateManager.rotate(90.0F * (-toolJig.rotation), 0.0F, 1.0F, 0.0F);
		GlStateManager.translate(-1.5F, -0.0F, -1.0F);
		if(!toolJig.getStackInSlot(0).isEmpty())
			renderItemInSlot(toolJig, toolJig.getStackInSlot(0), 1.625F, 0F, -0.125F, 0.5F, toolJig.itemRotation[0], toolJig.itemJump[0], toolJig.itemJumpPrev[0], partialTicks);
		if(!toolJig.getStackInSlot(1).isEmpty())
			renderItemInSlot(toolJig, toolJig.getStackInSlot(1), 1.625F, 0F, 0.75F, 0.5F, toolJig.itemRotation[1], toolJig.itemJump[1], toolJig.itemJumpPrev[1], partialTicks);
		if(!toolJig.getStackInSlot(2).isEmpty())
			renderItemInSlot(toolJig, toolJig.getStackInSlot(2), 1.625F, 0F, 1.625F, 0.5F, toolJig.itemRotation[2], toolJig.itemJump[2], toolJig.itemJumpPrev[2], partialTicks);
		if(!toolJig.getStackInSlot(3).isEmpty())
			renderItemInSlot(toolJig, toolJig.getStackInSlot(3), 0.75F, 0F, -0.125F, 0.5F, toolJig.itemRotation[3], toolJig.itemJump[3], toolJig.itemJumpPrev[3], partialTicks);
		if(!toolJig.getStackInSlot(4).isEmpty())
			renderItemInSlot(toolJig, toolJig.getStackInSlot(4), 0.75F, 0F, 0.75F, 0.5F, toolJig.itemRotation[4], toolJig.itemJump[4], toolJig.itemJumpPrev[4], partialTicks);
		if(!toolJig.getStackInSlot(5).isEmpty())
			renderItemInSlot(toolJig, toolJig.getStackInSlot(5), 0.75F, 0F, 1.625F, 0.5F, toolJig.itemRotation[5], toolJig.itemJump[5], toolJig.itemJumpPrev[5], partialTicks);
		if(!toolJig.getStackInSlot(6).isEmpty())
			renderItemInSlot(toolJig, toolJig.getStackInSlot(6), -0.125F, 0F, -0.125F, 0.5F, toolJig.itemRotation[6], toolJig.itemJump[6], toolJig.itemJumpPrev[6], partialTicks);
		if(!toolJig.getStackInSlot(7).isEmpty())
			renderItemInSlot(toolJig, toolJig.getStackInSlot(7), -0.125F, 0F, 0.75F, 0.5F, toolJig.itemRotation[7], toolJig.itemJump[7], toolJig.itemJumpPrev[7], partialTicks);
		if(!toolJig.getStackInSlot(8).isEmpty())
			renderItemInSlot(toolJig, toolJig.getStackInSlot(8), -0.125F, 0F, 1.625F, 0.5F, toolJig.itemRotation[8], toolJig.itemJump[8], toolJig.itemJumpPrev[8], partialTicks);
		if(!toolJig.getStackInSlot(9).isEmpty())
			renderItemInSlot(toolJig, toolJig.getStackInSlot(9), 0.25F, -1.875F, 0.75F, 1F, toolJig.itemRotation[4], toolJig.itemJump[8], toolJig.itemJumpPrev[8], partialTicks);
		GlStateManager.popMatrix();
	}

	public void renderItemInSlot(TileEntityWorkStump toolJig, ItemStack stack, float x, float y, float z, float scale, float rotation, float jump, float prevJump, float partialTicks) {
		if (!stack.isEmpty()) {
			float jumpUP = jump * 0.05F + (jump * 0.05F - prevJump * 0.05F) * partialTicks;
			GlStateManager.pushMatrix();
			GlStateManager.translate(x, y, z);
			GlStateManager.translate(0.75F, 0.52F + jumpUP, 0.25F);
			GlStateManager.scale(-scale, -scale, scale);
			GlStateManager.rotate(0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(90F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(90F + rotation, 0.0F, 0.0F, 1.0F);
			bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
			RenderHelper.disableStandardItemLighting();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			Minecraft.getMinecraft().getRenderItem().renderItem(stack, Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, (World) null, (EntityLivingBase) null));
			GlStateManager.enableLighting();
			GlStateManager.disableBlend();
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.popMatrix();
		}
	}

}
