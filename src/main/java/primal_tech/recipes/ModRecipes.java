package primal_tech.recipes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockStoneSlab;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;
import primal_tech.ModBlocks;
import primal_tech.ModItems;
import primal_tech.configs.ConfigHandler;

public class ModRecipes {
	public static final ItemStack NBT_FIRE_STICKS = new ItemStack(ModItems.FIRE_STICKS);
	public static IRecipe CLAY_KILN, BONE_KNIFE, STICK_BUNDLE, FIBRE_TORCH, CHARCOAL, ROCK, WOOD_CLUB, STONE_CLUB, BONE_CLUB,
	PLANT_FIBRES, TWINE, FIRE_STICKS, BONE_SWORD, BONE_AXE, BONE_PICKAXE, BONE_SHOVEL, BONE_SHEARS, WOODEN_HOPPER, CHARCOAL_HOPPER,
	FLUID_BLADDER, FLINT_SAW_BLADE, STONE_MALLET, WORK_STUMP, WORK_STUMP_II, STONE_GRILL, WATER_SAW, STONE_ANVIL, WOODEN_BASIN, LEAF_BED;

	public static void init() {
		makeNBTStuffForMePlease();

		CLAY_KILN = new ShapedOreRecipe(getResource("recipe_clay_kiln"), new ItemStack(ModBlocks.CLAY_KILN_ITEM), "CCC", "C C", "CCC", 'C', new ItemStack(Items.CLAY_BALL));
		CLAY_KILN.setRegistryName(getResource("recipe_clay_kiln"));

		// Bone tools
		BONE_PICKAXE = new ShapedOreRecipe(getResource("recipe_bone_pickaxe"), new ItemStack(ModItems.BONE_PICKAXE, 1), "XXX", " # ", " # ", '#', "stickWood", 'X', "flakeBone");
		BONE_PICKAXE.setRegistryName(getResource("recipe_bone_pickaxe"));

		BONE_SHOVEL = new ShapedOreRecipe(getResource("recipe_bone_shovel"), new ItemStack(ModItems.BONE_SHOVEL, 1), "X", "#", "#", '#', "stickWood", 'X', "flakeBone");
		BONE_SHOVEL.setRegistryName(getResource("recipe_bone_shovel"));

		BONE_AXE = new ShapedOreRecipe(getResource("recipe_bone_axe"), new ItemStack(ModItems.BONE_AXE, 1), "XX", "X#", " #", '#', "stickWood", 'X', "flakeBone");
		BONE_AXE.setRegistryName(getResource("recipe_bone_axe"));

		BONE_SWORD = new ShapedOreRecipe(getResource("recipe_bone_sword"), new ItemStack(ModItems.BONE_SWORD, 1), "X", "X", "#", '#', "stickWood", 'X', "flakeBone");
		BONE_SWORD.setRegistryName(getResource("recipe_bone_sword"));

		BONE_SHEARS = new ShapedOreRecipe(getResource("recipe_bone_shears"), new ItemStack(ModItems.BONE_SHEARS, 1), " #", "# ", '#', "flakeBone");
		BONE_SHEARS.setRegistryName(getResource("recipe_bone_shears"));

		BONE_KNIFE = new ShapedOreRecipe(getResource("recipe_bone_knife"), new ItemStack(ModItems.BONE_KNIFE), " B", "S ", 'B', "flakeBone", 'S', "stickWood");
		BONE_KNIFE.setRegistryName(getResource("recipe_bone_knife"));

		STICK_BUNDLE = new ShapedOreRecipe(getResource("recipe_stick_bundle"), new ItemStack(ModBlocks.STICK_BUNDLE_ITEM), "STS", "SSS", "STS", 'T', "string", 'S', "stickWood");
		STICK_BUNDLE.setRegistryName(getResource("recipe_stick_bundle"));

		FIBRE_TORCH = new ShapedOreRecipe(getResource("recipe_fibre_torch"), new ItemStack(ModBlocks.FIBRE_TORCH_ITEM, 4, 0), "F", "S", 'F', new ItemStack(ModItems.PLANT_FIBRES, 1, 0), 'S', "stickWood");
		FIBRE_TORCH.setRegistryName(getResource("recipe_fibre_torch"));

		CHARCOAL = new ShapelessOreRecipe(getResource("recipe_charcoal"), new ItemStack(Items.COAL, 4, 1), new ItemStack(ModBlocks.CHARCOAL_BLOCK_ITEM));
		CHARCOAL.setRegistryName(getResource("recipe_charcoal"));

		ROCK = new ShapelessOreRecipe(getResource("recipe_rock"), new ItemStack(ModItems.ROCK, 4, 0), "cobblestone");
		ROCK.setRegistryName(getResource("recipe_rock")); 

		WOOD_CLUB = new ShapedOreRecipe(getResource("recipe_wood_club"), new ItemStack(ModItems.WOOD_CLUB), "L", "S", 'L', "logWood", 'S', "stickWood");
		WOOD_CLUB.setRegistryName(getResource("recipe_wood_club"));

		STONE_CLUB = new ShapedOreRecipe(getResource("recipe_stone_club"), new ItemStack(ModItems.STONE_CLUB), "C", "S", 'C', "cobblestone", 'S', "stickWood");
		STONE_CLUB.setRegistryName(getResource("recipe_stone_club"));

		BONE_CLUB = new ShapedOreRecipe(getResource("recipe_bone_club"), new ItemStack(ModItems.BONE_CLUB), "B", "S", 'B', new ItemStack(Blocks.BONE_BLOCK), 'S', "stickWood");
		BONE_CLUB.setRegistryName(getResource("recipe_bone_club"));

		PLANT_FIBRES = new ShapelessOreRecipe(getResource("recipe_plant_fibres"), new ItemStack(ModItems.PLANT_FIBRES), new ItemStack(Blocks.TALLGRASS, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(ModItems.BONE_KNIFE, 1, OreDictionary.WILDCARD_VALUE));
		PLANT_FIBRES.setRegistryName(getResource("recipe_plant_fibres"));

		TWINE = new ShapelessOreRecipe(getResource("recipe_twine"), new ItemStack(ModItems.TWINE, 3, 0), new ItemStack(ModItems.PLANT_FIBRES), new ItemStack(ModItems.PLANT_FIBRES), new ItemStack(ModItems.PLANT_FIBRES));
		TWINE.setRegistryName(getResource("recipe_twine"));

		FIRE_STICKS = new ShapelessOreRecipe(getResource("recipe_fire_sticks"), NBT_FIRE_STICKS, "stickWood", new ItemStack(ModItems.BONE_KNIFE, 1, OreDictionary.WILDCARD_VALUE));
		FIRE_STICKS.setRegistryName(getResource("recipe_fire_sticks"));

		WOODEN_HOPPER = new ShapedOreRecipe(getResource("recipe_wooden_hopper"), new ItemStack(ModBlocks.WOODEN_HOPPER_ITEM), "I I", "ICI", " I ", 'I', "plankWood", 'C', new ItemStack(Blocks.TRAPDOOR));
		WOODEN_HOPPER.setRegistryName(getResource("recipe_wooden_hopper"));

		CHARCOAL_HOPPER = new ShapedOreRecipe(getResource("recipe_charcoal_hopper"), new ItemStack(ModBlocks.CHARCOAL_HOPPER_ITEM), "I I", "ICI", " I ", 'I', new ItemStack(ModBlocks.CHARCOAL_BLOCK_ITEM), 'C', new ItemStack(Blocks.TRAPDOOR));
		CHARCOAL_HOPPER.setRegistryName(getResource("recipe_charcoal_hopper"));

		FLUID_BLADDER = new ShapedOreRecipe(getResource("recipe_fluid_bladder"), new ItemStack(ModItems.FLUID_BLADDER), "TL ", "L L", " L ", 'L', new ItemStack(Items.LEATHER), 'T', "string");
		FLUID_BLADDER.setRegistryName(getResource("recipe_fluid_bladder"));

		FLINT_SAW_BLADE = new ShapedOreRecipe(getResource("recipe_flint_saw_blade"), new ItemStack(ModItems.FLINT_SAW_BLADE), "F F", " B ", "F F", 'F', new ItemStack(Items.FLINT), 'B', new ItemStack(ModBlocks.FLINT_BLOCK_ITEM));
		FLINT_SAW_BLADE.setRegistryName(getResource("recipe_flint_saw_blade"));

		STONE_MALLET = new ShapedOreRecipe(getResource("recipe_stone_mallet"), new ItemStack(ModItems.STONE_MALLET), " BS", " SB", "S  ", 'B', "stone", 'S', "stickWood");
		STONE_MALLET.setRegistryName(getResource("recipe_stone_mallet"));

		WORK_STUMP = new ShapedOreRecipe(getResource("recipe_work_stump"), new ItemStack(ModBlocks.WORK_STUMP_ITEM), "S", "L", 'S', "slabWood", 'L', "logWood");
		WORK_STUMP.setRegistryName(getResource("recipe_work_stump"));

		WORK_STUMP_II = new ShapedOreRecipe(getResource("recipe_work_stump_2"), new ItemStack(ModBlocks.WORK_STUMP_II_ITEM), "PPP", "PWP", "PPP", 'P', "plankWood", 'W', new ItemStack(ModBlocks.WORK_STUMP_ITEM));
		WORK_STUMP_II.setRegistryName(getResource("recipe_work_stump_2"));

		STONE_GRILL = new ShapedOreRecipe(getResource("recipe_stone_grill"), new ItemStack(ModBlocks.STONE_GRILL_ITEM), "SSS", "C C", " C ", 'S', new ItemStack(Blocks.STONE_SLAB, 1, BlockStoneSlab.EnumType.COBBLESTONE.ordinal()), 'C', "cobblestone");
		STONE_GRILL.setRegistryName(getResource("recipe_stone_grill"));

		STONE_ANVIL = new ShapedOreRecipe(getResource("recipe_stone_anvil"), new ItemStack(ModBlocks.STONE_ANVIL_ITEM), "H", "S", 'H', new ItemStack(Blocks.STONE_SLAB, 1, BlockStoneSlab.EnumType.STONE.ordinal()), 'S', "stone");
		STONE_ANVIL.setRegistryName(getResource("recipe_stone_anvil"));

		WATER_SAW = new ShapedOreRecipe(getResource("recipe_water_saw"), new ItemStack(ModBlocks.WATER_SAW_ITEM), "PBP", "SLS", "PLP", 'P', new ItemStack(Blocks.WOODEN_PRESSURE_PLATE), 'B', new ItemStack(ModItems.FLINT_SAW_BLADE), 'S', "stickWood", 'L', "logWood");
		WATER_SAW.setRegistryName(getResource("recipe_water_saw"));

		WOODEN_BASIN = new ShapedOreRecipe(getResource("recipe_wooden_basin"), new ItemStack(ModBlocks.WOODEN_BASIN_ITEM), "PSP", "PPP", "B B", 'P', "plankWood", 'S', "stickWood", 'B', "stone");
		WOODEN_BASIN.setRegistryName(getResource("recipe_wooden_basin"));

		LEAF_BED = new ShapedOreRecipe(getResource("recipe_leaf_bed"), new ItemStack(ModItems.LEAF_BED_ITEM), "LLS", "HHH", 'L', "treeLeaves", 'S', "slabWood", 'H', new ItemStack(Blocks.HAY_BLOCK));
		LEAF_BED.setRegistryName(getResource("recipe_leaf_bed"));
	}

	private static void makeNBTStuffForMePlease() {
		NBT_FIRE_STICKS.setTagCompound(new NBTTagCompound());
		NBT_FIRE_STICKS.getTagCompound().setInteger("rubbingCount", 0);
		NBT_FIRE_STICKS.getTagCompound().setBoolean("animate", false);
	}

	@Mod.EventBusSubscriber(modid = "primal_tech")
	public static class RegistrationHandlerRecipes {
		@SubscribeEvent
		public static void registerRecipes(final RegistryEvent.Register<IRecipe> event) {
			init();
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
				FIRE_STICKS,
				BONE_SWORD,
				BONE_AXE,
				BONE_PICKAXE,
				BONE_SHOVEL,
				BONE_SHEARS,
				WOODEN_HOPPER,
				CHARCOAL_HOPPER,
				FLUID_BLADDER,
				FLINT_SAW_BLADE,
				STONE_MALLET,
				WORK_STUMP,
				WORK_STUMP_II,
				STONE_GRILL,
				WATER_SAW,
				STONE_ANVIL,
				WOODEN_BASIN,
				LEAF_BED
				);
		}
	}
	
	@Mod.EventBusSubscriber(modid = "primal_tech")
	public static class RegistrationHandler {
		@SubscribeEvent(priority = EventPriority.LOWEST)
		public static void registerOreDictEntries(final RegistryEvent.Register<Item> event) {
			OreDictionary.registerOre("string", ModItems.TWINE);
			OreDictionary.registerOre("flakeBone", ModItems.BONE_SHARD);
		}
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

			String[] finalEntry = outputItemList.get(0).trim().split(",");
			if (finalEntry.length != 2)
				throw new IllegalArgumentException("Illegal entry found when reading Primal Tech config file: " + outputItemList.get(0));
			finalOutPutItem.add(finalEntry[0]);
			finalOutPutItem.add(finalEntry[1]);

			String[] finalEntry2 = inputItemList.get(0).trim().split(",");
			if (finalEntry2.length != 2)
				throw new IllegalArgumentException("Illegal entry found when reading Primal Tech config file: " + inputItemList.get(0));
			finalInPutItem.add(finalEntry2[0]);
			finalInPutItem.add(finalEntry2[1]);

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

			String[] finalEntry = outputItemList.get(0).trim().split(",");
			if (finalEntry.length != 3)
				throw new IllegalArgumentException("Illegal entry found when reading Primal Tech config file: " + outputItemList.get(0));
			finalOutPutItem.add(finalEntry[0]);
			finalOutPutItem.add(finalEntry[1]);
			finalOutPutItem.add(finalEntry[2]);

			String[] finalEntry2 = inputItemList.get(0).trim().split(",");
			if (finalEntry2.length != 2)
				throw new IllegalArgumentException("Illegal entry found when reading Primal Tech config file: " + inputItemList.get(0));
			finalInPutItem.add(finalEntry2[0]);
			finalInPutItem.add(finalEntry2[1]);

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

			String[] finalEntry = outputItemList.get(0).trim().split(",");
			if (finalEntry.length != 3)
				throw new IllegalArgumentException("Illegal entry found when reading Primal Tech config file: " + outputItemList.get(0));
			finalOutPutItem.add(finalEntry[0]);
			finalOutPutItem.add(finalEntry[1]);
			finalOutPutItem.add(finalEntry[2]);

			String[] finalEntry2 = inputItemList.get(0).trim().split(",");
			if (finalEntry2.length != 2)
				throw new IllegalArgumentException("Illegal entry found when reading Primal Tech config file: " + inputItemList.get(0));
			finalInPutItem.add(finalEntry2[0]);
			finalInPutItem.add(finalEntry2[1]);

		ItemStack outStack = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation(finalOutPutItem.get(0))), Integer.valueOf(finalOutPutItem.get(2)), Integer.valueOf(finalOutPutItem.get(1)));
		ItemStack inStack = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation(finalInPutItem.get(0))), 1, Integer.valueOf(finalInPutItem.get(1)));

		if(!outStack.isEmpty() && !inStack.isEmpty())
			StoneAnvilRecipes.addRecipe(outStack, inStack);
		}
	}
	
	public static void addWoodenBasinRecipes() {
		//I mean it's also an inefficient mess but it works O.o
		for (int items = 0; items < ConfigHandler.WOODEN_BASIN_RECIPES.length; items++) {
			List<String> outputItemList = new ArrayList<String>();
			List<String> inputItemList = new ArrayList<String>();
			String[] entry = ConfigHandler.WOODEN_BASIN_RECIPES[items].trim().split("#");
			if (entry.length != 3)
				throw new IllegalArgumentException("Illegal entry found when reading Primal Tech config file: " + ConfigHandler.WOODEN_BASIN_RECIPES[items]);
			outputItemList.add(entry[0]);

			Fluid inputFluid;
			if (FluidRegistry.isFluidRegistered(entry[1]))
				inputFluid = FluidRegistry.getFluid(entry[1]);
			else
				throw new IllegalArgumentException("Illegal Fluid entry found when reading Primal Tech config file: " + ConfigHandler.WOODEN_BASIN_RECIPES[items]);
			inputItemList.add(entry[2]);

			String[] finalOutPutEntry = outputItemList.get(0).trim().split(",");
			if (finalOutPutEntry.length != 3)
				throw new IllegalArgumentException("Illegal entry found when reading Primal Tech config file: " + outputItemList.get(0));
			ItemStack outStack = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation(finalOutPutEntry[0])), Integer.valueOf(finalOutPutEntry[2]), Integer.valueOf(finalOutPutEntry[1]));

			String[] finalInputEntry = inputItemList.get(0).trim().split(",");
			if (finalInputEntry.length < 2)
				throw new IllegalArgumentException("Illegal entry found when reading Primal Tech config file: " + inputItemList.get(0));

			ItemStack inStack1 = ItemStack.EMPTY;
			ItemStack inStack2 = ItemStack.EMPTY;
			ItemStack inStack3 = ItemStack.EMPTY;
			ItemStack inStack4 = ItemStack.EMPTY;

			if (finalInputEntry.length > 0)
				inStack1 = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation(finalInputEntry[0])), 1, Integer.valueOf(finalInputEntry[1]));
			if (finalInputEntry.length > 2)
				inStack2 = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation(finalInputEntry[2])), 1, Integer.valueOf(finalInputEntry[3]));
			if (finalInputEntry.length > 4)
				inStack3 = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation(finalInputEntry[4])), 1, Integer.valueOf(finalInputEntry[5]));
			if (finalInputEntry.length > 6)
				inStack4 = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation(finalInputEntry[6])), 1, Integer.valueOf(finalInputEntry[7]));

		if(!outStack.isEmpty() && !inStack1.isEmpty())
			WoodenBasinRecipes.addRecipe(outStack, inputFluid, inStack1, inStack2, inStack3, inStack4);

		}
	}

	private static ResourceLocation getResource(String inName) {
		return new ResourceLocation("primal_tech", inName);
	}

}