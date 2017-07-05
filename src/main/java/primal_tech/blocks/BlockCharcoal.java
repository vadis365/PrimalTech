package primal_tech.blocks;

import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import primal_tech.PrimalTech;
import primal_tech.configs.ConfigHandler;

public class BlockCharcoal extends BlockRotatedPillar {

	public static final PropertyEnum<BlockLog.EnumAxis> LOG_AXIS = PropertyEnum.<BlockLog.EnumAxis>create("axis", BlockLog.EnumAxis.class);
	
	public BlockCharcoal() {
		super(Material.WOOD);
		setHarvestLevel("axe", 0);
        setHardness(2.0F);
        setSoundType(SoundType.WOOD);
		setCreativeTab(PrimalTech.TAB);
		setDefaultState(blockState.getBaseState().withProperty(LOG_AXIS, BlockLog.EnumAxis.Y));
	}

	@Override
	 public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return this.getStateFromMeta(meta).withProperty(LOG_AXIS, BlockLog.EnumAxis.fromFacingAxis(facing.getAxis()));
    }

	@Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        switch (rot) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                switch ((BlockLog.EnumAxis)state.getValue(LOG_AXIS)) {
                    case X:
                        return state.withProperty(LOG_AXIS, BlockLog.EnumAxis.Z);
                    case Z:
                        return state.withProperty(LOG_AXIS, BlockLog.EnumAxis.X);
                    default:
                        return state;
                }
            default:
                return state;
        }
    }

	@Override
	public IBlockState getStateFromMeta(int meta) {
		IBlockState state = getDefaultState();
		switch (meta & 12) {
			case 0:
				state = state.withProperty(LOG_AXIS, BlockLog.EnumAxis.Y);
				break;
			case 4:
				state = state.withProperty(LOG_AXIS, BlockLog.EnumAxis.X);
				break;
			case 8:
				state = state.withProperty(LOG_AXIS, BlockLog.EnumAxis.Z);
				break;
			default:
				state = state.withProperty(LOG_AXIS, BlockLog.EnumAxis.NONE);
		}
		return state;
	}

	@Override
	@SuppressWarnings("incomplete-switch")
	public int getMetaFromState(IBlockState state) {
		int meta = 0;
		switch (state.getValue(LOG_AXIS)) {
			case X:
				meta |= 4;
				break;
			case Z:
				meta |= 8;
				break;
			case NONE:
				meta |= 12;
				break;
		}

		return meta;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { LOG_AXIS });
	}

	@Override
    public boolean isFireSource(World world, BlockPos pos, EnumFacing side) {
        if (side == EnumFacing.UP)
            return true;
        return false;
    }

	@Override
    public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return ConfigHandler.CHARCOAL_BURN_TIME;
    }

}