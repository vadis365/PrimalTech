package primal_tech.blocks;

import java.util.Random;

import net.minecraft.block.BlockTorch;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import primal_tech.PrimalTech;

public class BlockFibreTorch extends BlockTorch {

	public BlockFibreTorch() {
		setSoundType(SoundType.WOOD);
		setLightLevel(0F);
		setCreativeTab(PrimalTech.TAB);
	}

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			return true;
		} else {
			ItemStack mainHandItem = player.getHeldItem(EnumHand.MAIN_HAND);
			if (!mainHandItem.isEmpty() && mainHandItem.getItem() == PrimalTech.FIBRE_TORCH_ITEM_LIT) {
				world.setBlockState(pos, PrimalTech.FIBRE_TORCH_LIT.getDefaultState().withProperty(FACING, state.getValue(FACING)), 3);
				world.playSound((EntityPlayer)null, pos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 0.2F, 0.1F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
			}
			return true;
		}
	}

}
