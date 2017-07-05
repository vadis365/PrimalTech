package primal_tech.items;

import net.minecraft.init.Items;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import primal_tech.PrimalTech;

public class ItemBoneAxe extends ItemAxe {

	public ItemBoneAxe(ToolMaterial material) {
		super(material, material.getDamageVsEntity() + 7F, -3.0F);
		setCreativeTab(PrimalTech.TAB);
	}

	@Override
	public boolean getIsRepairable(ItemStack stack, ItemStack material) {
		return material.getItem() == Items.BONE;
	}
}
