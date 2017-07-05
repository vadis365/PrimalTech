package primal_tech.jei.stone_grill;

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
import primal_tech.PrimalTech;

public class GrillCategory<T extends IRecipeWrapper> extends BlankRecipeCategory<GrillRecipeWrapper> {

	public static final String UID = "primal_tech.stone_grill_recipes";
	private final ResourceLocation backgroundLocation;
	private final IDrawableStatic background;
	private final IDrawableAnimated arrow;
	private final IDrawableAnimated flame;
	
	public GrillCategory(IGuiHelper guiHelper) {
		backgroundLocation =  new ResourceLocation("primal_tech","textures/gui/clay_kiln_jei_gui.png");

		background = guiHelper.createDrawable(backgroundLocation, 0, 0, 100, 100);

		IDrawableStatic flameDrawable = guiHelper.createDrawable(backgroundLocation, 101, 0, 14, 14);
		flame = guiHelper.createAnimatedDrawable(flameDrawable, 20, IDrawableAnimated.StartDirection.BOTTOM, false);

		IDrawableStatic arrowDrawable = guiHelper.createDrawable(backgroundLocation, 100, 14, 16, 14);
		arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);

	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void drawExtras(Minecraft minecraft) {
		arrow.draw(minecraft, 60, 25);
		flame.draw(minecraft, 44, 44);
	}

	@Override
	public String getUid() {
		return UID;
	}

	@Override
	public String getTitle() {
		return PrimalTech.STONE_GRILL.getLocalizedName();
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, GrillRecipeWrapper recipeWrapper, IIngredients ingredients) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		guiItemStacks.init(0, true, 5, 23);
		guiItemStacks.init(1, false, 77, 23);
		guiItemStacks.init(2, false, 41, 23);

		guiItemStacks.set(ingredients);
		recipeWrapper.setCurrentIngredients(guiItemStacks.getGuiIngredients());
	}
}
