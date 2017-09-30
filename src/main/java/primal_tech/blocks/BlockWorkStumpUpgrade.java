package primal_tech.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import primal_tech.ModItems;
import primal_tech.ModSounds;
import primal_tech.PrimalTech;
import primal_tech.configs.ConfigHandler;
import primal_tech.tiles.TileEntityWorkStumpUpgraded;

public class BlockWorkStumpUpgrade extends BlockWorkStump {

	public BlockWorkStumpUpgrade() {
		super();
		setHardness(1.5F);
		setResistance(10.0F);
		setSoundType(SoundType.WOOD);
		setCreativeTab(PrimalTech.TAB);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityWorkStumpUpgraded();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote)
			return true;
		EnumFacing direction = (EnumFacing) state.getValue(FACING);
		ItemStack stack = player.getHeldItemMainhand();

		if (world.getTileEntity(pos) instanceof TileEntityWorkStumpUpgraded) {
			TileEntityWorkStumpUpgraded tile = (TileEntityWorkStumpUpgraded) world.getTileEntity(pos);
			if (side.getIndex() == 1) {
				int slotClicked = getSlotClicked(direction, hitX, hitZ);
				if (!player.isSneaking()) {
					if (stack.getItem() != ModItems.ROCK && getSlotClicked(direction, hitX, hitZ) != 10) {
						if (!stack.isEmpty() && (tile.getStackInSlot(slotClicked).isEmpty() || tile.getStackInSlot(slotClicked).getItem() == stack.getItem()  && tile.getStackInSlot(slotClicked).getItemDamage() == stack.getItemDamage() && tile.getStackInSlot(slotClicked).getCount() < 64 && tile.getStackInSlot(slotClicked).isStackable())) {
							if (!world.isRemote) {
								if (!tile.getStackInSlot(slotClicked).isEmpty()) {
									tile.getStackInSlot(slotClicked).grow(1);
									stack.shrink(1);
									tile.setStrikes(0);
								} else {
									tile.setInventorySlotContents(slotClicked, stack.splitStack(1));
									tile.setStrikes(0);
								}
								tile.markForUpdate();
								return true;
							}
						}
					} else if (!stack.isEmpty() && stack.getItem() == ModItems.ROCK) {
						tile.setStrikes(tile.getStrikes() + 1);
						stack.damageItem(1, player);
						tile.setHit(true);
						tile.markForUpdate();
						if (tile.getDamage() >= ConfigHandler.WORK_STUMP_II_DAMAGE) {
							breakBlock(world, pos, state);
							world.destroyBlock(pos, false);
							world.playSound((EntityPlayer)null, pos, ModSounds.BREAKING_STUFF, SoundCategory.BLOCKS, 1F, 1F);
						}
					}
				} else if (getSlotClicked(direction, hitX, hitZ) != 10) {
					ItemStack stack2 = tile.getStackInSlot(slotClicked);
					if (!stack2.isEmpty()) {
						if (!player.inventory.addItemStackToInventory(stack2))
							ForgeHooks.onPlayerTossEvent(player, stack2, false);
						tile.setInventorySlotContents(slotClicked, stack2.splitStack(1));
						tile.setStrikes(0);
						tile.markForUpdate();
						return true;
					}
				}
			}
			if (side.getIndex() != 1 && side.getIndex() == direction.getIndex()) {
				if (!stack.isEmpty() && tile.getStackInSlot(9).isEmpty() && stack.getItem() == ModItems.ROCK) {
					if (!world.isRemote) {
						tile.setInventorySlotContents(9, stack.splitStack(1));
						tile.markForUpdate();
						return true;
					}
				} else {
					ItemStack stack2 = tile.getStackInSlot(9);
					if (!stack2.isEmpty()) {
						if (!player.inventory.addItemStackToInventory(stack2))
							ForgeHooks.onPlayerTossEvent(player, stack2, false);
						tile.setInventorySlotContents(9, ItemStack.EMPTY);
						tile.markForUpdate();
					}
				}
			}
		}
		return true;
	}
}
