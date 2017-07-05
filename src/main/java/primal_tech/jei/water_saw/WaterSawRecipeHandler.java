package primal_tech.jei.water_saw;

import javax.annotation.Nonnull;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class WaterSawRecipeHandler implements IRecipeHandler<WaterSawRecipeWrapper> {

    public WaterSawRecipeHandler() {}

    @Nonnull
    @Override
    public Class<WaterSawRecipeWrapper> getRecipeClass() {
        return WaterSawRecipeWrapper.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid(@Nonnull WaterSawRecipeWrapper recipe) {
        return WaterSawCategory.UID;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(WaterSawRecipeWrapper recipe) {
		return recipe;
    }

	@Override
	public boolean isRecipeValid(WaterSawRecipeWrapper recipe) {
		return recipe.getInput().size() > 0 && recipe.getOutput().size() > 0;
	}
}