package primal_tech.tiles;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import primal_tech.configs.ConfigHandler;
import primal_tech.inventory.ContainerWorkStump;
import primal_tech.inventory.InventoryWorkStump;

public class TileEntityWorkStump extends TileEntityInventoryHelper implements ITickable {
	public byte rotation = 0;
	private static final int[] SLOTS = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
	public float[] itemRotation = {0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F};
	public int itemJump[] = {0, 0, 0, 0, 0, 0, 0, 0, 0};
	public int itemJumpPrev[] = {0, 0, 0, 0, 0, 0, 0, 0, 0};
	public boolean hit = false;
	public int damage = 0;
	public int strikes = 0;
	public InventoryCrafting craftMatrix;
	private IItemHandler itemHandler;

	public TileEntityWorkStump() {
		super(10);
	}

	@Override
	public void update() {
		if (getWorld().isRemote) {
			itemJumpPrev = itemJump;
		}
		if (gethit()) {
			if (getWorld().isRemote) {
				for(int count = 0; count < 9; count++) {
					itemJump[count] = 1 + getWorld().rand.nextInt(5);
					itemRotation[count] = (getWorld().rand.nextFloat() - getWorld().rand.nextFloat()) * 25F;
				}
			}
			if (!getWorld().isRemote)
				getWorld().playSound((EntityPlayer) null, pos, SoundEvents.BLOCK_WOOD_HIT, SoundCategory.BLOCKS, 1F, 0.75F);
			setHit(false);
		}

		if (!gethit()) {
			if (getWorld().isRemote) {
				if (itemJump[0] > 0)
					itemJump[0]--;
				if (itemJump[1] > 0)
					itemJump[1]--;
				if (itemJump[2] > 0)
					itemJump[2]--;
				if (itemJump[3] > 0)
					itemJump[3]--;
				if (itemJump[4] > 0)
					itemJump[4]--;
				if (itemJump[5] > 0)
					itemJump[5]--;
				if (itemJump[6] > 0)
					itemJump[6]--;
				if (itemJump[7] > 0)
					itemJump[7]--;
				if (itemJump[8] > 0)
					itemJump[8]--;
			}
		}

		if (!getWorld().isRemote) {
			if (this instanceof TileEntityWorkStumpUpgraded ? getStrikes() >= ConfigHandler.CRAFTING_STRIKES_II : getStrikes() >= ConfigHandler.CRAFTING_STRIKES) {
				craftMatrix = new InventoryWorkStump(new ContainerWorkStump(), this);
				ItemStack output = CraftingManager.findMatchingResult(craftMatrix, getWorld());
				if (output.isEmpty()) {
					setStrikes(0);
					return;
				}
				NonNullList<ItemStack> stuffLeft = CraftingManager.getRemainingItems(craftMatrix, getWorld());
				for (int index = 0; index < 9; index++) {
					if(!stuffLeft.get(index).isEmpty()) 
						if (stuffLeft.get(index).getItem() != getItems().get(index).getItem()) {
							spawnItemStack(getWorld(), pos.getX() + 0.5D, pos.getY() + 1D, pos.getZ() + 0.5D, stuffLeft.get(index));
							decrStackSize(index, 1);
						}
						else
							setInventorySlotContents(index, stuffLeft.get(index));
					else
						decrStackSize(index, 1);
				}
				spawnItemStack(getWorld(), pos.getX() + 0.5D, pos.getY() + 1D, pos.getZ() + 0.5D, output);
				setDamage(getDamage() + 1);
				setStrikes(0);
				EntityXPOrb orb = new EntityXPOrb(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 1);
				world.spawnEntity(orb);
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
        this.getWorld().notifyBlockUpdate(this.getPos(), state, state, 2);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setByte("rotation", rotation);
		nbt.setInteger("damage", damage);
		nbt.setInteger("strikes", strikes);
		nbt.setBoolean("hit", hit);
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		rotation = nbt.getByte("rotation");
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
}
