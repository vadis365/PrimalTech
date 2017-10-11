package primal_tech.tiles;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import primal_tech.configs.ConfigHandler;
import primal_tech.recipes.WoodenBasinRecipes;

public class TileEntityWoodenBasin extends TileEntityInventoryHelper implements ITickable {
    public FluidTankTile tank;
	private IItemHandler itemHandler;
    private int stirProgress = 0;
    private int prevStirProgress = 0;
    private int itemBob = 0;
    private int stirCount = 0;
	private boolean countUp = true;
	private boolean mixing = false;
	public TileEntityWoodenBasin() {
		super(4);
        this.tank = new FluidTankTile(null, Fluid.BUCKET_VOLUME * 3);
        this.tank.setTileEntity(this);
	}

	@Override
	public void update() {
		prevStirProgress = stirProgress;
		if (getWorld().isRemote) {
			
			if (countUp && itemBob <= 20) {
				itemBob++;
				if (itemBob == 20)
					countUp = false;
			}
			if (!countUp && itemBob >= 0) {
				itemBob--;
				if (itemBob == 0)
					countUp = true;
			}
		}

		if (getMixing() && stirProgress < 90)
			stirProgress += 2;
		if (stirProgress >= 90) {
			setMixing(false);
			stirProgress = 0;
			prevStirProgress = 0;
		}

		if (!getWorld().isRemote) {
			if (getStirCount() >= ConfigHandler.WOODEN_BASIN_STIRS && tank.getFluidAmount() >= Fluid.BUCKET_VOLUME && stirProgress >= 88) {
				ItemStack output = ItemStack.EMPTY;
				WoodenBasinRecipes recipe = WoodenBasinRecipes.getRecipe(tank, getItems().get(0), getItems().get(1), getItems().get(2), getItems().get(3));
				if (recipe == null) {
					setStirCount(0);
					return;
				}
				if (recipe != null) {
					output = recipe.getOutput();
					for (int index = 0; index < 4; index++)
						if (!getItems().get(index).isEmpty())
							setInventorySlotContents(index, ItemStack.EMPTY);
					tank.drain(Fluid.BUCKET_VOLUME, true);
					setStirCount(0);
					spawnItemStack(getWorld(), pos.getX() + 0.5D, pos.getY() + 1D, pos.getZ() + 0.5D, output);
					EntityXPOrb orb = new EntityXPOrb(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 1);
					world.spawnEntity(orb);
				}
			}
		}
	}

	public static void spawnItemStack(World world, double x, double y, double z, ItemStack stack) {
		EntityItem entityitem = new EntityItem(world, x, y, z, stack);
        entityitem.motionX = 0D;
        entityitem.motionY = 0D;
        entityitem.motionZ = 0D;
		entityitem.setPickupDelay(20);
		world.spawnEntity(entityitem);
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}

	public boolean getMixing() {
		return mixing;
	}

	public void setMixing(boolean mix) {
		mixing = mix;
		markForUpdate();
	}

	public void setStirCount(int stirs) {
		this.stirCount = stirs;
		markForUpdate();
	}

	public int getStirCount() {
		return stirCount;
	}

	public int getStirProgress() {
		return stirProgress;
	}
	
	public int getPrevStirProgress() {
		return prevStirProgress;
	}

	public int getItemBob() {
		return itemBob;
	}

    public void markForUpdate() {
        IBlockState state = this.getWorld().getBlockState(this.getPos());
        this.getWorld().notifyBlockUpdate(this.getPos(), state, state, 2);
    }

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		super.onDataPacket(net, packet);
		readFromNBT(packet.getNbtCompound());
		tank.onContentsChanged();
		return;
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		return new SPacketUpdateTileEntity(getPos(), 0, tag);
	}

	@Override
    public NBTTagCompound getUpdateTag() {
		NBTTagCompound tag = new NBTTagCompound();
        return writeToNBT(tag);
    }

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		tank.readFromNBT(tagCompound);
		mixing = tagCompound.getBoolean("mixing");
		stirCount = tagCompound.getInteger("stirCount");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		tank.writeToNBT(tagCompound);
		tagCompound.setBoolean("mixing", getMixing());
		tagCompound.setInteger("stirCount", stirCount);
		return tagCompound;
	}

	// INVENTORY CAPABILITIES STUFF

	protected IItemHandler createUnSidedHandler() {
		return new InvWrapper(this);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return (T) tank;
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (T) (itemHandler == null ? (itemHandler = createUnSidedHandler()) : itemHandler);
		return super.getCapability(capability, facing);
	}

	// INVENTORY LEGACY SUPPORT

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		int[] SLOTS = new int[getSizeInventory()];
		for (int index = 0; index < SLOTS.length; index++)
			SLOTS[index] = index;
		return SLOTS;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return true;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return true;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return null;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}
}
