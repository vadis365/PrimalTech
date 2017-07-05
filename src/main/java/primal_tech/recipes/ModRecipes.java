package primal_tech.recipes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import primal_tech.ModBlocks;
import primal_tech.ModItems;
import primal_tech.configs.ConfigHandler;

public class ModRecipes {

	public static void addRecipes() {

		// OreDictionary.registerOre("string", ModItems.TWINE);
		// OreDictionary.registerOre("flakeBone", ModItems.BONE_SHARD);

		 addShapedRecipe(getResource("primal_tech:clay_kiln"), getResource("recipe_clay_kiln"), new ItemStack(ModBlocks.CLAY_KILN), "CCC", "C C", "CCC", 'C', new ItemStack(Items.CLAY_BALL));
		 addShapedRecipe(getResource("primal_tech:bone_knife"), getResource("recipe_bone_knife"), new ItemStack(ModItems.BONE_KNIFE), " B", "S ", 'B', new ItemStack(ModItems.BONE_SHARD), 'S', "stickWood");
		 addShapedRecipe(getResource("primal_tech:stick_bundle"), getResource("recipe_stick_bundle"), new ItemStack(ModBlocks.STICK_BUNDLE), "STS", "SSS", "STS", 'T', new ItemStack(ModItems.TWINE), 'S', "stickWood");
		 addShapedRecipe(getResource("primal_tech:fibre_torch"), getResource("recipe_fibre_torch"), new ItemStack(ModBlocks.FIBRE_TORCH, 4, 0), "F", "S", 'F', new ItemStack(ModItems.PLANT_FIBRES, 1, 0), 'S', "stickWood");
		 addShapelessRecipe(getResource("primal_tech:charcoal"), getResource("recipe_charcoal"), new ItemStack(Items.COAL, 4, 1), Ingredient.fromStacks(new ItemStack(ModBlocks.CHARCOAL_BLOCK)));
		 addShapelessRecipe(getResource("primal_tech:rock"), getResource("recipe_rock"), new ItemStack(ModItems.ROCK, 4, 0), Ingredient.fromStacks(new ItemStack(Blocks.COBBLESTONE)));
		 
		 addShapedRecipe(getResource("primal_tech:wood_club"), getResource("recipe_wood_club"), new ItemStack(ModItems.WOOD_CLUB), "L", "S", 'L', "logWood", 'S', "stickWood");
		 addShapedRecipe(getResource("primal_tech:stone_club"), getResource("recipe_stone_club"), new ItemStack(ModItems.STONE_CLUB), "C", "S", 'C', "cobblestone", 'S', "stickWood");
		 addShapedRecipe(getResource("primal_tech:bone_club"), getResource("recipe_bone_club"), new ItemStack(ModItems.BONE_CLUB), "B", "S", 'B', new ItemStack(Blocks.BONE_BLOCK), 'S', "stickWood");

		 addShapelessRecipe(getResource("primal_tech:plant_fibres"), getResource("recipe_plant_fibres"), new ItemStack(ModItems.PLANT_FIBRES), Ingredient.fromStacks(new ItemStack(Blocks.TALLGRASS, 1, OreDictionary.WILDCARD_VALUE)), Ingredient.fromStacks(new ItemStack(ModItems.BONE_KNIFE, 1, OreDictionary.WILDCARD_VALUE)));
		 addShapelessRecipe(getResource("primal_tech:twine"), getResource("recipe_twine"), new ItemStack(ModItems.TWINE, 3, 0), Ingredient.fromStacks(new ItemStack(ModItems.PLANT_FIBRES)), Ingredient.fromStacks(new ItemStack(ModItems.PLANT_FIBRES)), Ingredient.fromStacks(new ItemStack(ModItems.PLANT_FIBRES)));
		 addShapelessRecipe(getResource("primal_tech:fire_sticks"), getResource("recipe_fire_sticks"), new ItemStack(ModItems.FIRE_STICKS), Ingredient.fromStacks(new ItemStack(Items.STICK)), Ingredient.fromStacks(new ItemStack(ModItems.BONE_KNIFE, 1, OreDictionary.WILDCARD_VALUE)));
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

	private static void addShapelessRecipe(ResourceLocation name, ResourceLocation group, ItemStack output, Ingredient... params) {
		GameRegistry.addShapelessRecipe(name, group, output, params);
	}

	private static void addShapedRecipe(ResourceLocation name, ResourceLocation group, ItemStack output, Object... params) {
		GameRegistry.addShapedRecipe(name, group, output, params);
	}

	private static ResourceLocation getResource(String inName) {
		return new ResourceLocation(inName);
	}

}