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
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import primal_tech.ModItems;
import primal_tech.ModSounds;
import primal_tech.PrimalTech;
import primal_tech.configs.ConfigHandler;
import primal_tech.tiles.TileEntityWorkStump;

public class BlockWorkStump extends Block implements ITileEntityProvider {
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final AxisAlignedBB JIG_AABB = new AxisAlignedBB(0D, 0D, 0D, 1D, 0.9375D, 1D);

	public BlockWorkStump() {
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
		return JIG_AABB;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return JIG_AABB;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
		return JIG_AABB;
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean whatIsThis) {
		addCollisionBoxToList(pos, entityBox, collidingBoxes, JIG_AABB);
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
		return null;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityWorkStump();
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
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		if (!world.isRemote && !player.capabilities.isCreativeMode) {
			TileEntityWorkStump tile = (TileEntityWorkStump) world.getTileEntity(pos);
			if (tile instanceof TileEntityWorkStump) {
				InventoryHelper.dropInventoryItems(world, pos, tile);
				for (int i = 0; i < tile.getSizeInventory(); ++i) {
					ItemStack itemstack = tile.getStackInSlot(i);
					if (!itemstack.isEmpty())
						tile.setInventorySlotContents(i, ItemStack.EMPTY);
				}
				NBTTagCompound nbt = new NBTTagCompound();
				tile.writeToNBT(nbt);
				ItemStack stack = new ItemStack(Item.getItemFromBlock(this), 1, 0);
				if (tile.getDamage() > 0)
					stack.setTagCompound(nbt);
				InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
				world.removeTileEntity(pos);
			}
		}
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(world, pos, state, placer, stack);
		if (!world.isRemote && stack.hasTagCompound()) {
			TileEntityWorkStump tile = (TileEntityWorkStump) world.getTileEntity(pos);
			if (tile instanceof TileEntityWorkStump) {
				if (stack.getTagCompound().hasKey("damage")) {
					tile.setDamage(stack.getTagCompound().getInteger("damage"));
					tile.markForUpdate();
				}
			}
		}
		((TileEntityWorkStump) world.getTileEntity(pos)).rotation = (byte) (((MathHelper.floor((double) (placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) + 1) % 4);
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntityWorkStump tile = (TileEntityWorkStump) world.getTileEntity(pos);
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
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote)
			return true;
		EnumFacing direction = (EnumFacing) state.getValue(FACING);
		ItemStack stack = player.getHeldItemMainhand();

		if (world.getTileEntity(pos) instanceof TileEntityWorkStump) {
			TileEntityWorkStump tile = (TileEntityWorkStump) world.getTileEntity(pos);
			if (side.getIndex() == 1) {
				getSlotClicked(direction, hitX, hitZ);
				if (stack.getItem() != ModItems.ROCK && getSlotClicked(direction, hitX, hitZ) != 10) {
					if (!stack.isEmpty() && tile.getStackInSlot(getSlotClicked(direction, hitX, hitZ)).isEmpty()) {
						if (!world.isRemote) {
							tile.setInventorySlotContents(getSlotClicked(direction, hitX, hitZ), stack.splitStack(1));
							tile.setStrikes(0);
							tile.markForUpdate();
							return true;
						}
					} else {
						ItemStack stack2 = tile.getStackInSlot(getSlotClicked(direction, hitX, hitZ));
						if (!stack2.isEmpty()) {
							if (!player.inventory.addItemStackToInventory(stack2))
								ForgeHooks.onPlayerTossEvent(player, stack2, false);
							tile.setInventorySlotContents(getSlotClicked(direction, hitX, hitZ), ItemStack.EMPTY);
							tile.setStrikes(0);
							tile.markForUpdate();
						}
					}
				} else if (!stack.isEmpty() && stack.getItem() == ModItems.ROCK) {
					tile.setStrikes(tile.getStrikes() + 1);
					stack.damageItem(1, player);
					tile.setHit(true);
					tile.markForUpdate();
					if (tile.getDamage() >= ConfigHandler.WORK_STUMP_DAMAGE) {
						breakBlock(world, pos, state);
						world.destroyBlock(pos, false);
						world.playSound((EntityPlayer)null, pos, ModSounds.BREAKING_STUFF, SoundCategory.BLOCKS, 1F, 1F);
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

	public int getSlotClicked(EnumFacing direction, float hitX, float hitZ) {
		int slot = 10;
		if(hitX >= 0.4F && hitX <= 0.6F && hitZ >= 0.4F && hitZ <= 0.6F) {
			slot = 4;
		}
		if(hitX >= 0.125F && hitX <= 0.33F && hitZ >= 0.125F && hitZ <= 0.33F) {
			if(direction == EnumFacing.NORTH)
				slot = 8;
			if(direction == EnumFacing.EAST)
				slot = 2;
			if(direction == EnumFacing.WEST)
				slot = 6;
			if(direction == EnumFacing.SOUTH)
				slot = 0;
		}

		if(hitX >= 0.4F && hitX <= 0.6F && hitZ >= 0.125F && hitZ <= 0.33F) {
			if(direction == EnumFacing.NORTH)
				slot = 7;
			if(direction == EnumFacing.EAST)
				slot = 5;
			if(direction == EnumFacing.WEST)
				slot = 3;
			if(direction == EnumFacing.SOUTH)
				slot = 1;
		}

		if(hitX >= 0.67F && hitX <= 0.875F && hitZ >= 0.125F && hitZ <= 0.33F) {
			if(direction == EnumFacing.NORTH)
				slot = 6;
			if(direction == EnumFacing.EAST)
				slot = 8;
			if(direction == EnumFacing.WEST)
				slot = 0;
			if(direction == EnumFacing.SOUTH)
				slot = 2;
		}
		
		if(hitX >= 0.125F && hitX <= 0.33F && hitZ >= 0.4F && hitZ <= 0.6F) {
			if(direction == EnumFacing.NORTH)
				slot = 5;
			if(direction == EnumFacing.EAST)
				slot = 1;
			if(direction == EnumFacing.WEST)
				slot = 7;
			if(direction == EnumFacing.SOUTH)
				slot = 3;
		}
		
		if(hitX >= 0.67F && hitX <= 0.875F && hitZ >= 0.4F && hitZ <= 0.6F) {
			if(direction == EnumFacing.NORTH)
				slot = 3;
			if(direction == EnumFacing.EAST)
				slot = 7;
			if(direction == EnumFacing.WEST)
				slot = 1;
			if(direction == EnumFacing.SOUTH)
				slot = 5;
		}

		if(hitX >= 0.125F && hitX <= 0.33F && hitZ >= 0.67F && hitZ <= 0.875F) {
			if(direction == EnumFacing.NORTH)
				slot = 2;
			if(direction == EnumFacing.EAST)
				slot = 0;
			if(direction == EnumFacing.WEST)
				slot = 8;
			if(direction == EnumFacing.SOUTH)
				slot = 6;
		}
		
		if(hitX >= 0.4F && hitX <= 0.6F && hitZ >= 0.67F && hitZ <= 0.875F) {
			if(direction == EnumFacing.NORTH)
				slot = 1;
			if(direction == EnumFacing.EAST)
				slot = 3;
			if(direction == EnumFacing.WEST)
				slot = 5;
			if(direction == EnumFacing.SOUTH)
				slot = 7;
		}

		if(hitX >= 0.67F && hitX <= 0.875F && hitZ >= 0.67F && hitZ <= 0.875F) {
			if(direction == EnumFacing.NORTH)
				slot = 0;
			if(direction == EnumFacing.EAST)
				slot = 6;
			if(direction == EnumFacing.WEST)
				slot = 2;
			if(direction == EnumFacing.SOUTH)
				slot = 8;
		}
		return slot;
	}

}
