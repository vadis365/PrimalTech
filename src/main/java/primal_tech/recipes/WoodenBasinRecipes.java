package primal_tech.recipes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class WoodenBasinRecipes {

	private static final List<WoodenBasinRecipes> recipes = new ArrayList<WoodenBasinRecipes>();

	public static void addRecipe(ItemStack output, Object... input) {
		recipes.add(new WoodenBasinRecipes(output, input));
	}

	public static ItemStack getOutput(ItemStack... input) {
		WoodenBasinRecipes recipe = getRecipe(input);
		return recipe != null ? recipe.getOutput() : ItemStack.EMPTY;
	}

	public static WoodenBasinRecipes getRecipe(ItemStack... input) {
		for (WoodenBasinRecipes recipe : recipes)
			if (recipe.matches(input))
				return recipe;
		return null;
	}

	public static List<WoodenBasinRecipes> getRecipeList() {
		return Collections.unmodifiableList(recipes);
	}

	private final ItemStack output;
	private final Object[] input;

	private WoodenBasinRecipes(ItemStack output, Object... input) {
		this.output = output.copy();
		this.input = new Object[input.length];

		if (input.length > 4)
			throw new IllegalArgumentException("Input must be 1 to 4.");

		for (int c = 0; c < input.length; c++)
			if (input[c] instanceof ItemStack)
				this.input[c] = ((ItemStack) input[c]).copy();
			else if (input[c] instanceof String)
				this.input[c] = OreDictionary.getOres((String) input[c]);
			else
				throw new IllegalArgumentException("Input must be an ItemStack or an OreDictionary name");
	}

	public Object[] getInputs() {
		return input;
	}

	public ItemStack getOutput() {
		return output.copy();
	}

	public boolean matches(ItemStack... stacks) {
		label: for (Object input : this.input) {
			for (int i = 0; i < stacks.length; i++)
				if (!stacks[i].isEmpty())
					if (areStacksTheSame(input, stacks[i])) {
						stacks[i] = ItemStack.EMPTY;
						continue label;
					}

			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean areStacksTheSame(Object obj, ItemStack target) {
		if (obj instanceof ItemStack)
			return areStacksTheSame((ItemStack) obj, target, false);
		else if (obj instanceof List) {
			List<ItemStack> list = (List<ItemStack>) obj;

			for (ItemStack stack : list)
				if (areStacksTheSame(stack, target, false))
					return true;
		}

		return false;
	}

	public static boolean areStacksTheSame(ItemStack stack1, ItemStack stack2, boolean matchSize) {
		if (stack1.isEmpty() || stack2.isEmpty())
			return false;

		if (stack1.getItem() == stack2.getItem())
			if (stack1.getItemDamage() == stack2.getItemDamage() || isWildcard(stack1.getItemDamage()) || isWildcard(stack2.getItemDamage()))
				if (!matchSize || stack1.getCount() == stack2.getCount()) {
					if (stack1.hasTagCompound() && stack2.hasTagCompound())
						return stack1.getTagCompound().equals(stack2.getTagCompound());
					return stack1.hasTagCompound() == stack2.hasTagCompound();
				}
		return false;
	}

	private static boolean isWildcard(int meta) {
		return meta == OreDictionary.WILDCARD_VALUE;
	}
}