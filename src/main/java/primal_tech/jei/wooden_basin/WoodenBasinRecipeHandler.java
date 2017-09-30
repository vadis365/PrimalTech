package primal_tech.jei.wooden_basin;

import javax.annotation.Nonnull;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class WoodenBasinRecipeHandler implements IRecipeHandler<WoodenBasinRecipeWrapper> {

    public WoodenBasinRecipeHandler() {}

    @Nonnull
    @Override
    public Class<WoodenBasinRecipeWrapper> getRecipeClass() {
        return WoodenBasinRecipeWrapper.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid(@Nonnull WoodenBasinRecipeWrapper recipe) {
        return WoodenBasinCategory.UID;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(WoodenBasinRecipeWrapper recipe) {
		return recipe;
    }

	@Override
	public boolean isRecipeValid(WoodenBasinRecipeWrapper recipe) {
		return recipe.getInput().size() > 0 && recipe.getOutput().size() > 0;
	}
}