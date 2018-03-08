package primal_tech.items;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> list, ITooltipFlag flag) {
		list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.clubs").getFormattedText());
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