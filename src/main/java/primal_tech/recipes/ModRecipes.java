package primal_tech.recipes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import primal_tech.PrimalTech;
import primal_tech.configs.ConfigHandler;

public class ModRecipes {

	public static void addRecipes() {

		 OreDictionary.registerOre("string", PrimalTech.TWINE);
		 OreDictionary.registerOre("flakeBone", PrimalTech.BONE_SHARD);

		 addShapedRecipe(new ItemStack(PrimalTech.CLAY_KILN), "CCC", "C C", "CCC", 'C', new ItemStack(Items.CLAY_BALL));
		 addShapedRecipe(new ItemStack(PrimalTech.BONE_KNIFE), " B", "S ", 'B', new ItemStack(PrimalTech.BONE_SHARD), 'S', "stickWood");
		 addShapedRecipe(new ItemStack(PrimalTech.STICK_BUNDLE), "STS", "SSS", "STS", 'T', new ItemStack(PrimalTech.TWINE), 'S', "stickWood");
		 addShapedRecipe(new ItemStack(PrimalTech.FIBRE_TORCH, 4, 0), "F", "S", 'F', new ItemStack(PrimalTech.PLANT_FIBRES, 1, 0), 'S', "stickWood");
		 addShapelessRecipe(new ItemStack(Items.COAL, 4, 1), PrimalTech.CHARCOAL_BLOCK);
		 addShapelessRecipe(new ItemStack(PrimalTech.ROCK, 4, 0), "cobblestone");
		 
		 addShapedRecipe(new ItemStack(PrimalTech.WOOD_CLUB), "L", "S", 'L', "logWood", 'S', "stickWood");
		 addShapedRecipe(new ItemStack(PrimalTech.STONE_CLUB), "C", "S", 'C', "cobblestone", 'S', "stickWood");
		 addShapedRecipe(new ItemStack(PrimalTech.BONE_CLUB), "B", "S", 'B', new ItemStack(Blocks.BONE_BLOCK), 'S', "stickWood");

		 addShapelessRecipe(new ItemStack(PrimalTech.PLANT_FIBRES), new ItemStack(Blocks.TALLGRASS, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(PrimalTech.BONE_KNIFE, 1, OreDictionary.WILDCARD_VALUE));
		 addShapelessRecipe(new ItemStack(PrimalTech.TWINE, 3, 0), new ItemStack(PrimalTech.PLANT_FIBRES), new ItemStack(PrimalTech.PLANT_FIBRES), new ItemStack(PrimalTech.PLANT_FIBRES));
		 addShapelessRecipe(new ItemStack(PrimalTech.FIRE_STICKS), "stickWood", new ItemStack(PrimalTech.BONE_KNIFE, 1, OreDictionary.WILDCARD_VALUE));
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

	public static void addStoneAnvilRecipes() {
		//I mean it's also an inefficient mess but it works O.o
		for (int items = 0; items < ConfigHandler.STONE_ANVIL_RECIPES.length; items++) {
			List<String> outputItemList = new ArrayList<String>();
			List<String> inputItemList = new ArrayList<String>();
			List<String> finalOutPutItem = new ArrayList<String>();
			List<String> finalInPutItem = new ArrayList<String>();
			String[] entry = ConfigHandler.STONE_ANVIL_RECIPES[items].trim().split("#");
			if (entry.length != 2)
				throw new IllegalArgumentException("Illegal entry found when reading Primal Tech config file: " + ConfigHandler.STONE_ANVIL_RECIPES[items]);
			outputItemList.add(entry[0]);
			inputItemList.add(entry[1]);

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
			StoneAnvilRecipes.addRecipe(outStack, inStack);
		}
	}

	private static void addShapelessRecipe(ItemStack output, Object... parameters) {
		GameRegistry.addRecipe(new ShapelessOreRecipe(output, parameters));
	}

	private static void addShapedRecipe(ItemStack output, Object... parameters) {
		GameRegistry.addRecipe(new ShapedOreRecipe(output, parameters));
	}

}