package primal_tech.client.render;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import primal_tech.tiles.TileEntityWoodenBasin;

@SideOnly(Side.CLIENT)
public class TileEntityWoodenBasinRenderer extends TileEntitySpecialRenderer<TileEntityWoodenBasin> {

	@Override
	public void render(TileEntityWoodenBasin tile, double x, double y, double z, float partialTick, int destroyStage, float alpha) {
		if(tile == null || !tile.hasWorld())
			return;
		
		float fluidLevel = tile.tank.getFluidAmount();
		float height = 0.0625F;
		if (fluidLevel > 0) {
			FluidStack fluidStack = new FluidStack(tile.tank.getFluid(), 100);
			height = (0.375F / tile.tank.getCapacity()) * tile.tank.getFluidAmount();

			TextureAtlasSprite fluidStillSprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(fluidStack.getFluid().getStill().toString());
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder buffer = tessellator.getBuffer();
			int fluidColor = fluidStack.getFluid().getColor(fluidStack);

			GlStateManager.disableLighting();
			GlStateManager.pushMatrix();
			GlStateManager.translate(x, y + 0.25F, z);
			GlStateManager.enableBlend();
			GlStateManager.enableAlpha();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			setGLColorFromInt(fluidColor);
			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			float xMax, zMax, xMin, zMin, yMin = 0;

			xMax = 1.625F;
			zMax = 1.625F;
			xMin = 0.375F;
			zMin = 0.375F;
			yMin = 0F;

			if (fluidLevel > Fluid.BUCKET_VOLUME * 2) {
				xMax = 1.75F;
				zMax = 1.75F;
				xMin = 0.25F;
				zMin = 0.25F;
				yMin = 0F;
			}

			renderCuboid(buffer, xMax, xMin, yMin, height, zMin, zMax, fluidStillSprite);
			tessellator.draw();
			GlStateManager.disableBlend();
			GlStateManager.popMatrix();
			GlStateManager.enableLighting();
		}
		
		int itemBob = tile.getItemBob();
		int stirProgress = tile.getStirProgress();
		int prevStirProgress = tile.getPrevStirProgress();
		float stirTicks = stirProgress + (stirProgress - prevStirProgress) * partialTick;
		double itemY = y + 0.25D + height;
		Random rand = new Random();
		rand.setSeed((long) (tile.getPos().getX() + tile.getPos().getY() + tile.getPos().getZ()));
		for (int i = 0; i <= 3; i++) {
			float randRot = rand.nextFloat() * 360.0F;
			double xo = -0.2D + rand.nextFloat() * 0.4D;
			double zo = -0.2D + rand.nextFloat() * 0.4D;
			double rot = (stirTicks < 90 && fluidLevel > 0 ? stirTicks * 4D + 45D : 45D) + randRot;
			GlStateManager.pushMatrix();
			GlStateManager.translate(x + 0.5D, 0, z + 0.5D);
			GlStateManager.rotate((float) -rot, 0, 1, 0);
			GlStateManager.translate(xo, 0, zo);
			renderItemInSlot(tile, i, 0, itemY, 0, fluidLevel > 0 ? (i % 2 == 0 ? (itemBob * 0.01D) : ((-itemBob + 20) * 0.01D)) : 0.0D, -rot);
			GlStateManager.popMatrix();
		}
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x + 0.5F, (float) y + 0.25F, (float) z + 0.5F);
		GlStateManager.rotate(stirTicks * 4, 0.0F, -1F, 0F);
		GlStateManager.rotate(35F, 1F, 0F, 0F);
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
		RenderHelper.disableStandardItemLighting();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.pushMatrix();
		GlStateManager.scale(0.125F, 0.6875F, 0.125F);
		Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(Blocks.LOG.getDefaultState(), 1.0F);
		GlStateManager.popMatrix();
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.popMatrix();
	}

	private void renderItemInSlot(TileEntityWoodenBasin tile, int slotIndex, double x, double y, double z, double itemBob, double rotation) {
		if (!tile.getStackInSlot(slotIndex).isEmpty()) {
			GlStateManager.pushMatrix();
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.translate(x, y, z);
			GlStateManager.scale(0.25D, 0.25D, 0.25D);
			GlStateManager.translate(0D, itemBob, 0D);
			GlStateManager.rotate((float) rotation, 0, 1, 0);
			Minecraft.getMinecraft().getRenderItem().renderItem(tile.getStackInSlot(slotIndex), TransformType.FIXED);
			GlStateManager.popMatrix();
		}
	}

	private void setGLColorFromInt(int color) {
		float red = (color >> 16 & 0xFF) / 255.0F;
		float green = (color >> 8 & 0xFF) / 255.0F;
		float blue = (color & 0xFF) / 255.0F;

		GlStateManager.color(red, green, blue, 1.0F);
	}

	private void renderCuboid(BufferBuilder buffer, float xMax, float xMin, float yMin, float height, float zMin, float zMax, TextureAtlasSprite textureAtlasSprite) {

		double uMin = (double) textureAtlasSprite.getMinU();
		double uMax = (double) textureAtlasSprite.getMaxU();
		double vMin = (double) textureAtlasSprite.getMinV();
		double vMax = (double) textureAtlasSprite.getMaxV();

		final double vHeight = vMax - vMin;

		// top only needed ;)
		addVertexWithUV(buffer, xMax, height, zMax, uMax, vMin);
		addVertexWithUV(buffer, xMax, height, zMin, uMin, vMin);
		addVertexWithUV(buffer, xMin, height, zMin, uMin, vMax);
		addVertexWithUV(buffer, xMin, height, zMax, uMax, vMax);

	}

	private void addVertexWithUV(BufferBuilder buffer, float x, float y, float z, double u, double v) {
		buffer.pos(x / 2f, y, z / 2f).tex(u, v).endVertex();
	}

	private void addVertexWithColor(BufferBuilder buffer, float x, float y, float z, float red, float green, float blue, float alpha) {
		buffer.pos(x / 2f, y, z / 2f).color(red, green, blue, alpha).endVertex();
	}
}