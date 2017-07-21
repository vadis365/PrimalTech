package primal_tech.tiles;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
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
import primal_tech.configs.ConfigHandler;
import primal_tech.inventory.InventoryWrapperKilnGrill;

public class TileEntityStoneGrill extends TileEntityInventoryHelper implements ITickable {
    private IItemHandler itemHandler;
	private static final int[] SLOTS = new int[] {0, 1};
	public int temp = 0;
	public boolean active;
	public byte rotation = 0;
	public TileEntityStoneGrill() {
		super(2);
	}

	@Override
    public void update() {
        BlockPos pos = this.getPos();

        if (getWorld().isRemote) 
            return;

        if ((getWorld().getBlockState(pos.down()).getBlock() == PrimalTech.CHARCOAL_HOPPER || getWorld().getBlockState(pos.down()).getBlock() == Blocks.FIRE || isFireSource(getWorld().getBlockState(pos.down()).getBlock())) && getTemp() < 200 && canSmelt()) {
        	setTemp(getTemp() + 1);
        	markForUpdate();
        }

        if ((getWorld().getBlockState(pos.down()).getBlock() != PrimalTech.CHARCOAL_HOPPER && getWorld().getBlockState(pos.down()).getBlock() != Blocks.FIRE && !isFireSource(getWorld().getBlockState(pos.down()).getBlock())) && getTemp() > 0) {
        	setTemp(getTemp() - 1);
        	markForUpdate();
        }

		if (getWorld().getBlockState(pos) != null && getTemp() >= 200 && canSmelt()) {
			smeltItem();
			setTemp(0);
			markForUpdate();
		}
		
		if(!canSmelt())
			setTemp(0);
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

	private boolean canSmelt() {
		if (getItems().get(0).isEmpty())
			return false;
		else {
			ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(getItems().get(0));

			if (itemstack.isEmpty())
				return false;
			else {
				ItemStack itemstack1 = getItems().get(1);
				if (itemstack1.isEmpty())
					return true;
				if (!itemstack1.isItemEqual(itemstack))
					return false;
				int result = itemstack1.getCount() + itemstack.getCount();
				return result <= 16;
			}
		}
	}

    public void smeltItem() {
		if (this.canSmelt()) {
			ItemStack itemstack = getItems().get(0);
			ItemStack itemstack1 = FurnaceRecipes.instance().getSmeltingResult(itemstack);
			ItemStack itemstack2 = getItems().get(1);

			if (itemstack2.isEmpty())
				getItems().set(1, itemstack1.copy());

			else if (itemstack2.getItem() == itemstack1.getItem())
				itemstack2.grow(itemstack1.getCount());
			itemstack.shrink(1);
		}
    }

	public void setTemp(int temperature) {
		temp = temperature;
		markForUpdate();
	}
	
	public int getTemp() {
		return temp;
	}

    public void markForUpdate() {
        IBlockState state = this.getWorld().getBlockState(this.getPos());
        this.getWorld().notifyBlockUpdate(this.getPos(), state, state, 2);
    }
    
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("temp", temp);
		nbt.setByte("rotation", rotation);
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		temp = nbt.getInteger("temp");
		rotation = nbt.getByte("rotation");
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
		return true;
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
