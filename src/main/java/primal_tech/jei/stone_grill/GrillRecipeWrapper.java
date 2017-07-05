package primal_tech.jei.stone_grill;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import mezz.jei.api.gui.IGuiIngredient;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class GrillRecipeWrapper extends BlankRecipeWrapper 
{
	private final List<List<ItemStack>> input;
	private final List<ItemStack> output;
	@Nullable
	private Map<Integer, ? extends IGuiIngredient<ItemStack>> currentIngredients = null;
	
	public GrillRecipeWrapper(List<ItemStack> inputs, List<ItemStack> outputs)  {
		this.input = Collections.singletonList(inputs);

		this.output = outputs;
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)  {
		
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputLists(ItemStack.class, input);
		ingredients.setOutputs(ItemStack.class, output);
	}

	public void setCurrentIngredients(Map<Integer, ? extends IGuiIngredient<ItemStack>> currentIngredients) {
		this.currentIngredients = currentIngredients;
	}

	public List getInput() {
		return input;
	}

	public List<ItemStack> getOutput() {
		return output;
}
}
