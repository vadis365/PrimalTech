package primal_tech.items;

import net.minecraft.init.Items;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import primal_tech.PrimalTech;

public class ItemBonePickaxe extends ItemPickaxe {

	public ItemBonePickaxe(ToolMaterial material) {
		super(material);
		setCreativeTab(PrimalTech.TAB);
	}

	@Override
	public boolean getIsRepairable(ItemStack stack, ItemStack material) {
		return material.getItem() == Items.BONE;
	}
}
