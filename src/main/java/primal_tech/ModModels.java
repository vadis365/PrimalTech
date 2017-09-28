package primal_tech;

import net.minecraft.block.properties.IProperty;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import primal_tech.blocks.BlockLeafBed;
import primal_tech.blocks.BlockWoodenHopper;

@Mod.EventBusSubscriber(modid = "primal_tech", value = Side.CLIENT)
@SideOnly(Side.CLIENT)
public class ModModels {
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(ModItems.FIRE_STICKS, 0, new ModelResourceLocation("primal_tech:fire_sticks", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.BONE_PICKAXE, 0, new ModelResourceLocation("primal_tech:bone_pickaxe", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.BONE_AXE, 0, new ModelResourceLocation("primal_tech:bone_axe", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.BONE_SHOVEL, 0, new ModelResourceLocation("primal_tech:bone_shovel", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.BONE_SWORD, 0, new ModelResourceLocation("primal_tech:bone_sword", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.BONE_KNIFE, 0, new ModelResourceLocation("primal_tech:bone_knife", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.BONE_SHEARS, 0, new ModelResourceLocation("primal_tech:bone_shears", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.BONE_SHARD, 0, new ModelResourceLocation("primal_tech:bone_shard", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.PLANT_FIBRES, 0, new ModelResourceLocation("primal_tech:plant_fibres", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.TWINE, 0, new ModelResourceLocation("primal_tech:twine", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModBlocks.FLINT_BLOCK_ITEM, 0, new ModelResourceLocation("primal_tech:flint_block", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModBlocks.CHARCOAL_BLOCK_ITEM, 0, new ModelResourceLocation("primal_tech:charcoal_block", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModBlocks.CLAY_KILN_ITEM, 0, new ModelResourceLocation("primal_tech:clay_kiln", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModBlocks.STICK_BUNDLE_ITEM, 0, new ModelResourceLocation("primal_tech:stick_bundle", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModBlocks.FIBRE_TORCH_ITEM, 0, new ModelResourceLocation("primal_tech:fibre_torch", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModBlocks.FIBRE_TORCH_ITEM_LIT, 0, new ModelResourceLocation("primal_tech:fibre_torch_lit", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModBlocks.WORK_STUMP_ITEM, 0, new ModelResourceLocation("primal_tech:work_stump", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModBlocks.WORK_STUMP_II_ITEM, 0, new ModelResourceLocation("primal_tech:work_stump_upgraded", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.ROCK, 0, new ModelResourceLocation("primal_tech:rock", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.WOOD_CLUB, 0, new ModelResourceLocation("primal_tech:wood_club", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.STONE_CLUB, 0, new ModelResourceLocation("primal_tech:stone_club", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.BONE_CLUB, 0, new ModelResourceLocation("primal_tech:bone_club", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModBlocks.STONE_GRILL_ITEM, 0, new ModelResourceLocation("primal_tech:stone_grill", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModBlocks.WOODEN_HOPPER_ITEM, 0, new ModelResourceLocation("primal_tech:wooden_hopper", "inventory"));
		ModelLoader.setCustomStateMapper((ModBlocks.WOODEN_HOPPER), (new StateMap.Builder()).ignore(new IProperty[] {BlockWoodenHopper.ENABLED}).build());
		ModelLoader.setCustomModelResourceLocation(ModBlocks.CHARCOAL_HOPPER_ITEM, 0, new ModelResourceLocation("primal_tech:charcoal_hopper", "inventory"));
		ModelLoader.setCustomStateMapper((ModBlocks.CHARCOAL_HOPPER), (new StateMap.Builder()).ignore(new IProperty[] {BlockWoodenHopper.ENABLED}).build());
		ModelLoader.setCustomModelResourceLocation(ModBlocks.WATER_SAW_ITEM, 0, new ModelResourceLocation("primal_tech:water_saw", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.FLINT_SAW_BLADE, 0, new ModelResourceLocation("primal_tech:flint_edged_disc", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.FLUID_BLADDER, 0, new ModelResourceLocation("primal_tech:fluid_bladder_empty", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.FLUID_BLADDER, 1, new ModelResourceLocation("primal_tech:fluid_bladder_filled", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModBlocks.STONE_ANVIL_ITEM, 0, new ModelResourceLocation("primal_tech:stone_anvil", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.STONE_MALLET, 0, new ModelResourceLocation("primal_tech:stone_mallet", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.LEAF_BED_ITEM, 0, new ModelResourceLocation("primal_tech:leaf_bed", "inventory"));
		ModelLoader.setCustomStateMapper((ModBlocks.LEAF_BED), (new StateMap.Builder()).ignore(new IProperty[] {BlockLeafBed.OCCUPIED}).build());
		ModelLoader.setCustomModelResourceLocation(ModBlocks.WOODEN_BASIN_ITEM, 0, new ModelResourceLocation("primal_tech:wooden_basin", "inventory"));
	}
}	
