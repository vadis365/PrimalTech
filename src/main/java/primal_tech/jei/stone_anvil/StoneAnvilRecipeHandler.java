package primal_tech.jei.stone_anvil;

import javax.annotation.Nonnull;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class StoneAnvilRecipeHandler implements IRecipeHandler<StoneAnvilRecipeWrapper> {

    public StoneAnvilRecipeHandler() {}

    @Nonnull
    @Override
    public Class<StoneAnvilRecipeWrapper> getRecipeClass() {
        return StoneAnvilRecipeWrapper.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid(@Nonnull StoneAnvilRecipeWrapper recipe) {
        return StoneAnvilCategory.UID;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(StoneAnvilRecipeWrapper recipe) {
		return recipe;
    }

	@Override
	public boolean isRecipeValid(StoneAnvilRecipeWrapper recipe) {
		return recipe.getInput().size() > 0 && recipe.getOutput().size() > 0;
	}
}