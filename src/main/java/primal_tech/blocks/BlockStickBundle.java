package primal_tech.blocks;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import primal_tech.PrimalTech;

public class BlockStickBundle extends Block {

	protected static final AxisAlignedBB STICK_BUNDLE_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 1.0D, 0.875D);
	public static final PropertyInteger IGNITION_STAGE = PropertyInteger.create("ignition", 0, 15);

	public BlockStickBundle() {
		super(Material.WOOD);
		setHardness(0.5F);
		setResistance(5.0F);
		setSoundType(SoundType.PLANT);
		setDefaultState(blockState.getBaseState().withProperty(IGNITION_STAGE, Integer.valueOf(0)));
		setTickRandomly(true);
		setCreativeTab(PrimalTech.TAB);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return STICK_BUNDLE_AABB;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return STICK_BUNDLE_AABB;
	}

	@Nullable
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return null;
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		List<ItemStack> ret = super.getDrops(world, pos, state, fortune);
		ret.add(new ItemStack(Items.STICK, 7, 0));
		ret.add(new ItemStack(PrimalTech.TWINE, 2, 0));
		return ret;
	}

	@Override
	public int damageDropped(IBlockState state) {
		return 0;
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
    public boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return true;
    }

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
    public int tickRate(World world) {
        return 1;
    }

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		int stage = ((Integer) state.getValue(IGNITION_STAGE)).intValue();
		if (stage > 0)
			world.setBlockState(pos, state.withProperty(IGNITION_STAGE, Integer.valueOf(0)), 3);
	}

	@Override
	 public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return this.getDefaultState();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			return true;
		} else {
			ItemStack mainHandItem = player.getHeldItem(EnumHand.MAIN_HAND);
			if (!mainHandItem.isEmpty() && mainHandItem.getItem() == Items.STICK) {
				state = state.cycleProperty(IGNITION_STAGE);
				world.setBlockState(pos, state, 3);
				if(state.getValue(IGNITION_STAGE).intValue() == 15) {
					world.setBlockState(pos, Blocks.FIRE.getDefaultState(), 3);
					mainHandItem.shrink(0);
				}
			}
			if (!mainHandItem.isEmpty() && mainHandItem.getItem() == PrimalTech.FIBRE_TORCH_ITEM_LIT) {
				state = state.cycleProperty(IGNITION_STAGE);
				world.setBlockState(pos, Blocks.FIRE.getDefaultState(), 3);
				world.playSound((EntityPlayer)null, pos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 0.2F, 0.1F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
			}
			return true;
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(IGNITION_STAGE, Integer.valueOf(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return ((Integer) state.getValue(IGNITION_STAGE)).intValue();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { IGNITION_STAGE });
	}

	@Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(Item.getItemFromBlock(this));
    }
	
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {

		double d0 = (double) pos.getX() + 0.5D;
		double d1 = (double) pos.getY() + 1.0D;
		double d2 = (double) pos.getZ() + 0.5D;
		double d3 = 0.22D;
		double d4 = 0.27D;
		if(state.getValue(IGNITION_STAGE).intValue() > 4)
			world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
		if(state.getValue(IGNITION_STAGE).intValue() > 7)
			world.spawnParticle(EnumParticleTypes.FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
	}
}
