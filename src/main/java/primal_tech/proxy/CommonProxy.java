package primal_tech.proxy;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;
import primal_tech.tiles.TileEntityKiln;
import primal_tech.tiles.TileEntityStoneAnvil;
import primal_tech.tiles.TileEntityStoneGrill;
import primal_tech.tiles.TileEntityWaterSaw;
import primal_tech.tiles.TileEntityWoodenHopper;
import primal_tech.tiles.TileEntityWorkStump;
import primal_tech.tiles.TileEntityWorkStumpUpgraded;

public class CommonProxy {

	public void registerRenderers() {}

	public void registerTileEntities() {
		registerTileEntity(TileEntityKiln.class, "clay_kiln");
		registerTileEntity(TileEntityWorkStump.class, "work_stump");
		registerTileEntity(TileEntityStoneGrill.class, "stone_grill");
		registerTileEntity(TileEntityWoodenHopper.class, "wooden_hopper");
		registerTileEntity(TileEntityWorkStumpUpgraded.class, "work_stump_upgraded");
		registerTileEntity(TileEntityWaterSaw.class, "water_saw");
		registerTileEntity(TileEntityStoneAnvil.class, "stone_anvil");
	}

	private void registerTileEntity(Class<? extends TileEntity> cls, String baseName) {
		GameRegistry.registerTileEntity(cls, "tile.primal_tech." + baseName);
	}

	public void postInit() {
	}

}
