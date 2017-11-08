package primal_tech;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import primal_tech.configs.ConfigHandler;
import primal_tech.items.ItemBoneAxe;
import primal_tech.items.ItemBonePickaxe;
import primal_tech.items.ItemBoneShears;
import primal_tech.items.ItemClub;
import primal_tech.items.ItemFireSticks;
import primal_tech.items.ItemFluidBladder;
import primal_tech.items.ItemLeafBed;
import primal_tech.items.ItemMaterials;

public class ModItems {
	public static Item FIRE_STICKS, BONE_PICKAXE, BONE_AXE, BONE_SHOVEL, BONE_SHEARS, FLUID_BLADDER, BONE_SHARD,
	PLANT_FIBRES, TWINE, ROCK, WOOD_CLUB, BONE_CLUB, STONE_CLUB, FLINT_SAW_BLADE, STONE_MALLET, LEAF_BED_ITEM;
	public static ItemSword BONE_SWORD, BONE_KNIFE;
	public static void init() {

		// Items
		FIRE_STICKS = new ItemFireSticks();
		FIRE_STICKS.setRegistryName("primal_tech", "fire_sticks").setUnlocalizedName("primal_tech.fire_sticks");

		BONE_PICKAXE = new ItemBonePickaxe(PrimalTech.TOOL_BONE);
		BONE_PICKAXE.setRegistryName("primal_tech", "bone_pickaxe").setUnlocalizedName("primal_tech.bone_pickaxe");

		BONE_AXE = new ItemBoneAxe(PrimalTech.TOOL_BONE);
		BONE_AXE.setRegistryName("primal_tech", "bone_axe").setUnlocalizedName("primal_tech.bone_axe");

		BONE_SHOVEL = new ItemSpade(PrimalTech.TOOL_BONE).setCreativeTab(PrimalTech.TAB);
		BONE_SHOVEL.setRegistryName("primal_tech", "bone_shovel").setUnlocalizedName("primal_tech.bone_shovel");

		BONE_SWORD = new ItemSword(PrimalTech.TOOL_BONE) {
			@Override
			public boolean getIsRepairable(ItemStack stack, ItemStack material) {
				return material.getItem() == Items.BONE;
			}
		};
		BONE_SWORD.setCreativeTab(PrimalTech.TAB);
		BONE_SWORD.setRegistryName("primal_tech", "bone_sword").setUnlocalizedName("primal_tech.bone_sword");

		BONE_KNIFE = new ItemSword(PrimalTech.TOOL_BONE_KNIFE) {
			@Override
			public boolean isRepairable() {
				return false;
			}

			@Override
			public boolean hasContainerItem() {
				return true;
			}

			@Override
			public ItemStack getContainerItem(ItemStack itemStack) {
				ItemStack returnItem = new ItemStack(itemStack.getItem(), 1, itemStack.getItemDamage() + 1);
				if (itemStack.isItemEnchanted()) {
					NBTTagCompound nbtcompound = itemStack.getTagCompound();
					returnItem.setTagCompound(nbtcompound);
				}
				return returnItem;
			}
		};
		BONE_KNIFE.setCreativeTab(PrimalTech.TAB);
		BONE_KNIFE.setRegistryName("primal_tech", "bone_knife").setUnlocalizedName("primal_tech.bone_knife");

		BONE_SHARD = new ItemMaterials().setCreativeTab(PrimalTech.TAB);
		BONE_SHARD.setRegistryName("primal_tech", "bone_shard").setUnlocalizedName("primal_tech.bone_shard");

		FLINT_SAW_BLADE = new ItemMaterials().setCreativeTab(PrimalTech.TAB);
		FLINT_SAW_BLADE.setRegistryName("primal_tech", "flint_edged_disc").setUnlocalizedName("primal_tech.flint_edged_disc");

		PLANT_FIBRES = new ItemMaterials().setCreativeTab(PrimalTech.TAB);
		PLANT_FIBRES.setRegistryName("primal_tech", "plant_fibres").setUnlocalizedName("primal_tech.plant_fibres");

		TWINE = new ItemMaterials().setCreativeTab(PrimalTech.TAB);
		TWINE.setRegistryName("primal_tech", "twine").setUnlocalizedName("primal_tech.twine");

		BONE_SHEARS = new ItemBoneShears();
		BONE_SHEARS.setRegistryName("primal_tech", "bone_shears").setUnlocalizedName("primal_tech.bone_shears");

		ROCK = new Item().setMaxDamage(ConfigHandler.ROCK_DAMAGE).setMaxStackSize(1);
		ROCK.setRegistryName("primal_tech", "rock").setUnlocalizedName("primal_tech.rock");
		ROCK.setCreativeTab(PrimalTech.TAB);

		WOOD_CLUB = new ItemClub(ToolMaterial.WOOD, Items.STICK).setCreativeTab(PrimalTech.TAB);
		WOOD_CLUB.setRegistryName("primal_tech", "wood_club").setUnlocalizedName("primal_tech.wood_club");

		STONE_CLUB = new ItemClub(ToolMaterial.STONE, Items.FLINT).setCreativeTab(PrimalTech.TAB);
		STONE_CLUB.setRegistryName("primal_tech", "stone_club").setUnlocalizedName("primal_tech.stone_club");

		BONE_CLUB = new ItemClub(PrimalTech.TOOL_BONE, ModItems.BONE_SHARD).setCreativeTab(PrimalTech.TAB);
		BONE_CLUB.setRegistryName("primal_tech", "bone_club").setUnlocalizedName("primal_tech.bone_club");

		FLUID_BLADDER = new ItemFluidBladder(Fluid.BUCKET_VOLUME).setCreativeTab(PrimalTech.TAB);
		FLUID_BLADDER.setRegistryName("primal_tech", "fluid_bladder").setUnlocalizedName("primal_tech.fluid_bladder");
		
		LEAF_BED_ITEM = new ItemLeafBed();
		LEAF_BED_ITEM.setRegistryName("primal_tech", "leaf_bed").setUnlocalizedName("primal_tech.leaf_bed");
		
		STONE_MALLET = new Item() {
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flagIn) {
				list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.mallet_1").getFormattedText());
			}
		};
		STONE_MALLET.setMaxDamage(ConfigHandler.MALLET_DAMAGE).setMaxStackSize(1).setCreativeTab(PrimalTech.TAB);
		STONE_MALLET.setRegistryName("primal_tech", "stone_mallet").setUnlocalizedName("primal_tech.stone_mallet");

	}
	
	@Mod.EventBusSubscriber(modid = "primal_tech")
	public static class RegistrationHandlerItems {
		public static final List<Item> ITEMS = new ArrayList<Item>();

		@SubscribeEvent
		public static void registerItems(final RegistryEvent.Register<Item> event) {
			final Item[] items = {
					FIRE_STICKS,
					BONE_PICKAXE,
					BONE_AXE,
					BONE_SHOVEL,
					BONE_SHEARS,
					FLUID_BLADDER,
					BONE_SHARD,
					PLANT_FIBRES,
					TWINE, ROCK,
					WOOD_CLUB,
					BONE_CLUB,
					STONE_CLUB,
					FLINT_SAW_BLADE,
					BONE_SWORD,
					BONE_KNIFE,
					STONE_MALLET,
					LEAF_BED_ITEM
			};
			final IForgeRegistry<Item> registry = event.getRegistry();

			for (final Item item : items) {
				registry.register(item);
				ITEMS.add(item);
			}
		}
	}

}
