package primal_tech.recipes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;
import primal_tech.ModBlocks;
import primal_tech.ModItems;
import primal_tech.configs.ConfigHandler;

public class ModRecipes {
	public static IRecipe CLAY_KILN, BONE_KNIFE, STICK_BUNDLE, FIBRE_TORCH, CHARCOAL, ROCK, WOOD_CLUB, STONE_CLUB, BONE_CLUB,
	PLANT_FIBRES, TWINE, FIRE_STICKS;

	public static void init() {
		CLAY_KILN = new ShapedOreRecipe(getResource("recipe_clay_kiln"), new ItemStack(ModBlocks.CLAY_KILN_ITEM), "CCC", "C C", "CCC", 'C', new ItemStack(Items.CLAY_BALL));
		CLAY_KILN.setRegistryName(getResource("primal_tech:recipe_clay_kiln"));

		BONE_KNIFE = new ShapedOreRecipe(getResource("recipe_bone_knife"), new ItemStack(ModItems.BONE_KNIFE), " B", "S ", 'B', new ItemStack(ModItems.BONE_SHARD), 'S', "stickWood");
		BONE_KNIFE.setRegistryName(getResource("primal_tech:recipe_bone_knife"));

		STICK_BUNDLE = new ShapedOreRecipe(getResource("recipe_stick_bundle"), new ItemStack(ModBlocks.STICK_BUNDLE_ITEM), "STS", "SSS", "STS", 'T', new ItemStack(ModItems.TWINE), 'S', "stickWood");
		STICK_BUNDLE.setRegistryName(getResource("primal_tech:recipe_stick_bundle"));

		FIBRE_TORCH = new ShapedOreRecipe(getResource("recipe_fibre_torch"), new ItemStack(ModBlocks.FIBRE_TORCH_ITEM, 4, 0), "F", "S", 'F', new ItemStack(ModItems.PLANT_FIBRES, 1, 0), 'S', "stickWood");
		FIBRE_TORCH.setRegistryName(getResource("primal_tech:recipe_fibre_torch"));

		CHARCOAL = new ShapelessOreRecipe(getResource("recipe_charcoal"), new ItemStack(Items.COAL, 4, 1), new ItemStack(ModBlocks.CHARCOAL_BLOCK_ITEM));
		CHARCOAL.setRegistryName(getResource("primal_tech:recipe_charcoal"));

		ROCK = new ShapelessOreRecipe(getResource("recipe_rock"), new ItemStack(ModItems.ROCK, 4, 0), "cobblestone");
		ROCK.setRegistryName(getResource("primal_tech:recipe_rock")); 

		WOOD_CLUB = new ShapedOreRecipe(getResource("recipe_wood_club"), new ItemStack(ModItems.WOOD_CLUB), "L", "S", 'L', "logWood", 'S', "stickWood");
		WOOD_CLUB.setRegistryName(getResource("primal_tech:recipe_wood_club"));

		STONE_CLUB = new ShapedOreRecipe(getResource("recipe_stone_club"), new ItemStack(ModItems.STONE_CLUB), "C", "S", 'C', "cobblestone", 'S', "stickWood");
		STONE_CLUB.setRegistryName(getResource("primal_tech:recipe_stone_club"));

		BONE_CLUB = new ShapedOreRecipe(getResource("recipe_bone_club"), new ItemStack(ModItems.BONE_CLUB), "B", "S", 'B', new ItemStack(Blocks.BONE_BLOCK), 'S', "stickWood");
		BONE_CLUB.setRegistryName(getResource("primal_tech:recipe_bone_club"));

		PLANT_FIBRES = new ShapelessOreRecipe(getResource("recipe_plant_fibres"), new ItemStack(ModItems.PLANT_FIBRES), new ItemStack(Blocks.TALLGRASS, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(ModItems.BONE_KNIFE, 1, OreDictionary.WILDCARD_VALUE));
		PLANT_FIBRES.setRegistryName(getResource("primal_tech:recipe_plant_fibres"));

		TWINE = new ShapelessOreRecipe(getResource("recipe_twine"), new ItemStack(ModItems.TWINE, 3, 0), new ItemStack(ModItems.PLANT_FIBRES), new ItemStack(ModItems.PLANT_FIBRES), new ItemStack(ModItems.PLANT_FIBRES));
		TWINE.setRegistryName(getResource("primal_tech:recipe_twine"));

		FIRE_STICKS = new ShapelessOreRecipe(getResource("recipe_fire_sticks"), new ItemStack(ModItems.FIRE_STICKS), "stickWood", new ItemStack(ModItems.BONE_KNIFE, 1, OreDictionary.WILDCARD_VALUE));
		FIRE_STICKS.setRegistryName(getResource("primal_tech:recipe_fire_sticks"));
	}

	@Mod.EventBusSubscriber(modid = "primal_tech")
	public static class RegistrationHandlerRecipes {
		@SubscribeEvent
		public static void registerRecipes(final RegistryEvent.Register<IRecipe> event) {
		final IForgeRegistry<IRecipe> registry = event.getRegistry();
		event.getRegistry().registerAll(
				CLAY_KILN,
				BONE_KNIFE,
				STICK_BUNDLE,
				FIBRE_TORCH,
				CHARCOAL,
				ROCK,
				WOOD_CLUB,
				STONE_CLUB,
				BONE_CLUB,
				PLANT_FIBRES,
				TWINE,
				FIRE_STICKS
				);
		}
	}

	public static void addRecipes() {
		// OreDictionary.registerOre("string", ModItems.TWINE);
		// OreDictionary.registerOre("flakeBone", ModItems.BONE_SHARD);
	}

	public static void addKilnRecipes() {
		//I mean it's an inefficient mess but it works O.o
		for (int items = 0; items < ConfigHandler.CLAY_KILN_RECIPES.length; items++) {
			List<String> outputItemList = new ArrayList<String>();
			List<String> inputItemList = new ArrayList<String>();
			List<String> finalOutPutItem = new ArrayList<String>();
			List<String> finalInPutItem = new ArrayList<String>();
			String[] entry = ConfigHandler.CLAY_KILN_RECIPES[items].trim().split("#");
			if (entry.length != 3)
				throw new IllegalArgumentException("Illegal entry found when reading Primal Tech config file: " + ConfigHandler.CLAY_KILN_RECIPES[items]);
			outputItemList.add(entry[0]);
			inputItemList.add(entry[1]);
			int itemCookTime = Integer.valueOf(entry[2]);

		for (int finalItem = 0; finalItem < 2; finalItem++) {
			String[] finalEntry = outputItemList.get(0).trim().split(",");
			if (finalEntry.length != 2)
				throw new IllegalArgumentException("Illegal entry found when reading Primal Tech config file: " + outputItemList.get(0));
			finalOutPutItem.add(finalEntry[0]);
			finalOutPutItem.add(finalEntry[1]);
		}

		for (int finalItem = 0; finalItem < 2; finalItem++) {
			String[] finalEntry = inputItemList.get(0).trim().split(",");
			if (finalEntry.length != 2)
				throw new IllegalArgumentException("Illegal entry found when reading Primal Tech config file: " + inputItemList.get(0));
			finalInPutItem.add(finalEntry[0]);
			finalInPutItem.add(finalEntry[1]);
		}

		ItemStack outStack = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation(finalOutPutItem.get(0))), 1, Integer.valueOf(finalOutPutItem.get(1)));
		ItemStack inStack = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation(finalInPutItem.get(0))), 1, Integer.valueOf(finalInPutItem.get(1)));

		if(!outStack.isEmpty() && !inStack.isEmpty())
			ClayKilnRecipes.addRecipe(outStack, inStack, itemCookTime);
		}
	}

	public static void addWaterSawRecipes() {
		//I mean it's also an inefficient mess but it works O.o
		for (int items = 0; items < ConfigHandler.WATER_SAW_RECIPES.length; items++) {
			List<String> outputItemList = new ArrayList<String>();
			List<String> inputItemList = new ArrayList<String>();
			List<String> finalOutPutItem = new ArrayList<String>();
			List<String> finalInPutItem = new ArrayList<String>();
			String[] entry = ConfigHandler.WATER_SAW_RECIPES[items].trim().split("#");
			if (entry.length != 3)
				throw new IllegalArgumentException("Illegal entry found when reading Primal Tech config file: " + ConfigHandler.WATER_SAW_RECIPES[items]);
			outputItemList.add(entry[0]);
			inputItemList.add(entry[1]);
			int choppingTime = Integer.valueOf(entry[2]);

		for (int finalItem = 0; finalItem < 3; finalItem++) {
			String[] finalEntry = outputItemList.get(0).trim().split(",");
			if (finalEntry.length != 3)
				throw new IllegalArgumentException("Illegal entry found when reading Primal Tech config file: " + outputItemList.get(0));
			finalOutPutItem.add(finalEntry[0]);
			finalOutPutItem.add(finalEntry[1]);
			finalOutPutItem.add(finalEntry[2]);
		}

		for (int finalItem = 0; finalItem < 2; finalItem++) {
			String[] finalEntry = inputItemList.get(0).trim().split(",");
			if (finalEntry.length != 2)
				throw new IllegalArgumentException("Illegal entry found when reading Primal Tech config file: " + inputItemList.get(0));
			finalInPutItem.add(finalEntry[0]);
			finalInPutItem.add(finalEntry[1]);
		}

		ItemStack outStack = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation(finalOutPutItem.get(0))), Integer.valueOf(finalOutPutItem.get(2)), Integer.valueOf(finalOutPutItem.get(1)));
		ItemStack inStack = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation(finalInPutItem.get(0))), 1, Integer.valueOf(finalInPutItem.get(1)));

		if(!outStack.isEmpty() && !inStack.isEmpty())
			WaterSawRecipes.addRecipe(outStack, inStack, choppingTime);
		}
	}

	private static ResourceLocation getResource(String inName) {
		return new ResourceLocation(inName);
	}

}