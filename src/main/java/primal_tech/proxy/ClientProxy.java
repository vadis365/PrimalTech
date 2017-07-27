package primal_tech.proxy;

import net.minecraft.block.properties.IProperty;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import primal_tech.PrimalTech;
import primal_tech.blocks.BlockWoodenHopper;
import primal_tech.client.render.TileEntityKilnRenderer;
import primal_tech.client.render.TileEntityStoneAnvilRenderer;
import primal_tech.client.render.TileEntityStoneGrillRenderer;
import primal_tech.client.render.TileEntityWaterSawRenderer;
import primal_tech.client.render.TileEntityWoodenHopperRenderer;
import primal_tech.client.render.TileEntityWorkStumpRenderer;
import primal_tech.client.render.TileEntityWorkStumpUpgradedRenderer;
import primal_tech.tiles.TileEntityKiln;
import primal_tech.tiles.TileEntityStoneAnvil;
import primal_tech.tiles.TileEntityStoneGrill;
import primal_tech.tiles.TileEntityWaterSaw;
import primal_tech.tiles.TileEntityWoodenHopper;
import primal_tech.tiles.TileEntityWorkStump;
import primal_tech.tiles.TileEntityWorkStumpUpgraded;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenderers() {
		ModelLoader.setCustomModelResourceLocation(PrimalTech.FIRE_STICKS, 0, new ModelResourceLocation("primal_tech:fire_sticks", "inventory"));
		ModelLoader.setCustomModelResourceLocation(PrimalTech.BONE_PICKAXE, 0, new ModelResourceLocation("primal_tech:bone_pickaxe", "inventory"));
		ModelLoader.setCustomModelResourceLocation(PrimalTech.BONE_AXE, 0, new ModelResourceLocation("primal_tech:bone_axe", "inventory"));
		ModelLoader.setCustomModelResourceLocation(PrimalTech.BONE_SHOVEL, 0, new ModelResourceLocation("primal_tech:bone_shovel", "inventory"));
		ModelLoader.setCustomModelResourceLocation(PrimalTech.BONE_SWORD, 0, new ModelResourceLocation("primal_tech:bone_sword", "inventory"));
		ModelLoader.setCustomModelResourceLocation(PrimalTech.BONE_KNIFE, 0, new ModelResourceLocation("primal_tech:bone_knife", "inventory"));
		ModelLoader.setCustomModelResourceLocation(PrimalTech.BONE_SHEARS, 0, new ModelResourceLocation("primal_tech:bone_shears", "inventory"));
		ModelLoader.setCustomModelResourceLocation(PrimalTech.BONE_SHARD, 0, new ModelResourceLocation("primal_tech:bone_shard", "inventory"));
		ModelLoader.setCustomModelResourceLocation(PrimalTech.PLANT_FIBRES, 0, new ModelResourceLocation("primal_tech:plant_fibres", "inventory"));
		ModelLoader.setCustomModelResourceLocation(PrimalTech.TWINE, 0, new ModelResourceLocation("primal_tech:twine", "inventory"));
		ModelLoader.setCustomModelResourceLocation(PrimalTech.FLINT_BLOCK_ITEM, 0, new ModelResourceLocation("primal_tech:flint_block", "inventory"));
		ModelLoader.setCustomModelResourceLocation(PrimalTech.CHARCOAL_BLOCK_ITEM, 0, new ModelResourceLocation("primal_tech:charcoal_block", "inventory"));
		ModelLoader.setCustomModelResourceLocation(PrimalTech.CLAY_KILN_ITEM, 0, new ModelResourceLocation("primal_tech:clay_kiln", "inventory"));
		ModelLoader.setCustomModelResourceLocation(PrimalTech.STICK_BUNDLE_ITEM, 0, new ModelResourceLocation("primal_tech:stick_bundle", "inventory"));
		ModelLoader.setCustomModelResourceLocation(PrimalTech.FIBRE_TORCH_ITEM, 0, new ModelResourceLocation("primal_tech:fibre_torch", "inventory"));
		ModelLoader.setCustomModelResourceLocation(PrimalTech.FIBRE_TORCH_ITEM_LIT, 0, new ModelResourceLocation("primal_tech:fibre_torch_lit", "inventory"));
		ModelLoader.setCustomModelResourceLocation(PrimalTech.WORK_STUMP_ITEM, 0, new ModelResourceLocation("primal_tech:work_stump", "inventory"));
		ModelLoader.setCustomModelResourceLocation(PrimalTech.WORK_STUMP_II_ITEM, 0, new ModelResourceLocation("primal_tech:work_stump_upgraded", "inventory"));
		ModelLoader.setCustomModelResourceLocation(PrimalTech.ROCK, 0, new ModelResourceLocation("primal_tech:rock", "inventory"));
		ModelLoader.setCustomModelResourceLocation(PrimalTech.WOOD_CLUB, 0, new ModelResourceLocation("primal_tech:wood_club", "inventory"));
		ModelLoader.setCustomModelResourceLocation(PrimalTech.STONE_CLUB, 0, new ModelResourceLocation("primal_tech:stone_club", "inventory"));
		ModelLoader.setCustomModelResourceLocation(PrimalTech.BONE_CLUB, 0, new ModelResourceLocation("primal_tech:bone_club", "inventory"));
		ModelLoader.setCustomModelResourceLocation(PrimalTech.STONE_GRILL_ITEM, 0, new ModelResourceLocation("primal_tech:stone_grill", "inventory"));
		ModelLoader.setCustomModelResourceLocation(PrimalTech.WOODEN_HOPPER_ITEM, 0, new ModelResourceLocation("primal_tech:wooden_hopper", "inventory"));
		ModelLoader.setCustomStateMapper((PrimalTech.WOODEN_HOPPER), (new StateMap.Builder()).ignore(new IProperty[] {BlockWoodenHopper.ENABLED}).build());
		ModelLoader.setCustomModelResourceLocation(PrimalTech.CHARCOAL_HOPPER_ITEM, 0, new ModelResourceLocation("primal_tech:charcoal_hopper", "inventory"));
		ModelLoader.setCustomStateMapper((PrimalTech.CHARCOAL_HOPPER), (new StateMap.Builder()).ignore(new IProperty[] {BlockWoodenHopper.ENABLED}).build());
		ModelLoader.setCustomModelResourceLocation(PrimalTech.WATER_SAW_ITEM, 0, new ModelResourceLocation("primal_tech:water_saw", "inventory"));
		ModelLoader.setCustomModelResourceLocation(PrimalTech.FLINT_SAW_BLADE, 0, new ModelResourceLocation("primal_tech:flint_edged_disc", "inventory"));
		ModelLoader.setCustomModelResourceLocation(PrimalTech.FLUID_BLADDER, 0, new ModelResourceLocation("primal_tech:fluid_bladder_empty", "inventory"));
		ModelLoader.setCustomModelResourceLocation(PrimalTech.FLUID_BLADDER, 1, new ModelResourceLocation("primal_tech:fluid_bladder_filled", "inventory"));
		ModelLoader.setCustomModelResourceLocation(PrimalTech.STONE_ANVIL_ITEM, 0, new ModelResourceLocation("primal_tech:stone_anvil", "inventory"));
		ModelLoader.setCustomModelResourceLocation(PrimalTech.STONE_MALLET, 0, new ModelResourceLocation("primal_tech:stone_mallet", "inventory"));

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityKiln.class, new TileEntityKilnRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWorkStump.class, new TileEntityWorkStumpRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWorkStumpUpgraded.class, new TileEntityWorkStumpUpgradedRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityStoneGrill.class, new TileEntityStoneGrillRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWoodenHopper.class, new TileEntityWoodenHopperRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWaterSaw.class, new TileEntityWaterSawRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityStoneAnvil.class, new TileEntityStoneAnvilRenderer());
	}

	@Override
	public void postInit() {
		ForgeHooksClient.registerTESRItemStack(PrimalTech.WATER_SAW_ITEM, 0, TileEntityWaterSaw.class);
	}
}
