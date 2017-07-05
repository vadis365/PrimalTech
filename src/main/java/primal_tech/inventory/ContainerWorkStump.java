package primal_tech.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerWorkStump extends Container {

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

}
