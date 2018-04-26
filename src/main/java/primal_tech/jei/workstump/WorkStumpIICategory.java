package primal_tech.jei.workstump;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.plugins.vanilla.crafting.CraftingRecipeCategory;
import mezz.jei.util.Translator;
import net.minecraft.item.ItemStack;
import primal_tech.ModBlocks;

public class WorkStumpIICategory
    extends CraftingRecipeCategory {

  public static final String UID = "primal_tech.workstump.upgraded";

  private final String localizedName;
  private final IDrawable icon;

  public WorkStumpIICategory(IGuiHelper guiHelper) {

    super(guiHelper);
    this.localizedName = Translator.translateToLocal("tile.primal_tech.work_stump_upgraded.name");
    this.icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.WORK_STUMP_II));
  }

  @Override
  public String getModName() {

    return "Primal Tech";
  }

  @Override
  public String getTitle() {

    return this.localizedName;
  }

  @Override
  public String getUid() {

    return UID;
  }

  @Override
  public IDrawable getIcon() {

    return this.icon;
  }
}
