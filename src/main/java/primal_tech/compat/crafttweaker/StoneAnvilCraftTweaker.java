package primal_tech.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import primal_tech.recipes.StoneAnvilRecipes;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;

@ZenRegister
@ZenClass("mods.primaltech.StoneAnvil")
public class StoneAnvilCraftTweaker {
    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient input) {
        ItemStack actualOutput = CraftTweakerMC.getItemStack(output);
        List<IItemStack> allInputs = input.getItems();

        if (actualOutput.isEmpty()) {
            CraftTweakerAPI.logError("Output was Empty for Stone Anvil Recipe.");
        }
        for (IItemStack itemStack : allInputs) {
            ItemStack properItemStack = CraftTweakerMC.getItemStack(itemStack);
            if (!properItemStack.isEmpty()) {
                StoneAnvilRecipes.addRecipe(actualOutput, properItemStack);
            }
        }
    }
}
