package primal_tech.tiles;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import primal_tech.blocks.BlockWoodenHopper;
import primal_tech.inventory.WoodenHopperItemHandler;

public class TileEntityWoodenHopper extends TileEntityInventoryHelper implements ITickable, IHopper {
	private static final int[] SLOTS = new int[] {0, 1, 2, 3};
	public byte rotation = 0;
	public TileEntityWoodenHopper() {
		super(4);
	}

    private int transferCooldown = -1;
    private long tickedGameTime;

    public static void registerFixesHopper(DataFixer fixer) {
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityWoodenHopper.class, new String[] {"Items"}));
    }

    public void markForUpdate() {
        IBlockState state = getWorld().getBlockState(getPos());
        getWorld().notifyBlockUpdate(getPos(), state, state, 3);
    }

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
        transferCooldown = nbt.getInteger("TransferCooldown");
		rotation = nbt.getByte("rotation");
    }

	@Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
        nbt.setInteger("TransferCooldown", transferCooldown);
        nbt.setByte("rotation", rotation);
        return nbt;
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
    public ItemStack decrStackSize(int index, int count) {
        ItemStack itemstack = ItemStackHelper.getAndSplit(getItems(), index, count);
        return itemstack;
    }

	@Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        getItems().set(index, stack);

        if (stack.getCount() > getInventoryStackLimit())
            stack.setCount(getInventoryStackLimit());
    }

	@Override
    public String getName() {
        return  "container.hopper";
    }
 
	@Override
    public int getInventoryStackLimit() {
        return 64;
    }

	@Override
    public void update() {
        if (world != null && !world.isRemote) {
            --transferCooldown;
            tickedGameTime = world.getTotalWorldTime();
            if (!isOnTransferCooldown()) {
                setTransferCooldown(0);
                updateHopper();  
            }
            markForUpdate();
        }
    }

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
	return oldState.getBlock() != newState.getBlock();
	}

    private boolean updateHopper() {
        if (world != null && !world.isRemote) {
            if (!isOnTransferCooldown() && BlockWoodenHopper.isEnabled(getBlockMetadata())) {
                boolean flag = false;
                if (!isInventoryEmpty())
                    flag = transferItemsOut();
                if (!isFull())
                    flag = captureDroppedItems(this) || flag;
                if (flag) {
                    setTransferCooldown(8);
                    markDirty();
                    return true;
                }
            }
            return false;
        }
        else
            return false;
    }

    private boolean isInventoryEmpty() {
        for (ItemStack itemstack : getItems()) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean isEmpty() {
        return isInventoryEmpty();
    }

    private boolean isFull() {
        for (ItemStack itemstack : getItems()) {
            if (itemstack.isEmpty() || itemstack.getCount() != itemstack.getMaxStackSize())
                return false;
        }
        return true;
    }

	private boolean transferItemsOut() {
		IInventory iinventory = getInventoryForHopperTransfer();
		EnumFacing enumfacing = BlockWoodenHopper.getFacing(getBlockMetadata()).getOpposite();
		TileEntity tile = getWorld().getTileEntity(pos.offset(enumfacing.getOpposite()));

		if (tile != null && tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, enumfacing)) {
			IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, enumfacing);
			for (int i = 0; i < this.getSizeInventory(); ++i) {
				if (!getStackInSlot(i).isEmpty()) {
					ItemStack stack = getStackInSlot(i).copy();
					stack.setCount(1);
					ItemStack stack1 = ItemHandlerHelper.insertItem(handler, stack, true);
					if (stack1.isEmpty()) {
						ItemHandlerHelper.insertItem(handler, this.decrStackSize(i, 1), false);
						this.markDirty();
						return true;
					}
				}
			}
		} else {
			if (iinventory == null)
				return false;
			if (isInventoryFull(iinventory, enumfacing)) {
				return false;
			} else {
				for (int i = 0; i < this.getSizeInventory(); ++i) {
					if (!getStackInSlot(i).isEmpty()) {
						ItemStack stack = getStackInSlot(i).copy();
						ItemStack stack1 = putStackInInventoryAllSlots(this, iinventory, decrStackSize(i, 1), enumfacing.getOpposite());
						if (stack1.isEmpty()) {
							iinventory.markDirty();
							return true;
						} else
							setInventorySlotContents(i, stack);
					}
				}

			}
		}
		return false;
	}

    private boolean isInventoryFull(IInventory inventoryIn, EnumFacing side) {
        if (inventoryIn instanceof ISidedInventory) {
            ISidedInventory isidedinventory = (ISidedInventory)inventoryIn;
            int[] aint = isidedinventory.getSlotsForFace(side);
            for (int k : aint) {
                ItemStack itemstack1 = isidedinventory.getStackInSlot(k);
                if (itemstack1.isEmpty() || itemstack1.getCount() != itemstack1.getMaxStackSize())
                    return false;
            }
        }
        else {
            int i = inventoryIn.getSizeInventory();
            for (int j = 0; j < i; ++j) {
                ItemStack itemstack = inventoryIn.getStackInSlot(j);
                if (itemstack.isEmpty() || itemstack.getCount() != itemstack.getMaxStackSize())
                    return false;
            }
        }

        return true;
    }

    private static boolean isInventoryEmpty(IInventory inventoryIn, EnumFacing side) {
        if (inventoryIn instanceof ISidedInventory) {
            ISidedInventory isidedinventory = (ISidedInventory)inventoryIn;
            int[] aint = isidedinventory.getSlotsForFace(side);

            for (int i : aint) {
                if (!isidedinventory.getStackInSlot(i).isEmpty())
                    return false;
            }
        }
        else {
            int j = inventoryIn.getSizeInventory();
            for (int k = 0; k < j; ++k) {
                if (!inventoryIn.getStackInSlot(k).isEmpty())
                    return false;
            }
        }
        return true;
    }

    public static boolean captureDroppedItems(IHopper hopper) {
        Boolean ret = net.minecraftforge.items.VanillaInventoryCodeHooks.extractHook(hopper);
        if (ret != null) return ret;
        IInventory iinventory = getHopperInventory(hopper);

        if (iinventory != null) {
            EnumFacing enumfacing = EnumFacing.DOWN;

            if (isInventoryEmpty(iinventory, enumfacing)) {
                return false;
            }
            if (iinventory instanceof ISidedInventory) {
                ISidedInventory isidedinventory = (ISidedInventory)iinventory;
                int[] aint = isidedinventory.getSlotsForFace(enumfacing);
                for (int i : aint) {
                    if (pullItemFromSlot(hopper, iinventory, i, enumfacing))
                        return true;
                }
            }
            else {
                int j = iinventory.getSizeInventory();
                for (int k = 0; k < j; ++k) {
                    if (pullItemFromSlot(hopper, iinventory, k, enumfacing))
                        return true;
                }
            }
        }
        else {
            for (EntityItem entityitem : getCaptureItems(hopper.getWorld(), hopper.getXPos(), hopper.getYPos(), hopper.getZPos())) {
                if (putDropInInventoryAllSlots((IInventory)null, hopper, entityitem))
                    return true;
            }
        }
        return false;
    }

    private static boolean pullItemFromSlot(IHopper hopper, IInventory inventoryIn, int index, EnumFacing direction) {
        ItemStack itemstack = inventoryIn.getStackInSlot(index);

        if (!itemstack.isEmpty() && canExtractItemFromSlot(inventoryIn, itemstack, index, direction)) {
            ItemStack itemstack1 = itemstack.copy();
            ItemStack itemstack2 = putStackInInventoryAllSlots(inventoryIn, hopper, inventoryIn.decrStackSize(index, 1), (EnumFacing)null);
            if (itemstack2.isEmpty()) {
                inventoryIn.markDirty();
                return true;
            }
            inventoryIn.setInventorySlotContents(index, itemstack1);
        }

        return false;
    }

    public static boolean putDropInInventoryAllSlots(IInventory iInventory, IInventory itemIn, EntityItem entityItem) {
        boolean flag = false;

        if (entityItem == null)
            return false;
        else {
            ItemStack itemstack = entityItem.getEntityItem().copy();
            ItemStack itemstack1 = putStackInInventoryAllSlots(iInventory, itemIn, itemstack, (EnumFacing)null);

            if (itemstack1.isEmpty()) {
                flag = true;
               entityItem.setDead();
            }
            else
               entityItem.setEntityItemStack(itemstack1);

            return flag;
        }
    }

    protected IItemHandler createUnSidedHandler() {
        return new WoodenHopperItemHandler(this);
    }

    public static ItemStack putStackInInventoryAllSlots(IInventory inventoryIn, IInventory stack, ItemStack side, @Nullable EnumFacing facing) {
        if (stack instanceof ISidedInventory && facing != null) {
            ISidedInventory isidedinventory = (ISidedInventory)stack;
            int[] aint = isidedinventory.getSlotsForFace(facing);
            for (int k = 0; k < aint.length && !side.isEmpty(); ++k) {
                side = insertStack(inventoryIn, stack, side, aint[k], facing);
            }
        }
        else {
            int i = stack.getSizeInventory();
            for (int j = 0; j < i && !side.isEmpty(); ++j) {
                side = insertStack(inventoryIn, stack, side, j, facing);
            }
        }
        return side;
    }

    private static boolean canInsertItemInSlot(IInventory inventoryIn, ItemStack stack, int index, EnumFacing side) {
        return !inventoryIn.isItemValidForSlot(index, stack) ? false : !(inventoryIn instanceof ISidedInventory) || ((ISidedInventory)inventoryIn).canInsertItem(index, stack, side);
    }

    private static boolean canExtractItemFromSlot(IInventory inventoryIn, ItemStack stack, int index, EnumFacing side) {
        return !(inventoryIn instanceof ISidedInventory) || ((ISidedInventory)inventoryIn).canExtractItem(index, stack, side);
    }

    private static ItemStack insertStack(IInventory inventoryIn, IInventory stack, ItemStack index, int side, EnumFacing p_174916_4_) {
        ItemStack itemstack = stack.getStackInSlot(side);

        if (canInsertItemInSlot(stack, index, side, p_174916_4_)) {
            boolean flag = false;
            boolean flag1 = stack.isEmpty();

            if (itemstack.isEmpty()) {
                stack.setInventorySlotContents(side, index);
                index = ItemStack.EMPTY;
                flag = true;
            }
            else if (canCombine(itemstack, index)) {
                int i = index.getMaxStackSize() - itemstack.getCount();
                int j = Math.min(index.getCount(), i);
                index.shrink(j);
                itemstack.grow(j);
                flag = j > 0;
            }

            if (flag) {
                if (flag1 && stack instanceof TileEntityWoodenHopper) {
                    TileEntityWoodenHopper tileentityhopper1 = (TileEntityWoodenHopper)stack;
                    if (!tileentityhopper1.mayTransfer()) {
                        int k = 0;
                        if (inventoryIn != null && inventoryIn instanceof TileEntityWoodenHopper) {
                            TileEntityWoodenHopper tileentityhopper = (TileEntityWoodenHopper)inventoryIn;
                            if (tileentityhopper1.tickedGameTime >= tileentityhopper.tickedGameTime)
                                k = 1;
                        }
                        tileentityhopper1.setTransferCooldown(8 - k);
                    }
                }
                stack.markDirty();
            }
        }
        return index;
    }

    private IInventory getInventoryForHopperTransfer() {
        EnumFacing enumfacing = BlockWoodenHopper.getFacing(getBlockMetadata());
        return getInventoryAtPosition(getWorld(), getXPos() + (double)enumfacing.getFrontOffsetX(), getYPos() + (double)enumfacing.getFrontOffsetY(), getZPos() + (double)enumfacing.getFrontOffsetZ());
    }

    public static IInventory getHopperInventory(IHopper hopper) {
        return getInventoryAtPosition(hopper.getWorld(), hopper.getXPos(), hopper.getYPos() + 1.0D, hopper.getZPos());
    }

    public static List<EntityItem> getCaptureItems(World worldIn, double p_184292_1_, double p_184292_3_, double p_184292_5_) {
        return worldIn.<EntityItem>getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(p_184292_1_ - 0.5D, p_184292_3_, p_184292_5_ - 0.5D, p_184292_1_ + 0.5D, p_184292_3_ + 1.5D, p_184292_5_ + 0.5D), EntitySelectors.IS_ALIVE);
    }

    public static IInventory getInventoryAtPosition(World worldIn, double x, double y, double z) {
        IInventory iinventory = null;
        int i = MathHelper.floor(x);
        int j = MathHelper.floor(y);
        int k = MathHelper.floor(z);
        BlockPos blockpos = new BlockPos(i, j, k);
        net.minecraft.block.state.IBlockState state = worldIn.getBlockState(blockpos);
        Block block = state.getBlock();
        if (block.hasTileEntity(state)) {
            TileEntity tileentity = worldIn.getTileEntity(blockpos);
            if (tileentity instanceof IInventory) {
                iinventory = (IInventory)tileentity;
                if (iinventory instanceof TileEntityChest && block instanceof BlockChest)
                    iinventory = ((BlockChest)block).getContainer(worldIn, blockpos, true);
            }
        }
        if (iinventory == null) {
            List<Entity> list = worldIn.getEntitiesInAABBexcluding((Entity)null, new AxisAlignedBB(x - 0.5D, y - 0.5D, z - 0.5D, x + 0.5D, y + 0.5D, z + 0.5D), EntitySelectors.HAS_INVENTORY);
            if (!list.isEmpty())
                iinventory = (IInventory)list.get(worldIn.rand.nextInt(list.size()));
        }
        return iinventory;
    }

    private static boolean canCombine(ItemStack stack1, ItemStack stack2) {
        return stack1.getItem() != stack2.getItem() ? false : (stack1.getMetadata() != stack2.getMetadata() ? false : (stack1.getCount() > stack1.getMaxStackSize() ? false : ItemStack.areItemStackTagsEqual(stack1, stack2)));
    }

    public double getXPos() {
        return (double)pos.getX() + 0.5D;
    }

    public double getYPos() {
        return (double)pos.getY() + 0.5D;
    }

    public double getZPos() {
        return (double)pos.getZ() + 0.5D;
    }

    public void setTransferCooldown(int ticks) {
        transferCooldown = ticks;
    }

    private boolean isOnTransferCooldown() {
        return transferCooldown > 0;
    }

    public boolean mayTransfer() {
        return transferCooldown > 8;
    }

    public long getLastUpdateTime() {
    	return tickedGameTime;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
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
		return getItems().get(index);
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}
}
