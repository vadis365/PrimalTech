package primal_tech.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import primal_tech.recipes.ClayKilnRecipes;
import primal_tech.recipes.StoneAnvilRecipes;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;

@ZenRegister
@ZenClass("mods.primaltech.ClayKiln")
public class ClayKilnCraftTweaker {
    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient input, int cookTime) {
        ItemStack actualOutput = CraftTweakerMC.getItemStack(output);
        List<IItemStack> allInputs = input.getItems();

        if (actualOutput.isEmpty()) {
            CraftTweakerAPI.logError("Output was Empty for Clay Kiln Recipe.");
        }
        for (IItemStack itemStack : allInputs) {
            ItemStack properItemStack = CraftTweakerMC.getItemStack(itemStack);
            if (!properItemStack.isEmpty()) {
                ClayKilnRecipes.addRecipe(actualOutput, properItemStack, cookTime);
            }
        }
    }
}
