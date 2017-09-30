package primal_tech.jei.wooden_basin;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import mezz.jei.api.gui.IGuiIngredient;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

public class WoodenBasinRecipeWrapper extends BlankRecipeWrapper 
{
	private final List<ItemStack> input;
	private final List<ItemStack> output;
	private final Fluid fluid;
	@Nullable
	private Map<Integer, ? extends IGuiIngredient<ItemStack>> currentIngredients = null;

	public WoodenBasinRecipeWrapper(Fluid fluid, List<ItemStack> inputs, List<ItemStack> outputs) {
		this.input = inputs;
		this.output = outputs;
		this.fluid = fluid;
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)  {
		
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputs(ItemStack.class, getInput());
		ingredients.setOutputs(ItemStack.class, getOutput());
	}

	public void setCurrentIngredients(Map<Integer, ? extends IGuiIngredient<ItemStack>> currentIngredients) {
		this.currentIngredients = currentIngredients;
	}

	public List<ItemStack> getInput() {
		return input;
	}

	public List<ItemStack> getOutput() {
		return output;
	}
	
	public Fluid getFluid() {
		return fluid;
	}
}
