package primal_tech.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import primal_tech.PrimalTech;
import primal_tech.tiles.TileEntityWoodenBasin;

public class BlockWoodenBasin extends Block implements ITileEntityProvider {

	public BlockWoodenBasin() {
		super(Material.WOOD);
		setHardness(1.5F);
		setResistance(10.0F);
		setSoundType(SoundType.WOOD);
		setCreativeTab(PrimalTech.TAB);
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return true;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityWoodenBasin();
	}

	@Nullable
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(this);
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntityWoodenBasin tile = (TileEntityWoodenBasin) world.getTileEntity(pos);
		if (tile != null) {
			InventoryHelper.dropInventoryItems(world, pos, tile);
		}
		super.breakBlock(world, pos, state);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack heldItem = player.getHeldItem(hand);
		final IFluidHandler fluidHandler = getFluidHandler(world, pos);

		if (fluidHandler != null && FluidUtil.getFluidHandler(heldItem) != null) {
			FluidUtil.interactWithFluidHandler(player, hand, world, pos, side);
			return FluidUtil.getFluidHandler(heldItem) != null;
		}

		if (!world.isRemote && world.getTileEntity(pos) instanceof TileEntityWoodenBasin) {
			TileEntityWoodenBasin tile = (TileEntityWoodenBasin) world.getTileEntity(pos);
			if (!player.isSneaking()) {
				if (tile != null && heldItem.isEmpty() && tile.tank.getFluidAmount() >= Fluid.BUCKET_VOLUME) {
					if(!tile.getMixing()) {
						tile.setMixing(true);
						tile.setStirCount(tile.getStirCount() + 1);
						world.playSound((EntityPlayer)null, pos, SoundEvents.ENTITY_PLAYER_SWIM, SoundCategory.BLOCKS, 0.5F, 0.25F);
						world.notifyBlockUpdate(pos, state, state, 3);
					return true;
					}
				}
				if (!heldItem.isEmpty()) {
					for (int i = 0; i <= 3; i++) {
						if (tile.getStackInSlot(i).isEmpty()) {
							ItemStack ingredient = heldItem.copy();
							ingredient.setCount(1);
							tile.setInventorySlotContents(i, ingredient);
							if (!player.capabilities.isCreativeMode)
								heldItem.shrink(1);
							tile.setStirCount(0);
							tile.setMixing(false);
							world.playSound((EntityPlayer)null, pos, SoundEvents.ENTITY_PLAYER_SPLASH, SoundCategory.BLOCKS, 0.75F, 2F);
							world.notifyBlockUpdate(pos, state, state, 3);
							return true;
						}
					}
				}
			}

			if (player.isSneaking()) {
				for (int i = 3; i >= 0; i--) {
					if (!tile.getStackInSlot(i).isEmpty()) {
						if (!player.inventory.addItemStackToInventory(tile.getStackInSlot(i)))
							ForgeHooks.onPlayerTossEvent(player, tile.getStackInSlot(i), false);
						tile.setInventorySlotContents(i, ItemStack.EMPTY);
						tile.setStirCount(0);
						tile.setMixing(false);
						world.playSound((EntityPlayer)null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.5F, 2F);
						world.notifyBlockUpdate(pos, state, state, 3);
						return true;
					}
				}
			}
		}
		return true;
	}

	@Nullable
	private IFluidHandler getFluidHandler(IBlockAccess world, BlockPos pos) {
		TileEntityWoodenBasin tileentity = (TileEntityWoodenBasin) world.getTileEntity(pos);
		return tileentity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
	}
}
