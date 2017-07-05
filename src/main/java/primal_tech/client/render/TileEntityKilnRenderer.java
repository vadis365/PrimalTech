package primal_tech.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import primal_tech.tiles.TileEntityKiln;

@SideOnly(Side.CLIENT)
public class TileEntityKilnRenderer extends TileEntitySpecialRenderer<TileEntityKiln> {

	private final RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();

	@Override
	public void render(TileEntityKiln tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if(tile == null || !tile.hasWorld())
			return;
		// Item
		int activeSlot = !tile.getStackInSlot(1).isEmpty() ? 1 : 0;
		ItemStack stack = tile.getStackInSlot(activeSlot);
		if (!stack.isEmpty()) {
			int meta = tile.getBlockMetadata();
			GlStateManager.pushMatrix();
			GlStateManager.translate(x + 0.5D, y + 0.3D, z + 0.5D);
			GlStateManager.scale(0.35D, 0.35D, 0.35D);
			if (meta == 2 || meta == 10)
				GlStateManager.rotate(180, 0, 1, 0);
			if (meta == 3 || meta == 11)
				GlStateManager.rotate(0, 0, 1, 0);
			if (meta == 4 || meta == 12)
				GlStateManager.rotate(270, 0, 1, 0);
			if (meta == 5 || meta == 13)
				GlStateManager.rotate(90, 0, 1, 0);
			bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
			Minecraft.getMinecraft().getRenderItem().renderItem(stack, Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, (World) null, (EntityLivingBase) null));
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.popMatrix();
			//could probably shrink this down :P
			GlStateManager.pushMatrix();
			GlStateManager.translate(x + 0.5D, y + 0.875D, z + 0.5D);
			GlStateManager.scale(0.152D, 0.125D, 0.125D);
			if (meta == 2 || meta == 10)
				GlStateManager.rotate(180, 0, 1, 0);
			if (meta == 3 || meta == 11)
				GlStateManager.rotate(0, 0, 1, 0);
			if (meta == 4 || meta == 12)
				GlStateManager.rotate(270, 0, 1, 0);
			if (meta == 5 || meta == 13)
				GlStateManager.rotate(90, 0, 1, 0);
			bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
			Minecraft.getMinecraft().getRenderItem().renderItem(stack, Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, (World) null, (EntityLivingBase) null));
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.popMatrix();
		}
	}
}