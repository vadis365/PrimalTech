package primal_tech.items;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import primal_tech.PrimalTech;

public class ItemClub extends ItemTool {
	ToolMaterial material;
	Item repairItem;

	public ItemClub(ToolMaterial materialIn, Item itemIn) {
		super(1F, 1F, materialIn, null);
		setCreativeTab(PrimalTech.TAB);
		this.material = materialIn;
		this.repairItem = itemIn;
	}

	@Override
	  public int getHarvestLevel(ItemStack stack, String toolClass, EntityPlayer player, IBlockState blockState) {
		if ("pickaxe".equals(toolClass) || "axe".equals(toolClass) || "shovel".equals(toolClass))
			return material.getHarvestLevel();
		return -1;
	}

	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state) {
		if (isToolEffective(state))
			return material.getEfficiency();
		return 1.0F;
	}

	public boolean isToolEffective(IBlockState state) {
		return state.getBlock().isToolEffective("pickaxe", state) || state.getBlock().isToolEffective("axe", state) || state.getBlock().isToolEffective("shovel", state);
	}

	@Override
	public boolean getIsRepairable(ItemStack stack, ItemStack material) {
		return repairItem != null && material.getItem() == repairItem;
	}
}