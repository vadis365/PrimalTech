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
import primal_tech.jei.stone_anvil.StoneAnvilCategory;
import primal_tech.jei.stone_anvil.StoneAnvilRecipeHandler;
import primal_tech.jei.stone_anvil.StoneAnvilRecipeWrapper;
import primal_tech.recipes.StoneAnvilRecipes;

@JEIPlugin
public class JEIPlugInStoneAnvil implements IModPlugin {
	@Override
	public void register(IModRegistry registry) {
		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		IIngredientRegistry ingredientRegistry = registry.getIngredientRegistry();
		registry.addRecipeCategories(new StoneAnvilCategory(guiHelper));
		registry.addRecipeHandlers(new StoneAnvilRecipeHandler());
		registry.addRecipeCategoryCraftingItem(new ItemStack(PrimalTech.STONE_ANVIL), StoneAnvilCategory.UID);
		
		List<StoneAnvilRecipeWrapper> recipes = new ArrayList<StoneAnvilRecipeWrapper>();
		recipes = getTestRecipe(recipes);
		registry.addRecipes(recipes);
	}
	
	private List<StoneAnvilRecipeWrapper> getTestRecipe(List<StoneAnvilRecipeWrapper> recipes) {
		for (StoneAnvilRecipes recipe : StoneAnvilRecipes.getRecipeList()) {
			List<ItemStack> output = new ArrayList();
			ItemStack inStack = recipe.getInput();
			ItemStack outStack = recipe.getOutput(inStack);
			ItemStack outStack2 = new ItemStack(PrimalTech.STONE_ANVIL);
			output.add(outStack);
			output.add(outStack2);
			recipes.add(new StoneAnvilRecipeWrapper(inStack, output));
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