package primal_tech.tiles;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import primal_tech.configs.ConfigHandler;
import primal_tech.recipes.StoneAnvilRecipes;

public class TileEntityStoneAnvil extends TileEntityInventoryHelper implements ITickable {
    private IItemHandler itemHandler;
	private static final int[] SLOTS = new int[] {0};
	public boolean active;
	public float itemRotation = 0;
	public int itemJump = 0;
	public int itemJumpPrev = 0;
	public boolean hit = false;
	public int damage = 0;
	public int strikes = 0;
	public TileEntityStoneAnvil() {
		super(1);
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public void update() {
		if (getWorld().isRemote) {
			itemJumpPrev = itemJump;
		}
		if (gethit()) {
			if (getWorld().isRemote) {
					itemJump = 1 + getWorld().rand.nextInt(5);
					itemRotation = (getWorld().rand.nextFloat() - getWorld().rand.nextFloat()) * 25F;
			}
			if (!getWorld().isRemote)
				getWorld().playSound((EntityPlayer) null, pos, SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 1F, 0.9F - getWorld().rand.nextFloat() * 0.125F);
			setHit(false);
		}

		if (!gethit()) {
			if (getWorld().isRemote) {
				if (itemJump > 0)
					itemJump --;
			}
		}

		if (!getWorld().isRemote) {
			if (getStrikes() >= ConfigHandler.STONE_ANVIL_CRAFTING_STRIKES) {
				ItemStack input = getItems().get(0);
				ItemStack output = StoneAnvilRecipes.getOutput(getItems().get(0));
				if (output.isEmpty() || output.equals(input)) {
					setStrikes(0);
					return;
				} else {
					decrStackSize(0, 1);
					spawnItemStack(getWorld(), pos.getX() + 0.5D, pos.getY() + 1D, pos.getZ() + 0.5D, output);
					setDamage(getDamage() + 1);
					setStrikes(0);
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
		entityitem.setPickupDelay(40);
		world.spawnEntity(entityitem);
	}

    public boolean gethit() {
		return hit;
	}

    public void setHit(boolean isHit) {
		hit = isHit;
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
	return oldState.getBlock() != newState.getBlock();
	}

	public void markForUpdate() {
        IBlockState state = this.getWorld().getBlockState(this.getPos());
        this.getWorld().notifyBlockUpdate(this.getPos(), state, state, 3);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("damage", damage);
		nbt.setInteger("strikes", strikes);
		nbt.setBoolean("hit", hit);
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		damage = nbt.getInteger("damage");
		strikes = nbt.getInteger("strikes");
		hit = nbt.getBoolean("hit");
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
		markForUpdate();
		return super.decrStackSize(index, count);
	}

	@Override
    public void setInventorySlotContents(int index, @Nullable ItemStack stack) {
		markForUpdate();
		super.setInventorySlotContents(index, stack);
    }
	
	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return SLOTS;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return false;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return getItems().get(index);
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	public void setStrikes(int strikes) {
		this.strikes = strikes;
		markForUpdate();
	}

	public int getStrikes() {
		return strikes;
	}
	
	public void setDamage(int damage) {
		this.damage = damage;
		markForUpdate();
	}

	public int getDamage() {
		return damage;
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
