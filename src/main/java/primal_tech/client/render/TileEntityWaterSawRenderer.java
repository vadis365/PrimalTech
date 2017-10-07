package primal_tech.client.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import primal_tech.ModBlocks;
import primal_tech.blocks.BlockWaterSaw;
import primal_tech.client.models.ModelWaterSaw;
import primal_tech.tiles.TileEntityWaterSaw;

@SideOnly(Side.CLIENT)
public class TileEntityWaterSawRenderer extends TileEntitySpecialRenderer<TileEntityWaterSaw> {
	private static final ResourceLocation BASE_TEXTURE = new ResourceLocation("primal_tech:textures/blocks/water_saw.png");
	private final ModelWaterSaw saw_base = new ModelWaterSaw();

	public void renderTile(TileEntityWaterSaw tile, double x, double y, double z, float partialTick, int destroyStage, float alpha) {
		IBlockState state = tile.getWorld().getBlockState(tile.getPos());

		if(state == null || state.getBlock() != ModBlocks.WATER_SAW)
			return;

		EnumFacing facing = state.getValue(BlockWaterSaw.FACING);

		
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x + 0.5F, (float) y + 0.3125F, (float) z + 0.5F);
		GlStateManager.scale(-1, -1, 1);

		switch (facing) {
		case UP:
		case DOWN:
		case NORTH:
			GlStateManager.rotate(0F, 0F, 1F, 0F);
			break;
		case SOUTH:
			GlStateManager.rotate(180F, 0.0F, 1F, 0F);
			break;
		case WEST:
			GlStateManager.rotate(-90F, 0.0F, 1F, 0F);
			break;
		case EAST:
			GlStateManager.rotate(90F, 0.0F, 1F, 0F);
			break;
		}

		ItemStack stack = tile.getStackInSlot(0);
		if (!stack.isEmpty()) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(0F, -0.35F, -0.3125F + (tile.getChoppingTime() != 0 ? ((float)tile.getChoppingProgress() / (float)tile.getChoppingTime() * 0.6875F) : 0F));
			GlStateManager.scale(0.3125D, 0.3125D, 0.3125D);
			GlStateManager.rotate(90F, 1.0F, 0F, 0F);
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
		GlStateManager.pushMatrix();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.pushMatrix();
		bindTexture(BASE_TEXTURE);
		GlStateManager.translate(0F, -1.1875F, 0F);
		GlStateManager.disableCull();
		saw_base.renderCasing();
		GlStateManager.enableCull();
		GlStateManager.popMatrix();		
		
		GlStateManager.pushMatrix();
		float ticks = tile.animationTicks + (tile.animationTicks - tile.prevAnimationTicks)  * partialTick;
		GlStateManager.rotate(ticks, 1.0F, 0F, 0F);
		
		GlStateManager.pushMatrix();
		bindTexture(BASE_TEXTURE);
		GlStateManager.translate(0F, -1.1875F, 0F);
		GlStateManager.disableCull();
		saw_base.render();
		GlStateManager.enableCull();
		GlStateManager.popMatrix();
		
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
		GlStateManager.popMatrix();
		GlStateManager.popMatrix();

	}

	@Override
	public void render(TileEntityWaterSaw te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if(te == null || !te.hasWorld()) {
			renderTileAsItem(x, y, z);
			return;
		}
		renderTile(te, x, y, z, partialTicks, destroyStage, alpha);
	}

	private void renderTileAsItem(double x, double y, double z) {
		GlStateManager.pushMatrix();
		bindTexture(BASE_TEXTURE);
		GlStateManager.translate((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
		GlStateManager.scale(-1, -1, 1);
		GlStateManager.disableCull();
		saw_base.render();
		saw_base.renderCasing();
		GlStateManager.enableCull();
		GlStateManager.popMatrix();
	}

}