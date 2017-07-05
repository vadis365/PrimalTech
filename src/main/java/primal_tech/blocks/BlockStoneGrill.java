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
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import primal_tech.PrimalTech;
import primal_tech.tiles.TileEntityStoneGrill;

public class BlockStoneGrill extends Block implements ITileEntityProvider {
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final AxisAlignedBB GRILL_AABB = new AxisAlignedBB(0D, 0D, 0D, 1D, 0.1875D, 1D);

	public BlockStoneGrill() {
		super(Material.ROCK);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		setHardness(1.5F);
		setResistance(10.0F);
		setSoundType(SoundType.STONE);
		setCreativeTab(PrimalTech.TAB);
	}

	// One of these fucking things must be right!!!?

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return GRILL_AABB;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return GRILL_AABB;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
		return GRILL_AABB;
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean whatIsThis) {
		addCollisionBoxToList(pos, entityBox, collidingBoxes, GRILL_AABB);
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
		return Item.getItemFromBlock(this);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityStoneGrill();
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
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		((TileEntityStoneGrill) world.getTileEntity(pos)).rotation = (byte) (((MathHelper.floor((double) (placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) + 1) % 4);
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntityStoneGrill tile = (TileEntityStoneGrill) world.getTileEntity(pos);
		if (tile != null) {
			InventoryHelper.dropInventoryItems(world, pos, tile);
		}
		super.breakBlock(world, pos, state);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {FACING});
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote)
			return true;
		EnumFacing direction = (EnumFacing) state.getValue(FACING);
		ItemStack stack = player.getHeldItemMainhand();
		if (world.getTileEntity(pos) instanceof TileEntityStoneGrill) {
			TileEntityStoneGrill tile = (TileEntityStoneGrill) world.getTileEntity(pos);
			if (side.getIndex() == 1) {
				int slotClicked = getSlotClicked(direction, hitX, hitZ);
				if (slotClicked == 0 && !player.isSneaking()) {
					if (!stack.isEmpty() && (tile.getStackInSlot(slotClicked).isEmpty() || tile.getStackInSlot(slotClicked).getItem() == stack.getItem() && tile.getStackInSlot(slotClicked).getCount() < 16 && tile.getStackInSlot(slotClicked).isStackable())) {
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
						float xpCount = FurnaceRecipes.instance().getSmeltingExperience(stack2);
						int slotItemCount = stack2.getCount();
						if (slotClicked == 1) {
							if (!world.isRemote) {
								if (xpCount == 0.0F)
									slotItemCount = 0;
								else if (xpCount < 1.0F) {
									int roundedXP = MathHelper.floor((float) slotItemCount * xpCount);
									if (roundedXP < MathHelper.ceil((float) slotItemCount * xpCount) && Math .random() < (double) ((float) slotItemCount * xpCount - (float) roundedXP))
										++roundedXP;
									slotItemCount = roundedXP;
								}
								while (slotItemCount > 0) {
									int xpSplit = EntityXPOrb.getXPSplit(slotItemCount);
									slotItemCount -= xpSplit;
									EntityXPOrb orb = new EntityXPOrb(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, xpSplit);
									world.spawnEntity(orb);
								}
							}
							slotItemCount = 0;
						}
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
				slot = 1;
			if(direction == EnumFacing.EAST)
				slot = 1;
			if(direction == EnumFacing.WEST)
				slot = 0;
			if(direction == EnumFacing.SOUTH)
				slot = 0;
		}

		if(hitX > 0.5F && hitX < 1F && hitZ > 0F && hitZ < 0.5F) {
			if(direction == EnumFacing.NORTH)
				slot = 0;
			if(direction == EnumFacing.EAST)
				slot = 1;
			if(direction == EnumFacing.WEST)
				slot = 0;
			if(direction == EnumFacing.SOUTH)
				slot = 1;
		}

		if(hitX > 0F && hitX < 0.5F && hitZ > 0.5F && hitZ < 1F) {
			if(direction == EnumFacing.NORTH)
				slot = 1;
			if(direction == EnumFacing.EAST)
				slot = 0;
			if(direction == EnumFacing.WEST)
				slot = 1;
			if(direction == EnumFacing.SOUTH)
				slot = 0;
		}

		if(hitX > 0.5F && hitX <= 1F && hitZ > 0.5F && hitZ < 1F) {
			if(direction == EnumFacing.NORTH)
				slot = 0;
			if(direction == EnumFacing.EAST)
				slot = 0;
			if(direction == EnumFacing.WEST)
				slot = 1;
			if(direction == EnumFacing.SOUTH)
				slot = 1;
		}
		return slot;
	}

}
