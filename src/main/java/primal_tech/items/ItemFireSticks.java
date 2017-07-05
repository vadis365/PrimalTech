package primal_tech.items;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import primal_tech.PrimalTech;
import primal_tech.network.FireSticksMessage;

public class ItemFireSticks extends Item {
	
	public ItemFireSticks() {
		maxStackSize = 1;
		setCreativeTab(PrimalTech.TAB);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
		list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.fire_sticks_1").getFormattedText());
		list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.fire_sticks_2").getFormattedText());
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		if (hasTag(stack)) {
			if (!isSelected) {
				if (stack.getTagCompound().getInteger("rubbingCount") != 0)
					stack.getTagCompound().setInteger("rubbingCount", 0);
				if (stack.getTagCompound().getBoolean("animate"))
					stack.getTagCompound().setBoolean("animate", false);
			}
		}
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			ItemStack mainHandItem = player.getHeldItem(EnumHand.MAIN_HAND);
			ItemStack offHandItem = player.getHeldItem(EnumHand.OFF_HAND);
			if (!mainHandItem.isEmpty() && !offHandItem.isEmpty()) {
				if (mainHandItem.getItem() == PrimalTech.FIRE_STICKS && offHandItem.getItem() == PrimalTech.FIRE_STICKS) {
					int count = mainHandItem.getTagCompound().getInteger("rubbingCount");
					if (mainHandItem.getTagCompound().getBoolean("animate")) {
						mainHandItem.getTagCompound().setInteger("rubbingCount", mainHandItem.getTagCompound().getInteger("rubbingCount") + 1);
						
						if (count == 10 || count == 30 || count == 50) {
							if (player.world.isRemote)
								player.swingArm(EnumHand.OFF_HAND);
						}

						if (count == 20 || count == 40 || count == 60) {
							if (player.world.isRemote)
								player.swingArm(EnumHand.MAIN_HAND);
						}
					}
					if (count >= 60) {
						mainHandItem.getTagCompound().setInteger("rubbingCount", 0);
						mainHandItem.getTagCompound().setBoolean("animate", false);

						RayTraceResult mop = this.rayTrace(world, player, false);
						if (mop != null && mop.typeOfHit == RayTraceResult.Type.BLOCK) {
							BlockPos clickPos = mop.getBlockPos();
							if (world.isBlockModifiable(player, clickPos)) {
								BlockPos targetPos = clickPos.offset(mop.sideHit);
								if (player.canPlayerEdit(targetPos, mop.sideHit, mainHandItem))
									PrimalTech.NETWORK_WRAPPER.sendToServer(new FireSticksMessage(player, targetPos));
							}
						}
					}
				}
			}
		}
	}

	@Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		IBlockState state = world.getBlockState(pos);
		ItemStack mainHandItem = player.getHeldItem(EnumHand.MAIN_HAND);
		ItemStack offHandItem = player.getHeldItem(EnumHand.OFF_HAND);

		if (state != null) {
			//eventually add capability here

			if (!mainHandItem.isEmpty() && !offHandItem.isEmpty()) {
				if (mainHandItem.getItem() == PrimalTech.FIRE_STICKS && offHandItem.getItem() == PrimalTech.FIRE_STICKS) {
					mainHandItem.getTagCompound().setInteger("rubbingCount", 0);
					mainHandItem.getTagCompound().setBoolean("animate", true);
					return EnumActionResult.SUCCESS;
				}
			}
		}
		return EnumActionResult.FAIL;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.NONE;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 60;
	}
	
	private boolean hasTag(ItemStack stack) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
			return false;
		}
		return true;
	}

}
