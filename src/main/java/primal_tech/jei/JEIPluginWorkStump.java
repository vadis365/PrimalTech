package primal_tech.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.ItemStack;
import primal_tech.ModBlocks;

@JEIPlugin
public class JEIPluginWorkStump
    implements IModPlugin {

  @Override
  public void register(IModRegistry registry) {

    registry.addRecipeCatalyst(new ItemStack(ModBlocks.WORK_STUMP), VanillaRecipeCategoryUid.CRAFTING);
    registry.addRecipeCatalyst(new ItemStack(ModBlocks.WORK_STUMP_II), VanillaRecipeCategoryUid.CRAFTING);
  }
}
