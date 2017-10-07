package primal_tech.tiles;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import primal_tech.blocks.BlockWaterSaw;
import primal_tech.recipes.WaterSawRecipes;

public class TileEntityWaterSaw extends TileEntityInventoryHelper implements ITickable {
    private IItemHandler itemHandler;
	private static final int[] SLOTS = new int[] {0};
	public boolean active;
	public int animationTicks, prevAnimationTicks, progress;
	public TileEntityWaterSaw() {
		super(1);
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
    public void update() {
		if (getWorld().isRemote && getActive()) {
			prevAnimationTicks = animationTicks;
			if (animationTicks < 360)
				animationTicks += 10;
			if (animationTicks >= 360) {
				animationTicks -= 360;
				prevAnimationTicks -= 360;
			}
		}
		
		if (getWorld().isRemote && !getActive())
			prevAnimationTicks = animationTicks;

		if (!getWorld().isRemote) {
			if (getWorld().getBlockState(pos) != null && getActive() && !getItems().get(0).isEmpty() && getChoppingTime() != 0) {
				setChoppingProgress(getChoppingProgress() + 1);
				if(getChoppingProgress()%5 == 0)
					world.playSound((EntityPlayer)null, pos, SoundEvents.BLOCK_FENCE_GATE_OPEN, SoundCategory.BLOCKS, 0.25F, 0.5F);
				if (getChoppingProgress() >= getChoppingTime()) {
					ItemStack output = WaterSawRecipes.getOutput(getItems().get(0));
					if (!output.isEmpty() && output != getItems().get(0)) {
						getItems().get(0).shrink(1);
						spawnItemStack(getWorld(), pos.getX() + 0.5D, pos.getY() + 1D, pos.getZ() + 0.5D, output);
						setChoppingProgress(0);
					}
				}
			}
			// meh - neighbour block changes don't do what I need - so this horrible check is here!
			if (getWorld().getTotalWorldTime() % 10 == 0) {
				if (!getActive() && getWaterFlow(getWorld().getBlockState(pos)))
					setActive(true);

				if (getActive() && !getWaterFlow(getWorld().getBlockState(pos)))
					setActive(false);
			}
		}
    }

	private boolean getWaterFlow(IBlockState state) {
		EnumFacing enumfacing = ((EnumFacing) state.getValue(BlockWaterSaw.FACING));
		IBlockState blockBelow = getWorld().getBlockState(pos.down());
		IBlockState blockBelowBack = getWorld().getBlockState(pos.down().offset(enumfacing.getOpposite()));
		if(blockBelow.getBlock() == Blocks.WATER && blockBelow.getBlock().getMetaFromState(blockBelow) == 0)
			if(blockBelowBack.getBlock() == Blocks.WATER && blockBelowBack.getBlock().getMetaFromState(blockBelowBack) == 1)
				return true;
		return false;
	}

	public static void spawnItemStack(World world, double x, double y, double z, ItemStack stack) {
		EntityItem entityitem = new EntityItem(world, x, y, z, stack);
        entityitem.motionX = 0D;
        entityitem.motionY = 0D;
        entityitem.motionZ = 0D;
		entityitem.setPickupDelay(40);
		world.spawnEntity(entityitem);
	}

	public void setChoppingProgress(int choppingProgress) {
		progress = choppingProgress;
		markForUpdate();
	}

	public int getChoppingProgress() {
		return progress;
	}

	public int getChoppingTime() {
		return WaterSawRecipes.getChopTime(getItems().get(0));
	}

	public boolean getActive() {
		return active;
	}

	public void setActive(boolean isActive) {
		active = isActive;
		markForUpdate();
	}

    public void markForUpdate() {
        IBlockState state = this.getWorld().getBlockState(this.getPos());
        this.getWorld().notifyBlockUpdate(this.getPos(), state, state, 2);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("progress", progress);
		nbt.setBoolean("active", active);
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		progress = nbt.getInteger("progress");
		active = nbt.getBoolean("active");
	}

	@Override
    public NBTTagCompound getUpdateTag() {
		NBTTagCompound nbt = new NBTTagCompound();
        return writeToNBT(nbt);
    }

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return new SPacketUpdateTileEntity(pos, 0, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		readFromNBT(packet.getNbtCompound());
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return SLOTS;
	}

	@Override
    public void setInventorySlotContents(int index, @Nullable ItemStack stack) {
		super.setInventorySlotContents(index, stack);
		markForUpdate();
    }

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return index == 0;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return index == 0;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return getItems().get(index);
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	// INVENTORY CAPABILITIES STUFF

	protected IItemHandler createUnSidedHandler() {
		return new InvWrapper(this);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (T) (itemHandler == null ? (itemHandler = createUnSidedHandler()) : itemHandler);
		return super.getCapability(capability, facing);
	}

}
