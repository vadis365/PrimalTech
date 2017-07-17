package primal_tech.tiles;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import primal_tech.PrimalTech;
import primal_tech.blocks.BlockClayKiln;
import primal_tech.configs.ConfigHandler;
import primal_tech.inventory.InventoryWrapperKilnGrill;
import primal_tech.recipes.ClayKilnRecipes;

public class TileEntityKiln extends TileEntityInventoryHelper implements ITickable {
	public int temp = 0;
	public int progress = 0;
	public boolean active;
    private IItemHandler itemHandler;
	private static final int[] SLOTS = new int[] {0, 1};

	public TileEntityKiln() {
		super(2);
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
    public void update() {
        BlockPos pos = this.getPos();

        if (getWorld().isRemote) 
            return;

        if ((getWorld().getBlockState(pos.down()).getBlock() == Blocks.FIRE || isFireSource(getWorld().getBlockState(pos.down()).getBlock()) || getWorld().getBlockState(pos.down()).getBlock() == PrimalTech.CHARCOAL_HOPPER) && getTemp() < 200) {
        	setTemp(getTemp() + 1);
        	markForUpdate();
        }

        if ((getWorld().getBlockState(pos.down()).getBlock() != Blocks.FIRE && !isFireSource(getWorld().getBlockState(pos.down()).getBlock()) && getWorld().getBlockState(pos.down()).getBlock() != PrimalTech.CHARCOAL_HOPPER) && getTemp() > 0) {
        	setTemp(getTemp() - 1);
        	markForUpdate();
        }

        if (getWorld().getBlockState(pos) != null && !active && getTemp() >= 200) {
        	IBlockState state = getWorld().getBlockState(pos);
        	((BlockClayKiln) state.getBlock()).setState(getWorld(), pos);
        	setTemp(0);
        	markForUpdate();
        }

		if (getWorld().getBlockState(pos) != null && active && !getItems().get(0).isEmpty() && getTemp() >= 200) {
			setCookingDuration(getCookingDuration() + 1);
			if (getCookingDuration() >= ClayKilnRecipes.getCooktime(getItems().get(0))) {
				ItemStack output = ClayKilnRecipes.getOutput(getItems().get(0));
				if(!output.isEmpty() && output != getItems().get(0)) {
				getItems().get(0).shrink(1);
				setInventorySlotContents(1, output);
				setCookingDuration(0);
				markForUpdate();
				}
			}
		}
    }

	private static Boolean isFireSource(Block block) {
		List<Block> blockList = new ArrayList<Block>();
		for (int blocks = 0; blocks < ConfigHandler.FIRE_SOURCES.length; blocks++) {
			String entry = ConfigHandler.FIRE_SOURCES[blocks].trim();
			Block outBlock = Block.REGISTRY.getObject(new ResourceLocation(entry));
			blockList.add(outBlock);
		}
		if(blockList.contains(block))
			return true;
		return false;
	}

	public void setActive(boolean isActive) {
		active = isActive;
		markForUpdate();
	}

	public void setTemp(int temperature) {
		temp = temperature;
		markForUpdate();
	}

	public int getTemp() {
		return temp;
	}

	public void setCookingDuration(int duration) {
		progress = duration;
		markForUpdate();
	}

	private int getCookingDuration() {
		return progress;
	}

    public void markForUpdate() {
        IBlockState state = this.getWorld().getBlockState(this.getPos());
        this.getWorld().notifyBlockUpdate(this.getPos(), state, state, 2);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setBoolean("active", active);
		nbt.setInteger("temp", temp);
		nbt.setInteger("progress", progress);
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		active = nbt.getBoolean("active");
		temp = nbt.getInteger("temp");
		progress = nbt.getInteger("progress");
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
		getWorld().markBlockRangeForRenderUpdate(getPos(), getPos());
	}

	@Override
    public void setInventorySlotContents(int index, @Nullable ItemStack stack) {
		super.setInventorySlotContents(index, stack);
		markForUpdate();
    }
	
	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return SLOTS;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return index == 0;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return index == 1;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return getItems().get(index);
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return getItems().get(0).isEmpty();
	}

	// INVENTORY CAPABILITIES STUFF

	protected IItemHandler createUnSidedHandler() {
		return new InventoryWrapperKilnGrill(this);
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
