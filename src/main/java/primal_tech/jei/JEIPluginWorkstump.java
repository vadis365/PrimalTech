package primal_tech.jei;

import mezz.jei.api.*;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.plugins.vanilla.crafting.CraftingRecipeChecker;
import mezz.jei.plugins.vanilla.crafting.ShapedOreRecipeWrapper;
import mezz.jei.plugins.vanilla.crafting.ShapedRecipesWrapper;
import mezz.jei.plugins.vanilla.crafting.ShapelessRecipeWrapper;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import primal_tech.ModBlocks;
import primal_tech.jei.workstump.WorkStumpCategory;
import primal_tech.jei.workstump.WorkStumpIICategory;

@JEIPlugin
public class JEIPluginWorkstump
    implements IModPlugin {

  @Override
  public void registerCategories(IRecipeCategoryRegistration registry) {

    IJeiHelpers jeiHelpers = registry.getJeiHelpers();
    IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

    registry.addRecipeCategories(
        new WorkStumpCategory(guiHelper),
        new WorkStumpIICategory(guiHelper)
    );
  }

  @Override
  public void register(IModRegistry registry) {

    IJeiHelpers jeiHelpers = registry.getJeiHelpers();

    this.create(registry, jeiHelpers, WorkStumpCategory.UID);
    registry.addRecipeCatalyst(new ItemStack(ModBlocks.WORK_STUMP), WorkStumpCategory.UID);

    this.create(registry, jeiHelpers, WorkStumpIICategory.UID);
    registry.addRecipeCatalyst(new ItemStack(ModBlocks.WORK_STUMP_II), WorkStumpIICategory.UID);
  }

  private void create(IModRegistry registry, IJeiHelpers jeiHelpers, String uid) {

    registry.addRecipes(CraftingRecipeChecker.getValidRecipes(jeiHelpers), uid);

    registry.handleRecipes(ShapedOreRecipe.class, recipe -> new ShapedOreRecipeWrapper(jeiHelpers, recipe), uid);
    registry.handleRecipes(ShapedRecipes.class, recipe -> new ShapedRecipesWrapper(jeiHelpers, recipe), uid);
    registry.handleRecipes(ShapelessOreRecipe.class, recipe -> new ShapelessRecipeWrapper<>(jeiHelpers, recipe), uid);
    registry.handleRecipes(ShapelessRecipes.class, recipe -> new ShapelessRecipeWrapper<>(jeiHelpers, recipe), uid);

    registry.addRecipeClickArea(GuiCrafting.class, 88, 32, 28, 23, uid);
  }
}
