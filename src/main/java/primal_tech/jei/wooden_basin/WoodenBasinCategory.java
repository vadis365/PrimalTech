package primal_tech.jei.wooden_basin;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import primal_tech.ModBlocks;

public class WoodenBasinCategory<T extends IRecipeWrapper> extends BlankRecipeCategory<WoodenBasinRecipeWrapper> {

	public static final String UID = "primal_tech.wooden_basin_recipes";

	private final ResourceLocation backgroundLocation;
	private final IDrawableStatic background;
	private final IDrawableAnimated arrow;

	public WoodenBasinCategory(IGuiHelper guiHelper) {
		backgroundLocation =  new ResourceLocation("primal_tech","textures/gui/wooden_basin_jei_gui.png");
		background = guiHelper.createDrawable(backgroundLocation, 0, 0, 100, 100);
		IDrawableStatic arrowDrawable = guiHelper.createDrawable(backgroundLocation, 100, 14, 16, 14);
		arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);

	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void drawExtras(Minecraft minecraft) {
		arrow.draw(minecraft, 42, 78);
		
	}

	@Override
	public String getUid() {
		return UID;
	}

	@Override
	public String getTitle() {
		return ModBlocks.WOODEN_BASIN.getLocalizedName();
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, WoodenBasinRecipeWrapper recipeWrapper, IIngredients ingredients) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		guiItemStacks.init(0, true, 41, 30); //centre slot
		guiItemStacks.init(1, true, 41, 6); //north slot
		guiItemStacks.init(2, true, 17, 30); //west slot
		guiItemStacks.init(3, true, 65, 30); //east slot
		guiItemStacks.init(4, true, 41, 54); //south slot
		guiItemStacks.init(5, false, 76, 76); //output slot
		guiItemStacks.init(6, false, 17, 76); // block picture

		guiItemStacks.set(ingredients);
		recipeWrapper.setCurrentIngredients(guiItemStacks.getGuiIngredients());
	}

	@Override
	public String getModName() {
		return "primal_tech";
	}
}
