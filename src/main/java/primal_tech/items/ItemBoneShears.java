package primal_tech.items;

import net.minecraft.init.Items;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import primal_tech.PrimalTech;

public class ItemBoneShears extends ItemShears {

	public ItemBoneShears() {
        setMaxStackSize(1);
        setMaxDamage(100);
		setCreativeTab(PrimalTech.TAB);
	}

	@Override
	public boolean getIsRepairable(ItemStack stack, ItemStack material) {
		return material.getItem() == Items.BONE;
	}
}