package primal_tech.jei.stone_anvil;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import mezz.jei.api.gui.IGuiIngredient;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class StoneAnvilRecipeWrapper extends BlankRecipeWrapper 
{
	private final List<ItemStack> input;
	private final List<ItemStack> output;
	@Nullable
	private Map<Integer, ? extends IGuiIngredient<ItemStack>> currentIngredients = null;
	
	public StoneAnvilRecipeWrapper(ItemStack inputs, List<ItemStack> outputs)  {
		this.input = Lists.newArrayList();
		this.input.add(inputs);

		this.output = outputs;
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

	public List getInput() {
		return input;
	}

	public List<ItemStack> getOutput() {
		return output;
}
}
