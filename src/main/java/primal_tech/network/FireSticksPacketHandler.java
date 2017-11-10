package primal_tech.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class FireSticksPacketHandler implements IMessageHandler<FireSticksMessage, IMessage> {

	@Override
	public IMessage onMessage(final FireSticksMessage message, MessageContext ctx) {

		final World world = DimensionManager.getWorld(message.dimension);

		if (world == null)
			return null;

		else if (!world.isRemote)
			if (ctx.getServerHandler().player.getEntityId() == message.entityID) {
				final EntityPlayerMP player = ctx.getServerHandler().player;
				player.getServer().addScheduledTask(new Runnable() {
					public void run() {
						if (Blocks.FIRE.canPlaceBlockAt(world, message.tilePos)) {
							world.playSound((EntityPlayer) null, message.tilePos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 0.2F, 0.1F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
							world.setBlockState(message.tilePos, Blocks.FIRE.getDefaultState(), 11);
							ItemStack mainHandItem = player.getHeldItem(EnumHand.MAIN_HAND);
							ItemStack offHandItem = player.getHeldItem(EnumHand.OFF_HAND);
							if (!mainHandItem.isEmpty() && !offHandItem.isEmpty()) {
								mainHandItem.shrink(1);
								offHandItem.shrink(1);
							}
						}
					}
				});
			}
		return null;
	}
}