package primal_tech.recipes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;
import primal_tech.jei.clay_kiln.KilnRecipeWrapper;

public class ClayKilnRecipes {

	private static final List<ClayKilnRecipes> recipes = new ArrayList<ClayKilnRecipes>();

	/**
	 *
	 * @param output
	 *            what will be produced by the recipe
	 * @param input
	 *            the input item for the recipe
	 * @param itemCookTime
	 *            time it takes to cook the item
	 */
	public static void addRecipe(ItemStack output, ItemStack input, int itemCookTime) {
		recipes.add(new ClayKilnRecipes(output, input, itemCookTime));
	}

	public static ItemStack getOutput(ItemStack input) {
		for (ClayKilnRecipes recipe : recipes)
			if (recipe.matches(input))
				return recipe.getOutput();

		return input;
	}

	public static int getCooktime(ItemStack input) {
		for (ClayKilnRecipes recipe : recipes)
			if (recipe.matches(input))
				return recipe.getCookTime();
		return 10;
	}

	public static List<ClayKilnRecipes> getRecipeList() {
		return Collections.unmodifiableList(recipes);
	}

	private final ItemStack output;
	private final ItemStack input;
	private final int cookTime;

	private ClayKilnRecipes(ItemStack output, ItemStack input, int itemCookTime) {
		this.output = output.copy();
		this.input = input.copy();
		this.cookTime = itemCookTime;

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

	private int getCookTime() {
		return cookTime;
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
