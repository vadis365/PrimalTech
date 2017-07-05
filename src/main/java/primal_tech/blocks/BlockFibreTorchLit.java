package primal_tech.blocks;

import java.util.Random;

import net.minecraft.block.BlockTorch;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import primal_tech.PrimalTech;

public class BlockFibreTorchLit extends BlockTorch {

	public BlockFibreTorchLit() {
		setSoundType(SoundType.WOOD);
		setLightLevel(0.6F);
		setCreativeTab(PrimalTech.TAB);
	}

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        EnumFacing enumfacing = (EnumFacing)stateIn.getValue(FACING);
        double xPos = (double)pos.getX() + 0.5D;
        double yPos = (double)pos.getY() + 0.7D;
        double zPos = (double)pos.getZ() + 0.5D;

        if (enumfacing.getAxis().isHorizontal()) {
            EnumFacing enumfacing1 = enumfacing.getOpposite();
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, xPos + 0.27D * (double)enumfacing1.getFrontOffsetX(), yPos + 0.22D, zPos + 0.27D * (double)enumfacing1.getFrontOffsetZ(), 0.0D, 0.0D, 0.0D, new int[0]);
            worldIn.spawnParticle(EnumParticleTypes.FLAME, xPos + 0.27D * (double)enumfacing1.getFrontOffsetX(), yPos + 0.22D, zPos + 0.27D * (double)enumfacing1.getFrontOffsetZ(), 0.0D, 0.0D, 0.0D, new int[0]);
        }
        else {
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, xPos, yPos, zPos, 0.0D, 0.0D, 0.0D, new int[0]);
            worldIn.spawnParticle(EnumParticleTypes.FLAME, xPos, yPos, zPos, 0.0D, 0.0D, 0.0D, new int[0]);
        }
    }
}
