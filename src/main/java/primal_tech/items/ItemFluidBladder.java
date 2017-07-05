package primal_tech.items;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.ItemFluidContainer;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;
import primal_tech.configs.ConfigHandler;

public class ItemFluidBladder extends ItemFluidContainer {

	public ItemFluidBladder(int capacity) {
		super(capacity);
		setHasSubtypes(true);
		setMaxStackSize(1);
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> list, ITooltipFlag flag) {
		if(stack.hasTagCompound() && stack.getTagCompound().hasKey("Fluid")) {
			FluidStack fluid = FluidStack.loadFluidStackFromNBT(stack.getTagCompound().getCompoundTag("Fluid"));
			if(fluid !=null) {
				list.add(TextFormatting.GREEN + "Contains: "+ fluid.getFluid().getLocalizedName(fluid));
				list.add(TextFormatting.BLUE + ""+ fluid.amount + "Mb");
			}
		}
		else
			list.add(TextFormatting.RED + "It's Empty!");
	}

	@Override
	public boolean hasContainerItem() {
		 return true;
	}

	@Override
	public ItemStack getContainerItem(ItemStack stack) {
		return new ItemStack(stack.getItem(), 1, 0);
	}

	@Nullable
	public static FluidStack getFluid(@Nonnull ItemStack stack) {
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Fluid"))
			return FluidStack.loadFluidStackFromNBT(stack.getTagCompound().getCompoundTag("Fluid"));
		return FluidStack.loadFluidStackFromNBT(stack.getTagCompound());
	}

	@Override
	@Nonnull
	public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, @Nonnull EntityPlayer player, @Nonnull EnumHand hand) {
		ItemStack itemstack = player.getHeldItem(hand);
		FluidStack fluidStack = getFluid(itemstack);
		if (fluidStack == null) {
			RayTraceResult target = this.rayTrace(world, player, true);
			if (target == null || target.typeOfHit != RayTraceResult.Type.BLOCK)
				return ActionResult.newResult(EnumActionResult.PASS, itemstack);
			BlockPos pos = target.getBlockPos();
			FluidActionResult filledResult = FluidUtil.tryPickUpFluid(itemstack, player, world, pos, target.sideHit);
			if (filledResult.isSuccess()) {
				ItemStack filledStack = filledResult.getResult().copy();
				filledStack.setItemDamage(1);
				return ActionResult.newResult(EnumActionResult.SUCCESS, filledStack);
			}else
				return ActionResult.newResult(EnumActionResult.PASS, itemstack);
		} else if (ConfigHandler.FLUID_BLADDER_PLACES_FLUID) {
			RayTraceResult mop = this.rayTrace(world, player, false);
			if (mop == null || mop.typeOfHit != RayTraceResult.Type.BLOCK)
				return ActionResult.newResult(EnumActionResult.PASS, itemstack);
			BlockPos clickPos = mop.getBlockPos();
			if (world.isBlockModifiable(player, clickPos)) {
				BlockPos targetPos = clickPos.offset(mop.sideHit);
				if (player.canPlayerEdit(targetPos, mop.sideHit, itemstack)) {
					FluidActionResult result = FluidUtil.tryPlaceFluid(player, world, targetPos, itemstack, fluidStack);
					if (result.isSuccess() && !player.capabilities.isCreativeMode) {
						player.addStat(StatList.getObjectUseStats(this));
						if (!world.isRemote) {
							IBlockState state = world.getBlockState(targetPos);
							if (state.getBlock() instanceof BlockLiquid) {
								BlockLiquid liquid = (BlockLiquid) state.getBlock();
								Block flowing = liquid.getFlowingBlock(state.getMaterial());
								world.setBlockState(targetPos, flowing.getDefaultState(), 11);
							}
						}
						itemstack.shrink(1);
						ItemStack drained = result.getResult();
						ItemStack drainedStack = result.getResult().copy();
						drainedStack.setItemDamage(0);
						ItemStack emptyStack = !drained.isEmpty() ? drained.copy() : drainedStack;
						if (itemstack.isEmpty())
							return ActionResult.newResult(EnumActionResult.SUCCESS, drainedStack);
						else {
							ItemHandlerHelper.giveItemToPlayer(player, drainedStack);
							return ActionResult.newResult(EnumActionResult.SUCCESS, itemstack);
						}

					}
				}
			}
		}
		return ActionResult.newResult(EnumActionResult.FAIL, itemstack);
	}

	@Override
	public ICapabilityProvider initCapabilities(@Nonnull final ItemStack stack, NBTTagCompound nbt) {
		return new FluidHandlerItemStack.SwapEmpty(stack, stack, capacity) {

			@Nonnull
			@Override
			public ItemStack getContainer() {
				if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Fluid"))
					container.setItemDamage(1);
				else
					container.setItemDamage(0);
				return container;
			}

			@Override
			protected void setContainerToEmpty() {
				if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Fluid")) {
					container.getTagCompound().removeTag("Fluid");
					container.setItemDamage(0);
				} else
					container.setItemDamage(1);
			}
		};
	}
}