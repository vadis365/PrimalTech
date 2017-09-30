package primal_tech.blocks;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import primal_tech.PrimalTech;
import primal_tech.tiles.TileEntityWoodenHopper;

public class BlockWoodenHopper extends BlockDirectional implements ITileEntityProvider {

    public static final PropertyBool ENABLED = PropertyBool.create("enabled");
    protected static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.625D, 1.0D);
    protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.125D);
    protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.875D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.875D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.125D, 1.0D, 1.0D);

    public BlockWoodenHopper() {
        super(Material.WOOD);
        setDefaultState(this.getBlockState().getBaseState().withProperty(FACING, EnumFacing.DOWN).withProperty(ENABLED, Boolean.valueOf(true)));
		setHardness(1.5F);
		setResistance(10.0F);
		setSoundType(SoundType.WOOD);
		setCreativeTab(PrimalTech.TAB);
    }

	@Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return FULL_BLOCK_AABB;
    }

	@Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
        addCollisionBoxToList(pos, entityBox, collidingBoxes, BASE_AABB);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_AABB);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_AABB);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_AABB);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_AABB);
    }

	@Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        EnumFacing enumfacing = facing.getOpposite();
        if (enumfacing == EnumFacing.UP)
            enumfacing = EnumFacing.DOWN;
        return getDefaultState().withProperty(FACING, enumfacing).withProperty(ENABLED, Boolean.valueOf(true));
    }

	@Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityWoodenHopper();
    }

	@Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        updateState(worldIn, pos, state);
    }

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		EnumFacing direction = (EnumFacing) state.getValue(FACING);
		if(direction == EnumFacing.DOWN)
			((TileEntityWoodenHopper) world.getTileEntity(pos)).rotation = (byte) (1);
		else
			((TileEntityWoodenHopper) world.getTileEntity(pos)).rotation = (byte) (((MathHelper.floor((double) (placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) + 1) % 4);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote)
			return true;
		EnumFacing direction = (EnumFacing) state.getValue(FACING);
		ItemStack stack = player.getHeldItemMainhand();
		if (world.getTileEntity(pos) instanceof TileEntityWoodenHopper) {
			TileEntityWoodenHopper tile = (TileEntityWoodenHopper) world.getTileEntity(pos);
			if (side.getIndex() == 1) {
				int slotClicked = getSlotClicked(direction, hitX, hitZ);
				if (!player.isSneaking()) {
					if (!stack.isEmpty() && (tile.getStackInSlot(slotClicked).isEmpty() || tile.getStackInSlot(slotClicked).getItem() == stack.getItem() && tile.getStackInSlot(slotClicked).getItemDamage() == stack.getItemDamage() && tile.getStackInSlot(slotClicked).getCount() < 64 && tile.getStackInSlot(slotClicked).isStackable())) {
						if (!world.isRemote) {
							if (!tile.getStackInSlot(slotClicked).isEmpty()) {
								tile.getStackInSlot(slotClicked).grow(1);
								stack.shrink(1);
							} else
								tile.setInventorySlotContents(slotClicked, stack.splitStack(1));
							tile.markForUpdate();
							return true;
						}
					}
				} else {
					ItemStack stack2 = tile.getStackInSlot(slotClicked);
					if (!stack2.isEmpty()) {
						if (!player.inventory.addItemStackToInventory(stack2))
							ForgeHooks.onPlayerTossEvent(player, stack2, false);
						tile.setInventorySlotContents(slotClicked, stack2.splitStack(1));
					}
					tile.markForUpdate();
					return true;
				}
			}
		}
		return true;
	}

	public int getSlotClicked(EnumFacing direction, float hitX, float hitZ) {
		int slot = 3;
		if(hitX > 0F && hitX < 0.5F && hitZ > 0F && hitZ < 0.5F) {
			if(direction == EnumFacing.NORTH)
				slot = 0;
			if(direction == EnumFacing.EAST)
				slot = 2;
			if(direction == EnumFacing.WEST)
				slot = 1;
			if(direction == EnumFacing.SOUTH || direction == EnumFacing.DOWN)
				slot = 3;
		}

		if(hitX > 0.5F && hitX < 1F && hitZ > 0F && hitZ < 0.5F) {
			if(direction == EnumFacing.NORTH)
				slot = 1;
			if(direction == EnumFacing.EAST)
				slot = 0;
			if(direction == EnumFacing.WEST)
				slot = 3;
			if(direction == EnumFacing.SOUTH || direction == EnumFacing.DOWN)
				slot = 2;
		}

		if(hitX > 0F && hitX < 0.5F && hitZ > 0.5F && hitZ < 1F) {
			if(direction == EnumFacing.NORTH)
				slot = 2;
			if(direction == EnumFacing.EAST)
				slot = 3;
			if(direction == EnumFacing.WEST)
				slot = 0;
			if(direction == EnumFacing.SOUTH || direction == EnumFacing.DOWN)
				slot = 1;
		}

		if(hitX > 0.5F && hitX <= 1F && hitZ > 0.5F && hitZ < 1F) {
			if(direction == EnumFacing.NORTH)
				slot = 3;
			if(direction == EnumFacing.EAST)
				slot = 1;
			if(direction == EnumFacing.WEST)
				slot = 2;
			if(direction == EnumFacing.SOUTH || direction == EnumFacing.DOWN)
				slot = 0;
		}
		return slot;
	}

	@Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
		updateState(world, pos, state);
    }

    private void updateState(World world, BlockPos pos, IBlockState state) {
        boolean flag = !world.isBlockPowered(pos);
        if (flag != ((Boolean)state.getValue(ENABLED)).booleanValue()) {
            world.setBlockState(pos, state.withProperty(ENABLED, Boolean.valueOf(flag)), 4);
            world.notifyBlockUpdate(pos, state, state, 3);
        }
    }

	@Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityWoodenHopper) {
            InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityWoodenHopper)tileentity);
            worldIn.updateComparatorOutputLevel(pos, this);
        }
        super.breakBlock(worldIn, pos, state);
    }

	@Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

	@Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

	@Override
    public boolean isTopSolid(IBlockState state) {
        return true;
    }

	@Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return true;
    }

    public static EnumFacing getFacing(int meta) {
        return EnumFacing.getFront(meta & 7);
    }

    public static boolean isEnabled(int meta) {
        return (meta & 8) != 8;
    }

	@Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

	@Override
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
        return Container.calcRedstone(worldIn.getTileEntity(pos));
    }

	@Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

	@Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, getFacing(meta)).withProperty(ENABLED, Boolean.valueOf(isEnabled(meta)));
    }

	@Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i = i | ((EnumFacing)state.getValue(FACING)).getIndex();

        if (!((Boolean)state.getValue(ENABLED)).booleanValue())
        {
            i |= 8;
        }

        return i;
    }

	@Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
    }

	@Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
    }

	@Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {FACING, ENABLED});
    }
}