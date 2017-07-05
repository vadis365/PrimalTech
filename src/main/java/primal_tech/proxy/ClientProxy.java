package primal_tech.proxy;

import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import primal_tech.ModBlocks;
import primal_tech.client.render.TileEntityKilnRenderer;
import primal_tech.client.render.TileEntityStoneGrillRenderer;
import primal_tech.client.render.TileEntityWaterSawRenderer;
import primal_tech.client.render.TileEntityWoodenHopperRenderer;
import primal_tech.client.render.TileEntityWorkStumpRenderer;
import primal_tech.client.render.TileEntityWorkStumpUpgradedRenderer;
import primal_tech.tiles.TileEntityKiln;
import primal_tech.tiles.TileEntityStoneGrill;
import primal_tech.tiles.TileEntityWaterSaw;
import primal_tech.tiles.TileEntityWoodenHopper;
import primal_tech.tiles.TileEntityWorkStump;
import primal_tech.tiles.TileEntityWorkStumpUpgraded;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenderers() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityKiln.class, new TileEntityKilnRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWorkStump.class, new TileEntityWorkStumpRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWorkStumpUpgraded.class, new TileEntityWorkStumpUpgradedRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityStoneGrill.class, new TileEntityStoneGrillRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWoodenHopper.class, new TileEntityWoodenHopperRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWaterSaw.class, new TileEntityWaterSawRenderer());
	}

	@Override
	public void postInit() {
		ForgeHooksClient.registerTESRItemStack(ModBlocks.WATER_SAW_ITEM, 0, TileEntityWaterSaw.class);
	}
}
