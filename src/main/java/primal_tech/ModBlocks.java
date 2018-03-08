package primal_tech;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import primal_tech.blocks.BlockCharcoal;
import primal_tech.blocks.BlockClayKiln;
import primal_tech.blocks.BlockFibreTorch;
import primal_tech.blocks.BlockFibreTorchLit;
import primal_tech.blocks.BlockFlint;
import primal_tech.blocks.BlockLeafBed;
import primal_tech.blocks.BlockStickBundle;
import primal_tech.blocks.BlockStoneAnvil;
import primal_tech.blocks.BlockStoneGrill;
import primal_tech.blocks.BlockWaterSaw;
import primal_tech.blocks.BlockWoodenBasin;
import primal_tech.blocks.BlockWoodenHopper;
import primal_tech.blocks.BlockWorkStump;
import primal_tech.blocks.BlockWorkStumpUpgrade;

public class ModBlocks {

	public static ItemBlock FLINT_BLOCK_ITEM, CHARCOAL_BLOCK_ITEM, CLAY_KILN_ITEM, STICK_BUNDLE_ITEM, FIBRE_TORCH_ITEM,
		WOODEN_HOPPER_ITEM, CHARCOAL_HOPPER_ITEM, FIBRE_TORCH_ITEM_LIT, WORK_STUMP_ITEM, STONE_GRILL_ITEM, WORK_STUMP_II_ITEM,
		WATER_SAW_ITEM, STONE_ANVIL_ITEM, WOODEN_BASIN_ITEM;
	public static Block CLAY_KILN, FLINT_BLOCK, STICK_BUNDLE, FIBRE_TORCH, FIBRE_TORCH_LIT, CHARCOAL_BLOCK, WORK_STUMP,
		STONE_GRILL, WOODEN_HOPPER, WORK_STUMP_II, CHARCOAL_HOPPER, WATER_SAW, STONE_ANVIL, LEAF_BED, WOODEN_BASIN;

	public static void init() {
		// Blocks and Item Blocks
		CLAY_KILN = new BlockClayKiln();
		CLAY_KILN_ITEM = new ItemBlock(CLAY_KILN) {
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> list, ITooltipFlag flag) {
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.clay_kiln").getFormattedText());
			}
		};
		CLAY_KILN.setRegistryName("primal_tech", "clay_kiln").setUnlocalizedName("primal_tech.clay_kiln");
		CLAY_KILN_ITEM.setRegistryName(CLAY_KILN.getRegistryName()).setUnlocalizedName("primal_tech.clay_kiln");

		FLINT_BLOCK = new BlockFlint();
		FLINT_BLOCK_ITEM = new ItemBlock(FLINT_BLOCK);
		FLINT_BLOCK.setRegistryName("primal_tech", "flint_block").setUnlocalizedName("primal_tech.flint_block");
		FLINT_BLOCK_ITEM.setRegistryName(FLINT_BLOCK.getRegistryName()).setUnlocalizedName("primal_tech.flint_block");

		STICK_BUNDLE = new BlockStickBundle();
		STICK_BUNDLE_ITEM = new ItemBlock(STICK_BUNDLE) {
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> list, ITooltipFlag flag) {
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.stick_bundle").getFormattedText());
			}
		};
		STICK_BUNDLE.setRegistryName("primal_tech", "stick_bundle").setUnlocalizedName("primal_tech.stick_bundle");
		STICK_BUNDLE_ITEM.setRegistryName(STICK_BUNDLE.getRegistryName()).setUnlocalizedName("primal_tech.stick_bundle");

		FIBRE_TORCH = new BlockFibreTorch();
		FIBRE_TORCH_ITEM = new ItemBlock(FIBRE_TORCH);
		FIBRE_TORCH.setRegistryName("primal_tech", "fibre_torch").setUnlocalizedName("primal_tech.fibre_torch");
		FIBRE_TORCH_ITEM.setRegistryName(FIBRE_TORCH.getRegistryName()).setUnlocalizedName("primal_tech.fibre_torch");

		FIBRE_TORCH_LIT = new BlockFibreTorchLit();
		FIBRE_TORCH_ITEM_LIT = new ItemBlock(FIBRE_TORCH_LIT) {
			@Override
			public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
				target.setFire(5);
				return true;
			}
		};
		FIBRE_TORCH_ITEM_LIT.setCreativeTab(PrimalTech.TAB);
		FIBRE_TORCH_LIT.setRegistryName("primal_tech", "fibre_torch_lit").setUnlocalizedName("primal_tech.fibre_torch_lit");
		FIBRE_TORCH_ITEM_LIT.setRegistryName(FIBRE_TORCH_LIT.getRegistryName()).setUnlocalizedName("primal_tech.fibre_torch_lit");

		CHARCOAL_BLOCK = new BlockCharcoal();
		CHARCOAL_BLOCK_ITEM = new ItemBlock(CHARCOAL_BLOCK) {
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> list, ITooltipFlag flag) {
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.charcoal_block").getFormattedText());
			}
		};
		CHARCOAL_BLOCK.setRegistryName("primal_tech", "charcoal_block").setUnlocalizedName("primal_tech.charcoal_block");
		CHARCOAL_BLOCK_ITEM.setRegistryName(CHARCOAL_BLOCK.getRegistryName()).setUnlocalizedName("primal_tech.charcoal_block");

		WORK_STUMP = new BlockWorkStump();
		WORK_STUMP_ITEM = new ItemBlock(WORK_STUMP) {
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> list, ITooltipFlag flag) {
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.work_stump").getFormattedText());
			}
		};
		WORK_STUMP.setRegistryName("primal_tech", "work_stump").setUnlocalizedName("primal_tech.work_stump");
		WORK_STUMP_ITEM.setRegistryName(WORK_STUMP.getRegistryName()).setUnlocalizedName("primal_tech.work_stump");

		STONE_GRILL = new BlockStoneGrill();
		STONE_GRILL_ITEM = new ItemBlock(STONE_GRILL) {
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> list, ITooltipFlag flag) {
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.stone_grill").getFormattedText());
			}
		};
		STONE_GRILL.setRegistryName("primal_tech", "stone_grill").setUnlocalizedName("primal_tech.stone_grill");
		STONE_GRILL_ITEM.setRegistryName(STONE_GRILL.getRegistryName()).setUnlocalizedName("primal_tech.stone_grill");

		WOODEN_HOPPER = new BlockWoodenHopper();
		WOODEN_HOPPER_ITEM = new ItemBlock(WOODEN_HOPPER) {
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> list, ITooltipFlag flag) {
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.wooden_hopper").getFormattedText());
			}
		};
		WOODEN_HOPPER.setRegistryName("primal_tech", "wooden_hopper").setUnlocalizedName("primal_tech.wooden_hopper");
		WOODEN_HOPPER_ITEM.setRegistryName(WOODEN_HOPPER.getRegistryName()).setUnlocalizedName("primal_tech.wooden_hopper");

		CHARCOAL_HOPPER = new BlockWoodenHopper() {
			@SideOnly(Side.CLIENT)
			public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
				if (rand.nextInt(24) == 0)
					world.playSound((double) ((float) pos.getX() + 0.5F), (double) ((float) pos.getY() + 0.5F), (double) ((float) pos.getZ() + 0.5F), SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F, false);

				for (int i = 0; i < 3; ++i) {
					double d0 = (double) pos.getX() + rand.nextDouble();
					double d1 = (double) pos.getY() + rand.nextDouble() * 0.5D + 0.5D;
					double d2 = (double) pos.getZ() + rand.nextDouble();
					world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
				}
			}
		};
		CHARCOAL_HOPPER_ITEM = new ItemBlock(CHARCOAL_HOPPER) {
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> list, ITooltipFlag flag) {
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.charcoal_hopper").getFormattedText());
			}
		};
		CHARCOAL_HOPPER.setRegistryName("primal_tech", "charcoal_hopper").setUnlocalizedName("primal_tech.charcoal_hopper");
		CHARCOAL_HOPPER_ITEM.setRegistryName(CHARCOAL_HOPPER.getRegistryName()).setUnlocalizedName("primal_tech.charcoal_hopper");

		WORK_STUMP_II = new BlockWorkStumpUpgrade();
		WORK_STUMP_II_ITEM = new ItemBlock(WORK_STUMP_II) {
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> list, ITooltipFlag flag) {
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.work_stump_mk2").getFormattedText());
			}
		};
		WORK_STUMP_II.setRegistryName("primal_tech", "work_stump_upgraded").setUnlocalizedName("primal_tech.work_stump_upgraded");
		WORK_STUMP_II_ITEM.setRegistryName(WORK_STUMP_II.getRegistryName()).setUnlocalizedName("primal_tech.work_stump_upgraded");

		WATER_SAW = new BlockWaterSaw();
		WATER_SAW_ITEM = new ItemBlock(WATER_SAW) {
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> list, ITooltipFlag flag) {
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.water_saw").getFormattedText());
			}
		};
		WATER_SAW.setRegistryName("primal_tech", "water_saw").setUnlocalizedName("primal_tech.water_saw");
		WATER_SAW_ITEM.setRegistryName(WATER_SAW.getRegistryName()).setUnlocalizedName("primal_tech.water_saw");

		STONE_ANVIL = new BlockStoneAnvil();
		STONE_ANVIL_ITEM = new ItemBlock(STONE_ANVIL) {
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> list, ITooltipFlag flag) {
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.stone_anvil").getFormattedText());
			}
		};
		STONE_ANVIL.setRegistryName("primal_tech", "stone_anvil").setUnlocalizedName("primal_tech.stone_anvil");
		STONE_ANVIL_ITEM.setRegistryName(STONE_ANVIL.getRegistryName()).setUnlocalizedName("primal_tech.stone_anvil");

		LEAF_BED = new BlockLeafBed();
		LEAF_BED.setRegistryName("primal_tech", "leaf_bed").setUnlocalizedName("primal_tech.leaf_bed");

		WOODEN_BASIN = new BlockWoodenBasin();
		WOODEN_BASIN_ITEM = new ItemBlock(WOODEN_BASIN) {
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> list, ITooltipFlag flag) {
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.wooden_basin").getFormattedText());
			}
		};
		WOODEN_BASIN.setRegistryName("primal_tech", "wooden_basin").setUnlocalizedName("primal_tech.wooden_basin");
		WOODEN_BASIN_ITEM.setRegistryName(WOODEN_BASIN.getRegistryName()).setUnlocalizedName("primal_tech.wooden_basin");
	}

	@Mod.EventBusSubscriber(modid = "primal_tech")
	public static class RegistrationHandlerBlocks {
		public static final List<Block> BLOCKS = new ArrayList<Block>();
		public static final List<Item> ITEM_BLOCKS = new ArrayList<Item>();

		@SubscribeEvent
		public static void registerBlocks(final RegistryEvent.Register<Block> event) {
			init();
			final Block[] blocks = {
					CLAY_KILN,
					FLINT_BLOCK,
					STICK_BUNDLE,
					FIBRE_TORCH,
					FIBRE_TORCH_LIT,
					CHARCOAL_BLOCK,
					WORK_STUMP,
					STONE_GRILL,
					WOODEN_HOPPER,
					WORK_STUMP_II,
					CHARCOAL_HOPPER,
					WATER_SAW,
					STONE_ANVIL,
					LEAF_BED, 
					WOODEN_BASIN
					};
			final IForgeRegistry<Block> registry = event.getRegistry();
			for (final Block block : blocks) {
				registry.register(block);
				BLOCKS.add(block);
			}
		}

		@SubscribeEvent
		public static void registerItemBlocks(final RegistryEvent.Register<Item> event) {
			final ItemBlock[] items = {
					FLINT_BLOCK_ITEM,
					CHARCOAL_BLOCK_ITEM,
					CLAY_KILN_ITEM,
					STICK_BUNDLE_ITEM,
					FIBRE_TORCH_ITEM,
					WOODEN_HOPPER_ITEM,
					CHARCOAL_HOPPER_ITEM,
					FIBRE_TORCH_ITEM_LIT,
					WORK_STUMP_ITEM,
					STONE_GRILL_ITEM,
					WORK_STUMP_II_ITEM,
					WATER_SAW_ITEM,
					STONE_ANVIL_ITEM, 
					WOODEN_BASIN_ITEM
					};
			final IForgeRegistry<Item> registry = event.getRegistry();
			for (final ItemBlock item : items) {
				registry.register(item);
				ITEM_BLOCKS.add(item);
			}
		}
	}

}
