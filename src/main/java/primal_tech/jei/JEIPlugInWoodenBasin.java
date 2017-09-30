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
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import primal_tech.ModBlocks;
import primal_tech.jei.wooden_basin.WoodenBasinCategory;
import primal_tech.jei.wooden_basin.WoodenBasinRecipeHandler;
import primal_tech.jei.wooden_basin.WoodenBasinRecipeWrapper;
import primal_tech.recipes.WoodenBasinRecipes;

@JEIPlugin
public class JEIPlugInWoodenBasin implements IModPlugin {
	@Override
	public void register(IModRegistry registry) {
		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		IIngredientRegistry ingredientRegistry = registry.getIngredientRegistry();
		registry.addRecipeCategories(new WoodenBasinCategory(guiHelper));
		registry.addRecipeHandlers(new WoodenBasinRecipeHandler());
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.WOODEN_BASIN), WoodenBasinCategory.UID);
		
		List<WoodenBasinRecipeWrapper> recipes = new ArrayList<WoodenBasinRecipeWrapper>();
		recipes = getTestRecipe(recipes);
		registry.addRecipes(recipes);
	}
	
	private List<WoodenBasinRecipeWrapper> getTestRecipe(List<WoodenBasinRecipeWrapper> recipes) {
		for (WoodenBasinRecipes recipe : WoodenBasinRecipes.getRecipeList()) {
			List<ItemStack> output = new ArrayList();
			List<ItemStack> input = new ArrayList();
			ItemStack inStack1 = (ItemStack) recipe.getInputs()[0];
			ItemStack inStack2 = (ItemStack) recipe.getInputs()[1];
			ItemStack inStack3 = (ItemStack) recipe.getInputs()[2];
			ItemStack inStack4 = (ItemStack) recipe.getInputs()[3];
			Fluid fluidIn = recipe.getFluidStack().getFluid();
			ItemStack fluidBucket = FluidUtil.getFilledBucket(recipe.getFluidStack());
			input.add(fluidBucket);
			input.add(inStack1);
			input.add(inStack2);
			input.add(inStack3);
			input.add(inStack4);
			ItemStack outStack = recipe.getOutput(new FluidTank(recipe.getFluidStack(), Fluid.BUCKET_VOLUME), inStack1, inStack2, inStack3, inStack4);
			ItemStack outStack2 = new ItemStack(ModBlocks.WOODEN_BASIN);
			output.add(outStack);
			output.add(outStack2);
			recipes.add(new WoodenBasinRecipeWrapper(fluidIn, input, output));
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