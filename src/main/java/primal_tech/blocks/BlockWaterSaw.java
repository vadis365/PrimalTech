package primal_tech.blocks;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import primal_tech.PrimalTech;
import primal_tech.tiles.TileEntityWaterSaw;

public class BlockWaterSaw extends Block implements ITileEntityProvider {
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final AxisAlignedBB SAW_AABB = new AxisAlignedBB(0D, 0D, 0D, 1D, 0.5D, 1D);

	public BlockWaterSaw() {
		super(Material.WOOD);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		setHardness(1.5F);
		setResistance(10.0F);
		setSoundType(SoundType.WOOD);
		setCreativeTab(PrimalTech.TAB);
	}

	// One of these fucking things must be right!!!?

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return SAW_AABB;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return SAW_AABB;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
		return SAW_AABB;
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean whatIsThis) {
		addCollisionBoxToList(pos, entityBox, collidingBoxes, SAW_AABB);
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

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

    @SideOnly(Side.CLIENT)
    public boolean addDestroyEffects(World world, BlockPos pos, net.minecraft.client.particle.ParticleManager manager) {
        return true;
    }

	@Nullable
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(this);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityWaterSaw();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing facing = EnumFacing.getFront(meta);
		if (facing.getAxis() == EnumFacing.Axis.Y)
			facing = EnumFacing.NORTH;
		return getDefaultState().withProperty(FACING, facing);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = 0;
		meta = meta | ((EnumFacing) state.getValue(FACING)).getIndex();

		return meta;
	}

	@Override
	 public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote)
			return true;
		ItemStack stack = player.getHeldItemMainhand();
		if (world.getTileEntity(pos) instanceof TileEntityWaterSaw) {
			TileEntityWaterSaw tile = (TileEntityWaterSaw) world.getTileEntity(pos);
			if (!stack.isEmpty() && tile.getStackInSlot(0).isEmpty()) {
				if (!world.isRemote) {
					tile.setInventorySlotContents(0, new ItemStack(stack.getItem(), 1, stack.getItemDamage()));
					tile.setChoppingProgress(0);
					if (!player.capabilities.isCreativeMode)
						stack.shrink(1);
						tile.markForUpdate();
						world.playSound((EntityPlayer)null, pos, SoundEvents.BLOCK_FENCE_GATE_OPEN, SoundCategory.BLOCKS, 0.5F, 2F);
					return true;
				}
			} else {
				if (!world.isRemote) {
					ItemStack stack2 = tile.getStackInSlot(0);
					if (!stack2.isEmpty()) {
						if (!player.inventory.addItemStackToInventory(stack2))
							ForgeHooks.onPlayerTossEvent(player, stack2, false);
						tile.setInventorySlotContents(0, ItemStack.EMPTY);
						tile.setChoppingProgress(0);
						world.playSound((EntityPlayer)null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.5F, 2F);
						tile.markForUpdate();
					}
				}
			}
		}
		return true;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntityWaterSaw tile = (TileEntityWaterSaw) world.getTileEntity(pos);
		if (tile != null) {
			InventoryHelper.dropInventoryItems(world, pos, tile);
		}
		world.playEvent(2001, pos, Block.getStateId(Blocks.LOG.getDefaultState()));
		super.breakBlock(world, pos, state);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {FACING});
	}

}
