package primal_tech.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import primal_tech.ModItems;

public class BlockLeafBed extends BlockHorizontal {
	
    public static final PropertyEnum<BlockLeafBed.EnumPartType> PART = PropertyEnum.<BlockLeafBed.EnumPartType>create("part", BlockLeafBed.EnumPartType.class);
    public static final PropertyBool OCCUPIED = PropertyBool.create("occupied");
    protected static final AxisAlignedBB BED_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.625D, 1.0D);

	public BlockLeafBed() {
		super(Material.LEAVES);
		disableStats();
	}

	@Override
	public boolean isBed(IBlockState state, IBlockAccess world, BlockPos pos, Entity player) {
		return true;
	}

	@Override
	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
		if (state.getValue(PART) == BlockLeafBed.EnumPartType.FOOT)
			super.dropBlockAsItemWithChance(worldIn, pos, state, chance, 0);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return state.getValue(PART) == BlockLeafBed.EnumPartType.HEAD ? null : ModItems.LEAF_BED_ITEM;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(ModItems.LEAF_BED_ITEM);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote) {
			return true;
		} else {
			if (state.getValue(PART) != BlockLeafBed.EnumPartType.HEAD) {
				pos = pos.offset((EnumFacing) state.getValue(FACING));
				state = worldIn.getBlockState(pos);
				if (state.getBlock() != this)
					return true;
			}

			if (worldIn.provider.canRespawnHere() && worldIn.getBiome(pos) != Biomes.HELL) {
				if (((Boolean) state.getValue(OCCUPIED)).booleanValue()) {
					EntityPlayer entityplayer = this.getPlayerInBed(worldIn, pos);

					if (entityplayer != null) {
						playerIn.sendStatusMessage(new TextComponentTranslation("tile.bed.occupied", new Object[0]), true);
						return true;
					}

					state = state.withProperty(OCCUPIED, Boolean.valueOf(false));
					worldIn.setBlockState(pos, state, 4);
				}

				EntityPlayer.SleepResult entityplayer$sleepresult = playerIn.trySleep(pos);

				if (entityplayer$sleepresult == EntityPlayer.SleepResult.OK) {
					state = state.withProperty(OCCUPIED, Boolean.valueOf(true));
					worldIn.setBlockState(pos, state, 4);
					return true;
				} else {
					if (entityplayer$sleepresult == EntityPlayer.SleepResult.NOT_POSSIBLE_NOW)
						playerIn.sendStatusMessage(new TextComponentTranslation("tile.bed.noSleep", new Object[0]), true);
					else if (entityplayer$sleepresult == EntityPlayer.SleepResult.NOT_SAFE)
						playerIn.sendStatusMessage(new TextComponentTranslation("tile.bed.notSafe", new Object[0]), true);
					else if (entityplayer$sleepresult == EntityPlayer.SleepResult.TOO_FAR_AWAY)
						playerIn.sendStatusMessage(new TextComponentTranslation("tile.bed.tooFarAway", new Object[0]), true);

					return true;
				}
			} else {
				worldIn.setBlockToAir(pos);
				BlockPos blockpos = pos.offset(((EnumFacing) state.getValue(FACING)).getOpposite());

				if (worldIn.getBlockState(blockpos).getBlock() == this)
					worldIn.setBlockToAir(blockpos);

				worldIn.newExplosion((Entity) null, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, 5.0F, true, true);
				return true;
			}
		}
	}

	@Nullable
	private EntityPlayer getPlayerInBed(World worldIn, BlockPos pos) {
		for (EntityPlayer entityplayer : worldIn.playerEntities)
			if (entityplayer.isPlayerSleeping() && entityplayer.bedLocation.equals(pos))
				return entityplayer;
		return null;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		EnumFacing enumfacing = (EnumFacing) state.getValue(FACING);

		if (state.getValue(PART) == BlockLeafBed.EnumPartType.HEAD) {
			if (worldIn.getBlockState(pos.offset(enumfacing.getOpposite())).getBlock() != this)
				worldIn.setBlockToAir(pos);
		} else if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock() != this) {
			worldIn.setBlockToAir(pos);
			if (!worldIn.isRemote)
				this.dropBlockAsItem(worldIn, pos, state, 0);
		}
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BED_AABB;
	}

	@Nullable
	public static BlockPos getSafeExitLocation(World worldIn, BlockPos pos, int tries) {
		EnumFacing enumfacing = (EnumFacing) worldIn.getBlockState(pos).getValue(FACING);
		int i = pos.getX();
		int j = pos.getY();
		int k = pos.getZ();

		for (int l = 0; l <= 1; ++l) {
			int i1 = i - enumfacing.getFrontOffsetX() * l - 1;
			int j1 = k - enumfacing.getFrontOffsetZ() * l - 1;
			int k1 = i1 + 2;
			int l1 = j1 + 2;

			for (int i2 = i1; i2 <= k1; ++i2) {
				for (int j2 = j1; j2 <= l1; ++j2) {
					BlockPos blockpos = new BlockPos(i2, j, j2);

					if (hasRoomForPlayer(worldIn, blockpos)) {
						if (tries <= 0) {
							return blockpos;
						}

						--tries;
					}
				}
			}
		}

		return null;
	}

	protected static boolean hasRoomForPlayer(World worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos.down()).isOpaqueCube() && !worldIn.getBlockState(pos).getMaterial().isSolid() && !worldIn.getBlockState(pos.up()).getMaterial().isSolid();
	}

	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state) {
		return EnumPushReaction.DESTROY;
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
		if (player.capabilities.isCreativeMode && state.getValue(PART) == BlockLeafBed.EnumPartType.HEAD) {
			BlockPos blockpos = pos.offset(((EnumFacing) state.getValue(FACING)).getOpposite());
			if (worldIn.getBlockState(blockpos).getBlock() == this)
				worldIn.setBlockToAir(blockpos);
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing = EnumFacing.getHorizontal(meta);
		return (meta & 8) > 0 ? this.getDefaultState().withProperty(PART, BlockLeafBed.EnumPartType.HEAD).withProperty(FACING, enumfacing).withProperty(OCCUPIED, Boolean.valueOf((meta & 4) > 0)) : this.getDefaultState().withProperty(PART, BlockLeafBed.EnumPartType.FOOT).withProperty(FACING, enumfacing);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		if (state.getValue(PART) == BlockLeafBed.EnumPartType.FOOT) {
			IBlockState iblockstate = worldIn.getBlockState(pos.offset((EnumFacing) state.getValue(FACING)));
			if (iblockstate.getBlock() == this)
				state = state.withProperty(OCCUPIED, iblockstate.getValue(OCCUPIED));
		}
		return state;
	}

	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate((EnumFacing) state.getValue(FACING)));
	}

	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation((EnumFacing) state.getValue(FACING)));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int i = 0;
		i = i | ((EnumFacing) state.getValue(FACING)).getHorizontalIndex();

		if (state.getValue(PART) == BlockLeafBed.EnumPartType.HEAD) {
			i |= 8;

			if (((Boolean) state.getValue(OCCUPIED)).booleanValue()) {
				i |= 4;
			}
		}

		return i;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, PART, OCCUPIED });
	}

	public static enum EnumPartType implements IStringSerializable {
		HEAD("head"), FOOT("foot");

		private final String name;

		private EnumPartType(String name) {
			this.name = name;
		}

		public String toString() {
			return this.name;
		}

		public String getName() {
			return this.name;
		}
	}

}
