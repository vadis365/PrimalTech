package primal_tech.inventory;

import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import primal_tech.tiles.TileEntityWorkStump;

public class InventoryWorkStump extends InventoryCrafting {
	private NonNullList<ItemStack> stackList;
	private Container container;
	private static final int INV_WIDTH = 3;
	private final TileEntityWorkStump tile;

	public InventoryWorkStump(Container container, TileEntityWorkStump tile) {
		super(container, 3, 3);
		this.stackList = tile.getItems();
		this.tile = tile;
	}

	@Override
    public int getSizeInventory() {
        return 9;
    }

	@Override
    public ItemStack getStackInSlot(int index) {
        return index >= this.getSizeInventory() ? ItemStack.EMPTY : (ItemStack)this.stackList.get(index);
    }
	
    public ItemStack getStackInRowAndColumn(int row, int column) {
        return row >= 0 && row < INV_WIDTH && column >= 0 && column <= INV_WIDTH ? this.getStackInSlot(row + column * INV_WIDTH) : ItemStack.EMPTY;
    }

	@Override
	public String getName() {
		return "container.crafting";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void markDirty() {
		this.tile.markDirty();
		IBlockState state = this.tile.getWorld().getBlockState(this.tile.getPos());
		this.tile.getWorld().notifyBlockUpdate(this.tile.getPos(), state, state, 3);
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return true;
	}
}

