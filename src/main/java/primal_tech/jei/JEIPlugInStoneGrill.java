package primal_tech.jei;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import primal_tech.PrimalTech;
import primal_tech.jei.stone_grill.GrillCategory;
import primal_tech.jei.stone_grill.GrillRecipeHandler;
import primal_tech.jei.stone_grill.GrillRecipeWrapper;

@JEIPlugin
public class JEIPlugInStoneGrill implements IModPlugin {
	@Override
	public void register(IModRegistry registry) {
		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		IIngredientRegistry ingredientRegistry = registry.getIngredientRegistry();
		registry.addRecipeCategories(new GrillCategory(guiHelper));
		registry.addRecipeHandlers(new GrillRecipeHandler());
		registry.addRecipeCategoryCraftingItem(new ItemStack(PrimalTech.STONE_GRILL), GrillCategory.UID);
		
		List<GrillRecipeWrapper> recipes = new ArrayList<GrillRecipeWrapper>();
		recipes = getFurnaceRecipes(recipes, jeiHelpers);
		registry.addRecipes(recipes);
	}

	public List<GrillRecipeWrapper> getFurnaceRecipes(List<GrillRecipeWrapper> recipes, IJeiHelpers helpers) {
		IStackHelper stackHelper = helpers.getStackHelper();
		FurnaceRecipes furnaceRecipes = FurnaceRecipes.instance();
		Map<ItemStack, ItemStack> smeltingMap = furnaceRecipes.getSmeltingList();
		for (Map.Entry<ItemStack, ItemStack> entry : smeltingMap.entrySet()) {
			List<ItemStack> output = new ArrayList();
			ItemStack inStack = entry.getKey();
			ItemStack outStack = entry.getValue();
			ItemStack outStack2 = new ItemStack(PrimalTech.STONE_GRILL);
			List<ItemStack> inputs = stackHelper.getSubtypes(inStack);
			output.add(outStack);
			output.add(outStack2);
			recipes.add(new GrillRecipeWrapper(inputs, output));
		}
		return recipes;
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime arg0) {

	}

	@Override
	public void registerIngredients(IModIngredientRegistration arg0) {

	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistry arg0) {

	}
}