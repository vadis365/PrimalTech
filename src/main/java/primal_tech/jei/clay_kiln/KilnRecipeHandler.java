package primal_tech.jei.clay_kiln;

import javax.annotation.Nonnull;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class KilnRecipeHandler implements IRecipeHandler<KilnRecipeWrapper> {

    public KilnRecipeHandler() {}

    @Nonnull
    @Override
    public Class<KilnRecipeWrapper> getRecipeClass() {
        return KilnRecipeWrapper.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid(@Nonnull KilnRecipeWrapper recipe) {
        return KilnCategory.UID;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(KilnRecipeWrapper recipe) {
		return recipe;
    }

	@Override
	public boolean isRecipeValid(KilnRecipeWrapper recipe) {
		return recipe.getInput().size() > 0 && recipe.getOutput().size() > 0;
	}
}