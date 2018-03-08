package primal_tech.events;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import primal_tech.ModItems;
import primal_tech.configs.ConfigHandler;
import primal_tech.items.ItemClub;

public class EntityBoneShardEvent {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void dropEvent(LivingDropsEvent event) {
		if (event.getEntityLiving().getEntityWorld().isRemote || !ConfigHandler.DROP_BONE_SHARD)
			return;
		if (event.getEntityLiving().getHealth() > 0.0F)
			return;
		if (event.getSource().getTrueSource() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
				if (player.getHeldItemMainhand().getItem() instanceof ItemClub) {
					int dropChance = event.getEntityLiving().getEntityWorld().rand.nextInt(ConfigHandler.BONE_SHARD_RATE);
					if (dropChance == 0) {
						ItemStack stack = new ItemStack(ModItems.BONE_SHARD);
						if (stack != null)
							addDrop(stack, event.getEntityLiving(), event.getDrops());
					}
				}
			}
		}

	private void addDrop(ItemStack stack, EntityLivingBase entity, List<EntityItem> list) {
		if (stack.getCount() <= 0)
			return;
		EntityItem entityItem = new EntityItem(entity.getEntityWorld(), entity.posX, entity.posY, entity.posZ, stack);
		entityItem.setDefaultPickupDelay();
		list.add(entityItem);
	}
}