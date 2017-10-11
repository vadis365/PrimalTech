package primal_tech.events;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import primal_tech.ModBlocks;
import primal_tech.blocks.BlockFibreTorchLit;
import primal_tech.configs.ConfigHandler;

public class TorchLightEvent {

	@SubscribeEvent
	public void onUseItem(RightClickBlock event) {
		EntityPlayer player = (EntityPlayer) event.getEntityPlayer();
		ItemStack handItem = player.getHeldItem(event.getHand());
			if (!handItem.isEmpty()) {
				if (handItem.getItem() == ModBlocks.FIBRE_TORCH_ITEM) {
					if (isFireSource(event.getWorld().getBlockState(event.getPos().up()).getBlock()) && event.getFace().getIndex() == 1 || isFireSource(event.getWorld().getBlockState(event.getPos()).getBlock())) {
						lightTorch(handItem, player, new ItemStack(ModBlocks.FIBRE_TORCH_ITEM_LIT));
						event.getWorld().playSound((EntityPlayer)null, event.getPos(), SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 0.2F, 0.1F + (event.getWorld().rand.nextFloat() - event.getWorld().rand.nextFloat()) * 0.8F);
						handItem.shrink(1);
						event.setUseItem(Result.DENY);
						event.setCanceled(true);
					}
				}
			}
	}

	protected ItemStack lightTorch(ItemStack stackIn, EntityPlayer player, ItemStack stack) {
		if (stackIn.getCount() <= 0)
			return stack;
		else {
			if (!player.inventory.addItemStackToInventory(stack))
				player.dropItem(stack, false);
			return stackIn;
		}
	}

	private static Boolean isFireSource(Block block) {
		List<Block> blockList = new ArrayList<Block>();
		for (int blocks = 0; blocks < ConfigHandler.FIRE_SOURCES.length; blocks++) {
			String entry = ConfigHandler.FIRE_SOURCES[blocks].trim();
			Block outBlock = Block.REGISTRY.getObject(new ResourceLocation(entry));
			blockList.add(outBlock);
		}
		if(blockList.contains(block) || block instanceof BlockFibreTorchLit)
			return true;
		return false;
	}
}
