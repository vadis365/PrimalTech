package primal_tech.jei;

import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import net.minecraft.item.ItemStack;
import primal_tech.ModBlocks;
import primal_tech.jei.water_saw.WaterSawCategory;
import primal_tech.jei.water_saw.WaterSawRecipeHandler;
import primal_tech.jei.water_saw.WaterSawRecipeWrapper;
import primal_tech.recipes.WaterSawRecipes;

@JEIPlugin
public class JEIPlugInWaterSaw implements IModPlugin {
	@Override
	public void register(IModRegistry registry) {
		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		IIngredientRegistry ingredientRegistry = registry.getIngredientRegistry();
		registry.addRecipeCategories(new WaterSawCategory(guiHelper));
		registry.addRecipeHandlers(new WaterSawRecipeHandler());
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.WATER_SAW), WaterSawCategory.UID);
		
		List<WaterSawRecipeWrapper> recipes = new ArrayList<WaterSawRecipeWrapper>();
		recipes = getTestRecipe(recipes);
		registry.addRecipes(recipes);
	}
	
	private List<WaterSawRecipeWrapper> getTestRecipe(List<WaterSawRecipeWrapper> recipes) {
		for (WaterSawRecipes recipe : WaterSawRecipes.getRecipeList()) {
			List<ItemStack> output = new ArrayList();
			ItemStack inStack = recipe.getInput();
			ItemStack outStack = recipe.getOutput(inStack);
			ItemStack outStack2 = new ItemStack(ModBlocks.WATER_SAW);
			output.add(outStack);
			output.add(outStack2);
			recipes.add(new WaterSawRecipeWrapper(inStack, output));
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