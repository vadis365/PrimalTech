package primal_tech.jei.stone_grill;

import javax.annotation.Nonnull;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class GrillRecipeHandler implements IRecipeHandler<GrillRecipeWrapper> {

    public GrillRecipeHandler() {}

    @Nonnull
    @Override
    public Class<GrillRecipeWrapper> getRecipeClass() {
        return GrillRecipeWrapper.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid(@Nonnull GrillRecipeWrapper recipe) {
        return GrillCategory.UID;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(GrillRecipeWrapper recipe) {
		return recipe;
    }

	@Override
	public boolean isRecipeValid(GrillRecipeWrapper recipe) {
		return recipe.getInput().size() > 0 && recipe.getOutput().size() > 0;
	}
}