package primal_tech.client.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import primal_tech.ModBlocks;
import primal_tech.blocks.BlockStoneAnvil;
import primal_tech.tiles.TileEntityStoneAnvil;


public class TileEntityStoneAnvilRenderer extends TileEntitySpecialRenderer<TileEntityStoneAnvil> {

	@Override
	public void render(TileEntityStoneAnvil tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		IBlockState state = tile.getWorld().getBlockState(tile.getPos());

		if(state == null || state.getBlock() != ModBlocks.STONE_ANVIL)
			return;

		EnumFacing facing = state.getValue(BlockStoneAnvil.FACING);

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
			float yUp = -0.75F;
			if(stack.getItem() instanceof ItemBlock)
				yUp = -1F;
			renderItemInSlot(tile, tile.getStackInSlot(0), -0.75F, yUp, -0.25F, 0.5F, tile.itemRotation, tile.itemJump, tile.itemJumpPrev, partialTicks);
			GlStateManager.popMatrix();
		}

		GlStateManager.popMatrix();
	}

	public void renderItemInSlot(TileEntityStoneAnvil tile, ItemStack stack, float x, float y, float z, float scale, float rotation, float jump, float prevJump, float partialTicks) {
		if (!stack.isEmpty()) {
			float jumpUP = jump * 0.025F + (jump * 0.025F - prevJump * 0.025F) * partialTicks;
			GlStateManager.pushMatrix();
			bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
			GlStateManager.translate(x, y, z);
			GlStateManager.translate(0.75F, 0.52F - jumpUP, 0.25F);
			GlStateManager.scale(-scale, -scale, scale);
			GlStateManager.rotate(-90F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(-90F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(-90F + rotation, 0.0F, 0.0F, 1.0F);
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
