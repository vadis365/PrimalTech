package primal_tech.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import primal_tech.ModBlocks;
import primal_tech.PrimalTech;
import primal_tech.recipes.ClayKilnRecipes;
import primal_tech.tiles.TileEntityKiln;

public class BlockClayKiln extends Block implements ITileEntityProvider {
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyBool FIRED = PropertyBool.create("fired");

	public BlockClayKiln() {
		super(Material.ROCK);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(FIRED, false));
		setHardness(1.5F);
		setResistance(10.0F);
		setSoundType(SoundType.STONE);
		setCreativeTab(PrimalTech.TAB);
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Nullable
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		if(getMetaFromState(state) < 8)
			return Item.getItemFromBlock(this);
		else if(getMetaFromState(state) >= 8) {
			return Items.BRICK;
		}
		return null;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityKiln();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing facing = EnumFacing.getFront(meta);
		if (facing.getAxis() == EnumFacing.Axis.Y)
			facing = EnumFacing.NORTH;
		return getDefaultState().withProperty(FACING, facing).withProperty(FIRED, Boolean.valueOf((meta & 8) > 0));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = 0;
		meta = meta | ((EnumFacing) state.getValue(FACING)).getIndex();

		if (((Boolean) state.getValue(FIRED)).booleanValue())
			meta |= 8;

		return meta;
	}

	@Override
	 public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(FIRED, false);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {FACING, FIRED});
	}

	public void setState(World world, BlockPos pos) {
		if (!world.isRemote) {
			TileEntityKiln tile = (TileEntityKiln) world.getTileEntity(pos);
			IBlockState state = world.getBlockState(pos);
			world.setBlockState(pos, ModBlocks.CLAY_KILN.getDefaultState().withProperty(FACING, state.getValue(FACING)).withProperty(FIRED, true), 3);
			if (tile != null) {
				tile.validate();
				world.setTileEntity(pos, tile);
				tile.setActive(true);
			}
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote)
			return true;
		ItemStack stack = player.getHeldItemMainhand();
		if (world.getTileEntity(pos) instanceof TileEntityKiln) {
			TileEntityKiln tile = (TileEntityKiln) world.getTileEntity(pos);
			int activeSlot = !tile.getStackInSlot(1).isEmpty() ? 1 : 0;
			if (!stack.isEmpty() && tile.getStackInSlot(0).isEmpty() && tile.getStackInSlot(1).isEmpty()) {
				if (!world.isRemote) {
					tile.setInventorySlotContents(0, new ItemStack(stack.getItem(), 1, stack.getItemDamage()));
					tile.setCookingDuration(0);
					if (!player.capabilities.isCreativeMode)
						stack.shrink(1);
					if (!world.isRemote)
						tile.markForUpdate();
					return true;
				}
			} else {
				if (!world.isRemote) {
					ItemStack stack2 = tile.getStackInSlot(activeSlot);
					if (!stack2.isEmpty()) {
						if (stack2 == ClayKilnRecipes.getOutput(stack2) && activeSlot == 1) {
							EntityXPOrb orb = new EntityXPOrb(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 1);
							world.spawnEntity(orb);
						}
						if (!player.inventory.addItemStackToInventory(stack2))
							ForgeHooks.onPlayerTossEvent(player, stack2, false);
						tile.setInventorySlotContents(activeSlot, ItemStack.EMPTY);
						tile.markForUpdate();
					}
				}
			}
		}
		return true;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntityKiln tile = (TileEntityKiln) world.getTileEntity(pos);
		if (tile != null) {
			InventoryHelper.dropInventoryItems(world, pos, tile);
		}
		super.breakBlock(world, pos, state);
	}

}
