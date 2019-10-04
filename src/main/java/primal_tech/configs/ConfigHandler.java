package primal_tech.configs;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ConfigHandler {

	public static final ConfigHandler INSTANCE = new ConfigHandler();
	public Configuration CONFIG;
	public static String[] CLAY_KILN_RECIPES;
	public static String[] WATER_SAW_RECIPES;
	public static String[] STONE_ANVIL_RECIPES;
	public static String[] FIRE_SOURCES;
	public static String[] WOODEN_BASIN_RECIPES;
	public static String[] USED_CATEGORIES = { "Clay Kiln Recipes", "Blocks Considered as Fire Sources", "Work Stump Setting", "Charcoal", "Water Powered Saw Recipes", "Fluid Bladder", "Stone Anvil Setting", "Stone Anvil Recipes", "Wooden Basin Setting", "Wooden Basin Recipes", "Clubs and Bone Shards" };
	public static int WORK_STUMP_DAMAGE;
	public static int WORK_STUMP_II_DAMAGE;
	public static int CRAFTING_STRIKES;
	public static int CRAFTING_STRIKES_II;
	public static int ROCK_DAMAGE;
	public static int MALLET_DAMAGE;
	public static int CHARCOAL_BURN_TIME;
	public static boolean FLUID_BLADDER_PLACES_FLUID;
	public static boolean FLUID_BLADDER_LAVA_PICKUP;
	public static int STONE_ANVIL_DAMAGE;
	public static int STONE_ANVIL_CRAFTING_STRIKES;
	public static int WOODEN_BASIN_STIRS;
	public static boolean DROP_BONE_SHARD;
	public static int BONE_SHARD_RATE;
	public void loadConfig(FMLPreInitializationEvent event) {
		CONFIG = new Configuration(event.getSuggestedConfigurationFile());
		CONFIG.load();
		syncConfigs();
	}

	private void syncConfigs() {
		CONFIG.addCustomCategoryComment("Clay Kiln Recipes", "output, input syntax is: modName:itemName,metaData#modName:itemName,metaData#cookingSpeedInTicks");
		CLAY_KILN_RECIPES = CONFIG.getStringList("Clay Kiln Recipes", "Clay Kiln Recipes", new String[] { 
				"primal_tech:flint_block,0#minecraft:gravel,0#200",
				"primal_tech:charcoal_block,0#minecraft:log,0#200",
				"primal_tech:charcoal_block,0#minecraft:log,1#200",
				"primal_tech:charcoal_block,0#minecraft:log,2#200",
				"primal_tech:charcoal_block,0#minecraft:log,3#200",
				"primal_tech:charcoal_block,0#minecraft:log2,0#200",
				"primal_tech:charcoal_block,0#minecraft:log2,1#200"
				}, "Happy Birthday!");
		
		CONFIG.addCustomCategoryComment("Water Powered Saw Recipes", "output, input syntax is: modName:itemName,metaData,outputAmount#modName:itemName,metaData#choppingSpeedInTicks");
		WATER_SAW_RECIPES = CONFIG.getStringList("Water Powered Saw Recipes", "Water Powered Saw Recipes", new String[] {
		"minecraft:planks,0,4#minecraft:log,0#80",
		"minecraft:planks,1,4#minecraft:log,1#80",
		"minecraft:planks,2,4#minecraft:log,2#80",
		"minecraft:planks,3,4#minecraft:log,3#80",
		"minecraft:planks,4,4#minecraft:log2,0#80",
		"minecraft:planks,5,4#minecraft:log2,1#80"
		}, "Happy Birthday!");

		CONFIG.addCustomCategoryComment("Stone Anvil Recipes", "output, input syntax is: modName:itemName,metaData,amount#modName:itemName,metaData");
		STONE_ANVIL_RECIPES = CONFIG.getStringList("Stone Anvil Recipes", "Stone Anvil Recipes", new String[] { "minecraft:flint,0,4#primal_tech:flint_block,0"}, "Happy Birthday!");

		CONFIG.addCustomCategoryComment("Blocks Considered as Fire Sources", "Used By unlit torches (can be used for more later :P ) " );
		FIRE_SOURCES = CONFIG.getStringList("Fire Source blocks", "Blocks Considered as Fire Sources", new String[] { "minecraft:fire", "primal_tech:fibre_torch_lit"}, "Happy Birthday!");

		CONFIG.addCustomCategoryComment("Wooden Basin Recipes", "output, input syntax is: modName:itemName,metaData,outputAmount#fluidName#modName:itemName,metaData (up to 4 input Items seperated with commas");
		WOODEN_BASIN_RECIPES = CONFIG.getStringList("Wooden Basin Recipes", "Wooden Basin Recipes", new String[] {
		"minecraft:obsidian,0,1#lava#minecraft:cobblestone,0,minecraft:cobblestone,0"
		}, "Happy Birthday!");

		WORK_STUMP_DAMAGE = CONFIG.getInt("Crafting Uses Before Stump Breaks", "Work Stump Setting", 20, 3, Integer.MAX_VALUE, "");

		WORK_STUMP_II_DAMAGE = CONFIG.getInt("Crafting Uses Before Upgraded Stump Breaks", "Work Stump Setting", 40, 3, Integer.MAX_VALUE, "");

		STONE_ANVIL_DAMAGE = CONFIG.getInt("Crafting Uses Before Stone Anvil Breaks", "Stone Anvil Setting", 20, 3, Integer.MAX_VALUE, "");

		CRAFTING_STRIKES = CONFIG.getInt("Craftng Rock Strikes Needed To Craft on The Stump", "Work Stump Setting", 4, 1, Integer.MAX_VALUE, "");

		CRAFTING_STRIKES_II = CONFIG.getInt("Craftng Rock Strikes Needed To Craft on The Upgraded Stump", "Work Stump Setting", 4, 1, Integer.MAX_VALUE, "");

		ROCK_DAMAGE = CONFIG.get("Work Stump Setting", "Max Damage Of Crafing Rock", 160).getInt(160);

		MALLET_DAMAGE = CONFIG.get("Stone Anvil Setting", "Max Damage Of Stone Mallet", 160).getInt(160);

		STONE_ANVIL_CRAFTING_STRIKES = CONFIG.getInt("Strikes Needed To Craft on The Stone Avil", "Stone Anvil Setting", 4, 1, Integer.MAX_VALUE, "");

		CHARCOAL_BURN_TIME = CONFIG.getInt("Chance that fire will consume this block. 300 being a 100% chance, 0, being a 0% chance", "Charcoal", 0, 0, 300, "");

		FLUID_BLADDER_PLACES_FLUID = CONFIG.get("Fluid Bladder", "Can Place Fluids in World", true).getBoolean(true);

		FLUID_BLADDER_LAVA_PICKUP = CONFIG.get("Fluid Bladder", "Can Pick Up Lava", false).getBoolean(false);

		WOODEN_BASIN_STIRS = CONFIG.get("Wooden Basin Setting", "How Many Stirs Before Result", 3).getInt(3);

		DROP_BONE_SHARD = CONFIG.get("Clubs and Bone Shards", "Boneshards drop when Mobs are killed with Clubs.", true).getBoolean(true);

		BONE_SHARD_RATE = CONFIG.getInt("Chance Boneshards drop when killed with Clubs. Higher numbers = less chance", "Clubs and Bone Shards", 3, 1, Integer.MAX_VALUE, "");

		if (CONFIG.hasChanged())
			CONFIG.save();
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals("primal_tech"))
			syncConfigs();
	}
}