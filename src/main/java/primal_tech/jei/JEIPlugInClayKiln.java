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
import primal_tech.PrimalTech;
import primal_tech.jei.clay_kiln.KilnCategory;
import primal_tech.jei.clay_kiln.KilnRecipeHandler;
import primal_tech.jei.clay_kiln.KilnRecipeWrapper;
import primal_tech.recipes.ClayKilnRecipes;

@JEIPlugin
public class JEIPlugInClayKiln implements IModPlugin {
	@Override
	public void register(IModRegistry registry) {
		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		IIngredientRegistry ingredientRegistry = registry.getIngredientRegistry();
		registry.addRecipeCategories(new KilnCategory(guiHelper));
		registry.addRecipeHandlers(new KilnRecipeHandler());
		registry.addRecipeCategoryCraftingItem(new ItemStack(PrimalTech.CLAY_KILN), KilnCategory.UID);
		
		List<KilnRecipeWrapper> recipes = new ArrayList<KilnRecipeWrapper>();
		recipes = getTestRecipe(recipes);
		registry.addRecipes(recipes);
	}
	
	private List<KilnRecipeWrapper> getTestRecipe(List<KilnRecipeWrapper> recipes) {
		for (ClayKilnRecipes recipe : ClayKilnRecipes.getRecipeList()) {
			List<ItemStack> output = new ArrayList();
			ItemStack inStack = recipe.getInput();
			ItemStack outStack = recipe.getOutput(inStack);
			ItemStack outStack2 = new ItemStack(PrimalTech.CLAY_KILN);
			output.add(outStack);
			output.add(outStack2);
			recipes.add(new KilnRecipeWrapper(inStack, output));
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