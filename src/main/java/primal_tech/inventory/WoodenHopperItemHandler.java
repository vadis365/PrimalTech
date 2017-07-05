package primal_tech.inventory;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.wrapper.InvWrapper;
import primal_tech.tiles.TileEntityWoodenHopper;

public class WoodenHopperItemHandler extends InvWrapper {
	private final TileEntityWoodenHopper hopper;

	public WoodenHopperItemHandler(TileEntityWoodenHopper hopper) {
		super(hopper);
		this.hopper = hopper;
	}

	@Override
	@Nonnull
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
		if (simulate)
			return super.insertItem(slot, stack, simulate);
		else {
			boolean wasEmpty = getInv().isEmpty();
			int originalStackSize = stack.getCount();
			stack = super.insertItem(slot, stack, simulate);
			if (wasEmpty && originalStackSize > stack.getCount())
				if (!hopper.mayTransfer())
					hopper.setTransferCooldown(8);
			return stack;
		}
	}
}