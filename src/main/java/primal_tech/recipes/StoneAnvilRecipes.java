package primal_tech.recipes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;

public class StoneAnvilRecipes {

	private static final List<StoneAnvilRecipes> recipes = new ArrayList<StoneAnvilRecipes>();

	/**
	 *
	 * @param output
	 *            what will be produced by the recipe
	 * @param input
	 *            the input item for the recipe
	 */
	public static void addRecipe(ItemStack output, ItemStack input) {
		recipes.add(new StoneAnvilRecipes(output, input));
	}

	public static ItemStack getOutput(ItemStack input) {
		for (StoneAnvilRecipes recipe : recipes)
			if (recipe.matches(input))
				return recipe.getOutput();

		return input;
	}


	public static List<StoneAnvilRecipes> getRecipeList() {
		return Collections.unmodifiableList(recipes);
	}

	private final ItemStack output;
	private final ItemStack input;

	private StoneAnvilRecipes(ItemStack output, ItemStack input) {
		this.output = output.copy();
		this.input = input.copy();

			if (input instanceof ItemStack)
				input = input.copy();

			else
				throw new IllegalArgumentException("Input must be an ItemStack");
	}

	public ItemStack getInput() {
		return input.copy();
	}

	public ItemStack getOutput() {
		return output.copy();
	}

	public boolean matches(ItemStack stacks) {
		if (!stacks.isEmpty())
			if (areStacksTheSame(getInput(), stacks)) {
				return true;
			}
		return false;
	}

	@SuppressWarnings("unchecked")
	private boolean areStacksTheSame(ItemStack stack, ItemStack target) {
		return areStacksTheSame(stack, target, false);
	}

	public static boolean areStacksTheSame(ItemStack stack1, ItemStack stack2, boolean matchSize) {
		if (stack1.isEmpty() || stack2.isEmpty())
			return false;

		if (stack1.getItem() == stack2.getItem())
			if (stack1.getItemDamage() == stack2.getItemDamage())
				if (!matchSize || stack1.getCount() == stack2.getCount()) {
					if (stack1.hasTagCompound() && stack2.hasTagCompound())
						return stack1.getTagCompound().equals(stack2.getTagCompound());
					return stack1.hasTagCompound() == stack2.hasTagCompound();
				}
		return false;
	}
}
