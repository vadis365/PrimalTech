package primal_tech.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.oredict.IOreDictEntry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import primal_tech.recipes.WoodenBasinRecipes;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.primaltech.WoodenBasin")
public class WoodenBasinCraftTweaker {
    @ZenMethod
    public static void addRecipe(IItemStack output, ILiquidStack inputFluid, IIngredient[] ingredients) {
        ItemStack actualOutput = CraftTweakerMC.getItemStack(output);
        FluidStack fluidStack = CraftTweakerMC.getLiquidStack(inputFluid);

        if (actualOutput.isEmpty()) {
            CraftTweakerAPI.logError("Output was Empty for Wooden Basin Recipe.");
        }

        Object[] inputs = new Object[ingredients.length];
        for (int i = 0; i < ingredients.length; i++) {
            IIngredient ingredient = ingredients[i];
            if (ingredient instanceof IItemStack) {
                inputs[i] = CraftTweakerMC.getItemStack(ingredient);
            } else if (ingredient instanceof IOreDictEntry) {
                inputs[i] = ((IOreDictEntry) ingredient).getName();
            }
        }
        for (int i = 0; i < ingredients.length; i++) {

        }

        WoodenBasinRecipes.addRecipe(actualOutput, fluidStack, inputs);
    }
}
